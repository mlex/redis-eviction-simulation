package com.instana;

import java.util.Random;

public class RedisEvictionSimulation {

  public static int TAKE_COUNT = 20;
  public static double FINISH_PROB = 0.25;
  public static int TAKES_PER_SEC = 10;

  private final Random random;
  private final long ttl;
  private final long writeRate;

  public RedisEvictionSimulation(long ttl, long writeRate) {
    this(new Random(), ttl, writeRate);
  }

  public RedisEvictionSimulation(Random random, long ttl, long writeRate) {
    this.random = random;
    this.ttl = ttl;
    this.writeRate = writeRate;
  }

  public long[] simulate(long startSize, int iterations) {
    int iterationCounter = 0;
    long[] result = new long[iterations];
    double currentSize = (double) startSize;
    double currentProb;
    for (iterationCounter=0; iterationCounter < iterations; ++iterationCounter) {
      currentProb = (currentSize - writeRate*ttl) / currentSize;
      //System.out.println(currentProb);
      currentSize = Math.max(writeRate, currentSize + writeRate - TAKES_PER_SEC * takeRandomUntil(TAKE_COUNT, currentProb, FINISH_PROB));
      result[iterationCounter] = (long) currentSize;
    }
    return result;
  }

  public int takeRandomUntil(int count, double prob, double finishProb) {
    int result = 0;
    int add = 0;
    do {
      add = takeRandom(count, prob);
      result += add;
    } while (add >= finishProb * count && count < Integer.MAX_VALUE);
    return result;
  }

  public int takeRandom(int count, double prob) {
    int result = 0;
    for (int i=0; i<count; ++i) {
      if (random.nextDouble() < prob) {
        ++result;
      }
    }
    return result;
  }
}
