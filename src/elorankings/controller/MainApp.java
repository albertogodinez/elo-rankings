package elorankings.controller;


import elorankings.model.PRSettings;
import elorankings.model.PlayerProfile;
import java.io.IOException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;




public class MainApp extends Application {

    private Stage primaryStage;
    static private BorderPane rootLayout;
    private ObservableList<PRSettings> prList = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("EloApp");

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
    
    public void openPROptionsScreen(String prName){
        try {
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
            
            PRSettings prSettings = new PRSettings();
            // Gives the controller access to the PR Settings
            PRSettingsController controller = loader.getController();
            controller.setMainApp(this, prSettings);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openNewPRSettings2(PRSettings prSetting) {
        try {
            FXMLLoader loader = new FXMLLoader();
            
            loader.setLocation(MainApp.class.getResource("/elorankings/view/PRSettings2.fxml"));
            HBox prSetting2 = (HBox) loader.load();

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

    public static void main(String[] args) {
        launch(args);
    }
}