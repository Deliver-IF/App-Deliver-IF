package com.deliverif.app.algorithm;

import com.deliverif.app.algorithm.astar.Graph;
import com.deliverif.app.algorithm.astar.HaversineScorer;
import com.deliverif.app.algorithm.astar.RouteFinder;
import com.deliverif.app.model.DeliveryTour;
import com.deliverif.app.model.Intersection;
import javafx.util.Pair;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
public class NaiveAlgorithm implements AbstractSearchOptimalTourAlgorithm {
    public static NaiveAlgorithm instance;
    private Graph<Intersection> graph;
    private RouteFinder<Intersection> routeFinder;

    private NaiveAlgorithm() {
    }
    public static NaiveAlgorithm getInstance() {
        if (instance == null) {
            instance = new NaiveAlgorithm();
        }
        return instance;
    }
    public void optimize(DeliveryTour deliveryTour){
        Set<Intersection> intersections = deliveryTour.getCityMap().getIntersections();
        graph = new Graph<>(intersections, deliveryTour.getCityMap().getConnections());
        routeFinder = new RouteFinder<>(graph, new HaversineScorer(), new HaversineScorer());
        List<Intersection> route = new ArrayList<>();
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
        deliveryTour.getTour().clear();
        for (int i = 0; i < route.size()-1; i++) {
            deliveryTour.getTour().add(deliveryTour.getCityMap().getSegmentsMap().get(new Pair<>(route.get(i).getId(), route.get(i+1).getId())));
        }
    }
}
