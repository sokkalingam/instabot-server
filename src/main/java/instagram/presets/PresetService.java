package instagram.presets;

import instagram.database.UserPresetRepo;
import instagram.model.PresetData;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PresetService {

    @Autowired private UserPresetRepo userPresetRepo;

    public List<PresetData> getAll() {
        return userPresetRepo.findAll();
    }

    public PresetData getPreset(String presetName) {
        return userPresetRepo.findByName(presetName);
    }

    public void addPreset(PresetData data) {
        PresetData currentData = getPreset(data.getName());
        if (currentData != null) {
            userPresetRepo.delete(currentData);
        }
        data = processPreset(data);
        userPresetRepo.save(data);
    }

    public PresetData processPreset(PresetData data) {
        // Remove blank comments
        data.getData().comments = data.getData().comments.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
        return data;
    }

}
