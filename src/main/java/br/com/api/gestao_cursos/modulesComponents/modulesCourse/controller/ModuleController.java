package br.com.api.gestao_cursos.modulesComponents.modulesCourse.controller;

import br.com.api.gestao_cursos.exceptions.ForbbidenException;
import br.com.api.gestao_cursos.modulesComponents.modulesCourse.dto.CreateModuleRequestDto;
import br.com.api.gestao_cursos.modulesComponents.modulesCourse.entity.ModuleCourseEntity;
import br.com.api.gestao_cursos.modulesComponents.modulesCourse.useCaseService.CreateModuleUseCaseService;
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

@RequestMapping("/modules")
@RestController
public class ModuleController {

    @Autowired
    CreateModuleUseCaseService createModuleUseCaseService;

    @PreAuthorize("hasRole('instructor')")
    @PostMapping("/create")
    public ResponseEntity<?> create(HttpServletRequest request,
                                    @Valid @RequestBody CreateModuleRequestDto createModuleRequestDto) {

        try {
            String idUserRequestObject = request.getAttribute("userId").toString();
            UUID idUserRequest = UUID.fromString(idUserRequestObject);
            ModuleCourseEntity moduleCourseEntity = this.createModuleUseCaseService.create(createModuleRequestDto, idUserRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(moduleCourseEntity.getId());
        } catch (ForbbidenException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }



}
