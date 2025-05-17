package SmartCampusServicesPortal.repository;

import SmartCampusServicesPortal.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    List<Announcement> findByAuthorId(Long authorId);
    List<Announcement> findByCourseId(Long courseId);
    List<Announcement> findAllByOrderByCreatedAtDesc();
}
