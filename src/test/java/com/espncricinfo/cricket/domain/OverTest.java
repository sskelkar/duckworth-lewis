package com.espncricinfo.cricket.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.espncricinfo.cricket.domain.Over;

@RunWith(JUnit4.class)
public class OverTest {

  @Test(expected=IllegalArgumentException.class)
  public void parseIncompleteOverString() {
    Over.valueOf("10.");     
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void parseOverWithInvalidNoOfBalls() {
    Over.valueOf("0.7");     
  }
 
  @Test(expected=IllegalArgumentException.class)
  public void parseOverWithInvalidNoOfBallsDigits() {
    Over.valueOf("0.61");     
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void parseOverWithAlphabets() {
    Over.valueOf("010a.6");     
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void parseNegativeOver() {
    Over.valueOf("-10.6");     
  }
  
  @Test
  public void parseValidOvers() {
    Over.valueOf("100.1");
    Over.valueOf("0.4");
    Over.valueOf("5.6");
    Over.valueOf("20.0");
    Over.valueOf("20");
  }
  
  @Test
  public void initializeOver() {
    Over o = Over.valueOf("10.4");
    
    assertTrue("no of balls in given over", o.getBalls() == 64);
  }
  
  @Test
  public void printOver() {
    Over o = Over.valueOf("48.3");
    assertTrue("over is printed in correct format", o.toString().equals("48.3"));
  }
  
  @Test
  public void getBalls() {
    assertTrue(Over.valueOf("50.0").getBalls() == 300);
    assertTrue(Over.valueOf("21.1").getBalls() == 127);
    assertTrue(Over.valueOf("10").getBalls() == 60);
  }
  
  @Test
  public void testEquality() {
    assertTrue(Over.valueOf("50.0").equals(Over.valueOf("50")));
    assertTrue(Over.valueOf("46.6").equals(Over.valueOf("47")));
    
    assertFalse(Over.valueOf("11.0").equals(Over.valueOf("10.1")));
    assertFalse(Over.valueOf("11.0").equals(null));
  }
}
