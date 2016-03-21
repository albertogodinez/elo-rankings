package elorankings.controller;

import elorankings.model.PRSettings;
import java.io.File;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;



public class PRSettingsController2 {
    @FXML
    private TextArea playertagsBulk;
    
    PRSettings prSettings;
    private MainApp mainApp;
    
    public PRSettingsController2(){
    }
    

/**
  * Is called by the main application to give a reference back to itself.
  * @param mainApp
  * @param prSettings
**/
    public void setMainApp(MainApp mainApp, PRSettings prSettings){
        this.mainApp = mainApp;
        this.prSettings = prSettings;
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/elorankings/view/PRSettings2.fxml"));
        loader.setController(this);    
    }
    
    @FXML
    private void savePlayers(){
        prSettings.sortByTag();
        boolean addSuccessful = true;
        
        for(String line : playertagsBulk.getText().split("\n")){
            if(!line.isEmpty())
                addSuccessful = prSettings.addPlayerByTag(line);
            if(!addSuccessful){
                Alert alert = new Alert(AlertType.WARNING);
                alert.initOwner(mainApp.getPrimaryStage());
                alert.setTitle("PlayerTag Taken");
                alert.setHeaderText("PlayerTag " + line +" is already taken");
                alert.setContentText("Please enter another tag for this user");

                alert.showAndWait();
            }
        }
        if(addSuccessful){
            File prSettingsFile = mainApp.getPrSettingsFilePath();
            if(prSettingsFile != null){
                mainApp.savePrSettingsDataToFile(prSettingsFile);
            }
            else{
                System.out.println("No file exists");
            }
            prSettings.printAllPlayers();
            mainApp.showMainMenu();
        }
    }
    
    @SuppressWarnings("unused")
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
    private void backToPRSettings1(){
        mainApp.backToPRSettings1(prSettings, "prSettings2");
    }
    
}
