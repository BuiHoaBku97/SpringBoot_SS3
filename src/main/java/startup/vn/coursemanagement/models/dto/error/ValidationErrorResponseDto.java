package startup.vn.coursemanagement.models.dto.error;

import java.util.List;

public record ValidationErrorResponseDto(
        int status,
        String error,
        List<FieldErrorDto> fieldErrors
) {
    public record FieldErrorDto(String field, String message) {
    }
}
