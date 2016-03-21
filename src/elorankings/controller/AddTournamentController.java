package elorankings.controller;

import challongeapi.ChallongeWrapper;
import challongeapi.pojoclasses.Match;
import challongeapi.pojoclasses.Tournament;
import elorankings.formula.EloFormula;
import elorankings.model.PRSettings;
import elorankings.model.PlayerProfile;
import java.io.File;
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
import javafx.stage.FileChooser;

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
    PRSettings pr;
    
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
        
        this.pr = pr;
    }
    
    private boolean confirmationDialog(){
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
    
    private double retrieveInput(){
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
        		+ "\nPlease enter another Challonge URL.");

        alert.showAndWait();
    }
    
    @FXML
    public void backToPROptionsScreen(){
        File prSettingsFile = mainApp.getPrSettingsFilePath();
            if(prSettingsFile != null){
                mainApp.savePrSettingsDataToFile(prSettingsFile);
            }
            else{
                handleSaveAs();
            }
            
        mainApp.openPROptionsScreen(pr.getPrName());
    }
    
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
       
        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            mainApp.savePrSettingsDataToFile(file);
        }
    }
    
    @FXML
    public void addTournament(){
        ChallongeWrapper cw = new ChallongeWrapper();
        
        try{
        	cw.runUrl(pr.getChallongeUsername(), pr.getChallongeApiKey(), challongeLink.getText());
        	
        	Tournament tempTournament = cw.getTournamentInfo();
            List<String> participants =  cw.getParticipants();
            List<String> unusedParticipants = new ArrayList<>();
            pr.sortByTag();
            List<PlayerProfile> tempProfiles = pr.getAllPlayers();
            List<Match> matches = cw.getMatches();
            
            double pointPercentage = 1;
            if(!confirmationDialog())
                pointPercentage = retrieveInput();
            
            for(PlayerProfile playerProfile : tempProfiles){
                unusedParticipants.add(playerProfile.getPlayersTag());
            }
        
            for(int i = 0; i < participants.size(); i++){
                if(!pr.tagTaken(participants.get(i))){
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
                        pr.addPlayerByTag(participants.get(i));
                }
                    unusedParticipants.remove(participants.get(i));
            } 
            
            EloFormula eloForm = new EloFormula();
            PlayerProfile tempWinner, tempLoser;
            
            for(Match match : matches){
                tempWinner = pr.getPlayerByTag(match.getWinner());
                tempLoser = pr.getPlayerByTag(match.getLoser());
                
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
                if(pointPercentage==1){
                    tempLoser.incrementTourneySetsLost();      
                    System.out.println("this is NOT swiss or round robin!");
                }
            }
            
            if(pointPercentage==1){
                pr.incrementTotalTournaments();
                pr.updateTournamentsEntered(participants, tempTournament.getStartedAt());
            }
        }
        catch(Exception e){
        	failedURLAlert();
        }  
    }
    
}
