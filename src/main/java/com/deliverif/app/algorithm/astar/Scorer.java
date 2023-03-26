package com.deliverif.app.algorithm.astar;

public interface Scorer<T extends GraphNode> {
    double computeCost(T from, T to);
}