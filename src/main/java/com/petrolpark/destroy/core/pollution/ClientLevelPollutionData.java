package com.petrolpark.destroy.core.pollution;

import com.petrolpark.destroy.core.pollution.Pollution.PollutionType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher.RenderChunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientLevelPollutionData {
    
    private static Pollution levelPollution;

    private static Integer lastRenderedSmogLevel = null;

    public static void setLevelPollution(Pollution levelPollution) {
        ClientLevelPollutionData.levelPollution = levelPollution;
        if (lastRenderedSmogLevel == null || Math.abs(lastRenderedSmogLevel - levelPollution.get(PollutionType.SMOG)) >= 1000) {
            Minecraft mc = Minecraft.getInstance();
            for (RenderChunk chunk : mc.levelRenderer.viewArea.chunks) chunk.setDirty(false); // Re-render all chunks
            lastRenderedSmogLevel = levelPollution.get(PollutionType.SMOG);
        };
    };

    public static Pollution getLevelPollution() {
        return levelPollution;
    };
}
