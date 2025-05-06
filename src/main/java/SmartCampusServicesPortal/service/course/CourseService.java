package SmartCampusServicesPortal.service.course;

import SmartCampusServicesPortal.model.course.Course;
import SmartCampusServicesPortal.repository.course.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseByCode(String code) {
        return courseRepository.findByCode(code);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
}
