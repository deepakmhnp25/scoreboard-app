package com.sportsradar.scoreboard.service;

import com.sportsradar.scoreboard.model.Game;

import java.util.HashMap;
import java.util.Map;

public class ScoreBoardService {

    /**
     * This method starts the game with scores 0-0
     * @param homeTeam
     * @param awayTeam
     */
    public Game startGame(String homeTeam, String awayTeam) {
        Game startingGame = new Game();
        Map<String, String> homeTeamMap = new HashMap<>();
        homeTeamMap.put(homeTeam, "0");
        startingGame.setHomeTeam(homeTeamMap);
        Map<String, String> awayTeamMap = new HashMap<>();
        awayTeamMap.put(awayTeam, "0");
        startingGame.setAwayTeam(awayTeamMap);
        return startingGame;
    }
}
