package com.lms.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity @Table(name = "courses")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Course {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private String title;
    @Column(columnDefinition = "TEXT") private String description;
    @Enumerated(EnumType.STRING) private CourseStatus status;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "instructor_id") private User instructor;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true) private List<Lesson> lessons;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL) private List<Enrollment> enrollments;
    @Column(name = "created_at") private LocalDateTime createdAt;
    @Column(name = "updated_at") private LocalDateTime updatedAt;
    @PrePersist public void prePersist() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); if (status == null) status = CourseStatus.PENDING; }
    @PreUpdate public void preUpdate() { updatedAt = LocalDateTime.now(); }
    public enum CourseStatus { PENDING, APPROVED, REJECTED }
}
