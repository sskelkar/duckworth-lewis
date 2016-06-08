package com.espncricinfo.cricket.domain;

import java.util.regex.Pattern;

public final class Over {

  private static final String OVER_PATTERN = "^[0-9]+(.[0-6])?$";
  private int balls;
  private String representation;
  
  private Over(int balls) {
    this(balls, String.valueOf(balls / 6).concat(".").concat(String.valueOf(balls % 6)));
  }
  
  private Over(int balls, String representation) {
    this.balls = balls;
    this.representation = representation;
  }
  
  public static Over valueOf(String over) {
    if(!Pattern.matches(OVER_PATTERN, over))
      throw new IllegalArgumentException("Input doesn't represent a valid over");
    int decimalIndex = over.indexOf(".");
    int balls;
    if(decimalIndex > -1)
      balls = Integer.valueOf(over.substring(0, decimalIndex)) * 6 + Integer.valueOf(over.substring(decimalIndex+1));
    else {
      balls = Integer.valueOf(over) * 6;
      over = over + ".0";
    }
    return new Over(balls, over);
  }
  
  public Integer getBalls() {
    return balls;
  }

  @Override
  public String toString() {
    return representation;
  }
  
  @Override
  public boolean equals(Object o) {    
    if(o == null || !o.getClass().equals(this.getClass()))
      return false;
    if(o == this)
      return true;
    Over over = (Over) o;
    
    return this.getBalls().equals(over.getBalls());
  }
  
  @Override
  public int hashCode() {
    return this.balls;
  }
  
  public Over add(Over o) {
    if(o == null)
      return this;
    
    return new Over(this.getBalls() + o.getBalls());
  }
  
  public Over subtract(Over o) {
    if(o == null)
      return this;
    
    return new Over(this.getBalls() - o.getBalls());
  }
}
