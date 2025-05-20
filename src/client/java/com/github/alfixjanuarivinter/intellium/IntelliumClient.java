package com.github.alfixjanuarivinter.intellium;

import com.github.alfixjanuarivinter.intellium.config.IntelliumConfig;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class IntelliumClient implements ClientModInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger("Intellium");

    @Override
    public void onInitializeClient() {
        IntelliumConfig.load(new File(MinecraftClient.getInstance().runDirectory, "config/intellium.json"));

        MinecraftClient client = MinecraftClient.getInstance();

        client.execute(() -> {
            try {
                String renderer = GL11.glGetString(GL11.GL_RENDERER);
                if (renderer != null && renderer.toLowerCase().contains("intel")) {
                    LOGGER.info("Intel GPU detected: " + renderer);

                    if (!IntelliumConfig.getInstance().enableIntelOptimizations) {
                        LOGGER.info("Intel optimizations disabled in config.");
                        return;
                    }

                    GameOptions options = client.options;
                    if (options != null) {
                        // View Distance
                        if (options.getViewDistance() != null) {
                            options.getViewDistance().setValue(IntelliumConfig.getInstance().viewDistance);
                            LOGGER.info("Applied view distance from config.");
                        }

                        // Particle Setting
                        SimpleOption<?> particleOption = options.getParticles();
                        if (particleOption != null) {
                            Object[] enumConstants = particleOption.getValue().getClass().getEnumConstants();
                            for (Object val : enumConstants) {
                                if (val.toString().equalsIgnoreCase(IntelliumConfig.getInstance().particleSetting)) {
                                    @SuppressWarnings("unchecked")
                                    SimpleOption<Object> castedOption = (SimpleOption<Object>) particleOption;
                                    castedOption.setValue(val);
                                    LOGGER.info("Applied particle setting from config.");
                                    break;
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Error during Intel GPU optimization", e);
            }
        });
    }
}
