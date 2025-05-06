package SmartCampusServicesPortal.repository.course;

import SmartCampusServicesPortal.model.course.CourseSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface CourseScheduleRepository extends JpaRepository<CourseSchedule, Long> {
    List<CourseSchedule> findByCourseId(Long courseId);
    List<CourseSchedule> findByLecturerId(Long lecturerId);
    List<CourseSchedule> findByRoomId(Long roomId);
    List<CourseSchedule> findByDayOfWeek(DayOfWeek dayOfWeek);
}
