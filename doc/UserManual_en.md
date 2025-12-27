## 3. User Manual and Deployment Configuration

### 3.1 System Overview & Quick Start

The Smart Campus Management System is a comprehensive educational management platform supporting three user roles: administrator, teacher, and student. Administrators have the highest system privileges, allowing them to manage user accounts, configure system parameters, and view global data reports. Teachers can manage course information, enter student grades, approve make-up exam requests, and view teaching-related statistics. Students can view personal grades, select courses, pay tuition, and check timetables. This manual provides detailed instructions for all system functions and configuration methods to help users quickly get started.

The system uses a web browser as the client. Users simply enter the system address in the browser to access it. Mainstream browsers such as Chrome, Firefox, Edge, and Safari are supported. It is recommended to use the latest browser version for the best experience. On first access, users must log in with their assigned username and password. After successful login, users enter the system homepage and use functions according to their role permissions.

### 3.2 Administrator Guide

#### 3.2.1 User Management

Administrators can create, query, modify, and delete user accounts in the user management module. On the user management page, all users are listed, including username, real name, user type, email, status, and creation time.

![](image.png)

To create a new user, click "Register New User" and fill in the basic information form. The system will automatically validate the input. Roles can be assigned during creation, granting the user all permissions of that role. After creation, the system assigns a user ID for future reference.

![](image-1.png)

User information can be viewed and updated, including personal details and status. For password reset, administrators can set a new password, which the user must use on next login.

![](image-2.png)


#### 3.2.2 Course Management

Administrators can manage all course information in the course management module. The course list includes course code, name, credits, teacher, classroom, and capacity.

![](image-3.png)


Course information can be modified, such as name, credits, and capacity. Changes should be made carefully after the course has started, especially capacity adjustments, which may affect enrolled students.

![](image-4.png)


#### 3.2.3 Classroom Management

The classroom management module maintains classroom resources. The classroom list includes name, location, capacity, and equipment.

![](image-5.png)

When creating a new classroom, fill in the basic information. The name is recommended in the format "Building-Room", e.g., "Building A101". Location describes the specific position to help users find the classroom. Capacity sets the maximum number of students for scheduling. 

![](image-6.png)

#### 3.2.4 Tuition Management

The tuition management module handles student tuition payments. The tuition list includes student name, amount, semester, payment status, and deadline.

![](image-7.png)

To create a tuition record, select the student, set the amount, choose the semester, and set the payment deadline. Amount is in RMB and supports decimals. Semester identifies the academic year and term, e.g., "2023-2024 First Semester". The deadline is the last payment date; overdue students may face course selection restrictions.

![](image-8.png)

### 3.3 Teacher Guide

#### 3.3.1 Grade Entry

To enter new grades, click "Grade Entry", select the student, set the exam type, and input scores. The system supports multiple exam types, such as assignments, midterms, and finals. Teachers enter scores for each type, and the system automatically calculates the total grade based on preset weights: Total Grade = Assignment × 20% + Midterm × 30% + Final × 50%.

![](image-9.png)

After entry, the system validates the data. Score range checks ensure grades are between 0-100, and grade conversion automatically assigns grade levels. 

#### 3.3.2 Make-up Exam Approval

The make-up exam approval function handles student requests. Teachers see a list of requests, including student name, course, original grade, reason, and request time.

![](image-10.png)

During approval, teachers review details and confirm authenticity. If approved, teachers schedule the make-up exam and fill in comments. The date must be in the future, and the location must be an existing classroom. If rejected, teachers provide a reason, which is notified to the student.

After the make-up exam, teachers enter the new grade and comments. The system updates the record and links the new grade to the original. Teachers can view all make-up records.

![](image-11.png)

#### 3.3.3 Teacher Timetable Management

Teachers can create and update teaching timetables for their courses. The timetable management page shows all courses taught by the teacher.

![](image-12.png)

### 3.4 Student Guide

#### 3.4.1 Grade Inquiry

Students can view their academic grades. The grade inquiry page lists courses, credits, grades, levels, and GPA.

![](image-13.png)

Transcript generation allows students to obtain a standardized PDF for a selected term, including course list, credit statistics, average score, and GPA. Transcripts can be used for scholarships, study abroad, etc.

![](image-14.png)

- print:

![](image-15.png)

#### 3.4.2 Course Selection & Withdrawal

Course selection allows students to choose courses for the next or current term. The selection page lists available courses, including name, teacher, time, credits, and remaining slots.

![](image-16.png)

Students can view course details, including description, prerequisites, and teacher info. On confirmation, the system checks prerequisites, capacity, and schedule conflicts. If all conditions are met, selection succeeds; otherwise, the system shows the reason for failure.

Withdrawal allows students to cancel selected courses within the allowed period. After withdrawal, the slot is released and statistics updated. There is a deadline for withdrawal; late requests are usually not allowed.

![](image-17.png)

#### 3.4.3 Timetable Inquiry

The timetable page shows the student's schedule for the current term. Each course displays name, time, location, and teacher. Students can view details such as description and syllabus. Timetable supports week and list views, and term switching.

![](image-18.png)

#### 3.4.4 Tuition Payment

The tuition payment function handles student payments. The payment page lists pending tuition, amount, deadline, and status.

![](image-19.png)

To pay, students click the payment button and choose a method. On success, the system updates the status and generates a record. Students can view payment history.

![](image-20.png)

### 3.5 Deployment Configuration Guide

#### 3.5.1 Environment Requirements

The Smart Campus Management System supports Windows, Linux, and macOS. JDK 17 or higher is required. The development environment uses the H2 embedded database.

Hardware: At least dual-core CPU and 4GB RAM are recommended. Network: HTTP (port 80) and HTTPS (port 443) must be open for browser and mobile access.

#### 3.5.2 Configuration File

The main configuration file is `src/main/resources/application.yml`, defining runtime parameters, datasource, and security settings.

Datasource example:
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    username: sa
    password:
    driver-class-name: org.h2.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

JWT security configuration:
```yaml
jwt:
  secret: your-256-bit-secret-key-for-jwt-token-signing
  expiration: 86400000
```

#### 3.5.3 Build & Run

The project uses Maven. For the first build, run:
```bash
mvn clean install -DskipTests
```

After building, run the application:
```bash
java -jar target/smart-campus-0.0.1-SNAPSHOT.jar
```

Or run directly with Maven:
```bash
mvn spring-boot:run
```

By default, the app listens on port 8081. Open `http://localhost:8081` in your browser to see the welcome page. API docs: `http://localhost:8081/swagger-ui.html`.

#### 3.5.4 Docker Deployment

Docker is supported. The project root contains a Dockerfile for building the image.

H2 file database is used for local persistence, with data saved via Docker volume.

Build the Docker image:
```bash
docker build -t smart-campus:latest .
```

Run the container:
```bash
docker run -d -p 8081:8081 -v h2-data:/data --name smart-campus smart-campus:latest
```

Use Docker Compose to start the service:
```yaml
version: '3.8'
services:
  smart-campus:
    build: .
    container_name: smart-campus-app
    ports:
      - "8081:8081"
    volumes:
      - h2-data:/data
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - SPRING_DATASOURCE_URL=jdbc:h2:file:/data/smartcampus;DB_CLOSE_DELAY=-1;MODE=MYSQL;DATABASE_TO_LOWER=TRUE
      - SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.h2.Driver
      - SPRING_DATASOURCE_USERNAME=sa
      - SPRING_DATASOURCE_PASSWORD=
      - SPRING_JPA_HIBERNATE_DDL_AUTO=create-drop
    restart: unless-stopped

volumes:
  h2-data:
```

### Port Description

| Port | Purpose |
|------|---------|
| 8081 | Main app port for web and API |

### Database Access

After the container starts, data is stored in the Docker volume `h2-data`. H2 database is only accessible inside the container. Connection string:
```
jdbc:h2:file:/data/smartcampus
```

> **Note**: This deployment does not expose the H2 TCP port and does not support direct access by external database tools. For external access, use the application API or modify `docker-compose.yml` to add port 9092 mapping.
