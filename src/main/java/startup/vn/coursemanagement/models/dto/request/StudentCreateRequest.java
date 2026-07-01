package startup.vn.coursemanagement.models.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import startup.vn.coursemanagement.models.entity.Student;

public record StudentCreateRequest(
        @NotBlank(message = "name is required")
        String name,
        @NotBlank(message = "email is required")
        @Email(message = "email must be valid")
        String email
) {
    public Student toEntity() {
        return Student.builder()
                .name(name)
                .email(email)
                .build();
    }
}
