package uk.gov.dwp.payments;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class ClnLine {
    @Id
    private String id;

    private String custProfile;

    private String fullData;
}
