package com.emtdev.morph.bandit;

public interface UpdateStrategy {

   public double update(int numCounts, double value, double reward);
}
