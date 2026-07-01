package startup.vn.coursemanagement.models.dto.response;

import startup.vn.coursemanagement.models.entity.StudentEnrollment;

public record EnrollmentResponseDto(Long id, Long studentId, Long courseId) {
    public static EnrollmentResponseDto fromEntity(StudentEnrollment enrollment) {
        Long studentId = enrollment.getStudent() != null ? enrollment.getStudent().getId() : null;
        Long courseId = enrollment.getCourse() != null ? enrollment.getCourse().getId() : null;
        return new EnrollmentResponseDto(enrollment.getId(), studentId, courseId);
    }
}
