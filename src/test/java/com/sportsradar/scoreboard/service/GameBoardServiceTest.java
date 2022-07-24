package com.sportsradar.scoreboard.service;

import com.sportsradar.scoreboard.model.Game;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;


public class GameBoardServiceTest {

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

}