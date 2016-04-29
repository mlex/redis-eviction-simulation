package com.instana;

import java.util.Arrays;

public class Main {
  public static final long TTL = 600L;
  public static final long WRITE_RATE = 60_000L;
  public static final long START_SIZE = TTL * WRITE_RATE;


  public static void main(String[] args) {
    singlePassWithPrint();
  }

  private static void singlePassWithPrint() {
    RedisEvictionSimulation simulation = new RedisEvictionSimulation(TTL, WRITE_RATE);
    long[] results = simulation.simulate(START_SIZE, 10_000);
    for (int i=0; i < results.length; ++i) {
      System.out.printf("%d: %d\n", i, results[i]);
    }
    long stableSize = results[results.length - 1];
    double expiredPercentage = ((double) stableSize - (WRITE_RATE * TTL)) / stableSize * 100;
    System.out.printf("WRITE-RATE: %d, STABLE-SIZE: %d, EXPIRED-PERCENTAGE: %f\n",
                      WRITE_RATE,
                      stableSize,
                      expiredPercentage);
  }

  private static void rateToNecessarySize() {
    for (int i=1; i <= 1000; ++i) {
      int writeRate = i * 1000;
      RedisEvictionSimulation simulation = new RedisEvictionSimulation(TTL, writeRate);
      long[] results = simulation.simulate(TTL * i * 1000, 1_000);
      long stableSize = results[results.length - 1];
      System.out.printf("%d: %d (%f)\n", writeRate, stableSize, (double)stableSize / writeRate / TTL);
    }
  }

}
