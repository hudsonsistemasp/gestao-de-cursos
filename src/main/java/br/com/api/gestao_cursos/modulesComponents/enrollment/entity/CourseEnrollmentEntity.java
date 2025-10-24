package br.com.api.gestao_cursos.modulesComponents.enrollment.entity;

import br.com.api.gestao_cursos.modulesComponents.courses.entities.CourseEntity;
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


@Entity
@Table(name = "tb_CourseEnrollment")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseEnrollmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;


    //---------RELACIONAMENTO DAS CLASSES - INICIO
    //---------Para que o aluno se relacione com esta classe do Curso precisamos destes 2 atributos:
    // O Entity do student e um id que aponte para ele como Foreign Key
    @Column(name = "student_id")
    private UUID studentId;

    @ManyToOne
    @JoinColumn(name = "student_id", insertable = false, updatable = false)
    private UserEntity userEntity;

    @NotBlank(message = "Id do curso é obrigatório")
    @Column(name = "course_id")
    private UUID courseId;

    //updatable e insertable = false quer dizer que essa propriedade(entity) não será usada
    //para persistir os dados, blindamos para isso. No caso usaremos um DTO  para persistir.
    @ManyToOne
    @JoinColumn(name = "course_id", insertable=false, updatable=false)
    private CourseEntity courseEntity;

    //---------RELACIONAMENTO DAS CLASSES - FIM

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updateAt;

}
