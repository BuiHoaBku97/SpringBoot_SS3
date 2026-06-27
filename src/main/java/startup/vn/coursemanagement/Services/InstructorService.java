package startup.vn.coursemanagement.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import startup.vn.coursemanagement.exceptions.ResourceNotFoundException;
import startup.vn.coursemanagement.models.dto.response.CourseResponseDto;
import startup.vn.coursemanagement.models.dto.response.InstructorDetailDto;
import startup.vn.coursemanagement.models.entity.Course;
import startup.vn.coursemanagement.models.entity.CourseStatus;
import startup.vn.coursemanagement.models.entity.Instructor;
import startup.vn.coursemanagement.models.entity.Enrollment;
import startup.vn.coursemanagement.repositories.CourseRepository;
import startup.vn.coursemanagement.repositories.EnrollmentRepository;
import startup.vn.coursemanagement.repositories.InstructorRepository;

import java.util.List;
import java.util.Set;

@Service
public class InstructorService {
    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Autowired
    public InstructorService(
            InstructorRepository instructorRepository,
            CourseRepository courseRepository,
            EnrollmentRepository enrollmentRepository
    ) {
        this.instructorRepository = instructorRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public InstructorService(InstructorRepository instructorRepository) {
        this(instructorRepository, new CourseRepository(), new EnrollmentRepository());
    }

    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll();
    }

    public Instructor getInstructorById(Long id) {
        return instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));
    }

    public Instructor createInstructor(Instructor instructor) {
        return instructorRepository.create(instructor);
    }

    public Instructor updateInstructor(Long id, Instructor instructor) {
        Instructor existing = instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));
        return instructorRepository.update(existing, instructor);
    }

    public Instructor deleteInstructorById(Long id) {
        Instructor existing = instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));
        return instructorRepository.delete(existing);
    }

    public List<InstructorDetailDto> getInstructorDetails() {
        Set<Long> courseIdsWithEnrollment = enrollmentRepository.findAll().stream()
                .map(Enrollment::getCourseId)
                .collect(java.util.stream.Collectors.toSet());

        return instructorRepository.findAll().stream()
                .map(instructor -> {
                    List<CourseResponseDto> courses = courseRepository.findAll().stream()
                            .filter(course -> course.getInstructorId().equals(instructor.getId()))
                            .filter(course -> course.getStatus() == CourseStatus.ACTIVE)
                            .filter(course -> courseIdsWithEnrollment.contains(course.getId()))
                            .map(CourseResponseDto::fromEntity)
                            .toList();

                    return new InstructorDetailDto(
                            instructor.getId(),
                            instructor.getName(),
                            instructor.getEmail(),
                            courses
                    );
                })
                .toList();
    }
}
