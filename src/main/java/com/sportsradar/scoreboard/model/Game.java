package com.sportsradar.scoreboard.model;

import java.util.Map;

/**
 * Pojo Class represent each game
 *
 * @Author Deepak Mohan
 * @Version 1.0
 */
public class Game {

    private Map<String, String> homeTeam;

    private Map<String, String> awayTeam;

    public Map<String, String> getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Map<String, String> homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Map<String, String> getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Map<String, String> awayTeam) {
        this.awayTeam = awayTeam;
    }

    @Override
    public String toString() {
        String homeTeamName = null;
        String homeTeamScore = null;
        String awayTeamName = null;
        String awayTeamScore = null;

        for(Map.Entry<String, String> entry: this.homeTeam.entrySet()){
            homeTeamName = entry.getKey();
            homeTeamScore = entry.getValue();
        }
        for(Map.Entry<String, String> entry: this.awayTeam.entrySet()){
            awayTeamName = entry.getKey();
            awayTeamScore = entry.getValue();
        }
        StringBuilder scoreSb = new StringBuilder(homeTeamName).append(" - ").append(awayTeamName).append(": ").append(homeTeamScore).append(" - ").append(awayTeamScore);
        return scoreSb.toString();
    }
}
