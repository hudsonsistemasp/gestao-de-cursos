package br.com.api.gestao_cursos.modulesComponents.lessons.controller;

import br.com.api.gestao_cursos.modulesComponents.lessons.dto.LessonsRequestDto;
import br.com.api.gestao_cursos.modulesComponents.lessons.useCaseService.CreateLessonUseCaseService;
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
@RequestMapping("/lessons")
public class LessonController {

    @Autowired
    CreateLessonUseCaseService createLessonUseCaseService;

    @PreAuthorize("hasRole('instructor')")
    @PostMapping("/create")
    public ResponseEntity<?> create(HttpServletRequest request, @Valid @RequestBody LessonsRequestDto lessonsRequestDto){
        UUID idUserRequest = UUID.fromString(request.getAttribute("userId").toString());
        try {
           var lessonsEntinty = createLessonUseCaseService.create(idUserRequest, lessonsRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(lessonsEntinty);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
