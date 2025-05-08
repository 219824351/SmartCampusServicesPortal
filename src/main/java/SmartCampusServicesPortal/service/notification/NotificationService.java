package SmartCampusServicesPortal.service.notification;

import SmartCampusServicesPortal.model.notification.Notification;
import SmartCampusServicesPortal.model.RelatedEntityType;
import SmartCampusServicesPortal.repository.notification.NotificationRepository;
import SmartCampusServicesPortal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public Notification createNotification(Long recipientId, String message, RelatedEntityType relatedEntityType, Long relatedEntityId) {
        Notification notification = new Notification();
        notification.setRecipient(userRepository.findById(recipientId).orElseThrow());
        notification.setMessage(message);
        notification.setRelatedEntityType(relatedEntityType);
        notification.setRelatedEntityId(relatedEntityId);
        return notificationRepository.save(notification);
    }

    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByRecipientId(userId);
    }

    public Notification markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow();
        notification.setIsRead(true);
        notification.setReadAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    public long getUnreadCount(Long userId) {

        return notificationRepository.countByRecipientIdAndIsReadFalse(userId);
    }
}
