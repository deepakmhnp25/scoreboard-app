package com.sportsradar.scoreboard.service;

import com.sportsradar.scoreboard.model.Game;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

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
        Assert.assertEquals("0", startingGame.getHomeTeam().get(homeTeam));
        Assert.assertEquals("0", startingGame.getAwayTeam().get(awayTeam));
        for(Map.Entry<String, String> entry: startingGame.getHomeTeam().entrySet()){
            Assert.assertEquals(homeTeam, entry.getKey());
        }
        for(Map.Entry<String, String> entry: startingGame.getAwayTeam().entrySet()){
            Assert.assertEquals(awayTeam, entry.getKey());
        }
    }

    /**
     * Update score upon user input
     */
    @Test
    public void shouldUpdateScore() {
        Map<String, String> homeTeam = new HashMap<>();
        Map<String, String> awayTeam = new HashMap<>();
        homeTeam.put("Manu FC", "0");
        awayTeam.put("Barcelona FC", "0");
        Game game = new Game();
        game.setHomeTeam(homeTeam);
        game.setAwayTeam(awayTeam);
        InputStream stdin = supplyInputs("2\n3");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(byteArrayOutputStream);
        PrintStream stdout = System.out;
        System.setOut(ps);
        scoreBoardService.updateScore(new Scanner(System.in),stdout, Optional.of(game));
        System.setIn(stdin);
        System.setOut(stdout);
        Assert.assertEquals("2", game.getHomeTeam().entrySet().stream().findFirst().get().getValue());
        Assert.assertEquals("3", game.getAwayTeam().entrySet().stream().findFirst().get().getValue());
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