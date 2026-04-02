package com.lms.controller;

import com.lms.dto.*;
import com.lms.service.CourseService;
import com.lms.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/instructor")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
public class InstructorController {
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    @GetMapping("/courses")
    public ResponseEntity<ApiResponse<List<CourseDTO>>> getMyCourses(Authentication auth) {
        return ResponseEntity.ok(ApiResponse.success("Courses fetched", courseService.getCoursesByInstructor(auth.getName())));
    }

    @PostMapping("/courses")
    public ResponseEntity<ApiResponse<CourseDTO>> createCourse(@RequestBody CourseDTO dto, Authentication auth) {
        return ResponseEntity.ok(ApiResponse.success("Course created", courseService.createCourse(dto, auth.getName())));
    }

    @PutMapping("/courses/{id}")
    public ResponseEntity<ApiResponse<CourseDTO>> updateCourse(@PathVariable Long id, @RequestBody CourseDTO dto, Authentication auth) {
        return ResponseEntity.ok(ApiResponse.success("Course updated", courseService.updateCourse(id, dto, auth.getName())));
    }

    @PostMapping("/courses/{courseId}/lessons")
    public ResponseEntity<ApiResponse<LessonDTO>> addLesson(@PathVariable Long courseId, @RequestBody LessonDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("Lesson added", courseService.addLesson(courseId, dto)));
    }

    @DeleteMapping("/lessons/{lessonId}")
    public ResponseEntity<ApiResponse<Void>> deleteLesson(@PathVariable Long lessonId) {
        courseService.deleteLesson(lessonId);
        return ResponseEntity.ok(ApiResponse.success("Lesson deleted", null));
    }

    @GetMapping("/courses/{courseId}/enrollments")
    public ResponseEntity<ApiResponse<List<EnrollmentDTO>>> getCourseEnrollments(@PathVariable Long courseId) {
        return ResponseEntity.ok(ApiResponse.success("Enrollments fetched", enrollmentService.getCourseEnrollments(courseId)));
    }
}
