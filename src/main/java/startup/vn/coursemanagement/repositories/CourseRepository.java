package startup.vn.coursemanagement.repositories;

import org.springframework.stereotype.Repository;
import startup.vn.coursemanagement.models.entity.Course;
import startup.vn.coursemanagement.models.entity.CourseStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CourseRepository {
    private final List<Course> courses = new ArrayList<>(List.of(
            Course.builder().id(1L).title("Spring Boot Basics").status(CourseStatus.ACTIVE).instructorId(1L).build(),
            Course.builder().id(2L).title("Java OOP").status(CourseStatus.DRAFT).instructorId(2L).build()
    ));

    public List<Course> findAll() {
        return List.copyOf(courses);
    }

    public Optional<Course> findById(Long id) {
        return courses.stream()
                .filter(course -> course.getId().equals(id))
                .findFirst();
    }

    public Course create(Course course) {
        Long nextId = courses.stream()
                .mapToLong(Course::getId)
                .max()
                .orElse(0L) + 1;
        Course saved = Course.builder()
                .id(nextId)
                .title(course.getTitle())
                .status(course.getStatus())
                .instructorId(course.getInstructorId())
                .build();
        courses.add(saved);
        return saved;
    }

    public Optional<Course> update(Long id, Course course) {
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getId().equals(id)) {
                Course updated = Course.builder()
                        .id(id)
                        .title(course.getTitle())
                        .status(course.getStatus())
                        .instructorId(course.getInstructorId())
                        .build();
                courses.set(i, updated);
                return Optional.of(updated);
            }
        }
        return Optional.empty();
    }

    public Optional<Course> deleteById(Long id) {
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getId().equals(id)) {
                return Optional.of(courses.remove(i));
            }
        }
        return Optional.empty();
    }
}
