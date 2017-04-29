package discordbot.command.util;

import java.awt.Color;
import java.util.List;

import com.darichey.discord.api.Command;
import com.darichey.discord.api.CommandRegistry;

import discordbot.BotMain;
import discordbot.command.BotCommands;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public final class UtilCommands extends BotCommands {

	private UtilCommands() {
	}

	public static void load() {

		//*** Create the commands ***

		//help
		Command help = new Command("help");
		help.withDescription("Show all the bot commands, or search information of a command.");
		help.withUsage("```java"
				+ "help [command]");
		help.caseSensitive(false);
		//help.withAliases("test");
		help.onExecuted(context -> help(context.getMessage().getChannel()));

		//ping
		Command ping = new Command("ping");
		ping.withDescription("Test if the bot its operative.");
		ping.caseSensitive(false);
		//ping.withAliases("test");
		ping.onExecuted(context -> ping(context.getMessage().getChannel()));

		//help
		CommandRegistry.getForClient(BotMain.client).register(help);

		//ping
		CommandRegistry.getForClient(BotMain.client).register(ping);

	}

	public static void help(IChannel channel) {

		List<Command> commandList = CommandRegistry.getForClient(BotMain.client).getCommands();

		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.withColor(new Color(102, 204, 0));
		embedBuilder.setLenient(true);
		
		StringBuilder sb;
		for (Command command : commandList) {
			
			sb = new StringBuilder();
			sb.append(command.getUsage());
			sb.append(System.getProperty("line.separator"));
			sb.append(command.getDescription());

			embedBuilder.appendField(command.getName(), sb.toString(), false);

			//TODO define DEBUG constant
			if(true) {
				sb = new StringBuilder();
				sb.append(System.getProperty("line.separator"));
				sb.append("Name: " + command.getName());
				sb.append("Aliases: " + command.getAliases());
				sb.append("Description: \"" + command.getDescription() + "\" ");
				sb.append("Required permissions: " + command.getRequiredPermissions()); 
				sb.append("Usage: " + command.getUsage());
				sb.append("Case sensitive: " + command.isCaseSensitive());
				sb.append("Delete after use: " + command.deletesCommand());
				sb.append(System.getProperty("line.separator"));
			}

		}

		BotMain.sendMessage(embedBuilder.build(), channel);

	}

	private static void ping(IChannel channel) {
		try {
			BotMain.sendMessage("Pong!", channel);
		} catch (MissingPermissionsException | RateLimitException | DiscordException e) {
			e.printStackTrace();
		}
	}

}
