package startup.vn.coursemanagement.repositories;

import org.springframework.stereotype.Repository;
import startup.vn.coursemanagement.models.entity.Instructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InstructorRepository {
    private final List<Instructor> instructors = new ArrayList<>(List.of(
            Instructor.builder().id(1L).name("Nguyen Van A").email("a@example.com").build(),
            Instructor.builder().id(2L).name("Tran Thi B").email("b@example.com").build()
    ));

    public List<Instructor> findAll() {
        return List.copyOf(instructors);
    }

    public Optional<Instructor> findById(Long id) {
        return instructors.stream()
                .filter(instructor -> instructor.getId().equals(id))
                .findFirst();
    }

    public Instructor create(Instructor instructor) {
        Long nextId = instructors.stream()
                .mapToLong(Instructor::getId)
                .max()
                .orElse(0L) + 1;
        Instructor saved = Instructor.builder()
                .id(nextId)
                .name(instructor.getName())
                .email(instructor.getEmail())
                .build();
        instructors.add(saved);
        return saved;
    }

    public Optional<Instructor> update(Long id, Instructor instructor) {
        for (int i = 0; i < instructors.size(); i++) {
            if (instructors.get(i).getId().equals(id)) {
                Instructor updated = Instructor.builder()
                        .id(id)
                        .name(instructor.getName())
                        .email(instructor.getEmail())
                        .build();
                instructors.set(i, updated);
                return Optional.of(updated);
            }
        }
        return Optional.empty();
    }

    public Optional<Instructor> deleteById(Long id) {
        for (int i = 0; i < instructors.size(); i++) {
            if (instructors.get(i).getId().equals(id)) {
                return Optional.of(instructors.remove(i));
            }
        }
        return Optional.empty();
    }
}
