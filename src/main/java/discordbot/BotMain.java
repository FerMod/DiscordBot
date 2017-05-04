package discordbot;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import discordbot.stattrack.UserStats;
import sx.blah.discord.Discord4J;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;

public class BotMain {

	private static final String TOKEN = "bot_token";
	public static final boolean DEBUG = true;

	private static Map<IUser, UserStats> usersStatsMap;

	public static IDiscordClient client;

	private final static BotMain instance = new BotMain();

	/** Don't let anyone else instantiate this class */
	private BotMain() {
		usersStatsMap = Collections.synchronizedMap(new HashMap<IUser, UserStats>());
	}

	public static BotMain getInstance() {		
		return instance;
	}

	private static String getBotToken() {

		Stream<String> stringStream = null;

		try {
			// Read all lines from a file as a Stream. Bytes from the file are decoded into characters using the UTF-8 charset
			stringStream = Files.lines(Paths.get(TOKEN));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return stringStream.map(i -> i.toString()).collect(Collectors.joining());
	}

	/**
	 * Creates and returns a new instance of the Discord client
	 * 
	 * @param login if is {@code true} creates the client instance and logs the client in, otherwise if
	 * 				is {@code false} creates the client instance but it doesn't log the client in yet.</br>
	 * 				It needs to be logged in, by calling the method {@link IDiscordClient#login()}
	 * @return a {@code Optional} of type {@code IDiscordClient} with a new instance of the Discord client
	 * @throws DiscordException This is thrown if there was a problem building the client
	 */
	public static IDiscordClient createClient(boolean login) throws DiscordException {

		ClientBuilder clientBuilder = new ClientBuilder(); // Create the ClientBuilder instance
		clientBuilder.withToken(getBotToken()); // Add the login info to the builder

		if (login) {
			client = clientBuilder.login();
		} else {
			client = clientBuilder.build();
		}

		return client;
	}

	public static IDiscordClient login() throws DiscordException {
		if(client == null) {
			createClient(true);
		} else {
			client.login();
		}
		return client;
	}

	public static void sendMessage(String message, IChannel channel) throws DiscordException, MissingPermissionsException {
		sendMessage(null, message, channel);
	}

	public static void sendMessage(String title, String message, IChannel channel) throws DiscordException, MissingPermissionsException {
		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.withColor(new Color(102, 204, 0));
		if(title != null) {
			embedBuilder.withTitle(title);
		}
		embedBuilder.appendDescription(message);
		sendMessage(embedBuilder.build(), channel);
	}

	public static void sendMessage(String title, EmbedObject embedObject, IChannel channel) throws DiscordException, MissingPermissionsException {
		MessageBuilder messageBuilder = new MessageBuilder(BotMain.client);
		messageBuilder.withChannel(channel).withEmbed(embedObject);
		messageBuilder.build();
	}

	public static void sendMessage(EmbedObject embedObject, IChannel channel) throws DiscordException, MissingPermissionsException {
		MessageBuilder messageBuilder = new MessageBuilder(BotMain.client);
		messageBuilder.withChannel(channel).withEmbed(embedObject);
		messageBuilder.build();
	}
	
	public static UserStats getUserStats(IUser user) {
		return usersStatsMap.get(user);
	}
	
	public static Map<IUser, UserStats> getUsersStatsMap() {
		return usersStatsMap;
	}

	public static void setUsersStatsMap(Map<IUser, UserStats> usersStatsMap) {
		BotMain.usersStatsMap = Collections.synchronizedMap(usersStatsMap);
	}

}
