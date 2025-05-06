package SmartCampusServicesPortal.model.notification;

import SmartCampusServicesPortal.model.RelatedEntityType;
import SmartCampusServicesPortal.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    @Column(nullable = false)
    private String message;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Enumerated(EnumType.STRING) @Column(name = "related_entity_type")
    private RelatedEntityType relatedEntityType;

    @Column(name = "related_entity_id")
    private Long relatedEntityId;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
