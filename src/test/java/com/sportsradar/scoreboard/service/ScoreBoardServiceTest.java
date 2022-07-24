package com.sportsradar.scoreboard.service;

import com.sportsradar.scoreboard.model.Score;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;


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
        Score startingScore = scoreBoardService.startGame(homeTeam, awayTeam);

        // Then
        // checking the initial scores
        Assert.assertEquals("0", startingScore.getHomeTeamScore().get(homeTeam));
        Assert.assertEquals("0", startingScore.getAwayTeamScore().get(awayTeam));
        for(Map.Entry<String, String> entry: startingScore.getHomeTeamScore().entrySet()){
            Assert.assertEquals(homeTeam, entry.getKey());
        }
        for(Map.Entry<String, String> entry: startingScore.getAwayTeamScore().entrySet()){
            Assert.assertEquals(awayTeam, entry.getKey());
        }
    }

}