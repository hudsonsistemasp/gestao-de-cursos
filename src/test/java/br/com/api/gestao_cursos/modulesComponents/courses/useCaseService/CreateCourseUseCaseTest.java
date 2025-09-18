package br.com.api.gestao_cursos.modulesComponents.courses.useCaseService;

import br.com.api.gestao_cursos.modulesComponents.courses.dto.CreateCourseRequestDto;
import br.com.api.gestao_cursos.modulesComponents.courses.repository.CourseRepository;
import br.com.api.gestao_cursos.modulesComponents.users.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    }


}
