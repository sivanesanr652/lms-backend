package com.lms.service;

import com.lms.dto.CourseDTO;
import com.lms.dto.LessonDTO;
import com.lms.entity.Course;
import com.lms.entity.Lesson;
import com.lms.entity.User;
import com.lms.repository.CourseRepository;
import com.lms.repository.EnrollmentRepository;
import com.lms.repository.LessonRepository;
import com.lms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;

    public List<CourseDTO> getAllApprovedCourses() {
        return courseRepository.findByStatus(Course.CourseStatus.APPROVED)
            .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public CourseDTO getCourseById(Long id) {
        return courseRepository.findById(id).map(this::toDTO)
            .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    public CourseDTO createCourse(CourseDTO dto, String email) {
        User instructor = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        Course course = Course.builder()
            .title(dto.getTitle())
            .description(dto.getDescription())
            .instructor(instructor)
            .status(Course.CourseStatus.PENDING)
            .build();
        return toDTO(courseRepository.save(course));
    }

    public CourseDTO updateCourse(Long id, CourseDTO dto, String email) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        return toDTO(courseRepository.save(course));
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public CourseDTO updateCourseStatus(Long id, Course.CourseStatus status) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));
        course.setStatus(status);
        return toDTO(courseRepository.save(course));
    }

    public List<CourseDTO> getCoursesByInstructor(String email) {
        User instructor = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return courseRepository.findByInstructor(instructor).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public LessonDTO addLesson(Long courseId, LessonDTO dto) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        Lesson lesson = Lesson.builder()
            .title(dto.getTitle())
            .content(dto.getContent())
            .contentType(dto.getContentType())
            .mediaUrl(dto.getMediaUrl())
            .lessonOrder(dto.getLessonOrder())
            .course(course)
            .build();
        return toLessonDTO(lessonRepository.save(lesson));
    }

    public void deleteLesson(Long lessonId) {
        lessonRepository.deleteById(lessonId);
    }

    public List<LessonDTO> getLessonsByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        return lessonRepository.findByCourseOrderByLessonOrderAsc(course)
            .stream().map(this::toLessonDTO).collect(Collectors.toList());
    }

    public CourseDTO toDTO(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setStatus(course.getStatus());
        dto.setCreatedAt(course.getCreatedAt());
        if (course.getInstructor() != null) {
            dto.setInstructorId(course.getInstructor().getId());
            dto.setInstructorName(course.getInstructor().getName());
        }
        dto.setEnrollmentCount(enrollmentRepository.countByCourse(course));
        return dto;
    }

    public LessonDTO toLessonDTO(Lesson lesson) {
        LessonDTO dto = new LessonDTO();
        dto.setId(lesson.getId());
        dto.setTitle(lesson.getTitle());
        dto.setContent(lesson.getContent());
        dto.setContentType(lesson.getContentType());
        dto.setMediaUrl(lesson.getMediaUrl());
        dto.setLessonOrder(lesson.getLessonOrder());
        if (lesson.getCourse() != null) dto.setCourseId(lesson.getCourse().getId());
        return dto;
    }
}
