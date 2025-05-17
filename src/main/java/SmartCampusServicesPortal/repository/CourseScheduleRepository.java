package SmartCampusServicesPortal.repository;

import SmartCampusServicesPortal.model.CourseSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CourseScheduleRepository extends JpaRepository<CourseSchedule, Long> {
    @Query("SELECT cs FROM CourseSchedule cs JOIN cs.course c JOIN c.enrollments e " +
            "WHERE e.student.id = :studentId AND e.status = 'ACTIVE' " +
            "AND cs.startDate <= :endDate AND cs.endDate >= :startDate")
    List<CourseSchedule> findStudentSchedules(Long studentId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT cs FROM CourseSchedule cs WHERE cs.lecturer.id = :lecturerId " +
            "AND cs.startDate <= :endDate AND cs.endDate >= :startDate")
    List<CourseSchedule> findLecturerSchedules(Long lecturerId, LocalDate startDate, LocalDate endDate);

    List<CourseSchedule> findByCourseId(Long courseId);
    List<CourseSchedule> findByLecturerId(Long lecturerId);
}