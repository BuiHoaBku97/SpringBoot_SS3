package startup.vn.coursemanagement.models.dto.response;

public record EnrollmentDetailDto(
        Long id,
        String studentName,
        CourseResponseDto course
) {
}
