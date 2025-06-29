package com.petrolpark.destroy.core.data;

import com.petrolpark.destroy.Destroy;
import com.petrolpark.destroy.content.processing.ageing.AgingBarrelBlock;
import com.petrolpark.destroy.content.processing.distillation.BubbleCapBlock;
import com.petrolpark.destroy.content.processing.sieve.MechanicalSieveBlock;
import com.petrolpark.destroy.core.block.FlippableRotatedPillarBlock;
import com.petrolpark.destroy.core.chemistry.vat.uv.BlacklightBlock;
import com.simibubi.create.content.contraptions.chassis.RadialChassisBlock;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;

public class DestroyBlockStateGen {
    public static <B extends AgingBarrelBlock> NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> agingBarrel() {
        return (c, p) -> {
            MultiPartBlockStateBuilder builder = p.getMultipartBuilder(c.get());
            for (Direction d : Iterate.horizontalDirections) {
                builder.part()
                    .modelFile(p.models().getExistingFile(p.modLoc("block/" + c.getName() + "/open")))
                    .rotationY((((int) d.toYRot()) + 180) % 360)
                    .addModel()
                    .condition(B.FACING, d)
                    .condition(B.IS_OPEN, true)
                    .end();
            }

            for (int i = 0; i < 5; i++) {
                builder.part()
                    .modelFile(p.models().getExistingFile(p.modLoc("block/" + c.getName() + "/balloon_" + i)))
                    .addModel()
                    .condition(B.PROGRESS, i)
                    .condition(B.IS_OPEN, false)
                    .end();
            }

            builder.part()
                .modelFile(p.models().getExistingFile(p.modLoc("block/" + c.getName() + "/closed")))
                .addModel()
                .condition(B.IS_OPEN, false)
                .end();
        };
    }

    public static <B extends BubbleCapBlock> NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> bubbleCap() {
        return (c,p) -> {
            final String[] names = {"/neither", "/top", "/bottom", "/both"};
            p.getVariantBuilder(c.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(p.models().getExistingFile(p.modLoc("block/" + c.getName()+ names[(state.getValue(B.TOP) ? 1 : 0) + (state.getValue(B.BOTTOM) ? 2 : 0)])))
                .build()
            );
        };
    }

    public static <B extends BlacklightBlock> NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> blacklight() {
        return (c, p) ->
            p.getVariantBuilder(c.get()).forAllStatesExcept(state -> {
                Direction dir = state.getValue(BlockStateProperties.FACING);
                boolean flipped = state.getValue(B.FLIPPED);
                String model = "block/blacklight";
                int rX, rY;

                if(flipped && dir.getAxis().isHorizontal()) {
                    model = "block/blacklight_flipped";
                    rX = 0;
                    rY = ((int)dir.toYRot() + 180) % 360;
                } else {
                    rX = dir == Direction.DOWN ? 0 : dir.getAxis().isHorizontal() ? 90 : 180;
                    rY = dir.getAxis().isVertical() ? (flipped ? 90 : 0) : ((int)dir.toYRot()) % 360;
                }

                return ConfiguredModel.builder()
                    .modelFile(p.models().getExistingFile(p.modLoc(model)))
                    .rotationX(rX).rotationY(rY)
                    .build();
            }, BlockStateProperties.WATERLOGGED);
    }

    // thanks petrol
    public static <B extends Block> NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> bullshitHorizontalAxisBlock(BooleanProperty xProperty, boolean customItem) {
        return (c, p) -> p.getVariantBuilder(c.get()).forAllStates(state ->
                ConfiguredModel.builder()
                    .modelFile(customItem ? AssetLookup.partialBaseModel(c, p) : AssetLookup.standardModel(c, p))
                    .rotationY(state.getValue(xProperty) ? 90 : 0)
                    .build()
        );
    }

    public static <B extends Block> NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> nothing() {
        return (c, p) -> p.getVariantBuilder(c.get()).partialState()
            .setModels(ConfiguredModel.builder().modelFile(p.models().getExistingFile(p.mcLoc("block/air"))).build());
    }

    public static <B extends Block> NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> simpleBlock() {
        return (c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.standardModel(c, p));
    }
    public static <B extends Block> NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> simpleBlock(String path) {
        return (c, p) -> p.simpleBlock(c.getEntry(), p.models().getExistingFile(p.modLoc("block/" + path)));
    }
    public static <B extends Block> NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> simpleBlock(ResourceLocation fullPath) {
        return (c, p) -> p.simpleBlock(c.getEntry(), p.models().getExistingFile(fullPath));
    }

    public static <B extends Block> NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> cubeBottomTop(String path) {
        return (c, p) -> p.simpleBlock(c.get(), p.models().cubeBottomTop(c.getName(),
            p.modLoc("block/" + path + "_side"),
            p.modLoc("block/" + path + "_bottom"),
            p.modLoc("block/" + path + "_top")));
    }

    public static <B extends RotatedPillarBlock> NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> rotatedPillar(String path) {
        return (c, p) -> p.axisBlock(c.get(),
            p.modLoc("block/" + path + "_side"),
            p.modLoc("block/" + path + "_end"));
    }
    public static <B extends RotatedPillarBlock> NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> rotatedPillar(ResourceLocation side, ResourceLocation end) {
        return (c, p) -> p.axisBlock(c.get(), side, end);
    }

    public static <B extends FlippableRotatedPillarBlock> NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> flippableRotatedPillar(String path) {
        return (c, p) -> {
            ModelFile model = p.models().withExistingParent(c.getName(), Destroy.asResource("block/cube_flippable_rotated"))
                    .texture("end", p.modLoc("block/" + path + "_end"))
                    .texture("side1", p.modLoc("block/" + path + "_side_1"))
                    .texture("side2", p.modLoc("block/" + path + "_side_2"));

            ModelFile model_flipped = p.models().withExistingParent(c.getName() + "_flipped", Destroy.asResource("block/cube_flippable_rotated_flipped"))
                .texture("end", p.modLoc("block/" + path + "_end"))
                .texture("side1", p.modLoc("block/" + path + "_side_1"))
                .texture("side2", p.modLoc("block/" + path + "_side_2"));

            p.getVariantBuilder(c.get())
                .partialState().with(FlippableRotatedPillarBlock.AXIS, Direction.Axis.X).with(FlippableRotatedPillarBlock.FLIPPED, false)
                .modelForState().rotationX(90).rotationY(90).modelFile(model).addModel()
                .partialState().with(FlippableRotatedPillarBlock.AXIS, Direction.Axis.Y).with(FlippableRotatedPillarBlock.FLIPPED, false)
                .modelForState().rotationY(90).modelFile(model).addModel()
                .partialState().with(FlippableRotatedPillarBlock.AXIS, Direction.Axis.Z).with(FlippableRotatedPillarBlock.FLIPPED, false)
                .modelForState().rotationX(90).modelFile(model).addModel()
                .partialState().with(FlippableRotatedPillarBlock.AXIS, Direction.Axis.X).with(FlippableRotatedPillarBlock.FLIPPED, true)
                .modelForState().rotationX(90).rotationY(90).modelFile(model_flipped).addModel()
                .partialState().with(FlippableRotatedPillarBlock.AXIS, Direction.Axis.Y).with(FlippableRotatedPillarBlock.FLIPPED, true)
                .modelForState().rotationY(90).modelFile(model_flipped).addModel()
                .partialState().with(FlippableRotatedPillarBlock.AXIS, Direction.Axis.Z).with(FlippableRotatedPillarBlock.FLIPPED, true)
                .modelForState().rotationX(90).modelFile(model_flipped).addModel();
        };
    }

    public static <B extends Block> NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> periodicTableSolidBlock(ResourceLocation front, ResourceLocation side) {
        return (c, p) -> p.horizontalBlock(c.get(), p.models()
            .withExistingParent(c.getName(), p.modLoc("block/periodic_table/base"))
            .texture("side", side)
            .texture("front", front)
        );
    }

    public static <B extends Block> NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> periodicTableFluidBlock(ResourceLocation front, ResourceLocation fluid) {
        return (c, p) -> p.horizontalBlock(c.get(), p.models()
            .withExistingParent(c.getName(), p.modLoc("block/periodic_table/tank_base"))
            .texture("fluid", fluid)
            .texture("front", front)
        );
    }
}
