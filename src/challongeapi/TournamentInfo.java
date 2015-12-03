package challongeapi;


import challongeapi.deserializers.MyDeserializer;
import challongeapi.pojoclasses.Match;
import challongeapi.pojoclasses.Participant;
import challongeapi.pojoclasses.Tournament;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClients;

 
public class TournamentInfo {
    
    public TournamentInfo(){
         
    }
    
    public static Tournament getTournamentInfo(String jsonUrl){
        HttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(jsonUrl);
        
        try {
            HttpResponse response = client.execute(request);
            //System.out.print(response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            if(entity != null){
                //Allows me to search through the Tournament Object using the 
                //Tournaments POJO
                Gson gson = new GsonBuilder().
                        registerTypeAdapter(Tournament.class, new MyDeserializer<>()).
                        create();
                ContentType contentType = ContentType.getOrDefault(entity);
                Charset charset = contentType.getCharset();
                Reader reader = new InputStreamReader(entity.getContent(), charset);
                
                /*Goes through seperate JSON files received from the Challonge API.
                */
                Tournament obj = gson.fromJson(reader, Tournament.class);
                return obj;
                //System.out.print(obj.getName() + "\n"+ obj.getStartedAt() + "\n" + obj.getParticipantCount());
            }
        } catch (IOException ex) {
            Logger.getLogger(TournamentInfo.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return null;
    }
    
    public static List<Participant> getParticipants(String jsonUrl){
        HttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(jsonUrl);
        
        try {
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            if(entity != null){
                //Allows me to search through the Participant Object using the 
                //Participant/Participants POJO
                //Gson gson = new GsonBuilder().
                        //registerTypeAdapter(ParticipantWrapper.class, new MyDeserializer<>()).
                        //create();
                ContentType contentType = ContentType.getOrDefault(entity);
                Charset charset = contentType.getCharset();
                Reader reader = new InputStreamReader(entity.getContent(), charset);
                
                /*Goes through seperate JSON files received from the Challonge API.
                */
                ArrayList<Participant> al = new ArrayList<>();
               
                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse(reader);
               
               
               if(jsonElement.isJsonArray()){
                   Iterator itor = jsonElement.getAsJsonArray().iterator();
                   
                   while(itor.hasNext()){
                       JsonObject jObject = (JsonObject) itor.next();
                       al.add(new Gson().fromJson(jObject.get("participant"), Participant.class));
                   }
               }
                return al;
            }
        } catch (IOException ex) {
            Logger.getLogger(TournamentInfo.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return null;
    }
    
   public static List<Match> getMatches(String jsonUrl){
        HttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(jsonUrl);
        
        try {
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            if(entity != null){
                //Allows me to search through the Participant Object using the 
                //Participant/Participants POJO
                //Gson gson = new GsonBuilder().
                        //registerTypeAdapter(MatchesWrapper.class, new MyDeserializer<>()).
                        //create();
                ContentType contentType = ContentType.getOrDefault(entity);
                Charset charset = contentType.getCharset();
                Reader reader = new InputStreamReader(entity.getContent(), charset);
                
                /*Goes through seperate JSON files received from the Challonge API.
                */
                ArrayList<Match> al = new ArrayList<>();
               
                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse(reader);
               
               
               if(jsonElement.isJsonArray()){
                   Iterator itor = jsonElement.getAsJsonArray().iterator();
                   
                   while(itor.hasNext()){
                       JsonObject jObject = (JsonObject) itor.next();
                       al.add(new Gson().fromJson(jObject.get("match"), Match.class));
                   }
                   
               }
                return al;
            }
        } catch (IOException ex) {
            Logger.getLogger(TournamentInfo.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return null;
       
   }
}
