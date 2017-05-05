package discordbot.command.tool;

import java.awt.Color;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import com.darichey.discord.api.Command;
import com.darichey.discord.api.CommandContext;
import com.darichey.discord.api.CommandRegistry;

import discordbot.BotMain;
import discordbot.command.BotCommands;
import discordbot.stattrack.UserStats;
import discordbot.stattrack.UserStats.Stat;
import sx.blah.discord.Discord4J;
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
		help.withUsage("help *[command]*");
		help.withDescription("Show all the bot commands, or search information of a command.");
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
		stats.withUsage("stats *[username]*");
		stats.withDescription("Shows your stats, or given a username, the stats of the first user found that matches the username.");
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
		CommandRegistry.getForClient(BotMain.client).register(stats);

	}

	//TODO move to another section
	public static void stats(CommandContext commandContext) {
		try {

			EmbedBuilder embedBuilder = new EmbedBuilder();
			embedBuilder.withColor(new Color(102, 204, 0));
			embedBuilder.setLenient(true);


			IUser author = null;
			String[] args = commandContext.getArgs();
			if(args.length > 0) {
				List<IUser> userList = commandContext.getMessage().getGuild().getUsersByName(commandContext.getArgs()[0], true);
				if(!userList.isEmpty()) {
					author = userList.get(0);
				} else {
					embedBuilder.withColor(new Color(255,140,0));
					embedBuilder.appendDesc("User **`" + args[0] + "`** not found.");
				}
			} else {
				author = commandContext.getMessage().getAuthor();	
			}
			embedBuilder.withAuthorIcon(author.getAvatarURL());
			embedBuilder.withAuthorName(author.getDisplayName(commandContext.getMessage().getGuild()));
			UserStats userStats = BotMain.getUserStats(author);
			for (Stat stat : userStats.getStatsMap().keySet()) {
				embedBuilder.appendField(stat.toString(), userStats.getStatsMap().get(stat).toString(), true);
			}

			BotMain.sendMessage(embedBuilder.build(), commandContext.getMessage().getChannel());

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

}
