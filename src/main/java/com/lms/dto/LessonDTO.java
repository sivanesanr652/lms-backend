package com.lms.dto;
import com.lms.entity.Lesson;
import lombok.Data;

@Data
public class LessonDTO {
    private Long id;
    private String title;
    private String content;
    private Lesson.ContentType contentType;
    private String mediaUrl;
    private Integer lessonOrder;
    private Long courseId;
}
