package SmartCampusServicesPortal.repository.maintenance;

import SmartCampusServicesPortal.model.maintance.MaintenanceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceRequestRepository extends JpaRepository<MaintenanceRequest, Long> {
    List<MaintenanceRequest> findByReporterId(Long reporterId);
    List<MaintenanceRequest> findByTechnicianId(Long technicianId);
    List<MaintenanceRequest> findByStatus(String status);
    List<MaintenanceRequest> findByPriority(String priority);
}
