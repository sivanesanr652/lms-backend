# LMS Backend - Spring Boot

## Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8.0+

## Setup

### 1. Create MySQL Database
```sql
CREATE DATABASE lms_db;
```

### 2. Configure Database
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### 3. Run
```bash
mvn spring-boot:run
```

Server starts on: http://localhost:8080
Swagger UI: http://localhost:8080/swagger-ui.html

## API Endpoints

### Auth (Public)
- POST /api/auth/register
- POST /api/auth/login

### Admin (Role: ADMIN)
- GET    /api/admin/users
- PUT    /api/admin/users/{id}/role?role=STUDENT|INSTRUCTOR|ADMIN
- DELETE /api/admin/users/{id}
- GET    /api/admin/courses
- PUT    /api/admin/courses/{id}/status?status=APPROVED|REJECTED
- DELETE /api/admin/courses/{id}

### Instructor (Role: INSTRUCTOR)
- GET    /api/instructor/courses
- POST   /api/instructor/courses
- PUT    /api/instructor/courses/{id}
- POST   /api/instructor/courses/{courseId}/lessons
- DELETE /api/instructor/lessons/{lessonId}
- GET    /api/instructor/courses/{courseId}/enrollments

### Student (Role: STUDENT)
- GET    /api/student/courses
- GET    /api/student/courses/{id}
- GET    /api/student/courses/{courseId}/lessons
- POST   /api/student/enroll/{courseId}
- DELETE /api/student/unenroll/{courseId}
- GET    /api/student/my-enrollments
- PUT    /api/student/enrollments/{id}/progress?progress=50

## Default Admin Account
Create via register API:
```json
{ "name": "Admin", "email": "admin@lms.com", "password": "admin123", "role": "ADMIN" }
```
