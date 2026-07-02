package startup.vn.coursemanagement.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import startup.vn.coursemanagement.models.entity.Course;
import startup.vn.coursemanagement.models.entity.CourseStatus;
import startup.vn.coursemanagement.models.dto.response.CourseResponseV2;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("select new startup.vn.coursemanagement.models.dto.response.CourseResponseV2(c.id, c.title, c.status) " +
            "from Course c " +
            "where (:keyword is null or lower(c.title) like lower(concat('%', :keyword, '%'))) " +
            "and (:status is null or c.status = :status)")
    Page<CourseResponseV2> findAllByFilters(
            @Param("keyword") String keyword,
            @Param("status") CourseStatus status,
            Pageable pageable
    );
}
