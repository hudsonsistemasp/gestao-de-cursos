package br.com.api.gestao_cursos.exceptions;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

//OBS: O @Valid que está na Controller é que faz acionar essa classe
@ControllerAdvice //Essa anotação diz ao Spring que vai dedicar somente à camada Controller
public class ExceptionHandlerController {

    //Tudo para baixo é seguindo a documentação do Spring

    private MessageSource messageSource;

    public ExceptionHandlerController(MessageSource messageSource){
        this.messageSource = messageSource;
    }

    //E tenho que dizer para que ele fique de guardião e intercepte tudo que for dessa natureza exception.E assim existem outras que posso personalizar
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorMessageDto>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){

        List<ErrorMessageDto> listErros = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(erros -> {
            String message = messageSource.getMessage(erros, LocaleContextHolder.getLocale());
            ErrorMessageDto errorsMessageDto = new ErrorMessageDto(message, erros.getField());
            listErros.add(errorsMessageDto);
        });


        //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(listErros);
        //ou
        return new ResponseEntity<>(listErros, HttpStatus.BAD_REQUEST);
    }

}
