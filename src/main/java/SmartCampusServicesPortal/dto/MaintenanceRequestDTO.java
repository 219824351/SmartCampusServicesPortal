package SmartCampusServicesPortal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceRequestDTO {
    private Long id;
    private String title;
    private String description;
    private Long roomId;
    private String roomName;
    private Long reporterId;
    private String reporterName;
    private Long technicianId;
    private String technicianName;
    private String status;
    private String priority;
    private LocalDateTime reportedAt;
    private LocalDateTime resolvedAt;
    private String resolutionNotes;
}
