package discordbot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

public class Example {

	static final String BOT_TOKEN = "bot.token";
	
	//https://discordapp.com/oauth2/authorize?client_id=306780786609356811&scope=bot&permissions=66321471
	public static void main(String[] args) {

		createClient(getBotToken(), true);

	}

	// Read file using lines() and Stream Approach
	private static String getBotToken() {

		Stream<String> stringStream = null;

		try {
			// Read all lines from a file as a Stream. Bytes from the file are decoded into characters using the UTF-8 charset
			stringStream = Files.lines(Paths.get(BOT_TOKEN));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return stringStream.map(i -> i.toString()).collect(Collectors.joining());
	}

	public static IDiscordClient createClient(String token, boolean login) { // Returns a new instance of the Discord client

		ClientBuilder clientBuilder = new ClientBuilder(); // Creates the ClientBuilder instance
		clientBuilder.withToken(token); // Adds the login info to the builder

		try {
			if (login) {
				return clientBuilder.login(); // Creates the client instance and logs the client in
			} else {
				return clientBuilder.build(); // Creates the client instance but it doesn't log the client in yet, you would have to call client.login() yourself
			}

		} catch (DiscordException e) { // This is thrown if there was a problem building the client
			e.printStackTrace();
			return null;
		}

	}

}
