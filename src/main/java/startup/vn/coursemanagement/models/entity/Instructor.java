package startup.vn.coursemanagement.models.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Instructor {
    private Long id;
    private String name;
    private String email;
}
