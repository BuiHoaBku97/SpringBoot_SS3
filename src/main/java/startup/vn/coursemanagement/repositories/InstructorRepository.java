package startup.vn.coursemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import startup.vn.coursemanagement.models.entity.Instructor;


public interface InstructorRepository extends JpaRepository<Instructor, Long> {

}
