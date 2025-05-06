package SmartCampusServicesPortal.model.enrollment;

import SmartCampusServicesPortal.model.user.User;
import SmartCampusServicesPortal.model.course.Course;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "enrollments")
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "enrolled_at", updatable = false)
    private LocalDateTime enrolledAt;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private EnrollmentStatus status = EnrollmentStatus.ACTIVE;

    @PrePersist
    protected void onCreate() {
        enrolledAt = LocalDateTime.now();
    }
}
