package instagram.css;

import instagram.model.Css;
import instagram.model.enums.CSS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/css")
public class CssController {

    @Autowired
    private CssService cssService;

    @GetMapping("/list")
    public List<Css> getCssList() {
        return cssService.getAll();
    }

    @GetMapping("/names")
    public CSS[] getCssNames() {
        return CSS.values();
    }

    @GetMapping("/{name}")
    public Css getCss(@PathVariable String name) {
        return cssService.getCssByName(name);
    }

    @PostMapping("/add")
    public Css add(@RequestBody Css css) {
        try {
            CSS.valueOf(css.getName());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
        return cssService.addCss(css);
    }

}
