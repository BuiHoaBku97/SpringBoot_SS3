package startup.vn.coursemanagement.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "student_enrollments")
public class StudentEnrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Course course;
}
