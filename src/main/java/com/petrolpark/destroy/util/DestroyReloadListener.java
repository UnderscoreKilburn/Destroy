package com.petrolpark.destroy.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.MoveToPetrolparkLibrary;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

@MoveToPetrolparkLibrary
public abstract class DestroyReloadListener implements ResourceManagerReloadListener {

    private static final Gson GSON = new Gson();

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        beforeReload();
        for (String namespace : resourceManager.getNamespaces()) {
            ResourceLocation location = new ResourceLocation(namespace, getPath() + ".json");
            Optional<Resource> resource = resourceManager.getResource(location);
            if (resource.isPresent()) {
                try (InputStream inputStream = resource.get().open()) {
                    JsonObject jsonObject = GSON.fromJson(new InputStreamReader(inputStream, StandardCharsets.UTF_8), JsonObject.class);
                    forEachNameSpaceJsonFile(jsonObject);
                } catch (IOException e) {
                    Destroy.LOGGER.error("Failed to read Destroy resource: " + location, e);
                };
            };
        };
        afterReload();
    };

    public void beforeReload() {};

    public void afterReload() {};

    public abstract String getPath();

    public abstract void forEachNameSpaceJsonFile(JsonObject jsonObject);
    
};
