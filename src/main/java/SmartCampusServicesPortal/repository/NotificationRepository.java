package SmartCampusServicesPortal.repository;

import SmartCampusServicesPortal.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientId(Long recipientId);
    List<Notification> findByRecipientIdAndIsReadFalse(Long recipientId);
    long countByRecipientIdAndIsReadFalse(Long recipientId);
    List<Notification> findByRecipientIdOrderByCreatedAtDesc(Long id);
    Optional<Notification> findByIdAndRecipientId(Long id, Long recipientId);
}
