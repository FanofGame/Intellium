package com.github.alfixjanuarivinter.intellium;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Intellium implements ModInitializer {
	public static final String MOD_ID = "intellium";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Intellium - Optimizing for Intel GPUs");
	}
}
