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
import startup.vn.coursemanagement.models.dto.request.InstructorRequestDto;
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
    public ResponseEntity<List<InstructorResponseDto>> getInstructors() {
        return ResponseEntity.ok(instructorService.getAllInstructors().stream()
                .map(InstructorResponseDto::fromEntity)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstructorResponseDto> getInstructorById(@PathVariable Long id) {
        Instructor instructor = instructorService.getInstructorById(id);
        return instructor == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(InstructorResponseDto.fromEntity(instructor));
    }

    @PostMapping
    public ResponseEntity<InstructorResponseDto> createInstructor(@Valid @RequestBody InstructorRequestDto request) {
        Instructor created = instructorService.createInstructor(request.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(InstructorResponseDto.fromEntity(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstructorResponseDto> updateInstructor(@PathVariable Long id, @Valid @RequestBody InstructorRequestDto request) {
        Instructor updated = instructorService.updateInstructor(id, request.toEntity());
        return updated == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(InstructorResponseDto.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<InstructorResponseDto> deleteInstructor(@PathVariable Long id) {
        Instructor deleted = instructorService.deleteInstructorById(id);
        return deleted == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(InstructorResponseDto.fromEntity(deleted));
    }
}
