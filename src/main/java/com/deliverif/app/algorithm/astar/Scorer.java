package com.deliverif.app.algorithm.astar;

public interface Scorer<T extends GraphNode> {
    float computeCost(T from, T to);
}