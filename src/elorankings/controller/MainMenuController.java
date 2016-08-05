package elorankings.controller;



import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;



public class MainMenuController {
//Reference to the Main App 
    private MainApp mainApp;
    @FXML
    private Button viewAllPr;
    
    public MainMenuController(){
    }
    
/**
  * Is called by the main application to give a reference back to itself.
  * 
  * @param mainApp
**/
    
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/elorankings/view/MainMenu.fxml"));
        loader.setController(this);
    }
    
/**
 * Called when the user clicks on the delete button.
**/
    @FXML
    private void handleNewPR() {
        mainApp.openNewPRSettings1();
    }
    
    @FXML
    private void openPRList(){
        mainApp.openPRList();
    }
  

}
