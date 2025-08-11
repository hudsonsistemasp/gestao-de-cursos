package br.com.api.gestao_cursos.repository;

import br.com.api.gestao_cursos.modules.courses.entities.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CourseRepository extends JpaRepository<CourseEntity, UUID> {
}
