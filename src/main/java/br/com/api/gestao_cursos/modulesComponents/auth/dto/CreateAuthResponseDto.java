package br.com.api.gestao_cursos.modulesComponents.auth.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CreateAuthResponseDto {

    private String access_token;
    private Date expires_in;
    private Date created_at;



}
