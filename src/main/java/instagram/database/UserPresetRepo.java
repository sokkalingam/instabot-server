package instagram.database;

import instagram.model.PresetData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserPresetRepo extends MongoRepository<PresetData, String> {

    PresetData findByName(String name);

    List<PresetData> findAll();

}
