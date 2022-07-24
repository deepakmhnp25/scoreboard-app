import com.sportsradar.scoreboard.model.Game;
import com.sportsradar.scoreboard.service.ScoreBoardService;
import org.apache.commons.lang3.StringUtils;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Predicate;

public class ScoreboardApplication {

    public static final String EXITING_TO_MAIN_MENU = "Exiting to Main Menu ...";

    public static void main(String args[]) {

        ScoreBoardService scoreBoardService = new ScoreBoardService();

        System.out.println("CHOOSE AN OPTION BELOW\n=====================");
        Scanner scanner = new Scanner(System.in);
        PrintStream printStream = new PrintStream(System.out);
        List<Game> gameSummary = new ArrayList<>();
        showMainMenu(scoreBoardService, scanner, printStream, gameSummary);

    }

    /**
     * This method is to show the Main Menu
     *
     * @param scoreBoardService
     * @param gameSummary
     */
    private static void showMainMenu(ScoreBoardService scoreBoardService,Scanner scanner, PrintStream printStream, List<Game> gameSummary) {
        System.out.println(" Main Menu");
        System.out.println(" 1. Start a new game \n 2. Finish a game \n 3. Update score \n 4. Live Score (Summary) \n 5. Exit Score Board App");
        System.out.println("Enter your option ");
        if (scanner.hasNext()) {
            StringBuilder menuOptionSb = new StringBuilder(scanner.next());
            String menuOptionSelected = scanner.hasNextLine() ? menuOptionSb.append(scanner.nextLine()).toString() : menuOptionSb.toString();
            // validate menu input and perform selected operation
            validateAndPerformScoreboardOperation(scoreBoardService, scanner, printStream, gameSummary, menuOptionSelected);
        }
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
            System.out.println("============ PLEASE ENTER A VALID MENU OPTION ! ============");
            showMainMenu(scoreBoardService, scanner, printStream, gameSummary);
        }
    }

    private static void doScoreBoardOperation(ScoreBoardService scoreBoardService, Scanner scanner, PrintStream printStream, List<Game> gameSummary, int option) {

        switch (option) {
            // Option to start the game
            case 1:
                startNewGame(scoreBoardService, scanner, printStream, gameSummary);
                break;
            case 2:
                finishGame(scoreBoardService, scanner, printStream, gameSummary);
                break;
            case 3:
                updateScore(scoreBoardService, scanner, printStream, gameSummary);
                break;

        }
    }

    /**
     * Update score of an ongoing match
     * @param scoreBoardService
     * @param scanner
     * @param printStream
     * @param gameSummary
     */
    private static void updateScore(ScoreBoardService scoreBoardService, Scanner scanner, PrintStream printStream, List<Game> gameSummary) {
        if (gameSummary.isEmpty()) {
            System.out.println("There are no ongoing live matches currently !");
        } else {
            System.out.println("Enter Home / Away team name to Update the score. (Enter 5 to exit to Main Menu)");
            StringBuilder teamNamesb = new StringBuilder(scanner.next());
            String teamName = scanner.hasNextLine() ? teamNamesb.append(scanner.nextLine()).toString() : teamNamesb.toString();
            if (teamName.equalsIgnoreCase("5")) {
                System.out.println(EXITING_TO_MAIN_MENU);
                showMainMenu(scoreBoardService, scanner, printStream, gameSummary);
            } else {
                if(gameAlreadyExistForTheTeam(gameSummary, teamName)) {
                    Predicate<Game> findGame = game -> null != game.getHomeTeam().get(teamName) || null != game.getAwayTeam().get(teamName);
                    Optional<Game> optionalGame = gameSummary.stream().filter(findGame).findFirst();
                    Game game;
                    if(optionalGame.isPresent()){
                        // Ongoing game exist
                        game = optionalGame.get();
                        System.out.println("Enter home team score");
                        StringBuilder homeTeamScoreSb = new StringBuilder(scanner.next());
                        String homeTeamScore = scanner.hasNextLine() ? homeTeamScoreSb.append(scanner.nextLine()).toString() : homeTeamScoreSb.toString();
                        System.out.println("Enter home team score");
                        StringBuilder awayTeamScoreSb = new StringBuilder(scanner.next());
                        String awayTeamScore = scanner.hasNextLine() ? awayTeamScoreSb.append(scanner.nextLine()).toString() : awayTeamScoreSb.toString();
                        game.getHomeTeam().entrySet().stream().findFirst().ifPresentOrElse(homeTeam -> homeTeam.setValue(homeTeamScore), () -> {System.out.println("Unable to update the score");
                            showMainMenu(scoreBoardService, scanner, printStream, gameSummary);});
                        game.getAwayTeam().entrySet().stream().findFirst().ifPresentOrElse(awayTeam -> awayTeam.setValue(awayTeamScore), () ->  {System.out.println("Unable to update the score");
                            showMainMenu(scoreBoardService, scanner, printStream, gameSummary);});
                        System.out.println("Score updated successfully!");
                        getScoreSummary(printStream, gameSummary);
                    } else {
                        System.out.println("Game Not Found!!!");
                    }

                    showMainMenu(scoreBoardService, scanner, printStream, gameSummary);
                } else {
                    System.out.println("There is no ongoing matches from the input team name");
                }
            }
        }
    }

    /**
     * Finish an on going match
     * @param scoreBoardService
     * @param scanner
     * @param printStream
     * @param gameSummary
     */
    private static void finishGame(ScoreBoardService scoreBoardService, Scanner scanner, PrintStream printStream, List<Game> gameSummary) {
        if (gameSummary.isEmpty()) {
            System.out.println("There are no ongoing live matches currently !");
        } else {
            getScoreSummary(printStream, gameSummary);
            System.out.println("Enter Home / Away team name to Finish the game. (Enter 5 to exit to Main Menu)");
            StringBuilder teamNamesb = new StringBuilder(scanner.next());
            String teamName = scanner.hasNextLine() ? teamNamesb.append(scanner.nextLine()).toString() : teamNamesb.toString();
            if (teamName.equalsIgnoreCase("5")) {
                System.out.println(EXITING_TO_MAIN_MENU);
                showMainMenu(scoreBoardService, scanner, printStream, gameSummary);
            } else {
                if(gameAlreadyExistForTheTeam(gameSummary, teamName)) {
                    Predicate<Game> isFinished = game -> null != game.getHomeTeam().get(teamName) || null != game.getAwayTeam().get(teamName);
                    List<Game> collect = gameSummary.stream().filter(isFinished).toList();
                    gameSummary.removeAll(collect);
                    System.out.println("Game Finished !");
                    getScoreSummary(printStream, gameSummary);
                    showMainMenu(scoreBoardService, scanner, printStream, gameSummary);
                } else {
                    System.out.println("There is no ongoing matches from the input team name");
                }
            }
        }
    }

    private static void getScoreSummary(PrintStream printStream, List<Game> gameSummary) {
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
        System.out.println("Enter home team name to proceed. (Enter 5 to exit to Main Menu)");
        StringBuilder homeTeamNameSb = new StringBuilder(scanner.next());
        String homeTeam = scanner.hasNextLine() ? homeTeamNameSb.append(scanner.nextLine()).toString() : homeTeamNameSb.toString();
        if (homeTeam.equalsIgnoreCase("5")) {
            System.out.println(EXITING_TO_MAIN_MENU);
            showMainMenu(scoreBoardService, scanner, printStream, gameSummary);
        } else {
            System.out.println("Enter away team name (Enter 5 to exit to Main Menu)");
            StringBuilder awayTeamNameSb = new StringBuilder(scanner.next());
            String awayTeam = scanner.hasNextLine() ? awayTeamNameSb.append(scanner.nextLine()).toString() : awayTeamNameSb.toString();
            if (awayTeam.equalsIgnoreCase("5")) {
                System.out.println(EXITING_TO_MAIN_MENU);
                showMainMenu(scoreBoardService, scanner, printStream, gameSummary);
            } else {
                if(!gameAlreadyExistForTheTeam(gameSummary, homeTeam, awayTeam)) {
                    Game gameStarted = scoreBoardService.startGame(homeTeam, awayTeam);
                    // add match to live score summary
                    gameSummary.add(gameStarted);
                    System.out.println("Game Started !");
                    // Print current score summary of live games
                    System.out.println("Score Summary (Live Score)\n================");
                    getScoreSummary(printStream, gameSummary);
                    // show main menu after successfully starting the game
                    showMainMenu(scoreBoardService, scanner, printStream, gameSummary);
                } else {
                    System.out.println("Team is already playing, You cant start another game with the same team !");
                }
            }
        }
    }

    private static boolean gameAlreadyExistForTheTeam(List<Game> gameSummary, String homeTeam, String... awayTeam) {
        Predicate<Game> teamAlreadyPlaying = game -> null != game.getHomeTeam().get(homeTeam) || null != game.getAwayTeam().get(homeTeam)  ||
                null != game.getHomeTeam().get(awayTeam) || null != game.getAwayTeam().get(awayTeam);
        return gameSummary.stream().anyMatch(teamAlreadyPlaying);
    }
}
