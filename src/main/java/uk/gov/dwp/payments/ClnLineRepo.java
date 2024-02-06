package uk.gov.dwp.payments;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ClnLineRepo extends MongoRepository<ClnLine, String> {

    List<ClnLine> findAll();

    List<ClnLine> findByCustProfile(String custProfile);
}
