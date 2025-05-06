package SmartCampusServicesPortal.model.maintance;


import SmartCampusServicesPortal.model.room.Room;
import SmartCampusServicesPortal.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "maintenance_requests")
public class MaintenanceRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technician_id")
    private User technician;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaintenanceStatus status = MaintenanceStatus.OPEN;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaintenancePriority priority = MaintenancePriority.MEDIUM;

    @Column(name = "reported_at", nullable = false)
    private LocalDateTime reportedAt;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "resolution_notes")
    private String resolutionNotes;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (reportedAt == null) {
            reportedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
