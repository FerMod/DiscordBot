package discordbot.events;

import sx.blah.discord.Discord4J;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.ReadyEvent;

public class BotReadyEvent implements IListener<ReadyEvent> {
	
	@Override
	public void handle(ReadyEvent event) {
		Discord4J.LOGGER.info("Bot ready!");		
//		XMLWriter xmlWriter = new XMLWriter();
//		xmlWriter.createFile();
	}

}
