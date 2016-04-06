package elorankings.controller;

import java.io.File;

import elorankings.model.PRSettings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;


public class PRListController {
    @FXML
    private ListView<String> prList;
    public static final ObservableList<String> data = 
        FXCollections.observableArrayList();
    
    private ObservableList<PRSettings>allPr;
    
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
        allPr = this.mainApp.getPRs();
        for(PRSettings prSettings : allPr){
            if(!data.contains(prSettings.getPrName())){
                data.add(prSettings.getPrName());
            }
        }
        prList.setItems(data);
    }
    
    private void noSelectionWarning(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("No Power Ranking selected");
        alert.setHeaderText("Please make sure to select a Power Ranking");
        
        alert.showAndWait();    
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
    
/**
 * Called whenever user clicks BACK
 * Loads the main menu
 **/
    @FXML
    private void backToMainMenu(){
        mainApp.showMainMenu();
    }
    
    
    @FXML
    private void openSelectedPr(){
        try{
        	String selectedPr = prList.getSelectionModel().getSelectedItem().toString();
            mainApp.openPROptionsScreen(selectedPr);
        }catch(Exception e){
        	noSelectionWarning();
        }
    }
    
    @FXML
    private void deleteSelectedPr(){
    	try{
            String selectedPr = prList.getSelectionModel().getSelectedItem().toString();
            
            for(int i=0; i < allPr.size(); i++){
            	if(allPr.get(i).getPrName().equals(selectedPr)){
            		allPr.remove(i);
            	}
           
            }
            
            data.remove(selectedPr);
           
            
            
            File prSettingsFile = mainApp.getPrSettingsFilePath();
            if(prSettingsFile != null){
            mainApp.savePrSettingsDataToFile(prSettingsFile);
            }
            else{
                handleSaveAs();
            }
            
            if(allPr.size() <= 0){
            	mainApp.showMainMenu();
            }
    	}catch(Exception e){
    		noSelectionWarning();
    	}
    }
    
    
}
