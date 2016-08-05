package elorankings.controller;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class RootLayerController {
    private MainApp mainApp;

    @FXML
    private MenuItem closeButton;
    
    public RootLayerController(){
    }
	/**
	  * Is called by the main application to give a reference back to itself.
	  * 
	  * @param mainApp
	**/
	    
	    public void setMainApp(MainApp mainApp) {
	        this.mainApp = mainApp;
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/elorankings/view/MainMenu.fxml"));
	        loader.setController(this);
	    }

	    @FXML
	    public void handleClose(){
	        Platform.exit();
	    }
	    
	    @FXML
	    public void handleHelp(){
	        try {
	            Desktop.getDesktop().browse(new URI("https://docs.google.com/document/d/1MNM-aEDplUzdq_sI4fr9IkOdyZlZ5QH67gEQJaaCQqk/edit?usp=sharing"));
	        } catch (IOException e1) {
	            e1.printStackTrace();
	        } catch (URISyntaxException e1) {
	            e1.printStackTrace();
	        }
	    }
	    
	    @FXML
	    public void handleAbout(){
	    	
	        try {
	            Desktop.getDesktop().browse(new URI("http://www.elocentral.com"));
	        } catch (IOException e1) {
	            e1.printStackTrace();
	        } catch (URISyntaxException e1) {
	            e1.printStackTrace();
	        }
	    }


}
