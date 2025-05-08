package SmartCampusServicesPortal.controller.maintenance;

import SmartCampusServicesPortal.model.user.User;
import SmartCampusServicesPortal.model.maintance.MaintenanceRequest;
import SmartCampusServicesPortal.service.maintenance.MaintenanceRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/maintenance")
@RequiredArgsConstructor
public class MaintenanceRequestController {
    private final MaintenanceRequestService maintenanceRequestService;

    @PostMapping
    public ResponseEntity<MaintenanceRequest> createRequest(
            @RequestBody MaintenanceRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = ((User) userDetails).getId();
        return ResponseEntity.ok(maintenanceRequestService.createRequest(request, userId));
    }

    @PutMapping("/{id}/assign")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<MaintenanceRequest> assignTechnician(
            @PathVariable Long id,
            @RequestParam Long technicianId) {
        return ResponseEntity.ok(maintenanceRequestService.assignTechnician(id, technicianId));
    }

    @PutMapping("/{id}/resolve")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('LECTURER')")
    public ResponseEntity<MaintenanceRequest> resolveRequest(
            @PathVariable Long id,
            @RequestParam String resolutionNotes) {
        return ResponseEntity.ok(maintenanceRequestService.resolveRequest(id, resolutionNotes));
    }

    @GetMapping("/my-requests")
    public ResponseEntity<List<MaintenanceRequest>> getMyRequests(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = ((User) userDetails).getId();
        return ResponseEntity.ok(maintenanceRequestService.getRequestsByReporter(Long.valueOf(userId)));
    }

    @GetMapping("/assigned-to-me")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('LECTURER')")
    public ResponseEntity<List<MaintenanceRequest>> getAssignedToMe(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = ((User) userDetails).getId();
        return ResponseEntity.ok(maintenanceRequestService.getRequestsByTechnician(Long.valueOf(userId)));
    }
}
