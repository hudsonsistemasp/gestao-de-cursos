package br.com.api.gestao_cursos.modules.auth.dto;

import lombok.Data;

@Data
public class CreateAuthRequestDto {
    private String email;
    private String password;
}
