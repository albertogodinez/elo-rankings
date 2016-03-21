package elorankings.controller;

import elorankings.model.PRSettings;
import elorankings.model.PlayerProfile;
import java.io.File;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;


public class PlayerProfileController {
    @FXML
    private TextField playersTag;
    @FXML
    private Label totalTournaments;
    @SuppressWarnings("rawtypes")
	@FXML
    private ComboBox playersStatus;
    @FXML
    private Label lastDateEntered;
    @FXML
    private TextField playersScore;
    
    MainApp mainApp;
    PlayerProfile playerProfile;
    private PRSettings pr;
    private boolean isNewPlayer;
    
    /**
  * Is called by the main application to give a reference back to itself.
  * 
  * @param mainApp
     * @param prName
     * @param playerProfile
**/
    public void setMainApp(MainApp mainApp, PRSettings pr, PlayerProfile playerProfile) {
        this.mainApp = mainApp;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/elorankings/view/MainMenu.fxml"));
        loader.setController(this);
        
        //Used to find the corresponding Power Ranking that contains player
        this.pr = pr;
        
        if(playerProfile != null){
            this.playerProfile = playerProfile;
            isNewPlayer = false;
        }
        else
            isNewPlayer = true;
        
        
        fillProfile();
        
        
            
    }
    
    @SuppressWarnings("unchecked")
	private void fillProfile(){
        if(!isNewPlayer){
            playersTag.setText(playerProfile.getPlayersTag());
            playersScore.setText(Double.toString(playerProfile.getScore()));
            System.out.println("Tourneys entered: " + playerProfile.getTourneysEntered());
            totalTournaments.setText(Integer.toString(playerProfile.getTourneysEntered()));
            
            if(playerProfile.getLastDateEntered() != null && playerProfile.getLastDateEntered().length()>0)
                lastDateEntered.setText(playerProfile.getLastDateEntered().toString());
            else 
                lastDateEntered.setText("Player has not entered any tournaments.");
            
            playersStatus.getSelectionModel().select(playerProfile.getPlayersStatus());
        }
        else{
            playersScore.setText(Double.toString(pr.getInitScore()));
            totalTournaments.setText("0");
            lastDateEntered.setText("Player has not entered any tournaments");
            playersStatus.getSelectionModel().select("unrated");
        }   
    }
    
    private boolean checkIfLabelsEmpty(){
        if(playersTag.getText() == null || playersTag.getText().trim().isEmpty() ||
           playersScore.getText() == null || playersScore.getText().trim().isEmpty())
                return true;
        
        return false;
        
    }
    
    private boolean isNewNameTaken(){
        for(PlayerProfile tempPlayerProfile : pr.getAllPlayers()){
            if(!isNewPlayer){
                if(tempPlayerProfile.getPlayerId() != playerProfile.getPlayerId() && 
                   playersTag.getText().equalsIgnoreCase(tempPlayerProfile.getPlayersTag()))
                    return true;
            }
            else{
                if(tempPlayerProfile.getPlayersTag().equalsIgnoreCase(playersTag.getText()))
                    return true;
            }
                
        }
        return false;
    }
    
    @FXML
    public void savePlayerInfo(){
        
        if(!checkIfLabelsEmpty() && !isNewNameTaken()){
            if(!isNewPlayer){
                playerProfile.setPlayersTag(playersTag.getText());
                playerProfile.setScore(Double.parseDouble(playersScore.getText()));
                playerProfile.setPlayersStatus(playersStatus.getSelectionModel().getSelectedItem().toString());
            }
            else {
                PlayerProfile tempProfile = new PlayerProfile(playersTag.getText(),
                                                              Double.parseDouble(playersScore.getText()),
                                                               -1);
                tempProfile.setPlayersStatus(playersStatus.getSelectionModel().getSelectedItem().toString());
                pr.addPlayerByObject(tempProfile);
            }
            
            File prSettingsFile = mainApp.getPrSettingsFilePath();
            if(prSettingsFile != null){
                mainApp.savePrSettingsDataToFile(prSettingsFile);
            }
            else{
                handleSaveAs();
            }
            
            pr.sortByScore();
            mainApp.openPRView(pr.getPrName());
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Empty Text Fields");
            alert.setHeaderText("Please make sure all text fields are filled out.");
            //alert.setContentText("Please enter another tag for this user");

            alert.showAndWait();   
        }
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
    public void backToPlayerList(){
        mainApp.openPRView(pr.getPrName());
    }
}
