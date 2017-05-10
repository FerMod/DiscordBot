package discordbot.command;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.darichey.discord.api.Command;

import discordbot.command.tool.ToolCommands;
import sx.blah.discord.handle.obj.Permissions;

public abstract class BotCommands {
	
//	private static Map<String, BotCommands> commandsMap;
	
	public static void load() {
		
//		commandsMap = Collections.synchronizedMap(new HashMap<String, BotCommands>());
		
		ToolCommands.load();
//		commandsMap.put(ToolCommands.CATEGORY, ToolCommands.getInstance());
	}
	
	public static String getCommandInformation(Command command) {
		return getCommandInformation(command, false, false);
	}
	
	public static String getCommandInformation(Command command, boolean showPermissions) {
		return getCommandInformation(command, showPermissions, false);
	}

	public static String getCommandInformation(Command command, boolean showPermissions, boolean showDetails) {
	
		StringBuilder sb = new StringBuilder();	
	
		sb.append(command.getUsage());
		sb.append(System.getProperty("line.separator"));
		sb.append(command.getDescription());
	
		if(showPermissions && command.getRequiredPermissions().size() != 0) {
	
			sb.append(System.getProperty("line.separator"));
			sb.append("Required permissions: "); 
	
			Iterator<Permissions> it = command.getRequiredPermissions().iterator();
			while(it.hasNext()) {
				Permissions permissions = it.next();
				sb.append("**`" + permissions + "`** ");
				if(it.hasNext()) {
					sb.append(", ");
				}
			}
	
		}
	
		if(showDetails) {
			sb.append(System.getProperty("line.separator"));
			sb.append("Case sensitive: " + command.isCaseSensitive());
	
			sb.append(System.getProperty("line.separator"));
			sb.append("Delete user command: " + command.deletesCommand());
		}
	
		return sb.toString();		
	}

	public static String getCommandTitle(Command command) {
		return getCommandTitle(command, false);
	}

	public static String getCommandTitle(Command command, boolean showAlias) {
		StringBuilder sb = new StringBuilder();
		sb.append(command.getName());
		for (String alias : command.getAliases()) {
			sb.append(" / " + alias);
		}
		return sb.toString();
	}

//	public static Map<String, BotCommands> getCommandsMap() {
//		return commandsMap;
//	}

//	public static void setCommandsMap(Map<String, BotCommands> commandsMap) {
//		BotCommands.commandsMap = commandsMap;
//	}

}

