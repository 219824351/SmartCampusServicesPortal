package SmartCampusServicesPortal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementDTO {
    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private String authorName;
    private Long courseId;
    private String courseName;
    private LocalDateTime createdAt;
}
