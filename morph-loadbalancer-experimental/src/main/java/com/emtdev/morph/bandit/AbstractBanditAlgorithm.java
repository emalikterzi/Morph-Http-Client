package com.emtdev.morph.bandit;


public abstract class AbstractBanditAlgorithm implements BanditAlgorithm {

   int numArms;
   int[] counts;
   double[] values;
   private UpdateStrategy updateStrategy = new AverageUpdateStrategy();


   @Override
   public void update(int arm, double reward) {
      counts[arm]++;
      values[arm] = updateStrategy.update(counts[arm], values[arm], reward);

   }

   @Override
   public void renewArms(int numArms) {
      this.numArms = numArms;
      this.counts = new int[numArms];
      this.values = new double[numArms];
      this.reset();
   }

   @Override
   public void reset() {
      for (int i = 0; i < counts.length; i++) {
         counts[i] = 0;
         values[i] = 0;
      }
   }

   public abstract String toString();
}
