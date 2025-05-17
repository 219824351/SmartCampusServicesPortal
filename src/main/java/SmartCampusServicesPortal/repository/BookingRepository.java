package SmartCampusServicesPortal.repository;

import SmartCampusServicesPortal.model.Booking;
import SmartCampusServicesPortal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);

    @Query("SELECT b FROM Booking b WHERE " +
            "(:status IS NULL OR b.status = :status) AND " +
            "(:roomId IS NULL OR b.room.id = :roomId) AND " +
            "(:date IS NULL OR DATE(b.startTime) = :date) " +
            "ORDER BY b.startTime")
    List<Booking> findWithFilters(String status, Long roomId, LocalDate date);

    List<Booking> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT b FROM Booking b WHERE b.startTime > :startTime AND b.user.id = :userId ORDER BY b.startTime ASC")
    List<Booking> findFutureBookingsByUser(@Param("startTime") LocalDateTime startTime, @Param("userId") Long userId);
}
