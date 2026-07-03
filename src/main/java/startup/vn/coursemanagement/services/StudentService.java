package startup.vn.coursemanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import startup.vn.coursemanagement.models.dto.request.StudentCreateRequest;
import startup.vn.coursemanagement.models.dto.response.PageResponse;
import startup.vn.coursemanagement.models.dto.response.StudentResponseV2;
import startup.vn.coursemanagement.models.entity.Student;
import startup.vn.coursemanagement.repositories.StudentRepository;

@Service
public class StudentService {
    private static final int DEFAULT_PAGE_SIZE = 10;

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(StudentCreateRequest request) {
        Student student = request.toEntity();
        return studentRepository.save(student);
    }

    public PageResponse<StudentResponseV2> getPagedStudents(int page, int size, String sortBy, Sort.Direction direction, String keyword) {
        int safePage = Math.max(page, 0);
        int safeSize = size > 0 ? size : DEFAULT_PAGE_SIZE;
        String resolvedSortBy = (sortBy == null || sortBy.isBlank()) ? "id" : sortBy;
        String resolvedKeyword = (keyword == null || keyword.isBlank()) ? null : keyword.trim();

        Pageable pageable = direction == null
                ? PageRequest.of(safePage, safeSize)
                : PageRequest.of(safePage, safeSize, Sort.by(direction, resolvedSortBy));

        var mappedPage = studentRepository.findAllByKeyword(resolvedKeyword, pageable);
        return new PageResponse<>(
                mappedPage.getContent(),
                mappedPage.getNumber(),
                mappedPage.getSize(),
                Math.toIntExact(mappedPage.getTotalElements()),
                mappedPage.getTotalPages(),
                mappedPage.isLast()
        );
    }
}
