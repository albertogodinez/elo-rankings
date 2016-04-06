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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
/*
   Model class for each of the Power Rankings
*/
@XmlType(name="prsetting")
public class PRSettings implements Cloneable{
    //Holds all of the players that are in this PR
    //Considering changing to maps instead, but will change later
    @XmlElementWrapper(name="players")
    @XmlElement(name ="player")
    private ArrayList<PlayerProfile> playersList;
    private final StringProperty prName;
    private final DoubleProperty initScore;
    private final StringProperty challongeUsername;
    private final StringProperty challongeApiKey;
    private final BooleanProperty showPlacingDiff;
    private final BooleanProperty showPointDiff;
    private final BooleanProperty removeInactive;
    /*
    **Number of tournaments an Unrated player must attend before being considered
    **Active
    */
    private final IntegerProperty numTourneysNeeded;
    //Number of sets player needs to play in order to be considered Active
    private final IntegerProperty numSetsNeeded;
    //Number of tournaments missed before Active player is considered Inactive
    private final IntegerProperty numTourneysForInactive;
    //Number of tournaments entered before Inactive player is considered Active
    private final IntegerProperty numTourneysForActive;
    private final BooleanProperty implementPointDecay;
    //Decay starts taking effect on this missed tournament
    private final IntegerProperty startOfDecay;
    //This is the amount of points that are taken off per week for players
    //who are going through decay.
    private final IntegerProperty pointsRemoved;
    private boolean samePointsRemoved;
    //This keeps track of the players current id
    private int currentId;
    /*
    **This will increment every single time a new tournament is added
    **and will be used to see how many tournaments the user has missed
    */
    private int totalNumTournaments;
    private final StringProperty lastDateEntered;
    
    public PRSettings(){
    	playersList = new ArrayList<PlayerProfile>();
        prName = new SimpleStringProperty("");
        initScore = new SimpleDoubleProperty(2);
        challongeUsername = new SimpleStringProperty("");
        challongeApiKey = new SimpleStringProperty("");
        showPlacingDiff = new SimpleBooleanProperty();
        showPointDiff = new SimpleBooleanProperty();
        removeInactive = new SimpleBooleanProperty();
        numTourneysNeeded = new SimpleIntegerProperty(3);
        numSetsNeeded = new SimpleIntegerProperty(8);
        numTourneysForInactive = new SimpleIntegerProperty(5);
        numTourneysForActive = new SimpleIntegerProperty(2);
        implementPointDecay = new SimpleBooleanProperty(false);
        startOfDecay = new SimpleIntegerProperty(3);
        pointsRemoved = new SimpleIntegerProperty(5);
        samePointsRemoved = true;
        currentId=0;
        totalNumTournaments=0;
        lastDateEntered = new SimpleStringProperty("");
    }
    
    public PRSettings(PRSettings pr) {
    	playersList = new ArrayList<PlayerProfile>();
    	PlayerProfile tempPro;
    	for(PlayerProfile playerPro : pr.getAllPlayers()){
    		tempPro = new PlayerProfile(playerPro);
    		playersList.add(tempPro);
    	}
        this.prName = new SimpleStringProperty(pr.getPrName());
        this.initScore = new SimpleDoubleProperty(pr.getInitScore());
        this.challongeUsername = new SimpleStringProperty(pr.getChallongeUsername());
        this.challongeApiKey = new SimpleStringProperty(pr.getChallongeApiKey());
        this.showPlacingDiff = new SimpleBooleanProperty(pr.getShowPlacingDiff());
        this.showPointDiff = new SimpleBooleanProperty(pr.getShowPointDiff());
        this.removeInactive = new SimpleBooleanProperty(pr.getRemoveInactive());
        this.numTourneysNeeded = new SimpleIntegerProperty(pr.getNumTourneysNeeded());
        this.numSetsNeeded = new SimpleIntegerProperty(pr.getNumSetsNeeded());
        this.numTourneysForInactive = new SimpleIntegerProperty(pr.getNumTourneysForInactive());
        this.numTourneysForActive = new SimpleIntegerProperty(pr.getNumTourneysForActive());
        this.implementPointDecay = new SimpleBooleanProperty(pr.getImplementPointDecay());
        this.startOfDecay = new SimpleIntegerProperty(pr.getStartOfDecay());
        this.pointsRemoved = new SimpleIntegerProperty(pr.getPointsRemoved());
        this.samePointsRemoved = pr.getSamePointsRemoved();
        this.currentId= pr.getCurrentId();
        this.totalNumTournaments=pr.getTotalNumTournaments();
        this.lastDateEntered = new SimpleStringProperty(pr.getLastDateEntered());
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
    
    public void setInitScore(double initScore){
        this.initScore.set(initScore);
    }
    
    public void setChallongeUsername(String username){
        challongeUsername.set(username);
    }
    
    public String getChallongeUsername(){
        return challongeUsername.get();
    }
    
    public void setChallongeApiKey(String apiKey){
        challongeApiKey.set(apiKey);
    }
    
    public String getChallongeApiKey(){
        return challongeApiKey.get();
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
    
    public void setNumTourneysNeeded(int numTourneys){
        numTourneysNeeded.set(numTourneys);
        
        for(PlayerProfile playerPro : playersList){
            if(playerPro.getTourneysEntered() < numTourneysNeeded.get() && 
               playerPro.getPlayersStatus().equals("active"))
                playerPro.setPlayersStatus("unrated");
            else if(playerPro.getTourneysEntered() >= numTourneysNeeded.get() &&
                    playerPro.getPlayersStatus().equals("unrated"))
                playerPro.setPlayersStatus("active");
        }
    }
    
    public int getNumTourneysNeeded(){
        return numTourneysNeeded.get();
    }
    
    public int getNumSetsNeeded(){
        return numSetsNeeded.get();
    }
    
    public void setNumSetsNeeded(int numSetsNeeded){
        this.numSetsNeeded.set(numSetsNeeded);
    }
    
    public BooleanProperty removeInactive(){
        return removeInactive;
    }
    
    public boolean getRemoveInactive(){
        return removeInactive.get();
    }
    
    public void setRemoveInactive(boolean removeInactive){
        this.removeInactive.set(removeInactive);
        
        if(this.removeInactive.get()){
        	for(PlayerProfile playerPro : playersList){
        		if((totalNumTournaments - playerPro.getLastTournamentEntered()) >= numTourneysForInactive.get()){
        			playerPro.setTourneysWhileInactive(0);
        			playerPro.setPlayersStatus("inactive");
        		}
        	}
        }
        else{
        	for(PlayerProfile playerPro : playersList){
        		if(playerPro.getPlayersStatus().equals("inactive"))
        			playerPro.setPlayersStatus("active");
        	}
        }
    }
    
    public int getNumTourneysForInactive(){
        return numTourneysForInactive.get();
    }
    
    public void setNumTourneysForInactive(int numTourneysForInactive){
        this.numTourneysForInactive.set(numTourneysForInactive);
    }

    public int getNumTourneysForActive(){
        return numTourneysForActive.get();
    }
    
    public void setNumTourneysForActive(int numTourneysForActive){
        this.numTourneysForActive.set(numTourneysForActive);
    }

    public boolean getImplementPointDecay(){
        return implementPointDecay.get();
    }
    
    public void setImplementPointDecay(boolean implementPointDecay){
        this.implementPointDecay.set(implementPointDecay);
    }

    public int getStartOfDecay(){
        return startOfDecay.get();
    }
    
    public void setStartOfDecay(int startOfDecay){
        this.startOfDecay.set(startOfDecay);
    }
    
    public int getPointsRemoved(){
        return pointsRemoved.get();
    }
    
    public void setPointsRemoved(int pointsRemoved){
        //if pointsRemoved is < 0, this will mean that 
        //we will use a formula to get the average points
        //removed per tournament missed for decay
        if(pointsRemoved > 0){
            this.pointsRemoved.set(pointsRemoved);
            samePointsRemoved=true;
        }
        else{
            samePointsRemoved=false;
        }
    }
    
    public void incrementTotalTournaments(){
        totalNumTournaments++;
    }
    
    public int getTotalNumTournaments(){
        return totalNumTournaments;
    }
    
    public void setTotalNumTournaments(int totalNumTournaments){
        this.totalNumTournaments = totalNumTournaments;
    }
    
    public void sortByTag(){
        Collections.sort(playersList, new PlayerTagComparator());
    }
    
    public void sortByScore(){
        Collections.sort(playersList, new PlayerScoreComparator());
        int i = 1;
        double prevScore = -1;
        for(PlayerProfile player : playersList){
            if(player.getPlayersStatus().equals("active")){
                if(prevScore < 0){
                    player.setRanking(i);
                    prevScore = player.getScore();
                }
                else if(prevScore == player.getScore())
                    player.setRanking(i);
                else{
                    i++;
                    player.setRanking(i);
                    prevScore = player.getScore();
                }
            }
        }
    }
    
     public boolean deletePlayerByTag(String tag){
        for(int i=0; i<playersList.size(); i++){
            if(playersList.get(i).getPlayersTag().equals(tag)){
                playersList.remove(i);
                return true;
            }
        }
        return false;
    }
    
    public boolean addPlayerByTag(String playerTag){
        if(playersList.size() > 0){
        //System.out.print("\nTagTaken id:" + tagUsed(playerTag));
            if(tagUsed(playerTag) < 0){
                playersList.add(new PlayerProfile(playerTag, initScore.get(), currentId));
                currentId++;
            }
            else
                return false;
        }
        else {
            playersList.add(new PlayerProfile(playerTag,initScore.get(), currentId));
            currentId++;
        }
        
        return true;
        
    }
    
    public boolean addPlayerByObject(PlayerProfile newPlayer){
        if(playersList.size() > 0){
            if(tagUsed(newPlayer.getPlayersTag()) < 0){
                newPlayer.setPlayerId(currentId);
                playersList.add(newPlayer);
                currentId++;
            }
            else
                return false;
        }
        else{
            newPlayer.setPlayerId(currentId);
            playersList.add(newPlayer);
            currentId++;
        }
        
        return true;
    }
    
    //Following functions update players stats
    public Boolean tagTaken(String playerTag){
        if(tagUsed(playerTag) < 0)
            return false;
        else
            return true;
    }
    
    public boolean containsCaseInsensitive(String s, List<String> l){
        for (String string : l){
            if (string.equalsIgnoreCase(s)){
                return true;
            }
        }
        return false;
    }
    
    public Boolean updateTournamentsEntered(List<String> participants, String dateOfTournament){
        for(int i=0; i< playersList.size(); i++){
            //This is done if a player in the PR entered the tournament
            if(containsCaseInsensitive(playersList.get(i).getPlayersTag(), participants)){
                System.out.println(playersList.get(i).getPlayersTag() + " is being updated.\n");
                if(!playersList.get(i).getLastDateEntered().equals(lastDateEntered.get())){
                	playersList.get(i).setLastDateEntered(dateOfTournament);
                	playersList.get(i).incrementTournamentsEntered();
                	//playersList.get(i).setLastTournamentEntered(totalNumTournaments);
                	playersList.get(i).setTournamentsMissed(0);
                }
                if(playersList.get(i).getTourneysEntered() >= numTourneysNeeded.get()){
                    String status = playersList.get(i).getPlayersStatus();
                    if(status.equals("unrated"))
                        playersList.get(i).setPlayersStatus("active");
                }
                
                if(playersList.get(i).getPlayersStatus().equals("inactive") && 
                		!playersList.get(i).getLastDateEntered().equals(lastDateEntered.get())){
                    playersList.get(i).incrementTourneysEnteredWhileInactive();
                    if(playersList.get(i).getTourneysWhileInactive() >= numTourneysForActive.get())
                        playersList.get(i).setPlayersStatus("active");
                }
                playersList.get(i).resetTourneySets();
            }
            else{
            	if(playersList.get(i).getPlayersStatus().equals("active")){
            		if(!lastDateEntered.get().equals(playersList.get(i).getLastDateMissed())){
            			playersList.get(i).setTournamentsMissed(playersList.get(i).getTournamentsMissed()+1);
            		}
            		
            		System.out.println(playersList.get(i).getPlayersTag() + " : " + playersList.get(i).getTournamentsMissed());
            		
            		if(removeInactive.getValue() && !lastDateEntered.get().equals(playersList.get(i).getLastDateMissed())){
            			if(playersList.get(i).getTournamentsMissed() >= numTourneysForInactive.get()){
            				playersList.get(i).setTourneysWhileInactive(0);
            				playersList.get(i).setPlayersStatus("inactive");
            			}
            			System.out.println("\npoint decay is : " + implementPointDecay.getValue());
            			if(implementPointDecay.getValue() && playersList.get(i).getTournamentsMissed() >= startOfDecay.getValue()){
            				System.out.println("\nPoints removed: " + pointsRemoved.getValue());
            				playersList.get(i).setScore(playersList.get(i).getScore()-pointsRemoved.getValue());
            			}
            		}
            		playersList.get(i).setLastDateMissed(lastDateEntered.get());
            	}
            }
        }
        return true;
    }
    
    public int findPlayerIndexUsingTag(String playerTag){
        for(int i =0; i < playersList.size(); i++){
            if(playersList.get(i).getPlayersTag().equalsIgnoreCase(playerTag))
                return i; 
        }
        return -1;
    }
    //End of functions that update players stats
    /*
    **Searches through player list using custom comparator to see if tag is 
    **already taken 
    */
    private int tagUsed(String playerTag){
        //The -1 represents the player ID
        return Collections.binarySearch(playersList, new PlayerProfile(playerTag, initScore.get(), -1), new PlayerTagComparator());
    }
    
    public boolean getSamePointsRemoved(){
        return samePointsRemoved;
    }
    
    public void setSamePointsRemoved(boolean samePointsRemoved){
        this.samePointsRemoved = samePointsRemoved;
    }
    
    public void printAllPlayers(){
        for(PlayerProfile currPlayer : playersList){
            System.out.print(currPlayer.getPlayersTag() + " " + currPlayer.getScore() + "\n");
        }
    }
    
    public  List<PlayerProfile> getAllPlayers(){
        return playersList;
    }
    
    public void setAllPlayers(ArrayList<PlayerProfile> newPlayers){
    	this.playersList = newPlayers;
    }
    
    public PlayerProfile getLastPlayerAdded(){
        return playersList.get(playersList.size()-1);
    }
    
    //This functions can more than likely be made faster
    public PlayerProfile getPlayerByTag(String playerTag){
        for(PlayerProfile playerProfile : playersList){
            if(playerProfile.getPlayersTag().equalsIgnoreCase(playerTag))
                return playerProfile;
        }
        
        return null;
    }
    
    public int getCurrentId(){
        return currentId;
    }
    
    public void setCurrentId(int currentId){
        this.currentId = currentId;
    }
    
    class PlayerTagComparator implements Comparator<PlayerProfile>{
        @Override
        public int compare(PlayerProfile p1, PlayerProfile p2){
            //System.out.println("p1: " + p1.getPlayersTag() + " p2: " + p2.getPlayersTag());
            return p1.getPlayersTag().compareToIgnoreCase(p2.getPlayersTag());
        }
    }
    
    class PlayerScoreComparator implements Comparator<PlayerProfile>{
        @Override
        public int compare(PlayerProfile p1, PlayerProfile p2){
            return p1.getScore() > p2.getScore() ? -1 : p1.getScore() == p2.getScore() ? 0 : 1;
        }
    }

	public StringProperty showLastDateEntered() {
		return lastDateEntered;
	}
	
	public String getLastDateEntered(){
		return lastDateEntered.get();
	}
	
	public void setLastDateEntered(String lastDateEntered){
		this.lastDateEntered.set(lastDateEntered);
	}
}
