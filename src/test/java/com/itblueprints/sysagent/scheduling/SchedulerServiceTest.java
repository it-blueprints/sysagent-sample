package com.itblueprints.sysagent.scheduling;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.support.CronExpression;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SchedulerServiceTest {

    @Test
    public void testCronExpressions() {

        val cron = CronExpression.parse("0 0 * * * *");
        val time = LocalDateTime.of(2024,1,1,10,0,0);

        System.out.println(cron.next(time.minusSeconds(6)));

    }

}