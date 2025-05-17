package SmartCampusServicesPortal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private Long id;
    private Long userId;
    private String userName;
    private Long roomId;
    private String roomName;
    private String buildingName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String purpose;
}
