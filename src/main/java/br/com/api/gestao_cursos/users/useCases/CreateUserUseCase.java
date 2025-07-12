package br.com.api.gestao_cursos.users.useCases;

import br.com.api.gestao_cursos.users.entities.UserEntity;

import java.util.regex.Pattern;

public class CreateUserUseCase {

    private static final String regexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    public void create(UserEntity userEntity){
        //1.Não deve ser possível que usuário se cadastre sem preencher todos os campos obrigatórios.
        if (userEntity.getName().isEmpty() || userEntity.getPassword().isEmpty() || userEntity.getRole() == null) {
            throw new RuntimeException("Dados incompletos, favor preencheer todos!");
        }

        //2. Não deve ser possível que o usuário se cadastre uzando um e-mail em formato inválido
        Pattern pattern = Pattern.compile(regexPattern);
        if (pattern.matcher(userEntity.getEmail()).matches()) {
            throw new RuntimeException("Email está em formato inválido!");
        }


        //2.1. Não deve ser possível que o usuário se cadastre uzando um e-mail que esteja cadastrado no sistema.
        //3. O usuário pode cadastrar senhas em texto simples; comprimento mínimo, caracteres especiais
        //4. O sistema envie um e-mail de confirmação para o endereço fornecido pelo usuário após o cadastro
        //5.Não deve ser possível que o sistema envie o e-mail de confirmação se a verificação dos dados do usuário falhar.
    }
}
