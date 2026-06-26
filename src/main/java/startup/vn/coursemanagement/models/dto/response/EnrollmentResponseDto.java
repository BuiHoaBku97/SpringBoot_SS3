package startup.vn.coursemanagement.models.dto.response;

import startup.vn.coursemanagement.models.entity.Enrollment;

public record EnrollmentResponseDto(Long id, String studentName, Long courseId) {
    public static EnrollmentResponseDto fromEntity(Enrollment enrollment) {
        return new EnrollmentResponseDto(enrollment.getId(), enrollment.getStudentName(), enrollment.getCourseId());
    }
}
