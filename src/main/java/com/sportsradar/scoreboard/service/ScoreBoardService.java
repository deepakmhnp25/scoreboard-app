package com.sportsradar.scoreboard.service;

import com.sportsradar.scoreboard.model.Game;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

/**
 * Service Class catering Functionalities of Scoreboard Application
 *
 * @Author Deepak Mohan
 * @Version 1.0
 */
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

    public void updateScore(Scanner scanner, PrintStream printStream, Optional<Game> optionalGame) {
        Game game;
        if (optionalGame.isPresent()) {
            game = optionalGame.get();
            printStream.println("Enter home team score");
            StringBuilder homeTeamScoreSb = new StringBuilder(scanner.next());
            String homeTeamScore = scanner.hasNextLine() ? homeTeamScoreSb.append(scanner.nextLine()).toString() : homeTeamScoreSb.toString();
            printStream.println("Enter away team score");
            StringBuilder awayTeamScoreSb = new StringBuilder(scanner.next());
            String awayTeamScore = scanner.hasNextLine() ? awayTeamScoreSb.append(scanner.nextLine()).toString() : awayTeamScoreSb.toString();
            game.getHomeTeam().entrySet().stream().findFirst().ifPresentOrElse(homeTeam -> homeTeam.setValue(homeTeamScore), () ->
                    printStream.println("Unable to update the score"));
            game.getAwayTeam().entrySet().stream().findFirst().ifPresentOrElse(awayTeam -> awayTeam.setValue(awayTeamScore), () ->
                    printStream.println("Unable to update the score"));
            printStream.println("Score updated successfully!");
        }
    }
}
