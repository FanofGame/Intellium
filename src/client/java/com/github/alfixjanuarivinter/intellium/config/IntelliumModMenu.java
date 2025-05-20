package com.github.alfixjanuarivinter.intellium.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class IntelliumModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            IntelliumConfig config = IntelliumConfig.getInstance();
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.literal("Intellium Config"))
                    .setSavingRunnable(config::save);

            ConfigCategory general = builder.getOrCreateCategory(Text.literal("General"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            general.addEntry(entryBuilder.startBooleanToggle(Text.literal("Enable Intel Optimizations"), config.enableIntelOptimizations)
                    .setSaveConsumer(val -> config.enableIntelOptimizations = val)
                    .build());

            general.addEntry(entryBuilder.startIntSlider(Text.literal("View Distance"), config.viewDistance, 2, 16)
                    .setSaveConsumer(val -> config.viewDistance = val)
                    .build());

            general.addEntry(entryBuilder.startTextField(Text.literal("Particle Setting (minimal / decreased / all)"), config.particleSetting)
                    .setSaveConsumer(val -> config.particleSetting = val.toLowerCase())
                    .build());

            builder.setTransparentBackground(true);
            return builder.build();
        };
    }

}
