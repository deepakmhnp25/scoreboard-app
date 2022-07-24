import com.sportsradar.scoreboard.model.Score;
import com.sportsradar.scoreboard.service.ScoreBoardService;
import org.apache.commons.lang3.StringUtils;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ScoreboardApplication {

    public static void main(String args[]) {

        ScoreBoardService scoreBoardService = new ScoreBoardService();

        System.out.println("CHOOSE AN OPTION BELOW\n=====================");
        Scanner scanner = new Scanner(System.in);
        PrintStream printStream = new PrintStream(System.out);
        List<Score> scoreSummary = new ArrayList<>();
        showMainMenu(scoreBoardService, scanner, printStream, scoreSummary);

    }

    /**
     * This method is to show the Main Menu
     * @param scoreBoardService
     * @param scanner
     * @param printStream
     * @param scoreSummary
     */
    private static void showMainMenu(ScoreBoardService scoreBoardService, Scanner scanner, PrintStream printStream, List<Score> scoreSummary) {
        System.out.println(" Main Menu");
        System.out.println(" 1. Start a new game \n 2. Finish a game \n 3. Update score \n 4. Live Score (Summary) \n 5. Exit Score Board App");
        System.out.println("Enter your option ");
        if(scanner.hasNext()) {
            StringBuilder menuOptionSb = new StringBuilder(scanner.next());
            String menuOptionSelected = scanner.hasNextLine() ? menuOptionSb.append(scanner.nextLine()).toString() : menuOptionSb.toString();
            // validate menu input and perform selected operation
            validateAndPerformScoreboardOperation(scoreBoardService, scanner, printStream, scoreSummary, menuOptionSelected);
        }
    }

    /**
     * validate menu input and perform selected operation
     * @param scoreBoardService
     * @param scanner
     * @param printStream
     * @param scoreSummary
     * @param menuOptionSelected
     */
    private static void validateAndPerformScoreboardOperation(ScoreBoardService scoreBoardService, Scanner scanner, PrintStream printStream, List<Score> scoreSummary, String menuOptionSelected) {
        if (StringUtils.equalsAny(menuOptionSelected, "1", "2", "3", "4", "5")) {
            // Valid option selected
            int option = Integer.parseInt(menuOptionSelected);
            if(option != 5){
                doScoreBoardOperation(scoreBoardService, scanner, printStream, scoreSummary, option);
            }
        } else {
            // Invalid option selected
            System.out.println("============ PLEASE ENTER A VALID MENU OPTION ! ============");
            showMainMenu(scoreBoardService, new Scanner(System.in), printStream, scoreSummary);
        }
    }

    private static void doScoreBoardOperation(ScoreBoardService scoreBoardService, Scanner scanner, PrintStream printStream, List<Score> scoreSummary, int option) {

        switch (option){
            // Option to start the game
            case 1:
                startNewGame(scoreBoardService, scanner, printStream, scoreSummary);
            case 2:
                if(scoreSummary.isEmpty()) {
                    System.out.println("There are no ongoing live matches currently !");
                } else {
                    scoreSummary.stream().forEach(s -> printStream.println(s.toString()));
                    System.out.println("Enter Home / Away team name to Finish the game");
                    StringBuilder teamNamesb = new StringBuilder(scanner.next());
                    String teamName = scanner.hasNextLine() ? teamNamesb.append(scanner.nextLine()).toString() : teamNamesb.toString();
                    if (teamName.equalsIgnoreCase("5")) {
                        System.out.println("Exiting to Main Menu ...");
                        showMainMenu(scoreBoardService, new Scanner(System.in), new PrintStream(System.out), scoreSummary);
                    } else {

                    }
                }

        }
    }

    /**
     * Method start a new game, exit to main menu upon failing to enter home / away team
     * @param scoreBoardService
     * @param scanner
     * @param printStream
     * @param scoreSummary
     */
    private static void startNewGame(ScoreBoardService scoreBoardService, Scanner scanner, PrintStream printStream, List<Score> scoreSummary) {
        System.out.println("Enter home team name to proceed. (Enter 5 to exit to Main Menu)");
        StringBuilder homeTeamNameSb = new StringBuilder(scanner.next());
        String homeTeam = scanner.hasNextLine() ? homeTeamNameSb.append(scanner.nextLine()).toString() : homeTeamNameSb.toString();
        if(homeTeam.equalsIgnoreCase("5")){
            System.out.println("Exiting to Main Menu ...");
            showMainMenu(scoreBoardService, new Scanner(System.in), new PrintStream(System.out), scoreSummary);
        } else {
            System.out.println("Enter away team name");
            StringBuilder awayTeamNameSb = new StringBuilder(scanner.next());
            String awayTeam = scanner.hasNextLine() ? awayTeamNameSb.append(scanner.nextLine()).toString() : awayTeamNameSb.toString();
            if (awayTeam.equalsIgnoreCase("5")) {
                System.out.println("Exiting to Main Menu ...");
                showMainMenu(scoreBoardService, new Scanner(System.in), new PrintStream(System.out), scoreSummary);
            } else {
                Score gameStarted = scoreBoardService.startGame(homeTeam, awayTeam);
                // add match to live score summary
                scoreSummary.add(gameStarted);
                System.out.println("Game Started !");
                // Print current score summary of live games
                System.out.println("Score Summary (Live Score)\n================");
                scoreSummary.stream().forEach(s -> printStream.println(s.toString()));
                // show main menu after successfully starting the game
                showMainMenu(scoreBoardService, scanner, new PrintStream(System.out), scoreSummary);
            }
        }
    }

}
