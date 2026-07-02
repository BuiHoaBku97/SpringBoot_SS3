package startup.vn.coursemanagement.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import startup.vn.coursemanagement.models.entity.CourseStatus;
import startup.vn.coursemanagement.models.entity.Course;
import startup.vn.coursemanagement.models.dto.response.CourseResponseV2;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("select new startup.vn.coursemanagement.models.dto.response.CourseResponseV2(c.id, c.title, c.status) " +
            "from Course c where c.status = :status")
    Page<CourseResponseV2> findAllByStatus(@Param("status") CourseStatus status, Pageable pageable);
}
