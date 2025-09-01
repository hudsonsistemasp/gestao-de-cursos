package br.com.api.gestao_cursos.modulesComponents.courses.useCaseService;

import br.com.api.gestao_cursos.exceptions.ValidationException;
import br.com.api.gestao_cursos.modulesComponents.courses.repository.CourseRepository;
import br.com.api.gestao_cursos.modulesComponents.courses.dto.CreateCourseRequestDto;
import br.com.api.gestao_cursos.modulesComponents.courses.entities.CourseEntity;
import br.com.api.gestao_cursos.modulesComponents.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateCourseUseCase {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    public CourseEntity validateData(CreateCourseRequestDto createCourseRequestDto, String instructorId) throws Exception {

        //Verificar se o instructor existe na tabela antes de ele ser cadastrado no curso
        if (!userRepository.existsById(UUID.fromString(instructorId))){
            throw new Exception("Instrutor não existe no sistema. Favor cadastrá-lo!");
        }

        /*Não deve ser possível que:
        1. O professor criar um curso sem preencher todos os campos obrigatórios ou com informações inválidas.*/
        if (createCourseRequestDto.getTitle().trim().isEmpty() || createCourseRequestDto.getTitle().trim().isBlank() || createCourseRequestDto.getTitle() == null){
            throw new ValidationException("Curso necessita de ter titulo!");
        }
        if (createCourseRequestDto.getDescription().trim().isEmpty() || createCourseRequestDto.getDescription().trim().isBlank() || createCourseRequestDto.getDescription() == null){
            throw new ValidationException("Curso necessita de ter descrição!");
        }

        //2. Cadastrar curso com o mesmo nome
        var courseTitleEntity = courseRepository.findByTitle(createCourseRequestDto.getTitle());
        if (courseTitleEntity.isPresent()) {
            if (courseTitleEntity.get().getTitle().trim().equals(createCourseRequestDto.getTitle().trim())) {
                throw new Exception("Curso já está cadastrado.");
            }
        }

        //Converter para Entity
        CourseEntity courseEntity = CourseEntity.builder()
                .title(createCourseRequestDto.getTitle())
                .description(createCourseRequestDto.getDescription())
                .instructorId(UUID.fromString(instructorId))//Futuro vai vir pela autenticação
                .build();

        return courseRepository.save(courseEntity);
    }


}
