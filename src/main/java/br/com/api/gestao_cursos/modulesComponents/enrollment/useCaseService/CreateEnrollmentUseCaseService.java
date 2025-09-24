package br.com.api.gestao_cursos.modulesComponents.enrollment.useCaseService;

import br.com.api.gestao_cursos.modulesComponents.courses.repository.CourseRepository;
import br.com.api.gestao_cursos.modulesComponents.enrollment.entity.CourseEnrollmentEntity;
import br.com.api.gestao_cursos.modulesComponents.enrollment.repository.CourseEnrollmentRepository;
import br.com.api.gestao_cursos.modulesComponents.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@Service
public class CreateEnrollmentUseCaseService {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseEnrollmentRepository courseEnrollmentRepository;

    @Transactional(readOnly = false, rollbackFor = {SQLException.class})
    public UUID createEnrollment(UUID courseId, UUID studentId){
        //Não deve ser possível que:
        //O Aluno se inscreva se não existir cadastro deste
        if (userRepository.findById(studentId).isEmpty()){
            throw new RuntimeException("Estudante não encontrado no cadastro do sistema.");
        }

        //O Aluno se inscreva em um curso que não está mais disponível;
        if (!courseRepository.existsById(courseId)){
            throw new RuntimeException("Curso não está mais disponível.");
        }

        //O Aluno se inscreva em um curso no qual já está inscrito;
        if (courseEnrollmentRepository.findByCourseIdAndStudentId(courseId, studentId).isPresent()){
            throw new RuntimeException("Aluno já está inscrito neste curso.");
        }

        //Fazer inscrição do student no curso
        var courseEnrollment = CourseEnrollmentEntity.builder().studentId(studentId).courseId(courseId).build();
        CourseEnrollmentEntity courseSaved = courseEnrollmentRepository.save(courseEnrollment);
        return courseSaved.getId();
    }

}
