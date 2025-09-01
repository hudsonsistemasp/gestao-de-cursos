package br.com.api.gestao_cursos.modulesComponents.courses.entities;

import br.com.api.gestao_cursos.modulesComponents.users.entities.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_course")
@Entity
public class CourseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Title do curso é obrigatório")
    private String title;

    @NotBlank(message = "Description do curso é obrigatória")
    private String description;

    //Para que o instrutor se relacione com esta classe Curso precisamos destes 2 atributos: instructor e id dele
    @ManyToOne
    @JoinColumn(name = "instructor_id", updatable = false, insertable = false)
    private UserEntity instructor;

    @Column(name = "instructor_id")
    private UUID instructorId;
    //---------

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}



