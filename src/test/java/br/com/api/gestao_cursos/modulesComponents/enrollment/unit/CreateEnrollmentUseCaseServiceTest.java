package br.com.api.gestao_cursos.modulesComponents.enrollment.unit;

import br.com.api.gestao_cursos.modulesComponents.courses.repository.CourseRepository;
import br.com.api.gestao_cursos.modulesComponents.enrollment.entity.CourseEnrollmentEntity;
import br.com.api.gestao_cursos.modulesComponents.enrollment.repository.CourseEnrollmentRepository;
import br.com.api.gestao_cursos.modulesComponents.enrollment.useCaseService.CreateEnrollmentUseCaseService;
import br.com.api.gestao_cursos.modulesComponents.users.entities.UserEntity;
import br.com.api.gestao_cursos.modulesComponents.users.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateEnrollmentUseCaseServiceTest {
    @InjectMocks
    private CreateEnrollmentUseCaseService createEnrollmentUseCaseService;
    @Mock
    UserRepository userRepository;
    @Mock
    CourseRepository courseRepository;
    @Mock
    CourseEnrollmentRepository courseEnrollmentRepository;

    /*CENÁRIOS:
    * "Estudante não encontrado no cadastro do sistema."
    * "Curso não está mais disponível."
    * "Aluno já está inscrito neste curso."
    * "SUCESSO NA INSCRIÇÃO */

    @Test
    void shouldThrowsWhenStudentNotFound(){
        //given
        UUID studentId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        //WHEN
        when(userRepository.findById(studentId)).thenReturn(Optional.empty());
        //then
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> {
            UUID result = createEnrollmentUseCaseService.createEnrollment(courseId, studentId);
        });

        assertEquals("Estudante não encontrado no cadastro do sistema.", runtimeException.getMessage());
        verify(userRepository, times(1)).findById(studentId);
    }
    @Test
    void shouldThrowsWhenCourseNotAvailable(){
        //given
        UUID courseId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        //WHEN
        when(userRepository.findById(studentId)).thenReturn(Optional.of(new UserEntity()));
        when(courseRepository.existsById(courseId)).thenReturn(false);
        //then
        RuntimeException runtimeException = assertThrows(RuntimeException.class, ()->{
            UUID result = createEnrollmentUseCaseService.createEnrollment(courseId, studentId);
        });
        assertEquals("Curso não está mais disponível.", runtimeException.getMessage());
        verify(userRepository, times(1)).findById(any());
        verify(courseRepository, times(1)).existsById(courseId);
    }
    @Test
    void shouldThrowsWhenStudentIsEnrolledInTheCourse(){
        //given
        UUID courseId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        //when
        when(userRepository.findById(studentId)).thenReturn(Optional.of(new UserEntity()));
        when(courseRepository.existsById(courseId)).thenReturn(true);
        when(courseEnrollmentRepository.findByCourseIdAndStudentId(courseId, studentId)).thenReturn(Optional.of(new CourseEnrollmentEntity()));
        //then
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> {
            UUID result = createEnrollmentUseCaseService.createEnrollment(courseId, studentId);
        });
        assertEquals("Aluno já está inscrito neste curso.", runtimeException.getMessage());
        verify(userRepository, times(1)).findById(any());
        verify(courseRepository, times(1)).existsById(any());
        verify(courseEnrollmentRepository, times(1)).findByCourseIdAndStudentId(any(), any());
    }
    @Test
    void shouldCreateEnrollmentSuccessfully(){
        //given
        UUID studentId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        UUID courseEnrollmentId = UUID.randomUUID();
        String titleCourseEnrollment = "title test Enrollment";
        UUID enrollmentSavedId = UUID.randomUUID();
        var courseEnrollmentRequest = CourseEnrollmentEntity.builder()
                .studentId(studentId)
                .courseId(courseId)
                .build();
        var courseEnrollmentEntityResult = CourseEnrollmentEntity.builder()
                .id(courseEnrollmentId)
                .title(titleCourseEnrollment)
                .studentId(studentId)
                .courseId(courseId)
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
        //WHEN
        when(userRepository.findById(studentId)).thenReturn(Optional.of(new UserEntity()));
        when(courseRepository.existsById(courseId)).thenReturn(true);
        when(courseEnrollmentRepository.findByCourseIdAndStudentId(courseId, studentId)).thenReturn(Optional.empty());
        when(courseEnrollmentRepository.save(courseEnrollmentRequest)).thenReturn(courseEnrollmentEntityResult);
        //then
        UUID result = createEnrollmentUseCaseService.createEnrollment(courseId, studentId);
        assertEquals(courseEnrollmentId, result);

        verify(userRepository,times(1)).findById(any());
        verify(courseRepository,times(1)).existsById(any());
        verify(courseEnrollmentRepository, times(1)).findByCourseIdAndStudentId(any(), any());

    }

}
