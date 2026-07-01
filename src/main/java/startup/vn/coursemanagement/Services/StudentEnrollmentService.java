package startup.vn.coursemanagement.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import startup.vn.coursemanagement.exceptions.CourseNotActiveException;
import startup.vn.coursemanagement.exceptions.ResourceNotFoundException;
import startup.vn.coursemanagement.models.dto.response.EnrollmentDetailDto;
import startup.vn.coursemanagement.models.dto.response.CourseResponseDto;
import startup.vn.coursemanagement.models.entity.Course;
import startup.vn.coursemanagement.models.entity.CourseStatus;
import startup.vn.coursemanagement.models.entity.Student;
import startup.vn.coursemanagement.models.entity.StudentEnrollment;
import startup.vn.coursemanagement.repositories.*;

import java.util.List;

@Service
public class StudentEnrollmentService {
    private final StudentEnrollmentRepository studentEnrollmentRepository;
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public StudentEnrollmentService(StudentEnrollmentRepository studentEnrollmentRepository, CourseRepository courseRepository, InstructorRepository instructorRepository, StudentRepository studentRepository) {
        this.studentEnrollmentRepository = studentEnrollmentRepository;
        this.courseRepository = courseRepository;
        this.instructorRepository = instructorRepository;
        this.studentRepository = studentRepository;
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
        StudentEnrollment existing = studentEnrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
        enrollment.setId(id);
        return studentEnrollmentRepository.save(resolveEnrollment(enrollment));
    }

    public void deleteEnrollmentById(Long id) {
        StudentEnrollment existing = studentEnrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
        studentEnrollmentRepository.delete(existing);
    }

    public EnrollmentDetailDto enrollCourse(StudentEnrollment enrollment){
        StudentEnrollment resolvedEnrollment = resolveEnrollment(enrollment);
        Course course = resolvedEnrollment.getCourse();
        Student student = resolvedEnrollment.getStudent();

        course = courseRepository.findById(course.getId())
                .orElseThrow( () -> new ResourceNotFoundException("Course not found") );

        if ( course.getStatus() != CourseStatus.ACTIVE) {
            throw new CourseNotActiveException("Course is not active");
        }

        if (course.getInstructor() == null || course.getInstructor().getId() == null) {
            throw new ResourceNotFoundException("Instructor not found");
        }

        instructorRepository.findById(course.getInstructor().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));

        student = studentRepository.findById(student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        var newEnrollment = studentEnrollmentRepository.save(StudentEnrollment.builder()
                .student(student)
                .course(course)
                .build());
        return new EnrollmentDetailDto(
                newEnrollment.getId(),
                newEnrollment.getStudent().getId(),
                newEnrollment.getStudent().getName(),
                CourseResponseDto.fromEntity(course)
        );
    }

    private StudentEnrollment resolveEnrollment(StudentEnrollment enrollment) {
        Student student = studentRepository.findById(enrollment.getStudent().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        Course course = courseRepository.findById(enrollment.getCourse().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        return StudentEnrollment.builder()
                .student(student)
                .course(course)
                .build();
    }
}
