package com.example.computerscienceia;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import java.util.ArrayList;

public class Unit {
    private final String unitType;
    private final int moveDistance;
    private final int moveShootDistance;
    private final int shootDistance;
    private final boolean isAllied;
    private int row;
    private int column;

    private Hexagon[][] listHexagons;
    private ArrayList<Polygon> possibleMoveLocations;
    private final ArrayList<Integer> numberOfDiceArr;
    private boolean isAttacking;
    private Color highlightColor;
    public Unit(String unitType, int row, int col, boolean isAllied){
        this.isAllied = isAllied;
        this.row = row;
        this.column = col;
        this.unitType = unitType;
        numberOfDiceArr = new ArrayList<>();

        if(unitType.equals("Infantry")){
            moveDistance = 2;
            moveShootDistance = 1;
            shootDistance = 3;
            numberOfDiceArr.add(3);
            numberOfDiceArr.add(2);
            numberOfDiceArr.add(1);

        }
        else if(unitType.equals("Armored vehicles")){
            moveDistance = 3;
            moveShootDistance = 3;
            shootDistance = 3;
            numberOfDiceArr.add(3);
            numberOfDiceArr.add(3);
            numberOfDiceArr.add(3);
        }
        else{
            moveDistance = 1;
            moveShootDistance =0;
            shootDistance = 6;
            numberOfDiceArr.add(3);
            numberOfDiceArr.add(3);
            numberOfDiceArr.add(2);
            numberOfDiceArr.add(2);
            numberOfDiceArr.add(1);
            numberOfDiceArr.add(1);

        }
    }
    public ArrayList<Integer> getNumberOfDiceArr(){
        return numberOfDiceArr;
    }
    public int getMoveShootDistance(){
        return moveShootDistance;
    }
    public ArrayList<Polygon> getPossibleMoveLocations(){
        return possibleMoveLocations;
    }
    public void setRow(int row){
        this.row = row;
    }
    public void setCol(int col){
        this.column = col;
    }
    public int getRow(){
        return row;
    }
    public int getColumn(){
        return column;
    }
    public String getUnitType(){
        return unitType;
    }
    public boolean getIsAllied(){
        return this.isAllied;
    }
    public void updateLastClickedColAndRow(){
        Main.lastClickedCol = this.column;
        Main.lastClickedRow = this.row;
    }
    public void highlightAttackPositions(){
        showMovePositions(Color.RED, true);
    }
    public void showMovePositions(Color highlightColor, boolean passedIsAttacking){
        this.isAttacking = passedIsAttacking;
        this.listHexagons = Main.listHexagons;
        this.highlightColor = highlightColor;
        updateLastClickedColAndRow();
        Main.isClickingToMoveUnit = true;


        if(!isAttacking) {
            possibleMoveLocations = new ArrayList<>();
            possibleMoveLocations.add(listHexagons[row][column].getHexagon());
            listHexagons[row][column].getHexagon().setStroke(highlightColor);
            listHexagons[row][column].getHexagon().setStrokeWidth(5);
            expandInALlDirections(row,column,moveDistance);
        }
        else{
            expandInALlDirections(row,column,shootDistance);
        }
        Main.createLines();
        Main.sendObstaclesToFront();
        sendTroopsToFront();
    }
    public static void sendTroopsToFront(){
        for(int r = 0; r< Main.troopLabelList.length;r++){
            for(int c = 0; c< Main.troopLabelList[r].length;c++){
                if(Main.troopLabelList[r][c] != null) {
                    Main.troopLabelList[r][c].toFront();
                }
            }
        }
    }
    public void expandInALlDirections(int rowOfPolygon, int colOfPolygon, int remainingDist){
        goNE(rowOfPolygon,colOfPolygon,remainingDist);
        goE(rowOfPolygon,colOfPolygon,remainingDist);
        goW(rowOfPolygon,colOfPolygon,remainingDist);
        goSE(rowOfPolygon,colOfPolygon,remainingDist);
        goNW(rowOfPolygon,colOfPolygon,remainingDist);
        goSW(rowOfPolygon,colOfPolygon,remainingDist);
    }
    public void goNE(int startingRow, int startingCol, int remainingDist){
        if(remainingDist > 0){
            if(startingRow %2 == 1){
                checkAndHighlight(startingRow-1,startingCol+1,remainingDist);
            }
            else{
                checkAndHighlight(startingRow-1,startingCol,remainingDist);
            }
        }
    }
    public void goE(int startingRow, int startingCol, int remainingDist){
        if(remainingDist > 0){
            checkAndHighlight(startingRow,startingCol+1,remainingDist);
        }
    }
    public void goSE(int startingRow, int startingCol, int remainingDist){
        if(remainingDist > 0){
            if(startingRow %2 == 1){
                checkAndHighlight(startingRow+1,startingCol+1,remainingDist);
            }
            else{
                checkAndHighlight(startingRow+1,startingCol,remainingDist);
            }
        }
    }
    public void goNW(int startingRow, int startingCol, int remainingDist){
        if(remainingDist > 0){
            if(startingRow %2 == 1){
                checkAndHighlight(startingRow-1,startingCol,remainingDist);
            }
            else{
                checkAndHighlight(startingRow-1,startingCol-1,remainingDist);
            }
        }
    }
    public void goW(int startingRow, int startingCol, int remainingDist){
        if(remainingDist > 0){
            checkAndHighlight(startingRow,startingCol-1,remainingDist);
        }
    }
    public void goSW(int startingRow, int startingCol, int remainingDist){
        if(remainingDist > 0){
            if(startingRow %2 == 1){
                checkAndHighlight(startingRow+1,startingCol,remainingDist);
            }
            else{
                checkAndHighlight(startingRow+1,startingCol-1,remainingDist);
            }
        }
    }
    public void checkAndHighlight(int endingRow, int endingCol, int remainingMoveDistance){
        int movePenalty = 0;
        if (canMoveOrShoot(endingRow,endingCol)) {
            if(isAttacking){
                listHexagons[endingRow][endingCol].updateTroops();
                if(listHexagons[endingRow][endingCol].hasTroops() && listHexagons[endingRow][endingCol].getUnit().isAllied != this.isAllied){
                    listHexagons[endingRow][endingCol].getHexagon().setStroke(highlightColor);
                    listHexagons[endingRow][endingCol].getHexagon().setStrokeWidth(5);
                    listHexagons[endingRow][endingCol].getHexagon().toFront();
                    listHexagons[endingRow][endingCol].setDistanceToMoveToCurrentLocation(this.shootDistance - remainingMoveDistance+1);
                }
            } else {
                listHexagons[endingRow][endingCol].getHexagon().setStroke(highlightColor);
                listHexagons[endingRow][endingCol].getHexagon().setStrokeWidth(5);
                listHexagons[endingRow][endingCol].getHexagon().toFront();
                listHexagons[endingRow][endingCol].setDistanceToMoveToCurrentLocation(this.moveDistance - remainingMoveDistance+1);
            }
            if(!isAttacking){
                movePenalty = listHexagons[endingRow][endingCol].getMovesPenalty();
                if(listHexagons[endingRow][endingCol].getObstacle() != null){
                    movePenalty += listHexagons[endingRow][endingCol].getObstacle().getMovesPenalty();
                }
            }
            expandInALlDirections(endingRow, endingCol, remainingMoveDistance - 1 - movePenalty);
        }
    }
    public boolean canMoveOrShoot(int endingRow, int endingCol){
        if(endingRow >= 0 && endingRow < listHexagons.length){
            if (endingCol >= 0 && endingCol < listHexagons[endingRow].length) {
                if (!isAttacking) {
                    if (listHexagons[endingRow][endingCol].getCanPositionOnIt()) {
                        this.possibleMoveLocations.add(listHexagons[endingRow][endingCol].getHexagon());
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }
}
