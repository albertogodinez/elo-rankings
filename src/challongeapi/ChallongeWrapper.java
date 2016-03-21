package challongeapi;


import challongeapi.pojoclasses.Match;
import challongeapi.pojoclasses.Participant;
import challongeapi.pojoclasses.Tournament;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/*
** This class is in charge of calling all other classes and making sure that
** each class is executed properly and according to what the user would like
** to do.
*/

@SuppressWarnings("unused")
public class ChallongeWrapper {
    private static Tournament tournament;
    private static List<Participant> participants;
    private static List<Match> matches;
    private static Map<String, String> players;
	private static String tournamentName;
    private static String tournamentType;
    private static String id;
    private static int numOfParticipants;
    private static String dateCreated;
    private static String USER_NAME = null;
    private static String API_KEY = null;
    private static String BASE_URL;
    
    private static String tournamentRequest;
    private static String participantRequest;
    private static String matchesRequest;
    
    
    private void setUserName(String username){
        USER_NAME = username;
    }
    
    private void setApiKey(String apiKey){
        API_KEY = apiKey;
    }
    
    //Must set API_KEY and USER_NAME beforehand
    public boolean runUrl(String username, String apiKey, String challongeLink){
       // if(!matches.isEmpty() && !participants.isEmpty() && !players.isEmpty()){
      //      matches.clear();
       //     participants.clear();
       //     players.clear();  
        //}
        matches = new ArrayList<Match>();
        participants = new ArrayList<Participant>();
        players = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
        
        setUserName(username);
        setApiKey(apiKey);
        
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
        
        tournamentType = tournament.getTournamentType();
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
    
    
    public Tournament getTournamentInfo(){
        if(ChallongeWrapper.getTournamentInfo(tournamentRequest))
            return tournament;
        else
            return null;
    }    
    
    /*
      The key contains the users playerId and the Object itself contains the
      players tag/name
    */
    public List<String> getParticipants(){
        if(getParticipants(participantRequest)){
            List<String> playersList = new ArrayList<String>();
            for(Map.Entry<String,String> entry: players.entrySet()){
                playersList.add(entry.getValue());
            }
            return playersList;
            
        }
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
    /* THIS WAS USED TO CHECK THIS CLASS
    public static void main(String[] args){
        //https://alberto_g90:VnU1jHWxzJZ6Rsh2hEkkfm30TVZ2OvxICQGKUYWJ@api.challonge.com/v1/tournaments/i1aunxjd/.json
        ChallongeWrapper challongeWrapper = new ChallongeWrapper();
        String userName = "alberto_g90",
                apiKey = "VnU1jHWxzJZ6Rsh2hEkkfm30TVZ2OvxICQGKUYWJ",
                url = "http://challonge.com/85sf7tkj";
        challongeWrapper.setUserName(userName);
        challongeWrapper.setApiKey(apiKey);
        challongeWrapper.runUrl(userName, apiKey, url);
        
        Tournament tournamentInfo = challongeWrapper.getTournamentInfo();
        List<String> playersInfo = challongeWrapper.getParticipants();
        List<Match> matches1 = challongeWrapper.getMatches();
        
        System.out.println("\n\nTournament type: " + tournamentInfo.getTournamentType()
                            + "\nTournament ID: " + tournamentInfo.getId() +
                           "\nTournament Name: " + tournamentInfo.getName() + 
                           "\nTournament Started at: " + tournamentInfo.getStartedAt() + 
                           "\nTotal Number of Participants" + tournamentInfo.getParticipantCount());
        
        for(Match match : matches1){
            System.out.println("Winner: " + match.getWinner() + "    Loser: " + match.getLoser());
        }
       
    }*/
}
