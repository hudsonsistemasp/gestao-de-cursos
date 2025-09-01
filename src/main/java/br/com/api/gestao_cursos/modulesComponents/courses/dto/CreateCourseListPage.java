package br.com.api.gestao_cursos.modulesComponents.courses.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCourseListPage {
    
    private List<CreateCourseRequestDto> contentPage;
    private int numPageActual;
    private int amountByPage;
    private Long totalRecords;
    private int totalPages;

}
