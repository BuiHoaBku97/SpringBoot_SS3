package startup.vn.coursemanagement.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import startup.vn.coursemanagement.exceptions.CourseNotActiveException;
import startup.vn.coursemanagement.exceptions.ResourceNotFoundException;
import startup.vn.coursemanagement.models.dto.response.EnrollmentDetailDto;
import startup.vn.coursemanagement.models.entity.Course;
import startup.vn.coursemanagement.models.entity.CourseStatus;
import startup.vn.coursemanagement.models.entity.Student;
import startup.vn.coursemanagement.models.entity.StudentEnrollment;
import startup.vn.coursemanagement.mappers.CourseMapper;
import startup.vn.coursemanagement.repositories.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Service
public class StudentEnrollmentService {
    private final StudentEnrollmentRepository studentEnrollmentRepository;
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final StudentRepository studentRepository;
    private final CourseMapper courseMapper;

    @Autowired
    public StudentEnrollmentService(StudentEnrollmentRepository studentEnrollmentRepository, CourseRepository courseRepository, InstructorRepository instructorRepository, StudentRepository studentRepository, CourseMapper courseMapper) {
        this.studentEnrollmentRepository = studentEnrollmentRepository;
        this.courseRepository = courseRepository;
        this.instructorRepository = instructorRepository;
        this.studentRepository = studentRepository;
        this.courseMapper = courseMapper;
    }

    public StudentEnrollmentService(StudentEnrollmentRepository studentEnrollmentRepository, CourseRepository courseRepository, InstructorRepository instructorRepository, StudentRepository studentRepository) {
        this(studentEnrollmentRepository, courseRepository, instructorRepository, studentRepository, Mappers.getMapper(CourseMapper.class));
    }


    public List<StudentEnrollment> getAllEnrollments() {
        return studentEnrollmentRepository.findAll();
    }

    public StudentEnrollment getEnrollmentById(Long id) {
        return studentEnrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
    }

    public StudentEnrollment createEnrollment(StudentEnrollment enrollment) {
        return studentEnrollmentRepository.save(resolveEnrollment(enrollment));
    }

    public StudentEnrollment updateEnrollment(Long id, StudentEnrollment enrollment) {
        studentEnrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
        StudentEnrollment resolvedEnrollment = resolveEnrollment(enrollment);
        resolvedEnrollment.setId(id);
        return studentEnrollmentRepository.save(resolvedEnrollment);
    }

    public void deleteEnrollmentById(Long id) {
        StudentEnrollment existing = studentEnrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
        studentEnrollmentRepository.delete(existing);
    }

    public StudentEnrollment enrollStudent(Long studentId, Long courseId) {
        StudentEnrollment enrollment = buildResolvedEnrollment(studentId, courseId);
        Course course = enrollment.getCourse();
        Student student = enrollment.getStudent();

        if (course.getStatus() != CourseStatus.ACTIVE) {
            throw new CourseNotActiveException("Course is not active");
        }

        if (course.getInstructor() == null || course.getInstructor().getId() == null) {
            throw new ResourceNotFoundException("Instructor not found");
        }

        instructorRepository.findById(course.getInstructor().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));

        studentRepository.findById(student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        return studentEnrollmentRepository.save(StudentEnrollment.builder()
                .student(student)
                .course(course)
                .build());
    }

    public EnrollmentDetailDto enrollCourse(StudentEnrollment enrollment){
        StudentEnrollment newEnrollment = enrollStudent(enrollment.getStudent().getId(), enrollment.getCourse().getId());
        return new EnrollmentDetailDto(
                newEnrollment.getId(),
                newEnrollment.getStudent().getId(),
                newEnrollment.getStudent().getName(),
                courseMapper.toDto(newEnrollment.getCourse())
        );
    }

    private StudentEnrollment resolveEnrollment(StudentEnrollment enrollment) {
        return buildResolvedEnrollment(enrollment.getStudent().getId(), enrollment.getCourse().getId());
    }

    private StudentEnrollment buildResolvedEnrollment(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        return StudentEnrollment.builder()
                .student(student)
                .course(course)
                .build();
    }
}
