package instagram.presets;

import instagram.database.UserPresetRepo;
import instagram.model.PresetData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PresetService {

    @Autowired private UserPresetRepo userPresetRepo;

    public PresetData getPreset(String presetName) {
        return userPresetRepo.findByName(presetName);
    }

    public void addPreset(PresetData data) {
        PresetData currentData = getPreset(data.getName());
        if (currentData != null) {
            userPresetRepo.delete(currentData);
        }
        userPresetRepo.save(data);
    }

}
