package instagram.logger;

import instagram.model.Data;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

public class LogService {

    private final static Logger logger = Logger.getLogger(LogService.class);
    private StringBuilder logBuilder = new StringBuilder();
    private StringBuilder errBuilder = new StringBuilder();

    public LogService append(Object text) {
        logBuilder.append(text).append("|");
        return this;
    }

    public LogService append(Data data) {
        logBuilder.append(data.username).append("|");
        return this;
    }

    public LogService appendData(Data data) {
        logBuilder.append(data.username).append(String.valueOf(data));
        return this;
    }

    public LogService appendErr(Object text) {
        errBuilder.append(text).append("|");
        return this;
    }

    public LogService appendErr(Data data) {
        errBuilder.append(data.username).append("|");
        return this;
    }

    public LogService log() {
        logger.info(logBuilder.toString());
        // Clear String Builder
        logBuilder.setLength(0);
        return this;
    }

    public LogService err() {
        logger.error(errBuilder.toString());
        // Clear String Builder
        errBuilder.setLength(0);
        return this;
    }


}
