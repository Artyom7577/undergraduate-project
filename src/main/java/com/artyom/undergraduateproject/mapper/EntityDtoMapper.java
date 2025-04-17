package com.artyom.undergraduateproject.mapper;

import com.artyom.undergraduateproject.dto.SupermarketDto;
import com.artyom.undergraduateproject.entity.Supermarket;
import com.fasterxml.jackson.databind.JsonNode;

public class EntityDtoMapper {

    public static Supermarket toEntity(SupermarketDto supermarketDto) {
        Supermarket supermarket = new Supermarket();
        supermarket.setId(supermarketDto.id());
        supermarket.setName(supermarketDto.name());
        supermarket.setLatitude(supermarketDto.latitude());
        supermarket.setLongitude(supermarketDto.longitude());
        return supermarket;
    }

    public static SupermarketDto toDto(Supermarket supermarket) {
        return new SupermarketDto(
            supermarket.getId(),
            supermarket.getName(),
            supermarket.getLongitude(),
            supermarket.getLatitude()
        );
    }

    public static Supermarket fromJsonNode(JsonNode place) {
        Supermarket supermarket = new Supermarket();
        supermarket.setName(place.path("displayName").path("text").asText());
        JsonNode coordinates = place.path("location");
        supermarket.setLatitude(coordinates.get("latitude").asDouble());
        supermarket.setLongitude(coordinates.get("longitude").asDouble());
        return supermarket;
    }
}
