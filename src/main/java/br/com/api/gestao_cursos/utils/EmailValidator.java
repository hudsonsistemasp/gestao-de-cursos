package br.com.api.gestao_cursos.utils;

import java.util.regex.Pattern;

public class EmailValidator {
    private static final String REGEX_PATTERN = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    public static boolean validateEmail(String email){
        Pattern pattern = Pattern.compile(REGEX_PATTERN);
        return pattern.matcher(email).matches();
    }
}
