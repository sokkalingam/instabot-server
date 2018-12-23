package instagram.database;

import instagram.model.Css;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CssRepo extends MongoRepository<Css, String> {

    Css findByName(String name);

    List<Css> findAll();

}
