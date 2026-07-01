package startup.vn.coursemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import startup.vn.coursemanagement.models.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
