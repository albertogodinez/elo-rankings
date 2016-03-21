package elorankings.model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name = "prsettings")
public class PRSettingsWrapper extends PRSettings{
    //@XmlElement(name = "prsetting")
    private ObservableList<PRSettings> prList = FXCollections.observableArrayList();
    
    @XmlElements({ @XmlElement(name = "prsetting", type = PRSettings.class) })
    public ObservableList<PRSettings> getPrList(){
        return prList;
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("prsetting:");
        
        for(PRSettings node : prList){
            sb.append("\n");
            sb.append(" " + node.toString());
        }
        
        return sb.toString();
    }
    
    public void setPrList(ObservableList<PRSettings> prList){
        this.prList = prList;
    }
}
