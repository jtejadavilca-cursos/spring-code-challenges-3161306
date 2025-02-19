package com.cecilireid.springchallenges;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;


@RestController
@RequestMapping("cateringJobs")
public class CateringJobController {
    //private static final String IMAGE_API = "https://foodish-api.herokuapp.com";

    private static final String IMAGE_API = "https://fastly.picsum.photos";
    //private static final String IMAGE_API = "https://picsum.photos";
    private final CateringJobRepository cateringJobRepository;

    private final WebClient client;

    public CateringJobController(CateringJobRepository cateringJobRepository, WebClient.Builder webClientBuilder) {
        this.cateringJobRepository = cateringJobRepository;
        this.client = webClientBuilder.baseUrl(IMAGE_API).build();
    }

    @GetMapping
    @ResponseBody
    public List<CateringJob> getCateringJobs() {
        return cateringJobRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public CateringJob getCateringJobById(@PathVariable Long id) {
        if (cateringJobRepository.existsById(id)) {
            return cateringJobRepository.findById(id).get();
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/status/{status}")
    public List<CateringJob> getCateringJobsByStatus(@PathVariable Status status) {
        return this.cateringJobRepository.findByStatus(status)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public CateringJob createCateringJob(@RequestBody CateringJob job) {
        return this.cateringJobRepository.save(job);
    }

    @PutMapping("/{id}")
    public CateringJob updateCateringJob(@RequestBody CateringJob cateringJob, @PathVariable Long id) {
        return this.cateringJobRepository.findById(id)
                .map(job -> {
                    job.setCustomerName(cateringJob.getCustomerName());
                    job.setPhoneNumber(cateringJob.getPhoneNumber());
                    job.setEmail(cateringJob.getEmail());
                    job.setMenu(cateringJob.getMenu());
                    job.setNoOfGuests(cateringJob.getNoOfGuests());
                    job.setStatus(cateringJob.getStatus());
                    return this.cateringJobRepository.save(job);
                })
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/{id}")
    public CateringJob patchCateringJob(@PathVariable Long id, @RequestBody JsonNode json) {
        return this.cateringJobRepository.findById(id)
                .map(job -> {
                    if (json.has("customerName")) {
                        job.setCustomerName(json.get("customerName").asText());
                    }
                    if (json.has("phoneNumber")) {
                        job.setPhoneNumber(json.get("phoneNumber").asText());
                    }
                    if (json.has("email")) {
                        job.setEmail(json.get("email").asText());
                    }
                    if (json.has("menu")) {
                        job.setMenu(json.get("menu").asText());
                    }
                    if (json.has("noOfGuests")) {
                        job.setNoOfGuests(json.get("noOfGuests").asInt());
                    }
                    if (json.has("status")) {
                        job.setStatus(Status.valueOf(json.get("status").asText()));
                    }
                    return this.cateringJobRepository.save(job);
                })
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/surprise")
    public Mono<ResponseEntity<byte[]>> getSurpriseImage() {
        return client.get()
                .uri("/id/700/500/500.jpg?hmac=wjNWwfys2n_BG0QY66XIzgzha89OkBDGliKXILUpAG8")
                //.uri("/500")
                .retrieve()
                .bodyToMono(byte[].class) // Obtener la imagen como bytes
                .map(bytes -> ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG) // Asegurar que el tipo sea imagen
                        .body(bytes));
    }
}
