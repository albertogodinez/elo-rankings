/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elorankings.controller;

import elorankings.model.PRSettings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

/**
 *
 * @author Alberto
 */
public class PRSettingsController2 {
    @FXML
    private TextField playerTag;
    @FXML 
    private TextField playerScore;
    @FXML
    private Button addNewPlayer;
    @FXML
    private GridPane playersGrid;
    @FXML
    private Label addPlayersInfo;
    @FXML
    private Tab addBulkTag;
    @FXML
    private Tab addIndivTag;
    @FXML
    private TextArea playertagsBulk;
    
    PRSettings prSettings;
    private MainApp mainApp;
    private int currentRow=1;
    private Button deletePlayerButton;
    
    public PRSettingsController2(){
    }
    

    
    public void setMainApp(MainApp mainApp, PRSettings prSettings){
        this.mainApp = mainApp;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/elorankings/view/MainMenu.fxml"));
        loader.setController(this);
        
        this.prSettings = prSettings;
    }
    
    @FXML
    private void addNewPlayer(){
        
        TextField playerTagField = new TextField();
        playerTagField.setMaxSize(playerTag.getMaxWidth(), playerTag.getMaxHeight());
        playerTagField.setPrefWidth(playerTag.getPrefWidth());
        playerTagField.setPrefHeight(playerTag.getPrefHeight());
        
        TextField playerScoreField = new TextField();
        playerScoreField.setMaxSize(playerTag.getMaxWidth(), playerTag.getMaxHeight());
        playerScoreField.setPrefWidth(playerTag.getPrefWidth());
        playerScoreField.setPrefHeight(playerTag.getPrefHeight());
        
       
        Button addPlayerButton = addNewPlayer;
        playersGrid.getChildren().remove(addNewPlayer);
        
        deletePlayerButton = new Button();
        deletePlayerButton.setMaxSize(addNewPlayer.getMaxWidth(), addNewPlayer.getMaxWidth());
        deletePlayerButton.setPrefWidth(addNewPlayer.getPrefWidth());
        deletePlayerButton.setPrefHeight(addNewPlayer.getPrefHeight());
        deletePlayerButton.setText("-");
        deletePlayerButton.setStyle("-fx-font-size: 16px;");
        deletePlayerButton.setStyle("-fx-font-weight: bold;");
        deletePlayerButton.setId("deletePlayer");
        
      //  deletePlayerButton.setOnAction((event)->{
        //    playersGrid.getChildren().s
       // });
        
        
 
        RowConstraints row = new RowConstraints(40);
        
        playersGrid.getRowConstraints().add(row);
        playersGrid.add(playerTagField, 0, currentRow);
        playersGrid.add(playerScoreField, 1, currentRow);
        playersGrid.add(addPlayerButton, 2, currentRow);
        playersGrid.add(deletePlayerButton, 2, currentRow-1);
        
        currentRow++;
    }
    
    @FXML
    private void changeToBulkLabel(){
        addPlayersInfo.setText("Add 1 player per line and to add player ranking, seperate them with comma (no space after comma)"
                + "\n\nExample:"
                + "\nPlayers Tag,1600"
                + "\n\nOR"
                + "\n\nPlayers Tag");
      
    }
    
    @FXML
    private void changeToIndivLabel(){
//        addPlayersInfo.setText("Add Players Individually by providing a tag name and initial ranking.  If no Initial ranking is provided, then player will have the initial ranking that was set on the previous page.");
    }
    
    @FXML
    private void savePlayers(){
        //String temp = playertagsBulk.getText();
        for(String line : playertagsBulk.getText().split("\n")){
            //if(!prSettings.tagTaken(line)){
            //    System.out.print("player added!");
                if(!prSettings.addPlayer(line)){
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.initOwner(mainApp.getPrimaryStage());
                    alert.setTitle("PlayerTag Taken");
                    alert.setHeaderText("PlayerTag " + line +" is already taken");
                    alert.setContentText("Please enter another tag for this user");
                    
                    alert.showAndWait();
                }
                    
           // }
           // else
           //     System.out.print("Player Tag " + line + " already taken. Is that Ok?");
        }
        
        prSettings.printAllPlayers();
    }
    
    @FXML
    private void backToPRSettings1(){
        mainApp.backToPRSettings1(prSettings);
        //mainApp.openNewPRSettings1();
    }
    
}
