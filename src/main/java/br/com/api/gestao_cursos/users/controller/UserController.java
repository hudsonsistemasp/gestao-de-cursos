package br.com.api.gestao_cursos.users.controller;

import br.com.api.gestao_cursos.exceptions.ValidationException;
import br.com.api.gestao_cursos.users.dto.CreateUserRequestDto;
import br.com.api.gestao_cursos.users.entities.UserEntity;
import br.com.api.gestao_cursos.users.useCases.CreateUserUseCase;
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
    private CreateUserUseCase createUserUseCase;

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody CreateUserRequestDto userRequestDto){
        try {
            String result = this.createUserUseCase.validateData(userRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }
}
