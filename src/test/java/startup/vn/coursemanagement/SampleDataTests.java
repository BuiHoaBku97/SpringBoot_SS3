package startup.vn.coursemanagement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import startup.vn.coursemanagement.controllers.SampleDataController;
import startup.vn.coursemanagement.models.dto.ApiResponse;
import startup.vn.coursemanagement.models.dto.response.SampleDataSeedResponse;
import startup.vn.coursemanagement.models.dto.response.StudentSeedResponse;
import startup.vn.coursemanagement.models.entity.Course;
import startup.vn.coursemanagement.models.entity.Instructor;
import startup.vn.coursemanagement.models.entity.Student;
import startup.vn.coursemanagement.repositories.CourseRepository;
import startup.vn.coursemanagement.repositories.InstructorRepository;
import startup.vn.coursemanagement.repositories.StudentRepository;
import startup.vn.coursemanagement.services.SampleDataService;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SampleDataTests {

    private final InstructorRepository instructorRepository = Mockito.mock(InstructorRepository.class);
    private final CourseRepository courseRepository = Mockito.mock(CourseRepository.class);
    private final StudentRepository studentRepository = Mockito.mock(StudentRepository.class);
    private final EntityManager entityManager = Mockito.mock(EntityManager.class);
    private final Query query = Mockito.mock(Query.class);

    @Test
    void seedSampleDataCreatesTwentyInstructorsAndTwentyCourses() {
        AtomicReference<List<Course>> capturedCourses = new AtomicReference<>();
        when(entityManager.createNativeQuery("TRUNCATE TABLE student_enrollments, courses, instructors, students RESTART IDENTITY CASCADE"))
                .thenReturn(query);
        when(query.executeUpdate()).thenReturn(0);
        when(instructorRepository.saveAll(anyList())).thenAnswer(invocation -> {
            List<Instructor> instructors = invocation.getArgument(0);
            AtomicLong id = new AtomicLong(1L);
            instructors.forEach(instructor -> instructor.setId(id.getAndIncrement()));
            return instructors;
        });
        when(courseRepository.saveAll(anyList())).thenAnswer(invocation -> {
            List<Course> courses = invocation.getArgument(0);
            capturedCourses.set(courses);
            AtomicLong id = new AtomicLong(1L);
            courses.forEach(course -> course.setId(id.getAndIncrement()));
            return courses;
        });
        when(studentRepository.saveAll(anyList())).thenAnswer(invocation -> {
            List<Student> students = invocation.getArgument(0);
            AtomicLong id = new AtomicLong(1L);
            students.forEach(student -> student.setId(id.getAndIncrement()));
            return students;
        });

        SampleDataService service = new SampleDataService(
                instructorRepository,
                courseRepository,
                studentRepository,
                entityManager
        );

        SampleDataSeedResponse response = service.seedSampleData();

        assertEquals(20, response.instructorsCreated());
        assertEquals(20, response.coursesCreated());
        assertEquals(20, response.studentsCreated());
        assertNotNull(capturedCourses.get());
        assertEquals(20, capturedCourses.get().size());
        capturedCourses.get().forEach(course -> {
            assertNotNull(course.getInstructor());
            assertNotNull(course.getInstructor().getId());
            assertTrue(course.getInstructor().getId() >= 1L);
            assertTrue(course.getInstructor().getId() <= 20L);
        });
        verify(entityManager).createNativeQuery("TRUNCATE TABLE student_enrollments, courses, instructors, students RESTART IDENTITY CASCADE");
        verify(query).executeUpdate();
    }

    @Test
    void initStudentsCreatesTwentyStudentsOnly() {
        when(entityManager.createNativeQuery("TRUNCATE TABLE student_enrollments, students RESTART IDENTITY CASCADE"))
                .thenReturn(query);
        when(query.executeUpdate()).thenReturn(0);
        when(studentRepository.saveAll(anyList())).thenAnswer(invocation -> {
            List<Student> students = invocation.getArgument(0);
            AtomicLong id = new AtomicLong(1L);
            students.forEach(student -> student.setId(id.getAndIncrement()));
            return students;
        });

        SampleDataService service = new SampleDataService(
                instructorRepository,
                courseRepository,
                studentRepository,
                entityManager
        );

        StudentSeedResponse response = service.initStudents();

        assertEquals(20, response.studentsCreated());
        verify(entityManager).createNativeQuery("TRUNCATE TABLE student_enrollments, students RESTART IDENTITY CASCADE");
        verify(query).executeUpdate();
    }

    @Test
    void controllerReturnsSuccessResponse() {
        SampleDataService service = Mockito.mock(SampleDataService.class);
        when(service.seedSampleData()).thenReturn(new SampleDataSeedResponse(20, 20, 20));
        when(service.initStudents()).thenReturn(new StudentSeedResponse(20));

        SampleDataController controller = new SampleDataController(service);
        ResponseEntity<ApiResponse<SampleDataSeedResponse>> response = controller.seedSampleData();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().success());
        assertEquals("Sample data created successfully", response.getBody().message());
        assertEquals(20, response.getBody().data().instructorsCreated());
        assertEquals(20, response.getBody().data().coursesCreated());
        assertEquals(20, response.getBody().data().studentsCreated());

        ResponseEntity<ApiResponse<StudentSeedResponse>> studentResponse = controller.initStudents();
        assertEquals(HttpStatus.OK, studentResponse.getStatusCode());
        assertEquals(20, studentResponse.getBody().data().studentsCreated());
    }
}
