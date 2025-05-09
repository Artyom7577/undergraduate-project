package com.artyom.undergraduateproject.config;

import com.artyom.undergraduateproject.service.SupermarketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ApplicationConfig implements CommandLineRunner {
    private final SupermarketService supermarketService;

    public ApplicationConfig(SupermarketService supermarketService) {
        this.supermarketService = supermarketService;
    }

    @Override
    public void run(String... args) {
        supermarketService.updateSupermarketsFromGoogle()
            .subscribe(
                unused -> {},
                error -> log.error("❌ Error during supermarket update", error),
                () -> log.info("✅ Supermarket update completed")
            );
    }
}
