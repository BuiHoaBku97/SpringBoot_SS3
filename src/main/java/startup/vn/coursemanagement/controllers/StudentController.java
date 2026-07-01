package startup.vn.coursemanagement.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import startup.vn.coursemanagement.Services.StudentService;
import startup.vn.coursemanagement.models.dto.ApiResponse;
import startup.vn.coursemanagement.models.dto.request.StudentCreateRequest;
import startup.vn.coursemanagement.models.dto.response.StudentResponseDto;
import startup.vn.coursemanagement.models.entity.Student;

@Controller
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponseDto>> createStudent(@Valid @RequestBody StudentCreateRequest request) {
        Student created = studentService.createStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(
                "Student created successfully",
                StudentResponseDto.fromEntity(created)
        ));
    }
}
