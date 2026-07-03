package startup.vn.coursemanagement.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import startup.vn.coursemanagement.models.dto.response.StudentResponseV2;
import startup.vn.coursemanagement.models.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("select new startup.vn.coursemanagement.models.dto.response.StudentResponseV2(s.id, s.name, s.email) " +
            "from Student s " +
            "where (:keyword is null or :keyword = '' " +
            "or lower(s.name) like lower(concat('%', cast(:keyword as string), '%')) " +
            "or lower(s.email) like lower(concat('%', cast(:keyword as string), '%')))")
    Page<StudentResponseV2> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
