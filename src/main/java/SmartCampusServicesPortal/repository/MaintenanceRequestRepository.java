package SmartCampusServicesPortal.repository;

import SmartCampusServicesPortal.model.MaintenanceRequest;
import SmartCampusServicesPortal.model.User;
import SmartCampusServicesPortal.model.enums.MaintenanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceRequestRepository extends JpaRepository<MaintenanceRequest, Long> {
    List<MaintenanceRequest> findByReporter(User reporter);
    List<MaintenanceRequest> findByTechnician(User technician);

    @Query("SELECT mr FROM MaintenanceRequest mr WHERE " +
            "(:status IS NULL OR mr.status = :status) AND " +
            "(:roomId IS NULL OR mr.room.id = :roomId) AND " +
            "(:technicianId IS NULL OR mr.technician.id = :technicianId) " +
            "ORDER BY mr.priority DESC, mr.reportedAt")
    List<MaintenanceRequest> findWithFilters(String status, Long roomId, Long technicianId);
    List<MaintenanceRequest> findByStatusIn(List<MaintenanceStatus> statuses);
}
