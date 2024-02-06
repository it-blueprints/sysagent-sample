package uk.gov.dwp.payments;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
public class BenefitPayment {

    @Id
    private String id;

    private int seq;

    private int partition;

    private String custProfile;

    private String data1;

    private String data2;

    boolean processed = false;
}
