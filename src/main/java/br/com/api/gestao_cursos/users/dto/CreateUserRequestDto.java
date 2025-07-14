package br.com.api.gestao_cursos.users.dto;

import br.com.api.gestao_cursos.users.entities.RoleUser;
import lombok.Data;

@Data
public class CreateUserRequestDto {

    private String name;
    private String email;
    private String password;
    private RoleUser role;

}
