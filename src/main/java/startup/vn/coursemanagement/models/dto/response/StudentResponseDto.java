package startup.vn.coursemanagement.models.dto.response;

import startup.vn.coursemanagement.models.entity.Student;

public record StudentResponseDto(Long id, String name, String email) {
    public static StudentResponseDto fromEntity(Student student) {
        return new StudentResponseDto(student.getId(), student.getName(), student.getEmail());
    }
}
