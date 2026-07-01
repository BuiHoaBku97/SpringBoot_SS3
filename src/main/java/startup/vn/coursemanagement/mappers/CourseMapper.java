package startup.vn.coursemanagement.mappers;

import org.mapstruct.Mapper;
import startup.vn.coursemanagement.models.dto.response.CourseInstructorResponse;
import startup.vn.coursemanagement.models.dto.response.CourseResponse;
import startup.vn.coursemanagement.models.entity.Course;
import startup.vn.coursemanagement.models.entity.Instructor;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    CourseResponse toDto(Course course);

    CourseInstructorResponse toInstructorDto(Instructor instructor);
}
