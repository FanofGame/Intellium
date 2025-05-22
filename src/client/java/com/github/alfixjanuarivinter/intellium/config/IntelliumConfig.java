package com.github.alfixjanuarivinter.intellium.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.JsonToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class IntelliumConfig {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(File.class, new FileTypeAdapter())
            .create();
    private static IntelliumConfig INSTANCE;

    // Configuration fields with default values
    public boolean enableIntelOptimizations = true;
    public int viewDistance = 6;
    public String particleSetting = "minimal";
    public static boolean enableOpenGLOptimizations = true;

    private File configFile;

    // Private constructor to enforce singleton pattern and loading via load()
    private IntelliumConfig(File configFile) {
        this.configFile = configFile;
    }

    public static IntelliumConfig load(File configFile) {
        if (INSTANCE == null) {
            INSTANCE = new IntelliumConfig(configFile);

            if (configFile.exists()) {
                try (FileReader reader = new FileReader(configFile)) {
                    IntelliumConfig loaded = GSON.fromJson(reader, IntelliumConfig.class);
                    if (loaded != null) {
                        INSTANCE = loaded;
                        INSTANCE.configFile = configFile;  // reassign configFile reference
                    } else {
                        System.err.println("[IntelliumConfig] Config file was empty or invalid, using defaults.");
                    }
                } catch (IOException e) {
                    System.err.println("[IntelliumConfig] Failed to read config file. Using defaults.");
                    e.printStackTrace();
                } catch (com.google.gson.JsonSyntaxException e) {
                    System.err.println("[IntelliumConfig] Config file contains invalid JSON. Using defaults.");
                    e.printStackTrace();
                }
            } else {
                // Ensure parent directories exist if file does not exist
                File parent = configFile.getParentFile();
                if (parent != null && !parent.exists() && !parent.mkdirs()) {
                    System.err.println("[IntelliumConfig] Failed to create config directory.");
                }
            }

            // Always save after loading to ensure file exists and is up to date
            INSTANCE.save();
        }
        return INSTANCE;
    }

    public void save() {
        try (FileWriter writer = new FileWriter(configFile)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            System.err.println("[IntelliumConfig] Failed to save config.");
            e.printStackTrace();
        }
    }

    public static IntelliumConfig getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("[IntelliumConfig] Config not loaded! Call load() first.");
        }
        return INSTANCE;
    }

    // Custom TypeAdapter for File objects
    private static class FileTypeAdapter extends TypeAdapter<File> {
        @Override
        public void write(JsonWriter out, File value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value.getPath());
            }
        }

        @Override
        public File read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return new File(in.nextString());
        }
    }
}
