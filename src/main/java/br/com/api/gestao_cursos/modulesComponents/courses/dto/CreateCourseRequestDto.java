package br.com.api.gestao_cursos.modulesComponents.courses.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCourseRequestDto {

    @NotBlank(message = "Não pode estar vazio")
    @Size(min = 2, max = 50, message = "title deve ter entre 10 a 50 caracteres")
    private String title;

    @NotBlank(message = "Não pode estar vazio")
    @Size(min = 10, max = 100, message = "description deve ter entre 10 a 100 caracteres")
    private String description;

}
