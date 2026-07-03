package startup.vn.coursemanagement.services;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startup.vn.coursemanagement.models.dto.response.SampleDataSeedResponse;
import startup.vn.coursemanagement.models.dto.response.StudentSeedResponse;
import startup.vn.coursemanagement.models.entity.Course;
import startup.vn.coursemanagement.models.entity.CourseStatus;
import startup.vn.coursemanagement.models.entity.Instructor;
import startup.vn.coursemanagement.models.entity.Student;
import startup.vn.coursemanagement.repositories.CourseRepository;
import startup.vn.coursemanagement.repositories.InstructorRepository;
import startup.vn.coursemanagement.repositories.StudentRepository;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Service
public class SampleDataService {
    private static final int SAMPLE_ROW_COUNT = 20;

    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final EntityManager entityManager;

    @Autowired
    public SampleDataService(
            InstructorRepository instructorRepository,
            CourseRepository courseRepository,
            StudentRepository studentRepository,
            EntityManager entityManager
    ) {
        this.instructorRepository = instructorRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public SampleDataSeedResponse seedSampleData() {
        entityManager.createNativeQuery(
                "TRUNCATE TABLE student_enrollments, courses, instructors, students RESTART IDENTITY CASCADE"
        ).executeUpdate();

        List<Instructor> instructors = IntStream.rangeClosed(1, SAMPLE_ROW_COUNT)
                .mapToObj(index -> Instructor.builder()
                        .name("Instructor " + index)
                        .email("instructor" + index + "@example.com")
                        .build())
                .toList();

        List<Instructor> savedInstructors = instructorRepository.saveAll(instructors);

        List<Course> courses = IntStream.rangeClosed(1, SAMPLE_ROW_COUNT)
                .mapToObj(index -> Course.builder()
                        .title("Sample Course " + index)
                        .status(randomCourseStatus())
                        .instructor(randomInstructor(savedInstructors))
                        .build())
                .toList();

        List<Course> savedCourses = courseRepository.saveAll(courses);

        List<Student> students = IntStream.rangeClosed(1, SAMPLE_ROW_COUNT)
                .mapToObj(index -> Student.builder()
                        .name("Student " + index)
                        .email("student" + index + "@example.com")
                        .build())
                .toList();

        List<Student> savedStudents = studentRepository.saveAll(students);

        return new SampleDataSeedResponse(savedInstructors.size(), savedCourses.size(), savedStudents.size());
    }

    @Transactional
    public StudentSeedResponse initStudents() {
        entityManager.createNativeQuery(
                "TRUNCATE TABLE student_enrollments, students RESTART IDENTITY CASCADE"
        ).executeUpdate();

        List<Student> students = IntStream.rangeClosed(1, SAMPLE_ROW_COUNT)
                .mapToObj(index -> Student.builder()
                        .name("Student " + index)
                        .email("student" + index + "@example.com")
                        .build())
                .toList();

        List<Student> savedStudents = studentRepository.saveAll(students);
        return new StudentSeedResponse(savedStudents.size());
    }

    private Instructor randomInstructor(List<Instructor> instructors) {
        return instructors.get(ThreadLocalRandom.current().nextInt(instructors.size()));
    }

    private CourseStatus randomCourseStatus() {
        CourseStatus[] statuses = CourseStatus.values();
        return statuses[ThreadLocalRandom.current().nextInt(statuses.length)];
    }
}
