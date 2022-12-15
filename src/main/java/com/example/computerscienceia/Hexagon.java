package com.example.computerscienceia;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.Objects;

public class Hexagon {
    private boolean canPositionOnIt;
    private boolean mustStopOnIt;
    private int movesPenalty;
    private Obstacle obstacle;
    private final Polygon hex;
    private boolean hasTroops;
    private final int row;
    private final int col;
    private final Label troopLabel;
    private String section;
    private int distanceToMoveToCurrentPosition = 0;
    private int defenseBonusAgainstTanks = 0;
    private int defenseBonusAgainstInfantry = 0;
    public Hexagon(Polygon hex, int row, int col){
        this.row = row;
        this.col = col;
        this.hex = hex;
        String name;

        troopLabel = new Label();
        Color color = (Color) hex.getFill();
        if(color.equals(Color.GREY)){
            name = "Village";
        }
        else if (color.equals(Color.LIGHTGREEN)){
            name = "Plains";
        }
        else if (color.equals(Color.BLUE)){
            name = "River";
            }
        else if(color.equals(Color.DARKGREEN)){
            name = "Forest";
        }
        else{
            name = "None of the above";
        }
        switch (Objects.requireNonNull(name)) {
            case "River" -> {
                canPositionOnIt = false;
                mustStopOnIt = false;
                movesPenalty = 0;
            }
            case "Plains" -> {
                canPositionOnIt = true;
                mustStopOnIt = false;
                movesPenalty = 0;
            }
            case "Forest", "Village" -> {
                defenseBonusAgainstInfantry = 1;
                defenseBonusAgainstTanks = 2;
                canPositionOnIt = true;
                mustStopOnIt = true;
                movesPenalty = 3;
            }
        }
        attachObstacle();
        updateTroops();
        calculateAndSetSection();
    }
    public boolean getMustStopOnIt(){
        attachObstacle();
        if(obstacle != null && obstacle.getMustStopOnIt()){
            this.mustStopOnIt = true;
        }
        return this.mustStopOnIt;
    }
    public int getDefenseBonusAgainstTanks(){
        return defenseBonusAgainstTanks;
    }
    public int getDefenseBonusAgainstInfantry(){
        return defenseBonusAgainstInfantry;
    }
    private void attachObstacle(){
        for(Obstacle obs : Main.listObstacles){
            if(obs != null && obs.getRow() == this.row && obs.getCol() == this.col){
                obstacle = obs;
            }
        }
    }
    public void setDistanceToMoveToCurrentLocation(int dist){
        if(distanceToMoveToCurrentPosition == 0) {
            distanceToMoveToCurrentPosition = dist;
        }
        else if(distanceToMoveToCurrentPosition > dist){
            distanceToMoveToCurrentPosition = dist;
        }
    }
    public int getDistanceToMoveToCurrentPosition(){
        return distanceToMoveToCurrentPosition;
    }
    public int getRow(){
        return row;
    }
    public int getCol(){
        return col;
    }
    public String getSection(){
        return this.section;
    }
    public void calculateAndSetSection(){
        if(row % 2 ==0){
            if(col <= 3){
                section = "Left";
            }
            else if (col <= 8){
                section = "Center";
            }
            else{
                section = "Right";
            }
        }
        else{
            if(col < 3){
                section = "Left";
            }
            else if(col == 3){
                section = "Left and Center";
            }
            else if (col <= 7){
                section = "Center";
            }
            else if(col == 8){
                section = "Center and Right";
            }
            else{
                section = "Right";
            }
        }
    }
    public void updateTroops(){
        hasTroops = false;
        for(int i = 0; i<Main.listUnit.length;i++){
            if(Main.listUnit[i] != null && Main.listUnit[i].getRow() == row && Main.listUnit[i].getColumn() == col){
                hasTroops = true;
            }
        }
    }
    public Boolean getCanPositionOnIt(){
        updateTroops();
        if(obstacle != null){
            if (canPositionOnIt || obstacle.getNameOfObstacle().equals("Bridges")){
                return !hasTroops();
            }
        }
        else{
            if(canPositionOnIt){
                return !hasTroops();
            }
        }
        return false;
    }
    public int getMovesPenalty(){
        if(movesPenalty < 3 && obstacle != null){
            movesPenalty = obstacle.getMovesPenalty();
        }
        return movesPenalty;
    }
    public Polygon getHexagon(){
        return this.hex;
    }
    public boolean hasTroops(){
        updateTroops();
        return hasTroops;
    }
    public int getIndexUnit(){
        int indexUnit = -1;
        for(int i = 0; i < Main.listUnit.length;i++){
            if(Main.listUnit[i] != null && Main.listUnit[i].getRow() == row && Main.listUnit[i].getColumn() == col){
                indexUnit = i;
            }
        }
        return indexUnit;
    }
    public Unit getUnit(){
        updateTroops();
        if(getIndexUnit() != -1){
            return Main.listUnit[getIndexUnit()];
        }
        else{
            return null;
        }
    }
    public boolean returnIsAllied(){
        int unitIndex = getIndexUnit();
        return Main.listUnit[unitIndex].getIsAllied();
    }
    public Obstacle getObstacle(){
        attachObstacle();
        return obstacle;
    }
    public Label getTroopLabel(){
        int indexUnit= getIndexUnit();
        switch(Main.listUnit[indexUnit].getUnitType()){
            case "Infantry" ->{
                troopLabel.setText("I I I I ");
            }
            case "Armored vehicles" -> {
                troopLabel.setText("T T T");
            }
            case "Artillery" -> {
                troopLabel.setText("A A");
            }
        }
        if(Main.listUnit[indexUnit].getIsAllied()){
            troopLabel.setStyle("-fx-text-fill: green;-fx-font: 25 georgia;");
        }
        else{
            troopLabel.setStyle("-fx-text-fill: black;-fx-font: 25 georgia;");
        }
        return troopLabel;
    }
}
