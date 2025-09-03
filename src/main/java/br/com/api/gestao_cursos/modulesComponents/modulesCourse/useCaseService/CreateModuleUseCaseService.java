package br.com.api.gestao_cursos.modulesComponents.modulesCourse.useCaseService;

import br.com.api.gestao_cursos.exceptions.ForbbidenException;
import br.com.api.gestao_cursos.modulesComponents.courses.entities.CourseEntity;
import br.com.api.gestao_cursos.modulesComponents.courses.repository.CourseRepository;
import br.com.api.gestao_cursos.modulesComponents.modulesCourse.dto.CreateModuleRequestDto;
import br.com.api.gestao_cursos.modulesComponents.modulesCourse.entity.ModuleCourseEntity;
import br.com.api.gestao_cursos.modulesComponents.modulesCourse.repository.ModuleCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@Service
public class CreateModuleUseCaseService {
//    Não deve ser possível que:
//Cadastrar módulos com o mesmo nome no mesmo curso. O sistema deve impedir a duplicação de nomes de módulos dentro do mesmo curso.
//Cadastrar um módulo com dados inválidos.
//Cadastrar um módulo onde o instrutor do curso não for o instrutor autenticado


//Cadastrar um módulo sem associá-lo a um curso. O módulo deve estar vinculado a um curso existente
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModuleCourseRepository moduleCourseRepository;

    @Transactional(readOnly = false, rollbackFor = {SQLException.class})
    public ModuleCourseEntity create(CreateModuleRequestDto createModuleRequestDto, UUID idUserRequest) throws Exception {
        // O usuário deve estar autenticado na plataforma com permissão de instrutor;==colocar no Controller
        // O curso ao qual o módulo será associado, já deve estar cadastrado no sistema;==no courseRepository
        Optional<CourseEntity> course = courseRepository.findById(createModuleRequestDto.getIdCourse());
        if (course.isEmpty()){
            throw new Exception("Curso não existe no sistema");
        }

        // O curso deve pertencer ao instrutor autenticado;1º opção)no courseRepository e comparar o id do curso com o autenticado
        // O curso deve pertencer ao instrutor autenticado;2º opção)pego o ID que vem pela requisição e comparo o que está no optional do course
        if (!course.get().getInstructorId().equals(idUserRequest)){
            throw new ForbbidenException("Instrutor não autorizado para esta operação.");
        }
        
        //Verificar se o módulo existe no sistema com o mesmo nome para o mesmo curso
        var moduleTitleToCourseID = moduleCourseRepository.findByTitleAndCourseId(createModuleRequestDto.getTitle(), createModuleRequestDto.getIdCourse());
        if (moduleTitleToCourseID.isPresent()) {
            throw new Exception("Módulo já está cadastrado no sistema com este Título.");
        }

        //Não deve Cadastrar algum módulo, num curso, em uma posição que já esteja ocupada, ou seja,
        // módulos não podem ter a mesma ordem de exibição, cada posição deve ser única.
        var moduleCourseById = moduleCourseRepository.findByDisplayOrder(createModuleRequestDto.getDisplayOrder());
        if (moduleCourseById.isPresent()){
            throw new Exception("A ordem de exibição já está ocupada por outro módulo.");
        }

        //Não deve Cadastrar algum módulo com o mesmo nome, num curso, em uma posição que já esteja ocupada, ou seja,
        // módulos não podem ter a mesma ordem de exibição, cada posição deve ser única.
        ModuleCourseEntity moduleCourse = moduleCourseRepository.findByCourseIdAndDisplayOrder(createModuleRequestDto.getIdCourse(),
                createModuleRequestDto.getDisplayOrder());
        if (moduleCourse != null) {
            throw new Exception("Módulo existe na posição de exibição informada do curso, favor verificar!");
        }


        //SALVAR O MÓDULO NO CURSO
        //Montar o Entity com o builder
        var moduleCourseEntity = ModuleCourseEntity.builder()
                .idCourse(createModuleRequestDto.getIdCourse())
                .title(createModuleRequestDto.getTitle())
                .description(createModuleRequestDto.getDescription())
                .displayOrder(createModuleRequestDto.getDisplayOrder())
                .build();


        return moduleCourseRepository.save(moduleCourseEntity);
    }
}
