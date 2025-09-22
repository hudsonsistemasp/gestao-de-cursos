package br.com.api.gestao_cursos.modulesComponents.courses.useCaseService;

import br.com.api.gestao_cursos.modulesComponents.courses.dto.CreateCourseListPage;
import br.com.api.gestao_cursos.modulesComponents.courses.entities.CourseEntity;
import br.com.api.gestao_cursos.modulesComponents.courses.repository.CourseRepository;
import br.com.api.gestao_cursos.modulesComponents.courses.utils.ConvertEntityToDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListPageCourseUseCase {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    ConvertEntityToDto convertEntityToDto;


    @Transactional(readOnly = true)
    public CreateCourseListPage getAllCourses(int numberOfPage, int amountByPage,
                                              String orderBy, String orderAscOrDesc) {

    Sort sortBy = orderAscOrDesc.equalsIgnoreCase("asc") ? Sort.by(orderBy).ascending() : Sort.by(orderBy).descending();
    Pageable pageable = PageRequest.of(numberOfPage, amountByPage, sortBy);
    Page<CourseEntity> courseEntityPage = courseRepository.findAll(pageable);

    CreateCourseListPage courseListPage = new CreateCourseListPage();
    courseListPage.setContentPage(convertEntityToDto.toListCourseDto(courseEntityPage.getContent()));
    courseListPage.setNumPageActual(courseEntityPage.getNumber());
    courseListPage.setAmountByPage(courseEntityPage.getSize());
    courseListPage.setTotalRecords(courseEntityPage.getTotalElements());
    courseListPage.setTotalPages(courseEntityPage.getTotalPages());

    return courseListPage;
    }
}
