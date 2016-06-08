package com.espncricinfo.cricket.dl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;

import com.espncricinfo.cricket.domain.Over;

public class ResourcePercentage {

  private Map<Integer, BigDecimal> resourcePerLostWicket;
  private Map<Over, Map<Integer, BigDecimal>> resourcePerRemainingOver;
  
  /**
   * Returns resource percentage equivalent to given available overs left and wickets already lost. 
   * @param remainingOvers
   * @param wicketsLost
   * @return
   */
  public BigDecimal forOverRemaingAndWicketsLost(Over remainingOvers, Integer wicketsLost) {
    if(resourcePerRemainingOver == null)
      initializeResourcePercentageTable();
    
    return resourcePerRemainingOver.get(remainingOvers).get(wicketsLost);
  }
  
  private void initializeResourcePercentageTable() {
    ClassPathResource classPathResource = new ClassPathResource("resource-percentage-table");
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(classPathResource.getFile()));
      String line;
      resourcePerRemainingOver = new HashMap<>();
      
      //first line contains wicket information contained in this sample
      line = br.readLine();
      String[] wickets = line.split(" ");
      
      while ((line = br.readLine()) != null) {
        String[] tokens = line.split(" ");
        Over remainingOvers = Over.valueOf(tokens[0]);
        resourcePerLostWicket = new HashMap<>();
        
        for(int i=0; i<wickets.length; i++) {
          resourcePerLostWicket.put(Integer.valueOf(wickets[i]), new BigDecimal(tokens[i+1]));
        }
        
        resourcePerRemainingOver.put(remainingOvers, resourcePerLostWicket);        
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      if(br != null) {
        try {
          br.close();
        }
        catch(Exception e){}
      }        
    }
  }
}
