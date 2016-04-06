package elorankings.controller;

import elorankings.formula.EloFormula;
import elorankings.model.PRSettings;
import elorankings.model.PlayerProfile;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;


@SuppressWarnings("rawtypes")
public class TwoPlayerRankerController {
	@FXML
    private ComboBox winnerComboBox;
    @FXML
    private ComboBox loserComboBox;
    
    MainApp mainApp;
    PRSettings oldPr;
    PRSettings newPr;
    private List<PlayerProfile> playersList = new ArrayList<PlayerProfile>();
    
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
        playersList = newPr.getAllPlayers();
        
        fillComboBoxes();
    }
    
    @SuppressWarnings("unchecked")
	private void fillComboBoxes(){
        for(PlayerProfile currentPlayer : playersList){
            winnerComboBox.getItems().add(currentPlayer.getPlayersTag());
            loserComboBox.getItems().add(currentPlayer.getPlayersTag());
        }
    }
    
    private boolean selectedPlayersNotSame(){
        String winner = (String) winnerComboBox.getSelectionModel().getSelectedItem();
        String loser = (String) loserComboBox.getSelectionModel().getSelectedItem();
        
        if(winner.equals(loser))
            return false;
        
        return true;
    }
    
    private boolean confirmationDialog(){
        Alert alert = new Alert(AlertType.CONFIRMATION);
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
            //System.out.println("Your choice: " + result.get());
            String tempArr[] = result.get().split("%");
            temp = tempArr[0];
        }
        returnedValue = Double.parseDouble(temp);
        returnedValue = .01*returnedValue;
        
        return returnedValue;
    }
    
    private void calculateNewScore(double pointPercentage){
        EloFormula eloForm = new EloFormula();
        PlayerProfile tempWinner, tempLoser;
        
        tempWinner = newPr.getPlayerByTag((String) winnerComboBox.getSelectionModel().getSelectedItem());
        tempLoser = newPr.getPlayerByTag((String)loserComboBox.getSelectionModel().getSelectedItem());
        
        tempWinner.setPreviousScore(tempWinner.getScore());
        tempLoser.setPreviousScore(tempLoser.getScore());
        
        if(tempWinner.getPlayersStatus().equals("active"))
        	tempWinner.setPreviousRanking(tempWinner.getRanking());
        else
        	tempWinner.setPreviousRanking(-1);
        
        if(tempLoser.getPlayersStatus().equals("active"))
        	tempLoser.setPreviousRanking(tempLoser.getRanking());
        else
        	tempLoser.setPreviousRanking(-1);
        
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
        
        tempWinner.setScore(eloForm.getWinnerRating());
        tempLoser.setScore(eloForm.getLoserRating());   
    }
    
    @FXML
    public void getRanking(){
        double pointPercentage = 1;
        if(selectedPlayersNotSame()){
            if(confirmationDialog())
                calculateNewScore(pointPercentage);
            else{
                pointPercentage = retrieveInput();
                calculateNewScore(pointPercentage);
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Same players selected");
            alert.setHeaderText("The same player has been selected for winner and loser");
            alert.setContentText("Please select different player for either winner or loser");

            alert.showAndWait();
        }
    }
    
    @FXML
    public void backToPROptionsScreen(){
    	mainApp.backToPROptionsScreen(oldPr);
    }
    
    @FXML
    public void saveUpdate(){
    	mainApp.backToPROptionsScreen(newPr);
    }
}
