package com.sportsradar.scoreboard.service;

import com.sportsradar.scoreboard.model.Score;

import java.util.HashMap;
import java.util.Map;

public class ScoreBoardService {

    /**
     * This method starts the game with scores 0-0
     * @param homeTeam
     * @param awayTeam
     */
    public Score startGame(String homeTeam, String awayTeam) {
        Score startingScore = new Score();
        Map<String, String> homeTeamMap = new HashMap<>();
        homeTeamMap.put(homeTeam, "0");
        startingScore.setHomeTeamScore(homeTeamMap);
        Map<String, String> awayTeamMap = new HashMap<>();
        awayTeamMap.put(awayTeam, "0");
        startingScore.setAwayTeamScore(awayTeamMap);
        return startingScore;
    }
}
