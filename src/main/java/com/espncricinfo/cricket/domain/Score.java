package com.espncricinfo.cricket.domain;

public final class Score {

  private int runs;
  private Over overs;
  private int wickets;
  
  public static final Score INITIAL_SCORE = new Score(0, 0, Over.valueOf("0"));
  public Score(Integer runs, Over overs) {
    this(runs, 0, overs);
  }
  
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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((overs == null) ? 0 : overs.hashCode());
    result = prime * result + runs;
    result = prime * result + wickets;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Score other = (Score) obj;
    if (overs == null) {
      if (other.overs != null)
        return false;
    }
    else if (!overs.equals(other.overs))
      return false;
    if (runs != other.runs)
      return false;
    if (wickets != other.wickets)
      return false;
    return true;
  }
  

  
  
}
