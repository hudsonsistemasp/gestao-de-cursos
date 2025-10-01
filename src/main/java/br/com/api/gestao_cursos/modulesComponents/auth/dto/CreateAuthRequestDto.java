package br.com.api.gestao_cursos.modulesComponents.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAuthRequestDto {
    private String email;
    private String password;
}
