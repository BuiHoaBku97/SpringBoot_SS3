package startup.vn.coursemanagement.models.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import startup.vn.coursemanagement.models.entity.Enrollment;

public record EnrollCourseRequestDto(
        @NotBlank(message = "studentName is required")
        String studentName,
        @NotNull(message = "courseId is required")
        Long courseId
) {
    public Enrollment toEntity() {
        return Enrollment.builder()
                .studentName(studentName)
                .courseId(courseId)
                .build();
    }
}
