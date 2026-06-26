package startup.vn.coursemanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import startup.vn.coursemanagement.Services.InstructorService;
import startup.vn.coursemanagement.models.entity.Instructor;

import java.util.List;

@Controller
public class InstructorController {
    private final InstructorService instructorService;

    @Autowired
    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @GetMapping("/instructors")
    @ResponseBody
    public List<Instructor> getInstructors() {
        return instructorService.getAllInstructors();
    }
}
