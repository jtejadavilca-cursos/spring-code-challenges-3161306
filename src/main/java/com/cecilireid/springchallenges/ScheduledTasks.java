package com.cecilireid.springchallenges;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScheduledTasks {

    private final CateringJobRepository cateringJobRepository;

    public ScheduledTasks(CateringJobRepository cateringJobRepository) {
        this.cateringJobRepository = cateringJobRepository;
    }

    //@Scheduled(fixedRate = 10000)
    @Scheduled(cron = "*/10 * * * * *")
    public void reportOrderStats() {
        long numOrders = cateringJobRepository.count();

        log.info("Number of orders: " + numOrders);
    }
}
