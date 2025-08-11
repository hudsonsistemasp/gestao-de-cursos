package br.com.api.gestao_cursos.modules.auth.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CreateAuthResponse {

    private String access_token;
    private Date expires_in;



}
