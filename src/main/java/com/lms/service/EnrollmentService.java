package com.lms.service;

import com.lms.dto.EnrollmentDTO;
import com.lms.entity.Course;
import com.lms.entity.Enrollment;
import com.lms.entity.User;
import com.lms.repository.CourseRepository;
import com.lms.repository.EnrollmentRepository;
import com.lms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public EnrollmentDTO enroll(Long courseId, String email) {
        User student = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));
        if (enrollmentRepository.existsByStudentAndCourse(student, course))
            throw new RuntimeException("Already enrolled");
        if (course.getStatus() != Course.CourseStatus.APPROVED)
            throw new RuntimeException("Course is not available for enrollment");
        Enrollment enrollment = Enrollment.builder().student(student).course(course).progress(0).build();
        return toDTO(enrollmentRepository.save(enrollment));
    }

    public List<EnrollmentDTO> getMyEnrollments(String email) {
        User student = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return enrollmentRepository.findByStudent(student).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public EnrollmentDTO updateProgress(Long enrollmentId, Integer progress) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
            .orElseThrow(() -> new RuntimeException("Enrollment not found"));
        enrollment.setProgress(Math.min(100, Math.max(0, progress)));
        return toDTO(enrollmentRepository.save(enrollment));
    }

    public List<EnrollmentDTO> getCourseEnrollments(Long courseId) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));
        return enrollmentRepository.findByCourse(course).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public void unenroll(Long courseId, String email) {
        User student = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));
        Enrollment enrollment = enrollmentRepository.findByStudentAndCourse(student, course)
            .orElseThrow(() -> new RuntimeException("Enrollment not found"));
        enrollmentRepository.delete(enrollment);
    }

    public EnrollmentDTO toDTO(Enrollment e) {
        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setId(e.getId());
        dto.setProgress(e.getProgress());
        dto.setEnrolledAt(e.getEnrolledAt());
        if (e.getStudent() != null) { dto.setStudentId(e.getStudent().getId()); dto.setStudentName(e.getStudent().getName()); }
        if (e.getCourse() != null) { dto.setCourseId(e.getCourse().getId()); dto.setCourseTitle(e.getCourse().getTitle()); }
        return dto;
    }
}
