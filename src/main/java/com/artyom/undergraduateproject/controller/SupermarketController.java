package com.artyom.undergraduateproject.controller;

import com.artyom.undergraduateproject.dto.Point;
import com.artyom.undergraduateproject.dto.SupermarketDto;
import com.artyom.undergraduateproject.mapper.EntityDtoMapper;
import com.artyom.undergraduateproject.service.SupermarketService;
import com.artyom.undergraduateproject.service.VoronoiService;
import org.kynosarges.tektosyne.geometry.PointD;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SupermarketController {

    private final SupermarketService supermarketService;
    private final VoronoiService voronoiService;

    public SupermarketController(SupermarketService supermarketService, VoronoiService voronoiService) {
        this.supermarketService = supermarketService;
        this.voronoiService = voronoiService;
    }

    @GetMapping(value = "/supermarkets", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<SupermarketDto> getSupermarkets() {
        return supermarketService.getSupermarkets()
            .map(EntityDtoMapper::toDto);
    }

    @GetMapping(value = "/polygons")
    public Mono<List<List<Point>>> getVoronoiPolygons() {
        return voronoiService.calculateVoronoi()
            .map(this::googleReturn);
    }

    private List<List<Point>> googleReturn(PointD[][] pointDS) {
        return Arrays.stream(pointDS)
            .map(pointDS1 ->
                     Arrays.stream(pointDS1)
                         .map(pointD -> new Point(pointD.x, pointD.y))
                         .toList())
            .toList();
    }
}
