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
    
    PRSettings newPr;
    
  
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
                pointsRemoved.setDisable(true);
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
                    removeSameCheckBox.setDisable(true);
                }
                else{
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
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        newPr = new PRSettings();
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/elorankings/view/MainMenu.fxml"));
        loader.setController(this);
    }
    
  
    private boolean checkIfEmpty(){
       //The following check to make sure TextFields are filled out
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
            return true;
        }
        if(!implementPointDecay.disabledProperty().getValue()){
            if(implementPointDecay.getSelectionModel().isEmpty())
                return true;
            else if("Yes".equals(implementPointDecay.getValue().toString())){
                if(!removeSameCheckBox.isSelected() && !removeAvgCheckBox.isSelected())
                    return true;
            }
        }
        
        //The following check to see if ComboBoxes are filled out
        if("SELECT".equals(removeInnactive.getValue().toString()) ||
        	"SELECT".equals(showPlacingDiff.getValue().toString())||
        	"SELECT".equals(showPointDiff.getValue().toString())){
        	return true;
        }
        if(!implementPointDecay.disabledProperty().getValue()){
        	if("SELECT".equals(implementPointDecay.getValue().toString())){
        		return true;
        	}
        }

        return false;
    }
    
    private void loadPRSettings2(){
    	mainApp.getPRs().add(newPr);
        mainApp.openNewPRSettings2(newPr);
    }

    public void setOldMainApp(MainApp mainApp, PRSettings prSettings, String prevMenu){
        this.prevMenu = prevMenu;
        this.mainApp = mainApp;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/elorankings/view/PRSettings.fxml"));
        loader.setController(this);
        
        newPr = prSettings;
        prName.setText(newPr.getPrName());
        initScore.setText(Integer.toString((int) newPr.getInitScore()));
        challongeUsername.setText(newPr.getChallongeUsername());
        challongeApiKey.setText(newPr.getChallongeApiKey());
        numTourneysNeeded.setText(Integer.toString((int)newPr.getNumTourneysNeeded()));
        numSetsNeeded.setText(Integer.toString((int)newPr.getNumSetsNeeded()));
        
        if(newPr.getShowPlacingDiff())
            showPlacingDiff.getSelectionModel().selectFirst();
        else
            showPlacingDiff.getSelectionModel().selectLast();
        
        if(newPr.getShowPointDiff())
            showPointDiff.getSelectionModel().selectFirst();
        else
            showPointDiff.getSelectionModel().selectLast();
        
        if(newPr.getRemoveInactive()){
            removeInnactive.getSelectionModel().selectFirst();
            numTourneysForInnactive.setText(Integer.toString(newPr.getNumTourneysForInactive()));
            numTourneysForActive.setText(Integer.toString(newPr.getNumTourneysForActive()));
            
            if(newPr.getImplementPointDecay()){
                implementPointDecay.getSelectionModel().selectFirst();
                startOfDecay.setText(Integer.toString(newPr.getStartOfDecay()));
                if(newPr.getSamePointsRemoved()){
                    removeSameCheckBox.setSelected(true);
                    pointsRemoved.setText(Integer.toString(newPr.getPointsRemoved()));
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
                mainApp.openPROptionsScreen(newPr.getPrName());
            });
        }

    }
    
    private boolean isNumber(){
    	try {
    	    double d = Double.parseDouble(initScore.getText());
    	    d = Double.parseDouble(numTourneysNeeded.getText());
    	    d = Double.parseDouble(numSetsNeeded.getText());
    	    

            if("Yes".equals(removeInnactive.getValue().toString())){
            	d = Double.parseDouble(numTourneysForInnactive.getText());
            	d = Double.parseDouble(numTourneysForActive.getText());
            }
            if(!implementPointDecay.disabledProperty().getValue() && "Yes".equals(implementPointDecay.getValue().toString())){
                /*if(newPr.getImplementPointDecay())*/
                    d = Double.parseDouble((startOfDecay.getText()));
                    if(removeSameCheckBox.isSelected())
                        d = Double.parseDouble((pointsRemoved.getText()));
            }
            
            
    	    // string is a number
    	    return true;
    	} catch (NumberFormatException e) {
    	    // string is not a number
    		return false;
 	
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
    	boolean prNameTaken, isNumber=true,isEmpty;
    	//This if/else is done in order to prevent app from thinking that
    	//name is taken whenever it is opened from options screen
    	if(!prevMenu.equals("optionsScreen") && !prevMenu.equals("prSettings2"))
            prNameTaken = prNameTaken();
    	else
    		prNameTaken = false;
    	
    	isEmpty = checkIfEmpty();
    	
    	if(!isEmpty)
    		isNumber = isNumber();
    	
        if(!checkIfEmpty() && !prNameTaken && isNumber){
            boolean tempBool;
        
            newPr.setPrName(prName.getText());
            newPr.setInitScore(Double.parseDouble(initScore.getText()));
            newPr.setChallongeUsername(challongeUsername.getText());
            newPr.setChallongeApiKey(challongeApiKey.getText());
            newPr.setNumTourneysNeeded(Integer.parseInt(numTourneysNeeded.getText()));
            newPr.setNumSetsNeeded(Integer.parseInt(numSetsNeeded.getText()));
            
            tempBool = "Yes".equals(showPlacingDiff.getValue().toString());
            newPr.setShowPlacingDiff(tempBool);

            tempBool = "Yes".equals(showPointDiff.getValue().toString());
            newPr.setShowPointDiff(tempBool);

            tempBool = "Yes".equals(removeInnactive.getValue().toString());
            newPr.setRemoveInactive(tempBool);
            
            if(newPr.getRemoveInactive()){
                newPr.setNumTourneysForInactive(Integer.parseInt(numTourneysForInnactive.getText()));
                newPr.setNumTourneysForActive(Integer.parseInt(numTourneysForActive.getText()));
            }
            
            //tempBool = "Yes".equals(implementPointDecay.getValue().toString());
            
            if(!implementPointDecay.disabledProperty().getValue() && 
               "Yes".equals(implementPointDecay.getValue().toString())){
                newPr.setImplementPointDecay(tempBool);
                
                if(newPr.getImplementPointDecay())
                    newPr.setStartOfDecay(Integer.parseInt(startOfDecay.getText()));
                if(removeSameCheckBox.isSelected())
                    newPr.setPointsRemoved(Integer.parseInt(pointsRemoved.getText()));
                else
                    newPr.setPointsRemoved(Integer.parseInt(pointsRemoved.getText()));
            }

        	if(prevMenu.equals("optionsScreen")){
                File prSettingsFile = mainApp.getPrSettingsFilePath();
                if(prSettingsFile != null){
                    mainApp.savePrSettingsDataToFile(prSettingsFile);
                }
                else{
                    handleSaveAs();
                }
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
            else if(!isNumber){
            	title = "Number was not entered";
            	headerText = "One of the fields does not contain a number";
            	contextText = "Please enter numbers in the required fields.";
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
