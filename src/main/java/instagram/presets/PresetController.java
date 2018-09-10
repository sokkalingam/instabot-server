package instagram.presets;

import instagram.model.PresetData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;q

@RestController
@RequestMapping("/api/presets")
public class PresetController {

    @Autowired
    private PresetService presetService;

    private static final String PRESET_NAME = "presetName";

    @RequestMapping("/")
    public PresetData getPresetByCookie(@CookieValue(value = PRESET_NAME, defaultValue = "") String presetName,
                                HttpServletResponse response) {
        return getPresetByName(presetName, response);
    }

    @RequestMapping("/{presetName}")
    public PresetData getPresetByName(@PathVariable String presetName, HttpServletResponse response) {

        if (!StringUtils.isEmpty(presetName))
            response.addCookie(new Cookie(PRESET_NAME, presetName));

        return presetService.getPreset(presetName);
    }

    @RequestMapping("/add")
    public String addPreset(@Valid @RequestBody PresetData data) {
        presetService.addPreset(data);
        return data.getName() + " preset has been saved successfully!";
    }

}
