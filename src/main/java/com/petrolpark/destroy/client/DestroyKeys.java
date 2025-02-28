package com.petrolpark.destroy.client;

import org.lwjgl.glfw.GLFW;

import com.petrolpark.destroy.Destroy;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public enum DestroyKeys {
    
    HOTBAR_SLOT_9("hotbar.10", GLFW.GLFW_KEY_0),
	HOTBAR_SLOT_10("hotbar.11", GLFW.GLFW_KEY_MINUS),
	HOTBAR_SLOT_11("hotbar.12", GLFW.GLFW_KEY_EQUAL),
	HOTBAR_SLOT_12("hotbar.13", -1),
	HOTBAR_SLOT_13("hotbar.14", -1),
	HOTBAR_SLOT_14("hotbar.15", -1),
	HOTBAR_SLOT_15("hotbar.16", -1),
	HOTBAR_SLOT_16("hotbar.17", -1);

	public KeyMapping keybind;
	private String description;
	private int key;

	private DestroyKeys(String description, int defaultKey) {
		this.description = Destroy.MOD_ID + ".key." + description;
		this.key = defaultKey;
	};

    @SubscribeEvent
	public static void register(RegisterKeyMappingsEvent event) {
		for (DestroyKeys key : values()) {
			key.keybind = new KeyMapping(key.description, key.key, Destroy.NAME);
			event.register(key.keybind);
		};
	}
};
