package com.lms.dto;
import com.lms.entity.Course;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CourseDTO {
    private Long id;
    private String title;
    private String description;
    private Course.CourseStatus status;
    private Long instructorId;
    private String instructorName;
    private List<LessonDTO> lessons;
    private Long enrollmentCount;
    private LocalDateTime createdAt;
}
