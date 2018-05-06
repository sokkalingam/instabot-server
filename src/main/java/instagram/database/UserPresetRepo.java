package instagram.database;

import instagram.model.Data;
import instagram.model.PresetData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserPresetRepo extends MongoRepository<PresetData, String> {

    PresetData findByName(String name);

}