package SmartCampusServicesPortal.service;

import SmartCampusServicesPortal.dto.RoomResponseDTO;
import SmartCampusServicesPortal.model.Building;
import SmartCampusServicesPortal.model.Room;
import SmartCampusServicesPortal.model.User;
import SmartCampusServicesPortal.repository.BuildingRepository;
import SmartCampusServicesPortal.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final BuildingRepository buildingRepository;

    public List<RoomResponseDTO> findAvailableRooms(LocalDateTime startTime, LocalDateTime endTime,
                                                    Integer minCapacity, Boolean hasProjector,
                                                    Boolean hasWhiteboard) {
        List<Room> rooms = roomRepository.findAvailableRooms(
                minCapacity != null ? minCapacity : 1,
                hasProjector,
                hasWhiteboard,
                startTime,
                endTime
        );
        return rooms.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }

    public List<Building> getAllBuildings() {
        return buildingRepository.findAll();
    }

    public List<Room> getRoomsUserCanAccess(User user) {
        // In a real implementation, this would filter based on user permissions
        // For now, return all rooms
        return roomRepository.findAll();
    }

    public List<Room> getAllBookableRooms() {
        return roomRepository.findAll();
    }

    public List<Room> getAllMaintainableRooms() {
        return roomRepository.findAll();
    }

    private RoomResponseDTO convertToDTO(Room room) {
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
