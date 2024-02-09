package uk.gov.dwp.payments;

import com.itblueprints.sysagent.SysAgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminEP {

    //-------------------------------------------
    private final SysAgentService sysAgentService;
    private final BatchService batchService;
    private final ClnLineRepo clnLineRepo;
    private final BenefitPaymentRepo benefitPaymentRepo;

    @PostMapping("/run-job")
    public void runJob () {
        sysAgentService.runJob("uk.gov.dwp.payments.ProcessPaymentsJob");
    }

    @PostMapping("/delete-data")
    public void deleteData () {
        sysAgentService.resetCluster();
        clnLineRepo.deleteAll();
        benefitPaymentRepo.deleteAll();
    }

    @PostMapping("/create-data")
    public void createData () {
        batchService.createData();
    }
}
