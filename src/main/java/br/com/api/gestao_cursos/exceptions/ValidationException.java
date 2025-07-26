package br.com.api.gestao_cursos.exceptions;

public class ValidationException extends RuntimeException{

    public ValidationException(String message){
        super(message);
    }

}
