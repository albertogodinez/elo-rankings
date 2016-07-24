package elorankings.controller;

import challongeapi.ChallongeWrapper;
import challongeapi.pojoclasses.Match;
import challongeapi.pojoclasses.Tournament;
import elorankings.formula.EloFormula;
import elorankings.model.PRSettings;
import elorankings.model.PlayerProfile;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

/**
 *
 * @author Alberto
 */
public class AddTournamentController {
    @FXML
    private TextField challongeLink;
    @FXML
    private Button backButton;
    
    MainApp mainApp;
    PRSettings oldPr;
    PRSettings newPr;
    private boolean countAsTourney;
    
/**
  * Is called by the main application to give a reference back to itself.
  * 
  * @param mainApp
  * @param pr
**/
    public void setMainApp(MainApp mainApp, PRSettings pr) {
        this.mainApp = mainApp;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/elorankings/view/MainMenu.fxml"));
        loader.setController(this);
        
        oldPr = new PRSettings(pr);
        newPr = new PRSettings(pr);     
        newPr.sortByTag();
    }
    
    private boolean confirmPointsGivenDialog(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Give full points for rankings");
        alert.setContentText("Select OK if you would like players to receive full points "
                + "for points gained/loss. If you would like players to receive a fraction"
                + " of the points, select cancel.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            return true;
        } else {
            return false;
        }
    }
    
    private double retrievePointsToBeGiven(){
        double returnedValue;
        List<String> choices = new ArrayList<>();
        choices.add("25%");
        choices.add("50%");
        choices.add("75%");
        choices.add("100%");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("", choices);
        dialog.setTitle("Choose percentage");
        dialog.setHeaderText("Select percentage players will receive for points gained/loss");
        dialog.setContentText("Select a percentage:");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        String temp = "0";
        if (result.isPresent() && !result.get().isEmpty()){
            System.out.println("Your choice: " + result.get());
            String tempArr[] = result.get().split("%");
            temp = tempArr[0];
        }
        returnedValue = Double.parseDouble(temp);
        returnedValue = .01*returnedValue;
        
        return returnedValue;
    }
    
    private void failedURLAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText("URL Not Found");
        alert.setContentText("I'm sorry."
        		+ "\nI could not find the Challonge URL entered."
        		+ "\nPlease enter another Challonge URL or check to see if"
        		+ "the Challonge API Key and Username are entered correctly.");

        alert.showAndWait();
    }
    
    private boolean countTournamentDialog(){
    	Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Tournament");
        alert.setHeaderText("Increase tournaments attended for these players");
        alert.setContentText("Select OK if you would like this tournament to count "
        		+ "as a tournament attended for players selected. "
                + "If you would like this to not count as a tournament"
                + " select cancel.");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            return true;
        } else {
            return false;
        }
    }
    
    @FXML
    public void backToPROptionsScreen(){ 
       // mainApp.openPROptionsScreen(pr.getPrName());
        mainApp.backToPROptionsScreen(oldPr);
    }
    
    @FXML
    public void saveUpdate(){
    	mainApp.backToPROptionsScreen(newPr);
    }
    
    private void updatePreviousInfo(List<PlayerProfile> tempProfiles){
    	for(PlayerProfile playerPro : tempProfiles){
    		playerPro.setPreviousScore(playerPro.getScore());
    		if(playerPro.getPlayersStatus().equals("active"))
    			playerPro.setPreviousRanking(playerPro.getRanking());
    		else
    			playerPro.setPreviousRanking(-1);
    	}
    }
    
    @FXML
    public void addTournament(){
        ChallongeWrapper cw = new ChallongeWrapper();
        
        newPr.sortByTag();
        
        try{
        	cw.runUrl(newPr.getChallongeUsername(), newPr.getChallongeApiKey(), challongeLink.getText());
        	
        	Tournament tempTournament = cw.getTournamentInfo();
            List<String> participants =  cw.getParticipants();
            List<String> unusedParticipants = new ArrayList<>();
            
            List<PlayerProfile> tempProfiles = newPr.getAllPlayers();
            
            updatePreviousInfo(tempProfiles);
            
            List<Match> matches = cw.getMatches();
            
            double pointPercentage = 1;
            countAsTourney = countTournamentDialog();
            
            if(!confirmPointsGivenDialog())
                pointPercentage = retrievePointsToBeGiven();
            
            for(PlayerProfile playerProfile : tempProfiles){
                unusedParticipants.add(playerProfile.getPlayersTag());
            }
            
            
        
            for(int i = 0; i < participants.size(); i++){
                if(!newPr.tagTaken(participants.get(i))){
                    ChoiceDialog<String> dialog = new ChoiceDialog<>("",unusedParticipants);
                    dialog.setTitle("Player not found");
                    dialog.setHeaderText(participants.get(i)+ " was not found. Please choose another player tag or enter select cancel to create a new player profile");
                    dialog.setContentText("Choose player tag:");
                    
                    Optional<String> result = dialog.showAndWait();
                    if (result.isPresent() && !result.get().isEmpty()){
                        for(Match match: matches){
                            if(match.getWinner().equalsIgnoreCase(participants.get(i).trim()))
                                match.setWinner(result.get());
                            else if(match.getLoser().equalsIgnoreCase(participants.get(i).trim()))
                                match.setLoser(result.get());
                        }
                        participants.set(i, result.get());
                    }
                    else
                        newPr.addPlayerByTag(participants.get(i));
                }
                    unusedParticipants.remove(participants.get(i));
            } 
            
            EloFormula eloForm = new EloFormula();
            PlayerProfile tempWinner, tempLoser;
            
            for(Match match : matches){
                tempWinner = newPr.getPlayerByTag(match.getWinner());
                tempLoser = newPr.getPlayerByTag(match.getLoser());
                
                eloForm.setWinnerStatus(tempWinner.getPlayersStatus());
                eloForm.setLoserStatus(tempLoser.getPlayersStatus());
                
                eloForm.setWinnerRating(tempWinner.getScore());
                eloForm.setLoserRating(tempLoser.getScore());
                
                eloForm.setWinnerTotalSets(tempWinner.getSetsPlayed());
                eloForm.setLoserTotalSets(tempLoser.getSetsPlayed());
                
                eloForm.setWinnerTourneySetsPlayed(tempWinner.getTourneySetsPlayed());
                eloForm.setLoserTourneySetsPlayed(tempLoser.getTourneySetsPlayed());
                
                eloForm.setPointPercentage(pointPercentage);
                
                eloForm.calculateRating();
                
                System.out.println(tempWinner.getPlayersTag() + " (Winner)Old Rating: " + tempWinner.getScore() + 
                                   " and Total Sets: " + tempWinner.getSetsPlayed());
                System.out.println(tempLoser.getPlayersTag() + " (Loser)Old Rating: " + tempLoser.getScore() + 
                                    " and Total Sets: " + tempLoser.getSetsPlayed());
                
                tempWinner.setScore(eloForm.getWinnerRating());
                tempLoser.setScore(eloForm.getLoserRating());          
                
                tempWinner.incrementTourneySetsPlayed();
                tempLoser.incrementTourneySetsPlayed();
                
                //tempWinner.incrementSetsPlayed();
                //tempLoser.incrementSetsPlayed();
                
                System.out.println(tempWinner.getPlayersTag() + " (Winner)New Rating: " + tempWinner.getScore() + 
                                   " and Total Sets: " + tempWinner.getSetsPlayed());
                System.out.println(tempLoser.getPlayersTag() + " (Loser)New Rating: " + tempLoser.getScore() + 
                                   " and Total Sets: " + tempLoser.getSetsPlayed() + "\n\n");
                
                //System.out.println("\n\n Tournament Type: " + tempTournament.getTournamentType());
                if(countAsTourney){
                    tempLoser.incrementTourneySetsLost();      
                }
            }
            if(countAsTourney){
            	System.out.println("Got to here");
            	if(!newPr.getLastDateEntered().equals(tempTournament.getStartedAt()))
            		newPr.incrementTotalTournaments();

                newPr.setLastDateEntered(tempTournament.getStartedAt());
                newPr.updateTournamentsEntered(participants, tempTournament.getStartedAt());  
            }
            //This is going to have to eventually be changed!
        }
        catch(Exception e){
        	failedURLAlert();
        }  
    }
    
}
