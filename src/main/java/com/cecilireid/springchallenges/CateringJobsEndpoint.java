package com.cecilireid.springchallenges;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Endpoint(id = "order-by-status")
public class CateringJobsEndpoint {
    private final CateringJobRepository cateringJobRepository;

    public CateringJobsEndpoint(CateringJobRepository cateringJobRepository) {
        this.cateringJobRepository = cateringJobRepository;
    }
    @ReadOperation
    public Map<Status, Integer> getOrderByStatus() {
        var orders = cateringJobRepository.findAll();
        return Stream.of(Status.values())
                .collect(Collectors.toMap(status -> status, status -> (int) orders.stream().filter(order -> order.getStatus() == status).count()));
    }
}
