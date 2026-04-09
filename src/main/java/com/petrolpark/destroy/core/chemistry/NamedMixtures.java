package com.petrolpark.destroy.core.chemistry;

import com.petrolpark.destroy.chemistry.api.util.Constants;
import com.petrolpark.destroy.chemistry.legacy.LegacyMixture;
import com.petrolpark.destroy.chemistry.legacy.LegacyReaction;
import com.petrolpark.destroy.chemistry.legacy.LegacySpecies;
import com.petrolpark.destroy.chemistry.legacy.ReadOnlyMixture;
import com.petrolpark.destroy.chemistry.legacy.index.DestroyMolecules;
import com.petrolpark.destroy.chemistry.minecraft.MixtureFluid;
import com.simibubi.create.foundation.fluid.FluidHelper;
import net.createmod.catnip.data.Pair;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.function.Supplier;

public enum NamedMixtures {
    VODKA(() -> mixByVolume(entryList(DestroyMolecules.ETHANOL, 400.0, DestroyMolecules.WATER, 600.0))),
    ETHANOL_DISTILLATE(() -> mixByVolume(entryList(DestroyMolecules.ETHANOL, 300.0, DestroyMolecules.ACETIC_ACID, 30.0, DestroyMolecules.WATER, 670.0))),
    LATEX(() -> pure(DestroyMolecules.ISOPRENE)),
    DISTILLED_WATER(() -> pure(DestroyMolecules.WATER)),

    AMMONIA_SOLUTION(null, () -> mixByVolume(entryList(DestroyMolecules.AMMONIA, 20.0, DestroyMolecules.WATER, 980.0))),

    BORAX_SOLUTION(() -> solution(DestroyMolecules.WATER, 500, entryList(
        DestroyMolecules.BORIC_ACID, 0.16969298,
        DestroyMolecules.TETRAHYDROXYBORATE, 0.1201766,
        DestroyMolecules.SODIUM_ION, 0.15305416,
        DestroyMolecules.HYDROXIDE, 2.5182628e-4,
        DestroyMolecules.PROTON, 0.75738716,
        DestroyMolecules.TETRAHYDROXY_TETRABORATE, 0.004059694,
        DestroyMolecules.CHLORIDE, 0.7818937
    ))),

    BRINE(() -> solution(DestroyMolecules.WATER, 500, entryList(DestroyMolecules.SODIUM_ION, 1., DestroyMolecules.CHLORIDE, 1.))),
    MOLTEN_SULFUR(() -> pure(DestroyMolecules.OCTASULFUR).setTemperature(388)),

    MIXED_XYLENES(() -> mixByConcentration(entryList(
        DestroyMolecules.PARAXYLENE, 1.,
        DestroyMolecules.METAXYLENE, 1.,
        DestroyMolecules.ORTHOXYLENE, 1.,
        DestroyMolecules.ETHYLBENZENE, 1.
    ))),

    REFINERY_GAS(() -> pressurize(mixByConcentration(entryList(
        DestroyMolecules.METHANE, 1.,
        DestroyMolecules.ETHENE, 3.,
        DestroyMolecules.PROPENE, 2.,
        DestroyMolecules.BUTADIENE, 2.
    )), 1250000)),

    SODIUM_AMALGAM(() -> solution(DestroyMolecules.MERCURY, 250, entryList(DestroyMolecules.SODIUM_METAL, 0.9))),
    CHLORINE_SOLUTION(() -> solution(DestroyMolecules.WATER, 250, entryList(DestroyMolecules.HYDROCHLORIC_ACID, 0.92, DestroyMolecules.HYPOCHLOROUS_ACID, 0.92)))
    ;

    private final Lazy<FluidStack> fluid;

    NamedMixtures(Supplier<ReadOnlyMixture> mixtureSupplier) {
        fluid = Lazy.of(() -> MixtureFluid.of(1, mixtureSupplier.get(), "fluid.destroy." + name().toLowerCase()));
    }
    NamedMixtures(String descriptionId, Supplier<ReadOnlyMixture> mixtureSupplier) {
        fluid = Lazy.of(() -> MixtureFluid.of(1, mixtureSupplier.get(), descriptionId));
    }

    public FluidStack get(int amount) {
        return FluidHelper.copyStackWithAmount(fluid.get(), amount);
    }

    public static LegacyMixture solution(LegacySpecies solvent, int totalVolume, List<Pair<LegacySpecies, Double>> contents) {
        LegacyMixture mixture = new LegacyMixture();
        contents.forEach(e -> mixture.addMolecule(e.getFirst(), e.getSecond().floatValue()));

        double totalVolumeInLiters = (double)totalVolume / Constants.MILLIBUCKETS_PER_LITER;
        double solventVolumeInLiters = totalVolumeInLiters;

        for (Pair<LegacySpecies, Double> entry : contents) {
            LegacySpecies molecule = entry.getFirst();
            double molesOfMolecule = entry.getSecond() * totalVolumeInLiters;
            solventVolumeInLiters -= molesOfMolecule / molecule.getPureConcentration();
        }

        mixture.addMolecule(solvent, (float)(solvent.getPureConcentration() * (solventVolumeInLiters / totalVolumeInLiters)));
        return mixture.setTemperature(298f);
    }

    public static LegacyMixture pure(LegacySpecies molecule) {
        return LegacyMixture.pure(molecule).setTemperature(298f);
    }

    public static LegacyMixture mixByConcentration(List<Pair<LegacySpecies, Double>> contents) {
        LegacyMixture mixture = new LegacyMixture();
        contents.forEach(e -> mixture.addMolecule(e.getFirst(), e.getSecond().floatValue()));
        mixture.recalculateVolume(1000);
        return mixture;
    }

    public static LegacyMixture mixByVolume(List<Pair<LegacySpecies, Double>> contents) {
        return LegacyMixture.mix(contents.stream().map(p -> Pair.of(LegacyMixture.pure(p.getFirst()), p.getSecond())).toList()).setTemperature(298f);
    }

    public static LegacyMixture pressurize(LegacyMixture mixture, float targetPressure) {
        float currentPressure = LegacyReaction.GAS_CONSTANT * 1000f * mixture.getTemperature() * mixture.getTotalConcentration();
        mixture.scale(currentPressure / targetPressure);
        return mixture;
    }

    private static <K,V> List<Pair<K, V>> entryList(K k1, V v1) {
        return List.of(Pair.of(k1, v1));
    }
    private static <K,V> List<Pair<K, V>> entryList(K k1, V v1, K k2, V v2) {
        return List.of(Pair.of(k1, v1), Pair.of(k2, v2));
    }
    private static <K,V> List<Pair<K, V>> entryList(K k1, V v1, K k2, V v2, K k3, V v3) {
        return List.of(Pair.of(k1, v1), Pair.of(k2, v2), Pair.of(k3, v3));
    }
    private static <K,V> List<Pair<K, V>> entryList(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return List.of(Pair.of(k1, v1), Pair.of(k2, v2), Pair.of(k3, v3), Pair.of(k4, v4));
    }
    private static <K,V> List<Pair<K, V>> entryList(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return List.of(Pair.of(k1, v1), Pair.of(k2, v2), Pair.of(k3, v3), Pair.of(k4, v4), Pair.of(k5, v5));
    }
    private static <K,V> List<Pair<K, V>> entryList(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        return List.of(Pair.of(k1, v1), Pair.of(k2, v2), Pair.of(k3, v3), Pair.of(k4, v4), Pair.of(k5, v5), Pair.of(k6, v6));
    }
    private static <K,V> List<Pair<K, V>> entryList(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        return List.of(Pair.of(k1, v1), Pair.of(k2, v2), Pair.of(k3, v3), Pair.of(k4, v4), Pair.of(k5, v5), Pair.of(k6, v6), Pair.of(k7, v7));
    }
    private static <K,V> List<Pair<K, V>> entryList(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
        return List.of(Pair.of(k1, v1), Pair.of(k2, v2), Pair.of(k3, v3), Pair.of(k4, v4), Pair.of(k5, v5), Pair.of(k6, v6), Pair.of(k7, v7), Pair.of(k8, v8));
    }
}
