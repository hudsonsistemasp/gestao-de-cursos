package br.com.api.gestao_cursos.modulesComponents.auth.controller;

import br.com.api.gestao_cursos.modulesComponents.auth.dto.CreateAuthRequestDto;
import br.com.api.gestao_cursos.modulesComponents.auth.useCaseService.CreateAuthUseCase;
import br.com.api.gestao_cursos.modulesComponents.users.entities.RoleUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.attribute.UserPrincipalNotFoundException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private CreateAuthUseCase createAuthUseCase;

    @PostMapping("/instructor")
    public ResponseEntity<?> authInstructor(@RequestBody CreateAuthRequestDto createAuthRequestDto){

        try {
            var token = createAuthUseCase.create(createAuthRequestDto, RoleUser.instructor);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

    }

    @PostMapping("/student")
    public ResponseEntity<?> authStudent(@RequestBody CreateAuthRequestDto createAuthRequestDto){
        try {
            var token = createAuthUseCase.create(createAuthRequestDto, RoleUser.student);
            return ResponseEntity.ok(token);
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

}
