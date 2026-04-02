package com.lms.controller;

import com.lms.dto.*;
import com.lms.entity.Course;
import com.lms.entity.User;
import com.lms.service.CourseService;
import com.lms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final UserService userService;
    private final CourseService courseService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success("Users fetched", userService.getAllUsers()));
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<ApiResponse<UserDTO>> updateRole(@PathVariable Long id, @RequestParam User.Role role) {
        return ResponseEntity.ok(ApiResponse.success("Role updated", userService.updateUserRole(id, role)));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted", null));
    }

    @GetMapping("/courses")
    public ResponseEntity<ApiResponse<List<CourseDTO>>> getAllCourses() {
        return ResponseEntity.ok(ApiResponse.success("Courses fetched", courseService.getAllCourses()));
    }

    @PutMapping("/courses/{id}/status")
    public ResponseEntity<ApiResponse<CourseDTO>> updateCourseStatus(@PathVariable Long id, @RequestParam Course.CourseStatus status) {
        return ResponseEntity.ok(ApiResponse.success("Status updated", courseService.updateCourseStatus(id, status)));
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok(ApiResponse.success("Course deleted", null));
    }
}
