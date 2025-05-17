package SmartCampusServicesPortal.service;

import SmartCampusServicesPortal.dto.AnnouncementDTO;
import SmartCampusServicesPortal.model.Announcement;
import SmartCampusServicesPortal.model.Course;
import SmartCampusServicesPortal.model.User;
import SmartCampusServicesPortal.repository.AnnouncementRepository;
import SmartCampusServicesPortal.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnnouncementService {
    private final AnnouncementRepository announcementRepository;
    private final CourseRepository courseRepository;

    public Announcement createAnnouncement(Announcement announcement) {
        announcement.setCreatedAt(LocalDateTime.now());
        return announcementRepository.save(announcement);
    }

    public List<AnnouncementDTO> getAnnouncementsForLecturer(User lecturer) {
        return announcementRepository.findByAuthorId(lecturer.getId()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AnnouncementDTO> getAllAnnouncements() {
        return announcementRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void deleteAnnouncement(Long id) {
        announcementRepository.deleteById(id);
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    private AnnouncementDTO convertToDTO(Announcement announcement) {
        AnnouncementDTO dto = new AnnouncementDTO();
        dto.setId(announcement.getId());
        dto.setTitle(announcement.getTitle());
        dto.setContent(announcement.getContent());
        dto.setAuthorId(announcement.getAuthor().getId());
        dto.setAuthorName(announcement.getAuthor().getFirstName() + " " + announcement.getAuthor().getLastName());

        if (announcement.getCourse() != null) {
            dto.setCourseId(announcement.getCourse().getId());
            dto.setCourseName(announcement.getCourse().getName());
        }

        dto.setCreatedAt(announcement.getCreatedAt());
        return dto;
    }

    public Object getAllAnnounceableCourses() {
        return null;
    }
}
