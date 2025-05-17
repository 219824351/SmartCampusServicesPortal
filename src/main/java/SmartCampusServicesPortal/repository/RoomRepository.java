package SmartCampusServicesPortal.repository;

import SmartCampusServicesPortal.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findAll();
    @Query("SELECT r FROM Room r WHERE r.capacity >= ?1 " +
            "AND (?2 IS NULL OR r.hasProjector = ?2) " +
            "AND (?3 IS NULL OR r.hasWhiteboard = ?3) " +
            "AND r.id NOT IN (" +
            "SELECT b.room.id FROM Booking b " +
            "WHERE b.status = 'CONFIRMED' " +
            "AND ((b.startTime BETWEEN ?4 AND ?5) OR (b.endTime BETWEEN ?4 AND ?5) " +
            "OR (b.startTime <= ?4 AND b.endTime >= ?5)))")
    List<Room> findAvailableRooms(int minCapacity, Boolean hasProjector, Boolean hasWhiteboard,
                                  LocalDateTime startTime, LocalDateTime endTime);

    List<Room> findByBuildingId(Long buildingId);
}
