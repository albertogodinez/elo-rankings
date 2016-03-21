package elorankings.controller;

import elorankings.formula.DecimalUtils;
import elorankings.model.PRSettings;
import elorankings.model.PlayerProfile;
import java.io.File;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.FileChooser;

@SuppressWarnings("rawtypes")
public class PRViewController {
    @FXML
    private Label prName;
	@FXML
    private ListView allPlayersList;
    @FXML
    private ListView activePlayersList;
    @FXML
    private ListView inactivePlayersList;
    @FXML
    private ListView unratedPlayersList;
    @FXML
    private TabPane playersTab;
    
    private MainApp mainApp;
    private List<PlayerProfile> playersList;
    private PRSettings pr;
    
    public static final ObservableList allData = 
        FXCollections.observableArrayList();
    public static final ObservableList activeData = 
        FXCollections.observableArrayList();
    public static final ObservableList inactiveData = 
        FXCollections.observableArrayList();
    public static final ObservableList unratedData = 
        FXCollections.observableArrayList();
    
/**
  * Is called by the main application to give a reference back to itself.
  * 
  * @param mainApp
  * @param prName
**/
    public void setMainApp(MainApp mainApp, String prName) {
        this.mainApp = mainApp;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/elorankings/view/MainMenu.fxml"));
        loader.setController(this);
        
        this.prName.setText(prName);
        
        pr = mainApp.getAPr(this.prName.getText());
        pr.sortByScore();
        playersList = pr.getAllPlayers();
        
        showPRs();
    }
    
    @SuppressWarnings("unchecked")
	public void showPRs(){
        allData.clear();
        activeData.clear();
        inactiveData.clear();
        unratedData.clear();
        activePlayersList.setEditable(true);
        inactivePlayersList.setEditable(true);
        unratedPlayersList.setEditable(true);
        allPlayersList.setEditable(true);
        
        int i =1, innactiveNum=1, unratedNum = 1;
        double roundedScore;
        String formatedOutput;
        for(PlayerProfile player : playersList){
            roundedScore = DecimalUtils.round(player.getScore(), 2);
            formatedOutput = String.format("%1$-20s %2$10s", i + ". " + player.getPlayersTag() + " ",roundedScore);
            
            allData.add(formatedOutput);

            if("active".equals(player.getPlayersStatus())){
                formatedOutput = String.format("%1$-20s %2$10s", player.getRanking() + ". " + player.getPlayersTag() + " ",roundedScore);
                activeData.add(formatedOutput);
            }
            
            if("inactive".equals(player.getPlayersStatus())){
                formatedOutput = String.format("%1$-20s %2$10s", innactiveNum + ". " + player.getPlayersTag() + " ",roundedScore);
                inactiveData.add(formatedOutput);
                innactiveNum++;
            }

            if("unrated".equals(player.getPlayersStatus())){
                formatedOutput = String.format("%1$-20s %2$10s", unratedNum + ". " + player.getPlayersTag() + " ",roundedScore);
                unratedData.add(formatedOutput);
                unratedNum++;
            }
                
            i++;
        }
        
        allPlayersList.setItems(allData);
        activePlayersList.setItems(activeData);
        inactivePlayersList.setItems(inactiveData);
        unratedPlayersList.setItems(unratedData);
        
    }
    
    private String getSelectedPlayer(){
        String selectedPlayer = null;
        if("activePlayers".equals(playersTab.getSelectionModel().getSelectedItem().getId()))
            selectedPlayer = activePlayersList.getSelectionModel().getSelectedItem().toString();
        else if("allPlayers".equals(playersTab.getSelectionModel().getSelectedItem().getId()))
            selectedPlayer = allPlayersList.getSelectionModel().getSelectedItem().toString();
        else if("inactivePlayers".equals(playersTab.getSelectionModel().getSelectedItem().getId()))
            selectedPlayer = inactivePlayersList.getSelectionModel().getSelectedItem().toString();
        else if("unratedPlayers".equals(playersTab.getSelectionModel().getSelectedItem().getId()))
            selectedPlayer = unratedPlayersList.getSelectionModel().getSelectedItem().toString();
        return selectedPlayer;
    }
    
    private String deleteSelectedPlayer(){
        String selectedPlayer = null;
        if("activePlayers".equals(playersTab.getSelectionModel().getSelectedItem().getId())){
            selectedPlayer = activePlayersList.getSelectionModel().getSelectedItem().toString();
            activePlayersList.getItems().remove(activePlayersList.getSelectionModel().getSelectedItem());
        }
        else if("allPlayers".equals(playersTab.getSelectionModel().getSelectedItem().getId())){
            selectedPlayer = allPlayersList.getSelectionModel().getSelectedItem().toString();
            allPlayersList.getItems().remove(allPlayersList.getSelectionModel().getSelectedItem());
        }
        else if("inactivePlayers".equals(playersTab.getSelectionModel().getSelectedItem().getId())){
            selectedPlayer = inactivePlayersList.getSelectionModel().getSelectedItem().toString();
            inactivePlayersList.getItems().remove(inactivePlayersList.getSelectionModel().getSelectedItem());
        }
        else if("unratedPlayers".equals(playersTab.getSelectionModel().getSelectedItem().getId())){
            selectedPlayer = unratedPlayersList.getSelectionModel().getSelectedItem().toString();
            unratedPlayersList.getItems().remove(unratedPlayersList.getSelectionModel().getSelectedItem());
        }
        return selectedPlayer;
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
    public void backToPROptions(){
        mainApp.openPROptionsScreen(prName.getText());
    }
    
    @FXML
    public void addNewPlayer(){
        mainApp.openPlayerProfile(prName.getText(), null);
    }
    
    @FXML
    public void deletePlayer(){
        String selectedPlayer = deleteSelectedPlayer();
        if(selectedPlayer == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No player selected");
            alert.setHeaderText("Please select a player that you would like to delete");
            
            alert.showAndWait();
        }
        else{
            selectedPlayer = selectedPlayer.substring(selectedPlayer.indexOf(" ") + 1);
            selectedPlayer = selectedPlayer.substring(0, selectedPlayer.indexOf(" "));
            pr.deletePlayerByTag(selectedPlayer);
           
            File prSettingsFile = mainApp.getPrSettingsFilePath();
            if(prSettingsFile != null){
            mainApp.savePrSettingsDataToFile(prSettingsFile);
            }
            else{
                handleSaveAs();
            }
            showPRs();
        }       
    }
    
    @FXML
    public void copyListToClipboard(){
        ObservableList tempList;
        String selectedPlayerList = "";
        int iterator = 0;
        if("activePlayers".equals(playersTab.getSelectionModel().getSelectedItem().getId())){
            tempList = activePlayersList.getItems();
            while(iterator < tempList.size()){
                selectedPlayerList += tempList.get(iterator).toString() + "\n";
                iterator++;
            }
        }
        else if("allPlayers".equals(playersTab.getSelectionModel().getSelectedItem().getId())){
            tempList = allPlayersList.getItems();
            while(iterator < tempList.size()){
                selectedPlayerList += tempList.get(iterator).toString() + "\n";
                iterator++;
            }
        }
        else if("inactivePlayers".equals(playersTab.getSelectionModel().getSelectedItem().getId())){
            tempList = inactivePlayersList.getItems();
            while(iterator < tempList.size()){
                selectedPlayerList += tempList.get(iterator).toString() + "\n";
                iterator++;
            }
        }
        else if("unratedPlayers".equals(playersTab.getSelectionModel().getSelectedItem().getId())){
            tempList = unratedPlayersList.getItems();
            while(iterator < tempList.size()){
                selectedPlayerList += tempList.get(iterator).toString() + "\n";
                iterator++;
            }
        }
        
        if(selectedPlayerList!=null || !selectedPlayerList.equals("")){
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(selectedPlayerList);
            clipboard.setContent(content);
        }
    }
    
    @FXML
    public void editPlayer(){
        String selectedPlayer = getSelectedPlayer();
        if(selectedPlayer == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No player selected");
            alert.setHeaderText("Please make sure to select a player");
            
            alert.showAndWait();            
        }
        else{
            selectedPlayer = selectedPlayer.substring(selectedPlayer.indexOf(" ") + 1);
            selectedPlayer = selectedPlayer.substring(0, selectedPlayer.indexOf(" "));
            PlayerProfile playerProfile = pr.getPlayerByTag(selectedPlayer);
            mainApp.openPlayerProfile(prName.getText(), playerProfile);
        }
    }
}
