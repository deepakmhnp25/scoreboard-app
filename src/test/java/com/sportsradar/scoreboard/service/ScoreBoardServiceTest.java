package com.sportsradar.scoreboard.service;

import com.sportsradar.scoreboard.model.Game;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;

/**
 * Test Class for unit testing various operations
 *
 * @Author Deepak Mohan
 * @Version 1.0
 */
public class ScoreBoardServiceTest {

    private ScoreBoardService scoreBoardService;

    @Before
    public void initTest(){
        scoreBoardService = new ScoreBoardService();
    }

    /**
     * This test method ensures the capability to start a new match
     */
    @Test
    public void shouldStartAMatch(){
        // Given
        String homeTeam = "Liverpool FC";
        String awayTeam = "Arsenal FC";

        // When
        Game startingGame = scoreBoardService.startGame(homeTeam, awayTeam);

        // Then
        // checking the initial scores
        Assert.assertEquals(Integer.valueOf(0), startingGame.getHomeTeam().get(homeTeam));
        Assert.assertEquals(Integer.valueOf(0), startingGame.getAwayTeam().get(awayTeam));
        for(Map.Entry<String, Integer> entry: startingGame.getHomeTeam().entrySet()){
            Assert.assertEquals(homeTeam, entry.getKey());
        }
        for(Map.Entry<String, Integer> entry: startingGame.getAwayTeam().entrySet()){
            Assert.assertEquals(awayTeam, entry.getKey());
        }
    }

    /**
     * Update score upon user input
     */
    @Test
    public void shouldUpdateScore() {
        //when
        Game game = getGame("Manu FC", "Barcelona FC");
        scoreUpdate("2\n3", game);
        //then
        Assert.assertEquals(Integer.valueOf(2), Integer.valueOf(game.getHomeTeam().entrySet().stream().findFirst().get().getValue()));
        Assert.assertEquals(Integer.valueOf(3), Integer.valueOf(game.getAwayTeam().entrySet().stream().findFirst().get().getValue()));
    }

    @Test
    public void shouldShowSortedSummary() {
        //when
        Game game1 = getGame("Liverpool FC", "As Monaco");
        Game game2 = getGame("Juventus FC", "AC Milan");
        //when
        scoreUpdate("1\n1", game1);
        scoreUpdate("5\n3", game2);
        List<Game> gameSummary = new ArrayList<>();
        gameSummary.add(game1);
        gameSummary.add(game2);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(byteArrayOutputStream);
        PrintStream stdout = System.out;
        System.setOut(ps);

        scoreBoardService.getScoreSummary(ps, gameSummary);

        //then
        assertOutput(byteArrayOutputStream, "LIVE SCORE (Summary)\n==============\r\nJuventus FC - AC Milan: 5 - 3\r\nLiverpool FC - As Monaco: 1 - 1\r\n");
    }

    private void scoreUpdate(String userInputs, Game game) {
        InputStream stdin = supplyInputs(userInputs);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(byteArrayOutputStream);
        PrintStream stdout = System.out;
        System.setOut(ps);
        scoreBoardService.updateScore(new Scanner(System.in),stdout, Optional.of(game));
        System.setIn(stdin);
        System.setOut(stdout);
    }

    private Game getGame(String Liverpool_FC, String As_Monaco) {
        Map<String, Integer> homeTeam = new HashMap<>();
        Map<String, Integer> awayTeam = new HashMap<>();
        homeTeam.put(Liverpool_FC, 0);
        awayTeam.put(As_Monaco, 0);
        Game game = new Game();
        game.setHomeTeam(homeTeam);
        game.setAwayTeam(awayTeam);
        return game;
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
}