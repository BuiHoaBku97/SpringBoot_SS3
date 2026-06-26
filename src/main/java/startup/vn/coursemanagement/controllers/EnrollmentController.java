package startup.vn.coursemanagement.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import startup.vn.coursemanagement.Services.EnrollmentService;
import startup.vn.coursemanagement.models.dto.request.EnrollmentRequestDto;
import startup.vn.coursemanagement.models.dto.response.EnrollmentResponseDto;
import startup.vn.coursemanagement.models.entity.Enrollment;

import java.util.List;

@Controller
@RequestMapping("/api/enrollments")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public ResponseEntity<List<EnrollmentResponseDto>> getEnrollments() {
        return ResponseEntity.ok(enrollmentService.getAllEnrollments().stream()
                .map(EnrollmentResponseDto::fromEntity)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentResponseDto> getEnrollmentById(@PathVariable Long id) {
        Enrollment enrollment = enrollmentService.getEnrollmentById(id);
        return enrollment == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(EnrollmentResponseDto.fromEntity(enrollment));
    }

    @PostMapping
    public ResponseEntity<EnrollmentResponseDto> createEnrollment(@Valid @RequestBody EnrollmentRequestDto request) {
        Enrollment created = enrollmentService.createEnrollment(request.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(EnrollmentResponseDto.fromEntity(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnrollmentResponseDto> updateEnrollment(@PathVariable Long id, @Valid @RequestBody EnrollmentRequestDto request) {
        Enrollment updated = enrollmentService.updateEnrollment(id, request.toEntity());
        return updated == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(EnrollmentResponseDto.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EnrollmentResponseDto> deleteEnrollment(@PathVariable Long id) {
        Enrollment deleted = enrollmentService.deleteEnrollmentById(id);
        return deleted == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(EnrollmentResponseDto.fromEntity(deleted));
    }
}
