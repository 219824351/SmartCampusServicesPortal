package SmartCampusServicesPortal.controller;

import SmartCampusServicesPortal.dto.RoomResponseDTO;
import SmartCampusServicesPortal.model.*;
import SmartCampusServicesPortal.model.enums.MaintenancePriority;
import SmartCampusServicesPortal.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/lecturer")
public class LecturerController {

    private final RoomService roomService;
    private final BookingService bookingService;
    private final MaintenanceService maintenanceService;
    private final CourseService courseService;
    private final NotificationService notificationService;
    private final AnnouncementService announcementService;

    public LecturerController(RoomService roomService, BookingService bookingService,
                              MaintenanceService maintenanceService, CourseService courseService,
                              NotificationService notificationService, AnnouncementService announcementService) {
        this.roomService = roomService;
        this.bookingService = bookingService;
        this.maintenanceService = maintenanceService;
        this.courseService = courseService;
        this.notificationService = notificationService;
        this.announcementService = announcementService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("user", user);
//        model.addAttribute("upcomingClasses", courseService.getLecturerUpcomingClasses(user.getId(), 5));
        model.addAttribute("recentBookings", bookingService.getUserBookings(user, 5));
        return "lecturer/dashboard";
    }

    @GetMapping("/schedule")
    public String showSchedule(Model model, HttpServletRequest request,
                               @RequestParam(required = false) LocalDate startDate,
                               @RequestParam(required = false) LocalDate endDate) {
        User user = (User) request.getSession().getAttribute("user");
        LocalDate defaultStart = startDate != null ? startDate : LocalDate.now().withDayOfMonth(1);
        LocalDate defaultEnd = endDate != null ? endDate : defaultStart.plusMonths(1).minusDays(1);

//        model.addAttribute("schedule", courseService.getLecturerSchedule(user.getId(), defaultStart, defaultEnd));
        model.addAttribute("startDate", defaultStart);
        model.addAttribute("endDate", defaultEnd);
        return "lecturer/schedule";
    }

    @GetMapping("/timetable")
    public String showTimetable(Model model, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
//        model.addAttribute("weeklySchedule", courseService.getLecturerWeeklyTimetable(user.getId()));
        return "lecturer/timetable";
    }

    @GetMapping("/announcements")
    public String showAnnouncements(Model model, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("announcements", announcementService.getAnnouncementsForLecturer(user));
        return "lecturer/announcements";
    }

    @PostMapping("/announcements")
    public String createAnnouncement(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam(required = false) Long courseId,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        User user = (User) request.getSession().getAttribute("user");

        try {
            Announcement announcement = new Announcement();
            announcement.setTitle(title);
            announcement.setContent(content);
            announcement.setAuthor(user);

            if (courseId != null) {
                announcement.setCourse(courseService.getCourseById(courseId));
            }

            announcementService.createAnnouncement(announcement);
            redirectAttributes.addFlashAttribute("success", "Announcement created successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create announcement: " + e.getMessage());
        }

        return "redirect:/lecturer/announcements";
    }

    @GetMapping("/book-room")
    public String showRoomBooking(Model model, HttpServletRequest request,
                                  @RequestParam(required = false) LocalDate date,
                                  @RequestParam(required = false) String startTime,
                                  @RequestParam(required = false) Integer duration,
                                  @RequestParam(required = false) Integer capacity,
                                  @RequestParam(required = false) Boolean hasProjector,
                                  @RequestParam(required = false) Boolean hasWhiteboard,
                                  @RequestParam(required = false) Long selectedRoomId) {

        User user = (User) request.getSession().getAttribute("user");

        LocalDate currentDate = date != null ? date : LocalDate.now();
        LocalTime currentTime = startTime != null ? LocalTime.parse(startTime) : LocalTime.now().plusHours(1).withMinute(0);
        int selectedDuration = duration != null ? duration : 2;
        int minCapacity = capacity != null ? capacity : 1;

        LocalDateTime startDateTime = LocalDateTime.of(currentDate, currentTime);
        LocalDateTime endDateTime = startDateTime.plusHours(selectedDuration);

        model.addAttribute("currentDate", currentDate);
        model.addAttribute("currentTime", currentTime);
        model.addAttribute("selectedRoomId", selectedRoomId);
        model.addAttribute("selectedDuration", selectedDuration);

        List<RoomResponseDTO> availableRooms = roomService.findAvailableRooms(
                startDateTime,
                endDateTime,
                minCapacity,
                hasProjector,
                hasWhiteboard
        );

        model.addAttribute("availableRooms", availableRooms);
        model.addAttribute("userBookings", bookingService.getUserBookings(user, 5));
        model.addAttribute("buildings", roomService.getAllBuildings());
        return "lecturer/book-room";
    }

    @PostMapping("/bookings")
    public String createBooking(
            @RequestParam Long roomId,
            @RequestParam LocalDate date,
            @RequestParam String startTime,
            @RequestParam Integer duration,
            @RequestParam String purpose,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        User user = (User) request.getSession().getAttribute("user");
        LocalDateTime startDateTime = LocalDateTime.of(date, LocalTime.parse(startTime));
        LocalDateTime endDateTime = startDateTime.plusHours(duration);

        try {
            Room room = roomService.getRoomById(roomId);
            Booking booking = bookingService.createBooking(
                    user,
                    room,
                    startDateTime,
                    endDateTime,
                    purpose
            );

            redirectAttributes.addFlashAttribute("success",
                    "Room booked successfully! Booking ID: " + booking.getId());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Failed to book room: " + e.getMessage());
        }

        return "redirect:/lecturer/book-room";
    }

    @PostMapping("/bookings/{id}/cancel")
    public String cancelBooking(@PathVariable Long id,
                                HttpServletRequest request,
                                RedirectAttributes redirectAttributes) {
        User user = (User) request.getSession().getAttribute("user");

        try {
            bookingService.cancelBooking(id, user);
            redirectAttributes.addFlashAttribute("success", "Booking cancelled successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to cancel booking: " + e.getMessage());
        }

        return "redirect:/lecturer/book-room";
    }

    @GetMapping("/maintenance")
    public String showMaintenance(Model model, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("requests", maintenanceService.getUserMaintenanceRequests(user, 10));
        model.addAttribute("rooms", roomService.getRoomsUserCanAccess(user));
        return "lecturer/maintenance";
    }

    @PostMapping("/maintenance")
    public String submitMaintenanceRequest(
            @RequestParam Long roomId,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String priority,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        User user = (User) request.getSession().getAttribute("user");

        try {
            MaintenanceRequest req = new MaintenanceRequest();
            req.setTitle(title);
            req.setDescription(description);
            req.setRoom(roomService.getRoomById(roomId));
            req.setReporter(user);
            req.setPriority(MaintenancePriority.valueOf(priority));

            maintenanceService.createMaintenanceRequest(req);
            redirectAttributes.addFlashAttribute("success", "Maintenance request submitted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to submit request: " + e.getMessage());
        }

        return "redirect:/lecturer/maintenance";
    }
}