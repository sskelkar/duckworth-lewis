package com.espncricinfo.cricket.dl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

import com.espncricinfo.cricket.domain.MatchType;
import com.espncricinfo.cricket.domain.Over;
import com.espncricinfo.cricket.domain.Score;

@RunWith(JUnit4.class)
public class MatchSituationBuilderTest {
  @Mock
  ResourcePercentage mockedRP;
  
  @Test
  public void buildBasicMatchSituation() {
    Score first = new Score(100, 5, Over.valueOf("40"));
    Over firstInningOverLost = Over.valueOf("10");
    Score second = new Score(120, 5, Over.valueOf("50"));
    Over secondInningOverLost = Over.valueOf("0");
    
    MatchSituation match = MatchSituationBuilder.create(mockedRP)
        .matchType(MatchType.ODI)
        .firstInningScore(first)
        .firstInningOversLost(firstInningOverLost)
        .secondInningScore(second)
        .secondInningOversLost(secondInningOverLost)
        .getMatchSituation();
    
    Assert.assertTrue("match type", MatchType.ODI.equals(match.getMatchType()));
    Assert.assertTrue("first inning score", first.equals(match.getFirstInningScore()));
    Assert.assertTrue("first inning overs lost", Over.valueOf("10").equals(match.getFirstInningOversLost()));
    Assert.assertTrue("second inning score", new Score(120, 5, Over.valueOf("50")).equals(match.getSecondInningScore()));
    Assert.assertTrue("second inning overs lost", Over.valueOf("0").equals(match.getSecondInningOversLost()));
  }
  
  @Test
  public void shortenFirstInning() {
    MatchSituation match = MatchSituationBuilder.create(mockedRP).matchType(MatchType.ODI).shortenFirstInning(Over.valueOf("40")).getMatchSituation();
    
    Assert.assertTrue("adjusted first inning size", Over.valueOf("40").equals(match.getAdjustedFirstInningSize()));
  }
  
  @Test
  public void shortenSecondInning() {
    MatchSituation match = MatchSituationBuilder.create(mockedRP).matchType(MatchType.ODI).shortenSecondInning(Over.valueOf("42")).getMatchSituation();
    
    Assert.assertTrue("adjusted second inning size", Over.valueOf("42").equals(match.getAdjustedSecondInningSize()));
  }
  
  @Test
  public void shortenMatchSize() {
    MatchSituation match = MatchSituationBuilder.create(mockedRP).matchType(MatchType.ODI).shortenMatchSize(Over.valueOf("39")).getMatchSituation();
    
    Assert.assertTrue("adjusted first inning size", Over.valueOf("39").equals(match.getAdjustedFirstInningSize()));
    Assert.assertTrue("adjusted second inning size", Over.valueOf("39").equals(match.getAdjustedSecondInningSize()));
  }
  
  @Test(expected = IllegalStateException.class)
  public void initializeWithoutMatchType() {
    MatchSituationBuilder.create(mockedRP).getMatchSituation();
  }
}
