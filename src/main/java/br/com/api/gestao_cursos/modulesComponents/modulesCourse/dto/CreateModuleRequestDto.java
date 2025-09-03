package br.com.api.gestao_cursos.modulesComponents.modulesCourse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateModuleRequestDto {

    @NotNull(message = "Id do curso é obrigatório")
    private UUID idCourse;//Na criação do curso, já temos um Instrutor atrelado a ele

    @NotBlank(message = "Title do Modulo do curso é obrigatório")
    private String title;

    @NotBlank(message = "Description do Modulo do curso é obrigatório")
    private String description;

    @NotNull(message = "DisplayOrder do Modulo do curso é obrigatório")
    private Integer displayOrder;
}
