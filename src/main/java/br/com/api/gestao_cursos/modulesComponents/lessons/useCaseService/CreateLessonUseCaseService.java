package br.com.api.gestao_cursos.modulesComponents.lessons.useCaseService;

import br.com.api.gestao_cursos.modulesComponents.lessons.dto.LessonsRequestDto;
import br.com.api.gestao_cursos.modulesComponents.lessons.entity.LessonsEntity;
import br.com.api.gestao_cursos.modulesComponents.lessons.repository.LessonRepository;
import br.com.api.gestao_cursos.modulesComponents.modulesCourse.entity.ModuleCourseEntity;
import br.com.api.gestao_cursos.modulesComponents.modulesCourse.repository.ModuleCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@Service
public class CreateLessonUseCaseService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ModuleCourseRepository moduleCourseRepository;

    @Transactional(readOnly = false, rollbackFor = {SQLException.class})
    public LessonsEntity create(UUID idUserRequest, LessonsRequestDto lessonsRequestDto) throws Exception {

        //    Não deve ser possível que:
        Optional<ModuleCourseEntity> moduleCourseEntity = moduleCourseRepository.findById(lessonsRequestDto.getIdModuleCourse());

        //Cadastrar uma lesson onde o instrutor do curso não for o instrutor autenticado
        //Recuperar o ID do instructor associado ao curso e comparar com o do Request
        if (!moduleCourseEntity.get().getCourse().getInstructorId().equals(idUserRequest)){
            throw new Exception("Instructor não autorizado para esta operação.");
        }

        //Cadastrar uma lesson em um módulo que não existe
        if (!moduleCourseEntity.isPresent()){
            throw new Exception("Módulo não está cadastrado no sistema");
        }


        //Verificar se a aula existe no banco, caso exista lança exception, pois não pode ter o mesmo nome no mesmo Módulo.
        if (lessonRepository.findByTitleAndIdModuleCourse
                (lessonsRequestDto.getTitle(), lessonsRequestDto.getIdModuleCourse()).isPresent()){
            throw new Exception("Aula existe no módulo com este título");
        }

        //Não deve Cadastrar algum Lesson, no módulo, em uma posição que já esteja ocupada, ou seja,
        // aulas não podem ter a mesma ordem de exibição, cada posição deve ser única.
        if (lessonRepository.findByIdModuleCourseAndTitleAndDisplayOrder(lessonsRequestDto.getIdModuleCourse(),
                lessonsRequestDto.getTitle(), lessonsRequestDto.getDisplayOrder()).isPresent()){
            throw new Exception("A ordem de exibição desta Lesson já se encontra ocupada.");
        }

        LessonsEntity lessonsEntity = LessonsEntity.builder()
                .idModuleCourse(lessonsRequestDto.getIdModuleCourse())
                .title(lessonsRequestDto.getTitle())
                .displayOrder(lessonsRequestDto.getDisplayOrder())
                .build();


        return lessonRepository.save(lessonsEntity);
    }


}
