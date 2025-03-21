package com.petrolpark.destroy.chemistry.legacy.index.group;

import com.petrolpark.destroy.chemistry.legacy.LegacyAtom;
import com.petrolpark.destroy.chemistry.legacy.LegacyFunctionalGroup;
import com.petrolpark.destroy.chemistry.legacy.LegacyFunctionalGroupType;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyGroupTypes;

public class AcetylideGroup extends LegacyFunctionalGroup<AcetylideGroup> {

    public final LegacyAtom carbonWithCharge;
    public final LegacyAtom neutralCarbon;

    public AcetylideGroup(LegacyAtom carbonWithCharge, LegacyAtom neutralCarbon) {
        this.carbonWithCharge = carbonWithCharge;
        this.neutralCarbon = neutralCarbon;
    };

    @Override
    public LegacyFunctionalGroupType<? extends AcetylideGroup> getType() {
        return DestroyGroupTypes.ACETYLIDE;
    };
    
};
