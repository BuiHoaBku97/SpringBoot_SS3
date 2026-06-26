package startup.vn.coursemanagement.repositories;

import org.springframework.stereotype.Repository;
import startup.vn.coursemanagement.models.entity.Instructor;

import java.util.List;
import java.util.Optional;

@Repository
public class InstructorRepository {
    private final List<Instructor> instructors = List.of(
            Instructor.builder().id(1L).name("Nguyen Van A").email("a@example.com").build(),
            Instructor.builder().id(2L).name("Tran Thi B").email("b@example.com").build()
    );

    public List<Instructor> findAll() {
        return instructors;
    }

    public Optional<Instructor> findById(Long id) {
        return instructors.stream()
                .filter(instructor -> instructor.getId().equals(id))
                .findFirst();
    }
}
