package br.com.api.gestao_cursos.modulesComponents.auth.useCaseService.unit;

import br.com.api.gestao_cursos.modulesComponents.auth.dto.CreateAuthRequestDto;
import br.com.api.gestao_cursos.modulesComponents.auth.useCaseService.CreateAuthUseCase;
import br.com.api.gestao_cursos.modulesComponents.users.entities.RoleUser;
import br.com.api.gestao_cursos.modulesComponents.users.entities.UserEntity;
import br.com.api.gestao_cursos.modulesComponents.users.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateAuthUseCaseTest {

    @InjectMocks
    private CreateAuthUseCase createAuthUseCase;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldThrowsExceptionWhenUserNotExists(){
        //given
        UserEntity userEntity = new UserEntity();
        CreateAuthRequestDto createAuthRequestDto = new CreateAuthRequestDto("teste@teste.com.br","password");
        //when
        when(userRepository.findByEmail(createAuthRequestDto.getEmail())).thenReturn(Optional.empty());
        //then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            createAuthUseCase.create(createAuthRequestDto, RoleUser.instructor);
        });
        assertEquals("Usuário não existe no sistema!", exception.getMessage());

    }

    @Test
    void shouldThrowsExceptionWhenUserNotContainsPrivilege(){
        //given
        CreateAuthRequestDto createAuthRequestDto = CreateAuthRequestDto.builder()
                .email("teste@teste.com")
                .password("password")
                .build();

        UserEntity userEntity = UserEntity.builder().role(RoleUser.instructor).build();
        //when
        when(userRepository.findByEmail(createAuthRequestDto.getEmail())).thenReturn(Optional.of(userEntity));

        //then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            createAuthUseCase.create(createAuthRequestDto, RoleUser.student);
        });
        assertEquals("Privilégio do usuário não liberado!", exception.getMessage());
    }

    @Test
    void shouldThrowsExceptionWhenEmailOrPasswordNotEquals(){
        //given
        CreateAuthRequestDto createAuthRequestDto = new CreateAuthRequestDto("email@email.com", "password");
        UserEntity userEntity = UserEntity.builder().email("emailteste@email.com").password("passwordTest").role(RoleUser.instructor).build();

        //when
        when(userRepository.findByEmail(createAuthRequestDto.getEmail())).thenReturn(Optional.of(userEntity));

        //then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            createAuthUseCase.create(createAuthRequestDto, RoleUser.instructor);
        });
        assertEquals("Email ou senha não conferem.", exception.getMessage());
    }

    @Test
    void shouldCreateAuthUserSuccessfully() throws UserPrincipalNotFoundException {
        //given
        CreateAuthRequestDto createAuthRequestDto = new CreateAuthRequestDto("teste@teste.com","password");
        UserEntity userEntity = UserEntity.builder()
                .id(UUID.randomUUID())
                .email("teste@teste.com")
                .password("password")
                .role(RoleUser.student)
                .build();
        //when
        when(userRepository.findByEmail(createAuthRequestDto.getEmail())).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(createAuthRequestDto.getPassword(), userEntity.getPassword())).thenReturn(true);
        //then
        var result = createAuthUseCase.create(createAuthRequestDto, RoleUser.student);
        assertNotNull(result);
        verify(userRepository, times(1)).findByEmail(any());
        verify(passwordEncoder, times(1)).matches(any(), any());

    }





}
