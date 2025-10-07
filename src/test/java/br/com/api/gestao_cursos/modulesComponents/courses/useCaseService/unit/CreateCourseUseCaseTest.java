package br.com.api.gestao_cursos.modulesComponents.courses.useCaseService.unit;

import br.com.api.gestao_cursos.exceptions.ValidationException;
import br.com.api.gestao_cursos.modulesComponents.courses.dto.CreateCourseRequestDto;
import br.com.api.gestao_cursos.modulesComponents.courses.entities.CourseEntity;
import br.com.api.gestao_cursos.modulesComponents.courses.repository.CourseRepository;
import br.com.api.gestao_cursos.modulesComponents.courses.useCaseService.CreateCourseUseCaseService;
import br.com.api.gestao_cursos.modulesComponents.users.repository.UserRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

/*@ExtendWith:
Usado no JUnit5, que vem embutido na dependência do pom, springBootStartTest e quando preciso de suporte
para fazer injeção de dependências, contexto e etc
Usado para fazer testes de componentes do spring(@Service, @Component, @Repository)
  */
@ExtendWith(MockitoExtension.class)
public class CreateCourseUseCaseTest {
    //Fazer os mocks e os injects aqui:
    //A classe principal recebe esta anotação
    @InjectMocks
    CreateCourseUseCaseService createCourseUseCaseService;
    //Tudo o que a classe principal usar considera filhos e receberá @Mock
    @Mock
    CourseRepository courseRepository;
    @Mock
    UserRepository userRepository;

    @Test
    void shouldBeThrowsExceptionWhenInstructorNotExists(){
        //given
        CreateCourseRequestDto createCourseRequestDto = new CreateCourseRequestDto();
        UUID idInstructor = UUID.randomUUID();
        //when
        when(userRepository.existsById(idInstructor)).thenReturn(false);
        //then
        Exception exception = assertThrows(Exception.class, () -> {
            createCourseUseCaseService.validateData(createCourseRequestDto, String.valueOf(idInstructor));
        });
        assertEquals("Instrutor não existe no sistema. Favor cadastrá-lo!", exception.getMessage());
    }

    @Test
    void shouldThrowsValidationExceptionWhenTitleIsBlank() {
        CreateCourseRequestDto dto = CreateCourseRequestDto.builder()
                .title(" ")
                .description("Description valid")
                .build();

        String instructorId = UUID.randomUUID().toString();

        when(userRepository.existsById(UUID.fromString(instructorId))).thenReturn(true);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            createCourseUseCaseService.validateData(dto, instructorId);
        });

        assertEquals("Curso necessita de ter titulo!", exception.getMessage());
    }

    @Test
    void shouldThrowsValidationExceptionWhenTitleIsEmpty() {
        //Given
        CreateCourseRequestDto createCourseRequestDto = CreateCourseRequestDto.builder()
                .title("")
                .description("Description valid")
                .build();
        String idInstructor = UUID.randomUUID().toString();

        //when
        when(userRepository.existsById(UUID.fromString(idInstructor))).thenReturn(true);

        //then
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            createCourseUseCaseService.validateData(createCourseRequestDto, idInstructor);
        });

        assertEquals("Curso necessita de ter titulo!", exception.getMessage());
        verify(userRepository, times(1)).existsById(any());
    }

    @Test
    void shouldThrowsValidationExceptionWhenDescriptionIsEmpty(){
        //Given
        CreateCourseRequestDto createCourseRequestDto = CreateCourseRequestDto.builder()
                .title("Title valid to test")
                .description("")
                .build();
        UUID idInstructor = UUID.randomUUID();
        //When
        when(userRepository.existsById(idInstructor)).thenReturn(true);
        //Then
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            createCourseUseCaseService.validateData(createCourseRequestDto, String.valueOf(idInstructor));
        });
        assertEquals("Curso necessita de ter descrição!", exception.getMessage());
        verify(userRepository, times(1)).existsById(idInstructor);
    }

    @Test
    void shouldThrowsValidationExceptionWhenDescriptionIsBlank(){
        //Given
        CreateCourseRequestDto createCourseRequestDto = CreateCourseRequestDto.builder()
                .title("Title valid to test")
                .description(" ")
                .build();
        UUID idInstructor = UUID.randomUUID();
        //when
        when(userRepository.existsById(any())).thenReturn(true);
        //then
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            createCourseUseCaseService.validateData(createCourseRequestDto, String.valueOf(idInstructor));
        });
        assertEquals("Curso necessita de ter descrição!", exception.getMessage());
        verify(userRepository, times(1)).existsById(idInstructor);
    }

    @Test
    void shouldThrowsExceptionWhenFindCourseByTitle(){
        //Given
        CreateCourseRequestDto createCourseRequestDto = CreateCourseRequestDto.builder()
                .title("Title Course not Found")
                .description("Course to test valid")
                .build();
        UUID idInstructor = UUID.randomUUID();

        //WHEN
        when(userRepository.existsById(idInstructor)).thenReturn(true);
        when(courseRepository.findByTitle(createCourseRequestDto.getTitle())).thenReturn(Optional.of(new CourseEntity()));

        //then
        Exception exception = assertThrows(Exception.class, () ->{
            createCourseUseCaseService.validateData(createCourseRequestDto, String.valueOf(idInstructor));
        });

        assertEquals("Curso já está cadastrado.", exception.getMessage());
    }

    @Test
    void shouldCreateCourseSuccessfully() throws Exception {
        //Given
        CreateCourseRequestDto createCourseRequestDto = new CreateCourseRequestDto("Course Test","description test");
        String idInstructor = UUID.randomUUID().toString();
        UUID idCourse = UUID.randomUUID();
        CourseEntity courseEntity = CourseEntity.builder()
                .title("Course Test")
                .description("description test")
                .instructorId(UUID.fromString(idInstructor))
                .build();
        CourseEntity courseSaved = CourseEntity.builder()
                .id(idCourse)
                .title("Course Test")
                .description("description test")
                .instructorId(UUID.fromString(idInstructor))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        //when
        when(userRepository.existsById(UUID.fromString(idInstructor))).thenReturn(true);
        when(courseRepository.findByTitle(createCourseRequestDto.getTitle())).thenReturn(Optional.empty());
        when(courseRepository.save(courseEntity)).thenReturn(courseSaved);
        //then
        CourseEntity result = createCourseUseCaseService.validateData(createCourseRequestDto, idInstructor);
        Assertions.assertEquals(result.getInstructorId().toString(), idInstructor);
        Assertions.assertEquals("description test", result.getDescription());
        Assertions.assertEquals("Course Test", result.getTitle());
        verify(courseRepository, times(1)).save(any());

    }



}
