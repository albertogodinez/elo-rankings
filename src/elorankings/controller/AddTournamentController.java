package elorankings.controller;

import challongeapi.ChallongeWrapper;
import challongeapi.pojoclasses.Match;
import challongeapi.pojoclasses.Tournament;
import elorankings.model.PRSettings;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 *
 * @author Alberto
 */
public class AddTournamentController {
    @FXML
    private TextField challongeLink;
    @FXML
    private Button backButton;
    
    MainApp mainApp;
    PRSettings pr;
    
/**
  * Is called by the main application to give a reference back to itself.
  * 
  * @param mainApp
  * @param pr
**/
    public void setMainApp(MainApp mainApp, PRSettings pr) {
        this.mainApp = mainApp;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/elorankings/view/MainMenu.fxml"));
        loader.setController(this);
        
        this.pr = pr;
    }
    
    @FXML
    public void backToPROptionsScreen(){
        mainApp.openPROptionsScreen(pr.getPrName());
    }
    
    @FXML
    public void addTournament(){
        ChallongeWrapper cw = new ChallongeWrapper();
        if(!cw.runUrl(pr.getChallongeUsername(), pr.getChallongeApiKey(), challongeLink.getText())){
            /*TODO
            **Create Alert if it fails
            */
            System.out.println("Failed");
        }
        else{
            Tournament tempTournament = cw.getTournamentInfo();
            HashMap<String,String> participants = cw.getParticipants();
            List<Match> matches = cw.getMatches();
            
            //System.out.println("Tournament ID: " + tempTournament.getId() + "\n" +
              //                 "Tournament Name: "+ tempTournament.getName() + "\n" + 
                //               "Date tournament started on: "+ tempTournament.getStartedAt());
            
           // System.out.println("\nList of Participants/players");
            
            for(Map.Entry<String,String> entry : participants.entrySet()){
                //if(pr.tagTaken(entry.getValue())){
                if(!pr.updateTournamentsEntered(entry.getValue(), tempTournament.getStartedAt())){
                    //TODO
                    //Make sure I make this into an alert
                    System.out.println("Player " + entry.getValue() + " not found");
                }
                    
                //System.out.println(entry.getKey() + " : " + entry.getValue());
            }
            
            //System.out.println("\nList of Matches");
            //for(Match match: matches){
           //     System.out.println(match.getWinner() + " beat " + match.getLoser());
          //  }
        }
            
            
    }
}
