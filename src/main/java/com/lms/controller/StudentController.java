package com.lms.controller;

import com.lms.dto.*;
import com.lms.service.CourseService;
import com.lms.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    @GetMapping("/courses")
    public ResponseEntity<ApiResponse<List<CourseDTO>>> getAvailableCourses() {
        return ResponseEntity.ok(ApiResponse.success("Courses fetched", courseService.getAllApprovedCourses()));
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<ApiResponse<CourseDTO>> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Course fetched", courseService.getCourseById(id)));
    }

    @GetMapping("/courses/{courseId}/lessons")
    public ResponseEntity<ApiResponse<List<LessonDTO>>> getLessons(@PathVariable Long courseId) {
        return ResponseEntity.ok(ApiResponse.success("Lessons fetched", courseService.getLessonsByCourse(courseId)));
    }

    @PostMapping("/enroll/{courseId}")
    public ResponseEntity<ApiResponse<EnrollmentDTO>> enroll(@PathVariable Long courseId, Authentication auth) {
        return ResponseEntity.ok(ApiResponse.success("Enrolled successfully", enrollmentService.enroll(courseId, auth.getName())));
    }

    @DeleteMapping("/unenroll/{courseId}")
    public ResponseEntity<ApiResponse<Void>> unenroll(@PathVariable Long courseId, Authentication auth) {
        enrollmentService.unenroll(courseId, auth.getName());
        return ResponseEntity.ok(ApiResponse.success("Unenrolled", null));
    }

    @GetMapping("/my-enrollments")
    public ResponseEntity<ApiResponse<List<EnrollmentDTO>>> getMyEnrollments(Authentication auth) {
        return ResponseEntity.ok(ApiResponse.success("Enrollments fetched", enrollmentService.getMyEnrollments(auth.getName())));
    }

    @PutMapping("/enrollments/{id}/progress")
    public ResponseEntity<ApiResponse<EnrollmentDTO>> updateProgress(@PathVariable Long id, @RequestParam Integer progress) {
        return ResponseEntity.ok(ApiResponse.success("Progress updated", enrollmentService.updateProgress(id, progress)));
    }
}
