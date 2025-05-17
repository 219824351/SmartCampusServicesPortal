package SmartCampusServicesPortal.repository;

import SmartCampusServicesPortal.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByCode(String code);
}