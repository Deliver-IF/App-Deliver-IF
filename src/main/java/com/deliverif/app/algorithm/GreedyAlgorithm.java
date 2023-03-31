package com.deliverif.app.algorithm;

import com.deliverif.app.algorithm.astar.GetLengthScorer;
import com.deliverif.app.algorithm.astar.Graph;
import com.deliverif.app.algorithm.astar.HaversineScorer;
import com.deliverif.app.algorithm.astar.RouteFinder;
import com.deliverif.app.exceptions.WrongDeliveryTimeException;
import com.deliverif.app.model.DeliveryRequest;
import com.deliverif.app.model.DeliveryTour;
import com.deliverif.app.model.Intersection;
import lombok.Getter;

import java.util.*;

@Getter
public class GreedyAlgorithm extends AbstractSearchOptimalTourAlgorithm {
    public static GreedyAlgorithm instance;
    private Graph<Intersection> graph;
    private RouteFinder<Intersection> routeFinder;
    public static GreedyAlgorithm getInstance() {
        if (instance == null) {
            instance = new GreedyAlgorithm();
        }
        return instance;
    }
    public void optimize(DeliveryTour deliveryTour) throws WrongDeliveryTimeException {
        if (deliveryTour.getStops().size() == 0) {
            deliveryTour.getTour().clear();
            return;
        }

        Set<Intersection> intersections = new HashSet<>(deliveryTour.getCityMap().getIntersections().values());
        graph = new Graph<>(intersections, deliveryTour.getCityMap().getConnections());
        routeFinder = new RouteFinder<>(graph, new GetLengthScorer(), new HaversineScorer());
        List<Intersection> route = new ArrayList<>();
        List<Intersection> currentRoute = new ArrayList<>();

        deliveryTour.getStops().sort(Comparator.comparing(DeliveryRequest::getStartTimeWindow));

        float arrivalTime = 8*60;
        for (int i = 0; i < deliveryTour.getStops().size()+1; i++) {
            if (i == 0) {
                currentRoute.addAll(routeFinder.findRoute(deliveryTour.getCityMap().getWarehouse(), deliveryTour.getStops().get(i).getIntersection()));
            } else if (i == deliveryTour.getStops().size()) {
                currentRoute.addAll(routeFinder.findRoute(deliveryTour.getStops().get(i-1).getIntersection(), deliveryTour.getCityMap().getWarehouse()));
            } else {
                currentRoute.addAll(routeFinder.findRoute(deliveryTour.getStops().get(i-1).getIntersection(), deliveryTour.getStops().get(i).getIntersection()));
            }

            for (int iRoute = 0; iRoute < currentRoute.size()-1; iRoute++) {
                arrivalTime += timeTaken(currentRoute.get(iRoute).getReachableIntersections().get(currentRoute.get(iRoute+1)).getLength(), 250);
            }
            if (i < deliveryTour.getStops().size()) {
                deliveryTour.getStops().get(i).setArrivalTime((int) arrivalTime);
                arrivalTime = deliveryTour.getStops().get(i).getArrivalTime() + DeliveryRequest.DELIVERY_TIME;
            }
            if(i == 0){
                route.addAll(currentRoute);
            }else {
                route.addAll(currentRoute.subList(1, currentRoute.size()));
            }
            currentRoute.clear();
        }
        deliveryTour.getTour().clear();
        for (int i = 0; i < route.size()-1; i++) {
            deliveryTour.getTour().add(route.get(i).getReachableIntersections().get(route.get(i+1)));
        }
    }
}
