package uk.gov.dwp.payments;

import com.itblueprints.sysagent.Arguments;
import com.itblueprints.sysagent.step.PartitionedStep;
import com.itblueprints.sysagent.step.StepContext;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Step2_CreateHbs1File implements PartitionedStep {

    private final ClnLineRepo clnLineRepo;
    private static final DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;
    private BufferedWriter writer;

    @Value("${app.out-folder}")
    private String outFolder;

    //---------------------------------------------------------------
    @Override
    public List<Arguments> getPartitionArgumentsList(Arguments jobArguments) {
        val partitions = new ArrayList<Arguments>();
        for(val custProfile: List.of("resident_GB", "resident_NI")) {
            val part = new Arguments();
            part.put("custProfile", custProfile);
            partitions.add(part);
        }
        return partitions;
    }

    //---------------------------------------------------------------
    @Override
    public void run(StepContext context) {
        val args = context.getArguments();
        val pmtProfile = args.getString("pmtProfile");
        val custProfile = args.getString("custProfile");
        val runAt = args.getTime("jobStartedAt");
        val fileName = pmtProfile+"_"+custProfile+"_"+runAt.format(formatter)+".txt";

        try {
            writer = new BufferedWriter(new FileWriter(outFolder +"/" + fileName));
            val clnLines = clnLineRepo.findByCustProfile(custProfile);
            for (val clnLine : clnLines) {
                writer.write(clnLine.getFullData()+"\n");
            }
            writer.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
