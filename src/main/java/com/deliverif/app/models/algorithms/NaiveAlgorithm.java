package com.deliverif.app.models.algorithms;

import com.deliverif.app.models.algorithms.astar.Graph;
import com.deliverif.app.models.algorithms.astar.HaversineScorer;
import com.deliverif.app.models.algorithms.astar.RouteFinder;
import com.deliverif.app.models.map.DeliveryTour;
import com.deliverif.app.models.map.Intersection;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Getter
public class NaiveAlgorithm implements AbstractSearchOptimalTourAlgorithm {
    private Graph<Intersection> graph;
    private RouteFinder<Intersection> routeFinder;
    public void optimize(DeliveryTour deliveryTour){
        Set<Intersection> intersections = deliveryTour.getCityMap().getIntersections();
        graph = new Graph<>(intersections, deliveryTour.getCityMap().getConnections());
        routeFinder = new RouteFinder<>(graph, new HaversineScorer(), new HaversineScorer());
        List<Intersection> route = routeFinder.findRoute(deliveryTour.getCityMap().getWarehouse(), deliveryTour.getStops().get(0).getIntersection());
        route.stream().map(Intersection::getId).toList().forEach(System.out::println);
    }
}
