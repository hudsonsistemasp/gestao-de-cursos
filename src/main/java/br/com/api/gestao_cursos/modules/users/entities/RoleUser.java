package br.com.api.gestao_cursos.users.entities;

public enum RoleUser {
    PROFESSOR("PROFESSOR"), ALUNO("ALUNO");

    private String description;

    RoleUser(String description){
        this.description = description;
    }
     public String getDescription(){
        return this.description;
     }
}
