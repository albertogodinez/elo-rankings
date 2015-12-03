package elorankings.controller;

import elorankings.model.PRSettings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;


public class PRListController {
    @FXML
    private ListView prList;
    public static final ObservableList data = 
        FXCollections.observableArrayList();
    
    private ObservableList<PRSettings>tournaments;
    
    private MainApp mainApp;
    
    public PRListController(){ 
    }
    
/**
  * Is called by the main application to give a reference back to itself.
  * @param mainApp
**/ 
    public void setMainApp(MainApp mainApp){
        this.mainApp = mainApp;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/elorankings/view/MainMenu.fxml"));
        loader.setController(this);
        
        data.clear();
        prList.setEditable(true);      
        tournaments = this.mainApp.getPRs();
        for(PRSettings prSettings : tournaments){
            if(!data.contains(prSettings.getPrName()))
                data.add(prSettings.getPrName());
        }
        prList.setItems(data);
    }
    
/**
 * Called whenever user clicks BACK
 * Loads the main menu
 **/
    @FXML
    private void backToMainMenu(){
        mainApp.showMainMenu();
    }
    
    
    @FXML
    private void goToSelectedTournament(){
        String selectedPr = prList.getSelectionModel().getSelectedItem().toString();
        mainApp.openPROptionsScreen(selectedPr);
    }
}
