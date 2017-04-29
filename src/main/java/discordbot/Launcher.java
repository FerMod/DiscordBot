package discordbot;

import discordbot.command.BotCommands;
import discordbot.debugconsole.ConsoleWindow;
import discordbot.debugconsole.ConsoleWindow.CloseOperation;
import sx.blah.discord.util.DiscordException;

public class Launcher {
	
	//https://discordapp.com/oauth2/authorize?client_id=BOT_CLIENT_ID&scope=bot&permissions=66321471
	public static void main(String[] args) {
		
		ConsoleWindow.setDefaultCloseOperation(CloseOperation.EXIT_ON_CLOSE);
		ConsoleWindow.setVisible(true);
		
		try {
			
		BotMain.createClient(true);
		
		//Loads all the comands
		BotCommands.load();
		
		//Loads commands only from UtilCommands
//		long playgroundID = 172086612019249163L;
//		UtilCommands.help(BotMain.client.getChannelByID(playgroundID));
		
		} catch (DiscordException e) {
			System.err.println("An error has occurred.");
			e.printStackTrace();
		}
	}

}
