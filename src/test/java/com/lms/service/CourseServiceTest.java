package com.lms.service;

import com.lms.dto.CourseDTO;
import com.lms.entity.Course;
import com.lms.entity.User;
import com.lms.repository.CourseRepository;
import com.lms.repository.EnrollmentRepository;
import com.lms.repository.LessonRepository;
import com.lms.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock private CourseRepository courseRepository;
    @Mock private LessonRepository lessonRepository;
    @Mock private UserRepository userRepository;
    @Mock private EnrollmentRepository enrollmentRepository;
    @InjectMocks private CourseService courseService;

    @Test
    void testGetAllApprovedCourses() {
        Course course = Course.builder().id(1L).title("Java").status(Course.CourseStatus.APPROVED).build();
        when(courseRepository.findByStatus(Course.CourseStatus.APPROVED)).thenReturn(List.of(course));
        when(enrollmentRepository.countByCourse(any())).thenReturn(0L);
        List<CourseDTO> result = courseService.getAllApprovedCourses();
        assertEquals(1, result.size());
        assertEquals("Java", result.get(0).getTitle());
    }

    @Test
    void testCreateCourse() {
        String email = "inst@test.com";
        User instructor = User.builder().id(1L).email(email).name("Inst").role(User.Role.INSTRUCTOR).build();
        CourseDTO dto = new CourseDTO(); dto.setTitle("Spring Boot"); dto.setDescription("Learn SB");
        Course saved = Course.builder().id(1L).title("Spring Boot").instructor(instructor).status(Course.CourseStatus.PENDING).build();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(instructor));
        when(courseRepository.save(any())).thenReturn(saved);
        when(enrollmentRepository.countByCourse(any())).thenReturn(0L);
        CourseDTO result = courseService.createCourse(dto, email);
        assertEquals("Spring Boot", result.getTitle());
    }
}
