package com.deliverif.app.models.algorithms.astar;

public interface Scorer<T extends GraphNode> {
    double computeCost(T from, T to);
}