package SmartCampusServicesPortal.service;

import SmartCampusServicesPortal.model.SystemSettings;
import SmartCampusServicesPortal.repository.SystemSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SystemSettingsService {
    private final SystemSettingsRepository systemSettingsRepository;

    public List<SystemSettings> getAllSettings() {
        return systemSettingsRepository.findAll();
    }

    public void updateSetting(String key, String value) {
        Optional<SystemSettings> setting = systemSettingsRepository.findBySettingKey(key);
        if (setting.isPresent()) {
            SystemSettings existing = setting.get();
            existing.setSettingValue(value);
            systemSettingsRepository.save(existing);
        } else {
            throw new RuntimeException("Setting not found");
        }
    }

    public String getSettingValue(String key) {
        return systemSettingsRepository.findBySettingKey(key)
                .map(SystemSettings::getSettingValue)
                .orElse(null);
    }
}
