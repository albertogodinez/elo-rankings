/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elorankings.controller;

import java.util.HashMap;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Alberto
 */
public class ScreensController extends StackPane{
    private HashMap<String, Node> screens = new HashMap();
    
    public void addScreen(String name, Node screen) { 
       screens.put(name, screen); 
   }
}
