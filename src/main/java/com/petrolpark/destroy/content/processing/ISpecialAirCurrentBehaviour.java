package com.petrolpark.destroy.content.processing;

import com.simibubi.create.content.kinetics.fan.AirCurrent;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;

import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;

/**
 * Implement this and extend {@link TransportedItemStackHandlerBehaviour} for more liberal Air Current usage.
 */
public interface ISpecialAirCurrentBehaviour {
    
    public void tickAir(AirCurrent airCurrent, FanProcessingType processingType);
};
