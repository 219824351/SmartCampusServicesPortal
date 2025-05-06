package SmartCampusServicesPortal.repository.notification;

import SmartCampusServicesPortal.model.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientId(Long recipientId);
    List<Notification> findByRecipientIdAndIsReadFalse(Long recipientId);
    long countByRecipientIdAndIsReadFalse(Long recipientId);
}
