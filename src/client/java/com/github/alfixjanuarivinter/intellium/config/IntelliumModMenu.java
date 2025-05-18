package com.github.alfixjanuarivinter.intellium.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;

@Environment(EnvType.CLIENT)
public class IntelliumModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            // Create the config screen builder
            ConfigBuilder builder = ConfigBuilder.create()
                    .setTitle(Text.literal("Intellium Config"));

            // Create the category
            ConfigCategory general = builder.getOrCreateCategory(Text.literal("General"));

            // Entry builder for config entries
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            // Example boolean toggle for enabling Intel optimizations
            general.addEntry(entryBuilder.startBooleanToggle(
                    Text.literal("Enable Intel Optimizations"),
                    IntelliumConfig.getInstance().enableIntelOptimizations
            ).setSaveConsumer(newValue -> {
                IntelliumConfig.getInstance().enableIntelOptimizations = newValue;
                IntelliumConfig.getInstance().save();
            }).build());

            // Add more config entries here as needed
            // e.g. slider, enum selectors, etc.

            return builder.build();
        };
    }
}
