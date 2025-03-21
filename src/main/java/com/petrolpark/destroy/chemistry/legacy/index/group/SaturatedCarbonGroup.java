package com.petrolpark.destroy.chemistry.legacy.index.group;

import com.petrolpark.destroy.chemistry.legacy.LegacyAtom;
import com.petrolpark.destroy.chemistry.legacy.LegacyFunctionalGroup;

public abstract class SaturatedCarbonGroup extends LegacyFunctionalGroup<SaturatedCarbonGroup> {

    public final LegacyAtom highDegreeCarbon;
    public final LegacyAtom lowDegreeCarbon;
    public final boolean noHydrogens;

    public SaturatedCarbonGroup(LegacyAtom highDegreeCarbon, LegacyAtom lowDegreeCarbon, Boolean noHydrogens) {
        super();
        this.highDegreeCarbon = highDegreeCarbon;
        this.lowDegreeCarbon = lowDegreeCarbon;
        this.noHydrogens = noHydrogens;
    };

};
