package startup.vn.coursemanagement.repositories;

import org.springframework.stereotype.Repository;
import startup.vn.coursemanagement.models.entity.Enrollment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class EnrollmentRepository {
    private final List<Enrollment> enrollments = new ArrayList<>(List.of(
            Enrollment.builder().id(1L).studentName("Le Van C").courseId(1L).build(),
            Enrollment.builder().id(2L).studentName("Pham Thi D").courseId(2L).build()
    ));

    public List<Enrollment> findAll() {
        return List.copyOf(enrollments);
    }

    public Optional<Enrollment> findById(Long id) {
        return enrollments.stream()
                .filter(enrollment -> enrollment.getId().equals(id))
                .findFirst();
    }

    public Enrollment create(Enrollment enrollment) {
        Long nextId = enrollments.stream()
                .mapToLong(Enrollment::getId)
                .max()
                .orElse(0L) + 1;
        Enrollment saved = Enrollment.builder()
                .id(nextId)
                .studentName(enrollment.getStudentName())
                .courseId(enrollment.getCourseId())
                .build();
        enrollments.add(saved);
        return saved;
    }

    public Enrollment update(Enrollment existing, Enrollment enrollment) {
        Enrollment updated = Enrollment.builder()
                .id(existing.getId())
                .studentName(enrollment.getStudentName())
                .courseId(enrollment.getCourseId())
                .build();
        enrollments.set(enrollments.indexOf(existing), updated);
        return updated;
    }

    public Enrollment delete(Enrollment existing) {
        enrollments.remove(existing);
        return existing;
    }
}
