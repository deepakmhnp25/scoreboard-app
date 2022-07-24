import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * Test Class for Testing Various Functionalities of Scoreboard Application
 *
 * @Autthor Deepak Mohan
 * @Version 1.0
 */
public class ScoreboardApplicationTest {

    private ScoreboardApplication scoreboardApplication;

    @Before
    public void initTest() {
        scoreboardApplication = new ScoreboardApplication();
    }

    /**
     * Application should show main menu upon start up
     */
    @Test
    public void shouldShowMainMenu() {
        // Given show main menu and exit using option 5
        InputStream stdin = supplyInputs("5");
        // when
        ByteArrayOutputStream byteArrayOutputStream = executeSelectedMenuOption(stdin);
        //Then
        assertOutput(byteArrayOutputStream, "Main Menu");
    }

    /**
     * User failed to select a valid main menu option
     */
    @Test
    public void invalidMenuOption() {
        // Given wrong option "hi" selected
        InputStream stdin = supplyInputs("hi\n5");
        // when
        ByteArrayOutputStream byteArrayOutputStream = executeSelectedMenuOption(stdin);
        //Then
        assertOutput(byteArrayOutputStream, "============ PLEASE ENTER A VALID MENU OPTION ! ============");
    }

    /**
     * Start the game using a home team and away team name
     *
     * @throws Exception
     */
    @Test
    public void shouldStartAGame() throws Exception {
        // Given option 1 to start the game, select start the game, enter home and away teams and finally exit
        InputStream stdin = supplyInputs("1\nLiverpool FC\nAC Milan\n5");
        // when
        ByteArrayOutputStream byteArrayOutputStream = executeSelectedMenuOption(stdin);
        //Then
        assertOutput(byteArrayOutputStream, "Liverpool FC - AC Milan: 0 - 0");
    }

    /**
     * This test ensures the ability to start multiple games
     *
     * @throws Exception
     */
    @Test
    public void shouldStartMultipleGames() throws Exception {

        // Given option 1 to start the game, add multiple games
        InputStream stdin = supplyInputs("1\nLiverpool FC\nAC Milan\n1\nManu FC\nBarcelona\n5");
        // when
        ByteArrayOutputStream byteArrayOutputStream = executeSelectedMenuOption(stdin);
        ;
        // then
        assertOutput(byteArrayOutputStream, "Liverpool FC - AC Milan: 0 - 0");
        assertOutput(byteArrayOutputStream, "Manu FC - Barcelona: 0 - 0");
    }

    /**
     * Same team can not play more than one match
     *
     * @throws Exception
     */
    @Test
    public void sameTeamSouldOnlyPlayOneMatch() throws Exception {

        // Given option 1 to start the game, add multiple games
        InputStream stdin = supplyInputs("1\nLiverpool FC\nAC Milan\n1\nLiverpool FC\nAC Milan\n5");
        // when
        ByteArrayOutputStream byteArrayOutputStream = executeSelectedMenuOption(stdin);
        ;
        // then
        assertOutput(byteArrayOutputStream, "Team is already playing, You cant start another game with the same team !");
    }

    /**
     * Ensures Showing main menu upon failing to enter home team name
     */
    @Test
    public void shouldShowMainMenuFailingToEnterHomeTeam() {
        // Given option 1 to start the game, Enter show main menu option instead of adding home team name
        InputStream stdin = supplyInputs("1\n5");
        // when
        ByteArrayOutputStream byteArrayOutputStream = executeSelectedMenuOption(stdin);
        ;
        // then
        assertOutput(byteArrayOutputStream, "Exiting to Main Menu ...");
    }

    /**
     * Ensures Showing main menu upon failing to enter away team name
     */
    @Test
    public void shouldShowMainMenuFailingToEnterAwayTeam() {
        // Given option 1 to start the game, Enter show main menu option instead of adding away team name
        InputStream stdin = supplyInputs("1\nLiverpool FC\n5");
        // when
        ByteArrayOutputStream byteArrayOutputStream = executeSelectedMenuOption(stdin);
        ;
        // then
        assertOutput(byteArrayOutputStream, "Exiting to Main Menu ...");
    }

    /**
     * select finish game when there are no ongoing matches
     */
    @Test
    public void thereAreNoLiveMatches() {
        // Given option 2 to finish the game
        InputStream stdin = supplyInputs("2");
        // when
        ByteArrayOutputStream byteArrayOutputStream = executeSelectedMenuOption(stdin);
        // then
        assertOutput(byteArrayOutputStream, "There are no ongoing live matches currently !");
    }

    /**
     * Finish a game upon selection and remove from scoreboard
     */
    @Test
    public void shouldFinishGame() {
        // Given option 1 to start the game, start multiple games, finish Liverpool FC game
        InputStream stdin = supplyInputs("1\nLiverpool FC\nAC Milan\n1\nManu FC\nBarcelona\n2\nLiverpool FC\n5");
        // when
        ByteArrayOutputStream byteArrayOutputStream = executeSelectedMenuOption(stdin);
        // then
        assertOutput(byteArrayOutputStream, "Game Finished !");
    }

    /**
     * Trying to finish a game which is not ongoing.
     */
    @Test
    public void teamShouldPlayToFinishTheGame() {
        // Given option 1 to start the game,
        InputStream stdin = supplyInputs("1\nManu FC\nBarcelona\n2\nLiverpool FC\n5");
        // when
        ByteArrayOutputStream byteArrayOutputStream = executeSelectedMenuOption(stdin);
        // then
        assertOutput(byteArrayOutputStream, "There is no ongoing matches from the input team name");
    }

    /**
     * Update score for an ongoing match
     */
    @Test
    public void shouldUpdateScore() {
        // Given option 1 to start the game, update the score
        InputStream stdin = supplyInputs("1\nManu FC\nBarcelona\n3\nManu FC\n2\n1");
        // when
        ByteArrayOutputStream byteArrayOutputStream = executeSelectedMenuOption(stdin);
        // then
        assertOutput(byteArrayOutputStream, "Score updated successfully!");
    }

    @Test
    public void matchShouldExistToUpdateTheScore() {
        // Given option 1 to start the game, try to update the score for another match
        InputStream stdin = supplyInputs("1\nManu FC\nBarcelona\n3\nLiverpool FC");
        // when
        ByteArrayOutputStream byteArrayOutputStream = executeSelectedMenuOption(stdin);
        // then
        assertOutput(byteArrayOutputStream, "There is no ongoing matches from the input team name");
    }

    /**
     * Generic Method to assert the output
     *
     * @param byteArrayOutputStream
     * @param expected
     */
    private void assertOutput(ByteArrayOutputStream byteArrayOutputStream, String expected) {
        String outputText = byteArrayOutputStream.toString();
        Assert.assertTrue(outputText.contains(expected));
    }


    /**
     * Generic Method to execute Selected menu operation
     *
     * @param stdin
     * @return
     */
    private ByteArrayOutputStream executeSelectedMenuOption(InputStream stdin) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(byteArrayOutputStream);
        PrintStream stdout = System.out;
        System.setOut(ps);
        String[] args = {};
        ScoreboardApplication.main(args);
        System.setIn(stdin);
        System.setOut(stdout);
        ;
        return byteArrayOutputStream;
    }

    /**
     * Generic Method to create user inputs to the system
     *
     * @param userInputs
     * @return
     */
    private InputStream supplyInputs(String userInputs) {
        InputStream stdin = System.in; // backup System.in to restore it later
        ByteArrayInputStream in = new ByteArrayInputStream(userInputs.getBytes());
        System.setIn(in);
        return stdin;
    }
}