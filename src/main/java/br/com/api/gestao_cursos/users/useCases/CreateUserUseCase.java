package br.com.api.gestao_cursos.users.useCases;

import br.com.api.gestao_cursos.exceptions.ValidationException;
import br.com.api.gestao_cursos.users.dto.CreateUserRequestDto;
import br.com.api.gestao_cursos.users.entities.UserEntity;
import br.com.api.gestao_cursos.utils.EmailValidator;
import br.com.api.gestao_cursos.utils.PasswordValidator;
import org.springframework.stereotype.Service;

/*Como eu preciso usar essa classe na DTO, logo vou fazer o Spring instanciar
isso para mim, usando a anotação @Service e esta ficar disponível para uso como
um serviço de validação de dados. Assim injeto como dependência em outras classes*/
@Service
public class CreateUserUseCase {

    public String validateData(CreateUserRequestDto userRequestDto){
        //1.Não deve ser possível que usuário se cadastre sem preencher todos os campos obrigatórios.
        if (userRequestDto.getName().isEmpty() || userRequestDto.getPassword().trim().isEmpty() || userRequestDto.getRole() == null) {
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
        //4. O sistema envie um e-mail de confirmação para o endereço fornecido pelo usuário após o cadastro
        //5.Não deve ser possível que o sistema envie o e-mail de confirmação se a verificação dos dados do usuário falhar.

        return "retorno da API em breve";
    }
}
