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
import startup.vn.coursemanagement.Services.InstructorService;
import startup.vn.coursemanagement.models.dto.ApiResponse;
import startup.vn.coursemanagement.models.dto.request.InstructorCreateRequest;
import startup.vn.coursemanagement.models.dto.request.InstructorRequestDto;
import startup.vn.coursemanagement.models.dto.response.InstructorDetailDto;
import startup.vn.coursemanagement.models.dto.response.InstructorResponseDto;
import startup.vn.coursemanagement.models.entity.Instructor;

import java.util.List;

@Controller
@RequestMapping("/api/instructors")
public class InstructorController {
    private final InstructorService instructorService;

    @Autowired
    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<InstructorResponseDto>>> getInstructors() {
        return ResponseEntity.ok(ApiResponse.success(
                "Instructors retrieved successfully",
                instructorService.findAllInstructors().stream()
                        .map(InstructorResponseDto::fromEntity)
                        .toList()
        ));
    }

    @GetMapping("/details")
    public ResponseEntity<ApiResponse<List<InstructorDetailDto>>> getInstructorDetails() {
        return ResponseEntity.ok(ApiResponse.success(
                "Instructor details retrieved successfully",
                instructorService.getInstructorDetails()
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InstructorResponseDto>> getInstructorById(@PathVariable Long id) {
        Instructor instructor = instructorService.findInstructorById(id);
        return ResponseEntity.ok(ApiResponse.success("Instructor retrieved successfully", InstructorResponseDto.fromEntity(instructor)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<InstructorResponseDto>> createInstructor(@Valid @RequestBody InstructorCreateRequest request) {
        Instructor created = instructorService.createInstructor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(
                "Instructor created successfully",
                InstructorResponseDto.fromEntity(created)
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InstructorResponseDto>> updateInstructor(@PathVariable Long id, @Valid @RequestBody InstructorRequestDto request) {
        Instructor updated = instructorService.updateInstructor(id, request.toEntity());
        return ResponseEntity.ok(ApiResponse.success("Instructor updated successfully", InstructorResponseDto.fromEntity(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstructor(@PathVariable Long id) {
        instructorService.deleteInstructorById(id);
        return ResponseEntity.noContent().build();
    }
}
