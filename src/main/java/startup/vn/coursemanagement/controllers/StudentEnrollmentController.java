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
import startup.vn.coursemanagement.Services.StudentEnrollmentService;
import startup.vn.coursemanagement.models.dto.ApiResponse;
import startup.vn.coursemanagement.models.dto.request.EnrollCourseRequestDto;
import startup.vn.coursemanagement.models.dto.request.EnrollmentRequestDto;
import startup.vn.coursemanagement.models.dto.response.EnrollmentDetailDto;
import startup.vn.coursemanagement.models.dto.response.EnrollmentResponseDto;
import startup.vn.coursemanagement.models.entity.StudentEnrollment;

import java.util.List;

@Controller
@RequestMapping("/api/enrollments")
public class StudentEnrollmentController {
    private final StudentEnrollmentService enrollmentService;

    @Autowired
    public StudentEnrollmentController(StudentEnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EnrollmentResponseDto>>> getEnrollments() {
        return ResponseEntity.ok(ApiResponse.success(
                "Enrollments retrieved successfully",
                enrollmentService.getAllEnrollments().stream()
                        .map(EnrollmentResponseDto::fromEntity)
                        .toList()
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EnrollmentResponseDto>> getEnrollmentById(@PathVariable Long id) {
        StudentEnrollment enrollment = enrollmentService.getEnrollmentById(id);
        return ResponseEntity.ok(ApiResponse.success("Enrollment retrieved successfully", EnrollmentResponseDto.fromEntity(enrollment)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EnrollmentResponseDto>> createEnrollment(@Valid @RequestBody EnrollmentRequestDto request) {
        StudentEnrollment created = enrollmentService.createEnrollment(request.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(
                "Enrollment created successfully",
                EnrollmentResponseDto.fromEntity(created)
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EnrollmentResponseDto>> updateEnrollment(@PathVariable Long id, @Valid @RequestBody EnrollmentRequestDto request) {
        StudentEnrollment updated = enrollmentService.updateEnrollment(id, request.toEntity());
        return ResponseEntity.ok(ApiResponse.success("Enrollment updated successfully", EnrollmentResponseDto.fromEntity(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollmentById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/enroll-course")
    public ResponseEntity<ApiResponse<EnrollmentDetailDto>> enrollCourse(@Valid @RequestBody EnrollCourseRequestDto enrollCourseRequestDto) {
        var response = enrollmentService.enrollCourse(enrollCourseRequestDto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Course enrolled successfully", response));
    }
}
