package SmartCampusServicesPortal.service.maintenance;

import SmartCampusServicesPortal.model.maintance.MaintenanceRequest;
import SmartCampusServicesPortal.model.maintance.MaintenanceStatus;
import SmartCampusServicesPortal.repository.maintenance.MaintenanceRequestRepository;
import SmartCampusServicesPortal.repository.room.RoomRepository;
import SmartCampusServicesPortal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceRequestService {
    private final MaintenanceRequestRepository maintenanceRequestRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public MaintenanceRequest createRequest(MaintenanceRequest request, Long reporterId) {
        request.setRoom(roomRepository.findById(request.getRoom().getId()).orElseThrow());
        request.setReporter(userRepository.findById(reporterId).orElseThrow());
        return maintenanceRequestRepository.save(request);
    }

    public MaintenanceRequest assignTechnician(Long requestId, Long technicianId) {
        MaintenanceRequest request = maintenanceRequestRepository.findById(Long.valueOf(requestId)).orElseThrow();
        request.setTechnician(userRepository.findById(technicianId).orElseThrow());
        request.setStatus(MaintenanceStatus.IN_PROGRESS);
        request.setAssignedAt(LocalDateTime.now());
        return maintenanceRequestRepository.save(request);
    }

    public MaintenanceRequest resolveRequest(Long requestId, String resolutionNotes) {
        MaintenanceRequest request = maintenanceRequestRepository.findById(requestId).orElseThrow();
        request.setStatus(MaintenanceStatus.RESOLVED);
        request.setResolutionNotes(resolutionNotes);
        request.setResolvedAt(LocalDateTime.now());
        return maintenanceRequestRepository.save(request);
    }

    public List<MaintenanceRequest> getRequestsByReporter(Long reporterId) {
        return maintenanceRequestRepository.findByReporterId(reporterId);
    }

    public List<MaintenanceRequest> getRequestsByTechnician(Long technicianId) {
        return maintenanceRequestRepository.findByTechnicianId(technicianId);
    }
}
