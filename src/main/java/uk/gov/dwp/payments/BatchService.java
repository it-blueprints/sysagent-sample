package uk.gov.dwp.payments;

import com.itblueprints.sysagent.Arguments;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class BatchService {

    private final BenefitPaymentRepo benefitPaymentRepo;

    public void createData() {
        int seq = 0;
        for(val custProfile: List.of("resident_GB", "resident_NI")) {
            for(int i = 1; i < 6; i++){
                for(int j = 0; j < 1000; j++) {
                    val pmt = new BenefitPayment();
                    pmt.setSeq(seq++);
                    pmt.setCustProfile(custProfile);
                    pmt.setPartition(i);
                    pmt.setData1(nextRnd(words1));
                    pmt.setData2(nextRnd(words2));
                    benefitPaymentRepo.save(pmt);
                }
            }
        }
    }

    private String nextRnd(String[] words){
        return words[random.nextInt(10)];
    }
    private static final Random random = new Random();
    private static final String [] words1 = new String[] {"Tiger", "Goat", "Bird", "Lion", "Rhino", "Dog", "Cat", "Lizard", "Lamb", "Wolf"};
    private static final String [] words2 = new String[] {"Sky", "River", "Mountain", "Hill", "Valley", "Road", "Avenue", "Forest", "City", "County"};
}
