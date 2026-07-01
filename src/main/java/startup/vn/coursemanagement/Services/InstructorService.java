package startup.vn.coursemanagement.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import startup.vn.coursemanagement.exceptions.ResourceNotFoundException;
import startup.vn.coursemanagement.models.dto.request.InstructorCreateRequest;
import startup.vn.coursemanagement.models.dto.response.CourseResponse;
import startup.vn.coursemanagement.models.dto.response.InstructorDetailDto;
import startup.vn.coursemanagement.models.entity.Course;
import startup.vn.coursemanagement.models.entity.CourseStatus;
import startup.vn.coursemanagement.models.entity.Instructor;
import startup.vn.coursemanagement.models.entity.StudentEnrollment;
import startup.vn.coursemanagement.mappers.CourseMapper;
import startup.vn.coursemanagement.repositories.CourseRepository;
import startup.vn.coursemanagement.repositories.InstructorRepository;
import startup.vn.coursemanagement.repositories.StudentEnrollmentRepository;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Service
public class InstructorService {
    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final StudentEnrollmentRepository studentEnrollmentRepository;
    private final CourseMapper courseMapper;

    @Autowired
    public InstructorService(
            InstructorRepository instructorRepository,
            CourseRepository courseRepository,
            StudentEnrollmentRepository studentEnrollmentRepository,
            CourseMapper courseMapper
    ) {
        this.instructorRepository = instructorRepository;
        this.courseRepository = courseRepository;
        this.studentEnrollmentRepository = studentEnrollmentRepository;
        this.courseMapper = courseMapper;
    }

    public InstructorService(
            InstructorRepository instructorRepository,
            CourseRepository courseRepository,
            StudentEnrollmentRepository studentEnrollmentRepository
    ) {
        this(instructorRepository, courseRepository, studentEnrollmentRepository, Mappers.getMapper(CourseMapper.class));
    }

    public List<Instructor> findAllInstructors() {
        return instructorRepository.findAll();
    }

    public Instructor findInstructorById(Long id) {
        return instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));
    }

    public Instructor createInstructor(InstructorCreateRequest req) {
        Instructor instructor = Instructor.builder()
                .name(req.name())
                .email(req.email())
                .build();
        return instructorRepository.save(instructor);
    }

    public Instructor updateInstructor(Long id, Instructor instructor) {
        instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));

        instructor.setId(id);
        return instructorRepository.save(instructor);
    }

    public void deleteInstructorById(Long id) {
        Instructor existing = instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));
        instructorRepository.delete(existing);
    }

    public List<InstructorDetailDto> getInstructorDetails() {
        Set<Long> courseIdsWithEnrollment = studentEnrollmentRepository.findAll().stream()
                .map(StudentEnrollment::getCourse)
                .map(Course::getId)
                .collect(java.util.stream.Collectors.toSet());

        return instructorRepository.findAll().stream()
                .map(instructor -> {
                    List<CourseResponse> courses = courseRepository.findAll().stream()
                            .filter(course -> course.getInstructor() != null && course.getInstructor().getId().equals(instructor.getId()))
                            .filter(course -> course.getStatus() == CourseStatus.ACTIVE)
                            .filter(course -> courseIdsWithEnrollment.contains(course.getId()))
                            .map(courseMapper::toDto)
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
