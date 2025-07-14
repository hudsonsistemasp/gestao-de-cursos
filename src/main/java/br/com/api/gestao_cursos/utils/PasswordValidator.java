package br.com.api.gestao_cursos.utils;

import java.util.regex.Pattern;

public class PasswordValidator {
    private static final int MIN_LENGTH= 8;
    private static final int MAX_LENGTH = 20;
    private static final Pattern UPPER_CASE_STRING_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern LOWER_CASE_STRING_PATTERN = Pattern.compile("[a-z]");
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("[0-9]");


    public static boolean isPasswordStrongSecurity(String password){
        //Agora vamos testar cada uma das vars acima
        if (password.length() < MIN_LENGTH){
            return false;
        }
        if (password.length() > MAX_LENGTH){
            return false;
        }
        if (!NUMERIC_PATTERN.matcher(password).find()){
            return false;
        }
        if (!LOWER_CASE_STRING_PATTERN.matcher(password).find()){
            return false;
        }
        if (!UPPER_CASE_STRING_PATTERN.matcher(password).find()){
            return false;
        }

        return true;
    }

    public static void main(String[] args) {
        System.out.println(isPasswordStrongSecurity("11"));
    }
}
