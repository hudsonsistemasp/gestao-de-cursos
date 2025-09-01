package br.com.api.gestao_cursos.modulesComponents.courses.utils;

import br.com.api.gestao_cursos.modulesComponents.courses.dto.CreateCourseRequestDto;
import br.com.api.gestao_cursos.modulesComponents.courses.entities.CourseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ConvertEntityToDto {

    public CreateCourseRequestDto toCourseDto(CourseEntity courseEntity){

        CreateCourseRequestDto courseDto = CreateCourseRequestDto.builder()
                .title(courseEntity.getTitle())
                .description(courseEntity.getDescription())
                .build();

        return courseDto;
    }

    public List<CreateCourseRequestDto> toListCourseDto(List<CourseEntity> listCourses){
        if (listCourses.isEmpty()){
            return null;
        }

        //Criar uma lista para dar retorno nela
        List<CreateCourseRequestDto> listCoursesDto = new ArrayList<>();
        for (CourseEntity courseEntityList : listCourses) {
            listCoursesDto.add(this.toCourseDto(courseEntityList));
        }

        return listCoursesDto;
    }

}
