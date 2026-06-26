package startup.vn.coursemanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import startup.vn.coursemanagement.Services.CourseService;
import startup.vn.coursemanagement.models.entity.Course;

import java.util.List;

@Controller
public class CourseController {
    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/courses")
    @ResponseBody
    public List<Course> getCourses() {
        return courseService.getAllCourses();
    }
}
