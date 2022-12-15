package com.example.computerscienceia;

import javafx.scene.shape.Line;

public class Obstacle {
    private final String nameOfObstacle;
    private int movesPenalty;
    private final int row;
    private final int col;
    private Line firstLine;
   private Line secondLine;
   private boolean mustStopOnIt;
    public Obstacle(String obstacleName, int row, int col){
        this.row = row;
        this.col = col;
        nameOfObstacle = obstacleName;

        switch(obstacleName){
            case "Hedgehogs", "Barbed wire" -> {
                movesPenalty = 10;
                mustStopOnIt = true;
            }
            case "Bunkers", "Bridges", "Sand Bags" -> {
                movesPenalty = 0;
                mustStopOnIt = false;
            }
        }
    }
    public boolean getMustStopOnIt(){
        return mustStopOnIt;
    }
    public int getMovesPenalty(){
        return movesPenalty;
    }
    public int getShootProtection(String unitName){
        switch (unitName) {
            case "Infantry":
                if (nameOfObstacle.equals("Bunkers") || nameOfObstacle.equals("Sand Bags")) {
                    return 1;
                }
                break;
            case "Armored vehicles":
                if (nameOfObstacle.equals("Bunkers")) {
                    return 2;
                } else if (nameOfObstacle.equals("Sand Bags")) {
                    return 1;
                }
                break;
            default:
                return 0;
        }
        return 0;
    }
    public int getRow(){
        return this.row;
    }
    public int getCol(){
        return this.col;
    }
    public Line getFirstLine(){
        return firstLine;
    }
    public void setLine(Line passedFirstLine){
        firstLine = passedFirstLine;
    }
    public void setSecondLine(Line passedSecondLine){
        secondLine = passedSecondLine;
    }
    public Line getSecondLine(){
        return secondLine;
    }
    public String getNameOfObstacle(){
        return this.nameOfObstacle;
    }
}
