package com.espncricinfo.cricket.dl;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.*;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import com.espncricinfo.cricket.domain.MatchType;
import com.espncricinfo.cricket.domain.Over;
import com.espncricinfo.cricket.domain.Score;

@RunWith(MockitoJUnitRunner.class)
public class MatchSituationTest {
  @Mock
  private ResourcePercentage mockedRP;
  
  @Before
  public void init() {
    when(mockedRP.forOverRemaingAndWicketsLost(eq(Over.valueOf("50")), eq(0))).thenReturn(new BigDecimal("100"));
    when(mockedRP.forOverRemaingAndWicketsLost(eq(Over.valueOf("10")), eq(7))).thenReturn(new BigDecimal("20.6"));
    when(mockedRP.forOverRemaingAndWicketsLost(eq(Over.valueOf("10")), eq(5))).thenReturn(new BigDecimal("27.5"));
    when(mockedRP.forOverRemaingAndWicketsLost(eq(Over.valueOf("40")), eq(0))).thenReturn(new BigDecimal("90.3"));
    when(mockedRP.forOverRemaingAndWicketsLost(eq(Over.valueOf("45")), eq(0))).thenReturn(new BigDecimal("95.6"));
    when(mockedRP.forOverRemaingAndWicketsLost(eq(Over.valueOf("5")), eq(5))).thenReturn(new BigDecimal("16.4"));
    when(mockedRP.forOverRemaingAndWicketsLost(eq(Over.valueOf("20")), eq(5))).thenReturn(new BigDecimal("40.0"));
  }
  
  /**
   * Eample 1: Premature curtailment of Team 2's innings
   * Team 1 scored 250 runs in its 50 overs. Team 2 score 199/5 in 40 overs. Match is abandoned there after. Who won the match?
   */
  @Test
  public void secondInningCurtailed() {
    MatchSituation match = MatchSituationBuilder.create(mockedRP)
        .matchType(MatchType.ODI)
        .firstInningScore(new Score(250, Over.valueOf("50")))
        .secondInningScore(new Score(199, 5, Over.valueOf("40")))
        .secondInningOversLost(Over.valueOf("10"))
        .getMatchSituation();
    
    Integer revisedTarget = 182;
    
    Assert.assertTrue("revised target when 2nd inning interrupted", revisedTarget.equals(match.getTarget()));
  }
  /**
   * Example 2: Interruption to Team 2's innings
   * ODI shortened to 40 overs per side due to rain. Team 1 scored 200 from their 40. Team 2 scored 140/5 in 30 overs. After that 5 overs are lost. What is their revised target?
   */
  @Test
  public void interruptionInSecondInning() {
    MatchSituation match = MatchSituationBuilder.create(mockedRP)
        .matchType(MatchType.ODI)
        .shortenMatchSize(Over.valueOf("40"))
        .firstInningScore(new Score(200, Over.valueOf("40")))
        .secondInningScore(new Score(140, 5, Over.valueOf("30")))
        .secondInningOversLost(Over.valueOf("5"))
        .getMatchSituation();
    
    Integer expectedAdjustedTarget = 176;
    
    Assert.assertTrue("revised target when 2nd inning interrupted", expectedAdjustedTarget.equals(match.getTarget()));
  }
  
  /**
   * Example 3: Interruption to Team 1's innings. 
   * Team 1 scored 190/7 in 40 overs, when rain curtailed the remaining of their inning. Team 2's inning is also restricted to 40 overs. What's the revised target for them?
   */
  @Test
  public void interruptionInFirstInning() {    
    
    MatchSituation match = MatchSituationBuilder.create(mockedRP)
        .matchType(MatchType.ODI)
        .firstInningScore(new Score(190, 7, Over.valueOf("40")))
        .firstInningOversLost(Over.valueOf("10"))
        .shortenSecondInning(Over.valueOf("40"))
        .getMatchSituation();
    
    Integer expectedAdjustedTarget = 215;
    
    Assert.assertTrue("adjusted target", expectedAdjustedTarget.equals(match.getTarget()));
  }
  
  @Test(expected = IllegalStateException.class)
  public void getAdjustedTargetWithoutFirstInningScore() {
    MatchSituation match = MatchSituationBuilder.create(mockedRP)
        .matchType(MatchType.ODI)
        .getMatchSituation();
    
    match.getTarget();
  }
  
  @Test
  public void ifNoOversLostThenAdjustedTargetIsSameAsScore() {
    MatchSituation match = MatchSituationBuilder.create(mockedRP)
        .matchType(MatchType.ODI)
        .firstInningScore(new Score(190, 7, Over.valueOf("50")))
        .getMatchSituation();
    
    Assert.assertTrue(191 == match.getTarget());
  }
  
  @Test
  public void testGetTarget() {
    MatchSituation match = MatchSituationBuilder.create(mockedRP)
        .matchType(MatchType.ODI)
        .shortenMatchSize(Over.valueOf("40"))
        .firstInningScore(new Score(190, 7, Over.valueOf("40")))
        .getMatchSituation();
    
    Assert.assertTrue(191 == match.getTarget());
  }

  @Test(expected = Exception.class)
  public void targetIsSetOnlyAfterFirstInningFullCompletion() {
    MatchSituation match = MatchSituationBuilder.create(mockedRP)
        .matchType(MatchType.ODI)
        .firstInningScore(new Score(190, 7, Over.valueOf("40")))
        .getMatchSituation();
    
    match.getTarget();
  }
  
  @Test(expected = Exception.class)
  public void targetIsSetOnlyAfterFirstInningAdjustedCompletion() {
    MatchSituation match = MatchSituationBuilder.create(mockedRP)
        .matchType(MatchType.ODI)
        .shortenFirstInning(Over.valueOf("45"))
        .firstInningScore(new Score(190, 7, Over.valueOf("40")))
        .getMatchSituation();
    
    match.getTarget();
  }
  
  /**
   * Match shortened to 45 overs per side. Team 1 makes 190/7 in 40 overs. Remaining overs are lost. What's the target for team 2?
   */
  @Test
  public void getRevisedTargetIfOversAreLostInFirstInning() {
    MatchSituation match = MatchSituationBuilder.create(mockedRP)
        .matchType(MatchType.ODI)
        .shortenFirstInning(Over.valueOf("45"))
        .firstInningScore(new Score(190, 5, Over.valueOf("40")))
        .firstInningOversLost(Over.valueOf("5"))
        .getMatchSituation();
    
    Assert.assertTrue(match.getTarget() != 191);
    Assert.assertTrue(match.getTarget() == 237);
  }
  
  @Test
  public void getRevisedTargetIfOversAreLostInSecondInning() {    
    MatchSituation match = MatchSituationBuilder.create(mockedRP)
        .matchType(MatchType.ODI)
        .firstInningScore(new Score(250, 7, Over.valueOf("50")))
        .secondInningScore(new Score(125, 5, Over.valueOf("30")))
        .secondInningOversLost(Over.valueOf("10"))
        .getMatchSituation();

    Assert.assertTrue(match.getTarget() != 251);
  }
  
  /**
   * When there were no interruptions, targer is simply first inning score + 1
   */
  @Test
  public void targetIsFirstInningScorePlus1WhenNoInterruptions() {
    MatchSituation match = MatchSituationBuilder.create(mockedRP)
        .matchType(MatchType.ODI)
        .firstInningScore(new Score(250, 7, Over.valueOf("50")))
        .secondInningScore(new Score(125, 7, Over.valueOf("30")))
        .getMatchSituation();
    
    Assert.assertTrue(match.getTarget() == 251);
  }
  
  @Test
  public void testCalculateInningResourcesConsumed() {
    MatchSituation match = MatchSituationBuilder.create(mockedRP)
        .matchType(MatchType.ODI)
        .getMatchSituation();
    
    BigDecimal resources = match.calculateActualInningResources(null, new Score(220, 8, Over.valueOf("50")),null);
    
    Assert.assertTrue("100% resources consumed", resources.equals(new BigDecimal(100)));
  }
  
  @Test
  public void testCalculateInningResourcesConsumedWhenInningShortened() {
    MatchSituation match = MatchSituationBuilder.create(mockedRP)
        .matchType(MatchType.ODI)
        .getMatchSituation();
    
    BigDecimal resources = match.calculateActualInningResources(Over.valueOf("40"), new Score(220, 8, Over.valueOf("40")),null);
    
    Assert.assertTrue("90.3% resources consumed", resources.equals(new BigDecimal("90.3")));
  }
  
  @Test
  public void testCalculateInningResourcesConsumedWhenOversLost() {
    MatchSituation match = MatchSituationBuilder.create(mockedRP)
        .matchType(MatchType.ODI)
        .getMatchSituation();
    
    BigDecimal resources = match.calculateActualInningResources(null, new Score(210, 7, Over.valueOf("40")), Over.valueOf("10"));
    
    Assert.assertTrue("79.4% resources consumed", resources.equals(new BigDecimal("79.4"))); //100 - 20.6 
  }
  
  @Test
  public void testCalculateInningResourcesConsumedWhenOversLostAndInningShortened() {
    MatchSituation match = MatchSituationBuilder.create(mockedRP)
        .matchType(MatchType.ODI)
        .getMatchSituation();
    
    BigDecimal resources = match.calculateActualInningResources(Over.valueOf("40"), new Score(210, 7, Over.valueOf("30")), Over.valueOf("10"));
    
    Assert.assertTrue("69.7% resources consumed", resources.equals(new BigDecimal("69.7"))); //90.3 - 20.6 
  }
}
