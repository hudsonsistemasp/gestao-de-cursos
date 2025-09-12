package br.com.api.gestao_cursos.modulesComponents.modulesCourse.repository;

import br.com.api.gestao_cursos.modulesComponents.modulesCourse.entity.ModuleCourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ModuleCourseRepository extends JpaRepository<ModuleCourseEntity, UUID> {

    //Não deve Cadastrar algum módulo, num curso, em uma posição que já esteja ocupada, ou seja,
    // módulos não podem ter a mesma ordem de exibição, cada posição deve ser única.
    Optional<ModuleCourseEntity> findByDisplayOrder(Integer displayOrder);

    //Não deve Cadastrar algum módulo, num curso, em uma posição que já esteja ocupada, ou seja,
    // módulos não podem ter a mesma ordem de exibição, cada posição deve ser única.
    Optional<ModuleCourseEntity> findByCourseIdAndTitleAndDisplayOrder(UUID courseId, String title, Integer displayOrder);

    //Não deve cadastrar módulos com o mesmo nome para o mesmo curso
    Optional<ModuleCourseEntity> findByTitleAndCourseId(String title, UUID idCourse);


}
