package com.petrolpark.destroy.content.tool.swissarmyknife;

public interface IPreferredSwissArmyKnifeToolEntity {
    /**
     * Get the tool to which a Swiss Army Knife should switch if this entity is targeted.
     * This is only called client-side.
     * @param shiftDown Whether the Swiss Army Knife's user has shift down
     */
    public SwissArmyKnifeItem.Tool getToolForSwissArmyKnife(boolean shiftDown);
};
