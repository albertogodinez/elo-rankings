package elorankings.controller;


import elorankings.model.PRSettings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class PRSettingsController {
    @FXML
    private TextField prName;
    @FXML
    private TextField initScore;
    @FXML
    private ComboBox showPlacingDiff;
    @FXML
    private ComboBox showPointDiff;
    @FXML
    private ComboBox removeInnactive;
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
    private Label errorLabel;
    
    private MainApp mainApp;
    
    PRSettings newPRSettings;
    
  
    public PRSettingsController(){
    }
    
    
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
   
    private boolean checkIfEmpty(){
       
        if(prName.getText().trim().length() == 0 || 
           initScore.getText().trim().length() == 0 ||
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
        mainApp.updatePR(newPRSettings);
        mainApp.openNewPRSettings2(newPRSettings);
    }
/**
  * Is called by the main application to give a reference back to itself.
  * @param mainApp
**/
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/elorankings/view/PRSettings.fxml"));
        loader.setController(this);
    }
    
    public void setOldMainApp(MainApp mainApp, PRSettings prSettings){
        this.mainApp = mainApp;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/elorankings/view/PRSettings.fxml"));
        loader.setController(this);
        
        newPRSettings = prSettings;
        prName.setText(newPRSettings.getPrName());
        initScore.setText(Integer.toString((int) newPRSettings.getInitScore()));
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
        if(checkIfEmpty()){
            newPRSettings = new PRSettings();
            boolean tempBool;
        
            newPRSettings.setPrName(prName.getText());
            
        
            newPRSettings.setInitScore(Integer.parseInt(initScore.getText()));

            tempBool = "Yes".equals(showPlacingDiff.getValue().toString());
            newPRSettings.setShowPlacingDiff(tempBool);

            tempBool = "Yes".equals(showPointDiff.getValue().toString());
            newPRSettings.setShowPointDiff(tempBool);

            tempBool = "Yes".equals(removeInnactive.getValue().toString());
            newPRSettings.setRemoveInnactive(tempBool);
            
            //I'm pretty sure I'm checking for this twice, so check later to make sure i'm not
            if(!numTourneysForInnactive.disabledProperty().getValue() && !numTourneysForActive.disabledProperty().getValue()){
                newPRSettings.setNumTourneysForActive(Integer.parseInt(numTourneysForInnactive.getText()));
                newPRSettings.setNumTourneysForActive(Integer.parseInt(numTourneysForActive.getText()));
            }
            
            if(!implementPointDecay.disabledProperty().getValue()){
                tempBool = "Yes".equals(implementPointDecay.getValue().toString());
                newPRSettings.setImplementPointDecay(tempBool);

                newPRSettings.setStartOfDecay(Integer.parseInt(startOfDecay.getText()));
                if(removeSameCheckBox.isSelected())
                    newPRSettings.setPointsRemoved(Integer.parseInt(pointsRemoved.getText()));
                else
                    newPRSettings.setPointsRemoved(-1);
            }
            //After it is saved, then the second page is loaded*/
            loadPRSettings2();
        }
        else{
            //Display error message to user
            errorLabel.setText("Please fill out all of the form");
            //System.out.print("There are empty textfields");
        }
    }
}
