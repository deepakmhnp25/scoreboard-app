package com.org.scoreboard.model;

import java.util.Map;

/**
 * Pojo Class represent each game
 *
 * @Author Deepak Mohan
 * @Version 1.0
 */
public class Game {

    private Map<String, Integer> homeTeam;

    private Map<String, Integer> awayTeam;

    private Integer totalScore;

    public Map<String, Integer> getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Map<String, Integer> homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Map<String, Integer> getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Map<String, Integer> awayTeam) {
        this.awayTeam = awayTeam;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    @Override
    public String toString() {
        String homeTeamName = null;
        Integer homeTeamScore = null;
        String awayTeamName = null;
        Integer awayTeamScore = null;

        for(Map.Entry<String, Integer> entry: this.homeTeam.entrySet()){
            homeTeamName = entry.getKey();
            homeTeamScore = entry.getValue();
        }
        for(Map.Entry<String, Integer> entry: this.awayTeam.entrySet()){
            awayTeamName = entry.getKey();
            awayTeamScore = entry.getValue();
        }
        StringBuilder scoreSb = new StringBuilder(homeTeamName).append(" - ").append(awayTeamName).append(": ").append(homeTeamScore).append(" - ").append(awayTeamScore);
        return scoreSb.toString();
    }
}
