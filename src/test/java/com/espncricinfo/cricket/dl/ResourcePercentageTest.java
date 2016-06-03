package com.espncricinfo.cricket.dl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.espncricinfo.cricket.domain.Over;

@RunWith(JUnit4.class)
public class ResourcePercentageTest {
  
  private ResourcePercentage resourcePercentage;

  @Before
  public void init() {
    resourcePercentage = new ResourcePercentage(); // a real instance is being used instead of a mocked one to test file reading operation 
  }
  
  @Test
  public void fetchResourcePercentage() {
    Assert.assertTrue("100% resources at the start of match", resourcePercentage.forOverRemaingAndWicketsLost(Over.valueOf("50"), 0) == 100.0);
    
    Assert.assertTrue("7.5% resources after 9 wickets down and 10 overs remaining", resourcePercentage.forOverRemaingAndWicketsLost(Over.valueOf("10"), 9) == 7.5);
    
    Assert.assertTrue("16.4% resources after 5 wickets down and 5 overs remaining", resourcePercentage.forOverRemaingAndWicketsLost(Over.valueOf("5"), 5) == 16.4);
  }
}
