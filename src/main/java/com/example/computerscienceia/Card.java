package com.example.computerscienceia;

import java.util.ArrayList;

public class Card {
    private String[] allCardTexts;
    private String[][] allCardSections;
    private String text;
    private int numberOfMovableUnitsLeft=0;
    private int numberOfMovableUnitsCenter=0;
    private  int numberOfMovableUnitsRight=0;
    public static ArrayList<Integer> indexesOfCardsAlreadyUsed = new ArrayList<>();
    private int randomIndex;
    public Card(){
        text = "";
        allCardTexts = getAllCardTexts();
        allCardSections = getAllSections();
        generateRandomText();
    }
    public int getNumberOfMovableUnitsLeft(){
        return numberOfMovableUnitsLeft;
    }
    public int getNumberOfMovableUnitsRight(){
        return numberOfMovableUnitsRight;
    }
    public int getNumberOfMovableUnitsCenter(){
        return numberOfMovableUnitsCenter;
    }
    public void generateRandomText(){
        if (indexesOfCardsAlreadyUsed.size() == allCardTexts.length){
            indexesOfCardsAlreadyUsed = new ArrayList<>();
        }
        randomIndex = (int)(Math.random()*allCardTexts.length);
        while (isInIndexUsedList(randomIndex)){
            randomIndex = (int)(Math.random()*allCardTexts.length);
        }
        indexesOfCardsAlreadyUsed.add(randomIndex);
        text = allCardTexts[randomIndex];

        int n;
        if(text.contains("1")){
            n = 1;
        }
        else if (text.contains("2")){
            n = 2;
        }
        else if(text.contains("3")){
            n = 3;
        }
        else{
            n = 10;
        }

        String [] sectionsForThisCard = getSectionArr();
        for (String s : sectionsForThisCard) {
            switch (s) {
                case "Left" ->  numberOfMovableUnitsLeft+= n ;
                case "Center" -> numberOfMovableUnitsCenter+= n;
                case "Right" -> numberOfMovableUnitsRight+=n;
            }
        }
    }
    private boolean isInIndexUsedList(int randomIndex){
        boolean isInIndexUsedList = false;
        for(int i = 0; i < indexesOfCardsAlreadyUsed.size();i++){
            if (indexesOfCardsAlreadyUsed.get(i) == randomIndex){
                isInIndexUsedList = true;
            }
        }
        return isInIndexUsedList;
    }
    public String[] getSectionArr(){
        return allCardSections[randomIndex];
    }
    public String getCardText(){
        return text;
    }
    private String[][] getAllSections(){
        return new String[][]{
                {"Center","Left and Center","Center and Right"},
                {"Center","Left and Center","Center and Right"},
                {"Left","Left and Center"},
                {"Left","Left and Center"},
                {"Left","Left and Center","Center","Center and Right","Right"},
                {"Left","Left and Center","Center","Center and Right","Right"},
                {"Left","Left and Center","Center","Center and Right","Right"},
                {"Right", "Center and Right"},
                {"Right", "Center and Right"},
                {"Left","Left and Center"},
                {"Left","Left and Center"},
                {"Left","Left and Center"},
                {"Left","Left and Center"},
                {"Left","Left and Center","Center","Center and Right","Right"},
                {"Left","Left and Center","Right", "Center and Right"},
                {"Right", "Center and Right"},
                {"Right", "Center and Right"},
                {"Right", "Center and Right"},
                {"Right", "Center and Right"},
                {"Left and Center", "Center", "Center and Right"},
                {"Left and Center", "Center", "Center and Right"},
                {"Left and Center", "Center", "Center and Right"},
                {"Left and Center", "Center", "Center and Right"},
                {"Left and Center", "Center", "Center and Right"},
                {"Left","Left and Center"},
                {"Left","Left and Center"},
                {"Left","Left and Center"},
                {"Left and Center", "Center", "Center and Right"},
                {"Left and Center", "Center", "Center and Right"},
                {"Left and Center", "Center", "Center and Right"},
                {"Left and Center", "Center", "Center and Right"},
                {"Right", "Center and Right"},
                {"Right", "Center and Right"},
                {"Right", "Center and Right"},
                {"Left","Left and Center"},
                {"Left","Left and Center"},
                {"Left and Center", "Center", "Center and Right"},
                {"Left and Center", "Center", "Center and Right"},
                {"Right", "Center and Right"},
                {"Right", "Center and Right"},
        };
    }
    private String[] getAllCardTexts(){
        return new String[]{
                "Issue an order \n to 1 unit  \n in the Center",
                "Issue an order \n to 1 unit \n in the Center",
                "Issue an order \n to 1 unit \n on the Left Flank",
                "Issue an order \n to 1 unit \n on the Left Flank",
                "Issue an order \n to 1 unit \n in each section",
                "Issue an order \n to 1 unit \n in each section",
                "Issue an order \n to 1 unit \n in each section",
                "Issue an order \n to 1 unit \n on the Right Flank",
                "Issue an order \n to 1 unit \n on the Right Flank",
                "Issue an order \n to 2 units \n on the Left Flank",
                "Issue an order \n to 2 units \n on the Left Flank",
                "Issue an order \n to 2 units \n on the Left Flank",
                "Issue an order \n to 2 units \n on the Left Flank",
                "Issue an order \n to 2 units \n in each section",
                "Issue an order \n to 2 units \n on the Left Flank \n and 2 units on the Right Flank",
                "Issue an order \n to 2 units \n on the Right Flank",
                "Issue an order \n to 2 units \n on the Right Flank",
                "Issue an order \n to 2 units \n on the Right Flank",
                "Issue an order \n to 2 units \n on the Right Flank",
                "Issue an order \n to 2 units \n in the Center",
                "Issue an order \n to 2 units \n in the Center",
                "Issue an order \n to 2 units \n in the Center",
                "Issue an order \n to 2 units \n in the Center",
                "Issue an order \n to 2 units \n in the Center",
                "Issue an order \n to 3 units \n on the Left Flank",
                "Issue an order \n to 3 units \n on the Left Flank",
                "Issue an order \n to 3 units \n on the Left Flank",
                "Issue an order \n to 3 units \n in the Center",
                "Issue an order \n to 3 units \n in the Center",
                "Issue an order \n to 3 units \n in the Center",
                "Issue an order \n to 3 units \n in the Center",
                "Issue an order \n to 3 units \n on the Right Flank",
                "Issue an order \n to 3 units \n on the Right Flank",
                "Issue an order \n to 3 units \n on the Right Flank",
                "Issue an order \n to all units \n on the Left Flank",
                "Issue an order \n to all units \n on the Left Flank",
                "Issue an order \n to all units \n in the Center",
                "Issue an order \n to all units \n in the Center",
                "Issue an order \n to all units \n on the Right Flank",
                "Issue an order \n to all units \n on the Right Flank",
        };
    }
}
