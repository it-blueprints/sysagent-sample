package uk.gov.dwp.payments;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface BenefitPaymentRepo extends MongoRepository<BenefitPayment, String> {

    Page<BenefitPayment> findAll(Pageable pageable);

    @Query("{ 'custProfile' : ?0, 'partition' : ?1, processed: false }")
    Page<BenefitPayment> findForPartition(String custProf, int prtn, Pageable pgRequest);
}
