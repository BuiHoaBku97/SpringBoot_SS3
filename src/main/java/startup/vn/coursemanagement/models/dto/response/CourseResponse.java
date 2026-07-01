package startup.vn.coursemanagement.models.dto.response;

import startup.vn.coursemanagement.models.entity.CourseStatus;

public record CourseResponse(
        Long id,
        String title,
        CourseStatus status,
        CourseInstructorResponse instructor
) {
}
