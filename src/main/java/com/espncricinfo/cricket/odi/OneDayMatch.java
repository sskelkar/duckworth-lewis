package com.espncricinfo.cricket.odi;

import com.espncricinfo.cricket.domain.Over;
import com.espncricinfo.cricket.domain.Score;

public class OneDayMatch {
  private static final Over OVER_LIMIT = Over.valueOf("50"); 
  private Score firstInningScore;
  private Score secondInningScore;
  
  
  
  public void setFirstInningScore(Score score) {
    if(score.getOvers().getBalls() > OVER_LIMIT.getBalls())
      throw new IllegalArgumentException("Maximum 50 overs allowed in a one day game");
    
    this.firstInningScore = score;
  }
  
  public void setSecondInningScore(Score score) {
    if(score.getOvers().getBalls() > OVER_LIMIT.getBalls())
      throw new IllegalArgumentException("Maximum 50 overs allowed in a one day game");
    
    this.secondInningScore = score;
  }
}
