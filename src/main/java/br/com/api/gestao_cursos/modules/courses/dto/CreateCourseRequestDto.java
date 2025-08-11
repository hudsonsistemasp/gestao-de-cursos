package br.com.api.gestao_cursos.users.dto;

import lombok.Data;

@Data
public class CreateCourseRequestDto {

    private String titulo;
    private String descricao;

}
