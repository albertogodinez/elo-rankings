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
/*
   Model class for each of the Power Rankings
*/
public class PRSettings {
    //Holds all of the players that are in this PR
    //Considering changing to maps instead, but will change later
    private ArrayList<PlayerProfile> playersList = new ArrayList<PlayerProfile>();
    private final StringProperty prName;
    private final DoubleProperty initScore;
    private final StringProperty challongeUsername;
    private final StringProperty challongeApiKey;
    private final BooleanProperty showPlacingDiff;
    private final BooleanProperty showPointDiff;
    private final BooleanProperty removeInnactive;
    /*
    **Number of tournaments an Unrated player must attend before being considered
    **Active
    */
    private final IntegerProperty numTourneysNeeded;
    //Number of sets player needs to play in order to be considered Active
    private final IntegerProperty numSetsNeeded;
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
    //This keeps track of the players current id
    private int currentId=0;
    
    public PRSettings(){
        prName = new SimpleStringProperty("");
        initScore = new SimpleDoubleProperty();
        challongeUsername = new SimpleStringProperty("");
        challongeApiKey = new SimpleStringProperty("");
        showPlacingDiff = new SimpleBooleanProperty();
        showPointDiff = new SimpleBooleanProperty();
        removeInnactive = new SimpleBooleanProperty();
        numTourneysNeeded = new SimpleIntegerProperty(3);
        numSetsNeeded = new SimpleIntegerProperty(8);
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
        if(pointsRemoved > 0){
            this.pointsRemoved.set(pointsRemoved);
            samePointsRemoved=true;
        }
        else{
            samePointsRemoved=false;
        }
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
    
    public Boolean updateTournamentsEntered(String playerTag, String dateOfTournament){
        int playerIndex = findPlayerIndexUsingTag(playerTag);
        if(playerIndex == -1)
            return false;
        playersList.get(playerIndex).setLastDateEntered(dateOfTournament);
        playersList.get(playerIndex).incrementTournamentsEntered();
        if(playersList.get(playerIndex).getTourneysEntered() >= numTourneysNeeded.get()){
            String status = playersList.get(playerIndex).getPlayersStatus();
            if(status.equals("unrated"))
                playersList.get(playerIndex).setPlayersStatus("active");
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
    
    public void printAllPlayers(){
        for(PlayerProfile currPlayer : playersList){
            System.out.print(currPlayer.getPlayersTag() + " " + currPlayer.getScore() + "\n");
        }
    }
    
    public  List<PlayerProfile> getAllPlayers(){
        return playersList;
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
    
    class PlayerTagComparator implements Comparator<PlayerProfile>{
        @Override
        public int compare(PlayerProfile p1, PlayerProfile p2){
            return p1.getPlayersTag().compareToIgnoreCase(p2.getPlayersTag());
        }
    }
    
    class PlayerScoreComparator implements Comparator<PlayerProfile>{
        @Override
        public int compare(PlayerProfile p1, PlayerProfile p2){
            return p1.getScore() > p2.getScore() ? -1 : p1.getScore() == p2.getScore() ? 0 : 1;
        }
    }
    
 
    
}
