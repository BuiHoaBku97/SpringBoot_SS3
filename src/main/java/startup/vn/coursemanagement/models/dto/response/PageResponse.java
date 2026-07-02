package startup.vn.coursemanagement.models.dto.response;

import java.util.List;

public record PageResponse<T>(
        List<T> items,
        int page,
        int size,
        int totalItems,
        int totalPages,
        boolean isLast
) {
}
