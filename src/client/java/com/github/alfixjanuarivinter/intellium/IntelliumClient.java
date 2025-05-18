package com.github.alfixjanuarivinter.intellium;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntelliumClient implements ClientModInitializer {

	private static final Logger LOGGER = LoggerFactory.getLogger("Intellium");

	@Override
	public void onInitializeClient() {
		LOGGER.info("Initializing Intellium - Optimizing for Intel GPUs");

		MinecraftClient client = MinecraftClient.getInstance();

		client.execute(() -> {
			try {
				String renderer = GL11.glGetString(GL11.GL_RENDERER);
				if (renderer != null && renderer.toLowerCase().contains("intel")) {
					LOGGER.info("Intel GPU detected: " + renderer);

					GameOptions options = client.options;
					if (options != null) {
						// Set view distance
						if (options.getViewDistance() != null) {
							options.getViewDistance().setValue(6);
							LOGGER.info("Set view distance to 6 for Intel GPU performance");
						}

						// Set particles to minimal
						SimpleOption<?> particleOption = options.getParticles();
						if (particleOption != null) {
							Object currentValue = particleOption.getValue();
							if (currentValue != null && currentValue.getClass().isEnum()) {
								Object[] enumConstants = currentValue.getClass().getEnumConstants();
								if (enumConstants.length > 0) {
									// Safe raw cast
									@SuppressWarnings("unchecked")
									SimpleOption<Object> castedOption = (SimpleOption<Object>) particleOption;
									castedOption.setValue(enumConstants[0]); // Usually "Minimal"
									LOGGER.info("Set particle settings to minimal");
								}
							} else {
								LOGGER.warn("Particle option is not an enum type");
							}
						}
					}
				} else {
					LOGGER.info("Non-Intel GPU or unknown renderer: " + renderer);
				}
			} catch (Exception e) {
				LOGGER.error("Error during Intellium initialization", e);
			}
		});
	}
}
