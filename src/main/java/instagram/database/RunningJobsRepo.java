package instagram.database;

import instagram.model.Data;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RunningJobsRepo extends MongoRepository<Data, String> {

    @Override
    List<Data> findAll();

}
