package com.artyom.undergraduateproject.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document("supermarket")
public class Supermarket {
    @Id
    private String id;
    private String name;
    private double longitude;
    private double latitude;

    public Supermarket(String id, String name, double longitude, double latitude) {
        this.id = id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Supermarket() {
    }

    @Override
    public String toString() {
        return "Supermarket{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", longitude=" + longitude +
            ", latitude=" + latitude +
            '}';
    }
}
