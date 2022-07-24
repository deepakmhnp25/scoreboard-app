package com.sportsradar.scoreboard.model;

import java.util.Map;

public class Score {

    private Map<String, String> homeTeamScore;

    private Map<String, String> awayTeamScore;

    public Map<String, String> getHomeTeamScore() {
        return homeTeamScore;
    }

    public void setHomeTeamScore(Map<String, String> homeTeamScore) {
        this.homeTeamScore = homeTeamScore;
    }

    public Map<String, String> getAwayTeamScore() {
        return awayTeamScore;
    }

    public void setAwayTeamScore(Map<String, String> awayTeamScore) {
        this.awayTeamScore = awayTeamScore;
    }

    @Override
    public String toString() {
        String homeTeamName = null;
        String homeTeamScore = null;
        String awayTeamName = null;
        String awayTeamScore = null;

        for(Map.Entry<String, String> entry: this.homeTeamScore.entrySet()){
            homeTeamName = entry.getKey();
            homeTeamScore = entry.getValue();
        }
        for(Map.Entry<String, String> entry: this.awayTeamScore.entrySet()){
            awayTeamName = entry.getKey();
            awayTeamScore = entry.getValue();
        }
        StringBuilder scoreSb = new StringBuilder(homeTeamName).append(" - ").append(awayTeamName).append(": ").append(homeTeamScore).append(" - ").append(awayTeamScore);
        return scoreSb.toString();
    }
}
