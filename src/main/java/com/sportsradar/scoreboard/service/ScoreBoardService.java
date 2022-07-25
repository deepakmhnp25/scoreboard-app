package com.sportsradar.scoreboard.service;

import com.sportsradar.scoreboard.model.Game;

import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

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
        Map<String, Integer> homeTeamMap = new HashMap<>();
        homeTeamMap.put(homeTeam, 0);
        startingGame.setHomeTeam(homeTeamMap);
        Map<String, Integer> awayTeamMap = new HashMap<>();
        awayTeamMap.put(awayTeam, 0);
        startingGame.setAwayTeam(awayTeamMap);
        startingGame.setTotalScore(0);
        return startingGame;
    }

    /**
     * Updates Score of an ongoing match
     * @param scanner
     * @param printStream
     * @param optionalGame
     */
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
            try {
                int homeScore = Integer.parseInt(homeTeamScore);
                int awayScore = Integer.parseInt(awayTeamScore);
                game.getHomeTeam().entrySet().stream().findFirst().ifPresentOrElse(homeTeam -> homeTeam.setValue(homeScore), () ->
                        printStream.println("Unable to update the score"));
                game.getAwayTeam().entrySet().stream().findFirst().ifPresentOrElse(awayTeam -> awayTeam.setValue(awayScore), () ->
                        printStream.println("Unable to update the score"));
                game.setTotalScore(homeScore+awayScore);
                printStream.println("Score updated successfully!");
            } catch (NumberFormatException exception){
                printStream.println("Enter a valid score! Please try again");
            } catch (Exception exception){
                printStream.println("Could not update the score. Please try again!");
            }
        }
    }

    /**
     * Get sorted score summary
     * @param printStream
     * @param gameSummary
     */
    public void getScoreSummary(PrintStream printStream, List<Game> gameSummary) {
        printStream.println("LIVE SCORE (Summary)\n==============");
        List<Game> orderedList = gameSummary.stream().sorted(
                (o1, o2) -> {
                    if (o1.getTotalScore() == o2.getTotalScore()) {
                        return gameSummary.indexOf(o1) > gameSummary.indexOf(o2) ? -1 : 1;
                    } else if (o1.getTotalScore() < o2.getTotalScore()) {
                        return 1;
                    } else return -1;
                }
        ).toList();
        orderedList.stream().forEach(game -> printStream.println(game.toString()));
    }
}
