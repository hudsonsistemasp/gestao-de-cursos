package br.com.api.gestao_cursos.modulesComponents.users.entities;

public enum RoleUser {
    instructor("instructor"), student("student");

    private String description;

    RoleUser(String description){
        this.description = description;
    }
     public String getDescription(){
        return this.description;
     }
}
