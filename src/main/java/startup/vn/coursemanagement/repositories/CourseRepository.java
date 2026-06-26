package startup.vn.coursemanagement.repositories;

import org.springframework.stereotype.Repository;
import startup.vn.coursemanagement.models.entity.Course;
import startup.vn.coursemanagement.models.entity.CourseStatus;

import java.util.List;
import java.util.Optional;

@Repository
public class CourseRepository {
    private final List<Course> courses = List.of(
            Course.builder().id(1L).title("Spring Boot Basics").status(CourseStatus.ACTIVE).instructorId(1L).build(),
            Course.builder().id(2L).title("Java OOP").status(CourseStatus.DRAFT).instructorId(2L).build()
    );

    public List<Course> findAll() {
        return courses;
    }

    public Optional<Course> findById(Long id) {
        return courses.stream()
                .filter(course -> course.getId().equals(id))
                .findFirst();
    }
}
