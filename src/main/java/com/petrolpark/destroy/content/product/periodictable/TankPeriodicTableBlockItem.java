package com.petrolpark.destroy.content.product.periodictable;

public class TankPeriodicTableBlockItem extends PeriodicTableBlockItem {

    public TankPeriodicTableBlockItem(TankPeriodicTableBlock block, Properties properties) {
        super(block, properties);
    };

    public int getColor() {
        return ((TankPeriodicTableBlock)getBlock()).color;
    };
    
};
