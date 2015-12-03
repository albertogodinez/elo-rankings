package elorankings.model;


import java.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;



public class PlayerProfile {
    private final StringProperty playersTag;
    private List<StringProperty> possibleNames = new ArrayList<>();
    private HashMap<String, Integer> playerRecords = new HashMap<String, Integer>();
    private final DoubleProperty score;
    private final IntegerProperty ranking;
    private final IntegerProperty tourneysEntered;
    private final IntegerProperty setsPlayed;
    private final StringProperty lastDateEntered;
    private final StringProperty playersStatus;
    private int kRating;
    private int playerId;
    private int lastDateEnteredInt = 1;
    private int tournamentsMissed = 0;
    
    public PlayerProfile(String playersTag, Double score, Integer id){
        this.playersTag = new SimpleStringProperty(playersTag);
        this.score = new SimpleDoubleProperty(score);
        //If player is innactive or unrated, then set to -1
        ranking = new SimpleIntegerProperty(1);
        tourneysEntered = new SimpleIntegerProperty(0);
        setsPlayed = new SimpleIntegerProperty(0);
        //Players Status include, unrated, active, innactive
        playersStatus = new SimpleStringProperty("unrated");
        lastDateEntered = new SimpleStringProperty("");
        playerId = id;
    }
    
    public StringProperty playersTag(){
        return playersTag;
    }
    
    public String getPlayersTag(){
        return playersTag.get();
    }
    
    
    public void setPlayersTag(String playersTag){
        this.playersTag.set(playersTag);
    }
    
    public DoubleProperty score(){
        return score;
    }
    
    public double getScore(){
        return score.get();
    }
    
    public void setScore(double score){
        this.score.set(score);
    }
    
    public IntegerProperty ranking(){
        return ranking;
    }
    
    public Integer getRanking(){
        return ranking.get();
    }
    
    public void setRanking(int ranking){
        this.ranking.set(ranking);
    }
    
    public IntegerProperty tourneysEntered(){
        return tourneysEntered;
    }
    
    public Integer getTourneysEntered(){
        return tourneysEntered.get();
    }
    
    public void setTourneysEntered(int tourneysEntered){
        this.tourneysEntered.set(tourneysEntered);
    }
    
    public void incrementTournamentsEntered(){
        tourneysEntered.set(tourneysEntered.get()+1);
        if(playersStatus.equals("inactive"))
            tournamentsMissed--;
       //if(tourneysEntered.get())
    }
    
    public int getSetsPlayed(){
        return setsPlayed.get();
    }
    
    public void setSetsPlayed(int setsPlayed){
        this.setsPlayed.set(setsPlayed);
    }
    
    public StringProperty playersStatus(){
        return playersStatus;
    }
    
    public String getPlayersStatus(){
        return playersStatus.get();
    }
    
    public void setPlayersStatus(String playersStatus){
        this.playersStatus.set(playersStatus);
    }
    
    public StringProperty lastDateEntered(){
        return lastDateEntered;
    }
    
    public String getLastDateEntered(){
        return lastDateEntered.get();
    }
    
    public void setLastDateEntered(String lastDateEntered){
        this.lastDateEntered.set(lastDateEntered);
        String[] splitDate = lastDateEntered.split("/");
        int temp;
        lastDateEnteredInt = 1;
        for(int i = 0; i<splitDate.length; i++){
            temp = Integer.parseInt(splitDate[i]);
            lastDateEnteredInt *=temp;
        }
        System.out.println("In PlayerProfile class, " + lastDateEntered + " : " + lastDateEnteredInt);
    }
    
    
    public void setPlayerId(int id){
        playerId = id;
    }
    
    public int getPlayerId(){
        return playerId;
    }
    
    public void incrementTournamentsMissed(){
        tournamentsMissed++;
    }
    
    public int getTournamentsMissed(){
        return tournamentsMissed;

    //TODO I'm going to have to create a function in PRSettings to parse through
                //all active players and change accordingly!'
    }
}
