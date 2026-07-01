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
import startup.vn.coursemanagement.Services.CourseService;
import startup.vn.coursemanagement.models.dto.ApiResponse;
import startup.vn.coursemanagement.models.dto.request.CourseCreateRequest;
import startup.vn.coursemanagement.models.dto.request.CourseUpdateRequest;
import startup.vn.coursemanagement.models.dto.response.CourseResponseDto;
import startup.vn.coursemanagement.models.entity.Course;

import java.util.List;

@Controller
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseResponseDto>>> getCourses() {
        return ResponseEntity.ok(ApiResponse.success(
                "Courses retrieved successfully",
                courseService.getAllCourses().stream()
                        .map(CourseResponseDto::fromEntity)
                        .toList()
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponseDto>> getCourseById(@PathVariable Long id) {
        Course course = courseService.getCourseById(id);
        return ResponseEntity.ok(ApiResponse.success("Course retrieved successfully", CourseResponseDto.fromEntity(course)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CourseResponseDto>> createCourse(@Valid @RequestBody CourseCreateRequest request) {
        Course created = courseService.createCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(
                "Course created successfully",
                CourseResponseDto.fromEntity(created)
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseUpdateRequest request) {
        courseService.updateCourse(id, request);
        return ResponseEntity.ok(ApiResponse.success("Course updated successfully", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourseById(id);
        return ResponseEntity.noContent().build();
    }
}
