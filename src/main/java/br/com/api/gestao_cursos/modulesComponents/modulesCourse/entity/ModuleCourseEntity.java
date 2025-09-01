package br.com.api.gestao_cursos.modulesComponents.modulesCourse.entity;

import br.com.api.gestao_cursos.modulesComponents.courses.entities.CourseEntity;
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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tb_module_course")
public class ModuleCourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;

    private String description;

    private Integer displayOrder;

    @Column(name = "id_course")
    private UUID idCourse;

    @ManyToOne
    @JoinColumn(name = "id_course", updatable = false, insertable = false)
    private CourseEntity course;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}


