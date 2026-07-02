package startup.vn.coursemanagement.models.dto.response;

import startup.vn.coursemanagement.models.entity.CourseStatus;

public record CourseResponseV2(
        Long id,
        String title,
        CourseStatus status
) {
}
