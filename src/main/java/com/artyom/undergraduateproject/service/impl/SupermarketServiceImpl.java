package com.artyom.undergraduateproject.service.impl;

import static com.artyom.undergraduateproject.mapper.EntityDtoMapper.fromJsonNode;
import static com.artyom.undergraduateproject.service.util.Constants.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import com.artyom.undergraduateproject.entity.Supermarket;
import com.artyom.undergraduateproject.exceptions.ApplicationExceptions;
import com.artyom.undergraduateproject.mapper.EntityDtoMapper;
import com.artyom.undergraduateproject.repository.SupermarketRepository;
import com.artyom.undergraduateproject.service.SupermarketService;
import com.artyom.undergraduateproject.service.util.Constants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
class SupermarketServiceImpl implements SupermarketService {

    @Value("${google.map.base-url}")
    private String baseUrl;

    private final String apiKey;

    private final SupermarketRepository supermarketRepository;
    private final ObjectMapper objectMapper;

    SupermarketServiceImpl(SupermarketRepository supermarketRepository, ObjectMapper objectMapper) {
        this.supermarketRepository = supermarketRepository;
        this.objectMapper = objectMapper;
        this.apiKey = System.getenv("API_KEY");
    }

    @Override
    @Scheduled(cron = "0 1 22 22 * ?")
    public Mono<Void> updateSupermarketsFromGoogle() {
        log.info("Updating supermarkets started at: {}", LocalDateTime.now());

        return createSupermarketsFromGoogle()
            .doOnSuccess(ignored -> log.info("Updating supermarkets ended at: {}", LocalDateTime.now()));
    }

    @Override
    public Mono<Void> createSupermarketsFromGoogle() {
        AtomicInteger count = new AtomicInteger(0);

        return Flux.fromIterable(SUPERMARKETS)
            .flatMap(each -> {
                String body = each + PROMPT;

                return Mono.fromCallable(() -> Unirest.post(baseUrl)
                        .header(CONTENT_TYPE, "application/json")
                        .header(API, apiKey)
                        .header(FIELD_MASK_KEY, FIELD_MASK_LOCATION)
                        .body(textQuery(body))
                        .asString()
                    )
                    .subscribeOn(Schedulers.boundedElastic())
                    .onErrorResume(e -> {
                        log.warn("Request failed for supermarket {}: {}", each, e.getMessage());
                        return Mono.empty();
                    })
                    .flatMapMany(response -> {
                        if (response.getStatus() != HttpStatus.OK.value()) {
                            return Flux.empty();
                        }
                        try {
                            JsonNode rootNode = objectMapper.readTree(response.getBody());
                            JsonNode places = rootNode.path("places");
                            return Flux.fromIterable(places);
                        } catch (IOException e) {
                            return Flux.error(e);
                        }
                    })
                    .map(EntityDtoMapper::fromJsonNode)
                    .filterWhen(supermarket ->
                        supermarketExists(supermarket)
                            .map(exists -> !exists && isInYerevan(supermarket.getLatitude(), supermarket.getLongitude()))
                    ).flatMap(supermarket ->
                                  supermarketRepository.save(supermarket)
                                      .doOnSuccess(saved -> log.info("Added supermarket {} #{}", saved.getName(), count.incrementAndGet()))
                    );
            })
            .doOnError(throwable -> log.error("Something went wrong: {}", throwable.getMessage()))
            .then();
    }

    @Override
    public Flux<Supermarket> getSupermarkets() {
        return supermarketRepository.findAll()
            .doOnNext(supermarket -> log.info("({}, {}), ", supermarket.getLongitude(), supermarket.getLatitude()))
            .delayElements(Duration.ofMillis(15));
    }

    private Mono<Boolean> supermarketExists(Supermarket supermarket) {
        return supermarketRepository.findByLatitudeAndLongitude(supermarket.getLatitude(), supermarket.getLongitude())
            .hasElement();
    }


    private boolean isInYerevan(double latitude, double longitude) {
        double maxY = 40.261272;
        double minY = 40.084409;
        double maxX = 44.666528;
        double minX = 44.416956;
        return (latitude >= minY && latitude <= maxY) && (longitude >= minX && longitude <= maxX);
    }
}
