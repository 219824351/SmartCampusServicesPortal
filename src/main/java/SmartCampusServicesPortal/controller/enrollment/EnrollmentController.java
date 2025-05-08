package SmartCampusServicesPortal.controller.enrollment;

import SmartCampusServicesPortal.model.user.User;
import SmartCampusServicesPortal.service.enrollment.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<?> enrollCourse(
            @RequestParam Long courseId,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long studentId = ((User) userDetails).getId();
        return ResponseEntity.ok(enrollmentService.enrollStudent(studentId, courseId));
    }

    @DeleteMapping("/{enrollmentId}")
    public ResponseEntity<Void> dropCourse(
            @PathVariable Long enrollmentId,
            @AuthenticationPrincipal UserDetails userDetails) {
        enrollmentService.dropCourse(enrollmentId);
        return ResponseEntity.noContent().build();
    }
}
