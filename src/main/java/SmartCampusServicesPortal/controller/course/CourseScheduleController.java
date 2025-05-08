package SmartCampusServicesPortal.controller.course;

import SmartCampusServicesPortal.model.course.CourseSchedule;
import SmartCampusServicesPortal.service.course.CourseScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
public class CourseScheduleController {
    private final CourseScheduleService courseScheduleService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CourseSchedule> createSchedule(@RequestBody CourseSchedule schedule) {
        return ResponseEntity.ok(courseScheduleService.createSchedule(schedule));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<CourseSchedule>> getSchedulesByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseScheduleService.getSchedulesByCourse(courseId));
    }

    @GetMapping("/lecturer/{lecturerId}")
    public ResponseEntity<List<CourseSchedule>> getSchedulesByLecturer(@PathVariable Long lecturerId) {
        return ResponseEntity.ok(courseScheduleService.getSchedulesByLecturer(lecturerId));
    }
}
