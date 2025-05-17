package SmartCampusServicesPortal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleItemDTO {
    private Long id;
    private String type; // "COURSE" or "BOOKING"
    private String courseCode;
    private String title;
    private String roomName;
    private String buildingName;
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
}
