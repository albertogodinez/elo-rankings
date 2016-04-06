package elorankings.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class PlayerProfile {
    private final StringProperty playersTag;
    private final DoubleProperty score;
    private final IntegerProperty ranking;
    private final IntegerProperty tourneysEntered;
    private final IntegerProperty setsPlayed;
    private final StringProperty lastDateEntered;
    //This is the date for the last tournament date that the player missed
    private final StringProperty lastDateMissed;
    private final StringProperty playersStatus;
    private int playerId;
    private int tourneySetsPlayed = 1;
    private int tourneySetsLost = 0;
    private double previousScore = 0;
    private int previousRanking = -1;
    private int tournamentsMissed = 0;
    /*
    **This represents the last tournment that the player entered, by obtaining 
    **the total number.
    */
    private int lastTournamentEntered = 1;
    //Counts number of tournaments player has entered while being considered inactive
    private int tourneysWhileInactive = 0;
    
    public PlayerProfile(String playersTag, Double score, Integer id){
        this.playersTag = new SimpleStringProperty(playersTag);
        this.score = new SimpleDoubleProperty(score);
        //If player is inactive or unrated, then set to -1
        ranking = new SimpleIntegerProperty(1);
        tourneysEntered = new SimpleIntegerProperty(0);
        setsPlayed = new SimpleIntegerProperty(0);
        //Players Status include, unrated, active, inactive
        playersStatus = new SimpleStringProperty("unrated");
        lastDateEntered = new SimpleStringProperty("0");
        lastDateMissed = new SimpleStringProperty("0");
        playerId = id;
    }
    
    public PlayerProfile(){
        playersTag = new SimpleStringProperty("");
        score = new SimpleDoubleProperty();
        ranking = new SimpleIntegerProperty();
        tourneysEntered = new SimpleIntegerProperty();
        setsPlayed = new SimpleIntegerProperty();
        playersStatus = new SimpleStringProperty("");
        lastDateEntered = new SimpleStringProperty("0");
        lastDateMissed = new SimpleStringProperty("0");
        playerId = 1;
    }
    
    public PlayerProfile(PlayerProfile newProfile){
    	this.playersTag = new SimpleStringProperty(newProfile.getPlayersTag());
        this.score = new SimpleDoubleProperty(newProfile.getScore());
        this.ranking = new SimpleIntegerProperty(newProfile.getRanking());
        this.tourneysEntered = new SimpleIntegerProperty(newProfile.getTourneysEntered());
        this.setsPlayed = new SimpleIntegerProperty(newProfile.getSetsPlayed());
        this.playersStatus = new SimpleStringProperty(newProfile.getPlayersStatus());
        this.lastDateEntered = new SimpleStringProperty(newProfile.getLastDateEntered());
        this.playerId = newProfile.getPlayerId();
        this.tourneySetsPlayed = newProfile.getTourneySetsPlayed();
        this.tourneySetsLost = newProfile.getTourneySetsLost();
        this.previousScore = newProfile.getPreviousScore();
        this.previousRanking = newProfile.getPreviousRanking();
        this.tournamentsMissed = newProfile.getTournamentsMissed();
        this.lastTournamentEntered = newProfile.getLastTournamentEntered();
        this.tourneysWhileInactive = newProfile.getTourneysWhileInactive();
        this.lastDateMissed = new SimpleStringProperty(newProfile.getLastDateMissed());
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
    
    public int getRanking(){
        return ranking.get();
    }
    
    public void setRanking(int ranking){
        this.ranking.set(ranking);
    }
    
    public int getTourneySetsPlayed(){
        return tourneySetsPlayed;
    }
    
    public void setTourneySetsPlayed(int tourneySetsPlayed){
        this.tourneySetsPlayed = tourneySetsPlayed;
    }
    
    public void incrementTourneySetsPlayed(){
        tourneySetsPlayed++;
    }
    
    public int getTourneySetsLost(){
        return tourneySetsLost;
    }
    
    public void setTourneySetsLost(int tourneySetsLost){
        this.tourneySetsLost = tourneySetsLost;
    }
    
    public void incrementTourneySetsLost(){
        tourneySetsLost++;
    }
    
    public void resetTourneySets(){
        tourneySetsLost = 0;
        tourneySetsPlayed = 1;
    }
    
    public IntegerProperty tourneysEntered(){
        return tourneysEntered;
    }
    
    public int getTourneysEntered(){
        return tourneysEntered.get();
    }
    
    public void setTourneysEntered(int tourneysEntered){
        this.tourneysEntered.set(tourneysEntered);
    }
    
    public void incrementTournamentsEntered(){
        tourneysEntered.set(tourneysEntered.get()+1);
    }
    
    public int getSetsPlayed(){
        return setsPlayed.get();
    }
    
    public void setSetsPlayed(int setsPlayed){
        this.setsPlayed.set(setsPlayed);
    }
    
    public void incrementSetsPlayed(){
        setsPlayed.set(setsPlayed.get()+1);
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
    
    
    public String getLastDateEntered(){
        return lastDateEntered.get();
    }
    
    public void setLastDateEntered(String lastDateEntered){
        this.lastDateEntered.set(lastDateEntered);
        String[] splitDate = lastDateEntered.split("/");
        int temp;
        lastTournamentEntered = 1;
        for(int i = 0; i<splitDate.length; i++){
            temp = Integer.parseInt(splitDate[i]);
            lastTournamentEntered *=temp;
        }
    }
    
    
    public void setPlayerId(int id){
        playerId = id;
    }
    
    public int getPlayerId(){
        return playerId;
    }
    
    /*TODO: Probably going to have to delete this method
    **so remember to check later
    */
    public void incrementTourneysEnteredWhileInactive(){
        tourneysWhileInactive++;
    }
    /*TODO: Probably going to have to delete this method
    **so remember to check later
    */
    public int getTourneysWhileInactive(){
        return tourneysWhileInactive;
    }
    
    //Sets the total number of tournaments entered while inactive
    //*Good to use to set it to 0 when player first becomes inactive
    public void setTourneysWhileInactive(int numInactive){
        tourneysWhileInactive = numInactive;
    }
    
    public void setLastTournamentEntered(int tournamentNumber){
        lastTournamentEntered = tournamentNumber;
    }
    
    public int getLastTournamentEntered(){
        return lastTournamentEntered;
    }

	public double getPreviousScore() {
		return previousScore;
	}

	public void setPreviousScore(double previousScore) {
		this.previousScore = previousScore;
	}

	public int getPreviousRanking() {
		return previousRanking;
	}

	public void setPreviousRanking(int previousRanking) {
		this.previousRanking = previousRanking;
	}

	public int getTournamentsMissed() {
		return tournamentsMissed;
	}

	public void setTournamentsMissed(int tournamentsMissed) {
		this.tournamentsMissed = tournamentsMissed;
	}

	public StringProperty lastDateMissed(){
		return lastDateMissed;
	}
    
	public void setLastDateMissed(String lastDateMissed){
		this.lastDateMissed.set(lastDateMissed);
	}
	
	public String getLastDateMissed(){
		return lastDateMissed.get();
	}
}
