package br.com.api.gestao_cursos.modulesComponents.lessons.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonsRequestDto {

    @NotNull(message = "Id do Módulo do curso é obrigatório")
    private UUID idModuleCourse;

    @NotBlank(message = "Titulo da aula é obrigatório")
    private String title;

    @NotNull(message = "Ordem de exibição da aula é obrigatório")
    private Integer displayOrder;


}
