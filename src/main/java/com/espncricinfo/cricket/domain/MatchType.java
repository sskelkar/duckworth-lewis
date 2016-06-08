package com.espncricinfo.cricket.domain;

public enum MatchType {

  TEST {
    @Override
    public Over maxOvers() {
      throw new UnsupportedOperationException();
    }

    @Override
    public int averageFirstInningScore() {
      throw new UnsupportedOperationException();
    }
  },
  ODI {
    @Override
    public Over maxOvers() {
      return Over.valueOf("50");
    }

    @Override
    public int averageFirstInningScore() {
      return 225;
    }
  },
  T20 {
    @Override
    public Over maxOvers() {
      return Over.valueOf("20");
    }

    @Override
    public int averageFirstInningScore() {
      throw new UnsupportedOperationException();
    }
  };
  
  public abstract Over maxOvers();
  
  public abstract int averageFirstInningScore();
}
