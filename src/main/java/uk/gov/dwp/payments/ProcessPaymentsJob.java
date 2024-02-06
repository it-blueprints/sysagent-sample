package uk.gov.dwp.payments;

import com.itblueprints.sysagent.Arguments;
import com.itblueprints.sysagent.job.Job;
import com.itblueprints.sysagent.job.JobPipeline;
import com.itblueprints.sysagent.scheduling.ScheduledJob;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProcessPaymentsJob implements ScheduledJob {

    private final Step1_ProcessCustomer step1ProcessCustomer;
    private final Step2_CreateHbs1File step2CreateHbs1File;

    @Override
    public JobPipeline getPipeline() {

        return JobPipeline.create()
                .firstStep(step1ProcessCustomer)
                .nextStep(step2CreateHbs1File);
    }

    @Override
    public void onStart(Arguments jobArguments) {
        jobArguments.put("pmtProfile", "state_pension");
    }

    @Override
    public String getCron() {
        return "0 1 * * *";
    }
}
