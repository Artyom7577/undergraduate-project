package com.artyom.undergraduateproject.service;

import com.artyom.undergraduateproject.entity.Supermarket;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SupermarketService {
    Mono<Void> updateSupermarketsFromGoogle();

    Mono<Void> createSupermarketsFromGoogle();

    Flux<Supermarket> getSupermarkets();
}
