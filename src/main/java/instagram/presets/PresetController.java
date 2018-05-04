package instagram.presets;

import instagram.model.PresetData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/presets/")
public class PresetController {

    @Autowired
    private PresetService presetService;

    @RequestMapping("/{presetName}")
    public PresetData getPreset(@PathVariable String presetName) {
        return presetService.getPreset(presetName);
    }

    @RequestMapping("/add")
    public String addPreset(@Valid @RequestBody PresetData data) {
        presetService.addPreset(data);
        return data.getName() + " preset has been saved successfully!";
    }

}
