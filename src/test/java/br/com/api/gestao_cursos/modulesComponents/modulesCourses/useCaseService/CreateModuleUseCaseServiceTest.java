package br.com.api.gestao_cursos.modulesComponents.modulesCourses.useCaseService;

import br.com.api.gestao_cursos.exceptions.ForbbidenException;
import br.com.api.gestao_cursos.modulesComponents.courses.entities.CourseEntity;
import br.com.api.gestao_cursos.modulesComponents.courses.repository.CourseRepository;
import br.com.api.gestao_cursos.modulesComponents.modulesCourse.dto.CreateModuleRequestDto;
import br.com.api.gestao_cursos.modulesComponents.modulesCourse.entity.ModuleCourseEntity;
import br.com.api.gestao_cursos.modulesComponents.modulesCourse.repository.ModuleCourseRepository;
import br.com.api.gestao_cursos.modulesComponents.modulesCourse.useCaseService.CreateModuleUseCaseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateModuleUseCaseServiceTest {

    @InjectMocks
    CreateModuleUseCaseService createModuleUseCaseService;
    @Mock
    CourseRepository courseRepository;
    @Mock
    ModuleCourseRepository moduleCourseRepository;

    @Test
    void shouldThrowsExceptionWhenCourseDoesNotExists(){
        //Given
        UUID idCourse = UUID.randomUUID();
        UUID idRequest = UUID.randomUUID();
        CreateModuleRequestDto createModuleRequestDto = CreateModuleRequestDto.builder().idCourse(idCourse).build();
        //when
        when(courseRepository.findById(idCourse)).thenReturn(Optional.empty());
        //then
        Exception exception = assertThrows(Exception.class, () -> {
            createModuleUseCaseService.create(createModuleRequestDto, idRequest);
        });
        assertEquals("Curso não existe no sistema", exception.getMessage());
        verify(courseRepository, times(1)).findById(any());
    }

    @Test
    void shouldThrowsExceptionWhenInstructorIdIsNotAuthorized(){
        //Given
        UUID idUserRequest = UUID.randomUUID();//Id de quem está fazendo a requisição no momento
        UUID idInstructor = UUID.randomUUID();//id do instrutor que cadastrou um curso no Course Entity
        UUID idCourse = UUID.randomUUID();//id do curso para setar no Course Entity
        CreateModuleRequestDto createModuleRequestDto = CreateModuleRequestDto.builder().idCourse(idCourse).build();
        CourseEntity courseEntity = CourseEntity.builder()
                .id(idCourse)
                .instructorId(idInstructor)
                .build();
        //When
        when(courseRepository.findById(idCourse)).thenReturn(Optional.of(courseEntity));

        //then
        ForbbidenException forbbidenException = assertThrows(ForbbidenException.class, () -> {
           createModuleUseCaseService.create(createModuleRequestDto, idUserRequest);
        });

        assertEquals("Instrutor não autorizado para esta operação.",forbbidenException.getMessage());

        verify(courseRepository, times(1)).findById(idCourse);
    }

    @Test
    void shouldThrowsExceptionWhenModuleCourseExistsByTitleAndCourseId(){
        //given
        UUID idCourse = UUID.randomUUID();
        String titleModuleCourse = "Test";
        CreateModuleRequestDto createModuleRequestDto = CreateModuleRequestDto.builder().idCourse(idCourse).title(titleModuleCourse).build();
        UUID idUserRequest = UUID.randomUUID();
        CourseEntity course = CourseEntity.builder().id(idCourse).instructorId(idUserRequest).build();
        //when
        when(courseRepository.findById(idCourse)).thenReturn(Optional.of(course));
        when(moduleCourseRepository.findByTitleAndCourseId(titleModuleCourse, idCourse)).thenReturn(Optional.of(new ModuleCourseEntity()));
        //then
        Exception exception = assertThrows(Exception.class, () -> {
            createModuleUseCaseService.create(createModuleRequestDto, idUserRequest);
        });
        assertEquals("Módulo já está cadastrado no sistema com este Título para este curso!", exception.getMessage());
        verify(moduleCourseRepository, times(1)).findByTitleAndCourseId(any(), any());
    }

    @Test
    void shouldThrowsExceptionWhenModuleDisplayPositionIsOccupied(){
        //given
        UUID idCourse = UUID.randomUUID();
        CreateModuleRequestDto createModuleRequestDto = CreateModuleRequestDto.builder().idCourse(idCourse).displayOrder(1).build();
        UUID idUserRequest = UUID.randomUUID();
        CourseEntity course = CourseEntity.builder().id(idCourse).instructorId(idUserRequest).build();
        ModuleCourseEntity moduleCourse = ModuleCourseEntity.builder().displayOrder(1).build();

        //when
        when(courseRepository.findById(idCourse)).thenReturn(Optional.of(course));
        when(moduleCourseRepository.findByDisplayOrder(1)).thenReturn(Optional.of(moduleCourse));

        //then
        Exception exception = assertThrows(Exception.class, () -> {
            createModuleUseCaseService.create(createModuleRequestDto, idUserRequest);
        });
        assertEquals("A ordem de exibição já está ocupada por outro módulo.", exception.getMessage());
        verify(courseRepository, times(1)).findById(any());
        verify(moduleCourseRepository, times(1)).findByDisplayOrder(1);

    }

    @Test
    void shouldCreateModuleSuccessfully() throws Exception {
        UUID idCourse = UUID.randomUUID();
        UUID idUser = UUID.randomUUID();
        CreateModuleRequestDto dto = new CreateModuleRequestDto(idCourse, "Novo Módulo", "Descrição", 1);

        CourseEntity course = new CourseEntity();
        course.setInstructorId(idUser);

        ModuleCourseEntity expectedModule = ModuleCourseEntity.builder()
                .idCourse(idCourse)
                .title("Novo Módulo")
                .description("Descrição")
                .displayOrder(1)
                .build();

        when(courseRepository.findById(idCourse)).thenReturn(Optional.of(course));
        when(moduleCourseRepository.findByTitleAndCourseId("Novo Módulo", idCourse)).thenReturn(Optional.empty());
        when(moduleCourseRepository.findByDisplayOrder(1)).thenReturn(Optional.empty());
        when(moduleCourseRepository.save(any(ModuleCourseEntity.class))).thenReturn(expectedModule);

        ModuleCourseEntity result = createModuleUseCaseService.create(dto, idUser);

        assertEquals(expectedModule.getTitle(), result.getTitle());
    }

    @Test
    void shouldCreateModuleCourseSuccessfull() throws Exception {
        //given
        UUID idCourse = UUID.randomUUID();
        UUID idUserRequest = UUID.randomUUID();
        CourseEntity courseEntity = CourseEntity.builder().id(idCourse).instructorId(idUserRequest).build();
        CreateModuleRequestDto createModuleRequestDto = CreateModuleRequestDto.builder()
                .idCourse(idCourse)
                .title("module test")
                .description("description test")
                .displayOrder(1)
                .build();
        ModuleCourseEntity expectedModuleCourseEntity = returnModuleCourseEntitySaved();
        //when
        when(courseRepository.findById(idCourse)).thenReturn(Optional.of(courseEntity));
        when(moduleCourseRepository.findByTitleAndCourseId("module test", idCourse)).thenReturn(Optional.empty());
        when(moduleCourseRepository.findByDisplayOrder(1)).thenReturn(Optional.empty());
        when(moduleCourseRepository.save(any(ModuleCourseEntity.class))).thenReturn(expectedModuleCourseEntity);
        //then
        ModuleCourseEntity result = createModuleUseCaseService.create(createModuleRequestDto, idUserRequest);
        assertEquals(expectedModuleCourseEntity.getTitle(), result.getTitle());
        verify(courseRepository, times(1)).findById(idCourse);
        verify(moduleCourseRepository, times(1)).findByTitleAndCourseId("module test", idCourse);
        verify(moduleCourseRepository, times(1)).findByDisplayOrder(1);
        verify(moduleCourseRepository, times(1)).save(any());
    }

    @Test
    void shouldBeAbletoCreateNewModuleCourse(){

        //Given
        //Olhando o Service, Na criação de um module do curso, preciso de um ModuleRequestDto e de um idUserRequest
        //Criando objeto ModuleRequestDto
        var createModuleRequestDto = returnCreateModuleRequestDto();
        UUID idCourse = createModuleRequestDto.getIdCourse();
        CourseEntity respostaCourseEntity = returnCourseEntity();
        UUID userIdRequest = respostaCourseEntity.getInstructorId();

        // when
        //Para que a chamada do service abaixo não dê null, para isso preciso mockar a chamada ao repository
        //Mas o teste unitário não tem a finalidade de testar chamada a banco de dados, por isso mockar é a solução

        //1° validação que há no service: Verificar se o curso existe e mockar isso
        when(courseRepository.findById(idCourse)).thenReturn(Optional.of(respostaCourseEntity));
        //2° o curso tem que pertencer ao instructor autenticado: o ID que vem pela requisição tem que ser = ao que existe no curso

        //3° O módulo não pode existir no sitema com o mesmo nome para o mesmo curso
        when(moduleCourseRepository.findByTitleAndCourseId("title module test",
                createModuleRequestDto.getIdCourse())).thenReturn(Optional.empty());
        //4° Não deve Cadastrar algum módulo, num curso, em uma posição que já esteja ocupada
        when(moduleCourseRepository.findByDisplayOrder(1))
                .thenReturn(Optional.empty());

        //Mockar o ENTITY que vai ser retornado do bando de dados após ter salvado o Dto no repository
        ModuleCourseEntity moduleCourseEntitySaved = returnModuleCourseEntitySaved();
        when(moduleCourseRepository.save(any())).thenReturn(moduleCourseEntitySaved);

        try {
            ModuleCourseEntity resultado = createModuleUseCaseService.create(createModuleRequestDto, userIdRequest);
            //Then
            assertNotNull(resultado);
            verify(courseRepository, times(1)).findById(any());
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    private static CreateModuleRequestDto returnCreateModuleRequestDto(){
        UUID idCourse = UUID.randomUUID();
        return CreateModuleRequestDto.builder()
                .idCourse(idCourse)
                .title("title module test")
                .description("description test")
                .displayOrder(1)
                .build();
    }

    private static ModuleCourseEntity returnModuleCourseEntitySaved(){

        return ModuleCourseEntity.builder()
                .id(UUID.randomUUID())
                .idCourse(UUID.randomUUID())
                .title("module test")
                .description("description test")
                .displayOrder(1)
                .course(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private static CourseEntity returnCourseEntity(){
        UUID idCourse = UUID.randomUUID();
        UUID idInstructor = UUID.randomUUID();

        return CourseEntity.builder()
                .id(idCourse)
                .title("Curso Entity test")
                .description("Curso para aplicar test")
                .instructorId(idInstructor)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
