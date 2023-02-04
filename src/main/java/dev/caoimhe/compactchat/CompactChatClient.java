package dev.caoimhe.compactchat;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompactChatClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("compact-chat");

	@Override
	public void onInitializeClient() {
		LOGGER.info("Helloooo! Is anyone out there? (CompactChat is ready!)");
	}
}