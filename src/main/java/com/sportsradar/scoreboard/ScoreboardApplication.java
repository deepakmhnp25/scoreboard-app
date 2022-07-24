package com.sportsradar.scoreboard;

import com.sportsradar.scoreboard.model.Game;
import com.sportsradar.scoreboard.service.ScoreBoardService;
import org.apache.commons.lang3.StringUtils;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Predicate;

/**
 * Main class for Scoreboard Application
 *
 * @Author Deepak Mohan
 * @Version 1.0
 */
public class ScoreboardApplication {
    public static final String EXITING_TO_MAIN_MENU = "Exiting to Main Menu ...";

    public static void main(String args[]) {

        ScoreBoardService scoreBoardService = new ScoreBoardService();

        PrintStream printStream = new PrintStream(System.out);
        printStream.println("CHOOSE AN OPTION BELOW\n=====================");
        Scanner scanner = new Scanner(System.in);
        List<Game> gameSummary = new ArrayList<>();
        showMainMenu(scoreBoardService, scanner, printStream, gameSummary);
    }

    /**
     * This method is to show the Main Menu
     *
     * @param scoreBoardService
     * @param gameSummary
     */
    private static void showMainMenu(ScoreBoardService scoreBoardService, Scanner scanner, PrintStream printStream, List<Game> gameSummary) {
        printStream.println(" Main Menu");
        printStream.println(" 1. Start a new game \n 2. Finish a game \n 3. Update score \n 4. Live Score (Summary) \n 5. Exit Score Board App");
        printStream.println("Enter your option ");
        if (scanner.hasNext()) {
            StringBuilder menuOptionSb = new StringBuilder(scanner.next());
            String menuOptionSelected = scanner.hasNextLine() ? menuOptionSb.append(scanner.nextLine()).toString() : menuOptionSb.toString();
            // validate menu input and perform selected operation
            validateAndPerformScoreboardOperation(scoreBoardService, scanner, printStream, gameSummary, menuOptionSelected);
        }
    }


    /**
     * Perform scoreboard operations based on user input
     *
     * @param scoreBoardService
     * @param scanner
     * @param printStream
     * @param gameSummary
     * @param option
     */
    private static void doScoreBoardOperation(ScoreBoardService scoreBoardService, Scanner scanner, PrintStream printStream, List<Game> gameSummary, int option) {

        switch (option) {
            // Option to start the game
            case 1:
                startNewGame(scoreBoardService, scanner, printStream, gameSummary);
                break;
            // Finish an ongoing match
            case 2:
                finishGame(scoreBoardService, scanner, printStream, gameSummary);
                break;
            // Update game score
            case 3:
                updateGame(scoreBoardService, scanner, printStream, gameSummary);
                break;
            default: showMainMenu(scoreBoardService, scanner, printStream, gameSummary);

        }
    }

    /**
     * Update score of an ongoing match
     *
     * @param scoreBoardService
     * @param scanner
     * @param printStream
     * @param gameSummary
     */
    private static void updateGame(ScoreBoardService scoreBoardService, Scanner scanner, PrintStream printStream, List<Game> gameSummary) {
        if (gameSummary.isEmpty()) {
            printStream.println("There are no ongoing live matches currently !");
        } else {
            printStream.println("Enter Home / Away team name to Update the score. (Enter 5 to exit to Main Menu)");
            StringBuilder teamNamesb = new StringBuilder(scanner.next());
            String teamName = scanner.hasNextLine() ? teamNamesb.append(scanner.nextLine()).toString() : teamNamesb.toString();
            if (teamName.equalsIgnoreCase("5")) {
                printStream.println(EXITING_TO_MAIN_MENU);
                showMainMenu(scoreBoardService, scanner, printStream, gameSummary);
            } else {
                if (gameAlreadyExistForTheTeam(gameSummary, teamName)) {
                    updateScore(scoreBoardService, scanner, printStream, gameSummary, teamName);
                } else {
                    printStream.println("There is no ongoing matches from the input team name");
                }
            }
        }
    }

    /**
     * Update the game score
     *
     * @param scoreBoardService
     * @param scanner
     * @param printStream
     * @param gameSummary
     * @param teamName
     */
    private static void updateScore(ScoreBoardService scoreBoardService, Scanner scanner, PrintStream printStream,
                                    List<Game> gameSummary, String teamName) {
        Predicate<Game> findGame = game -> null != game.getHomeTeam().get(teamName) || null != game.getAwayTeam().get(teamName);
        Optional<Game> optionalGame = gameSummary.stream().filter(findGame).findFirst();
        if (optionalGame.isPresent()) {
            // Ongoing game exist
            scoreBoardService.updateScore(scanner, printStream, optionalGame);
            getScoreSummary(printStream, gameSummary);
            showMainMenu(scoreBoardService, scanner, printStream, gameSummary);
        } else {
            printStream.println("Game Not Found!!!");
        }

        showMainMenu(scoreBoardService, scanner, printStream, gameSummary);
    }

    /**
     * Finish an on going match
     *
     * @param scoreBoardService
     * @param scanner
     * @param printStream
     * @param gameSummary
     */
    private static void finishGame(ScoreBoardService scoreBoardService, Scanner scanner, PrintStream printStream, List<Game> gameSummary) {
        if (gameSummary.isEmpty()) {
            printStream.println("There are no ongoing live matches currently !");
        } else {
            getScoreSummary(printStream, gameSummary);
            printStream.println("Enter Home / Away team name to Finish the game. (Enter 5 to exit to Main Menu)");
            StringBuilder teamNamesb = new StringBuilder(scanner.next());
            String teamName = scanner.hasNextLine() ? teamNamesb.append(scanner.nextLine()).toString() : teamNamesb.toString();
            if (teamName.equalsIgnoreCase("5")) {
                printStream.println(EXITING_TO_MAIN_MENU);
                showMainMenu(scoreBoardService, scanner, printStream, gameSummary);
            } else {
                if (gameAlreadyExistForTheTeam(gameSummary, teamName)) {
                    Predicate<Game> isFinished = game -> null != game.getHomeTeam().get(teamName) || null != game.getAwayTeam().get(teamName);
                    List<Game> collect = gameSummary.stream().filter(isFinished).toList();
                    gameSummary.removeAll(collect);
                    printStream.println("Game Finished !");
                    getScoreSummary(printStream, gameSummary);
                    showMainMenu(scoreBoardService, scanner, printStream, gameSummary);
                } else {
                    printStream.println("There is no ongoing matches from the input team name");
                }
            }
        }
    }

    private static void getScoreSummary(PrintStream printStream, List<Game> gameSummary) {
        printStream.println("Live Scores (Summary)\n==============");
        gameSummary.stream().forEach(s -> printStream.println(s.toString()));
    }

    /**
     * Method start a new game, exit to main menu upon failing to enter home / away team
     *
     * @param scoreBoardService
     * @param scanner
     * @param printStream
     * @param gameSummary
     */
    private static void startNewGame(ScoreBoardService scoreBoardService, Scanner scanner, PrintStream printStream, List<Game> gameSummary) {
        printStream.println("Enter home team name to proceed. (Enter 5 to exit to Main Menu)");
        StringBuilder homeTeamNameSb = new StringBuilder(scanner.next());
        String homeTeam = scanner.hasNextLine() ? homeTeamNameSb.append(scanner.nextLine()).toString() : homeTeamNameSb.toString();
        if (homeTeam.equalsIgnoreCase("5")) {
            printStream.println(EXITING_TO_MAIN_MENU);
            showMainMenu(scoreBoardService, scanner, printStream, gameSummary);
        } else {
            printStream.println("Enter away team name (Enter 5 to exit to Main Menu)");
            StringBuilder awayTeamNameSb = new StringBuilder(scanner.next());
            String awayTeam = scanner.hasNextLine() ? awayTeamNameSb.append(scanner.nextLine()).toString() : awayTeamNameSb.toString();
            if (awayTeam.equalsIgnoreCase("5")) {
                printStream.println(EXITING_TO_MAIN_MENU);
                showMainMenu(scoreBoardService, scanner, printStream, gameSummary);
            } else {
                if (!gameAlreadyExistForTheTeam(gameSummary, homeTeam, awayTeam)) {
                    Game gameStarted = scoreBoardService.startGame(homeTeam, awayTeam);
                    // add match to live score summary
                    gameSummary.add(gameStarted);
                    printStream.println("Game Started !");
                    // Print current score summary of live games
                    printStream.println("Score Summary (Live Score)\n================");
                    getScoreSummary(printStream, gameSummary);
                    // show main menu after successfully starting the game
                    showMainMenu(scoreBoardService, scanner, printStream, gameSummary);
                } else {
                    printStream.println("Team is already playing, You cant start another game with the same team !");
                }
            }
        }
    }

    /**
     * Check if the game already exists
     *
     * @param gameSummary
     * @param homeTeam
     * @param awayTeam
     * @return
     */
    private static boolean gameAlreadyExistForTheTeam(List<Game> gameSummary, String homeTeam, String... awayTeam) {
        Predicate<Game> teamAlreadyPlaying = game -> null != game.getHomeTeam().get(homeTeam) || null != game.getAwayTeam().get(homeTeam) ||
                null != game.getHomeTeam().get(awayTeam) || null != game.getAwayTeam().get(awayTeam);
        return gameSummary.stream().anyMatch(teamAlreadyPlaying);
    }

    /**
     * validate menu input and perform selected operation
     *
     * @param scoreBoardService
     * @param scanner
     * @param printStream
     * @param gameSummary
     * @param menuOptionSelected
     */
    private static void validateAndPerformScoreboardOperation(ScoreBoardService scoreBoardService, Scanner scanner, PrintStream printStream, List<Game> gameSummary, String menuOptionSelected) {
        if (StringUtils.equalsAny(menuOptionSelected, "1", "2", "3", "4", "5")) {
            // Valid option selected
            int option = Integer.parseInt(menuOptionSelected);
            if (option != 5) {
                doScoreBoardOperation(scoreBoardService, scanner, printStream, gameSummary, option);
            }
        } else {
            // Invalid option selected
            printStream.println("============ PLEASE ENTER A VALID MENU OPTION ! ============");
            showMainMenu(scoreBoardService, scanner, printStream, gameSummary);
        }
    }
}