package br.com.api.gestao_cursos.modulesComponents.auth.dto;

import lombok.Data;

@Data
public class CreateAuthRequestDto {
    private String email;
    private String password;
}
