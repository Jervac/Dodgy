package com.dodgy.core;

import java.util.Random;

public class Shaker {

  public float time;
  Random random;
  float x, y;
  float current_time;
  float power;
  float current_power;

  public Shaker(){
    time = 0;
    current_time = 0;
    power = 0;
    current_power = 0;
  }
  
  public void shake(float power, float time) {
    random = new Random();
    this.power = power;
    this.time = time;
    this.current_time = 0;
  }
        
  public void tick(float delta) {
    if(current_time < time) {
    	current_time += delta * 1000;
      current_power = power * ((time - current_time) / time);
      x = (random.nextFloat() - 0.5f) * 2 * current_power;
      y = (random.nextFloat() - 0.5f) * 2 * current_power;
      Dodgy.cam.translate(-x, -y);
      
    } 
  }      
}