package SmartCampusServicesPortal.dto;

import SmartCampusServicesPortal.model.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponseDTO {
    private Long id;
    private Long buildingId;
    private String buildingName; // You might want to add this
    private String roomNumber;
    private String name;
    private RoomType type;
    private Integer capacity;
    private Boolean hasProjector;
    private Boolean hasWhiteboard;
    private Integer floor;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
