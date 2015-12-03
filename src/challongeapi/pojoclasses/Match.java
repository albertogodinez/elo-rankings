package challongeapi.pojoclasses;


import com.google.gson.annotations.SerializedName;


public class Match {
    @SerializedName ("winner_id") private String winner;
    @SerializedName ("loser_id") private String loser;
    
    public String getWinner(){
        return winner;
    }
    
    public String getLoser(){
        return loser;
    }
    
    public void setWinner(String winner){
        this.winner = winner;
    }
    
    public void setLoser(String loser){
        this.loser = loser;
    }
}
