package elorankings.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;

public class PROptionsScreenController {
    @FXML
    private Label prName;
    MainApp mainApp;
    
/**
  * Is called by the main application to give a reference back to itself.
  * 
  * @param mainApp
**/
    public void setMainApp(MainApp mainApp, String prName) {
        this.mainApp = mainApp;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/elorankings/view/MainMenu.fxml"));
        loader.setController(this);
        
        this.prName.setText(prName);
    }
    
    @FXML
    public void viewPowerRanking(){
        mainApp.openPRView(prName.getText());
    }
    
    @FXML
    public void addNewTournament(){
        mainApp.openAddTournamentScreen(mainApp.getAPr(prName.getText()));
    }
    
    @FXML
    public void editSettings(){
        mainApp.backToPRSettings1(mainApp.getAPr(prName.getText()), "optionsScreen");
    }
    
    @FXML
    public void backToPRList(){
        mainApp.openPRList();
    }
    
}
