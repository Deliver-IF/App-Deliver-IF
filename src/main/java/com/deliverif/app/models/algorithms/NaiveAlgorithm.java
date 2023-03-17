package com.deliverif.app.models.algorithms;

import com.deliverif.app.models.algorithms.astar.Graph;
import com.deliverif.app.models.algorithms.astar.HaversineScorer;
import com.deliverif.app.models.algorithms.astar.RouteFinder;
import com.deliverif.app.models.map.DeliveryRequest;
import com.deliverif.app.models.map.DeliveryTour;
import com.deliverif.app.models.map.Intersection;
import lombok.Getter;

import java.util.ArrayList;
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
        List<Intersection> route = new ArrayList<>();
        deliveryTour.getStops().stream().map(DeliveryRequest::getId).toList().forEach(System.out::println);
        for (int i = 0; i < deliveryTour.getStops().size()+1; i++) {
            if (i == 0) {
                route.addAll(routeFinder.findRoute(deliveryTour.getCityMap().getWarehouse(), deliveryTour.getStops().get(i).getIntersection()));
            }
            else if (i == deliveryTour.getStops().size()) {
                List<Intersection> tempRoute = routeFinder.findRoute(deliveryTour.getStops().get(i-1).getIntersection(), deliveryTour.getCityMap().getWarehouse());
                route.addAll(tempRoute.subList(1, tempRoute.size()));
            }
            else {
                List<Intersection> tempRoute = routeFinder.findRoute(deliveryTour.getStops().get(i-1).getIntersection(), deliveryTour.getStops().get(i).getIntersection());
                route.addAll(tempRoute.subList(1, tempRoute.size()));
            }
        }
        route.stream().map(Intersection::getId).toList().forEach(System.out::println);
    }
}
