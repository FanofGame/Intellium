package com.github.alfixjanuarivinter.intellium.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class IntelliumConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static IntelliumConfig INSTANCE;

    public boolean enableIntelOptimizations = true;
    public int viewDistance = 6;
    public String particleSetting = "minimal";

    private File configFile;

    private IntelliumConfig(File configFile) {
        this.configFile = configFile;
    }

    public static IntelliumConfig load(File configFile) {
        if (INSTANCE == null) {
            INSTANCE = new IntelliumConfig(configFile);
            if (configFile.exists()) {
                try (FileReader reader = new FileReader(configFile)) {
                    INSTANCE = GSON.fromJson(reader, IntelliumConfig.class);
                    INSTANCE.configFile = configFile;
                } catch (IOException e) {
                    System.err.println("Failed to load config.json, using defaults");
                    e.printStackTrace();
                }
            } else {
                INSTANCE.save();  // save default config if none exists
            }
        }
        return INSTANCE;
    }

    public void save() {
        try (FileWriter writer = new FileWriter(configFile)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            System.err.println("Failed to save config.json");
            e.printStackTrace();
        }
    }

    // Rename from get() to getInstance()
    public static IntelliumConfig getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Config not loaded yet! Call load() first.");
        }
        return INSTANCE;
    }
}
