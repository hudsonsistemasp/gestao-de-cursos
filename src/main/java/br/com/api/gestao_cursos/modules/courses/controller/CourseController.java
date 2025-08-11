package br.com.api.gestao_cursos.modules.courses;

import br.com.api.gestao_cursos.exceptions.ValidationException;
import br.com.api.gestao_cursos.modules.courses.repository.CourseRepository;
import br.com.api.gestao_cursos.modules.courses.dto.CreateCourseRequestDto;
import br.com.api.gestao_cursos.modules.courses.entities.CourseEntity;
import br.com.api.gestao_cursos.modules.courses.useCaseService.CreateCourseUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CreateCourseUseCase createCourseUseCase;

    @Autowired
    private CourseRepository courseRepository;



    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CreateCourseRequestDto createCourseRequestDto) {

        try{
            CourseEntity courseEntity = createCourseUseCase.validateData(createCourseRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Curso Criado com sucesso!");
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @GetMapping("/list")
    public ResponseEntity<?> listCourses(){

        try {
            List<CourseEntity> courseEntityList = courseRepository.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(courseEntityList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }





}
