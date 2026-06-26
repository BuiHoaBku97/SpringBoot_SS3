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
import startup.vn.coursemanagement.models.dto.request.CourseRequestDto;
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
    public ResponseEntity<List<CourseResponseDto>> getCourses() {
        return ResponseEntity.ok(courseService.getAllCourses().stream()
                .map(CourseResponseDto::fromEntity)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDto> getCourseById(@PathVariable Long id) {
        Course course = courseService.getCourseById(id);
        return course == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(CourseResponseDto.fromEntity(course));
    }

    @PostMapping
    public ResponseEntity<CourseResponseDto> createCourse(@Valid @RequestBody CourseRequestDto request) {
        Course created = courseService.createCourse(request.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(CourseResponseDto.fromEntity(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDto> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseRequestDto request) {
        Course updated = courseService.updateCourse(id, request.toEntity());
        return updated == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(CourseResponseDto.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CourseResponseDto> deleteCourse(@PathVariable Long id) {
        Course deleted = courseService.deleteCourseById(id);
        return deleted == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(CourseResponseDto.fromEntity(deleted));
    }
}
