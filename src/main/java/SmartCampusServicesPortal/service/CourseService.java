package SmartCampusServicesPortal.service;

import SmartCampusServicesPortal.dto.ScheduleItemDTO;
import SmartCampusServicesPortal.model.Course;

import SmartCampusServicesPortal.repository.CourseRepository;
import SmartCampusServicesPortal.repository.CourseScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseScheduleRepository courseScheduleRepository;
    private final BookingService bookingService;



    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseByCode(String code) {
        return Optional.ofNullable(courseRepository.findByCode(code));
    }

    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }


    public List<ScheduleItemDTO> getStudentSchedule(Long studentId, LocalDate startDate, LocalDate endDate) {
        List<ScheduleItemDTO> schedule = new ArrayList<>();

        // Add course schedules
        courseScheduleRepository.findStudentSchedules(studentId, startDate, endDate)
                .forEach(cs -> {
                    ScheduleItemDTO item = new ScheduleItemDTO();
                    item.setId(cs.getId());
                    item.setType("COURSE");
                    item.setCourseCode(cs.getCourse().getCode());
                    item.setTitle(cs.getCourse().getName());
                    item.setRoomName(cs.getRoom().getName());
                    item.setBuildingName(cs.getRoom().getBuilding().getName());
                    item.setDayOfWeek(cs.getDayOfWeek().name());
                    item.setStartTime(cs.getStartTime());
                    item.setEndTime(cs.getEndTime());
                    item.setDate(startDate); // For weekly view, this would be calculated
                    schedule.add(item);
                });

        return schedule;
    }

    public List<ScheduleItemDTO> getWeeklyTimetable(Long studentId) {
        // This would generate a weekly view by calculating dates for the current week
        // Implementation would be similar to getStudentSchedule but organized by day
        return new ArrayList<>();
    }

    public List<ScheduleItemDTO> getLecturerSchedule(Long lecturerId, LocalDate startDate, LocalDate endDate) {
        List<ScheduleItemDTO> schedule = new ArrayList<>();

        courseScheduleRepository.findLecturerSchedules(lecturerId, startDate, endDate)
                .forEach(cs -> {
                    ScheduleItemDTO item = new ScheduleItemDTO();
                    item.setId(cs.getId());
                    item.setType("COURSE");
                    item.setCourseCode(cs.getCourse().getCode());
                    item.setTitle(cs.getCourse().getName());
                    item.setRoomName(cs.getRoom().getName());
                    item.setBuildingName(cs.getRoom().getBuilding().getName());
                    item.setDayOfWeek(cs.getDayOfWeek().name());
                    item.setStartTime(cs.getStartTime());
                    item.setEndTime(cs.getEndTime());
                    item.setDate(startDate); // For weekly view, this would be calculated
                    schedule.add(item);
                });

        return schedule;
    }

    public List<ScheduleItemDTO> getLecturerWeeklyTimetable(Long lecturerId) {
        // Similar to getWeeklyTimetable but for lecturers
        return new ArrayList<>();
    }

    public List<ScheduleItemDTO> getLecturerUpcomingClasses(Long lecturerId, int limit) {
        // Get upcoming classes for the lecturer
        return new ArrayList<>();
    }


}
