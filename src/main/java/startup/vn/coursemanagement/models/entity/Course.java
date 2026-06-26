package startup.vn.coursemanagement.models.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Course {
    private Long id;
    private String title;
    private CourseStatus status;
    private Long instructorId;
}
