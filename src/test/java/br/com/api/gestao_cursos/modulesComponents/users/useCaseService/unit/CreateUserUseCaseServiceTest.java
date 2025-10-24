package br.com.api.gestao_cursos.modulesComponents.users.useCaseService.unit;

import br.com.api.gestao_cursos.exceptions.ValidationException;
import br.com.api.gestao_cursos.modulesComponents.users.dto.CreateUserRequestDto;
import br.com.api.gestao_cursos.modulesComponents.users.entities.RoleUser;
import br.com.api.gestao_cursos.modulesComponents.users.entities.UserEntity;
import br.com.api.gestao_cursos.modulesComponents.users.repository.UserRepository;
import br.com.api.gestao_cursos.modulesComponents.users.useCaseService.CreateUserUseCaseService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

//import static br.com.api.gestao_cursos.modulesComponents.users.entities.RoleUser.instructor;

@ExtendWith(MockitoExtension.class)
public class CreateUserUseCaseServiceTest {

    @InjectMocks
    CreateUserUseCaseService createUserUseCaseService;
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    void shouldThrowsExceptionWhenUserNameIsEmpty(){
        //given
        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto();
        createUserRequestDto.setName("");
        createUserRequestDto.setPassword("Criarteste*3");
        createUserRequestDto.setEmail("instructor@teste.com.br");
        createUserRequestDto.setRole(RoleUser.student);

        //when

        //then
        ValidationException validationException = Assertions.assertThrows(ValidationException.class, () -> {
            createUserUseCaseService.validateData(createUserRequestDto);
        });
        Assertions.assertEquals("Dados incompletos, favor preencheer todos os campos!", validationException.getMessage());
    }

    @Test
    void shouldThrowsExceptionWhenUserPasswordIsEmpty(){
        //given
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.builder()
                .name("nameTest")
                .password("")
                .role(RoleUser.student)
                .build();
        //when

        //then
        ValidationException validationException = Assertions.assertThrows(ValidationException.class, () -> {
            createUserUseCaseService.validateData(createUserRequestDto);
        });
        Assertions.assertEquals("Dados incompletos, favor preencheer todos os campos!", validationException.getMessage());
    }

    @Test
    void shouldThrowsExceptionWhenUserRoleIsNull(){
        //given
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.builder()
                .name("name test")
                .password("passwordTest")
                .role(null)
                .build();
        //when

        //then
        ValidationException validationException = Assertions.assertThrows(ValidationException.class, () -> {
            createUserUseCaseService.validateData(createUserRequestDto);
        });
        Assertions.assertEquals("Dados incompletos, favor preencheer todos os campos!", validationException.getMessage(), "Deu exception");
    }

    @Test
    void shouldThrowsExceptionWhenEmailIsInvalid(){
        //given
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.builder()
                .name("name Test")
                .email("emailInvalid")
                .password("passwordTest*12")
                .role(RoleUser.student)
                .build();

        //when

        //then
        ValidationException validationException = Assertions.assertThrows(ValidationException.class, () -> {
            createUserUseCaseService.validateData(createUserRequestDto);
        });
        Assertions.assertEquals("Email inválido!", validationException.getMessage());
    }

    @Test
    void shouldThrowsExceptionWhenPasswordIsWeak(){
        //givn
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.builder()
                .name("name Test")
                .email("instructor@teste.com.br")
                .password("123")
                .role(RoleUser.student)
                .build();
        //when

        //then
        ValidationException validationException = Assertions.assertThrows(ValidationException.class, ()->{
            createUserUseCaseService.validateData(createUserRequestDto);
        });
        Assertions.assertEquals("Senha não atende aos requisitos de segurança!", validationException.getMessage());
    }

    @Test
    void shouldThrowsExceptionWhenUserEmailAlreadyExists(){
        //given
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.builder()
                .name("name Test")
                .email("instructor@teste.com.br")
                .password("passwordTest*12")
                .role(RoleUser.student)
                .build();
        UserEntity userEntity = UserEntity.builder()
                .email("instructor@teste.com.br")
                .build();

        //when
        Mockito.when(userRepository.findByEmail("instructor@teste.com.br")).thenReturn(Optional.of(userEntity));
        //then
        ValidationException validationException = Assertions.assertThrows(ValidationException.class, () ->{
            createUserUseCaseService.validateData(createUserRequestDto);
        });
        Assertions.assertEquals("Email já cadastrado no sistema!", validationException.getMessage());
    }

    @Test
    void shouldCreateUserSuccessfully() throws ValidationException {
        //given

        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.builder()
                .name("Name Test")
                .email("instructor@teste.com.br")
                .password("passwordTest*12")
                .role(RoleUser.student)
                .build();
        
        UUID idUser = UUID.randomUUID();
        UserEntity userEntity = UserEntity.builder()
                .id(idUser)
                .name("Name Test")
                .email("instructor@teste.com.br")
                .password("passwordTest*12")
                .role(RoleUser.student)
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        //when
        Mockito.when(userRepository.findByEmail("instructor@teste.com.br")).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode("passwordTest*12")).thenReturn("strongly-encrypted-password");
        Mockito.when(userRepository.save(any())).thenReturn(userEntity);
        UserEntity result = createUserUseCaseService.validateData(createUserRequestDto);
        //then
        Mockito.verify(userRepository, Mockito.times(1)).save(any());
        Assertions.assertEquals("Name Test", result.getName());
        Assertions.assertEquals("instructor@teste.com.br", result.getEmail());
        Assertions.assertEquals("passwordTest*12", result.getPassword());

    }
}
