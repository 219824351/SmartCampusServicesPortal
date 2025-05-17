package SmartCampusServicesPortal.dto;

import SmartCampusServicesPortal.model.enums.RoomType;
import lombok.Data;

@Data
public class roomDTO {
    private Long buildingId;
    private String roomNumber;
    private String name;
    private RoomType type;
    private Integer capacity;
    private Boolean hasProjector;
    private Boolean hasWhiteboard;
    private Integer floor;
    private String description;
}
