package com.petrolpark.destroy.core.chemistry;

import com.petrolpark.destroy.chemistry.api.util.Constants;
import com.petrolpark.destroy.chemistry.legacy.LegacyMixture;
import com.petrolpark.destroy.chemistry.legacy.LegacyReaction;
import com.petrolpark.destroy.chemistry.legacy.LegacySpecies;
import com.petrolpark.destroy.chemistry.legacy.ReadOnlyMixture;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyMolecules;
import com.petrolpark.destroy.chemistry.minecraft.MixtureFluid;
import com.simibubi.create.foundation.fluid.FluidHelper;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public enum NamedMixtures {
    VODKA(() -> mixByVolume(Map.of(DestroyMolecules.ETHANOL, 400.0, DestroyMolecules.WATER, 600.0))),
    ETHANOL_DISTILLATE(() -> mixByVolume(Map.of(DestroyMolecules.ETHANOL, 300.0, DestroyMolecules.ACETIC_ACID, 30.0, DestroyMolecules.WATER, 670.0))),
    LATEX(() -> pure(DestroyMolecules.ISOPRENE)),
    DISTILLED_WATER(() -> pure(DestroyMolecules.WATER)),

    AMMONIA_SOLUTION(null, () -> mixByVolume(Map.of(DestroyMolecules.AMMONIA, 20.0, DestroyMolecules.WATER, 980.0))),

    BORAX_SOLUTION(() -> solution(DestroyMolecules.WATER, 500, Map.of(
        DestroyMolecules.BORIC_ACID, 0.16969298,
        DestroyMolecules.TETRAHYDROXYBORATE, 0.1201766,
        DestroyMolecules.SODIUM_ION, 0.15305416,
        DestroyMolecules.HYDROXIDE, 2.5182628e-4,
        DestroyMolecules.PROTON, 0.75738716,
        DestroyMolecules.TETRAHYDROXY_TETRABORATE, 0.004059694,
        DestroyMolecules.CHLORIDE, 0.7818937
    ))),

    BRINE(() -> solution(DestroyMolecules.WATER, 500, Map.of(DestroyMolecules.SODIUM_ION, 1., DestroyMolecules.CHLORIDE, 1.))),
    MOLTEN_SULFUR(() -> pure(DestroyMolecules.OCTASULFUR).setTemperature(388)),

    MIXED_XYLENES(() -> mixByConcentration(Map.of(
        DestroyMolecules.PARAXYLENE, 1.,
        DestroyMolecules.METAXYLENE, 1.,
        DestroyMolecules.ORTHOXYLENE, 1.,
        DestroyMolecules.ETHYLBENZENE, 1.
    ))),

    REFINERY_GAS(() -> pressurize(mixByConcentration(Map.of(
        DestroyMolecules.METHANE, 1.,
        DestroyMolecules.ETHENE, 3.,
        DestroyMolecules.PROPENE, 2.,
        DestroyMolecules.BUTADIENE, 2.
    )), 1250000)),

    SODIUM_AMALGAM(() -> solution(DestroyMolecules.MERCURY, 250, Map.of(DestroyMolecules.SODIUM_METAL, 0.9))),
    CHLORINE_SOLUTION(() -> solution(DestroyMolecules.WATER, 250, Map.of(DestroyMolecules.HYDROCHLORIC_ACID, 0.92, DestroyMolecules.HYPOCHLOROUS_ACID, 0.92)))
    ;

    private Lazy<FluidStack> fluid;

    NamedMixtures(Supplier<ReadOnlyMixture> mixtureSupplier) {
        fluid = Lazy.of(() -> MixtureFluid.of(1, mixtureSupplier.get(), "fluid.destroy." + name().toLowerCase()));
    }
    NamedMixtures(String descriptionId, Supplier<ReadOnlyMixture> mixtureSupplier) {
        fluid = Lazy.of(() -> MixtureFluid.of(1, mixtureSupplier.get(), descriptionId));
    }

    public FluidStack get(int amount) {
        return FluidHelper.copyStackWithAmount(fluid.get(), amount);
    }

    public static LegacyMixture solution(LegacySpecies solvent, int totalVolume, Map<LegacySpecies, Double> contents) {
        LegacyMixture mixture = new LegacyMixture();
        contents.forEach((sp, c) -> mixture.addMolecule(sp, c.floatValue()));

        double totalVolumeInLiters = (double)totalVolume / Constants.MILLIBUCKETS_PER_LITER;
        double solventVolumeInLiters = totalVolumeInLiters;

        for (Map.Entry<LegacySpecies, Double> entry : contents.entrySet()) {
            LegacySpecies molecule = entry.getKey();
            double molesOfMolecule = entry.getValue() * totalVolumeInLiters;
            solventVolumeInLiters -= molesOfMolecule / molecule.getPureConcentration();
        }

        mixture.addMolecule(solvent, (float)(solvent.getPureConcentration() * (solventVolumeInLiters / totalVolumeInLiters)));
        return mixture.setTemperature(298f);
    }

    public static LegacyMixture pure(LegacySpecies molecule) {
        return LegacyMixture.pure(molecule).setTemperature(298f);
    }

    public static LegacyMixture mixByConcentration(Map<LegacySpecies, Double> contents) {
        LegacyMixture mixture = new LegacyMixture();
        contents.forEach((sp, c) -> mixture.addMolecule(sp, c.floatValue()));
        mixture.recalculateVolume(1000);
        return mixture;
    }

    public static LegacyMixture mixByVolume(Map<LegacySpecies, Double> contents) {
        return LegacyMixture.mix(contents.entrySet().stream().collect(Collectors.toMap(
            e -> LegacyMixture.pure(e.getKey()),
            e -> e.getValue()
        ))).setTemperature(298f);
    }

    public static LegacyMixture pressurize(LegacyMixture mixture, float targetPressure) {
        float currentPressure = LegacyReaction.GAS_CONSTANT * 1000f * mixture.getTemperature() * mixture.getTotalConcentration();
        mixture.scale(currentPressure / targetPressure);
        return mixture;
    }
}
