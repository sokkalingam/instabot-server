package instagram.css;

import instagram.data.CssData;
import instagram.database.CssRepo;
import instagram.model.Css;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CssService {

    @Autowired
    private CssRepo cssRepo;

    public List<Css> getAll() {
        return cssRepo.findAll();
    }

    public Css getCssByName(String name) {
        return cssRepo.findByName(name);
    }

    public Css addCss(Css css) {
        Css res = cssRepo.save(css);
        CssData.updateMap(res);
        return css;
    }

    public void buildCssMap() {
        CssData.buildMap(getAll());
    }

}
