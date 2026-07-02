package startup.vn.coursemanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import startup.vn.coursemanagement.models.dto.request.StudentCreateRequest;
import startup.vn.coursemanagement.models.entity.Student;
import startup.vn.coursemanagement.repositories.StudentRepository;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(StudentCreateRequest request) {
        Student student = request.toEntity();
        return studentRepository.save(student);
    }
}
