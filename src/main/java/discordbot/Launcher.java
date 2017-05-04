package discordbot;


import discordbot.command.BotCommands;
import discordbot.debugconsole.ConsoleWindow;
import discordbot.debugconsole.ConsoleWindow.CloseOperation;
import discordbot.events.BotMessageReceivedEvent;
import discordbot.events.BotReadyEvent;
import sx.blah.discord.Discord4J;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.util.DiscordException;

public class Launcher extends Thread {

	/**
	 * Notes:
	 * 
	 * Exp system
	 * https://gamedev.stackexchange.com/a/20946/92980
	 * 
	 * Serialize data:
	 * http://stackoverflow.com/a/4118917/4134376
	 * 
	 */

	//https://discordapp.com/oauth2/authorize?client_id=BOT_CLIENT_ID&scope=bot&permissions=66321471
	public static void main(String[] args) {
		try {

			ConsoleWindow.setDefaultCloseOperation(CloseOperation.EXIT_ON_CLOSE);
			ConsoleWindow.setVisible(true);

			try {

				BotMain.getInstance();
				BotMain.createClient(true);

				// Load all the comands
				BotCommands.load();

				EventDispatcher dispatcher = BotMain.client.getDispatcher(); // Gets the EventDispatcher instance for this client instance
				dispatcher.registerListener(new BotReadyEvent()); // Registers the envent listener
				
				dispatcher.registerListener(new BotMessageReceivedEvent()); // Registers the envent listener
				
			
			} catch (DiscordException e) {
				Discord4J.LOGGER.error("An error has occurred.");
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
