package com.lms.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private String name;
    @Column(unique = true, nullable = false) private String email;
    @Column(nullable = false) private String password;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Role role;
    @Column(name = "created_at") private LocalDateTime createdAt;
    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL) private List<Course> courses;
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL) private List<Enrollment> enrollments;
    @PrePersist public void prePersist() { createdAt = LocalDateTime.now(); }
    public enum Role { ADMIN, INSTRUCTOR, STUDENT }
}
