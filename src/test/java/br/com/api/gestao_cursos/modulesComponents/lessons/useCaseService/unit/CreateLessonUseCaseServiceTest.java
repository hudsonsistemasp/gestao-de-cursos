package br.com.api.gestao_cursos.modulesComponents.lessons.useCaseService.unit;

import br.com.api.gestao_cursos.modulesComponents.courses.entities.CourseEntity;
import br.com.api.gestao_cursos.modulesComponents.lessons.dto.LessonsRequestDto;
import br.com.api.gestao_cursos.modulesComponents.lessons.entity.LessonsEntity;
import br.com.api.gestao_cursos.modulesComponents.lessons.repository.LessonRepository;
import br.com.api.gestao_cursos.modulesComponents.lessons.useCaseService.CreateLessonUseCaseService;
import br.com.api.gestao_cursos.modulesComponents.modulesCourse.entity.ModuleCourseEntity;
import br.com.api.gestao_cursos.modulesComponents.modulesCourse.repository.ModuleCourseRepository;
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
public class CreateLessonUseCaseServiceTest {
    @InjectMocks
    private CreateLessonUseCaseService createLessonUseCaseService;
    @Mock
    private LessonRepository lessonRepository;
    @Mock
    private ModuleCourseRepository moduleCourseRepository;

    /*
    * "Instructor não autorizado para esta operação."
    * "Módulo não está cadastrado no sistema"
    * "Aula existe no módulo com este título"
    * "A ordem de exibição desta Lesson já se encontra ocupada."
    * */

    @Test
    void shouldThrowsWhenModuleIsNotRegistered(){
        //Given
        UUID idUserRequest = UUID.randomUUID();
        UUID idModuleCourse = UUID.randomUUID();
        LessonsRequestDto lessonsRequestDto = LessonsRequestDto.builder().idModuleCourse(idModuleCourse).build();
        //when
        when(moduleCourseRepository.findById(lessonsRequestDto.getIdModuleCourse())).thenReturn(Optional.empty());
        //then
        Exception exception = assertThrows(Exception.class, () -> {
            createLessonUseCaseService.create(idUserRequest, lessonsRequestDto);
        });
        assertEquals("Módulo não está cadastrado no sistema", exception.getMessage());
        verify(moduleCourseRepository, times(1)).findById(any());

    }
    @Test
    void shouldThrowsWhenInstructorNotAuthorized(){
        //given
        UUID idUserRequest = UUID.randomUUID();
        UUID idInstructor = UUID.randomUUID();
        UUID idModuleCourse = UUID.randomUUID();
        LessonsRequestDto lessonsRequestDto = LessonsRequestDto.builder()
                .idModuleCourse(idModuleCourse)
                .title("Lesson title test")
                .displayOrder(1)
                .build();
        //Para inserir no ModuleCourseEntity e cobrir um cenário de teste
        CourseEntity courseEntity = CourseEntity.builder()
                                    .id(UUID.randomUUID())
                                    .title("Course title test")
                                    .description("Course description test")
                                    .createdAt(LocalDateTime.now())
                                    .updatedAt(LocalDateTime.now())
                                    .instructorId(idInstructor)
                                    .build();
        //Receberá o CourseEntity para que o cenário do Instrutor seja coberto
        ModuleCourseEntity moduleCourseEntity = ModuleCourseEntity.builder()
                                                .id(UUID.randomUUID())
                                                .title("Module Title test")
                                                .description("Module Description test")
                                                .displayOrder(1)
                                                .course(courseEntity)
                                                .createdAt(LocalDateTime.now())
                                                .updatedAt(LocalDateTime.now())
                                                .build();

        //when
        when(moduleCourseRepository.findById(lessonsRequestDto.getIdModuleCourse())).thenReturn(Optional.of(moduleCourseEntity));
        //then
        Exception exception = assertThrows(Exception.class, ()->{
            LessonsEntity result = createLessonUseCaseService.create(idUserRequest, lessonsRequestDto);
        });
        assertEquals("Instructor não autorizado para esta operação.", exception.getMessage());
        verify(moduleCourseRepository, times(1)).findById(lessonsRequestDto.getIdModuleCourse());
    }
    @Test
    void shouldThrowsWhenLessonExistsInTheModuleTheSameTitle(){
        //given
        UUID idModuleCourse = UUID.randomUUID();
        UUID idUserRequest = UUID.randomUUID();
        LessonsRequestDto lessonsRequestDto = LessonsRequestDto.builder()
                .idModuleCourse(idModuleCourse)
                .title("title lesson test")
                .build();
        CourseEntity courseEntity = CourseEntity.builder()
                .instructorId(idUserRequest)
                .build();
        ModuleCourseEntity moduleCourseEntity = ModuleCourseEntity.builder()
                .id(idModuleCourse)
                .course(courseEntity)
                .build();
        LessonsEntity lessonsEntity = new LessonsEntity();

        //when
        when(moduleCourseRepository.findById(lessonsRequestDto.getIdModuleCourse())).thenReturn(Optional.of(moduleCourseEntity));
        when(lessonRepository.findByTitleAndIdModuleCourse("title lesson test", lessonsRequestDto.getIdModuleCourse())).thenReturn(Optional.of(lessonsEntity));

        //then
        Exception exception = assertThrows(Exception.class, () -> {
            createLessonUseCaseService.create(idUserRequest, lessonsRequestDto);
        });
        assertEquals("Aula existe no módulo com este título", exception.getMessage());
        verify(moduleCourseRepository, times(1)).findById(any());
        verify(lessonRepository, times(1)).findByTitleAndIdModuleCourse(any(), any());
    }
    @Test
    void shouldThrowsWhenTheDisplayOrderIsBusy(){
        //given
        UUID idModuleCourse = UUID.randomUUID();
        UUID idUserRequest = UUID.randomUUID();
        CourseEntity courseEntity = CourseEntity.builder()
                .instructorId(idUserRequest)
                .build();

        ModuleCourseEntity moduleCourseEntity = ModuleCourseEntity.builder()
                .course(courseEntity)
                .build();
        LessonsRequestDto lessonsRequestDto = LessonsRequestDto.builder()
                .title("title test")
                .idModuleCourse(idModuleCourse)
                .displayOrder(0)
                .build();

        LessonsEntity lessonsEntity = LessonsEntity.builder()
                .idModuleCourse(idModuleCourse)
                .title("title test")
                .displayOrder(0)
                .build();

        //when
        when(moduleCourseRepository.findById(lessonsRequestDto.getIdModuleCourse())).thenReturn(Optional.of(moduleCourseEntity));
        when(lessonRepository.findByTitleAndIdModuleCourse(lessonsRequestDto.getTitle(), lessonsRequestDto.getIdModuleCourse())).thenReturn(Optional.empty());
        when(lessonRepository.findByIdModuleCourseAndTitleAndDisplayOrder(lessonsRequestDto.getIdModuleCourse(),
                lessonsRequestDto.getTitle(), lessonsRequestDto.getDisplayOrder())).thenReturn(Optional.of(lessonsEntity));

        //then
        Exception exception = assertThrows(Exception.class, () ->{
            createLessonUseCaseService.create(idUserRequest, lessonsRequestDto);
        });
        assertEquals("A ordem de exibição desta Lesson já se encontra ocupada.", exception.getMessage());
        verify(moduleCourseRepository, times(1)).findById(any());
        verify(lessonRepository, times(1)).findByTitleAndIdModuleCourse(any(), any());
        verify(lessonRepository, times(1)).findByIdModuleCourseAndTitleAndDisplayOrder(any(), any(), any());
    }
    @Test
    void shouldCreateLessonInTheModuleSuccessfully() throws Exception {
        //given
        UUID idUserRequest = UUID.randomUUID();
        UUID idModuleCourse = UUID.randomUUID();
        LessonsRequestDto lessonsRequestDto = LessonsRequestDto.builder()
                .idModuleCourse(idModuleCourse)
                .title("title test")
                .displayOrder(0)
                .build();

        CourseEntity courseEntity = CourseEntity.builder()
                .instructorId(idUserRequest)
                .build();

        ModuleCourseEntity moduleCourseEntity = ModuleCourseEntity.builder()
                .course(courseEntity)
                .build();

        LessonsEntity lesson = LessonsEntity.builder()
                .idModuleCourse(idModuleCourse)
                .title("title test")
                .displayOrder(0)
                .build();

        UUID idLessonSaved = UUID.randomUUID();
        LessonsEntity lessonSaved = LessonsEntity.builder()
                .id(idLessonSaved)
                .title("title test")
                .displayOrder(0)
                .idModuleCourse(idModuleCourse)
                .moduleCourseEntity(null)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
        //when
        when(moduleCourseRepository.findById(lessonsRequestDto.getIdModuleCourse())).thenReturn(Optional.of(moduleCourseEntity));
        when(lessonRepository.findByTitleAndIdModuleCourse(lessonsRequestDto.getTitle(),lessonsRequestDto.getIdModuleCourse())).thenReturn(Optional.empty());
        when(lessonRepository.findByIdModuleCourseAndTitleAndDisplayOrder(lessonsRequestDto.getIdModuleCourse(),
                lessonsRequestDto.getTitle(), lessonsRequestDto.getDisplayOrder())).thenReturn(Optional.empty());
        when(lessonRepository.save(lesson)).thenReturn(lessonSaved);

        //then
        LessonsEntity result = createLessonUseCaseService.create(idUserRequest, lessonsRequestDto);

        assertEquals(idModuleCourse, result.getIdModuleCourse());
        assertEquals(lessonSaved.getDisplayOrder(), result.getDisplayOrder());
        verify(moduleCourseRepository, times(1)).findById(any());
        verify(lessonRepository, times(1)).findByTitleAndIdModuleCourse(any(), any());
        verify(lessonRepository, times(1)).findByIdModuleCourseAndTitleAndDisplayOrder(any(), any(),any());
        verify(lessonRepository, times(1)).save(any());
    }
}
