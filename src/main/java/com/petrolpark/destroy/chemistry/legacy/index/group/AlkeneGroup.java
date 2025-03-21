package com.petrolpark.destroy.chemistry.legacy.index.group;

import com.petrolpark.destroy.chemistry.legacy.LegacyAtom;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyGroupTypes;
import com.petrolpark.destroy.chemistry.legacy.LegacyFunctionalGroupType;

public class AlkeneGroup extends SaturatedCarbonGroup {

    public AlkeneGroup(LegacyAtom highDegreeCarbon, LegacyAtom lowDegreeCarbon, Boolean noHydrogens) {
        super(highDegreeCarbon, lowDegreeCarbon, noHydrogens);
    };

    @Override
    public LegacyFunctionalGroupType<? extends SaturatedCarbonGroup> getType() {
        return DestroyGroupTypes.ALKENE;
    };
    
}
