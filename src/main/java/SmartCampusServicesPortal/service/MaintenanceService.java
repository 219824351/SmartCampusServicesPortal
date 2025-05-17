package SmartCampusServicesPortal.service;

import SmartCampusServicesPortal.dto.MaintenanceRequestDTO;
import SmartCampusServicesPortal.dto.RoomResponseDTO;
import SmartCampusServicesPortal.model.MaintenanceRequest;
import SmartCampusServicesPortal.model.Room;
import SmartCampusServicesPortal.model.User;
import SmartCampusServicesPortal.model.enums.MaintenanceStatus;
import SmartCampusServicesPortal.repository.MaintenanceRequestRepository;
import SmartCampusServicesPortal.repository.RoomRepository;
import SmartCampusServicesPortal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaintenanceService {
    private final MaintenanceRequestRepository maintenanceRequestRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final RoomService roomService;
    private final RoomRepository roomRepository;


    public MaintenanceRequest createRequest(MaintenanceRequest request, Long userId) {
        User reporter = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        request.setReporter(reporter);
        request.setReportedAt(LocalDateTime.now());
        request.setStatus(MaintenanceStatus.OPEN);
        return maintenanceRequestRepository.save(request);
    }

    public MaintenanceRequest resolveRequest(Long id, String resolutionNotes) {
        MaintenanceRequest request = maintenanceRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintenance request not found"));
        request.setStatus(MaintenanceStatus.RESOLVED);
        request.setResolutionNotes(resolutionNotes);
        request.setResolvedAt(LocalDateTime.now());
        return maintenanceRequestRepository.save(request);
    }

    public List<MaintenanceRequest> getRequestsByReporter(Long userId) {
        User reporter = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return maintenanceRequestRepository.findByReporter(reporter);
    }

    public List<MaintenanceRequest> getRequestsByTechnician(Long userId) {
        User technician = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return maintenanceRequestRepository.findByTechnician(technician);
    }


    public MaintenanceRequest createMaintenanceRequest(MaintenanceRequest request) {
        request.setReportedAt(LocalDateTime.now());
        request.setStatus(MaintenanceStatus.OPEN);
        return maintenanceRequestRepository.save(request);
    }

    public List<MaintenanceRequestDTO> getUserMaintenanceRequests(User user, int limit) {
        return maintenanceRequestRepository.findByReporter(user).stream()
                .limit(limit)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MaintenanceRequestDTO> getAllMaintenanceRequests(String status, Long roomId, Long technicianId) {
        return maintenanceRequestRepository.findWithFilters(status, roomId, technicianId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void assignTechnician(Long requestId, Long technicianId) {
        MaintenanceRequest request = maintenanceRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Maintenance request not found"));

        User technician = userService.getUserById(technicianId);
        request.setTechnician(technician);
        request.setAssignedAt(LocalDateTime.now());
        request.setStatus(MaintenanceStatus.IN_PROGRESS);
        maintenanceRequestRepository.save(request);
    }

    public void updateMaintenanceStatus(Long id, MaintenanceStatus status, String resolutionNotes) {
        MaintenanceRequest request = maintenanceRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintenance request not found"));

        request.setStatus(status);
        if (status == MaintenanceStatus.RESOLVED || status == MaintenanceStatus.CLOSED) {
            request.setResolvedAt(LocalDateTime.now());
            request.setResolutionNotes(resolutionNotes);
        }
        maintenanceRequestRepository.save(request);
    }

    private MaintenanceRequestDTO convertToDTO(MaintenanceRequest request) {
        MaintenanceRequestDTO dto = new MaintenanceRequestDTO();
        dto.setId(request.getId());
        dto.setTitle(request.getTitle());
        dto.setDescription(request.getDescription());
        dto.setRoomId(request.getRoom().getId());
        dto.setRoomName(request.getRoom().getName());
        dto.setReporterId(request.getReporter().getId());
        dto.setReporterName(request.getReporter().getFirstName() + " " + request.getReporter().getLastName());

        if (request.getTechnician() != null) {
            dto.setTechnicianId(request.getTechnician().getId());
            dto.setTechnicianName(request.getTechnician().getFirstName() + " " + request.getTechnician().getLastName());
        }

        dto.setStatus(request.getStatus().name());
        dto.setPriority(request.getPriority().name());
        dto.setReportedAt(request.getReportedAt());
        dto.setResolvedAt(request.getResolvedAt());
        dto.setResolutionNotes(request.getResolutionNotes());
        return dto;
    }


    public List<RoomResponseDTO> getAllMaintainableRooms() {
        List<Room> allRooms = roomRepository.findAll();
        List<MaintenanceRequest> activeMaintenance = maintenanceRequestRepository.findByStatusIn(
                Arrays.asList(MaintenanceStatus.OPEN, MaintenanceStatus.IN_PROGRESS)
        );

        Set<Long> roomsUnderMaintenance = activeMaintenance.stream()
                .map(req -> req.getRoom().getId())
                .collect(Collectors.toSet());

        return allRooms.stream()
                .filter(room -> !roomsUnderMaintenance.contains(room.getId()))
                .map(this::convertToRoomDTO)
                .collect(Collectors.toList());
    }

    private RoomResponseDTO convertToRoomDTO(Room room) {
        RoomResponseDTO dto = new RoomResponseDTO();
        dto.setId(room.getId());
        dto.setBuildingId(room.getBuilding().getId());
        dto.setBuildingName(room.getBuilding().getName());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setName(room.getName());
        dto.setType(room.getType());
        dto.setCapacity(room.getCapacity());
        dto.setHasProjector(room.getHasProjector());
        dto.setHasWhiteboard(room.getHasWhiteboard());
        dto.setFloor(room.getFloor());
        dto.setDescription(room.getDescription());
        dto.setCreatedAt(room.getCreatedAt());
        dto.setUpdatedAt(room.getUpdatedAt());
        return dto;
    }
}

