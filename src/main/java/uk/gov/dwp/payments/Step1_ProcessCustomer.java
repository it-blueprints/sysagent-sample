package uk.gov.dwp.payments;

import com.itblueprints.sysagent.Arguments;
import com.itblueprints.sysagent.step.BatchStepContext;
import com.itblueprints.sysagent.step.PartitionedBatchStep;
import com.itblueprints.sysagent.step.StepContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class Step1_ProcessCustomer implements PartitionedBatchStep<BenefitPayment, Step1_ProcessCustomer.Result> {

    private final BenefitPaymentRepo benefitPaymentRepo;
    private final ClnLineRepo clnLineRepo;

    //---------------------------------------------------------------
    @Override
    public List<Arguments> getPartitionArgumentsList(Arguments jobArgs) {
        log.info("Getting paritions");
        val partitionArgs = new ArrayList<Arguments>();
        for(val custProfile: List.of("resident_GB", "resident_NI")) {
            for(int i = 1; i < 6; i++){
                val part = new Arguments();
                part.put("custProfile", custProfile);
                part.put("partition", i);
                partitionArgs.add(part);
            }
        }
        return partitionArgs;
    }

    //------------------------------------------------------------
    @Override
    public Page<BenefitPayment> readPageOfItems(Pageable pgRequest, BatchStepContext context) {
        log.info("Executing readChunkOfItems for page="+pgRequest.getPageNumber()+" with arguments "+context.getArguments());
        val custProf = context.getArguments().getString("custProfile");
        val prtn = context.getArguments().getInt("partition");
        val result = benefitPaymentRepo.findForPartition(custProf, prtn, pgRequest);
        return result;
    }

    //------------------------------------------------------------------------------
    @Override
    public Step1_ProcessCustomer.Result processItem(BenefitPayment item, BatchStepContext context) {
        //log.info("Processing item");
        val cln = new ClnLine();
        cln.setFullData(item.getSeq()+", "+item.getCustProfile()+", "+item.getPartition()+", "+item.getData1()+" "+item.getData2());
        cln.setCustProfile(item.getCustProfile());
        item.processed = true;
        val result = new Result();
        result.benefitPayment = item;
        result.clnLine = cln;
        return result;
    }

    //---------------------------------------------------------------
    @Override
    public void writePageOfItems(Page<Result> page, BatchStepContext context) {
        log.info("Executing writeChunkOfItems for page="+page.getNumber());
        val pmts = page.stream().map(r -> r.benefitPayment).collect(Collectors.toList());
        val clnLines = page.stream().map(r -> r.clnLine).collect(Collectors.toList());
        benefitPaymentRepo.saveAll(pmts);
        clnLineRepo.saveAll(clnLines);
    }


    //=========================
    public static class Result {
        public BenefitPayment benefitPayment;
        public ClnLine clnLine;
    }

}
