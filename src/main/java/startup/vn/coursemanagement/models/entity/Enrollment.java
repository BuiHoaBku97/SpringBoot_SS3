package startup.vn.coursemanagement.models.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Enrollment {
    private Long id;
    private String studentName;
    private Long courseId;
}
