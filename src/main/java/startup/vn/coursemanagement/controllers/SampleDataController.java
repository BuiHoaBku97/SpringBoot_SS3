package startup.vn.coursemanagement.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import startup.vn.coursemanagement.models.dto.ApiResponse;
import startup.vn.coursemanagement.models.dto.response.SampleDataSeedResponse;
import startup.vn.coursemanagement.models.dto.response.StudentSeedResponse;
import startup.vn.coursemanagement.services.SampleDataService;

@RestController
@RequestMapping("/api/dev")
public class SampleDataController {
    private final SampleDataService sampleDataService;

    public SampleDataController(SampleDataService sampleDataService) {
        this.sampleDataService = sampleDataService;
    }

    @PostMapping("/sample-data")
    public ResponseEntity<ApiResponse<SampleDataSeedResponse>> seedSampleData() {
        SampleDataSeedResponse response = sampleDataService.seedSampleData();
        return ResponseEntity.ok(ApiResponse.success("Sample data created successfully", response));
    }

    @PostMapping("/sample-students")
    public ResponseEntity<ApiResponse<StudentSeedResponse>> initStudents() {
        StudentSeedResponse response = sampleDataService.initStudents();
        return ResponseEntity.ok(ApiResponse.success("Student sample data created successfully", response));
    }
}
