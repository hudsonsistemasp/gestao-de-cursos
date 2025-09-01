package br.com.api.gestao_cursos.modulesComponents.users.dto;

import br.com.api.gestao_cursos.modulesComponents.users.entities.RoleUser;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateUserRequestDto {

    @NotBlank(message = "Nome do usuário é obrigatório")
    private String name;

    @NotBlank(message = "Email do usuário é obrigatório")
    private String email;

    @NotBlank(message = "Obrigatório o preenchimento do password do usuário")
    private String password;

    @NotNull(message = "Obrigatório preenhcer a Role do usuário")
    @Enumerated(EnumType.STRING)
    private RoleUser role;

}
