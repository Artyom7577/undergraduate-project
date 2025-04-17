package com.artyom.undergraduateproject.repository;

import java.util.Optional;

import com.artyom.undergraduateproject.entity.Supermarket;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface SupermarketRepository extends ReactiveMongoRepository<Supermarket, String> {
    Mono<Supermarket> findByLatitudeAndLongitude(double latitude, double longitude);
}
