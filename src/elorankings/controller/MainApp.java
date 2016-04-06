package elorankings.controller;

import elorankings.model.PRSettings;
import elorankings.model.PRSettingsWrapper;
import elorankings.model.PlayerProfile;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.prefs.Preferences;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class MainApp extends Application {

    private Stage primaryStage;
    static private BorderPane rootLayout;
    private ObservableList<PRSettings> prList = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("EloApp");
        
        this.primaryStage.getIcons().add(new Image("file:resources/images/Logo1.png"));

        initRootLayout();

        showMainMenu();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/elorankings/view/Overview.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
            
            // Try to load last opened person file.
            File file = getPrSettingsFilePath();
            if (file != null) {
                loadPrSettingsDataFromFile(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    /**
     * Shows the person overview inside the root layout.
     */
    public void showMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader();
            
            loader.setLocation(MainApp.class.getResource("/elorankings/view/MainMenu.fxml"));
            AnchorPane overview = (AnchorPane) loader.load();

                // Set main menu into the center of root layout.
            rootLayout.setCenter(overview);
            

            // Give the controller access to the main app.
            MainMenuController controller = loader.getController();
            controller.setMainApp(this);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void openPRList(){
        try {
            FXMLLoader loader = new FXMLLoader();
            
            loader.setLocation(MainApp.class.getResource("/elorankings/view/PRList.fxml"));
            AnchorPane overview = (AnchorPane) loader.load();

            // Set main menu into the center of root layout.
            rootLayout.setCenter(overview);
            

            // Give the controller access to the main app.
            PRListController controller = loader.getController();
            controller.setMainApp(this);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    //LET ME TRY SOMETHING NEW
    

    public void backToPROptionsScreen(PRSettings pr){
        try {
        	for(int i=0; i < prList.size(); i++){
        		if(prList.get(i).getPrName().equals(pr.getPrName())){
        			prList.remove(i);
        			prList.add(i, pr);
        		}
        	}
        	File file = getPrSettingsFilePath();
        	savePrSettingsDataToFile(file);
        	
            FXMLLoader loader = new FXMLLoader();
            
            loader.setLocation(MainApp.class.getResource("/elorankings/view/PROptionsScreen.fxml"));
            AnchorPane overview = (AnchorPane) loader.load();

            // Set main menu into the center of root layout.
            rootLayout.setCenter(overview);
            

            // Give the controller access to the main app.
            PROptionsScreenController controller = loader.getController();
            controller.setMainApp(this, pr.getPrName());
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    public void openPROptionsScreen(String prName){
        try {
        	// Try to load last opened person file.
            //File file = getPrSettingsFilePath();
           // if (file != null) {
             //   loadPrSettingsDataFromFile(file);
            //}
            FXMLLoader loader = new FXMLLoader();
            
            loader.setLocation(MainApp.class.getResource("/elorankings/view/PROptionsScreen.fxml"));
            AnchorPane overview = (AnchorPane) loader.load();

            // Set main menu into the center of root layout.
            rootLayout.setCenter(overview);

         
            // Give the controller access to the main app.
            PROptionsScreenController controller = loader.getController();
            controller.setMainApp(this, prName);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public void openAddTournamentScreen(PRSettings pr){
        try {
            FXMLLoader loader = new FXMLLoader();
            
            loader.setLocation(MainApp.class.getResource("/elorankings/view/AddTournamentScreen.fxml"));
            AnchorPane overview = (AnchorPane) loader.load();

            // Set main menu into the center of root layout.
            rootLayout.setCenter(overview);
            

            // Give the controller access to the main app.
            AddTournamentController controller = loader.getController();
            controller.setMainApp(this, pr);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void openTwoPlayerRanker(PRSettings pr){
        try {
            FXMLLoader loader = new FXMLLoader();
            
            loader.setLocation(MainApp.class.getResource("/elorankings/view/TwoPlayerRanker.fxml"));
            AnchorPane overview = (AnchorPane) loader.load();

            //Set PR Settings into the center of root layout.
            rootLayout.setCenter(overview);
            

            // Gives the controller access to the PR Settings
            TwoPlayerRankerController controller = loader.getController();
            controller.setMainApp(this, pr);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void openPRView(String prName){
        try {
            FXMLLoader loader = new FXMLLoader();
            
            loader.setLocation(MainApp.class.getResource("/elorankings/view/PRView.fxml"));
            AnchorPane overview = (AnchorPane) loader.load();

            // Set main menu into the center of root layout.
            rootLayout.setCenter(overview);
            

            // Give the controller access to the main app.
            PRViewController controller = loader.getController();
            controller.setMainApp(this, prName);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void openPlayerProfile(String prName, PlayerProfile playerProfile){
        try {
            FXMLLoader loader = new FXMLLoader();
            
            loader.setLocation(MainApp.class.getResource("/elorankings/view/PlayerProfile.fxml"));
            AnchorPane overview = (AnchorPane) loader.load();

            // Set main menu into the center of root layout.
            rootLayout.setCenter(overview);
            

            // Give the controller access to the main app.
            PlayerProfileController controller = loader.getController();
            controller.setMainApp(this, getAPr(prName), playerProfile);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public void openNewPRSettings1() {
        try {
            FXMLLoader loader = new FXMLLoader();
            
            loader.setLocation(MainApp.class.getResource("/elorankings/view/PRSettings.fxml"));
            AnchorPane prSetting = (AnchorPane) loader.load();

            //Set PR Settings into the center of root layout.
            rootLayout.setCenter(prSetting);
            
            //PRSettings prSettings = new PRSettings();
            //prList.add(prSettings);
            
            // Gives the controller access to the PR Settings
            PRSettingsController controller = loader.getController();
            controller.setMainApp(this);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openNewPRSettings2(PRSettings prSetting) {
        try {
            FXMLLoader loader = new FXMLLoader();
            
            loader.setLocation(MainApp.class.getResource("/elorankings/view/PRSettings2.fxml"));
            AnchorPane prSetting2 = (AnchorPane) loader.load();

            //Set PR Settings into the center of root layout.
            rootLayout.setCenter(prSetting2);
            

            // Gives the controller access to the PR Settings
            PRSettingsController2 controller = loader.getController();
            controller.setMainApp(this, prSetting);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void backToPRSettings1(PRSettings prSettings, String prevMenu){
        try {
            FXMLLoader loader = new FXMLLoader();
            
            loader.setLocation(MainApp.class.getResource("/elorankings/view/PRSettings.fxml"));
            AnchorPane prSetting = (AnchorPane) loader.load();

            //Set PR Settings into the center of root layout.
            rootLayout.setCenter(prSetting);
            

            // Gives the controller access to the PR Settings
            //PRSettingsController controller = new PRSettingsController(prSettings);
            PRSettingsController controller = loader.getController();
            //controller.setMainApp(this);
            controller.setOldMainApp(this, prSettings, prevMenu);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Used to obtain copy of all of the available Rankings
    public ObservableList<PRSettings> getPRs(){
        return prList;
    }
    
    //Updates all of the rankings
    public void setPRs(ObservableList<PRSettings> prList){
        this.prList = prList;
    }
    
    //Adds single Power Ranking to PR list
    public void updatePR(PRSettings newPR){
        prList.add(newPR);
    }
    
    //Returns a single Power Ranking using the PR Name
    public PRSettings getAPr(String prName){
        for(PRSettings pr : prList){
            if(pr.getPrName() == prName)
                return pr;
        }
        return null;
    }
    
    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public BorderPane getRootLayout(){
        return rootLayout;
    }
    
    public void setRootLayout(HBox node){
        rootLayout.setCenter(node);
    }
    
    /**
    * Loads person data from the specified file. The current person data will
    * be replaced.
    * 
    * @param file
    */
    public void loadPrSettingsDataFromFile(File file) throws FileNotFoundException, IOException {
        try {
           JAXBContext context = JAXBContext.newInstance(PRSettingsWrapper.class);
           Unmarshaller um = context.createUnmarshaller();
           
           um.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());    

           // Reading XML from the file and unmarshalling.    
           PRSettingsWrapper wrapper = (PRSettingsWrapper) um.unmarshal(file);
           
           
           prList.clear();
           prList = wrapper.getPrList();
           
           //System.out.println(prList.get(0).getAllPlayers().get(0).getPlayersTag());
           //System.out.println("unmarshal test: " + wrapper.getPrList().get(1).getChallongeApiKey());
           //System.out.println("unmarshal test: " + wrapper.getPrList().get(1).getAllPlayers().get(0).getPlayersTag());
           //System.out.println("unmarshal test: " + wrapper.getPrList().get(1).getAllPlayers().get(0).getPlayersStatus());
           //System.out.println(prList.size());
           //System.out.println(prList.get(2).getChallongeApiKey());
           // Save the file path to the registry.
           setPrSettingsFilePath(file);

        } catch (JAXBException e) { 
           System.out.println(e.getStackTrace());
           Alert alert = new Alert(AlertType.ERROR);
           alert.setTitle("Error");
           alert.setHeaderText("Could not load data");
           alert.setContentText("Could not load data from file:\n" + file.getPath());

           alert.showAndWait();
        } 
    }
    
     /**
     * Saves the current person data to the specified file.
     * 
     * @param file
     */
    public void savePrSettingsDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(PRSettingsWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping our person data.
            PRSettingsWrapper wrapper = new PRSettingsWrapper();
            wrapper.setPrList(prList);
            // Marshalling and saving XML to the file.
            m.marshal(wrapper, file);

            // Save the file path to the registry.
            setPrSettingsFilePath(file);
        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + file.getPath());

            alert.showAndWait();
        }
    }
    
     /**
     * Returns the prsettings file preference, i.e. the file that was last opened.
     * The preference is read from the OS specific registry. If no such
     * preference can be found, null is returned.
     * 
     * @return
     */
    public File getPrSettingsFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            System.out.println("File path: " + filePath);
            return new File(filePath);
        } else {
            return null;
        }
    }
    /**
     * Sets the file path of the currently loaded file. The path is persisted in
     * the OS specific registry.
     * 
     * @param file the file or null to remove the path
     */
    public void setPrSettingsFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());

            // Update the stage title.
            //primaryStage.setTitle("EloApp - " + file.getName());
        } else {
            prefs.remove("filePath");

            // Update the stage title.
            //primaryStage.setTitle("EloApp");
        }
    }

    

    public static void main(String[] args) {
       launch(args);
    }
}