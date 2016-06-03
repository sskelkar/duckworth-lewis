package com.espncricinfo.cricket.domain;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;



@RunWith(JUnit4.class)
public class ScoreTest {

  @Test
  public void initializeScoreForStartOfMatch() {
    Over o = Over.valueOf("0.0");
    new Score(0, 0, o);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void scoreWithInvalidRuns() {
    new Score(-1, 0, Over.valueOf("0.0"));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void scoreWithInvalidWickets() {
    new Score(10, 11, Over.valueOf("0.0"));
  }
  
  @Test
  public void verifyToString() {
    Score s1 = new Score(100, 3, Over.valueOf("15.0"));
    
    assertTrue("100/3 15.0 overs".equals(s1.toString()));
    
    Score s2 = new Score(100, 3, Over.valueOf("20"));
    
    assertTrue("100/3 20.0 overs".equals(s2.toString()));
  }
}
