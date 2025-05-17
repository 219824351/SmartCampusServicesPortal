package SmartCampusServicesPortal.service;

import SmartCampusServicesPortal.model.Enrollment;
import SmartCampusServicesPortal.model.enums.EnrollmentStatus;
import SmartCampusServicesPortal.repository.CourseRepository;
import SmartCampusServicesPortal.repository.EnrollmentRepository;
import SmartCampusServicesPortal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public Enrollment enrollStudent(Long studentId, Long courseId) {
        if (enrollmentRepository.existsByStudentIdAndCourseId(Long.valueOf(studentId), courseId)) {
            throw new RuntimeException("Student is already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(userRepository.findById(studentId).orElseThrow());
        enrollment.setCourse(courseRepository.findById(courseId).orElseThrow());
        return enrollmentRepository.save(enrollment);
    }

    public void dropCourse(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElseThrow();
        enrollment.setStatus(EnrollmentStatus.DROPPED);
        enrollmentRepository.save(enrollment);
    }
}
