package com.espncricinfo.cricket.dl;

import com.espncricinfo.cricket.domain.MatchType;
import com.espncricinfo.cricket.domain.Over;
import com.espncricinfo.cricket.domain.Score;

public class MatchSituationBuilder {
  private MatchSituation match;
  
  private MatchSituationBuilder(ResourcePercentage rp) {
    match = new MatchSituation(rp);
  }
  
  public static MatchSituationBuilder create(ResourcePercentage rp) {
    return new MatchSituationBuilder(rp);
  }
  
  public MatchSituationBuilder matchType(MatchType type) {
    this.match.setMatchType(type);
    return this;
  }
  
  public MatchSituationBuilder firstInningScore(Score score) {
    this.match.setFirstInningScore(score);
    return this;
  }
  
  public MatchSituationBuilder firstInningOversLost(Over overs) {
    this.match.setFirstInningOversLost(overs);
    return this;
  }
  
  public MatchSituationBuilder secondInningScore(Score score) {
    this.match.setSecondInningScore(score);
    return this;
  }
  
  public MatchSituationBuilder shortenFirstInning(Over overs) {
    this.match.setAdjustedFirstInningSize(overs);
    return this;
  }
 
  public MatchSituationBuilder shortenSecondInning(Over overs) {
    this.match.setAdjustedSecondInningSize(overs);
    return this;
  }
  
  public MatchSituationBuilder secondInningOversLost(Over overs) {
    this.match.setSecondInningOversLost(overs);
    return this;
  }
  
  public MatchSituationBuilder shortenMatchSize(Over overs) {
    this.match.setAdjustedFirstInningSize(overs);
    this.match.setAdjustedSecondInningSize(overs);
    return this;
  }
  
  public MatchSituation getMatchSituation() {
    if(this.match.getMatchType() == null)
      throw new IllegalStateException("Match type must be specified");
    return this.match;
  }
}
