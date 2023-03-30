package com.deliverif.app.algorithm;

import com.deliverif.app.algorithm.astar.HaversineScorer;
import com.deliverif.app.algorithm.astar.Scorer;
import com.deliverif.app.model.DeliveryRequest;
import com.deliverif.app.model.DeliveryTour;
import com.deliverif.app.model.Intersection;
import com.deliverif.app.model.Segment;
import lombok.Getter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class AntColonyAlgorithm implements AbstractSearchOptimalTourAlgorithm {
    private static AntColonyAlgorithm instance = null;
    public static AntColonyAlgorithm getInstance() {
        if (instance == null) {
            instance = new AntColonyAlgorithm();
        }
        return instance;
    }


    final int MAX_ITERATIONS_PER_ANTS = 100000;
    final float ANT_SPEED = 250.0f; // 250 m/min
    final float PHEROMONE_EVAPORATION = 0.1f; // 10% of pheromone intensity is evaporated each "minute"
    final float BASE_PHEROMONE_INTENSITY = 0.1f;
    final float PHEROMONE_DEPOSIT_INTENSITY = 0.3f;
    final int Q = 1;
    final float K = 0.02f; // As K increases, the amount of pheromone will decrease (and vice versa)


    public void optimize(DeliveryTour deliveryTour){
        // Create the pheromone matrix from deliveryTour.getCityMap()
        final Map<Segment, Float> pheromoneMatrix = new HashMap<>();
        final Map<Segment, Float> pheromoneLastUpdateMatrix = new HashMap<>();

        // Get the optimal number of ants
        final int numberOfAnts = getOptimalNumberOfAnts(deliveryTour);

        // Place the ants on the map (at least one ant on the warehouse)
        final List<Ant> ants = placeAnts(deliveryTour, numberOfAnts);

        // Create a priority queue with ants ordered by next movement time
        Comparator<Ant> antComparator = (ant1, ant2) -> {
            if (ant1.getNextMovementTime() == ant2.getNextMovementTime()) {
                return 0;
            } else if (ant1.getNextMovementTime() < ant2.getNextMovementTime()) {
                return -1;
            }

            return 1;
        };
        final PriorityQueue<Ant> antsQueue = new PriorityQueue<>(antComparator);
        antsQueue.addAll(ants);

        float lastMovementTime = 0;
        final List<Segment> segmentsBorrowed = new ArrayList<>();
        for (int i = 0; i < MAX_ITERATIONS_PER_ANTS * numberOfAnts; i++) {
            final Ant ant = antsQueue.poll();
            if (ant == null) {
                break;
            }

            if (ant.getNextMovementTime() != lastMovementTime && Math.abs(ant.getNextMovementTime() - lastMovementTime) > 0.0001) {
                // Some time passed since the last time movement, so we need to update the pheromone matrix
                //    -> Evaporate pheromone if needed
                //    -> Add pheromone to the segments borrowed by the ants

                // For all segments that have been borrowed by ants, update the pheromone value
                lastMovementTime = ant.getNextMovementTime();
                for (Segment segment : segmentsBorrowed) {
                    Float pheromoneIntensity = pheromoneMatrix.getOrDefault(segment, BASE_PHEROMONE_INTENSITY);

                    pheromoneIntensity = this.evaporatePheromone(pheromoneIntensity, pheromoneLastUpdateMatrix.getOrDefault(segment, 0.0f), lastMovementTime);

                    pheromoneMatrix.put(segment, pheromoneIntensity);
                    pheromoneLastUpdateMatrix.put(segment, lastMovementTime);
                }
                segmentsBorrowed.clear();
            }

            Segment segment = ant.move(pheromoneMatrix, BASE_PHEROMONE_INTENSITY);
            if (segment != null) {
                segmentsBorrowed.add(segment);
            }
            antsQueue.add(ant);
        }

        // Find the best tour
        final Ant bestAnt = ants.stream().min(Comparator.comparingDouble(Ant::getBestTourDuration)).orElse(null);
        if (bestAnt == null || bestAnt.getBestTour() == null) {
            throw new IllegalStateException("No route found");
        }

        ants.forEach(ant -> {
            System.out.println("Ant tour duration : " + ant.getBestTourDuration());
            System.out.println("Ant tour count : " + ant.getCountTour());
            System.out.println("Ant new best tour : " + ant.getCountBeatBestTour());
            System.out.println("Ant same best tour : " + ant.getCountSameBestTour());
            System.out.println("====================");
        });

        // Update the delivery tour
        deliveryTour.getTour().clear();
        deliveryTour.getTour().addAll(bestAnt.getBestTourSegments());
    }

    private float evaporatePheromone(float pheromoneIntensity, float lastUpdate, float currentTime) {
        if (currentTime == 0) {
            return pheromoneIntensity;
        }

        return (float) (pheromoneIntensity / Math.exp(K * (currentTime - lastUpdate))) + PHEROMONE_DEPOSIT_INTENSITY;
    }

    private int getOptimalNumberOfAnts(DeliveryTour deliveryTour) {
        return Math.max(20, deliveryTour.getStops().size());
    }

    private List<Ant> placeAnts(DeliveryTour deliveryTour, int numberOfAnts) {
        final List<Ant> ants = new ArrayList<>();

        // TODO : Change to place ants randomly on the map except for the first one which is placed on the warehouse
        for (int i = 0; i < numberOfAnts; i++) {
            ants.add(new Ant(ANT_SPEED, deliveryTour.getCityMap().getWarehouse(), deliveryTour.getStops()));
        }

        return ants;
    }

    private static class  Ant {
        private final float speed;
        private final Intersection initialIntersection;
        private final List<DeliveryRequest> intersectionsToVisit;
        private Map<Intersection, DeliveryRequest> currentChunkToVisit;
        private int timeWindowCurrentChunk;

        private float nextMovementTime;

        private float currentTourDuration;
        private boolean currentTourIsValid;
        private Intersection currentIntersection;
        private List<Intersection> tour;

        private final Map<Intersection, Integer> countVisitedIntersectionsSinceLastVisit;
        @Getter
        private Pair<List<Intersection>, Float> bestTour;


        // Only fo logs
        @Getter
        private int countTour = -1;
        @Getter
        private int countSameBestTour = 0;
        @Getter
        private int countBeatBestTour = -1;


        public Ant(float speed, Intersection initialIntersection, List<DeliveryRequest> intersectionsToVisit) {
            this.speed = speed;
            this.nextMovementTime = 0;
            this.initialIntersection = initialIntersection;
            this.countVisitedIntersectionsSinceLastVisit = new HashMap<>();

            this.intersectionsToVisit = new ArrayList<>(intersectionsToVisit);
            this.intersectionsToVisit.sort(Comparator.comparing(DeliveryRequest::getStartTimeWindow));
            this.startNewTour();
        }

        public float getBestTourDuration() {
            if (this.bestTour == null) {
                return Float.MAX_VALUE;
            }

            return this.bestTour.getValue();
        }
        public Collection<? extends Segment> getBestTourSegments() {
            if (this.bestTour == null) {
                return null;
            }

            List<Segment> segments = new ArrayList<>();

            List<Intersection> intersections = this.bestTour.getKey();
            segments.add(this.initialIntersection.getSegmentTo(intersections.get(0)));
            for (int i = 0; i < this.bestTour.getKey().size() - 1; i++) {
                segments.add(intersections.get(i).getSegmentTo(intersections.get(i + 1)));
            }

            return segments;
        }

        public Segment move(Map<Segment, Float> pheromoneMatrix, Float defaultPheromoneIntensity) {
            if (currentTourIsFinished()) {
                // The ant has finished its tour, so we need to start a new one
                startNewTour();
            }

            Intersection nextIntersection = selectBestNextIntersection(pheromoneMatrix, defaultPheromoneIntensity);
            final int countVisit = this.countVisitedIntersectionsSinceLastVisit.getOrDefault(nextIntersection, 0);
            if (countVisit > 5) {
                // Reduce attractiveness of the segment to avoid ants to get stuck in a loop a new time
                pheromoneMatrix.put(
                        currentIntersection.getSegmentTo(nextIntersection),
                        pheromoneMatrix.getOrDefault(currentIntersection.getSegmentTo(nextIntersection), defaultPheromoneIntensity) / 2
                );

                // The ant may be stuck in a loop, so we need to start a new tour and select a new next intersection
                this.startNewTour();
                return null;
            }
            this.countVisitedIntersectionsSinceLastVisit.put(nextIntersection, countVisit + 1);

            final Segment segment = currentIntersection.getSegmentTo(nextIntersection);
            if (segment == null) {
                throw new IllegalStateException("No segment found between " + currentIntersection + " and " + nextIntersection);
            }

            if (this.currentChunkToVisit.containsKey(nextIntersection)) {
                DeliveryRequest request = this.currentChunkToVisit.remove(nextIntersection);
                if (this.currentTourDuration / 60 > request.getStartTimeWindow() - 8 + 1) {
                    // The ant is late for the delivery
                    this.currentTourIsValid = false;
                }
                this.currentTourDuration += request.getDeliveryDuration();
                this.nextMovementTime = currentTourDuration;

                if (this.currentChunkToVisit.size() == 0) {
                    this.populateNextChunk(request.getStartTimeWindow());
                }

                this.countVisitedIntersectionsSinceLastVisit.clear();
            }

            this.tour.add(nextIntersection);
            this.currentIntersection = nextIntersection;
            this.currentTourDuration += segment.getLength() / this.speed;
            this.nextMovementTime = currentTourDuration;

            return segment;
        }

        private void populateNextChunk(int currentStartTimeWindow) {
            // Find first delivery request with a time window greater than the current one
            Integer nextTimeWindow = this.intersectionsToVisit.stream()
                    .map(DeliveryRequest::getStartTimeWindow)
                    .filter(startTimeWindow -> startTimeWindow > currentStartTimeWindow)
                    .findFirst()
                    .orElse(null);

            if (nextTimeWindow == null) {
                return;
            }

            this.timeWindowCurrentChunk = nextTimeWindow;
            this.currentChunkToVisit = this.intersectionsToVisit.stream()
                    .filter(deliveryRequest -> deliveryRequest.getStartTimeWindow() == this.timeWindowCurrentChunk)
                    .collect(Collectors.toMap(DeliveryRequest::getIntersection, Function.identity()));
        }

        private boolean currentTourIsFinished() {
            return currentIntersection == initialIntersection && this.currentChunkToVisit.isEmpty();
        }

        public void startNewTour() {
            if (this.currentTourIsValid() && this.currentTourIsBetter()) {
                this.countBeatBestTour++;
                this.bestTour = new ImmutablePair<>(this.tour, this.currentTourDuration);
            }

            this.countTour++;
            this.currentTourIsValid = true;
            this.currentTourDuration = 0;
            this.currentIntersection = initialIntersection;
            this.tour = new ArrayList<>(); // Don't call "clear" on the list because it will clear the best tour
            this.countVisitedIntersectionsSinceLastVisit.clear();

            this.timeWindowCurrentChunk = this.intersectionsToVisit.get(0).getStartTimeWindow();
            this.currentChunkToVisit = this.intersectionsToVisit.stream()
                    .filter(deliveryRequest -> deliveryRequest.getStartTimeWindow() == this.timeWindowCurrentChunk)
                    .collect(Collectors.toMap(DeliveryRequest::getIntersection, Function.identity()));
        }

        private boolean currentTourIsValid() {
            return this.currentTourIsValid
                    && !tour.isEmpty()
                    && this.currentChunkToVisit.isEmpty()
                    && this.initialIntersection == this.currentIntersection;
        }
        private boolean currentTourIsBetter() {
            if (bestTour != null && this.currentTourDuration == this.bestTour.getValue()) {
                this.countSameBestTour++;
            }

            return bestTour == null || this.currentTourDuration < bestTour.getRight();
        }

        public float getNextMovementTime() {
            return nextMovementTime;
        }

        private Intersection selectBestNextIntersection(Map<Segment, Float> pheromoneMatrix, Float defaultPheromoneIntensity) {
            // Evaluate probabilities for each intersection reachable from the current intersection
            final Scorer<Intersection> scorer = new HaversineScorer();
            final Map<Intersection, Float> probabilities = new HashMap<>();
            final Map<Pair<Intersection, Segment>, Float> distances = new HashMap<>();
            float minDistance = Float.MAX_VALUE;
            float maxDistance = 0f;
            for (Map.Entry<Intersection, Segment> entry : currentIntersection.getReachableIntersections().entrySet()) {
                final Segment segment = entry.getValue();
                final Intersection intersection = entry.getKey();

                float distance;
                if (this.currentChunkToVisit.isEmpty()) {
                    distance = scorer.computeCost(intersection, this.initialIntersection);
                } else {
                    distance = this.currentChunkToVisit.keySet().stream()
                            .map(intersectionToVisit -> scorer.computeCost(intersection, intersectionToVisit))
                            .min(Float::compare)
                            .orElse(0f);
                }

                distances.put(Pair.of(intersection, segment), distance);
                minDistance = Math.min(minDistance, distance);
                maxDistance = Math.max(maxDistance, distance);
            }

            float range = maxDistance - minDistance;
            float denominator = 0f;
            for (Map.Entry<Pair<Intersection, Segment>, Float> entry : distances.entrySet()) {
                final Pair<Intersection, Segment> data = entry.getKey();
                final float pheromoneIntensity = this.getPheromoneIntensity(pheromoneMatrix, defaultPheromoneIntensity, data.getRight());

                final float distanceOverMin = entry.getValue() - minDistance;

                if (range == 0) {
                    // If we fall here, it means that there is only 1 intersection reachable from the current intersection
                    // So we don't really need to compute probabilities.
                    probabilities.put(data.getLeft(), pheromoneIntensity);
                    denominator += pheromoneIntensity;
                } else {
                    final float probability = pheromoneIntensity * (range - distanceOverMin) / (range / 2);
                    probabilities.put(data.getLeft(), probability);
                    denominator += probability;
                }
            }

            if (probabilities.isEmpty()) {
                // Return last intersection of tour
                return this.tour.get(this.tour.size() - 1);
            } else {
                // Generate a random number between 0 and 1
                final float random = new Random().nextFloat();

                float sumProbabilities = 0;
                for (Map.Entry<Intersection, Float> entry : probabilities.entrySet()) {
                    // TODO : Explore the possibility to reduce probabilities on segments that have been visited recently
                    // TODO : Explore the possibility to add a random factor to the probabilities
                    final float probability = entry.getValue() / denominator;
                    sumProbabilities += probability;

                    if (random <= sumProbabilities) {
                        return entry.getKey();
                    }
                }

                // We should never reach this point
                throw new IllegalStateException("Ant is lost, help him please!");
            }
        }

        private float getPheromoneIntensity(Map<Segment, Float> pheromoneMatrix, float defaultPheromoneIntensity, Segment segment) {
            float intensity = pheromoneMatrix.getOrDefault(segment, defaultPheromoneIntensity);
            if (tour.size() > 0 && this.currentIntersection.getSegmentTo(tour.get(tour.size() - 1)) == segment) {
                // TODO ; Improve to not penalize the segment if the ant is on delivery request
                intensity = intensity * 0.001f;
            }

            return intensity / this.countVisitedIntersectionsSinceLastVisit.getOrDefault(segment.getEndIntersection(this.currentIntersection), 1);
        }
    }
}
