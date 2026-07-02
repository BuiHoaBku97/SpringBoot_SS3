package startup.vn.coursemanagement.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import startup.vn.coursemanagement.models.entity.CourseStatus;
import startup.vn.coursemanagement.models.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("select c from Course c where c.status = :status")
    Page<Course> findAllByStatus(@Param("status") CourseStatus status, Pageable pageable);
}
