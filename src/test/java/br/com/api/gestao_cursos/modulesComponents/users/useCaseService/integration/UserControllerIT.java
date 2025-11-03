package br.com.api.gestao_cursos.modulesComponents.users.useCaseService.integration;

import br.com.api.gestao_cursos.modulesComponents.users.dto.CreateUserRequestDto;
import br.com.api.gestao_cursos.modulesComponents.users.entities.RoleUser;
import br.com.api.gestao_cursos.modulesComponents.users.entities.UserEntity;
import br.com.api.gestao_cursos.modulesComponents.users.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest//Carrega o contexto da aplicação
@AutoConfigureMockMvc//Configura o MockMvc para simular chamadas HTTP
@TestInstance(TestInstance.Lifecycle.PER_METHOD)//Permite usar o @BeforeAll não estático
public class UserControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;

    private String uriTest = "/users/create";

    @BeforeEach
    void setup(){
        userRepository.deleteAll();
    }

    @Test
    void shouldBeCreateUserSuccessfully() throws Exception {
        //dados de entrada
        CreateUserRequestDto requestDto = buildValidUserDtoForRequest();
        String jsonUser = objectMapper.writeValueAsString(requestDto);
        //Chamar o endpoint para test
        var result = mockMvc.perform(MockMvcRequestBuilders.post(uriTest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
        String resultBody = result.getResponse().getContentAsString();
        //Transformar a resposta JSON em um objeto navegável para validar os campos
        ObjectMapper objMapper = new ObjectMapper();
        JsonNode jsonNode = objMapper.readTree(resultBody);
        //confirmar se os dados retornados são como os enviados
        assertEquals("User Request Test", jsonNode.get("name").asText());
        assertEquals("test@test.com.br", jsonNode.get("email").asText());
        assertEquals("student", jsonNode.get("role").asText());
        //verificar se foi salvo no banco de dados
        Optional<UserEntity> userSaved = userRepository.findByEmail("test@test.com.br");
        assertTrue(userSaved.isPresent());
    }

    @Test
    void shouldThrowsExceptionWhenUserNameIsEmpty() throws Exception {
        //dados de entrada
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.builder()
                .name(null)
                .email("email@test.com")
                .password("passwordTest")
                .role(RoleUser.instructor)
                .build();
        //transformar num json string para passar no content
        String jsonContent = objectMapper.writeValueAsString(createUserRequestDto);
        mockMvc.perform(MockMvcRequestBuilders.post(uriTest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("[{\"message\":\"Nome do usuário é obrigatório\",\"field\":\"name\"}]"));

    }

    @Test
    void shouldThrowsExceptionWhenPassUserIsEmpty() throws Exception {
        //dados de entrada
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.builder()
                .name("user Name Test")
                .email("email@test.com")
                .password("")
                .role(RoleUser.instructor)
                .build();
        //transformar num json string para passar no content
        String jsonContent = objectMapper.writeValueAsString(createUserRequestDto);
        mockMvc.perform(MockMvcRequestBuilders.post(uriTest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("[{\"message\":\"Obrigatório o preenchimento do password do usuário\",\"field\":\"password\"}]"));

    }

    @Test
    void shouldThrowsExceptionWhenRoleUserIsNull() throws Exception {
        //dados de entrada
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.builder()
                .name("user Name Test")
                .email("email@test.com")
                .password("passwordTest")
                .role(null)
                .build();
        //transformar num json string para passar no content
        String jsonContent = objectMapper.writeValueAsString(createUserRequestDto);
        mockMvc.perform(MockMvcRequestBuilders.post(uriTest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("[{\"message\":\"Obrigatório preenhcer a Role do usuário\",\"field\":\"role\"}]"));

    }

    @Test
    void shouldBeThrowsExceptionWhenEmailIsInvalid() throws Exception {
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.builder()
                .name("User Request Test")
                .email("email-invalid")
                .role(RoleUser.student)
                .password("SenhaForte@123")
                .build();
        String jsonContent = objectMapper.writeValueAsString(createUserRequestDto);
        mockMvc.perform(MockMvcRequestBuilders.post(uriTest)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().string("Email inválido!"));
    }

    @Test
    void shouldBeThrowsExceptionWhenPasswordIsWeak() throws Exception {
         //entrada dos dados
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.builder()
                .name("user test")
                .email("email@email.com")
                .password("pass")
                .role(RoleUser.instructor)
                .build();
        //converter o objeto acima para passar no parâmetro content
        String jsonStringRequest = objectMapper.writeValueAsString(createUserRequestDto);
        //chamar o endpoint
        mockMvc.perform(MockMvcRequestBuilders.post(uriTest)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonStringRequest))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().string("Senha não atende aos requisitos de segurança!"));
    }

    @Test
    void shouldBeThrowsWhenUserEmailAlreadyExists() throws Exception {
         //Dados de entrada da requisição
        CreateUserRequestDto createUserRequestDto = buildValidUserDtoForRequest();

        UserEntity entitySaved = UserEntity.builder()
                .name(createUserRequestDto.getName())
                .email(createUserRequestDto.getEmail())
                .password(createUserRequestDto.getPassword())
                .role(createUserRequestDto.getRole())
                .build();
        //O mesmo objeto do request tem que ser igual ao que existe no banco
        userRepository.save(entitySaved);

        //Chamar o endpoint
        var jsonStringDto = objectMapper.writeValueAsBytes(createUserRequestDto);
        mockMvc.perform(MockMvcRequestBuilders.post(uriTest)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonStringDto))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().string("Email já cadastrado no sistema!"));

    }

    public static CreateUserRequestDto buildValidUserDtoForRequest(){
        return CreateUserRequestDto.builder()
                .name("User Request Test")
                .email("test@test.com.br")
                .role(RoleUser.student)
                .password("SenhaForte@123")
                .build();
    }

}
