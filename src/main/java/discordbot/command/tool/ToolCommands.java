package discordbot.command.tool;

import java.awt.Color;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.darichey.discord.api.Command;
import com.darichey.discord.api.CommandContext;
import com.darichey.discord.api.CommandRegistry;

import discordbot.BotMain;
import discordbot.command.BotCommands;
import discordbot.stattrack.UserStats;
import discordbot.stattrack.UserStats.Stat;
import sx.blah.discord.Discord4J;
import sx.blah.discord.handle.impl.obj.User;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class ToolCommands extends BotCommands {

	private final static ToolCommands instance = new ToolCommands();

	public static final String CATEGORY = "tools";
	//	private static Set<Command> commandSet;

	/** Don't let anyone else instantiate this class */
	private ToolCommands() {}

	public static ToolCommands getInstance() {
		return instance;
	}

	public static void load() {

		//		setCommandSet(Collections.synchronizedSet(new HashSet<>()));

		//*** Create the commands ***

		//help
		Command help = new Command("help");
		help.withDescription("Show all the bot commands, or search information of a command.");
		help.withUsage("help *[command]*");
		help.caseSensitive(false);
		//		Set<String> helpAliases = new LinkedHashSet<String>(Arrays.asList("man", "h"));
		//		help.withAliases(helpAliases);
		help.onExecuted(context -> help(context));
		
		//ping
		Command ping = new Command("ping");
		ping.withDescription("Test if the bot its operative.");
		ping.caseSensitive(false);
		//		ping.withAliases("test");
		ping.onExecuted(context -> ping(context));

		//kill
		Command kill = new Command("kill");
		kill.withDescription("Stop the execution of the bot.");
		kill.caseSensitive(false);
		kill.requirePermissions(EnumSet.of(Permissions.KICK));
		//		kill.withAliases("stop");
		kill.onExecuted(context -> kill(context));

		//storedusers
		Command stats = new Command("stats");
		stats.withDescription("Show some stats.");
		stats.caseSensitive(false);
		//		Set<String> helpAliases = new LinkedHashSet<String>(Arrays.asList("man", "h"));
		//		help.withAliases(helpAliases);
		stats.onExecuted(context -> stats(context));


		//help
		//		commandSet.add(help);
		CommandRegistry.getForClient(BotMain.client).register(help);

		//ping
		//		commandSet.add(ping);
		CommandRegistry.getForClient(BotMain.client).register(ping);

		//kill
		//		commandSet.add(kill);
		CommandRegistry.getForClient(BotMain.client).register(kill);

		//stats
		//		commandSet.add(storedusers);
		CommandRegistry.getForClient(BotMain.client).register(stats);

	}

	public static void stats(CommandContext commandContext) {
		try {
			
			StringBuilder sb = new StringBuilder();
			
			IUser author = commandContext.getMessage().getAuthor();
			sb.append(author.getDisplayName(commandContext.getMessage().getGuild()));
			
			UserStats userStats = BotMain.getUsersStatsMap().get(author);
			for (Stat stat : userStats.getStatsMap().keySet()) {
				sb.append(System.getProperty("line.separator"));
				sb.append(stat + ": ");
				sb.append(userStats.getStatsMap().get(stat));
			}
			
			BotMain.sendMessage(sb.toString(), commandContext.getMessage().getChannel());
			
		} catch (MissingPermissionsException | RateLimitException | DiscordException e) {
			e.printStackTrace();
		}
	}

	public static void help(CommandContext commandContext) {

		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.withColor(new Color(102, 204, 0));
		embedBuilder.setLenient(true);

		StringBuilder sb;

		if(commandContext.getArgs().length > 0) {

			String[] args = commandContext.getArgs();

			Optional<Command> command = CommandRegistry.getForClient(BotMain.client).getCommandByName(args[0], true);

			String title, content;
			if(command.isPresent()) {
				title = getCommandInformation(command.get(), false, false);
				content = getCommandTitle(command.get());
				embedBuilder.appendField(content, title, false);
			} else {
				embedBuilder.withColor(new Color(255,140,0));
				content = "Command **`" + args[0] + "`** not found.";
				embedBuilder.appendDesc(content);
			}	

		} else {

			List<Command> commandList = CommandRegistry.getForClient(BotMain.client).getCommands();

			for (Command command : commandList) {

				sb = new StringBuilder();
				sb.append(command.getUsage());
				sb.append(System.getProperty("line.separator"));
				sb.append(command.getDescription());

				embedBuilder.appendField(getCommandTitle(command), sb.toString(), false);

				if(BotMain.DEBUG) {
					sb = new StringBuilder();
					sb.append(System.getProperty("line.separator"));
					sb.append("Name: " + command.getName());
					sb.append(System.getProperty("line.separator"));
					sb.append("Aliases: " + command.getAliases());
					sb.append(System.getProperty("line.separator"));
					sb.append("Description: \"" + command.getDescription() + "\" ");
					sb.append(System.getProperty("line.separator"));
					sb.append("Required permissions: " + command.getRequiredPermissions()); 
					sb.append(System.getProperty("line.separator"));
					sb.append("Usage: " + command.getUsage());
					sb.append(System.getProperty("line.separator"));
					sb.append("Case sensitive: " + command.isCaseSensitive());
					sb.append(System.getProperty("line.separator"));
					sb.append("Delete after use: " + command.deletesCommand());
					sb.append(System.getProperty("line.separator"));
					Discord4J.LOGGER.debug(sb.toString());
				}

			}

		}

		BotMain.sendMessage(embedBuilder.build(), commandContext.getMessage().getChannel());

	}

	public static void ping(CommandContext commandContext) {
		try {
			BotMain.sendMessage("Pong!", commandContext.getMessage().getChannel());
		} catch (MissingPermissionsException | RateLimitException | DiscordException e) {
			e.printStackTrace();
		}
	}

	public static void kill(CommandContext commandContext) {
		try {
			IGuild guild = commandContext.getMessage().getGuild();
			BotMain.sendMessage("Killing the bot **" + BotMain.client.getOurUser().getNicknameForGuild(guild) + "**", commandContext.getMessage().getChannel());
			System.exit(0);
		} catch (MissingPermissionsException | RateLimitException | DiscordException e) {
			e.printStackTrace();
		}
	}

	//	public static Set<Command> getCommandSet() {
	//		return commandSet;
	//	}

	//	public static void setCommandSet(Set<Command> commandSet) {
	//		ToolCommands.commandSet = commandSet;
	//	}

}
