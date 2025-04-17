package com.artyom.undergraduateproject.service;

import org.kynosarges.tektosyne.geometry.PointD;
import reactor.core.publisher.Mono;

public interface VoronoiService {
    Mono<PointD[][]> calculateVoronoi();
}
