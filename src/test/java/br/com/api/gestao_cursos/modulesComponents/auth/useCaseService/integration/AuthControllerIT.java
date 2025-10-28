package br.com.api.gestao_cursos.modulesComponents.auth.useCaseService.integration;

import br.com.api.gestao_cursos.modulesComponents.auth.dto.CreateAuthRequestDto;
import br.com.api.gestao_cursos.modulesComponents.users.entities.RoleUser;
import br.com.api.gestao_cursos.modulesComponents.users.entities.UserEntity;
import br.com.api.gestao_cursos.modulesComponents.users.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_METHOD)//Permite usar o @BeforeAll não estático
public class AuthControllerIT {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MockMvc mockMvc;

    private String uriTest = "/auth/student";

    @BeforeEach
    void setup(){
        userRepository.deleteAll();
    }

    @Test
    void shouldBeCreateAuthSuccessfully() throws Exception {
        //dados de entrada
        UserEntity userEntity = UserEntity.builder()
                .name("name test")
                .email("email@test.com")
                .password(passwordEncoder.encode("passwordTest"))
                .role(RoleUser.student)
                .build();
        userRepository.save(userEntity);

        CreateAuthRequestDto createAuthRequestDto = CreateAuthRequestDto.builder()
                .email("email@test.com")
                .password("passwordTest")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        var authObjectMapper = objectMapper.writeValueAsString(createAuthRequestDto);//converter para passar no .content

        //chamar o endpoint e esse acessar o service com os objetos criados acima
        var result = mockMvc.perform(MockMvcRequestBuilders.post(uriTest)
                .contentType(MediaType.APPLICATION_JSON)
                .content(authObjectMapper))
                .andReturn();

        //Checkar as respostas
        String resultBody = result.getResponse().getContentAsString();
        ObjectMapper objMapperNode = new ObjectMapper();
        JsonNode jsonNode = objMapperNode.readTree(resultBody);//transformar em um objeto navegável
        //validar
        String token = jsonNode.get("access_token").asText();
        String createdAt = jsonNode.get("created_at").asText();
        String expiresIn = jsonNode.get("expires_in").asText();

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertNotNull(createdAt);
        assertNotNull(expiresIn);

    }

    @Test
    void shouldBeThrowsWhenUserEmailNotExists() throws Exception {
        //Dados de entrada
        CreateAuthRequestDto createAuthRequestDto = CreateAuthRequestDto.builder()
                .email("email@naoencontrado.com")
                .password("passwordTest")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String objContentJson = objectMapper.writeValueAsString(createAuthRequestDto);
        //invocar o endpoint
        var result = mockMvc.perform(MockMvcRequestBuilders.post(uriTest)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objContentJson))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().string("Usuário não existe no sistema!"));

    }

    @Test
    void shouldBeThrowsWhenRoleUserIsNotContainsPrivilege() throws Exception {
        //Given
        UserEntity userEntity = UserEntity.builder()
                .name("user Test")
                .email("email@email.com")
                .password(passwordEncoder.encode("passwordTest"))
                .role(RoleUser.instructor)
                .build();
        userRepository.save(userEntity);

        CreateAuthRequestDto createAuthRequestDto = CreateAuthRequestDto.builder()
                .email("email@email.com")
                .password("passwordTest")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String contentJsonString = objectMapper.writeValueAsString(createAuthRequestDto);
        RoleUser roleUser = RoleUser.instructor;

        //Invocar o endpoint
        mockMvc.perform(MockMvcRequestBuilders.post(uriTest)
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentJsonString))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().string("Privilégio do usuário não liberado!"));
    }

    @Test
    void shouldBeThrowsWhenPasswordUserIsDifferentUserRequest() throws Exception {
        //given
        UserEntity userEntity = UserEntity.builder()
                .name("user test")
                .email("emailtest@email.com")
                .password(passwordEncoder.encode("passwordTest"))
                .role(RoleUser.student)
                .build();
        userRepository.save(userEntity);

        CreateAuthRequestDto createAuthRequestDto = CreateAuthRequestDto.builder()
                .email("emailtest@email.com")
                .password("passwordDifferent")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStringRequest = objectMapper.writeValueAsString(createAuthRequestDto);

        mockMvc.perform(MockMvcRequestBuilders.post(uriTest)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonStringRequest))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().string("Email ou senha não conferem."));

    }
}
