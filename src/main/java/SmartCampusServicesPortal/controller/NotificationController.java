package SmartCampusServicesPortal.controller;


import SmartCampusServicesPortal.model.Notification;
import SmartCampusServicesPortal.model.User;
import SmartCampusServicesPortal.service.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/notifications/{id}/mark-read")
    public String markNotificationAsRead(@PathVariable Long id,
                                         HttpServletRequest request,
                                         RedirectAttributes redirectAttributes) {
        User user = (User) request.getSession().getAttribute("user");

        try {
            notificationService.markAsRead(id, user.getId());
            redirectAttributes.addFlashAttribute("success", "Notification marked as read");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update notification: " + e.getMessage());
        }

        return "redirect:/student/notifications";
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getMyNotifications(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = ((User) userDetails).getId();
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = ((User) userDetails).getId();
        return ResponseEntity.ok(notificationService.getUnreadCount(userId));
    }

}
