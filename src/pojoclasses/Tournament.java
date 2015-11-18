package pojoclasses;




public class Tournament {
    private String name;
    private String id;
    private int participants_count;
    private String started_at;
    
    public String getName(){
        return name;
    }
    
    public int getParticipantCount(){
        return participants_count;
    }
    
    public String getStartedAt(){
        return started_at;
    }
    
    public String getId(){
        return id;
    }
}
