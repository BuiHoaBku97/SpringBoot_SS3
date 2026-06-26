package startup.vn.coursemanagement.models.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import startup.vn.coursemanagement.models.entity.Course;
import startup.vn.coursemanagement.models.entity.CourseStatus;

public record CourseRequestDto(
        @NotBlank(message = "title is required")
        String title,
        @NotNull(message = "status is required")
        CourseStatus status,
        @NotNull(message = "instructorId is required")
        Long instructorId
) {
    public Course toEntity() {
        return Course.builder()
                .title(title)
                .status(status)
                .instructorId(instructorId)
                .build();
    }
}
