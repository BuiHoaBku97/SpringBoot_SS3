package startup.vn.coursemanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import startup.vn.coursemanagement.exceptions.ResourceNotFoundException;
import startup.vn.coursemanagement.models.dto.request.CourseCreateRequest;
import startup.vn.coursemanagement.models.dto.request.CourseUpdateRequest;
import startup.vn.coursemanagement.models.dto.response.CourseResponse;
import startup.vn.coursemanagement.models.dto.response.CourseResponseV2;
import startup.vn.coursemanagement.models.dto.response.PageResponse;
import startup.vn.coursemanagement.models.entity.Course;
import startup.vn.coursemanagement.models.entity.CourseStatus;
import startup.vn.coursemanagement.models.entity.Instructor;
import startup.vn.coursemanagement.mappers.CourseMapper;
import startup.vn.coursemanagement.repositories.CourseRepository;
import startup.vn.coursemanagement.repositories.InstructorRepository;
import org.mapstruct.factory.Mappers;

@Service
public class CourseService {
    private static final int DEFAULT_PAGE_SIZE = 10;

    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final CourseMapper courseMapper;

    @Autowired
    public CourseService(CourseRepository courseRepository, InstructorRepository instructorRepository, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.instructorRepository = instructorRepository;
        this.courseMapper = courseMapper;
    }

    public CourseService(CourseRepository courseRepository, InstructorRepository instructorRepository) {
        this(courseRepository, instructorRepository, Mappers.getMapper(CourseMapper.class));
    }

    public PageResponse<CourseResponseV2> getPagedCourses(
            int page,
            int size,
            String sortBy,
            Sort.Direction direction,
            String keyword,
            CourseStatus status
    ) {
        int safePage = Math.max(page, 0);
        int safeSize = size > 0 ? size : DEFAULT_PAGE_SIZE;

        Pageable pageable = direction == null
                ? PageRequest.of(safePage, safeSize)
                : PageRequest.of(
                        safePage,
                        safeSize,
                        Sort.by(direction, (sortBy == null || sortBy.isBlank()) ? "id" : sortBy)
                );

        String resolvedKeyword = (keyword == null || keyword.isBlank()) ? null : keyword.trim();
        Page<CourseResponseV2> mappedPage = courseRepository.findAllByFilters(resolvedKeyword, status, pageable);
        return new PageResponse<>(
                mappedPage.getContent(),
                mappedPage.getNumber(),
                mappedPage.getSize(),
                Math.toIntExact(mappedPage.getTotalElements()),
                mappedPage.getTotalPages(),
                mappedPage.isLast()
        );
    }

    public CourseResponse getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        return courseMapper.toDto(course);
    }

    public CourseResponse createCourse(CourseCreateRequest request) {
        Instructor instructor = instructorRepository.findById(request.instructorId())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));

        Course course = request.toEntity();
        course.setInstructor(instructor);
        return courseMapper.toDto(courseRepository.save(course));
    }

    public CourseResponse updateCourse(Long id, CourseUpdateRequest request) {
        Instructor instructor = instructorRepository.findById(request.instructorId())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));

        courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        Course course = request.toEntity();
        course.setId(id);
        course.setInstructor(instructor);
        return courseMapper.toDto(courseRepository.save(course));
    }

    public void deleteCourseById(Long id) {
        courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        courseRepository.deleteById(id);
    }
}
