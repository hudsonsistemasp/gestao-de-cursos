package br.com.api.gestao_cursos.users.useCases;

import br.com.api.gestao_cursos.exceptions.ValidationException;
import br.com.api.gestao_cursos.users.entities.UserEntity;
import br.com.api.gestao_cursos.utils.EmailValidator;
import br.com.api.gestao_cursos.utils.PasswordValidator;

import java.util.regex.Pattern;

public class CreateUserUseCase {


    public String create(UserEntity userEntity){
        //1.Não deve ser possível que usuário se cadastre sem preencher todos os campos obrigatórios.
        if (userEntity.getName().isEmpty() || userEntity.getPassword().isEmpty() || userEntity.getRole() == null) {
            throw new ValidationException("Dados incompletos, favor preencheer todos os campos!");
        }

        //2. Não deve ser possível que o usuário se cadastre uzando um e-mail em formato inválido
        if (!EmailValidator.validateEmail(userEntity.getEmail())){
           throw new ValidationException("Email inválido!");
        }

        //3. O usuário pode cadastrar senhas em texto simples; comprimento mínimo, caracteres especiais
        if (!PasswordValidator.isPasswordStrongSecurity(userEntity.getPassword())) {
            throw new ValidationException("Senha não atende aos requisitos de segurança!");
        }
        //2.1. Não deve ser possível que o usuário se cadastre uzando um e-mail que esteja cadastrado no sistema.
        //4. O sistema envie um e-mail de confirmação para o endereço fornecido pelo usuário após o cadastro
        //5.Não deve ser possível que o sistema envie o e-mail de confirmação se a verificação dos dados do usuário falhar.

        return "retorno da API em breve";
    }
}
