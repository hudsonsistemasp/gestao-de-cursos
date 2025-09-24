package br.com.api.gestao_cursos.modulesComponents.courses.useCaseService;

import br.com.api.gestao_cursos.modulesComponents.courses.dto.CreateCourseListPage;
import br.com.api.gestao_cursos.modulesComponents.courses.dto.CreateCourseRequestDto;
import br.com.api.gestao_cursos.modulesComponents.courses.entities.CourseEntity;
import br.com.api.gestao_cursos.modulesComponents.courses.repository.CourseRepository;
import br.com.api.gestao_cursos.modulesComponents.courses.utils.ConvertEntityToDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListPageCourseUseCaseServiceTest {
    @InjectMocks
    private ListPageCourseUseCaseService listPageCourseUseCaseService;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    ConvertEntityToDto convertEntityToDto;

    @Test
    void shouldReturnCourseListPagedSuccessfully(){
        //given
        int numberOfPage = 0;
        int amountByPage = 5;
        String orderBy = "title";
        String orderAscOrDesc = "desc";
        Sort sort = orderAscOrDesc.equalsIgnoreCase("desc") ? Sort.by(orderBy).descending() : Sort.by(orderBy).ascending();
        UUID idCourse = UUID.randomUUID();
        UUID idInstructor = UUID.randomUUID();
        List<CourseEntity> listCourseEntity = new ArrayList<>();
        CourseEntity courseEntity1 = CourseEntity.builder()
                .id(idCourse)
                .title("Course 1")
                .description("description course 1")
                .instructorId(idInstructor)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        listCourseEntity.add(courseEntity1);
        CourseEntity courseEntity2 = CourseEntity.builder()
                .id(idCourse)
                .title("Course 2")
                .description("description course 2")
                .instructorId(idInstructor)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        //resposta da conversão do Entity para DTO
        listCourseEntity.add(courseEntity2);
        List<CreateCourseRequestDto> listEntityConvertedToDto = List.of(
                new CreateCourseRequestDto("Course 1","description course 1"),
                new CreateCourseRequestDto("Course 2","description course 2")
        );


        //Pageable é um objeto que representa a requisição da paginação(numDepáginas,tamanho,ordenação)
        Pageable pageable = PageRequest.of(numberOfPage,amountByPage, sort);
        //Page já é o resultado paginado, da requisição e contém:
        // a lista dos elementos(getContent());
        //Informação da paginação: (getTotalElements(), getTotalPages()...)
        // e para teste usa-se o PageImpl<>
        Page<CourseEntity> page = new PageImpl<>(listCourseEntity, pageable, listCourseEntity.size());


        //when
        when(courseRepository.findAll(pageable)).thenReturn(page);
        when(convertEntityToDto.toListCourseDto(listCourseEntity)).thenReturn(listEntityConvertedToDto);

        //then
        CreateCourseListPage result = listPageCourseUseCaseService.getAllCourses(numberOfPage,
                amountByPage,orderBy,orderAscOrDesc);

        assertEquals(2, result.getTotalRecords());
        assertEquals(2, result.getContentPage().size());
        assertEquals(5, result.getAmountByPage());
        assertEquals(0, result.getNumPageActual());
    }

    @Test
    void shouldReturnCourseListPagedEmpty(){
        //given
        int numberOfPage = 2;
        int amountByPage = 5;
        String orderBy = "title";
        String orderAscOrDesc = "asc";

        Sort sortBy = orderAscOrDesc.equalsIgnoreCase("ASC") ? Sort.by(orderBy).ascending() : Sort.by(orderBy).descending();
        //Pageable, pois este é o objeto responsável pela requisição
        Pageable pageable = PageRequest.of(numberOfPage, amountByPage, sortBy);
        //Page, é o objeto responsável pelo resultado da requisição feita pelo Pageable
        Page<CourseEntity> courseEntityPageEmpty = new PageImpl<>(List.of(), pageable, 0);

        //when
        when(courseRepository.findAll(pageable)).thenReturn(courseEntityPageEmpty);
        when(convertEntityToDto.toListCourseDto(List.of())).thenReturn(List.of());

        //then
        CreateCourseListPage result = listPageCourseUseCaseService.getAllCourses(numberOfPage, amountByPage, orderBy, orderAscOrDesc);
        assertTrue(result.getContentPage().isEmpty());
        assertEquals(0, result.getTotalPages());
        assertEquals(0, result.getTotalRecords());

    }
}
