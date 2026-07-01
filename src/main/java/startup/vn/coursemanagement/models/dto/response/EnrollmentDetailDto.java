package startup.vn.coursemanagement.models.dto.response;

public record EnrollmentDetailDto(
        Long id,
        Long studentId,
        String studentName,
        CourseResponseDto course
) {
}
