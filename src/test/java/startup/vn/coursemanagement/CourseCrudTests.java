package startup.vn.coursemanagement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import startup.vn.coursemanagement.services.CourseService;
import startup.vn.coursemanagement.services.InstructorService;
import startup.vn.coursemanagement.services.StudentEnrollmentService;
import startup.vn.coursemanagement.services.StudentService;
import startup.vn.coursemanagement.controllers.CourseController;
import startup.vn.coursemanagement.controllers.GlobalExceptionHandler;
import startup.vn.coursemanagement.controllers.InstructorController;
import startup.vn.coursemanagement.controllers.StudentController;
import startup.vn.coursemanagement.controllers.StudentEnrollmentController;
import startup.vn.coursemanagement.exceptions.CourseNotActiveException;
import startup.vn.coursemanagement.exceptions.ResourceNotFoundException;
import startup.vn.coursemanagement.models.dto.ApiResponse;
import startup.vn.coursemanagement.models.dto.request.CourseCreateRequest;
import startup.vn.coursemanagement.models.dto.request.CourseUpdateRequest;
import startup.vn.coursemanagement.models.dto.request.InstructorCreateRequest;
import startup.vn.coursemanagement.models.dto.request.StudentCreateRequest;
import startup.vn.coursemanagement.models.dto.request.StudentEnrollmentRequestDto;
import startup.vn.coursemanagement.models.dto.response.CourseResponse;
import startup.vn.coursemanagement.models.dto.response.CourseResponseV2;
import startup.vn.coursemanagement.models.dto.response.InstructorDetailDto;
import startup.vn.coursemanagement.models.dto.response.InstructorResponseDto;
import startup.vn.coursemanagement.models.dto.response.PageResponse;
import startup.vn.coursemanagement.models.dto.response.StudentResponseDto;
import startup.vn.coursemanagement.models.dto.response.StudentResponseV2;
import startup.vn.coursemanagement.models.dto.response.EnrollmentResponseDto;
import startup.vn.coursemanagement.models.entity.Course;
import startup.vn.coursemanagement.models.entity.CourseStatus;
import startup.vn.coursemanagement.models.entity.Instructor;
import startup.vn.coursemanagement.models.entity.Student;
import startup.vn.coursemanagement.models.entity.StudentEnrollment;
import startup.vn.coursemanagement.repositories.CourseRepository;
import startup.vn.coursemanagement.repositories.InstructorRepository;
import startup.vn.coursemanagement.repositories.StudentEnrollmentRepository;
import startup.vn.coursemanagement.repositories.StudentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseCrudTests {

    private final CourseRepository courseRepository = Mockito.mock(CourseRepository.class);
    private final InstructorRepository instructorRepository = Mockito.mock(InstructorRepository.class);
    private final StudentEnrollmentRepository studentEnrollmentRepository = Mockito.mock(StudentEnrollmentRepository.class);
    private final StudentRepository studentRepository = Mockito.mock(StudentRepository.class);

    @Test
    void servicesThrowWhenNotFound() {
        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(instructorRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(studentEnrollmentRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

        InstructorService instructorService = new InstructorService(instructorRepository, courseRepository, studentEnrollmentRepository);
        assertThrows(ResourceNotFoundException.class, () -> instructorService.findInstructorById(999L));

        CourseService courseService = new CourseService(courseRepository, instructorRepository);
        assertThrows(ResourceNotFoundException.class, () -> courseService.getCourseById(999L));

        StudentEnrollmentService enrollmentService = new StudentEnrollmentService(
                studentEnrollmentRepository,
                courseRepository,
                instructorRepository,
                studentRepository
        );
        assertThrows(ResourceNotFoundException.class, () -> enrollmentService.getEnrollmentById(999L));
    }

    @Test
    void globalExceptionHandlerMapsDomainErrorsToHttpResponses() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        var notFound = handler.handleNotFound(new ResourceNotFoundException("Instructor not found"));
        assertEquals(HttpStatus.NOT_FOUND, notFound.getStatusCode());
        assertFalse(notFound.getBody().success());
        assertEquals("Instructor not found", notFound.getBody().message());

        var courseNotActive = handler.handleCourseNotActive(new CourseNotActiveException("Course is not active"));
        assertEquals(HttpStatus.BAD_REQUEST, courseNotActive.getStatusCode());
        assertFalse(courseNotActive.getBody().success());
        assertEquals("Course is not active", courseNotActive.getBody().message());
    }

    @Test
    void controllersReturnExpectedStatusesAndVoidBodiesForCreates() {
        Instructor instructorOne = Instructor.builder().id(1L).name("Nguyen Van A").email("a@example.com").courses(new ArrayList<>()).build();
        Instructor instructorTwo = Instructor.builder().id(2L).name("Tran Thi B").email("b@example.com").courses(new ArrayList<>()).build();
        Course activeCourse = Course.builder().id(1L).title("Spring Boot Basics").status(CourseStatus.ACTIVE).instructor(instructorOne).enrollments(new ArrayList<>()).build();
        Course inactiveCourse = Course.builder().id(2L).title("Java OOP").status(CourseStatus.CANCELLED).instructor(instructorTwo).enrollments(new ArrayList<>()).build();
        Student studentOne = Student.builder().id(1L).name("Le Van C").email("c@example.com").enrollments(new ArrayList<>()).build();
        StudentEnrollment enrollmentOne = StudentEnrollment.builder().id(1L).student(studentOne).course(activeCourse).build();

        when(courseRepository.findById(1L)).thenReturn(Optional.of(activeCourse));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(inactiveCourse));
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());
        when(courseRepository.save(any())).thenAnswer(invocation -> {
            Course saved = invocation.getArgument(0);
            saved.setId(3L);
            return saved;
        });
        when(courseRepository.findAll()).thenReturn(List.of(activeCourse, inactiveCourse));
        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructorOne));
        when(instructorRepository.findById(2L)).thenReturn(Optional.of(instructorTwo));
        when(instructorRepository.findAll()).thenReturn(List.of(instructorOne, instructorTwo));
        when(instructorRepository.save(any())).thenAnswer(invocation -> {
            Instructor saved = invocation.getArgument(0);
            saved.setId(3L);
            return saved;
        });
        when(studentRepository.findById(1L)).thenReturn(Optional.of(studentOne));
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());
        when(studentRepository.save(any())).thenAnswer(invocation -> {
            Student saved = invocation.getArgument(0);
            saved.setId(3L);
            return saved;
        });
        when(studentRepository.findAllByKeyword(any(), any(Pageable.class))).thenAnswer(invocation -> {
            Pageable pageable = invocation.getArgument(1);
            return new PageImpl<>(
                    List.of(
                            new StudentResponseV2(10L, "Student Alpha", "alpha@example.com"),
                            new StudentResponseV2(11L, "Student Beta", "beta@example.com")
                    ),
                    pageable,
                    2
            );
        });
        when(studentEnrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollmentOne));
        when(studentEnrollmentRepository.findById(999L)).thenReturn(Optional.empty());
        when(studentEnrollmentRepository.save(any())).thenAnswer(invocation -> {
            StudentEnrollment saved = invocation.getArgument(0);
            saved.setId(3L);
            return saved;
        });
        when(studentEnrollmentRepository.findAll()).thenReturn(List.of(enrollmentOne));
        when(courseRepository.findAllByFilters(Mockito.any(), Mockito.any(), Mockito.any(Pageable.class))).thenAnswer(invocation -> {
            Pageable pageable = invocation.getArgument(2);
            List<CourseResponseV2> courses = List.of(
                    new CourseResponseV2(10L, "Algorithms", CourseStatus.ACTIVE),
                    new CourseResponseV2(11L, "Databases", CourseStatus.COMPLETED)
            );
            return new PageImpl<>(courses, pageable, 2);
        });

        CourseController courseController = new CourseController(new CourseService(courseRepository, instructorRepository));
        ResponseEntity<ApiResponse<PageResponse<CourseResponseV2>>> pagedCoursesResponse = courseController.getCourses(0, 2, "title", Sort.Direction.ASC, null, CourseStatus.ACTIVE);
        assertEquals(HttpStatus.OK, pagedCoursesResponse.getStatusCode());
        assertTrue(pagedCoursesResponse.getBody().success());
        assertEquals(2, pagedCoursesResponse.getBody().data().items().size());
        assertEquals("Algorithms", pagedCoursesResponse.getBody().data().items().get(0).title());
        assertEquals("Databases", pagedCoursesResponse.getBody().data().items().get(1).title());
        assertEquals(0, pagedCoursesResponse.getBody().data().page());
        assertEquals(2, pagedCoursesResponse.getBody().data().size());
        assertEquals(2, pagedCoursesResponse.getBody().data().totalItems());
        assertEquals(1, pagedCoursesResponse.getBody().data().totalPages());
        assertTrue(pagedCoursesResponse.getBody().data().isLast());

        ResponseEntity<ApiResponse<CourseResponse>> courseResponse = courseController.getCourseById(1L);
        assertEquals(HttpStatus.OK, courseResponse.getStatusCode());
        assertTrue(courseResponse.getBody().success());
        assertEquals(1L, courseResponse.getBody().data().id());
        assertEquals(1L, courseResponse.getBody().data().instructor().id());

        ResponseEntity<ApiResponse<CourseResponse>> createdCourse = courseController.createCourse(
                new CourseCreateRequest("REST API", CourseStatus.ACTIVE, 1L)
        );
        assertEquals(HttpStatus.CREATED, createdCourse.getStatusCode());
        assertTrue(createdCourse.getBody().success());
        assertEquals("Course created successfully", createdCourse.getBody().message());
        assertEquals(3L, createdCourse.getBody().data().id());

        ResponseEntity<ApiResponse<CourseResponse>> updatedCourse = courseController.updateCourse(
                1L,
                new CourseUpdateRequest("REST API Updated", CourseStatus.COMPLETED, 1L)
        );
        assertEquals(HttpStatus.OK, updatedCourse.getStatusCode());
        assertTrue(updatedCourse.getBody().success());
        assertEquals("REST API Updated", updatedCourse.getBody().data().title());
        assertEquals(CourseStatus.COMPLETED, updatedCourse.getBody().data().status());
        assertEquals(1L, updatedCourse.getBody().data().instructor().id());

        InstructorController instructorController = new InstructorController(
                new InstructorService(instructorRepository, courseRepository, studentEnrollmentRepository)
        );
        ResponseEntity<ApiResponse<InstructorResponseDto>> createdInstructor = instructorController.createInstructor(
                new InstructorCreateRequest("New Instructor", "new@example.com")
        );
        assertEquals(HttpStatus.CREATED, createdInstructor.getStatusCode());
        assertTrue(createdInstructor.getBody().success());
        assertEquals(3L, createdInstructor.getBody().data().id());

        ResponseEntity<ApiResponse<List<InstructorDetailDto>>> instructorDetailsResponse = instructorController.getInstructorDetails();
        assertEquals(HttpStatus.OK, instructorDetailsResponse.getStatusCode());
        InstructorDetailDto instructorOneDetails = instructorDetailsResponse.getBody().data().stream()
                .filter(detail -> detail.id().equals(1L))
                .findFirst()
                .orElseThrow();
        assertEquals(1, instructorOneDetails.courses().size());
        assertEquals("Spring Boot Basics", instructorOneDetails.courses().get(0).title());

        StudentController studentController = new StudentController(new StudentService(studentRepository));
        ResponseEntity<ApiResponse<PageResponse<StudentResponseV2>>> studentsResponse = studentController.getStudents(
                0,
                2,
                "name",
                Sort.Direction.ASC,
                "Student"
        );
        assertEquals(HttpStatus.OK, studentsResponse.getStatusCode());
        assertTrue(studentsResponse.getBody().success());
        assertEquals(2, studentsResponse.getBody().data().items().size());
        assertEquals("Student Alpha", studentsResponse.getBody().data().items().get(0).name());

        ResponseEntity<ApiResponse<StudentResponseDto>> createdStudent = studentController.createStudent(
                new StudentCreateRequest("Student A", "student@example.com")
        );
        assertEquals(HttpStatus.CREATED, createdStudent.getStatusCode());
        assertTrue(createdStudent.getBody().success());
        assertEquals(3L, createdStudent.getBody().data().id());

        StudentEnrollmentController enrollmentController = new StudentEnrollmentController(
                new StudentEnrollmentService(studentEnrollmentRepository, courseRepository, instructorRepository, studentRepository)
        );
        ResponseEntity<ApiResponse<EnrollmentResponseDto>> createdEnrollment = enrollmentController.createEnrollment(
                new StudentEnrollmentRequestDto(1L, 1L)
        );
        assertEquals(HttpStatus.CREATED, createdEnrollment.getStatusCode());
        assertTrue(createdEnrollment.getBody().success());
        assertEquals(3L, createdEnrollment.getBody().data().id());

        ResponseEntity<Void> deletedEnrollment = enrollmentController.deleteEnrollment(1L);
        assertEquals(HttpStatus.NO_CONTENT, deletedEnrollment.getStatusCode());
        assertTrue(deletedEnrollment.getBody() == null);
    }

    @Test
    void getPagedCoursesDefaultsAndClampsParameters() {
        when(courseRepository.findAllByFilters(any(), any(), any(Pageable.class))).thenAnswer(invocation -> {
            Pageable pageable = invocation.getArgument(2);
            return new PageImpl<>(List.of(new CourseResponseV2(1L, "Spring Boot Basics", CourseStatus.ACTIVE)), pageable, 1);
        });

        CourseService courseService = new CourseService(courseRepository, instructorRepository);
        PageResponse<CourseResponseV2> response = courseService.getPagedCourses(-5, 0, null, Sort.Direction.DESC, null, CourseStatus.ACTIVE);

        assertEquals(1, response.items().size());
        assertEquals(1L, response.items().get(0).id());
        assertEquals(0, response.page());
        assertEquals(10, response.size());
        assertEquals(1, response.totalItems());
        assertEquals(1, response.totalPages());
        assertTrue(response.isLast());

        var pageableCaptor = org.mockito.ArgumentCaptor.forClass(Pageable.class);
        verify(courseRepository, times(1)).findAllByFilters(Mockito.any(), Mockito.any(), pageableCaptor.capture());
        Pageable pageable = pageableCaptor.getValue();
        assertNotNull(pageable);
        assertEquals(0, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
        assertTrue(pageable.getSort().getOrderFor("id") != null);
        assertEquals(Sort.Direction.DESC, pageable.getSort().getOrderFor("id").getDirection());
    }

    @Test
    void getPagedCoursesUsesInactiveFilterAndKeyword() {
        when(courseRepository.findAllByFilters(any(), any(), any(Pageable.class))).thenAnswer(invocation -> {
            Pageable pageable = invocation.getArgument(2);
            return new PageImpl<>(List.of(new CourseResponseV2(2L, "Inactive Course", CourseStatus.INACTIVE)), pageable, 1);
        });

        CourseService courseService = new CourseService(courseRepository, instructorRepository);
        PageResponse<CourseResponseV2> response = courseService.getPagedCourses(0, 2, "title", Sort.Direction.ASC, "Inactive", CourseStatus.INACTIVE);

        assertEquals(1, response.items().size());
        assertEquals("Inactive Course", response.items().get(0).title());

        var pageableCaptor = org.mockito.ArgumentCaptor.forClass(Pageable.class);
        var statusCaptor = org.mockito.ArgumentCaptor.forClass(CourseStatus.class);
        verify(courseRepository, times(1)).findAllByFilters(Mockito.any(), statusCaptor.capture(), pageableCaptor.capture());
        assertEquals(CourseStatus.INACTIVE, statusCaptor.getValue());
        assertEquals(0, pageableCaptor.getValue().getPageNumber());
        assertEquals(2, pageableCaptor.getValue().getPageSize());
        assertEquals(Sort.Direction.ASC, pageableCaptor.getValue().getSort().getOrderFor("title").getDirection());
    }

    @Test
    void getPagedCoursesWithoutDirectionIsUnsorted() {
        when(courseRepository.findAllByFilters(any(), any(), any(Pageable.class))).thenAnswer(invocation -> {
            Pageable pageable = invocation.getArgument(2);
            return new PageImpl<>(List.of(new CourseResponseV2(3L, "No Sort Course", CourseStatus.ACTIVE)), pageable, 1);
        });

        CourseService courseService = new CourseService(courseRepository, instructorRepository);
        PageResponse<CourseResponseV2> response = courseService.getPagedCourses(0, 5, "title", null, null, null);

        assertEquals(1, response.items().size());
        assertTrue(response.isLast());

        var pageableCaptor = org.mockito.ArgumentCaptor.forClass(Pageable.class);
        verify(courseRepository, times(1)).findAllByFilters(Mockito.any(), Mockito.any(), pageableCaptor.capture());
        assertTrue(pageableCaptor.getValue().getSort().isUnsorted());
    }

    @Test
    void getPagedStudentsWithoutDirectionIsUnsorted() {
        when(studentRepository.findAllByKeyword(any(), any(Pageable.class))).thenAnswer(invocation -> {
            Pageable pageable = invocation.getArgument(1);
            return new PageImpl<>(
                    List.of(new StudentResponseV2(1L, "Student Alpha", "alpha@example.com")),
                    pageable,
                    1
            );
        });

        StudentService studentService = new StudentService(studentRepository);
        PageResponse<StudentResponseV2> response = studentService.getPagedStudents(0, 5, "name", null, null);

        assertEquals(1, response.items().size());
        assertTrue(response.isLast());

        var pageableCaptor = org.mockito.ArgumentCaptor.forClass(Pageable.class);
        verify(studentRepository, times(1)).findAllByKeyword(Mockito.any(), pageableCaptor.capture());
        assertTrue(pageableCaptor.getValue().getSort().isUnsorted());
    }
}
