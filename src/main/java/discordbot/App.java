package discordbot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;

/**
 * Hello world!
 *
 */
public class App {
	
    public static void main(String[] args) {
        try {
        	String config = new String(Files.readAllBytes(Paths.get("config/bot.json")));
        	JSONObject configJson = new JSONObject(config);
        	String botToken = configJson.getString("botToken");
        	String game = configJson.getString("game");
        	String version = configJson.getString("version");
        	String clientID = configJson.getString("clientID");
        	
        	
        } catch(IOException e) {
        	System.out.println("Bot Config not found!");
        }
    }
    
}
