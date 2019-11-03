package com.emtdev.morph.bandit;

public interface BanditAlgorithm {

   int selectArm();

   void update(int arm, double reward);

   void renewArms(int numArms);

   void reset();


}
