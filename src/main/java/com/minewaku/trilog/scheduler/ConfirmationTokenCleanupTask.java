package com.minewaku.trilog.scheduler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.minewaku.trilog.repository.ConfirmationTokenRepository;
import com.minewaku.trilog.util.LogUtil;

@Component
public class ConfirmationTokenCleanupTask {

    private final ConfirmationTokenRepository confirmationTokenRepository;
   
    public ConfirmationTokenCleanupTask(ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    // CRON expression
    /*
    	1: second (0, 59)
    	2: minute (0, 59)
    	3: hour (0, 23)
    	4: day of the month (1-31)
    	5: month (1-12 or JAN-DEC)
    	6: day of the week (1-7 or SUN-SAT)
    	7: optional year (empty or 1970-2099)
    	
    	number: run at that specific time unit
    	*: run at every time unit
    	?: don't care about that specific time unit
    */
    @Scheduled(cron = "0 0 * * * ?") 
    public void cleanUpExpiredTokens() {
        LocalDateTime now = ZonedDateTime.now(ZoneId.of("Z")).toLocalDateTime();
        confirmationTokenRepository.deleteExpiredTokens(now);
        LogUtil.LOGGER.info("Expired confirmation tokens have been deleted.");
    }
}
