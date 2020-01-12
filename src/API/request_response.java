package API;

//import important package 
import Window.Search_win;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class request_response
{
    Map<String, String> map_JSON = new HashMap<>();
    
    public Map getMap()
    {
        return map_JSON;
    }

 //variable Title from other package 
    String title = Search_win.getTitle_movie();
    public void response() throws JSONException, IOException
    {

        title = title.replaceAll("\\s+", "+");

        //make request to server OMDBAPI.COM
        String url = "http://www.omdbapi.com/?t=" + title + "&apikey=fa42bd30";
        URL object = new URL(url);
        URLConnection con = object.openConnection();

        //received response
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }
        in.close();

        //change response from STRING to JSON
        JSONObject myResponse = new JSONObject(response.toString());
        
        map_JSON.put("Title", myResponse.getString("Title"));
        map_JSON.put("Year", myResponse.getString("Year"));
        map_JSON.put("Genre", myResponse.getString("Genre"));
        map_JSON.put("RunTime", myResponse.getString("Runtime"));
        map_JSON.put("Director", myResponse.getString("Director"));
        map_JSON.put("Country", myResponse.getString("Country"));
        
    }
 
}
