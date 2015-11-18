package deserializers;


import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;


public class MyDeserializer <T> implements JsonDeserializer<T>{
    @Override
    public T deserialize(JsonElement je, java.lang.reflect.Type type, JsonDeserializationContext jdc) 
            throws JsonParseException {
        
        // Get the "tournament" element from the parsed JSON
        JsonElement tournament = je.getAsJsonObject().get("tournament");

        // Deserialize it. You use a new instance of Gson to avoid infinite recursion
        // to this deserializer
        return new Gson().fromJson(tournament, type);
        }
}
