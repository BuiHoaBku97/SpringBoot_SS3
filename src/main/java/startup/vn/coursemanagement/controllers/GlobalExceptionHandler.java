package startup.vn.coursemanagement.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import startup.vn.coursemanagement.models.dto.ApiResponse;
import startup.vn.coursemanagement.models.dto.error.ValidationErrorResponseDto;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<ValidationErrorResponseDto>> handleValidation(MethodArgumentNotValidException ex) {
        List<ValidationErrorResponseDto.FieldErrorDto> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ValidationErrorResponseDto.FieldErrorDto(error.getField(), error.getDefaultMessage()))
                .toList();

        return ResponseEntity.badRequest().body(
                ApiResponse.<ValidationErrorResponseDto>failure(
                        "Validation failed",
                        new ValidationErrorResponseDto(
                        HttpStatus.BAD_REQUEST.value(),
                        "Validation failed",
                        fieldErrors
                        )
                )
        );
    }
}
