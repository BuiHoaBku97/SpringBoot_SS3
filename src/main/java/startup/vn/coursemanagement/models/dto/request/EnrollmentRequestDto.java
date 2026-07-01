package startup.vn.coursemanagement.models.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import startup.vn.coursemanagement.models.entity.Course;
import startup.vn.coursemanagement.models.entity.Student;
import startup.vn.coursemanagement.models.entity.StudentEnrollment;

public record EnrollmentRequestDto(
        @NotNull(message = "studentId is required")
        Long studentId,
        @NotNull(message = "courseId is required")
        Long courseId
) {
    public StudentEnrollment toEntity() {
        return StudentEnrollment.builder()
                .student(Student.builder().id(studentId).build())
                .course(Course.builder().id(courseId).build())
                .build();
    }
}
