package SmartCampusServicesPortal.controller.building;


import SmartCampusServicesPortal.model.Building;
import SmartCampusServicesPortal.service.building.BuildingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buildings")
@RequiredArgsConstructor
public class BuildingController {

    private final BuildingService buildingService;

    @GetMapping
    public ResponseEntity<List<Building>> getAllBuildings() {
        return ResponseEntity.ok(buildingService.getAllBuildings());
    }

    @GetMapping("/{code}")
    public ResponseEntity<Building> getBuildingByCode(@PathVariable String code) {
        return ResponseEntity.ok(buildingService.getBuildingByCode(code));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Building> createBuilding(@RequestBody Building building) {
        return ResponseEntity.ok(buildingService.createBuilding(building));
    }
}
