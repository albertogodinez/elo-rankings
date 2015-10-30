package elorankings.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
/*
   Model class for each of the Power Rankings
*/
public class PRSettings {
    //Holds all of the players that are in this PR
    private List<PlayerProfile> playersList = new ArrayList<PlayerProfile>();
    private final StringProperty prName;
    private final DoubleProperty initScore;
    private final BooleanProperty showPlacingDiff;
    private final BooleanProperty showPointDiff;
    private final BooleanProperty removeInnactive;
    //Number of tournaments missed before Active player is considered Innactive
    private final IntegerProperty numTourneysForInnactive;
    //Number of tournaments entered before Active player is considered Innactive
    private final IntegerProperty numTourneysForActive;
    private final BooleanProperty implementPointDecay;
    //Decay starts taking effect on this missed tournament
    private final IntegerProperty startOfDecay;
    //This is the amount of points that are taken off per week for players
    //who are going through decay.
    private final IntegerProperty pointsRemoved;
    private boolean samePointsRemoved = true;
    
    public PRSettings(){
        prName = new SimpleStringProperty("");
        initScore = new SimpleDoubleProperty();
        showPlacingDiff = new SimpleBooleanProperty();
        showPointDiff = new SimpleBooleanProperty();
        removeInnactive = new SimpleBooleanProperty();
        numTourneysForInnactive = new SimpleIntegerProperty(5);
        numTourneysForActive = new SimpleIntegerProperty(2);
        implementPointDecay = new SimpleBooleanProperty(false);
        startOfDecay = new SimpleIntegerProperty(3);
        pointsRemoved = new SimpleIntegerProperty(5);
    }
    
    public StringProperty prName(){
        return prName;
    }
    
    public String getPrName(){
        return prName.get();
    }
    
    public void setPrName(String prName){
        this.prName.set(prName);
    }
    
    public DoubleProperty initScore(){
        return initScore;
    }
    
    public double getInitScore(){
        return initScore.get();
    }
    
    public void setInitScore(int initScore){
        this.initScore.set(initScore);
    }
    
    public BooleanProperty showPlacingDiff(){
        return showPlacingDiff;
    }
    
    public boolean getShowPlacingDiff(){
        return showPlacingDiff.get();
    }
    
    public void setShowPlacingDiff(boolean showPlacingDiff){
        this.showPlacingDiff.set(showPlacingDiff);
    }
    
    public BooleanProperty showPointDiff(){
        return showPointDiff;
    }
    
    public boolean getShowPointDiff(){
        return showPointDiff.get();
    }
    
    public void setShowPointDiff(boolean showPointDiff){
        this.showPointDiff.set(showPointDiff);
    }
    
    public BooleanProperty removeInnactive(){
        return removeInnactive;
    }
    
    public boolean getRemoveInnactive(){
        return removeInnactive.get();
    }
    
    public void setRemoveInnactive(boolean removeInnactive){
        this.removeInnactive.set(removeInnactive);
    }
    
    public IntegerProperty numTourneysForInnactive(){
        return numTourneysForInnactive;
    }
    
    public int getNumTourneysForInnactive(){
        return numTourneysForInnactive.get();
    }
    
    public void setNumTourneysForInnactive(int numTourneysForInnactive){
        this.numTourneysForInnactive.set(numTourneysForInnactive);
    }
    
    public IntegerProperty numTourneysForActive(){
        return numTourneysForActive;
    }
    
    public int getNumTourneysForActive(){
        return numTourneysForActive.get();
    }
    
    public void setNumTourneysForActive(int numTourneysForActive){
        this.numTourneysForActive.set(numTourneysForActive);
    }
    
    public BooleanProperty implementPointDecay(){
        return implementPointDecay;
    }
    
    public boolean getImplementPointDecay(){
        return implementPointDecay.get();
    }
    
    public void setImplementPointDecay(boolean implementPointDecay){
        this.implementPointDecay.set(implementPointDecay);
    }
    
    public IntegerProperty startOfDecay(){
        return startOfDecay;
    }
    
    public int getStartOfDecay(){
        return startOfDecay.get();
    }
    
    public void setStartOfDecay(int startOfDecay){
        this.startOfDecay.set(startOfDecay);
    }
    
    public IntegerProperty pointsRemoved(){
        return pointsRemoved;
    }
    
    public int getPointsRemoved(){
        return pointsRemoved.get();
    }
    
    public void setPointsRemoved(int pointsRemoved){
        //if pointsRemoved is < 0, this will mean that 
        //we will use a formula to get the average points
        //removed per tournament missed for decay
        if(pointsRemoved > 0)
            this.pointsRemoved.set(pointsRemoved);
        else{
            samePointsRemoved=false;
        }
    }
    
    
    
    public void sortByTag(){
        Collections.sort(playersList, new PlayerTagComparator());
    }
    
    public void sortByScore(){
        Collections.sort(playersList, new PlayerScoreComparator());
    }
    
    public boolean addPlayer(String playerTag){
        if(playersList.size() > 0){
        //System.out.print("\nTagTaken id:" + tagTaken(playerTag));
            if(tagTaken(playerTag) < 0)
                playersList.add(new PlayerProfile(playerTag, initScore.get()));
            else
                return false;
        }
        else 
            playersList.add(new PlayerProfile(playerTag,initScore.get()));
        
        return true;
        
    }
    
    public int tagTaken(String playerTag){
        return Collections.binarySearch(playersList, new PlayerProfile(playerTag, initScore.get()), new PlayerTagComparator());
    }
    
    public boolean getSamePointsRemoved(){
        return samePointsRemoved;
    }
    
    public void printAllPlayers(){
        for(PlayerProfile currPlayer : playersList){
            System.out.print(currPlayer.getPlayersTag() + " " + currPlayer.getScore() + "\n");
        }
    }
    
    class PlayerTagComparator implements Comparator<PlayerProfile>{
        @Override
        public int compare(PlayerProfile p1, PlayerProfile p2){
            return p1.getPlayersTag().compareToIgnoreCase(p2.getPlayersTag());
        }
    }
    
    class PlayerScoreComparator implements Comparator<PlayerProfile>{
        @Override
        public int compare(PlayerProfile p1, PlayerProfile p2){
            return p1.getScore() < p2.getScore() ? -1 : p1.getScore() == p2.getScore() ? 0 : 1;
        }
    }
    
}
