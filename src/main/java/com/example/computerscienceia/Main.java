package com.example.computerscienceia;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Main extends Application {
    static Hexagon[][] listHexagons;
    ArrayList<Hexagon> alreadyMovedHexagons = new ArrayList<>();
    ArrayList<Hexagon> hexagonsMovedTooFar = new ArrayList<>();
    public static Color[][] listColors;
    public static Unit[] listUnit;
    public static Obstacle[] listObstacles;
    private int thresholdAmountOfKills;
    static AnchorPane mainPane;
    private Stage mainStage;
    private Stage summaryStage;
    private final ImageView firstDice = new ImageView();
    private final ImageView secondDice = new ImageView();
    private final ImageView thirdDice = new ImageView();
    public static Label[][] troopLabelList = new Label[9][13];

    private int numberOfPossibleUnitActivationsLeft;
    private int numberOfPossibleUnitActivationsRight;
    private int numberOfPossibleUnitActivationsCenter;
    private int numberOfMovedUnitsLeft = 0;
    private int numberOfMovedUnitsRight = 0;
    private int numberOfMovedUnitsCenter = 0;
    int remainingUnitsLeft;
    int remainingUnitsCenter;
    int remainingUnitsRight;

    private Card lastClickedCard;
    private Card card1;
    private Label cardLabel2;
    private Card card2;
    private Label cardLabel3;
    private Card card3;
    private Label cardLabel4;
    private Card card4;
    private Label cardLabel5;
    private Card card5;
    private Label cardLabel6;
    private Card card6;
    private Label cardLabel1;
    private Label playerLabel;
    Label firstScoreLabel;
    Label secondScoreLabel;


    static boolean isDoneMovingUnits = false;
    static boolean clickedAUnitWithWhichToAttack = false;
    static boolean isPlayer1Turn = true;
    public static boolean isClickingToMoveUnit = false;
    boolean isHandlingRetreat = false;
    private boolean clickedAUnitToAttack = false;



    Image armoredImage;
    Image grenadeImage;
    Image infantryImage;
    Image retreatImage;
    Image starImage;


    public static int lastClickedRow = -1;
    public static int lastClickedCol = -1;
    private int currentUnitIndex = -1;
    private int rowOfClick = -1;
    private int colOfClick = -1;
    int handleRetreatRemainingRetreats = 0;
    int lastClickedUnitIndex;
    private int alliedScore;
    private int axisScore;




    public static void main(String[] args) {
        launch(args);
    }
    @Override
    //First method called. Sets up the starting menu
    public void start(Stage startingStage) throws IOException {
        Parent startingRoot = FXMLLoader.load(getClass().getResource("startMenu.fxml"));
        Scene startingScene = new Scene(startingRoot);
        startingStage.setScene(startingScene);
        startingStage.show();
    }
    //Entered when first button on the starting Menu is clicked
    @FXML
    private void playScenario1() {
        listColors = getColors(1);
        listObstacles = getObstacles(1);
        listUnit = getUnits(1);
        thresholdAmountOfKills = 3;
        playGame();
    }
    //Entered when second button on the starting Menu is clicked
    @FXML
    private void playScenario2() {
        listColors = getColors(2);
        listObstacles = getObstacles(2);
        listUnit = getUnits(2);
        thresholdAmountOfKills = 2;
        playGame();
    }
    //Entered when third button on the starting menu is clicked
    @FXML
    private void playScenario3() {
        listColors = getColors(3);
        listObstacles = getObstacles(3);
        listUnit = getUnits(3);
        thresholdAmountOfKills = 3;
        playGame();
    }
    @FXML
    private void playGame() {
        mainStage = new Stage();
        mainPane = new AnchorPane();
        Scene mainScene = new Scene(mainPane, 1200, 1700);
        mainStage.setFullScreen(true);
        mainStage.setScene(mainScene);
        mainPane.getChildren().add(firstDice);
        mainPane.getChildren().add(secondDice);
        mainPane.getChildren().add(thirdDice);
        listHexagons = new Hexagon[9][];
        //Creates all the polygons for the board and adds them to listHexagons
        //Sets up the troops and vertical lines that separate the field
        createBoard();
        numberOfPossibleUnitActivationsLeft = numberOfPossibleActivationsSection("Left", isPlayer1Turn) +
                numberOfPossibleActivationsSection("Left and Center", isPlayer1Turn);
        numberOfPossibleUnitActivationsRight = numberOfPossibleActivationsSection("Right", isPlayer1Turn) +
                numberOfPossibleActivationsSection("Center and Right", isPlayer1Turn);
        numberOfPossibleUnitActivationsCenter = numberOfPossibleActivationsSection("Center", isPlayer1Turn) +
                numberOfPossibleActivationsSection("Center and Right", isPlayer1Turn) + numberOfPossibleActivationsSection("Left and Center", isPlayer1Turn);
        mainPane.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getX() < 926 && mouseEvent.getY() < 600) {
                handleUserClick(mouseEvent.getX(), mouseEvent.getY());
            }
        });
        setUpCards();
        mainStage.show();
    }
    public void createBoard() {
        for(int row = 0; row<= 8; row++) {
            ArrayList<Hexagon> tempHexagonArrList =  new ArrayList<>();
            double YCenter = 50 + (63*row);
            for (int col = 0; col <= 12 - row % 2; col++){
                Polygon hexagon = new Polygon();
                double XCenter = (70*col) + 50 + (row % 2*35);

                Double[] hexagonPoints = {
                        XCenter - 35.0, YCenter + 21.0,
                        XCenter + 0.0 , YCenter + 42.0,
                        XCenter + 35.0, YCenter + 21.0,
                        XCenter + 35.0, YCenter - 21.0,
                        XCenter + 0.0 , YCenter - 42.0,
                        XCenter - 35.0, YCenter - 21.0
                };
                hexagon.getPoints().addAll(hexagonPoints);
                hexagon.setStrokeWidth(2);
                hexagon.setFill(listColors[row][col]);
                hexagon.setStroke(Color.BLACK );
                mainPane.getChildren().add(hexagon);

                tempHexagonArrList.add(new Hexagon(hexagon,row,col));
            }
            Hexagon[] tempHexagonArr = new Hexagon[tempHexagonArrList.size()];

            for(int i = 0; i < tempHexagonArrList.size();i++){
                tempHexagonArr[i] = tempHexagonArrList.get(i);
            }
            listHexagons[row] = tempHexagonArr;
        }

        addTroopsToBoard();
        addObstaclesToBoard();

        createLines();
        playerLabel = new Label();
        playerLabel.setTranslateX(650);
        playerLabel.setTranslateY(600);
        playerLabel.setBorder(Border.stroke(Color.BLACK));
        playerLabel.setAlignment(Pos.CENTER);
        playerLabel.setStyle("-fx-text-fill: green;-fx-font: 20 georgia;");

        Label killsObjective = new Label();
        killsObjective.setTranslateX(650);
        killsObjective.setTranslateY(660);
        killsObjective.setBorder(Border.stroke(Color.BLACK));
        killsObjective.setAlignment(Pos.CENTER);
        killsObjective.setStyle("-fx-text-fill: green;-fx-font: 20 georgia;");
        killsObjective.setText("The kill Objective is " + thresholdAmountOfKills) ;
        mainPane.getChildren().add(killsObjective);


        if (isPlayer1Turn){
            playerLabel.setText("Turn : \n Allied Player") ;
        }
        else{
            playerLabel.setText("Turn : \n Axis Player");
        }
        mainPane.getChildren().add(playerLabel);

        firstScoreLabel = new Label("Allied Player Score is: 0");
        firstScoreLabel.setTranslateX(950);
        firstScoreLabel.setTranslateY(650);
        firstScoreLabel.setBorder(Border.stroke(Color.BLACK));
        firstScoreLabel.setStyle("-fx-text-fill: green;-fx-font: 17 georgia;");
        mainPane.getChildren().add(firstScoreLabel);

        secondScoreLabel = new Label("Axis Player Score is: 0");
        secondScoreLabel.setTranslateX(950);
        secondScoreLabel.setTranslateY(700);
        secondScoreLabel.setBorder(Border.stroke(Color.BLACK));
        secondScoreLabel.setStyle("-fx-text-fill: black;-fx-font: 17 georgia;");
        mainPane.getChildren().add(secondScoreLabel);

        Button showTroopAndTerrainInfoButton = new Button("Click Here for A detailed overview  \n of the terrain and troops");
        showTroopAndTerrainInfoButton.setTranslateX(650);
        showTroopAndTerrainInfoButton.setTranslateY(700);
        secondScoreLabel.setStyle("-fx-text-fill: black;-fx-font: 17 georgia;");
        mainPane.getChildren().add(showTroopAndTerrainInfoButton);

        showTroopAndTerrainInfoButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                summaryStage = new Stage();
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("troopAndTerrainSummary.fxml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                mainStage.setFullScreen(false);
                summaryStage.setFullScreen(false);
                Scene sceneStart = new Scene(root);
                summaryStage.setScene(sceneStart);
                summaryStage.show();
            }
        });
    }
    public static void sendObstaclesToFront(){
        for(int i = 0; i < listObstacles.length;i++) {
            if (listObstacles[i] != null) {
                listObstacles[i].getFirstLine().toFront();
                if (listObstacles[i].getSecondLine() != null) {
                    listObstacles[i].getSecondLine().toFront();
                }
            }
        }
    }
    //Sets up all the troop labels and adds them on the board
    public void addTroopsToBoard(){
        for(int r = 0; r < listHexagons.length; r++){
            double YCenter = 29 + (63*r);
            for(int c = 0; c < listHexagons[r].length;c++){
                Label troopLabel;
                double XCenter = (70*c) + 15 + (r % 2*35);
                if(listHexagons[r][c].hasTroops()) {
                    troopLabel = listHexagons[r][c].getTroopLabel();
                }
                else {
                    troopLabel = new Label("");
                }
                troopLabelList[r][c] = troopLabel;
                troopLabel.setTranslateX(XCenter);
                troopLabel.setTranslateY(YCenter);
                mainPane.getChildren().add(troopLabel);
                listHexagons[r][c].getHexagon().toFront();
                troopLabel.toFront();
            }
        }
    }
    public static void createLines(){
        Line firstVertical = new Line(295,29,295,575);
        mainPane.getChildren().add(firstVertical);
        firstVertical.setStroke(Color.RED);
        firstVertical.setStrokeWidth(4);
        firstVertical.setOpacity(0.75);

        Line secondVertical = new Line(645,29,645,575);
        mainPane.getChildren().add(secondVertical);
        secondVertical.setStroke(Color.RED);
        secondVertical.setStrokeWidth(4);
        secondVertical.setOpacity(0.75);
    }
    public void addObstaclesToBoard(){

        for(int i = 0; i<listObstacles.length;i++){
            int row = listObstacles[i].getRow();
            int col =listObstacles[i].getCol();
            switch (listObstacles[i].getNameOfObstacle()) {
                case "Bridges" -> {
                    double lineStartXCoordinate = ((listHexagons[row][col].getHexagon().getPoints().get(10) +
                            listHexagons[row][col].getHexagon().getPoints().get(8))) / 2.0;
                    double lineStartYCoordinate = ((listHexagons[row][col].getHexagon().getPoints().get(11) +
                            listHexagons[row][col].getHexagon().getPoints().get(9))) / 2.0;
                    double lineEndXCoordinate = ((listHexagons[row][col].getHexagon().getPoints().get(2) +
                            listHexagons[row][col].getHexagon().getPoints().get(4))) / 2.0;
                    double lineEndYCoordinate = ((listHexagons[row][col].getHexagon().getPoints().get(3) +
                            listHexagons[row][col].getHexagon().getPoints().get(5))) / 2.0;
                    Line bridgeLine = new Line(lineStartXCoordinate + 10.50007446, lineStartYCoordinate + 17.4999, lineEndXCoordinate - 10.5, lineEndYCoordinate - 17.499);
                    mainPane.getChildren().add(bridgeLine);
                    bridgeLine.setStroke(Color.GREY);
                    bridgeLine.setStrokeWidth(40.8166);
                    listObstacles[i].setLine(bridgeLine);
                    Unit.sendTroopsToFront();
                }
                case "Barbed wire" -> {
                    double lineStartXCoordinate = ((listHexagons[row][col].getHexagon().getPoints().get(0) + 20));
                    double lineStartYCoordinate = ((listHexagons[row][col].getHexagon().getPoints().get(1)));
                    double lineEndXCoordinate = ((listHexagons[row][col].getHexagon().getPoints().get(4) - 20));
                    double lineEndYCoordinate = lineStartYCoordinate;
                    Line barbedWireLine = new Line(lineStartXCoordinate, lineStartYCoordinate, lineEndXCoordinate, lineEndYCoordinate);
                    mainPane.getChildren().add(barbedWireLine);
                    barbedWireLine.setStroke(Color.web("#797b8c"));
                    barbedWireLine.setStrokeWidth(10);
                    Unit.sendTroopsToFront();
                    listObstacles[i].setLine(barbedWireLine);
                }
                case "Sand Bags" -> {
                    double lineStartXCoordinate = ((listHexagons[row][col].getHexagon().getPoints().get(0) + 8));
                    double lineStartYCoordinate = ((listHexagons[row][col].getHexagon().getPoints().get(1) - 15));
                    double lineEndXCoordinate = ((listHexagons[row][col].getHexagon().getPoints().get(2)));
                    double lineEndYCoordinate = ((listHexagons[row][col].getHexagon().getPoints().get(3) - 15));
                    Line firstSandLine = new Line(lineStartXCoordinate, lineStartYCoordinate, lineEndXCoordinate, lineEndYCoordinate);
                    lineStartXCoordinate = lineEndXCoordinate;
                    lineStartYCoordinate = lineEndYCoordinate;
                    lineEndXCoordinate = ((listHexagons[row][col].getHexagon().getPoints().get(4) - 8));
                    lineEndYCoordinate = ((listHexagons[row][col].getHexagon().getPoints().get(5) - 15));
                    Line secondSandLine = new Line(lineStartXCoordinate, lineStartYCoordinate, lineEndXCoordinate, lineEndYCoordinate);
                    mainPane.getChildren().add(firstSandLine);
                    firstSandLine.setStroke(Color.web("#FFFF00"));
                    firstSandLine.setStrokeWidth(5);
                    mainPane.getChildren().add(secondSandLine);
                    secondSandLine.setStroke(Color.web("#FFFF00"));
                    secondSandLine.setStrokeWidth(5);
                    Unit.sendTroopsToFront();
                    listObstacles[i].setLine(firstSandLine);
                    listObstacles[i].setSecondLine(secondSandLine);
                }
                case "Hedgehogs" ->{
                    double lineStartXCoordinate = ((listHexagons[row][col].getHexagon().getPoints().get(0)+20));
                    double lineStartYCoordinate = ((listHexagons[row][col].getHexagon().getPoints().get(1) - 15));
                    double lineEndXCoordinate = ((listHexagons[row][col].getHexagon().getPoints().get(4)-20));
                    double lineEndYCoordinate = ((listHexagons[row][col].getHexagon().getPoints().get(3) - 15));
                    Line firstHedgehogLine = new Line(lineStartXCoordinate, lineStartYCoordinate, lineEndXCoordinate, lineEndYCoordinate);
                    lineStartXCoordinate = ((listHexagons[row][col].getHexagon().getPoints().get(4)-20));
                    lineStartYCoordinate = ((listHexagons[row][col].getHexagon().getPoints().get(5) - 15));
                    lineEndXCoordinate = ((listHexagons[row][col].getHexagon().getPoints().get(0)+20));
                    lineEndYCoordinate = ((listHexagons[row][col].getHexagon().getPoints().get(3) - 15));
                    Line secondHedgehogLine = new Line(lineStartXCoordinate, lineStartYCoordinate, lineEndXCoordinate, lineEndYCoordinate);
                    mainPane.getChildren().add(firstHedgehogLine);
                    firstHedgehogLine.setStroke(Color.web("#797b8c"));
                    firstHedgehogLine.setStrokeWidth(5);
                    mainPane.getChildren().add(secondHedgehogLine);
                    secondHedgehogLine.setStroke(Color.web("#797b8c"));
                    secondHedgehogLine.setStrokeWidth(5);
                    Unit.sendTroopsToFront();
                    listObstacles[i].setLine(firstHedgehogLine);
                    listObstacles[i].setSecondLine(secondHedgehogLine);
                }
                case "Bunkers" ->{
                    double lineStartXCoordinate = listHexagons[row][col].getHexagon().getPoints().get(0) + 21;
                    double lineStartYCoordinate = (listHexagons[row][col].getHexagon().getPoints().get(1) +
                            listHexagons[row][col].getHexagon().getPoints().get(11))/2.0;
                    double lineEndXCoordinate = listHexagons[row][col].getHexagon().getPoints().get(6) - 21;
                    Line bunkerLine = new Line(lineStartXCoordinate, lineStartYCoordinate, lineEndXCoordinate, lineStartYCoordinate);
                    mainPane.getChildren().add(bunkerLine);
                    bunkerLine.setStroke(Color.web("#797b8c"));
                    bunkerLine.setStrokeWidth(42);
                    Unit.sendTroopsToFront();
                    listObstacles[i].setLine(bunkerLine);
                }
            }

        }
    }
    private void setUpCards() {
        card1 = new Card();
        cardLabel1 = new Label(card1.getCardText());
        cardLabel1.setTranslateX(950);
        cardLabel1.setTranslateY(50);
        setLabelProperties(cardLabel1);

        cardLabel1.setOnMouseClicked(mouseEvent -> {
            removeHighlighting();
            highlightPossibleUnitsToActivate(card1.getSectionArr(), true);
            lastClickedCard = card1;
            setUpUnitActivationsAndAttackingPhase();
            card1 = new Card();
            cardLabel1.setText(card1.getCardText());
            hideCards();
        });

        card2 = new Card();
        cardLabel2 = new Label(card2.getCardText());
        cardLabel2.setTranslateX(950);
        cardLabel2.setTranslateY(150);
        setLabelProperties(cardLabel2);

        cardLabel2.setOnMouseClicked(mouseEvent -> {
            removeHighlighting();
            highlightPossibleUnitsToActivate(card2.getSectionArr(), true);
            lastClickedCard = card2;
            setUpUnitActivationsAndAttackingPhase();
            card2 = new Card();
            cardLabel2.setText(card2.getCardText());
            hideCards();
        });

        card3 = new Card();
        cardLabel3 = new Label(card3.getCardText());
        cardLabel3.setTranslateX(950);
        cardLabel3.setTranslateY(250);
        setLabelProperties(cardLabel3);

        cardLabel3.setOnMouseClicked(mouseEvent -> {
            removeHighlighting();
            highlightPossibleUnitsToActivate(card3.getSectionArr(), true);
            lastClickedCard = card3;
            setUpUnitActivationsAndAttackingPhase();
            card3 = new Card();
            cardLabel3.setText(card3.getCardText());
            hideCards();
        });

        card4 = new Card();
        cardLabel4 = new Label(card4.getCardText());
        cardLabel4.setTranslateX(950);
        cardLabel4.setTranslateY(350);
        setLabelProperties(cardLabel4);

        cardLabel4.setOnMouseClicked(mouseEvent -> {
            removeHighlighting();
            highlightPossibleUnitsToActivate(card4.getSectionArr(), true);
            lastClickedCard = card4;
            setUpUnitActivationsAndAttackingPhase();
            card4 = new Card();
            cardLabel3.setText(card4.getCardText());
            hideCards();
        });

        card5 = new Card();
        cardLabel5 = new Label(card5.getCardText());
        cardLabel5.setTranslateX(950);
        cardLabel5.setTranslateY(450);
        setLabelProperties(cardLabel5);

        cardLabel5.setOnMouseClicked(mouseEvent -> {
            removeHighlighting();
            highlightPossibleUnitsToActivate(card5.getSectionArr(), true);
            lastClickedCard = card5;
            setUpUnitActivationsAndAttackingPhase();
            card5 = new Card();
            cardLabel5.setText(card5.getCardText());
            hideCards();
        });

        card6 = new Card();
        cardLabel6 = new Label(card6.getCardText());
        cardLabel6.setTranslateX(950);
        cardLabel6.setTranslateY(550);
        setLabelProperties(cardLabel6);

        cardLabel6.setOnMouseClicked(mouseEvent -> {
            removeHighlighting();
            highlightPossibleUnitsToActivate(card6.getSectionArr(), true);
            lastClickedCard = card6;
            setUpUnitActivationsAndAttackingPhase();
            card6 = new Card();
            cardLabel6.setText(card6.getCardText());
            hideCards();
        });
    }
    private void hideCards(){
        cardLabel1.setVisible(false);
        cardLabel2.setVisible(false);
        cardLabel3.setVisible(false);
        cardLabel4.setVisible(false);
        cardLabel5.setVisible(false);
        cardLabel6.setVisible(false);
    }
    private void showCards(){
        cardLabel1.setVisible(true);
        cardLabel1.setText(card1.getCardText());
        cardLabel2.setVisible(true);
        cardLabel2.setText(card2.getCardText());
        cardLabel3.setVisible(true);
        cardLabel3.setText(card3.getCardText());
        cardLabel4.setVisible(true);
        cardLabel4.setText(card4.getCardText());
        cardLabel5.setVisible(true);
        cardLabel5.setText(card5.getCardText());
        cardLabel6.setVisible(true);
        cardLabel6.setText(card6.getCardText());
    }
    private void setLabelProperties(Label cardLabel) {
        cardLabel.setBorder(Border.stroke(Color.BLACK));
        cardLabel.setStyle("-fx-text-fill: red;-fx-font: 18 georgia;");
        mainPane.getChildren().add(cardLabel);
    }
    private void handleUserClick(double clickedX, double clickedY) {
        if (isHandlingRetreat) {
            try {
                retreatUnit((int) clickedX, (int) clickedY);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (clickedAUnitToAttack) {
            try {
                handleAttack(clickedX, clickedY);
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        } else if (clickedAUnitWithWhichToAttack) {
            setUpRowColUnitIndex((int) clickedX, (int) clickedY);
            //check if the hexagon that we click is in the list of the moved / already clicked hexagons
            for (Hexagon alreadyMovedHexagon : alreadyMovedHexagons) {
                if (alreadyMovedHexagon.getHexagon().equals(listHexagons[rowOfClick][colOfClick].getHexagon())) {
                    listUnit[currentUnitIndex].highlightAttackPositions();
                    int countNumberOfPossiblePlacesToAttack = 0;
                    for (int r = 0; r < listHexagons.length; r++) {
                        for (int c = 0; c < listHexagons[r].length; c++) {
                            if (listHexagons[r][c].getHexagon().getStroke().equals(Color.RED)) {
                                countNumberOfPossiblePlacesToAttack++;
                            }
                        }
                    }
                    if (countNumberOfPossiblePlacesToAttack <= 0) {
                        alreadyMovedHexagons.remove(alreadyMovedHexagons.indexOf(listHexagons[rowOfClick][colOfClick]));
                        listHexagons[rowOfClick][colOfClick].getHexagon().setStroke(Color.BLACK);
                        listHexagons[rowOfClick][colOfClick].getHexagon().setStrokeWidth(2);
                        if (!isHandlingRetreat && !moreUnitsToMove()) {
                            handleSwitchingPlayers();

                        }
                    } else {
                        clickedAUnitToAttack = true;
                    }
                    break;
                }
            }
            lastClickedRow = rowOfClick;
            lastClickedCol = colOfClick;

        } else {
            setUpRowColUnitIndex((int) clickedX, (int) clickedY);
            if (!isClickingToMoveUnit && !isDoneMovingUnits) {
                if (currentUnitIndex != -1) {
                    for (int i = 0; i < lastClickedCard.getSectionArr().length; i++) {
                        if (listHexagons[rowOfClick][colOfClick].getSection().equals(lastClickedCard.getSectionArr()[i])) {
                            removeHighlighting();
                            listUnit[currentUnitIndex].showMovePositions(Color.YELLOW, false);
                            listHexagons[rowOfClick][colOfClick].calculateAndSetSection();
                            if (listHexagons[rowOfClick][colOfClick].getSection().equals("Left and Center") && remainingUnitsLeft > 0 && remainingUnitsCenter > 0) {
                                mainStage.setFullScreen(false);
                                TextInputDialog dialogWhereCount = new TextInputDialog("Please enter answer here");
                                dialogWhereCount.setHeaderText("You have selected a Unit that is on both the Left and the Center. Do you want this move to count towards the Left or Center ?");
                                String input = String.valueOf(dialogWhereCount.showAndWait());
                                input = input.substring(9, input.length() - 1);
                                if (input.equalsIgnoreCase("left")) {
                                    numberOfMovedUnitsLeft++;
                                } else if (input.equalsIgnoreCase("center")) {
                                    numberOfMovedUnitsCenter++;
                                }
                            } else if (listHexagons[rowOfClick][colOfClick].getSection().equals("Left and Center") && remainingUnitsLeft <= 0) {
                                numberOfMovedUnitsCenter++;
                            } else if (listHexagons[rowOfClick][colOfClick].getSection().equals("Left and Center") && remainingUnitsCenter <= 0) {
                                numberOfMovedUnitsLeft++;
                            } else if (listHexagons[rowOfClick][colOfClick].getSection().equals("Center and Right") && remainingUnitsCenter > 0 && remainingUnitsRight > 0) {
                                mainStage.setFullScreen(false);
                                TextInputDialog dialogWhereCount = new TextInputDialog("Please enter answer here");
                                dialogWhereCount.setHeaderText("You have selected a Unit that is on both the Center and the Right. Do you want this move to count towards the Center or Right ?");
                                String input = String.valueOf(dialogWhereCount.showAndWait());
                                input = input.substring(9, input.length() - 1);
                                if (input.equalsIgnoreCase("right")) {
                                    numberOfMovedUnitsRight++;
                                } else if (input.equalsIgnoreCase("center")) {
                                    numberOfMovedUnitsLeft++;
                                }
                            } else if (listHexagons[rowOfClick][colOfClick].getSection().equals("Center and Right") && remainingUnitsCenter <= 0) {
                                numberOfMovedUnitsRight++;
                            } else if (listHexagons[rowOfClick][colOfClick].getSection().equals("Center and Right") && remainingUnitsRight <= 0) {
                                numberOfMovedUnitsCenter++;
                            }
                            highlightHexagonsMoved();
                        }
                    }
                }
            } else if (isClickingToMoveUnit && !isDoneMovingUnits) {
                moveUnit(rowOfClick, colOfClick);
            }
            if (isDoneMovingUnits) {
                removeHighlighting();
                highlightHexagonsMoved();

                for (int i = 0; i < alreadyMovedHexagons.size(); i++) {
                    alreadyMovedHexagons.get(i).getHexagon().setStroke(Color.PURPLE);
                    alreadyMovedHexagons.get(i).getHexagon().setStrokeWidth(4);
                    alreadyMovedHexagons.get(i).getHexagon().toFront();

                    sendObstaclesToFront();

                    troopLabelList[alreadyMovedHexagons.get(i).getRow()][alreadyMovedHexagons.get(i).getCol()].toFront();
                    for (int r = 0; r < troopLabelList.length; r++) {
                        for (int c = 0; c < troopLabelList[r].length; c++) {
                            if (troopLabelList[r][c] != null) {
                                troopLabelList[r][c].toFront();
                            }
                        }
                    }
                }
                clickedAUnitWithWhichToAttack = true;
            }
        }
    }
    private void retreatUnit(double x, double y) throws IOException {
        setUpRowColUnitIndex((int) x, (int) y);

        //moves the Label to the current position on the board and updates it accordingly in troopLabelList
        swapPositionsOfLabels();
        int lastClickedUnitIndex = listHexagons[lastClickedRow][lastClickedCol].getIndexUnit();
        listUnit[lastClickedUnitIndex].setRow(rowOfClick);
        listUnit[lastClickedUnitIndex].setCol(colOfClick);

        if (getObstacle(rowOfClick, colOfClick) != null) {
            getObstacle(rowOfClick, colOfClick).getFirstLine().toFront();
            if (getObstacle(rowOfClick, colOfClick).getSecondLine() != null) {
                getObstacle(rowOfClick, colOfClick).getSecondLine().toFront();
            }
        }
        removeSandbagsAfterMoving();
        troopLabelList[rowOfClick][colOfClick].toFront();
        removeHighlighting();

        highlightHexagonsMoved();
        handleRetreatRemainingRetreats--;

        if (handleRetreatRemainingRetreats > 0) {
            isHandlingRetreat = true;
            handleRetreat();
        } else {
            isHandlingRetreat = false;
            handleRetreatRemainingRetreats = 0;
            if (!moreUnitsToMove()) {
                handleSwitchingPlayers();
            }
        }

    }
    private void setUpRowColUnitIndex(int mouseX, int mouseY) {
        rowOfClick = mouseY - 29;
        //Initial condition checks that the click is reasonable
        if (rowOfClick >= 0 && rowOfClick <= 546) {
            for (int row = 0; row < 9; row++) {
                if (rowOfClick > 63 * row && rowOfClick < 42 + (63 * row)) {
                    rowOfClick /= 63;
                    if (rowOfClick % 2 == 0) {
                        colOfClick = mouseX - 15;
                        if (colOfClick < 0 || colOfClick > 945) {
                            System.out.println("Wrong Click");
                        } else {
                            colOfClick /= 70;
                            currentUnitIndex = listHexagons[rowOfClick][colOfClick].getIndexUnit();
                            if (currentUnitIndex == -1) {
                                if (!isClickingToMoveUnit) {
                                    System.out.println("You clicked something that is not a unit");
                                }
                            }
                        }
                    } else {
                        colOfClick = mouseX - 50;
                        if (colOfClick < 0 || colOfClick > 875) {
                            System.out.println("Wrong Click");
                        } else {
                            colOfClick /= 70;
                            currentUnitIndex = listHexagons[rowOfClick][colOfClick].getIndexUnit();
                            if (currentUnitIndex == -1) {
                                if (!isClickingToMoveUnit) {
                                    System.out.println("You clicked something that is not a unit");
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    public void highlightPossibleUnitsToActivate(String[] sections, boolean weAreStarting){
        for (int r = 0; r < listHexagons.length;r++) {
            for (int c = 0; c < listHexagons[r].length;c++) {
                boolean found = false;
                for (Hexagon hexagon : alreadyMovedHexagons) {
                    if (hexagon.equals(listHexagons[r][c])) {
                        found = true;
                        break;
                    }
                }
                for (Hexagon hexagon : hexagonsMovedTooFar) {
                    if (hexagon.equals(listHexagons[r][c])) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    if (listHexagons[r][c].hasTroops()) {
                        int indexUnit = listHexagons[r][c].getIndexUnit();
                        if(indexUnit != -1){
                            boolean isAllied = listUnit[indexUnit].getIsAllied();
                            if (isPlayer1Turn == isAllied) {
                                for (int i = 0; i < sections.length;i++) {
                                    if(sections[i] != null) {
                                        if (listHexagons[r][c].getSection().equals(sections[i]) && hasUnitsLeftToMove(sections[i], weAreStarting)) {
                                            listHexagons[r][c].getHexagon().setStroke(Color.YELLOW);
                                            listHexagons[r][c].getHexagon().toFront();
                                            sendObstaclesToFront();
                                            Unit.sendTroopsToFront();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        highlightHexagonsMoved();
    }
    private int numberOfPossibleActivationsSection(String section, boolean isAllied){
        int numberOfPossibleActivations = 0;
        for(int r = 0; r < listHexagons.length;r++){
            for(int c = 0; c< listHexagons[r].length;c++){
                if (listHexagons[r][c].hasTroops() && listHexagons[r][c].getSection().equals(section)){
                    if(listHexagons[r][c].getUnit() != null && listHexagons[r][c].getUnit().getIsAllied() == isAllied){
                        numberOfPossibleActivations++;
                    }
                }
            }
        }
        return numberOfPossibleActivations;
    }
    private void setUpUnitActivationsAndAttackingPhase(){
        if(numberOfPossibleUnitActivationsLeft < lastClickedCard.getNumberOfMovableUnitsLeft()) {
            remainingUnitsLeft = numberOfPossibleUnitActivationsLeft - numberOfMovedUnitsLeft;
        }
        else{
            remainingUnitsLeft = lastClickedCard.getNumberOfMovableUnitsLeft()-numberOfMovedUnitsLeft;
        }
        if(numberOfPossibleUnitActivationsCenter < lastClickedCard.getNumberOfMovableUnitsCenter()) {
            remainingUnitsCenter = numberOfPossibleUnitActivationsCenter - numberOfMovedUnitsCenter;
        }
        else{
            remainingUnitsCenter = lastClickedCard.getNumberOfMovableUnitsCenter()-numberOfMovedUnitsCenter;
        }
        if(numberOfPossibleUnitActivationsRight < lastClickedCard.getNumberOfMovableUnitsRight()) {

            remainingUnitsRight = numberOfPossibleUnitActivationsRight - numberOfMovedUnitsRight;
        }
        else{
            remainingUnitsRight = lastClickedCard.getNumberOfMovableUnitsRight()-numberOfMovedUnitsRight;
        }
        if(remainingUnitsLeft <= 0){
            if(remainingUnitsCenter <= 0){
                if(remainingUnitsRight <= 0){
                    isDoneMovingUnits = true;
                }
            }
        }
    }
    public boolean hasUnitsLeftToMove(String section, boolean weAreStarting){
        if(weAreStarting){
            return true;
        }
        if(section.equals("Left") && remainingUnitsLeft > 0){
            return true;
        }
        if(section.equals("Center") && remainingUnitsCenter > 0){
            return true;
        }
        if(section.equals("Right") && remainingUnitsRight > 0){
            return true;
        }
        if(section.equals("Left and Center") && (remainingUnitsLeft > 0 || remainingUnitsCenter >0)){
            return true;
        }
        if(section.equals("Center and Right") && (remainingUnitsCenter > 0 || remainingUnitsRight >0)){
            return true;
        }
        return false;
    }





    private void handleAttack(double x, double y) throws IOException, InterruptedException {
        setUpRowColUnitIndex((int) x, (int) y);

        listHexagons[lastClickedRow][lastClickedCol].getHexagon().setStroke(Color.BLACK);
        listHexagons[lastClickedRow][lastClickedCol].getHexagon().setStrokeWidth(2);
        listHexagons[lastClickedRow][lastClickedCol].getHexagon().toFront();
        sendObstaclesToFront();
        Unit.sendTroopsToFront();

        int indexOfPurpleClicked = alreadyMovedHexagons.indexOf(listHexagons[lastClickedRow][lastClickedCol]);
        alreadyMovedHexagons.remove(indexOfPurpleClicked);

        boolean clickedShootableUnit = false;
        //check if the hexagon that we click is highlighted in red meaning that it can get shot
        for (int r = 0; r < listHexagons.length; r++) {
            for (int c = 0; c < listHexagons[r].length; c++) {
                listHexagons[r][c].updateTroops();
                //if troop is equal to red or in grey (a position where we can shoot but no units were present at the start)
                if ((listHexagons[r][c].getHexagon().getStroke().equals(Color.RED) ||
                        (listHexagons[r][c].getHexagon().getStroke().equals(Color.GREY) &&
                                listHexagons[r][c].hasTroops()
                                && listHexagons[r][c].returnIsAllied() != listHexagons[lastClickedRow][lastClickedCol].returnIsAllied()))
                        && r == rowOfClick && c == colOfClick) {
                    clickedShootableUnit = true;
                }
            }
        }
        if (clickedShootableUnit) {
            rollDice();
        }
    }
    public Image randomImage() throws FileNotFoundException {
        armoredImage = new Image(new FileInputStream("/Users/florianchevalier/IdeaProjects/TestingJavaFXIA/src/armoredImage.png"));
        grenadeImage = new Image(new FileInputStream("/Users/florianchevalier/IdeaProjects/TestingJavaFXIA/src/grenadeImage.png"));
        infantryImage = new Image(new FileInputStream("/Users/florianchevalier/IdeaProjects/TestingJavaFXIA/src/infantryImage.png"));
        retreatImage = new Image(new FileInputStream("/Users/florianchevalier/IdeaProjects/TestingJavaFXIA/src/retreatImage.png"));
        starImage = new Image(new FileInputStream("/Users/florianchevalier/IdeaProjects/TestingJavaFXIA/src/starImage.png"));
        int randomIndexImage = (int) (Math.random() * 6);
        switch (randomIndexImage) {
            case 0 -> {
                return armoredImage;
            }
            case 1 -> {
                return grenadeImage;
            }
            case 2, 3 -> {
                return infantryImage;
            }
            case 4 -> {
                return starImage;
            }
            case 5 -> {
                return retreatImage;
            }
        }
        return null;
    }
    private void addDiceOnBoard(int numberOfDiceToShoot) throws IOException {
        if (numberOfDiceToShoot == 1) {
            addFirstDice();
            secondDice.setVisible(false);
            thirdDice.setVisible(false);

        } else if (numberOfDiceToShoot == 2) {
            addFirstDice();
            addSecondDice();
            thirdDice.setVisible(false);
        } else if (numberOfDiceToShoot == 3) {
            addFirstDice();
            addSecondDice();
            addThirdDice();
        }
    }
    private void addFirstDice() throws IOException {
        firstDice.setVisible(true);
        firstDice.setImage(randomImage());
        firstDice.toFront();
        firstDice.setTranslateX(50);
        firstDice.setTranslateY(600);
        handleKillingTroops(firstDice);
    }
    private void addSecondDice() throws IOException {
        secondDice.setVisible(true);
        secondDice.setImage(randomImage());
        secondDice.toFront();
        secondDice.setTranslateX(250);
        secondDice.setTranslateY(600);
        handleKillingTroops(secondDice);
    }
    private void addThirdDice() throws IOException {
        thirdDice.setVisible(true);
        thirdDice.setImage(randomImage());
        thirdDice.toFront();
        thirdDice.setTranslateX(450);
        thirdDice.setTranslateY(600);
        handleKillingTroops(thirdDice);
    }
    private void handleKillingTroops(ImageView dice) {
        if (dice.getImage().equals(grenadeImage)) {
            if (troopLabelList[rowOfClick][colOfClick] != null) {
                handleGrenade();
            }
        } else if (dice.getImage().equals(armoredImage)) {

            if (troopLabelList[rowOfClick][colOfClick] != null) {
                handleArmoredImage();
            }
        } else if (dice.getImage().equals(infantryImage)) {
            if (troopLabelList[rowOfClick][colOfClick] != null) {
                handleInfantryImage();
            }
        } else if (dice.getImage().equals(starImage)) {
        } else if (dice.getImage().equals(retreatImage)) {
            if (troopLabelList[rowOfClick][colOfClick] != null && listHexagons[rowOfClick][colOfClick].getUnit() != null) {
                handleRetreat();
                handleRetreatRemainingRetreats++;
            }
        }
    }
    private void handleInfantryImage() {
        if (listHexagons[rowOfClick][colOfClick].getUnit() != null
                && listHexagons[rowOfClick][colOfClick].getUnit().getUnitType().equals("Infantry")) {
            killOneTroop(rowOfClick, colOfClick);
        }
    }
    private void handleGrenade() {
        if(listHexagons[rowOfClick][colOfClick].getUnit() != null) {
            killOneTroop(rowOfClick, colOfClick);
        }
    }
    private void handleArmoredImage() {
        if (listHexagons[rowOfClick][colOfClick].getUnit() != null
                && listHexagons[rowOfClick][colOfClick].getUnit().getUnitType().equals("Armored vehicles")) {
            killOneTroop(rowOfClick, colOfClick);
        }
    }
    private void handleRetreat() {
        boolean canRetreatSomewhere = false;
        if (isPlayer1Turn) {
            if (rowOfClick % 2 == 0) {
                if (rowOfClick - 1 >= 0 && colOfClick - 1 >= 0 && colOfClick - 1 <= 11 && listHexagons[rowOfClick - 1][colOfClick - 1].getCanPositionOnIt()) {
                    handleHighlightingRetreatPositions(rowOfClick - 1, colOfClick - 1);
                    canRetreatSomewhere = true;
                }
                if (rowOfClick - 1 >= 0 && colOfClick >= 0 && colOfClick <= 11 && listHexagons[rowOfClick - 1][colOfClick].getCanPositionOnIt()) {
                    handleHighlightingRetreatPositions(rowOfClick - 1, colOfClick);
                    canRetreatSomewhere = true;
                }
            } else if (rowOfClick % 2 == 1) {
                if (rowOfClick - 1 >= 0 && colOfClick + 1 >= 0 && colOfClick + 1 <= 12 && listHexagons[rowOfClick - 1][colOfClick + 1].getCanPositionOnIt()) {
                    handleHighlightingRetreatPositions(rowOfClick - 1, colOfClick + 1);
                    canRetreatSomewhere = true;
                }
                if (rowOfClick - 1 >= 0 && colOfClick >= 0 && colOfClick <= 12 && listHexagons[rowOfClick - 1][colOfClick].getCanPositionOnIt()) {
                    handleHighlightingRetreatPositions(rowOfClick - 1, colOfClick);
                    canRetreatSomewhere = true;
                }
            }
        } else {
            if (rowOfClick % 2 == 0) {
                if (rowOfClick + 1 <= 8 && colOfClick - 1 >= 0 && colOfClick - 1 <= 11 && listHexagons[rowOfClick + 1][colOfClick - 1].getCanPositionOnIt()) {
                    handleHighlightingRetreatPositions(rowOfClick + 1, colOfClick - 1);
                    canRetreatSomewhere = true;
                }
                if (rowOfClick + 1 <= 8 && colOfClick >= 0 && colOfClick <= 11 && listHexagons[rowOfClick + 1][colOfClick].getCanPositionOnIt()) {
                    handleHighlightingRetreatPositions(rowOfClick + 1, colOfClick);
                    canRetreatSomewhere = true;
                }
            } else if (rowOfClick % 2 == 1) {
                if (rowOfClick + 1 <= 8 && colOfClick + 1 <= 12 && colOfClick + 1 >= 0 && listHexagons[rowOfClick + 1][colOfClick + 1].getCanPositionOnIt()) {
                    handleHighlightingRetreatPositions(rowOfClick + 1, colOfClick + 1);
                    canRetreatSomewhere = true;
                }
                if (rowOfClick + 1 <= 8 && colOfClick >= 0 && colOfClick <= 12 && listHexagons[rowOfClick + 1][colOfClick].getCanPositionOnIt()) {
                    handleHighlightingRetreatPositions(rowOfClick + 1, colOfClick);
                    canRetreatSomewhere = true;
                }
            }
        }
        if (!canRetreatSomewhere) {
            isHandlingRetreat = false;
            killOneTroop(rowOfClick, colOfClick);
            handleRetreatRemainingRetreats--;
            if (handleRetreatRemainingRetreats > 0) {
                for (int i = 0; i < handleRetreatRemainingRetreats; i++) {
                    killOneTroop(rowOfClick, colOfClick);
                    handleRetreatRemainingRetreats--;
                }
            }
            if (handleRetreatRemainingRetreats <= 0) {
                handleRetreatRemainingRetreats = 0;
                if (!moreUnitsToMove()) {
                    handleSwitchingPlayers();
                }
            }
            if (troopLabelList[rowOfClick][colOfClick] == null) {
                removeHighlighting();
                isHandlingRetreat = false;
                handleRetreatRemainingRetreats = 0;
            }
        }
        lastClickedRow = rowOfClick;
        lastClickedCol = colOfClick;
    }
    private void handleHighlightingRetreatPositions(int rowToHighlight, int colToHighlight) {
        isHandlingRetreat = true;
        listHexagons[rowToHighlight][colToHighlight].getHexagon().setStroke(Color.YELLOW);
        listHexagons[rowToHighlight][colToHighlight].getHexagon().toFront();
        if (getObstacle(rowToHighlight, colToHighlight) != null) {
            getObstacle(rowToHighlight, colToHighlight).getFirstLine().toFront();
        }
        troopLabelList[rowToHighlight][colToHighlight].toFront();
    }

    private void killOneTroop(int passedRowOfClick, int passedColOfClick) {
        if (troopLabelList[passedRowOfClick][passedColOfClick] != null) {
            if (troopLabelList[passedRowOfClick][passedColOfClick].getText().contains("I")) {
                readjustLabel("I", passedRowOfClick, passedColOfClick);
                if (!troopLabelList[passedRowOfClick][passedColOfClick].getText().contains("I")) {
                    if (listHexagons[passedRowOfClick][passedColOfClick].getIndexUnit() != -1) {
                        listUnit[listHexagons[passedRowOfClick][passedColOfClick].getIndexUnit()] = null;
                    }
                    removeTroopAndUpdateKillCounter(passedRowOfClick, passedColOfClick);
                }
            } else if (troopLabelList[passedRowOfClick][passedColOfClick].getText().contains("A")) {
                readjustLabel("A", passedRowOfClick, passedColOfClick);
                if (!troopLabelList[passedRowOfClick][passedColOfClick].getText().contains("A")) {
                    if (listHexagons[passedRowOfClick][passedColOfClick].getIndexUnit() != -1) {
                        listUnit[listHexagons[passedRowOfClick][passedColOfClick].getIndexUnit()] = null;
                    }
                    removeTroopAndUpdateKillCounter(passedRowOfClick, passedColOfClick);
                }
            } else if (troopLabelList[passedRowOfClick][passedColOfClick].getText().contains("T")) {
                readjustLabel("T", passedRowOfClick, passedColOfClick);
                if (!troopLabelList[passedRowOfClick][passedRowOfClick].getText().contains("T")) {
                    if (listHexagons[passedRowOfClick][passedColOfClick].getIndexUnit() != -1) {
                        listUnit[listHexagons[passedRowOfClick][passedColOfClick].getIndexUnit()] = null;
                    }
                    removeTroopAndUpdateKillCounter(passedRowOfClick, passedColOfClick);
                }
            }
        }
    }
    private void removeTroopAndUpdateKillCounter(int passedRowOfClick, int passedColOfClick) {
        removeHighlightingForCaseWhenKilledWithRetreat();
        int currentX = (int) troopLabelList[passedRowOfClick][passedColOfClick].getTranslateX();
        int currentY = (int) troopLabelList[passedRowOfClick][passedColOfClick].getTranslateY();

        troopLabelList[passedRowOfClick][passedColOfClick] = new Label("");
        troopLabelList[passedRowOfClick][passedColOfClick].setTranslateX(currentX);
        troopLabelList[passedRowOfClick][passedColOfClick].setTranslateY(currentY);

        if (isPlayer1Turn) {
            alliedScore++;
            firstScoreLabel.setText(firstScoreLabel.getText().substring(0, firstScoreLabel.getText().length() - 1) + alliedScore);
            if (alliedScore == thresholdAmountOfKills) {
                switchToWinningScreen(true);
            }
        } else {
            axisScore++;
            if (axisScore == thresholdAmountOfKills) {
                switchToWinningScreen(false);
            }
            secondScoreLabel.setText(secondScoreLabel.getText().substring(0, secondScoreLabel.getText().length() - 1) + axisScore);
        }
    }
    private void moveUnit(int rowOfClick, int colOfClick){
        if (rowOfClick != -1 && !(colOfClick < 0 || colOfClick > 12)) {
            removeHighlighting();
            isClickingToMoveUnit = !isClickingToMoveUnit;
            lastClickedUnitIndex = listHexagons[lastClickedRow][lastClickedCol].getIndexUnit();
            boolean selectedMovableHexa = false;

            for (int i = 0; i < listUnit[lastClickedUnitIndex].getPossibleMoveLocations().size(); i++) {
                if (listUnit[lastClickedUnitIndex].getPossibleMoveLocations().get(i).
                        equals(listHexagons[rowOfClick][colOfClick].getHexagon())) {
                    selectedMovableHexa = true;
                }
            }
            if (selectedMovableHexa) {
                swapPositionsOfLabels();
                troopLabelList[rowOfClick][colOfClick].toFront();
                listUnit[lastClickedUnitIndex].setRow(rowOfClick);
                listUnit[lastClickedUnitIndex].setCol(colOfClick);


                String section = listHexagons[lastClickedRow][lastClickedCol].getSection();

                switch (section) {
                    case "Left" -> numberOfMovedUnitsLeft++;
                    case "Center" -> numberOfMovedUnitsCenter++;
                    case "Right" -> numberOfMovedUnitsRight++;
                }

                setUpUnitActivationsAndAttackingPhase();

                if((listHexagons[rowOfClick][colOfClick].getDistanceToMoveToCurrentPosition() <=  listUnit[lastClickedUnitIndex].getMoveShootDistance() &&
                        !listHexagons[rowOfClick][colOfClick].getMustStopOnIt())
                        || listHexagons[rowOfClick][colOfClick].getDistanceToMoveToCurrentPosition() == 0){
                    alreadyMovedHexagons.add(listHexagons[rowOfClick][colOfClick]);
                    listHexagons[rowOfClick][colOfClick].getHexagon().setStroke(Color.PURPLE);
                }
                else{
                    hexagonsMovedTooFar.add(listHexagons[rowOfClick][colOfClick]);
                    listHexagons[rowOfClick][colOfClick].getHexagon().setStroke(Color.ORANGE);
                }

                listHexagons[rowOfClick][colOfClick].getHexagon().setStrokeWidth(4);
                listHexagons[rowOfClick][colOfClick].getHexagon().toFront();
                troopLabelList[rowOfClick][colOfClick].toFront();
                resetDistancesToMoveToCurrentLocations();

                highlightPossibleUnitsToActivate(lastClickedCard.getSectionArr(), false);
                removeSandbagsAfterMoving();

                lastClickedRow = rowOfClick;
                lastClickedCol = colOfClick;

                if(noMoreHighlightedYellowUnits() && noMoreHighlightedPurpleUnits()){
                    handleSwitchingPlayers();
                }
            }
        }
    }
    private void resetDistancesToMoveToCurrentLocations(){
        for(int r = 0 ; r < listHexagons.length;r++){
            for (int c = 0; c < listHexagons[r].length;c++){
                listHexagons[r][c].setDistanceToMoveToCurrentLocation(0);
            }
        }
    }
    private void highlightHexagonsMoved() {
        for (Hexagon hex : alreadyMovedHexagons) {
            hex.getHexagon().setStroke(Color.PURPLE);
            hex.getHexagon().setStrokeWidth(4);
            hex.getHexagon().toFront();
            troopLabelList[hex.getRow()][hex.getCol()].toFront();
            sendObstaclesToFront();
            Unit.sendTroopsToFront();
        }
        for (Hexagon hex : hexagonsMovedTooFar) {
            hex.getHexagon().setStroke(Color.ORANGE);
            hex.getHexagon().setStrokeWidth(4);
            hex.getHexagon().toFront();
            troopLabelList[hex.getRow()][hex.getCol()].toFront();
            sendObstaclesToFront();
            Unit.sendTroopsToFront();
        }
    }
    private void removeSandbagsAfterMoving() {
        if (getObstacle(lastClickedRow, lastClickedCol) != null && getObstacle(lastClickedRow, lastClickedCol).getNameOfObstacle().equals("Sand Bags")) {
            for (int i = 0; i < listObstacles.length; i++) {
                if (listObstacles[i].getRow() == lastClickedRow && listObstacles[i].getCol() == lastClickedCol) {
                    listObstacles[i].getFirstLine().setVisible(false);
                    listObstacles[i].getSecondLine().setVisible(false);
                    listObstacles[i] = null;
                }
            }
        }
    }
    private void swapPositionsOfLabels(){


        //swap the x values
        double tempX = troopLabelList[lastClickedRow][lastClickedCol].getTranslateX();
        troopLabelList[lastClickedRow][lastClickedCol].setTranslateX(
                troopLabelList[rowOfClick][colOfClick].getTranslateX());
        troopLabelList[rowOfClick][colOfClick].setTranslateX(tempX);

        //swap the y values
        double tempY = troopLabelList[lastClickedRow][lastClickedCol].getTranslateY();
        troopLabelList[lastClickedRow][lastClickedCol].setTranslateY(
                troopLabelList[rowOfClick][colOfClick].getTranslateY());
        troopLabelList[rowOfClick][colOfClick].setTranslateY(tempY);

        //swap their position in the array so that it is updated accordingly
        Label tempLabel = troopLabelList[lastClickedRow][lastClickedCol];
        troopLabelList[lastClickedRow][lastClickedCol] = troopLabelList[rowOfClick][colOfClick];
        troopLabelList[rowOfClick][colOfClick] = tempLabel;
    }
    private void rollDice() throws IOException {
        handleRetreatRemainingRetreats = 0;
        int numberOfDiceToShoot = calculateNumberOfDice();
        addDiceOnBoard(numberOfDiceToShoot);
        clickedAUnitToAttack = false;
        clickedAUnitWithWhichToAttack = true;
        isClickingToMoveUnit = true;
        removeRedHighlighting();

        if (!isHandlingRetreat && !moreUnitsToMove()) {
            handleSwitchingPlayers();
        }

    }
    private void handleSwitchingPlayers() {
        isPlayer1Turn = !isPlayer1Turn;

        if (isPlayer1Turn) {
            playerLabel.setText("Turn : \n Allied Player");
        } else {
            playerLabel.setText("Turn : \n Axis Player");
        }

        removeHighlighting();

        numberOfMovedUnitsLeft = 0;
        numberOfMovedUnitsRight = 0;
        numberOfMovedUnitsCenter = 0;

        alreadyMovedHexagons = new ArrayList<>();
        hexagonsMovedTooFar = new ArrayList<>();

        numberOfPossibleUnitActivationsLeft = numberOfPossibleActivationsSection("Left", isPlayer1Turn) +
                numberOfPossibleActivationsSection("Left and Center", isPlayer1Turn);
        numberOfPossibleUnitActivationsRight = numberOfPossibleActivationsSection("Right", isPlayer1Turn) +
                numberOfPossibleActivationsSection("Center and Right", isPlayer1Turn);
        numberOfPossibleUnitActivationsCenter = numberOfPossibleActivationsSection("Center", isPlayer1Turn) +
                numberOfPossibleActivationsSection("Center and Right", isPlayer1Turn) +
                numberOfPossibleActivationsSection("Left and Center", isPlayer1Turn);

        showCards();
        clickedAUnitToAttack = false;
        clickedAUnitWithWhichToAttack = false;
        isClickingToMoveUnit = false;
        isDoneMovingUnits = false;
    }
    private boolean moreUnitsToMove() {
        for (int r = 0; r < listHexagons.length; r++) {
            for (int c = 0; c < listHexagons[r].length; c++) {
                if (listHexagons[r][c].getHexagon().getStroke().equals(Color.PURPLE)) {
                    return true;
                }
            }
        }
        return false;
    }
    private void removeRedHighlighting() {
        for (int r = 0; r < listHexagons.length; r++) {
            for (int c = 0; c < listHexagons[r].length; c++) {
                if (listHexagons[r][c].getHexagon().getStroke() == Color.RED) {
                    listHexagons[r][c].getHexagon().setStrokeWidth(2);
                    listHexagons[r][c].getHexagon().setStroke(Color.BLACK);

                }
            }
        }
    }
    private int calculateNumberOfDice() {
        lastClickedUnitIndex = listHexagons[lastClickedRow][lastClickedCol].getIndexUnit();
        int distance = listHexagons[rowOfClick][colOfClick].getDistanceToMoveToCurrentPosition();
        int numDiceBasedOnDistance = listUnit[lastClickedUnitIndex].getNumberOfDiceArr().get(distance - 1);
        if (listUnit[lastClickedUnitIndex].getUnitType().equals("Infantry") && listHexagons[rowOfClick][colOfClick].getDefenseBonusAgainstInfantry() != 0) {
            numDiceBasedOnDistance -= listHexagons[rowOfClick][colOfClick].getDefenseBonusAgainstInfantry();
        } else if (listUnit[lastClickedUnitIndex].getUnitType().equals("Armored vehicles") && listHexagons[rowOfClick][colOfClick].getDefenseBonusAgainstTanks() != 0) {
            numDiceBasedOnDistance -= listHexagons[rowOfClick][colOfClick].getDefenseBonusAgainstTanks();
        }
        resetDistancesToMoveToCurrentLocations();
        if ((getObstacle(rowOfClick, colOfClick) != null)) {
            int removedDicedFromTerrain = Objects.requireNonNull(getObstacle(rowOfClick, colOfClick)).
                    getShootProtection(listUnit[lastClickedUnitIndex].getUnitType());
            return numDiceBasedOnDistance - removedDicedFromTerrain;
        }
        return numDiceBasedOnDistance;
    }
    private void readjustLabel(String definingString, int passedRowOfClick, int passedColOfClick) {
        StringBuilder labelString = new StringBuilder(troopLabelList[passedRowOfClick][passedColOfClick].getText());
        int count = 0;
        while (labelString.toString().contains(definingString)) {
            count++;
            labelString = new StringBuilder(labelString.substring(labelString.indexOf(definingString) + 1));
        }
        labelString = new StringBuilder();
        for (int i = 0; i < count - 1; i++) {
            labelString.append(definingString + " ");
        }

        troopLabelList[passedRowOfClick][passedColOfClick].setText(labelString.toString());
    }
    private void removeHighlightingForCaseWhenKilledWithRetreat() {
        for (int r = 0; r < listHexagons.length; r++) {
            for (int c = 0; c < listHexagons[r].length; c++) {
                if (listHexagons[r][c].getHexagon().getStroke().equals(Color.YELLOW)) {
                    listHexagons[r][c].getHexagon().setStrokeWidth(2);
                    listHexagons[r][c].getHexagon().setStroke(Color.BLACK);
                    listHexagons[r][c].getHexagon().toFront();
                    sendObstaclesToFront();
                    Unit.sendTroopsToFront();
                    isHandlingRetreat = false;
                }
            }
        }
    }



    private void switchToWinningScreen(boolean isWinningPlayer1) {
        Stage winningStage = new Stage();
        AnchorPane winnerPane = new AnchorPane();
        Scene scene = new Scene(winnerPane, 1200, 1700);
        winningStage.setScene(scene);

        Label winnerLabel;
        if (isWinningPlayer1) {
            winnerLabel = new Label("Congratulations, the Allied Player Won!");
            winnerLabel.setStyle("-fx-text-fill: green;-fx-font: 35 georgia;");

            winnerLabel.setTranslateX(300);
            winnerLabel.setTranslateY(100);
        } else {
            winnerLabel = new Label("Congratulations, the Axis Player Won!");
            winnerLabel.setStyle("-fx-text-fill: green;-fx-font: 50 georgia;");

            winnerLabel.setTranslateX(300);
            winnerLabel.setTranslateY(100);
        }
        Button exitProgramButton = new Button("Click Here to exit the program");
        exitProgramButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.exit(0);
            }
        });
        exitProgramButton.setStyle("-fx-text-fill: green;-fx-font: 20 georgia;");
        exitProgramButton.setTranslateX(475);
        exitProgramButton.setTranslateY(500);

        winnerPane.getChildren().add(exitProgramButton);
        winnerPane.getChildren().add(winnerLabel);
        winningStage.show();
    }


    private Obstacle getObstacle(int passedRow, int passedCol) {
        for (int i = 0; i < listObstacles.length; i++) {
            if (listObstacles[i] != null) {
                if (listObstacles[i].getRow() == passedRow && listObstacles[i].getCol() == passedCol) {
                    return listObstacles[i];
                }
            }
        }
        return null;
    }
    private boolean noMoreHighlightedYellowUnits(){
        for (int r = 0; r < listHexagons.length;r++){
            for(int c = 0; c < listHexagons[r].length;c++){
                if(listHexagons[r][c].getHexagon().getStroke().equals(Color.YELLOW)){
                    return false;
                }
            }
        }
        return true;
    }
    private boolean noMoreHighlightedPurpleUnits(){
        for (int r = 0; r < listHexagons.length;r++){
            for(int c = 0; c < listHexagons[r].length;c++){
                if(listHexagons[r][c].getHexagon().getStroke().equals(Color.PURPLE)){
                    return false;
                }
            }
        }
        return true;
    }
    public void removeHighlighting(){
        for(int r = 0; r < listHexagons.length;r++){
            for (int c = 0; c< listHexagons[r].length;c++){
                listHexagons[r][c].getHexagon().setStroke(Color.BLACK);
                listHexagons[r][c].getHexagon().setStrokeWidth(2);
            }
        }
    }





    private static Obstacle[] getObstacles(int scenarioNumber){
        if(scenarioNumber == 1) {
            return new Obstacle[]{
                    new Obstacle("Bridges", 2, 3),
                    new Obstacle("Barbed wire", 2, 5),
                    new Obstacle("Bridges", 5, 1),
                    new Obstacle("Sand Bags", 3, 3),
                    new Obstacle("Barbed wire", 3, 4),
                    new Obstacle("Barbed wire", 4, 3),
                    new Obstacle("Barbed wire", 4, 4),
                    new Obstacle("Bridges", 5, 10),
                    new Obstacle("Bunkers", 2, 4),
            };
        }
        else if(scenarioNumber == 2){
            return new Obstacle[]{
                    new Obstacle("Sand Bags", 5, 1),
            };
        }
        else if (scenarioNumber ==3){
            return new Obstacle[]{
            };
        }
        else{
            return null;
        }
    }
    private static Unit[] getUnits(int scenarioNumber) {
        if(scenarioNumber == 1) {
            return new Unit[]{
                    new Unit("Infantry", 0, 0, false),
                    new Unit("Infantry", 1, 10, false),
                    new Unit("Infantry", 2, 2, false),
                    new Unit("Infantry", 2, 4, false),
                    new Unit("Infantry", 3, 3, false),
                    new Unit("Infantry", 5, 2, true),
                    new Unit("Infantry", 6, 2, true),
                    new Unit("Infantry", 6, 3, true),
                    new Unit("Infantry", 6, 12, false),
                    new Unit("Infantry", 7, 4, true),
                    new Unit("Infantry", 7, 5, true),
                    new Unit("Infantry", 7, 7, true),
                    new Unit("Infantry", 8, 5, true),
                    new Unit("Infantry", 8, 7, true),
                    new Unit("Infantry", 8, 8, true)
            };
        }
        else if(scenarioNumber == 2){
            return new Unit[]{
                    new Unit("Infantry", 0, 0, false),
                    new Unit("Infantry", 0, 1, false),
                    new Unit("Armored vehicles", 0, 11, false),
                    new Unit("Infantry", 0, 12, false),
                    new Unit("Infantry", 1, 0, false),
                    new Unit("Infantry", 1, 10, false),
                    new Unit("Infantry", 1, 11, false),
                    new Unit("Infantry", 2, 0, false),
                    new Unit("Infantry", 2, 10, false),
                    new Unit("Infantry", 4, 6, false),

                    new Unit("Infantry", 5, 1, true),
                    new Unit("Infantry", 5, 3, true),
                    new Unit("Infantry", 6, 6, true),
                    new Unit("Infantry", 7, 7, true),
                    new Unit("Infantry", 7, 10, true),
                    new Unit("Infantry", 8, 4, true),
                    new Unit("Infantry", 8, 7, true),
            };
        }
        else if (scenarioNumber ==3){
            return new Unit[]{
                    new Unit("Armored vehicles", 0, 0, false),
                    new Unit("Infantry", 0, 7, false),
                    new Unit("Infantry", 0, 8, false),
                    new Unit("Armored vehicles", 1, 1, false),
                    new Unit("Infantry", 1, 2, false),
                    new Unit("Infantry", 1, 11, false),
                    new Unit("Infantry", 2, 9, false),
                    new Unit("Infantry", 3, 5, false),


                    new Unit("Infantry", 5, 1, true),
                    new Unit("Infantry", 5, 8, true),
                    new Unit("Infantry", 6, 3, true),
                    new Unit("Infantry", 6, 8, true),
                    new Unit("Infantry", 6, 11, true),
                    new Unit("Infantry", 7, 3, true),
                    new Unit("Infantry", 7, 5, true)
            };
        }
        else{
            return null;
        }
    }
    private static Color[][] getColors(int scenarioNumber) {
        if(scenarioNumber == 1) {
            return new Color[][]{
                    {Color.GREY, Color.GREY, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.BLUE, Color.DARKGREEN, Color.DARKGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN},
                    {Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.BLUE, Color.LIGHTGREEN, Color.DARKGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN,},
                    {Color.LIGHTGREEN, Color.LIGHTGREEN, Color.GREY, Color.BLUE, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.BLUE},
                    {Color.LIGHTGREEN, Color.LIGHTGREEN, Color.BLUE, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.DARKGREEN, Color.LIGHTGREEN, Color.BLUE,},
                    {Color.GREY, Color.LIGHTGREEN, Color.BLUE, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.DARKGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.BLUE, Color.LIGHTGREEN},
                    {Color.LIGHTGREEN, Color.BLUE, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.BLUE, Color.BLUE, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.BLUE, Color.LIGHTGREEN,},
                    {Color.LIGHTGREEN, Color.BLUE, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.BLUE, Color.BLUE, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.BLUE, Color.LIGHTGREEN, Color.LIGHTGREEN},
                    {Color.BLUE, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.DARKGREEN, Color.BLUE, Color.DARKGREEN, Color.LIGHTGREEN,},
                    {Color.BLUE, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.DARKGREEN, Color.BLUE, Color.LIGHTGREEN, Color.DARKGREEN, Color.LIGHTGREEN},
            };
        }
        else if(scenarioNumber == 2){
            return new Color[][]{
                    {Color.LIGHTGREEN, Color.LIGHTGREEN, Color.DARKGREEN, Color.DARKGREEN, Color.DARKGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.DARKGREEN, Color.DARKGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN},
                    {Color.LIGHTGREEN, Color.LIGHTGREEN, Color.DARKGREEN, Color.DARKGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.DARKGREEN, Color.DARKGREEN, Color.DARKGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN,},
                    {Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.GREY, Color.DARKGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.DARKGREEN, Color.LIGHTGREEN, Color.DARKGREEN, Color.GREY},
                    {Color.LIGHTGREEN, Color.LIGHTGREEN, Color.GREY, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.GREY, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN,},
                    {Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.DARKGREEN, Color.LIGHTGREEN, Color.GREY, Color.GREY, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.DARKGREEN, Color.LIGHTGREEN},
                    {Color.LIGHTGREEN, Color.DARKGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.DARKGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN,},
                    {Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN},
                    {Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.DARKGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.DARKGREEN, Color.LIGHTGREEN, Color.DARKGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN,},
                    {Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN},
            };
        }
        else if (scenarioNumber ==3 ){
            return new Color[][]{
                    {Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.DARKGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN},
                    {Color.DARKGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.GREY, Color.LIGHTGREEN, Color.GREY, Color.DARKGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.DARKGREEN, Color.LIGHTGREEN,},
                    {Color.LIGHTGREEN, Color.DARKGREEN, Color.DARKGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN},
                    {Color.LIGHTGREEN, Color.LIGHTGREEN, Color.DARKGREEN, Color.LIGHTGREEN, Color.DARKGREEN, Color.LIGHTGREEN, Color.GREY, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.GREY,},
                    {Color.DARKGREEN, Color.DARKGREEN, Color.LIGHTGREEN, Color.DARKGREEN, Color.LIGHTGREEN, Color.DARKGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.DARKGREEN, Color.DARKGREEN, Color.DARKGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN},
                    {Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.DARKGREEN, Color.LIGHTGREEN, Color.DARKGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN,},
                    {Color.LIGHTGREEN, Color.LIGHTGREEN, Color.DARKGREEN, Color.LIGHTGREEN, Color.GREY, Color.LIGHTGREEN, Color.DARKGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.DARKGREEN, Color.LIGHTGREEN},
                    {Color.LIGHTGREEN, Color.DARKGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.DARKGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN,},
                    {Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.DARKGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN, Color.DARKGREEN, Color.LIGHTGREEN, Color.LIGHTGREEN},
            };
        }
        else{
            return null;
        }
    }
}