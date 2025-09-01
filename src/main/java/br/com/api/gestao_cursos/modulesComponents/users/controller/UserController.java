package br.com.api.gestao_cursos.modulesComponents.users.controller;

import br.com.api.gestao_cursos.exceptions.ValidationException;
import br.com.api.gestao_cursos.modulesComponents.users.dto.CreateUserRequestDto;
import br.com.api.gestao_cursos.modulesComponents.users.entities.UserEntity;
import br.com.api.gestao_cursos.modulesComponents.users.useCaseService.CreateUserUseCaseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    //Com essa anotação, falo para ele instanciar a classe CreateUserUseCase dentro dessa automaticamente
    @Autowired
    private CreateUserUseCaseService createUserUseCaseService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody CreateUserRequestDto userRequestDto) {
        try {
            UserEntity result = this.createUserUseCaseService.validateData(userRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch(ValidationException e){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
