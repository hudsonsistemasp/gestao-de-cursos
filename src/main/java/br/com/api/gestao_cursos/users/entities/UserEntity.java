package br.com.api.gestao_cursos.users.entities;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserEntity {

    private UUID id;
    private String name;
    private String email;
    private String password;
    private RoleUser role;
    private LocalDateTime createdAt;
    private LocalDate updateAt;

}
