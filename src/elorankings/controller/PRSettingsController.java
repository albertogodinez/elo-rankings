package elorankings.controller;


import elorankings.model.PRSettings;
import java.io.File;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;


@SuppressWarnings("rawtypes")
public class PRSettingsController {
    @FXML
    private TextField prName;
    @FXML
    private TextField initScore;
    @FXML
    private TextField challongeUsername;
    @FXML
    private TextField challongeApiKey;
	@FXML
    private ComboBox showPlacingDiff;
    @FXML
    private TextField numTourneysNeeded;
    @FXML
    private ComboBox showPointDiff;
    @FXML
    private ComboBox removeInnactive;
    @FXML
    private TextField numSetsNeeded;
    @FXML
    private TextField numTourneysForInnactive;
    @FXML
    private TextField numTourneysForActive;
    @FXML
    private ComboBox implementPointDecay;
    @FXML
    private TextField startOfDecay;
    @FXML
    private CheckBox removeSameCheckBox;
    @FXML
    private CheckBox removeAvgCheckBox;
    @FXML
    private TextField pointsRemoved;
    @FXML
    private Button saveButton;
    @FXML
    private Button backButton;
    
    private MainApp mainApp;
    private String prevMenu = "";
    
    PRSettings newPRSettings;
    
  
    public PRSettingsController(){
    }
    
    
    @SuppressWarnings("unchecked")
	@FXML
    private void initialize(){
        removeInnactive.getSelectionModel().selectedItemProperty().addListener(
            (ObservableValue observableValue, Object oldSelection, Object newSelection) -> {
            if("Yes".equals(newSelection.toString())){
                numTourneysForInnactive.setDisable(false);
                numTourneysForActive.setDisable(false);
                implementPointDecay.setDisable(false);
            }
            else{
                numTourneysForInnactive.setDisable(true);
                numTourneysForActive.setDisable(true);
                implementPointDecay.setDisable(true);
            }
        });
        
        implementPointDecay.getSelectionModel().selectedItemProperty().addListener(
        (ObservableValue observableValue, Object oldSelection, Object newSelection) ->{
            if("Yes".equals(newSelection.toString())){
                startOfDecay.setDisable(false);
                removeSameCheckBox.setDisable(false);
                removeAvgCheckBox.setDisable(false);
            }
            else{
                startOfDecay.setDisable(true);
                removeSameCheckBox.setDisable(true);
                removeAvgCheckBox.setDisable(true);
            }
        });
        
        removeSameCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue){
                if(newValue.booleanValue()){
                    pointsRemoved.setDisable(false);
                    removeAvgCheckBox.setDisable(true);
                }
                else{
                    pointsRemoved.setDisable(true);
                    removeAvgCheckBox.setDisable(false);
                }
            }
        });
        
        removeAvgCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue){
                if(newValue.booleanValue()){
                    //pointsRemoved.setDisable(false);
                    removeSameCheckBox.setDisable(true);
                }
                else{
                    //pointsRemoved.setDisable(true);
                    removeSameCheckBox.setDisable(false);
                }
            }
        });
    }
   
/**
  * Is called by the main application to give a reference back to itself.
  * @param mainApp
  * @param prSettings
**/
    public void setMainApp(MainApp mainApp, PRSettings prSettings) {
        this.mainApp = mainApp;
        newPRSettings = prSettings;
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/elorankings/view/MainMenu.fxml"));
        loader.setController(this);
    }
    
  
    private boolean checkIfEmpty(){
       
        if(prName.getText().trim().length() == 0 || 
           initScore.getText().trim().length() == 0 ||
           challongeUsername.getText().trim().length() == 0 ||
           challongeApiKey.getText().trim().length() == 0 ||
           numTourneysNeeded.getText().trim().length() == 0 ||
           numSetsNeeded.getText().trim().length() == 0 ||
           showPlacingDiff.getSelectionModel().isEmpty() ||
           showPointDiff.getSelectionModel().isEmpty() ||
           removeInnactive.getSelectionModel().isEmpty() || 
           numTourneysForInnactive.getText().trim().length() == 0 ||
           numTourneysForActive.getText().trim().length() == 0 ||
           startOfDecay.getText().trim().length() == 0 ||
           pointsRemoved.getText().trim().length() == 0){
            return false;
        }
        if(!implementPointDecay.disabledProperty().getValue()){
            if(implementPointDecay.getSelectionModel().isEmpty())
                return false;
            else if("Yes".equals(implementPointDecay.getValue().toString())){
                if(!removeSameCheckBox.isSelected() && !removeAvgCheckBox.isSelected())
                    return false;
            }
        }
        return true;
    }
    
    private void loadPRSettings2(){
        //mainApp.updatePR(newPRSettings);
        mainApp.openNewPRSettings2(newPRSettings);
    }

    public void setOldMainApp(MainApp mainApp, PRSettings prSettings, String prevMenu){
        this.prevMenu = prevMenu;
        this.mainApp = mainApp;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/elorankings/view/PRSettings.fxml"));
        loader.setController(this);
        
        newPRSettings = prSettings;
        prName.setText(newPRSettings.getPrName());
        initScore.setText(Integer.toString((int) newPRSettings.getInitScore()));
        challongeUsername.setText(newPRSettings.getChallongeUsername());
        challongeApiKey.setText(newPRSettings.getChallongeApiKey());
        numTourneysNeeded.setText(Integer.toString((int)newPRSettings.getNumTourneysNeeded()));
        numSetsNeeded.setText(Integer.toString((int)newPRSettings.getNumSetsNeeded()));
        
        if(newPRSettings.getShowPlacingDiff())
            showPlacingDiff.getSelectionModel().selectFirst();
        else
            showPlacingDiff.getSelectionModel().selectLast();
        
        if(newPRSettings.getShowPointDiff())
            showPointDiff.getSelectionModel().selectFirst();
        else
            showPointDiff.getSelectionModel().selectLast();
        
        if(newPRSettings.getRemoveInnactive()){
            removeInnactive.getSelectionModel().selectFirst();
            numTourneysForInnactive.setText(Integer.toString(newPRSettings.getNumTourneysForInnactive()));
            numTourneysForActive.setText(Integer.toString(newPRSettings.getNumTourneysForActive()));
            
            if(newPRSettings.getImplementPointDecay()){
                implementPointDecay.getSelectionModel().selectFirst();
                startOfDecay.setText(Integer.toString(newPRSettings.getStartOfDecay()));
                if(newPRSettings.getSamePointsRemoved()){
                    removeSameCheckBox.setSelected(true);
                    pointsRemoved.setText(Integer.toString(newPRSettings.getPointsRemoved()));
                }
                else
                    removeAvgCheckBox.setSelected(true);
                    
            }
            else
                implementPointDecay.getSelectionModel().selectLast();
        }
        else
            removeInnactive.getSelectionModel().selectLast();
        
        //If the previous menu was the options screen, disable and change buttons
        if(prevMenu.equals("optionsScreen")){
            saveButton.setText("Save");
            saveButton.setOnAction((event)->{
                savePRSettings();
                mainApp.openPROptionsScreen(prSettings.getPrName());
            });
            backButton.setOnAction((event)->{
                mainApp.openPROptionsScreen(newPRSettings.getPrName());
            });
        }

    }

    private boolean prNameTaken(){
        String tempName = prName.getText();
        
        for(PRSettings tempPr : mainApp.getPRs()){
            if(tempPr.getPrName().equals(tempName))
                return true;
        }
        
        return false;
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
    private void savePRSettings(){
        boolean prNameTaken = prNameTaken();
        if(checkIfEmpty() && !prNameTaken){
            boolean tempBool;
        
            newPRSettings.setPrName(prName.getText());
            newPRSettings.setInitScore(Double.parseDouble(initScore.getText()));
            newPRSettings.setChallongeUsername(challongeUsername.getText());
            newPRSettings.setChallongeApiKey(challongeApiKey.getText());
            newPRSettings.setNumTourneysNeeded(Integer.parseInt(numTourneysNeeded.getText()));
            newPRSettings.setNumSetsNeeded(Integer.parseInt(numSetsNeeded.getText()));
            
            tempBool = "Yes".equals(showPlacingDiff.getValue().toString());
            newPRSettings.setShowPlacingDiff(tempBool);

            tempBool = "Yes".equals(showPointDiff.getValue().toString());
            newPRSettings.setShowPointDiff(tempBool);

            tempBool = "Yes".equals(removeInnactive.getValue().toString());
            newPRSettings.setRemoveInnactive(tempBool);
            
            if(newPRSettings.getRemoveInnactive()){
                newPRSettings.setNumTourneysForInnactive(Integer.parseInt(numTourneysForInnactive.getText()));
                newPRSettings.setNumTourneysForActive(Integer.parseInt(numTourneysForActive.getText()));
            }
            
            if(!implementPointDecay.disabledProperty().getValue()){
                tempBool = "Yes".equals(implementPointDecay.getValue().toString());
                newPRSettings.setImplementPointDecay(tempBool);
                
                if(newPRSettings.getImplementPointDecay())
                    newPRSettings.setStartOfDecay(Integer.parseInt(startOfDecay.getText()));
                if(removeSameCheckBox.isSelected())
                    newPRSettings.setPointsRemoved(Integer.parseInt(pointsRemoved.getText()));
                else
                    newPRSettings.setPointsRemoved(-1);
            }
            
            File prSettingsFile = mainApp.getPrSettingsFilePath();
            if(prSettingsFile != null){
                mainApp.savePrSettingsDataToFile(prSettingsFile);
            }
            else{
                handleSaveAs();
            }
            //After it is saved, then the second page is loaded*/
            //if it is not loaded from the options screen
            if(!prevMenu.equals("optionsScreen")){
                loadPRSettings2();
            }
        }
        else{
            //Display error message to user
            String title,
                    headerText,
                    contextText;
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            
            if(prNameTaken){
                title = "PR Name Taken";
                headerText = "PR Name '" + prName.getText() + "' is already taken.";
                contextText = "Please enter another name for this PR";
            }
            else{
                title = "Empty Fields";
                headerText = "Certain fields have not been filled out";
                contextText = "Please make sure you fill out all available fields";
            }
                
            alert.setTitle(title);
            alert.setHeaderText(headerText);
            alert.setContentText(contextText);

            alert.showAndWait();
        }
    }
    
     /**
     * Opens a FileChooser to let the user select a file to save to.
     */
    //@FXML
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
}
