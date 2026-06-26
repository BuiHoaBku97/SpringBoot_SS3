package startup.vn.coursemanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import startup.vn.coursemanagement.Services.EnrollmentService;
import startup.vn.coursemanagement.models.entity.Enrollment;

import java.util.List;

@Controller
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/enrollments")
    @ResponseBody
    public List<Enrollment> getEnrollments() {
        return enrollmentService.getAllEnrollments();
    }
}
