package br.com.api.gestao_cursos.modulesComponents.courses.repository;

import br.com.api.gestao_cursos.modulesComponents.courses.entities.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, UUID>, PagingAndSortingRepository<CourseEntity, UUID> {
    Optional<CourseEntity> findByTitle(String title);
}
