package com.deliverif.app.algorithm.astar;

import com.deliverif.app.model.Intersection;

public class GetLengthScorer implements Scorer<Intersection> {
    @Override
    public float computeCost(Intersection from, Intersection to) {
        return from.getReachableIntersections().get(to).getLength();
    }
}