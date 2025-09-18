package br.com.api.gestao_cursos.modulesComponents.users.useCaseService;

import br.com.api.gestao_cursos.exceptions.ValidationException;
import br.com.api.gestao_cursos.modulesComponents.users.repository.UserRepository;
import br.com.api.gestao_cursos.modulesComponents.users.dto.CreateUserRequestDto;
import br.com.api.gestao_cursos.modulesComponents.users.entities.UserEntity;
import br.com.api.gestao_cursos.utils.EmailValidator;
import br.com.api.gestao_cursos.utils.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Optional;

/*Como eu preciso usar essa classe na DTO, logo vou fazer o Spring instanciar
isso para mim, usando a anotação @Service e esta ficar disponível para uso como
um serviço de validação de dados. Assim injeto como dependência em outras classes*/
@Service
public class CreateUserUseCaseService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = false, rollbackFor = {SQLException.class})
    public UserEntity validateData(CreateUserRequestDto userRequestDto) throws ValidationException {

        //1.Não deve ser possível que usuário se cadastre sem preencher todos os campos obrigatórios.
        if (userRequestDto.getName().isEmpty() || userRequestDto.getPassword().trim().isEmpty()
                || userRequestDto.getRole() == null) {
            throw new ValidationException("Dados incompletos, favor preencheer todos os campos!");
        }

        //2. Não deve ser possível que o usuário se cadastre uzando um e-mail em formato inválido
        if (!EmailValidator.validateEmail(userRequestDto.getEmail())){
           throw new ValidationException("Email inválido!");
        }

        //3. O usuário pode cadastrar senhas em texto simples; comprimento mínimo, caracteres especiais
        if (!PasswordValidator.isPasswordStrongSecurity(userRequestDto.getPassword())) {
            throw new ValidationException("Senha não atende aos requisitos de segurança!");
        }

        //2.1. Não deve ser possível que o usuário se cadastre uzando um e-mail que esteja cadastrado no sistema.
        Optional<UserEntity> userEntityByEmail = this.userRepository.findByEmail(userRequestDto.getEmail());
        if (userEntityByEmail.isPresent()){
            throw new ValidationException("Email já cadastrado no sistema!");
        }

        //TODO: 4. Criptografar o password do usuário antes de salvá-lo
        String passwordCrypt = passwordEncoder.encode(userRequestDto.getPassword());
        //TODO: 5. O sistema envie um e-mail de confirmação para o endereço fornecido pelo usuário após o cadastro
        //TODO: 6.Não deve ser possível que o sistema envie o e-mail de confirmação se a verificação dos dados do usuário falhar.

        UserEntity entitySaved = UserEntity.builder()
                .name(userRequestDto.getName())
                .email(userRequestDto.getEmail())
                .password(passwordCrypt)
                .role(userRequestDto.getRole())
                //.createdAt(LocalDateTime.now())
                //.updateAt(LocalDate.now())
                .build();

        return userRepository.save(entitySaved);
    }
}
