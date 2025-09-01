package br.com.api.gestao_cursos.modulesComponents.courses.controller;

import br.com.api.gestao_cursos.exceptions.ValidationException;
import br.com.api.gestao_cursos.modulesComponents.courses.dto.CreateCourseListPage;
import br.com.api.gestao_cursos.modulesComponents.courses.dto.CreateCourseRequestDto;
import br.com.api.gestao_cursos.modulesComponents.courses.entities.CourseEntity;
import br.com.api.gestao_cursos.modulesComponents.courses.repository.CourseRepository;
import br.com.api.gestao_cursos.modulesComponents.courses.useCaseService.CreateCourseUseCase;
import br.com.api.gestao_cursos.modulesComponents.courses.useCaseService.ListPageCourseUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    ListPageCourseUseCase listPageCourseUseCase;

    @Autowired
    private CreateCourseUseCase createCourseUseCase;

    @Autowired
    private CourseRepository courseRepository;


    @PostMapping("/create")
    @PreAuthorize("hasRole('instructor')")//Para essa anotação funcionar, tenho que habilitar @EnableMethodSecurity na classe SecurityConfig
    public ResponseEntity<?> create(HttpServletRequest request,
                                    @Valid @RequestBody CreateCourseRequestDto createCourseRequestDto) {
        String userIdDaRequest = request.getAttribute("userId").toString();//"userId" atributo que criei na classe SecurityFilter.java
        try{
            CourseEntity courseEntity = createCourseUseCase.validateData(createCourseRequestDto, userIdDaRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("Curso Criado com sucesso!");
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @PreAuthorize("hasAnyRole('instructor','student')")//Para essa anotação funcionar, tenho que habilitar @EnableMethodSecurity na classe SecurityConfig
    @GetMapping("/list")
    public ResponseEntity<?> listCourses(){

        try {
            List<CourseEntity> courseEntityList = courseRepository.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(courseEntityList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @PreAuthorize("hasAnyRole('instructor','student')")//Para essa anotação funcionar, tenho que habilitar @EnableMethodSecurity na classe SecurityConfig
    @GetMapping("/coursesPage")
    public ResponseEntity<CreateCourseListPage> listCoursesPage(@RequestParam int numberOfPage,
                                                                @RequestParam int amountByPage,
                                                                @RequestParam String orderBy,
                                                                @RequestParam String orderAscOrDesc){

        return ResponseEntity.ok(listPageCourseUseCase.
                getAllCourses(numberOfPage, amountByPage, orderBy, orderAscOrDesc));
    }



}
