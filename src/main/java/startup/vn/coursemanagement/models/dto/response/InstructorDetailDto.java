package startup.vn.coursemanagement.models.dto.response;

import java.util.List;

public record InstructorDetailDto(
        Long id,
        String name,
        String email,
        List<CourseResponse> courses
) {
}
