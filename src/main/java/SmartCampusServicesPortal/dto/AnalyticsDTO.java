package SmartCampusServicesPortal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsDTO {
    private String label;
    private Long value;
    private Double percentage;
}
