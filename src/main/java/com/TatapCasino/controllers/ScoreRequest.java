package com.TatapCasino.controllers;


import java.util.List;
import java.util.Map;

public class ScoreRequest {
    private String roomId;
    private List<Map<String, String>> playersScores;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public List<Map<String, String>> getPlayersScores() {
        return playersScores;
    }

    public void setPlayersScores(List<Map<String, String>> playersScores) {
        this.playersScores = playersScores;
    }
}
