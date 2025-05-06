package SmartCampusServicesPortal.repository.room;

import SmartCampusServicesPortal.model.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByBuildingId(Long buildingId);
    Optional<Room> findByBuildingIdAndRoomNumber(Long buildingId, String roomNumber);
}
