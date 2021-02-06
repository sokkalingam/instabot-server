package instagram.data;

import instagram.model.Css;
import instagram.model.enums.CSS;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CssData {

    private static final Map<String, String> cssMap = new ConcurrentHashMap<>();

    static {
        buildMap();
    }

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

    public static void buildMap() {
        cssMap.put(CSS.LIKE_BUTTON.name(), "div.eo2As button.wpO6b");
        cssMap.put(CSS.UNLIKE_BUTTON.name(), "div.eo2As button.wpO6b");
        cssMap.put(CSS.ARTICLE.name(), "article[role=presentation]");
        cssMap.put(CSS.PROFILE_NAME.name(), "a.sqdOP");
        cssMap.put(CSS.RIGHT_ARROW.name(), "a.coreSpriteRightPaginationArrow");
        cssMap.put(CSS.ERROR.name(), "h2._7UhW9");
    }
}
