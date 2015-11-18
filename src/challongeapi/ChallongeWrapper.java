package challongeapi;


import pojoclasses.Match;
import pojoclasses.Participant;
import pojoclasses.Tournament;
import java.util.HashMap;
import java.util.List;


/*
** This class is in charge of calling all other classes and making sure that
** each class is executed properly and according to what the user would like
** to do.
*/

public class ChallongeWrapper {
    private static Tournament tournament;
    private static List<Participant> participants;
    private static List<Match> matches;
    private static HashMap<String, String> players = new HashMap<String, String>();
    private static String tournamentName;
    private static String id;
    private static int numOfParticipants;
    private static String dateCreated;
    private static String USER_NAME = null;
    private static String API_KEY = null;
    private static String BASE_URL;
    
    private static String tournamentRequest;
    private static String participantRequest;
    private static String matchesRequest;
    
    
    public void setUserName(String username){
        USER_NAME = username;
    }
    
    public void setApiKey(String apiKey){
        API_KEY = apiKey;
    }
    
    //Must set API_KEY and USER_NAME beforehand
    public boolean obtainProperUrl(String challongeLink){
        if(API_KEY == null || USER_NAME == null)
            return false;
        BASE_URL = "https://" + USER_NAME + ":" + API_KEY + "@api.challonge.com/v1";
        String tournamentUrl = challongeLink.substring(challongeLink.lastIndexOf("/")+1);
        tournamentRequest = BASE_URL + "/tournaments/" + tournamentUrl;
        participantRequest = tournamentRequest + "/participants.json";
        matchesRequest = tournamentRequest + "/matches.json";
        tournamentRequest +=".json";
        
        //System.out.println(tournamentRequest + "\n" + participantRequest + "\n" + matchesRequest);
        return true;
    }
    
    
    
    private static boolean getTournamentInfo(String jsonUrl){
        tournament = TournamentInfo.getTournamentInfo(jsonUrl);
        if(tournament == null)
            return false;
        
        tournamentName = tournament.getName();
        id = tournament.getId();
        numOfParticipants = tournament.getParticipantCount();
        dateCreated = tournament.getStartedAt();
        
        //System.out.println(tournamentName + "\n" + id + "\n" + numOfParticipants + "\n" + dateCreated);
        
        return true;
    }
    
    private static boolean getParticipants(String jsonUrl){
        participants = TournamentInfo.getParticipants(jsonUrl);
        if(participants.size() <= 0)
            return false;
        for (Participant a : participants){
           players.put(a.getPlayerId(), a.getPlayerName());
        }
        /*(for(Map.Entry<String,String> entry : players.entrySet()){
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }*/
        return true;
    }
    
    private static boolean getMatches(String jsonUrl){
        matches = TournamentInfo.getMatches(jsonUrl);
        if(matches.size() <= 0)
            return false;
        
        replaceIdWithName();
        
        return true;
    }
    
    private static void replaceIdWithName(){
        int i = 0;
        String tempWinner, tempLoser;
        while(i < matches.size()){
            //System.out.print(i);
            //players.
            tempWinner = players.get(matches.get(i).getWinner());
            tempLoser = players.get(matches.get(i).getLoser());
            //System.out.println(i + ": " + tempWinner + ":" + tempLoser);
            matches.get(i).setWinner(tempWinner);
            matches.get(i).setLoser(tempLoser);
            i++;
        }
        
        /*for(Match match : matches){
            System.out.println("Winner: " + match.getWinner() + "    Loser: " + match.getLoser());
        }*/
    }
    
    
    public Tournament getTournament(){
        if(getTournamentInfo(tournamentRequest))
            return tournament;
        else
            return null;
    }    
    
    /*
      The key contains the users playerId and the Object itself contains the
      players tag/name
    */
    public HashMap<String,String> getParticipants(){
        if(getParticipants(participantRequest))
            return players;
        else
            return null;
    }
    
    /*
      Returns a list of Matches that contain the Winner and Loser of each match
    */
    public List<Match> getMatches(){
        if(getMatches(matchesRequest))
            return matches;
        else
            return null;
    }
    
    /*public static void main(String[] args){
        //https://alberto_g90:VnU1jHWxzJZ6Rsh2hEkkfm30TVZ2OvxICQGKUYWJ@api.challonge.com/v1/tournaments/i1aunxjd/.json
        ChallongeWrapper challongeWrapper = new ChallongeWrapper();
        challongeWrapper.setUserName("alberto_g90");
        challongeWrapper.setApiKey("VnU1jHWxzJZ6Rsh2hEkkfm30TVZ2OvxICQGKUYWJ");
        challongeWrapper.obtainProperUrl("http://challonge.com/zwobz1x");
        
        Tournament tournamentInfo = challongeWrapper.getTournament();
        HashMap<String,String> playerInfo = challongeWrapper.getParticipants();
        List<Match> matches1 = challongeWrapper.getMatches();
        
        System.out.println("Tournament ID: " + tournamentInfo.getId() +
                           "Tournament Name: " + tournamentInfo.getName() + 
                           "Tournament Started at: " + tournamentInfo.getStartedAt() + 
                           "Total Number of Participants" + tournamentInfo.getParticipantCount());
        
        for(Match match : matches1){
            System.out.println("Winner: " + match.getWinner() + "    Loser: " + match.getLoser());
        }
       
    }*/
}
