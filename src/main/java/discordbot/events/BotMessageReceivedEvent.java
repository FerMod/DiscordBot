package discordbot.events;

import discordbot.BotMain;
import discordbot.stattrack.UserStats;
import discordbot.stattrack.UserStats.Stat;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class BotMessageReceivedEvent implements IListener<MessageReceivedEvent>{

	/**
	 * Called when the client receives a message.
	 */
	@Override
	public void handle(MessageReceivedEvent event) {

		IMessage message = event.getMessage(); // Gets the message from the event object NOTE: This is not the content of the message, but the object itself
		//		IChannel channel = message.getChannel(); // Gets the channel in which this message was sent.

		try {
//			XMLWriter xmlWriter = new XMLWriter();

			IUser user = message.getAuthor();
			if(!BotMain.getUsersStatsMap().containsKey(user)) {
				BotMain.getUsersStatsMap().put(user, new UserStats(user));
			}
			
			if(!BotMain.getUserStats(user).getStatsMap().containsKey(Stat.MESSAGES_SENT)) {
				BotMain.getUserStats(user).addStat(Stat.MESSAGES_SENT);
			} else {
				BotMain.getUsersStatsMap().get(user).increase(Stat.MESSAGES_SENT);
				//xmlWriter.update("messages_sent", new UserStats(message.getAuthor()));
			}

		} catch (RateLimitException e) { // RateLimitException thrown. The bot is sending messages too quickly!
			System.err.print("Sending messages too quickly!");
			e.printStackTrace();
		} catch (DiscordException e) { // DiscordException thrown. Many possibilities. Use getErrorMessage() to see what went wrong.
			System.err.print(e.getErrorMessage()); // Print the error message sent by Discord
			e.printStackTrace();
		} catch (MissingPermissionsException e) { // MissingPermissionsException thrown. The bot doesn't have permission to send the message!
			System.err.print("Missing permissions for channel!");
			e.printStackTrace();
		}
	}
}
