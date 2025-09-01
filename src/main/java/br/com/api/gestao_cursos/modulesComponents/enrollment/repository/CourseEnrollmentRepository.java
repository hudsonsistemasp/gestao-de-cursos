package br.com.api.gestao_cursos.modulesComponents.enrollment.repository;

import br.com.api.gestao_cursos.modulesComponents.enrollment.entity.CourseEnrollmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollmentEntity, UUID> {
    Optional<CourseEnrollmentEntity> findByCourseIdAndStudentId(UUID courseId, UUID studentId);
}