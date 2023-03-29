package com.deliverif.app.models.algorithms.astar;

public interface Scorer<T extends GraphNode> {
    float computeCost(T from, T to);
}