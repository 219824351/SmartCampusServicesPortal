package SmartCampusServicesPortal.controller;

import SmartCampusServicesPortal.model.*;
import SmartCampusServicesPortal.model.enums.BookingStatus;
import SmartCampusServicesPortal.model.enums.MaintenancePriority;
import SmartCampusServicesPortal.model.enums.MaintenanceStatus;
import SmartCampusServicesPortal.model.enums.Role;
import SmartCampusServicesPortal.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AnalyticsService analyticsService;
    private final BookingService bookingService;
    private final MaintenanceService maintenanceService;
    private final AnnouncementService announcementService;
    private final UserService userService;
    private final SystemSettingsService systemSettingsService;

    public AdminController(AnalyticsService analyticsService, BookingService bookingService,
                           MaintenanceService maintenanceService, AnnouncementService announcementService,
                           UserService userService, SystemSettingsService systemSettingsService) {
        this.analyticsService = analyticsService;
        this.bookingService = bookingService;
        this.maintenanceService = maintenanceService;
        this.announcementService = announcementService;
        this.userService = userService;
        this.systemSettingsService = systemSettingsService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {

        model.addAttribute("roomUtilization", analyticsService.getRoomUtilizationStats());
        model.addAttribute("maintenanceStats", analyticsService.getMaintenanceStats());
        model.addAttribute("userActivity", analyticsService.getRecentUserActivity(10));
        return "admin/dashboard";
    }

    @GetMapping("/analytics")
    public String showAnalytics(Model model,
                                @RequestParam(required = false) LocalDate startDate,
                                @RequestParam(required = false) LocalDate endDate) {
        LocalDate defaultStart = startDate != null ? startDate : LocalDate.now().minusMonths(1);
        LocalDate defaultEnd = endDate != null ? endDate : LocalDate.now();

        model.addAttribute("startDate", defaultStart);
        model.addAttribute("endDate", defaultEnd);
        model.addAttribute("bookingTrends", analyticsService.getBookingTrends(defaultStart, defaultEnd));
        model.addAttribute("resourceUsage", analyticsService.getResourceUsage(defaultStart, defaultEnd));
        model.addAttribute("userEngagement", analyticsService.getUserEngagementStats(defaultStart, defaultEnd));
        return "admin/analytics";
    }

    @GetMapping("/bookings")
    public String manageBookings(Model model,
                                 @RequestParam(required = false) String status,
                                 @RequestParam(required = false) Long roomId,
                                 @RequestParam(required = false) LocalDate date) {
        model.addAttribute("bookings", bookingService.getAllBookings(status, roomId, date));
        model.addAttribute("rooms", bookingService.getAllBookableRooms());
        model.addAttribute("statuses", BookingStatus.values());
        return "admin/bookings";
    }

    @PostMapping("/bookings/{id}/update-status")
    public String updateBookingStatus(@PathVariable Long id,
                                      @RequestParam String status,
                                      RedirectAttributes redirectAttributes) {
        try {
            bookingService.updateBookingStatus(id, BookingStatus.valueOf(status));
            redirectAttributes.addFlashAttribute("success", "Booking status updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update booking: " + e.getMessage());
        }
        return "redirect:/admin/bookings";
    }

    @GetMapping("/maintenance")
    public String manageMaintenance(Model model,
                                    @RequestParam(required = false) String status,
                                    @RequestParam(required = false) Long roomId,
                                    @RequestParam(required = false) Long technicianId) {
        model.addAttribute("requests", maintenanceService.getAllMaintenanceRequests(status, roomId, technicianId));
        model.addAttribute("rooms", maintenanceService.getAllMaintainableRooms());
        model.addAttribute("technicians", userService.getTechnicians());
        model.addAttribute("statuses", MaintenanceStatus.values());
        model.addAttribute("priorities", MaintenancePriority.values());
        return "admin/maintenance";
    }

    @PostMapping("/maintenance/{id}/assign")
    public String assignMaintenanceRequest(@PathVariable Long id,
                                           @RequestParam Long technicianId,
                                           RedirectAttributes redirectAttributes) {
        try {
            maintenanceService.assignTechnician(id, technicianId);
            redirectAttributes.addFlashAttribute("success", "Technician assigned successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to assign technician: " + e.getMessage());
        }
        return "redirect:/admin/maintenance";
    }

    @PostMapping("/maintenance/{id}/update-status")
    public String updateMaintenanceStatus(@PathVariable Long id,
                                          @RequestParam String status,
                                          @RequestParam(required = false) String resolutionNotes,
                                          RedirectAttributes redirectAttributes) {
        try {
            maintenanceService.updateMaintenanceStatus(id, MaintenanceStatus.valueOf(status), resolutionNotes);
            redirectAttributes.addFlashAttribute("success", "Maintenance status updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update maintenance: " + e.getMessage());
        }
        return "redirect:/admin/maintenance";
    }

    @GetMapping("/announcements")
    public String manageAnnouncements(Model model) {
        model.addAttribute("announcements", announcementService.getAllAnnouncements());
        model.addAttribute("courses", announcementService.getAllAnnounceableCourses());
        return "admin/announcements";
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
                announcement.setCourse(announcementService.getCourseById(courseId));
            }

            announcementService.createAnnouncement(announcement);
            redirectAttributes.addFlashAttribute("success", "Announcement created successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create announcement: " + e.getMessage());
        }

        return "redirect:/admin/announcements";
    }

    @PostMapping("/announcements/{id}/delete")
    public String deleteAnnouncement(@PathVariable Long id,
                                     RedirectAttributes redirectAttributes) {
        try {
            announcementService.deleteAnnouncement(id);
            redirectAttributes.addFlashAttribute("success", "Announcement deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete announcement: " + e.getMessage());
        }
        return "redirect:/admin/announcements";
    }

    @GetMapping("/users")
    public String manageUsers(Model model,
                              @RequestParam(required = false) String role,
                              @RequestParam(required = false) String search) {
        model.addAttribute("users", userService.getUsers(role, search));
        model.addAttribute("roles", Role.values());
        return "admin/users";
    }

    @PostMapping("/users")
    public String createUser(@ModelAttribute User user,
                             RedirectAttributes redirectAttributes) {
        try {
            userService.createUser(user);
            redirectAttributes.addFlashAttribute("success", "User created successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create user: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/update-role")
    public String updateUserRole(@PathVariable Long id,
                                 @RequestParam String role,
                                 RedirectAttributes redirectAttributes) {
        try {
            userService.updateUserRole(id, Role.valueOf(role));
            redirectAttributes.addFlashAttribute("success", "User role updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update user: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/settings")
    public String manageSettings(Model model) {
        model.addAttribute("settings", systemSettingsService.getAllSettings());
        return "admin/settings";
    }

    @PostMapping("/settings")
    public String updateSetting(@RequestParam String key,
                                @RequestParam String value,
                                RedirectAttributes redirectAttributes) {
        try {
            systemSettingsService.updateSetting(key, value);
            redirectAttributes.addFlashAttribute("success", "Setting updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update setting: " + e.getMessage());
        }
        return "redirect:/admin/settings";
    }
}