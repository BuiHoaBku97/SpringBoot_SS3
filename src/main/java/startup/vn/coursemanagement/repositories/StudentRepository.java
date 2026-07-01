package startup.vn.coursemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import startup.vn.coursemanagement.models.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
