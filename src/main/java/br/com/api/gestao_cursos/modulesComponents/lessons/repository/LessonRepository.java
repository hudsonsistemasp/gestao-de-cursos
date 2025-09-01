package br.com.api.gestao_cursos.modulesComponents.lessons.repository;

import br.com.api.gestao_cursos.modulesComponents.lessons.entity.LessonsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LessonRepository extends JpaRepository<LessonsEntity, UUID> {
    Optional<LessonsEntity> findByTitleAndIdModuleCourse(String title, UUID idModuleCourse);
    Optional<LessonsEntity> findByIdModuleCourseAndTitleAndDisplayOrder(UUID idModuleCourse, String title, Integer displayOrder);

}
