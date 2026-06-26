package startup.vn.coursemanagement.models.dto.response;

import startup.vn.coursemanagement.models.entity.Course;
import startup.vn.coursemanagement.models.entity.CourseStatus;

public record CourseResponseDto(Long id, String title, CourseStatus status, Long instructorId) {
    public static CourseResponseDto fromEntity(Course course) {
        return new CourseResponseDto(course.getId(), course.getTitle(), course.getStatus(), course.getInstructorId());
    }
}
