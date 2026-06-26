package startup.vn.coursemanagement.models.dto.response;

import startup.vn.coursemanagement.models.entity.Instructor;

public record InstructorResponseDto(Long id, String name, String email) {
    public static InstructorResponseDto fromEntity(Instructor instructor) {
        return new InstructorResponseDto(instructor.getId(), instructor.getName(), instructor.getEmail());
    }
}
