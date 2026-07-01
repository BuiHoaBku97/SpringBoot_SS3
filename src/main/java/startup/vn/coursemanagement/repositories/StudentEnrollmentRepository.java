package startup.vn.coursemanagement.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import startup.vn.coursemanagement.models.entity.StudentEnrollment;

public interface StudentEnrollmentRepository extends JpaRepository<StudentEnrollment, Long> {
}
