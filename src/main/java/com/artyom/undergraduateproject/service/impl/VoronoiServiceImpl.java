package com.artyom.undergraduateproject.service.impl;

import java.time.Duration;
import java.util.Collections;
import java.util.stream.Collectors;

import com.artyom.undergraduateproject.entity.Supermarket;
import com.artyom.undergraduateproject.repository.SupermarketRepository;
import com.artyom.undergraduateproject.service.VoronoiService;
import org.kynosarges.tektosyne.geometry.PointD;
import org.kynosarges.tektosyne.geometry.Voronoi;
import org.kynosarges.tektosyne.geometry.VoronoiResults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class VoronoiServiceImpl implements VoronoiService {

    private final SupermarketRepository supermarketRepository;

    public VoronoiServiceImpl(SupermarketRepository supermarketRepository) {
        this.supermarketRepository = supermarketRepository;
    }

    @Override
    public Mono<PointD[][]> calculateVoronoi() {

        // Adjust points outside the range
        double maxY = 40.261272;
        double minY = 40.084409;
        double maxX = 44.666528;
        double minX = 44.416956;

        // Get all points from the repository
        Flux<PointD> flux = supermarketRepository.findAll()
            .map(supermarket -> new PointD(supermarket.getLongitude(), supermarket.getLatitude()));

        // Find Voronoi diagram
        return flux.collectList()
            .map(points -> points.toArray(new PointD[0]))
            .flatMap(array -> {
                return Mono.fromSupplier(() -> {
                    VoronoiResults result = Voronoi.findAll(array);
                    return result.voronoiRegions();
                }).subscribeOn(Schedulers.boundedElastic());
            }).map(voronoiDiagram -> {
                for (int i = 0; i < voronoiDiagram.length; i++) {
                    for (int j = 0; j < voronoiDiagram[i].length; j++) {
                        PointD point = voronoiDiagram[i][j];
                        point = point.restrict(minX, minY, maxX, maxY);
                        voronoiDiagram[i][j] = point;
                    }
                }
                return voronoiDiagram;
            });
    }

}
