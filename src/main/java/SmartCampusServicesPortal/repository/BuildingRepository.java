package SmartCampusServicesPortal.repository;

import SmartCampusServicesPortal.model.Building;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BuildingRepository extends JpaRepository<Building, Long> {
    Optional<Building> findByCode(String code);
}
