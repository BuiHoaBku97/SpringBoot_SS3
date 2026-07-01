package startup.vn.coursemanagement.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import startup.vn.coursemanagement.exceptions.ResourceNotFoundException;
import startup.vn.coursemanagement.models.dto.request.CourseCreateRequest;
import startup.vn.coursemanagement.models.dto.request.CourseUpdateRequest;
import startup.vn.coursemanagement.models.entity.Course;
import startup.vn.coursemanagement.models.entity.Instructor;
import startup.vn.coursemanagement.repositories.CourseRepository;
import startup.vn.coursemanagement.repositories.InstructorRepository;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, InstructorRepository instructorRepository) {
        this.courseRepository = courseRepository;
        this.instructorRepository = instructorRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    }

    public Course createCourse(CourseCreateRequest request) {
        Instructor instructor = instructorRepository.findById(request.instructorId())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));

        Course course = request.toEntity();
        course.setInstructor(instructor);
        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, CourseUpdateRequest request) {
        Instructor instructor = instructorRepository.findById(request.instructorId())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));

        courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        Course course = request.toEntity();
        course.setId(id);
        course.setInstructor(instructor);
        return courseRepository.save(course);
    }

    public void deleteCourseById(Long id) {
        courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        courseRepository.deleteById(id);
    }
}
