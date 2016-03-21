package challongeapi.pojoclasses;




public class Tournament {
    private String name;
    private String id;
    private int participants_count;
    private String started_at;
    private String tournament_type;
    
    public String getTournamentType(){
        return tournament_type;
    }
    
    public String getName(){
        return name;
    }
    
    public int getParticipantCount(){
        return participants_count;
    }
    
    public String getStartedAt(){
        return convertedDate();
    }
    
    public String getId(){
        return id;
    }
    
    /*
    **Changes the started_at variable to mm/dd/yyyy
    **instead of what is received
    */
    private String convertedDate(){
        
        String[] splitDate = started_at.split("-");
        splitDate[2] = splitDate[2].substring(0, 2);
        
        String temp = splitDate[1] + "/" + splitDate[2] + "/" + splitDate[0];
        return temp;
    }
}
