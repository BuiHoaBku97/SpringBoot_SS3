package startup.vn.coursemanagement.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import startup.vn.coursemanagement.exceptions.CourseNotActiveException;
import startup.vn.coursemanagement.exceptions.ResourceNotFoundException;
import startup.vn.coursemanagement.models.dto.response.EnrollmentDetailDto;
import startup.vn.coursemanagement.models.dto.response.CourseResponseDto;
import startup.vn.coursemanagement.models.entity.Course;
import startup.vn.coursemanagement.models.entity.CourseStatus;
import startup.vn.coursemanagement.models.entity.Enrollment;
import startup.vn.coursemanagement.repositories.CourseRepository;
import startup.vn.coursemanagement.repositories.EnrollmentRepository;
import startup.vn.coursemanagement.repositories.InstructorRepository;

import java.util.List;

@Service
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository, CourseRepository courseRepository, InstructorRepository instructorRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.instructorRepository = instructorRepository;
    }

    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    public Enrollment getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
    }

    public Enrollment createEnrollment(Enrollment enrollment) {
        return enrollmentRepository.create(enrollment);
    }

    public Enrollment updateEnrollment(Long id, Enrollment enrollment) {
        Enrollment existing = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
        return enrollmentRepository.update(existing, enrollment);
    }

    public Enrollment deleteEnrollmentById(Long id) {
        Enrollment existing = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
        return enrollmentRepository.delete(existing);
    }

    public EnrollmentDetailDto enrollCourse(Enrollment enrollment){
        var course = courseRepository.findById(enrollment.getCourseId())
                .orElseThrow( () -> new ResourceNotFoundException("Course not found") );

        if ( course.getStatus() != CourseStatus.ACTIVE) {
            throw new CourseNotActiveException("Course is not active");
        }

        instructorRepository.findById(course.getInstructorId())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));

        var newEnrollment = enrollmentRepository.create(enrollment);
        return new EnrollmentDetailDto(
                newEnrollment.getId(),
                newEnrollment.getStudentName(),
                CourseResponseDto.fromEntity(course)
        );
    }
}
