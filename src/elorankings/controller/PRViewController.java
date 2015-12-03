package elorankings.controller;

import elorankings.model.PRSettings;
import elorankings.model.PlayerProfile;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;

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
    
    public void showPRs(){
        allData.clear();
        activeData.clear();
        inactiveData.clear();
        unratedData.clear();
        //showActivePlayers(pr, playersList);
        activePlayersList.setEditable(true);
        inactivePlayersList.setEditable(true);
        unratedPlayersList.setEditable(true);
        allPlayersList.setEditable(true);
        
        int i =1;
        for(PlayerProfile player : playersList){
            //if(!data.contains(player.getPlayersTag())){
                allData.add(i + ". " + player.getPlayersTag() + "\t\t" + player.getScore());
                
                if("active".equals(player.getPlayersStatus()))
                    activeData.add(player.getRanking() + ". " + player.getPlayersTag() + "\t\t" + player.getScore());
                
                if("inactive".equals(player.getPlayersStatus()))
                    inactiveData.add(i + ". " + player.getPlayersTag() + "\t\t" + player.getScore());
                
                if("unrated".equals(player.getPlayersStatus()))
                    unratedData.add(i + ". " + player.getPlayersTag() + "\t\t" + player.getScore());
                i++;
            //}
        }
        
        allPlayersList.setItems(allData);
        activePlayersList.setItems(activeData);
        inactivePlayersList.setItems(inactiveData);
        unratedPlayersList.setItems(unratedData);
        
    }
    
    /*public void refreshActiveList(){
        allData.clear();
        activeData.clear();
        
        int i =1;
        for(PlayerProfile player : playersList){
            allData.add(i + ". " + player.getPlayersTag() + "\t\t" + player.getScore());
            if("active".equals(player.getPlayersStatus()))
                activeData.add(player.getRanking() + ". " + player.getPlayersTag() + "\t\t" + player.getScore());
        }
        allPlayersList.setItems(allData);
        activePlayersList.setItems(activeData);
    }
    
    public void refreshInactivePlayers(){
        inactiveData.clear();
        allData.clear();
        
        int i =1;
        for(PlayerProfile player : playersList){
            allData.add(i + ". " + player.getPlayersTag() + "\t\t" + player.getScore());
            if("inactive".equals(player.getPlayersStatus()))
                inactiveData.add(player.getRanking() + ". " + player.getPlayersTag() + "\t\t" + player.getScore());
        }
        
    }*/
    
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
        //System.out.println(selectedPlayer);
        return selectedPlayer;
    }
    
    private String deleteSelectedPlayer(){
        String selectedPlayer = null;
        if("activePlayers".equals(playersTab.getSelectionModel().getSelectedItem().getId())){
            selectedPlayer = activePlayersList.getSelectionModel().getSelectedItem().toString();
            activePlayersList.getItems().remove(activePlayersList.getSelectionModel().getSelectedItem());
            //showPRs();
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

    /* TODO: Remember to save the PR back to the original list of PRs in
    ** MainApp Object
    */
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
           selectedPlayer = selectedPlayer.substring(selectedPlayer.indexOf(" ") + 1, selectedPlayer.indexOf("\t"));
           pr.deletePlayerByTag(selectedPlayer);
           showPRs();
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
            //alert.setContentText("Please enter another tag for this user");

            alert.showAndWait();            
        }
        else{
            selectedPlayer = selectedPlayer.substring(selectedPlayer.indexOf(" ") + 1, selectedPlayer.indexOf("\t"));
            PlayerProfile playerProfile = pr.getPlayerByTag(selectedPlayer);
            mainApp.openPlayerProfile(prName.getText(), playerProfile);
        }
    }
}
