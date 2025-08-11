package br.com.api.gestao_cursos.users.useCases;

import br.com.api.gestao_cursos.repository.CourseRepository;
import br.com.api.gestao_cursos.users.dto.CreateCourseRequestDto;
import br.com.api.gestao_cursos.users.entities.CourseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateCourseUseCase {

    @Autowired
    private CourseRepository courseRepository;

    public CourseEntity validateData(CreateCourseRequestDto createCourseRequestDto){
        /*Não deve ser possível que:
        1. O professor criar um curso sem preencher todos os campos obrigatórios ou com informações inválidas.*/
        if (createCourseRequestDto.getTitulo().trim().isEmpty() || createCourseRequestDto.getTitulo().trim().isBlank()){
            throw new RuntimeException("Curso necessita de ter titulo!");
        }
        if (createCourseRequestDto.getDescricao().trim().isEmpty() || createCourseRequestDto.getDescricao().trim().isBlank()){
            throw new RuntimeException("Curso necessita de ter descrição!");
        }

        //Converter para Entity
        CourseEntity courseEntity = CourseEntity.builder()
                .titulo(createCourseRequestDto.getTitulo())
                .descricao(createCourseRequestDto.getDescricao())
                .build();

        return courseRepository.save(courseEntity);
    }


}
