package startup.vn.coursemanagement.repositories;

import org.springframework.stereotype.Repository;
import startup.vn.coursemanagement.models.entity.Enrollment;

import java.util.List;
import java.util.Optional;

@Repository
public class EnrollmentRepository {
    private final List<Enrollment> enrollments = List.of(
            Enrollment.builder().id(1L).studentName("Le Van C").courseId(1L).build(),
            Enrollment.builder().id(2L).studentName("Pham Thi D").courseId(2L).build()
    );

    public List<Enrollment> findAll() {
        return enrollments;
    }

    public Optional<Enrollment> findById(Long id) {
        return enrollments.stream()
                .filter(enrollment -> enrollment.getId().equals(id))
                .findFirst();
    }
}
