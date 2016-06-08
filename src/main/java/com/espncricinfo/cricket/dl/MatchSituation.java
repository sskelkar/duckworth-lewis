package com.espncricinfo.cricket.dl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import com.espncricinfo.cricket.domain.MatchType;
import com.espncricinfo.cricket.domain.Over;
import com.espncricinfo.cricket.domain.Score;

@Service
public class MatchSituation {

  private MatchType matchType;
  private Score firstInningScore = Score.INITIAL_SCORE;
  private Over firstInningOversLost;
  private Score secondInningScore = Score.INITIAL_SCORE;
  private Over secondInningOversLost;
  private Over adjustedFirstInningSize;
  private Over adjustedSecondInningSize;
  
  private ResourcePercentage resourcePercentage;
  private static final BigDecimal BIGDECIMAL_100 = new BigDecimal(100);
  
  public MatchSituation() {
    // default constructor
  }
  
  public MatchSituation(ResourcePercentage resourcePercentage) {
    this.resourcePercentage = resourcePercentage;
  }
  
  BigDecimal calculateActualInningResources(Over adjustedInningSize, Score inningScore, Over oversLost) {
    Over availableOversAtStartOfInning = adjustedInningSize == null ? matchType.maxOvers() : adjustedInningSize;
    BigDecimal startingResources = resourcePercentage.forOverRemaingAndWicketsLost(availableOversAtStartOfInning, 0);
    
    BigDecimal resourcesLost = BigDecimal.ZERO;
    if(oversLost != null) {
      // when no further play happened
      if(oversLost.add(inningScore.getOvers()).equals(availableOversAtStartOfInning)) {
        resourcesLost = resourcePercentage.forOverRemaingAndWicketsLost(oversLost, inningScore.getWickets());
      }
      // when play resumed after some overs lost
      else {
        Over oversLeft = availableOversAtStartOfInning.subtract(inningScore.getOvers());
        BigDecimal resourceLeftAtSuspension = resourcePercentage.forOverRemaingAndWicketsLost(oversLeft, inningScore.getWickets());
        
        BigDecimal resourceLeftAtResumption = resourcePercentage.forOverRemaingAndWicketsLost(oversLeft.subtract(oversLost), inningScore.getWickets());
        resourcesLost = resourceLeftAtSuspension.subtract(resourceLeftAtResumption);
      }
    }
    
    return startingResources.subtract(resourcesLost);
  }
  
  private Integer getRevisedTarget() {
    Integer parScore = firstInningScore.getRuns();
    BigDecimal firstInningResources = calculateActualInningResources(adjustedFirstInningSize, firstInningScore, firstInningOversLost);
    BigDecimal secondInningResources = calculateActualInningResources(adjustedSecondInningSize, secondInningScore, secondInningOversLost);

    if(secondInningScore.getOvers().equals(Over.valueOf("0"))) {  //2nd inning not started yet
      BigDecimal deficit = firstInningResources.subtract(secondInningResources);     
      Integer deficitRuns = deficit.multiply(new BigDecimal(matchType.averageFirstInningScore())).divide(BIGDECIMAL_100).intValue();
      
      parScore = parScore - deficitRuns + 1;
    }
    else {
      parScore = secondInningResources.divide(firstInningResources, 2, RoundingMode.CEILING).multiply(new BigDecimal(parScore)).intValue();
    }

    return parScore;
  }

  public Integer getTarget() {
    if(firstInningScore == null)
      throw new IllegalStateException("First inning score should be specified");
    
    int firstInningTotalBalls = firstInningScore.getOvers().getBalls() + (firstInningOversLost == null ? 0 : firstInningOversLost.getBalls()); 
    
    if((adjustedFirstInningSize != null && adjustedFirstInningSize.getBalls() == firstInningTotalBalls) || (matchType.maxOvers().getBalls() == firstInningTotalBalls)) {
      
      if(firstInningOversLost != null || secondInningOversLost != null)
        return getRevisedTarget();
      else
        return firstInningScore.getRuns() + 1;  
    }
    else
      throw new IllegalStateException("Target can be set after first inning is completed");
    
  }
  
  public Over getAdjustedFirstInningSize() {
    return adjustedFirstInningSize;
  }

  public void setAdjustedFirstInningSize(Over adjustedInningSize) {
    this.adjustedFirstInningSize = adjustedInningSize;
  }

  public Over getAdjustedSecondInningSize() {
    return adjustedSecondInningSize;
  }

  public void setAdjustedSecondInningSize(Over adjustedInningSize) {
    this.adjustedSecondInningSize = adjustedInningSize;
  }
  
  public void setMatchType(MatchType matchType) {
    this.matchType = matchType;
  }

  public Score getFirstInningScore() {
    return firstInningScore;
  }

  public void setFirstInningScore(Score firstInningScore) {
    this.firstInningScore = firstInningScore;
  }

  public MatchType getMatchType() {
    return matchType;
  }

  public Over getFirstInningOversLost() {
    return firstInningOversLost;
  }

  public void setFirstInningOversLost(Over firstInningOversLost) {
    this.firstInningOversLost = firstInningOversLost;
  }

  public Score getSecondInningScore() {
    return secondInningScore;
  }

  public void setSecondInningScore(Score secondInningScore) {
    this.secondInningScore = secondInningScore;
  }

  public Over getSecondInningOversLost() {
    return secondInningOversLost;
  }

  public void setSecondInningOversLost(Over secondInningOversLost) {
    this.secondInningOversLost = secondInningOversLost;
  }
  
  
}
