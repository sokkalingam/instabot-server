package instagram.data;

import instagram.model.Css;
import instagram.model.enums.CSS;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CssData {

    private static Map<String, String> cssMap = new ConcurrentHashMap<>();

    public static synchronized void updateMap(Css css) {
        cssMap.put(css.getName(), css.getValue());
    }

    public static synchronized String get(CSS name) {
        return cssMap.get(name.name());
    }

    public static synchronized void buildMap(List<Css> list) {
        for (Css item : list)
            cssMap.put(item.getName(), item.getValue());
    }
}
