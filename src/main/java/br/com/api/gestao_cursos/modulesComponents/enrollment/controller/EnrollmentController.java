package br.com.api.gestao_cursos.modulesComponents.enrollment.controller;

import br.com.api.gestao_cursos.modulesComponents.enrollment.useCaseService.CreateEnrollmentUseCaseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/students")
public class EnrollmentController {

    @Autowired
    private CreateEnrollmentUseCaseService createEnrollmentUseCaseService;

    @PreAuthorize("hasRole('student')")//Para essa anotação funcionar, tenho que habilitar @EnableMethodSecurity na classe SecurityConfig
    @PostMapping("/enrollments/")
    //Com HttpServletRequest irei trazer o studentId, a única coisa que virá pelo payload será o courseId
    public ResponseEntity<?> create(HttpServletRequest request,
                                    @Valid @RequestBody UUID courseId){
        try {
            var studentId = UUID.fromString(request.getAttribute("userId").toString());//O nome da propriedade tem que ser igual ao que está na classe SecurityFilter
            return ResponseEntity.ok(createEnrollmentUseCaseService.createEnrollment(courseId, studentId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
