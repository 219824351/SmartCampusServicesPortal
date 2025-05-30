package SmartCampusServicesPortal.service;

import SmartCampusServicesPortal.model.CourseSchedule;
import SmartCampusServicesPortal.repository.CourseRepository;
import SmartCampusServicesPortal.repository.CourseScheduleRepository;
import SmartCampusServicesPortal.repository.RoomRepository;
import SmartCampusServicesPortal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseScheduleService {
    private final CourseScheduleRepository courseScheduleRepository;
    private final CourseRepository courseRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public CourseSchedule createSchedule(CourseSchedule schedule) {
        schedule.setCourse(courseRepository.findById(schedule.getCourse().getId()).orElseThrow());
        schedule.setRoom(roomRepository.findById(schedule.getRoom().getId()).orElseThrow());
        schedule.setLecturer(userRepository.findById(schedule.getLecturer().getId()).orElseThrow());
        return courseScheduleRepository.save(schedule);
    }

    public List<CourseSchedule> getSchedulesByCourse(Long courseId) {
        return courseScheduleRepository.findByCourseId(courseId);
    }

    public List<CourseSchedule> getSchedulesByLecturer(Long lecturerId) {
        return courseScheduleRepository.findByLecturerId(lecturerId);
    }
}
