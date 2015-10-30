package elorankings.model;


import java.time.LocalDate;

import java.util.ArrayList;
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
    private final DoubleProperty score;
    private final IntegerProperty ranking;
    private final IntegerProperty tourneysEntered;
    private final ObjectProperty<LocalDate> lastDateEntered;
    private final StringProperty playersStatus;
    private int kRating;
    
    public PlayerProfile(String playersTag, Double score){
        this.playersTag = new SimpleStringProperty(playersTag);
        this.score = new SimpleDoubleProperty(score);
        //If player is innactive or unrated, then set to -1
        ranking = new SimpleIntegerProperty(-1);
        tourneysEntered = new SimpleIntegerProperty(0);
        //Players Status include, unrated, active, innactive, permanentlyInnactive
        playersStatus = new SimpleStringProperty("unrated");
        lastDateEntered = new SimpleObjectProperty<LocalDate>();
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
    
    public StringProperty playersStatus(){
        return playersStatus;
    }
    
    public String getPlayersStatus(){
        return playersStatus.get();
    }
    
    public void setPlayersStatus(String playersStatus){
        this.playersStatus.set(playersStatus);
    }
    
    public ObjectProperty<LocalDate> lastDateEntered(){
        return lastDateEntered;
    }
    
    public LocalDate getLastDateEntered(){
        return lastDateEntered.get();
    }
    
    public void setLastDateEntered(LocalDate lastDateEntered){
        this.lastDateEntered.set(lastDateEntered);
    }
}
