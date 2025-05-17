package SmartCampusServicesPortal.service;

import SmartCampusServicesPortal.dto.BookingDTO;
import SmartCampusServicesPortal.model.Booking;
import SmartCampusServicesPortal.model.Room;
import SmartCampusServicesPortal.model.User;
import SmartCampusServicesPortal.model.enums.BookingStatus;
import SmartCampusServicesPortal.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final RoomService roomService;

    public Booking createBooking(User user, Room room, LocalDateTime startTime,
                                 LocalDateTime endTime, String purpose) {
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setRoom(room);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setPurpose(purpose);
        booking.setStatus(BookingStatus.CONFIRMED);
        return bookingRepository.save(booking);
    }

    public void cancelBooking(Long bookingId, User user) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only cancel your own bookings");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    public List<BookingDTO> getUserBookings(User user, int i) {
        return bookingRepository.findByUser(user).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookingDTO> getUpcomingUserBookings(Long user, int limit) {
        // First get all upcoming bookings
        List<Booking> bookings = bookingRepository.findFutureBookingsByUser(
                LocalDateTime.now(),
                user
        );

        // Then apply limit and convert
        return bookings.stream()
                .limit(limit)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public List<BookingDTO> getAllBookings(String status, Long roomId, LocalDate date) {
        return bookingRepository.findWithFilters(status, roomId, date).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void updateBookingStatus(Long id, BookingStatus status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(status);
        bookingRepository.save(booking);
    }

    private BookingDTO convertToDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        dto.setUserId(booking.getUser().getId());
        dto.setUserName(booking.getUser().getFirstName() + " " + booking.getUser().getLastName());
        dto.setRoomId(booking.getRoom().getId());
        dto.setRoomName(booking.getRoom().getName());
        dto.setBuildingName(booking.getRoom().getBuilding().getName());
        dto.setStartTime(booking.getStartTime());
        dto.setEndTime(booking.getEndTime());
        dto.setStatus(booking.getStatus().name());
        dto.setPurpose(booking.getPurpose());
        return dto;
    }

    public Object getAllBookableRooms() {
        return null;
    }
}
