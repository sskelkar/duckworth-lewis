package com.espncricinfo.cricket.domain;

public final class Score {

  private int runs;
  private Over overs;
  private int wickets;
  
  public Score(Integer runs, Integer wickets, Over overs) {
    if(runs < 0)
      throw new IllegalArgumentException("Runs can't be negative!");    
    if(wickets < 0 || wickets > 10)
      throw new IllegalArgumentException("Invalid number of wickets");
    
    this.runs = runs;
    this.wickets = wickets;
    this.overs = overs;
  }
  
  @Override
  public String toString() {
    return runs + "/" + wickets + " " + overs + " overs";
  }

  public int getRuns() {
    return runs;
  }

  public Over getOvers() {
    return overs;
  }

  public int getWickets() {
    return wickets;
  }
}
