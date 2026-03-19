package com.petrolpark.destroy;

import static com.petrolpark.destroy.Destroy.REGISTRATE;
import static com.simibubi.create.AllTags.forgeItemTag;
import static com.simibubi.create.AllTags.forgeBlockTag;
import static com.simibubi.create.api.behaviour.display.DisplaySource.displaySource;
import static com.simibubi.create.foundation.data.CreateRegistrate.casingConnectivity;
import static com.simibubi.create.foundation.data.CreateRegistrate.connectedTextures;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

import com.petrolpark.destroy.config.DestroyAllConfigs;
import com.petrolpark.destroy.config.DestroyStressConfigs;
import com.petrolpark.destroy.content.logistics.creativepump.CreativePumpBlock;
import com.petrolpark.destroy.content.logistics.siphon.SiphonBlock;
import com.petrolpark.destroy.content.oil.pumpjack.PumpjackBlock;
import com.petrolpark.destroy.content.oil.pumpjack.PumpjackBlockItem;
import com.petrolpark.destroy.content.oil.pumpjack.PumpjackCamBlock;
import com.petrolpark.destroy.content.oil.pumpjack.PumpjackStructuralBlock;
import com.petrolpark.destroy.content.processing.ageing.AgingBarrelBlock;
import com.petrolpark.destroy.content.processing.centrifuge.CentrifugeBlock;
import com.petrolpark.destroy.content.processing.cooler.CoolerBlock;
import com.petrolpark.destroy.content.processing.distillation.BubbleCapBlock;
import com.petrolpark.destroy.content.processing.dynamo.DynamoBlock;
import com.petrolpark.destroy.content.processing.dynamo.arcfurnace.ArcFurnaceLidBlock;
import com.petrolpark.destroy.content.processing.extrusion.ExtrusionDieBlock;
import com.petrolpark.destroy.content.processing.glassblowing.BlowpipeBlock;
import com.petrolpark.destroy.content.processing.glassblowing.BlowpipeItem;
import com.petrolpark.destroy.content.processing.moltenblock.BorosilicateGlassFiberBlock;
import com.petrolpark.destroy.content.processing.moltenblock.FastCoolingMoltenPillarBlock;
import com.petrolpark.destroy.content.processing.moltenblock.MoltenBorosilicateGlassBlock;
import com.petrolpark.destroy.content.processing.moltenblock.MoltenStainlessSteelBlock;
import com.petrolpark.destroy.content.processing.phytomining.HeftyBeetrootBlock;
import com.petrolpark.destroy.content.processing.phytomining.MagicBeetrootShootsBlock;
import com.petrolpark.destroy.content.processing.sieve.MechanicalSieveBlock;
import com.petrolpark.destroy.content.processing.treetap.TreeTapBlock;
import com.petrolpark.destroy.content.processing.trypolithography.keypunch.KeypunchBlock;
import com.petrolpark.destroy.content.product.alcohol.UrineCauldronBlock;
import com.petrolpark.destroy.content.product.periodictable.PeriodicTableBlock;
import com.petrolpark.destroy.content.product.periodictable.PeriodicTableBlockItem;
import com.petrolpark.destroy.content.product.periodictable.TankPeriodicTableBlock;
import com.petrolpark.destroy.content.product.periodictable.TankPeriodicTableBlockItem;
import com.petrolpark.destroy.content.redstone.programmer.RedstoneProgrammerBlock;
import com.petrolpark.destroy.content.redstone.programmer.RedstoneProgrammerBlockItem;
import com.petrolpark.destroy.content.sandcastle.SandCastleBlock;
import com.petrolpark.destroy.core.block.FlippableRotatedPillarBlock;
import com.petrolpark.destroy.core.block.FullyGrownCropBlock;
import com.petrolpark.destroy.core.block.copycat.CopycatFullBlockModel;
import com.petrolpark.destroy.core.chemistry.storage.ElementTankBlock;
import com.petrolpark.destroy.core.chemistry.storage.SimplePlaceableMixtureTankBlock;
import com.petrolpark.destroy.core.chemistry.storage.SimplePlaceableMixtureTankBlockItem;
import com.petrolpark.destroy.core.chemistry.storage.measuringcylinder.MeasuringCylinderBlock;
import com.petrolpark.destroy.core.chemistry.storage.measuringcylinder.MeasuringCylinderBlockItem;
import com.petrolpark.destroy.core.chemistry.storage.testtube.TestTubeRackBlock;
import com.petrolpark.destroy.core.chemistry.vat.VatControllerBlock;
import com.petrolpark.destroy.core.chemistry.vat.VatControllerModel;
import com.petrolpark.destroy.core.chemistry.vat.VatSideBlock;
import com.petrolpark.destroy.core.chemistry.vat.observation.colorimeter.ColorimeterBlock;
import com.petrolpark.destroy.core.chemistry.vat.uv.BlacklightBlock;
import com.petrolpark.destroy.datagen.DestroyBlockStateGen;
import com.petrolpark.destroy.core.explosion.DynamiteBlock;
import com.petrolpark.destroy.core.explosion.PrimeableBombBlock;
import com.petrolpark.destroy.core.explosion.PrimedBombEntity;
import com.petrolpark.destroy.core.explosion.mixedexplosive.MixedExplosiveBlock;
import com.petrolpark.destroy.core.explosion.mixedexplosive.MixedExplosiveBlockItem;
import com.petrolpark.destroy.core.item.CombustibleBlockItem;
import com.petrolpark.destroy.core.pollution.PollutometerBlock;
import com.petrolpark.destroy.core.pollution.catalyticconverter.CatalyticConverterBlock;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTags;
import com.simibubi.create.AllTags.AllItemTags;
import com.simibubi.create.AllTags.AllBlockTags;
import com.simibubi.create.Create;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.fluids.PipeAttachmentModel;
import com.simibubi.create.content.processing.AssemblyOperatorBlockItem;
import com.simibubi.create.foundation.block.connected.SimpleCTBehaviour;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.BuilderTransformers;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.Tags;

import java.util.Map;

public class DestroyBlocks {

    // BLOCK ENTITIES

    public static final BlockEntry<AgingBarrelBlock> AGING_BARREL = REGISTRATE.block("aging_barrel", AgingBarrelBlock::new)
        .initialProperties(SharedProperties::wooden)
        .properties(p -> p
            .mapColor(MapColor.COLOR_BROWN)
            .noOcclusion()
        ).transform(TagGen.axeOnly())
        .blockstate(DestroyBlockStateGen.agingBarrel())
        .item()
        .tag(DestroyTags.Items.LIABLE_TO_CHANGE.tag)
        .transform(customItemModel())
        .register();

    public static final BlockEntry<BlowpipeBlock> BLOWPIPE = REGISTRATE.block("blowpipe", BlowpipeBlock::new)
        .initialProperties(AllBlocks.SHAFT)
        .properties(p -> p
            .mapColor(MapColor.NONE)
        ).blockstate(BlockStateGen.directionalBlockProvider(false))
        .item(BlowpipeItem::new)
        .build()
        .register();

    public static final BlockEntry<BubbleCapBlock> BUBBLE_CAP = REGISTRATE.block("bubble_cap", BubbleCapBlock::new)
        .initialProperties(SharedProperties::copperMetal)
        .properties(p -> p
            .mapColor(MapColor.COLOR_ORANGE)
            .noOcclusion()
        ).transform(displaySource(DestroyDisplaySources.BUBBLE_CAP))
        .transform(TagGen.pickaxeOnly())
        .blockstate(DestroyBlockStateGen.bubbleCap())
        .item()
        .transform(customItemModel("_", "both"))
        .register();

    public static final BlockEntry<CatalyticConverterBlock> CATALYTIC_CONVERTER = REGISTRATE.block("catalytic_converter", CatalyticConverterBlock::new)
        .initialProperties(SharedProperties::copperMetal)
        .transform(TagGen.pickaxeOnly())
        .blockstate((c, p) -> p.directionalBlock(c.get(), AssetLookup.standardModel(c, p)))
        .item()
        .build()
        .register();

    public static final BlockEntry<CentrifugeBlock> CENTRIFUGE = REGISTRATE.block("centrifuge", CentrifugeBlock::new)
        .initialProperties(SharedProperties::copperMetal)
        .properties(p -> p
            .mapColor(MapColor.COLOR_ORANGE)
            .noOcclusion()
        ).transform(displaySource(DestroyDisplaySources.CENTRIFUGE_INPUT))
        .transform(displaySource(DestroyDisplaySources.CENTRIFUGE_DENSE_OUTPUT))
        .transform(displaySource(DestroyDisplaySources.CENTRIFUGE_LIGHT_OUTPUT))
        .blockstate(BlockStateGen.horizontalBlockProvider(true))
        .transform(TagGen.pickaxeOnly())
        .transform(DestroyStressConfigs.setImpact(5.0))
        .item()
        .transform(customItemModel())
        .register();

    public static final BlockEntry<ColorimeterBlock> COLORIMETER = REGISTRATE.block("colorimeter", ColorimeterBlock::new)
        .initialProperties(() -> Blocks.OBSERVER)
        .transform(TagGen.pickaxeOnly())
        .transform(displaySource(DestroyDisplaySources.COLORIMETER))
        .blockstate((c,p) -> {
            p.horizontalBlock(c.get(), state -> p.models().getExistingFile(p.modLoc("block/" + c.getName()
                + (state.getValue(ColorimeterBlock.BLUSHING) ? "_blushing" : "")
                + (state.getValue(ColorimeterBlock.POWERED) ? "_powered" : ""))));
        })
        .item()
        .build()
        .register();

    public static final BlockEntry<CoolerBlock> COOLER = REGISTRATE.block("cooler", CoolerBlock::new)
        .initialProperties(SharedProperties::stone)
        .properties(p -> p
            .mapColor(MapColor.COLOR_GRAY)
            .noOcclusion()
            .sound(DestroySoundTypes.COOLER)
        ).transform(TagGen.pickaxeOnly())
        .blockstate(DestroyBlockStateGen.simpleBlock("cooler/brazier"))
        .item()
        .transform(customItemModel())
        .register();

    public static final BlockEntry<CreativePumpBlock> CREATIVE_PUMP = REGISTRATE.block("creative_pump", CreativePumpBlock::new)
        .initialProperties(AllBlocks.MECHANICAL_PUMP)
        .properties(p -> p
            .mapColor(MapColor.COLOR_PURPLE)
            .forceSolidOn()
        ).transform(TagGen.pickaxeOnly())
        .blockstate(BlockStateGen.directionalBlockProviderIgnoresWaterlogged(true))
        .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
        .item()
        .properties(p -> p
            .rarity(Rarity.EPIC)
        ).transform(customItemModel())
        .register();

    public static final BlockEntry<MixedExplosiveBlock> CUSTOM_EXPLOSIVE_MIX = REGISTRATE.block("custom_explosive_mix", MixedExplosiveBlock::new)
        .initialProperties(() -> Blocks.TNT)
        .properties(p -> p
            .mapColor(MapColor.SNOW)
        ).blockstate(DestroyBlockStateGen.simpleBlock())
        .item(MixedExplosiveBlockItem::new)
        .onRegister(registerPrimeableBombDispenserBehaviour())
        .build()
        .register();

    public static final BlockEntry<DynamoBlock> DYNAMO = REGISTRATE.block("dynamo", DynamoBlock::new)
        .initialProperties(SharedProperties::softMetal)
        .properties(p -> p
            .mapColor(MapColor.GOLD)
            .noOcclusion()
        ).transform(TagGen.pickaxeOnly())
        .transform(DestroyStressConfigs.setImpact(6.0))
        .blockstate((c,p) -> {
            BlockStateGen.horizontalAxisBlock(c, p, state -> p.models().getExistingFile(p.modLoc("block/" + c.getName()
                + (state.getValue(DynamoBlock.ARC_FURNACE) ? "/arc_furnace_dynamo_block" : "/block"))));
        })
        .item(AssemblyOperatorBlockItem::new)
        .tag(DestroyTags.Items.LIABLE_TO_CHANGE.tag)
        .transform(customItemModel())
        .register();

    public static final BlockEntry<ArcFurnaceLidBlock> ARC_FURNACE_LID = REGISTRATE.block("arc_furnace_lid", ArcFurnaceLidBlock::new)
        .initialProperties(AllBlocks.BASIN)
        .transform(TagGen.pickaxeOnly())
        .blockstate((c, p) -> BlockStateGen.horizontalAxisBlock(c, p, state -> p.models().getExistingFile(p.modLoc("block/dynamo/arc_furnace_lid_block"))))
        .register();

    public static final BlockEntry<ElementTankBlock> ELEMENT_TANK = REGISTRATE.block("element_tank", ElementTankBlock::new)
        .initialProperties(SharedProperties::stone)
        .properties(p -> p
            .strength(4f)
            .sound(SoundType.GLASS)
            .noOcclusion()
            .isValidSpawn(DestroyBlocks::never)
            .isRedstoneConductor(DestroyBlocks::never)
            .isSuffocating(DestroyBlocks::never)
            .isViewBlocking(DestroyBlocks::never)
        ).transform(TagGen.pickaxeOnly())
        .addLayer(() -> RenderType::cutoutMipped)
        .blockstate((c, p) -> p.horizontalBlock(c.get(),
            Destroy.asResource("block/borosilicate_glass"),
            Destroy.asResource("block/element_tank_side"),
            Destroy.asResource("block/element_tank_end")))
        .item()
        .build()
        .register();

    public static final BlockEntry<ExtrusionDieBlock> EXTRUSION_DIE = REGISTRATE.block("extrusion_die", ExtrusionDieBlock::new)
        .initialProperties(SharedProperties::softMetal)
        .properties(BlockBehaviour.Properties::noCollission
        ).transform(TagGen.pickaxeOnly())
        .blockstate(BlockStateGen.axisBlockProvider(false))
        .item()
        .build()
        .register();

    public static final BlockEntry<KeypunchBlock> KEYPUNCH = REGISTRATE.block("keypunch", KeypunchBlock::new)
        .initialProperties(SharedProperties::softMetal)
        .properties(BlockBehaviour.Properties::noOcclusion
        ).transform(TagGen.axeOrPickaxe())
        .blockstate(BlockStateGen.horizontalBlockProvider(true))
        .item(AssemblyOperatorBlockItem::new)
        .transform(customItemModel())
        .register();

    public static final BlockEntry<MeasuringCylinderBlock> MEASURING_CYLINDER = REGISTRATE.block("measuring_cylinder", MeasuringCylinderBlock::new)
        .blockstate(DestroyBlockStateGen.simpleBlock())
        .item(MeasuringCylinderBlockItem::new)
        .properties(p -> p
            .stacksTo(1)
        ).build()
        .register();

    public static final BlockEntry<MechanicalSieveBlock> MECHANICAL_SIEVE = REGISTRATE.block("mechanical_sieve", MechanicalSieveBlock::new)
        .initialProperties(SharedProperties::stone)
        .properties(BlockBehaviour.Properties::noOcclusion
        ).transform(DestroyStressConfigs.setImpact(0.5d))
        .transform(TagGen.axeOrPickaxe())
        //.blockstate(BlockStateGen.horizontalAxisBlockProvider(true)) // augh
        .blockstate(DestroyBlockStateGen.bullshitHorizontalAxisBlock(MechanicalSieveBlock.X, true, 0))
        .item()
        .transform(customItemModel())
        .register();
        
    public static final BlockEntry<PollutometerBlock> POLLUTOMETER = REGISTRATE.block("pollutometer", PollutometerBlock::new)
        .initialProperties(SharedProperties::stone)
        .properties(p -> p
            .mapColor(MapColor.NONE)
            .noOcclusion()
        ).transform(displaySource(DestroyDisplaySources.POLLUTOMETER))
        .transform(TagGen.pickaxeOnly())
        .blockstate(BlockStateGen.horizontalBlockProvider(true))
        .item()
        .transform(customItemModel())
        .register();

    public static final BlockEntry<PumpjackBlock> PUMPJACK = REGISTRATE.block("pumpjack", PumpjackBlock::new)
        .initialProperties(SharedProperties::copperMetal)
        .properties(p -> p
            .mapColor(MapColor.COLOR_ORANGE)
            .noOcclusion()
            .isSuffocating((state, level, pos) -> false)
        ).transform(TagGen.pickaxeOnly())
        .transform(DestroyStressConfigs.setImpact(8.0))
        .blockstate((c, p) -> p.horizontalBlock(c.get(), state -> p.models().getExistingFile(p.modLoc("block/pumpjack/base"))))
        .item(PumpjackBlockItem::new)
        .model((c, p) -> p.generated(c, Destroy.asResource("item/" + c.getName())))
        .build()
        .register();

    public static final BlockEntry<PumpjackCamBlock> PUMPJACK_CAM = REGISTRATE.block("pumpjack_cam", PumpjackCamBlock::new)
        .initialProperties(SharedProperties::copperMetal)
        .properties(p -> p
            .mapColor(MapColor.COLOR_ORANGE)
            .noOcclusion()
            .isSuffocating((state, level, pos) -> false)
        ).transform(TagGen.pickaxeOnly())
        .blockstate(DestroyBlockStateGen.nothing())
        .register();

    public static final BlockEntry<PumpjackStructuralBlock> PUMPJACK_STRUCTURAL = REGISTRATE.block("pumpjack_structure", PumpjackStructuralBlock::new)
        .initialProperties(SharedProperties::copperMetal)
        .properties(p -> p
            .mapColor(MapColor.COLOR_ORANGE)
            .noOcclusion()
            .isSuffocating((state, level, pos) -> false)
        ).transform(TagGen.pickaxeOnly())
        .blockstate(DestroyBlockStateGen.nothing())
        .register();

    public static final BlockEntry<RedstoneProgrammerBlock> REDSTONE_PROGRAMMER = REGISTRATE.block("redstone_programmer", RedstoneProgrammerBlock::new)
        .initialProperties(SharedProperties::wooden)
        .properties(p -> p
            .noOcclusion()
            .noLootTable() // Handled in RedstoneProgrammerBlock class
        ).blockstate((c, p) -> p.getVariantBuilder(c.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(AssetLookup.forPowered(c, p).apply(state))
                .rotationY(((int)state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 0) % 360)
                .build()))
        .item(RedstoneProgrammerBlockItem::new)
        .transform(customItemModel("_", "block"))
        .register();

    public static final BlockEntry<SandCastleBlock> SAND_CASTLE = REGISTRATE.block("sand_castle", SandCastleBlock::new)
        .initialProperties(() -> Blocks.POPPY)
        .properties(p -> p
            .mapColor(MapColor.SAND)
            .noOcclusion()
            .noLootTable()
            .instabreak()
            .sound(SoundType.SAND)
        ).blockstate((c, p) -> BlockStateGen.simpleBlock(c, p, state -> p.models().getExistingFile(p.modLoc(
            switch(state.getValue(SandCastleBlock.MATERIAL)) {
                case SAND -> "block/sand_castle";
                case RED_SAND -> "block/red_sand_castle";
                case SOUL_SAND -> "block/soul_sand_castle";
            }))
        )).register();

    public static final BlockEntry<SiphonBlock> SIPHON = REGISTRATE.block("siphon", SiphonBlock::new)
        .initialProperties(AllBlocks.FLUID_TANK)
        .properties(p -> p
            .mapColor(MapColor.METAL)
        ).transform(TagGen.pickaxeOnly())
        .blockstate((c, p) -> BlockStateGen.simpleBlock(c, p, AssetLookup.forPowered(c, p, "siphon")))
        .item()
        .build()
        .register();

    public static final BlockEntry<TestTubeRackBlock> TEST_TUBE_RACK = REGISTRATE.block("test_tube_rack", TestTubeRackBlock::new)
        .initialProperties(() -> Blocks.OAK_PLANKS)
        .properties(p -> p
        ).tag(BlockTags.MINEABLE_WITH_AXE)
        .blockstate(DestroyBlockStateGen.bullshitHorizontalAxisBlock(TestTubeRackBlock.X, false, 90))
        .item()
        .build()
        .register();

    public static final BlockEntry<TreeTapBlock> TREE_TAP = REGISTRATE.block("tree_tap", TreeTapBlock::new)
        .initialProperties(AllBlocks.DEPLOYER)
        .transform(TagGen.axeOrPickaxe())
        .blockstate(BlockStateGen.horizontalBlockProvider(true))
        .item()
        .transform(customItemModel())
        .register();

    public static final BlockEntry<VatControllerBlock> VAT_CONTROLLER = REGISTRATE.block("vat_controller", VatControllerBlock::new)
        .initialProperties(SharedProperties::copperMetal)
        .properties(BlockBehaviour.Properties::noOcclusion
        ).onRegister(connectedTextures(() -> new EncasedCTBehaviour(DestroySpriteShifts.STAINLESS_STEEL_BLOCK)))
        .onRegister(casingConnectivity((block, cc) -> cc.make(block, DestroySpriteShifts.STAINLESS_STEEL_BLOCK,
			(s, f) -> f != s.getValue(VatControllerBlock.FACING)))
        ).transform(displaySource(DestroyDisplaySources.VAT_CONTROLLER_ALL))
        .transform(displaySource(DestroyDisplaySources.VAT_CONTROLLER_SOLUTION))
        .transform(displaySource(DestroyDisplaySources.VAT_CONTROLLER_GAS))
        .transform(TagGen.pickaxeOnly())
        .blockstate((c, p) -> p.horizontalBlock(c.get(),
            Destroy.asResource("block/stainless_steel_block"),
            Destroy.asResource("block/vat/vat_controller"),
            Destroy.asResource("block/stainless_steel_block")))
        .item()
        .build()
        .register();

    public static final BlockEntry<VatSideBlock> VAT_SIDE = REGISTRATE.block("vat_side", VatSideBlock::new)
        .transform(BuilderTransformers.copycat())
        .properties(p -> p
            .isViewBlocking(DestroyBlocks::never)
        ).onRegister(CreateRegistrate.blockModel(() -> CopycatFullBlockModel::new))
        .transform(displaySource(DestroyDisplaySources.VAT_SIDE_ALL))
        .transform(displaySource(DestroyDisplaySources.VAT_SIDE_SOLUTION))
        .transform(displaySource(DestroyDisplaySources.VAT_SIDE_GAS))
        .blockstate(DestroyBlockStateGen.nothing())
        .register();

    public static final BlockEntry<UrineCauldronBlock> URINE_CAULDRON = REGISTRATE.block("urine_cauldron", p -> new UrineCauldronBlock(p, DestroyCauldronInteractions.URINE))
        .initialProperties(() -> Blocks.WATER_CAULDRON)
        .tag(BlockTags.CAULDRONS)
        .blockstate(DestroyBlockStateGen.simpleBlock())
        .register();

    public static final BlockEntry<BlacklightBlock> BLACKLIGHT = REGISTRATE.block("blacklight", BlacklightBlock::new)
        .initialProperties(() -> Blocks.LANTERN)
        .properties(p -> p
            .mapColor(MapColor.COLOR_PURPLE)
            .sound(SoundType.GLASS)
            .forceSolidOn()
        ).transform(TagGen.pickaxeOnly())
        .blockstate(DestroyBlockStateGen.blacklight())
        .item()
        .model((c, p) -> p.generated(c, Destroy.asResource("item/" + c.getName())))
        .build()
        .register();

    public static final BlockEntry<SimplePlaceableMixtureTankBlock>
    
    BEAKER = REGISTRATE.block("beaker", SimplePlaceableMixtureTankBlock.of(() -> DestroyAllConfigs.SERVER.blocks.beakerCapacity.get(), 5.5f, 0.5f, 5.5f, 10.5f, 7f, 10.5f, DestroyVoxelShapes.BEAKER))
        .initialProperties(MEASURING_CYLINDER)
        .blockstate(DestroyBlockStateGen.simpleBlock())
        .item(SimplePlaceableMixtureTankBlockItem::new)
        .build()
        .register(),

    ROUND_BOTTOMED_FLASK = REGISTRATE.block("round_bottomed_flask", SimplePlaceableMixtureTankBlock.of(() -> DestroyAllConfigs.SERVER.blocks.roundBottomedFlaskCapacity.get(), 5.5f, 0.5f, 5.5f, 10.5f, 4.5f, 10.5f, DestroyVoxelShapes.ROUND_BOTTOMED_FLASK))
        .initialProperties(BEAKER)
        .blockstate(DestroyBlockStateGen.simpleBlock())
        .item(SimplePlaceableMixtureTankBlockItem::new)
        .build()
        .register();

    // EXPLOSIVES

    public static final BlockEntry<PrimeableBombBlock<PrimedBombEntity.Anfo>> ANFO_BLOCK = REGISTRATE.block("anfo_block", p -> new PrimeableBombBlock<PrimedBombEntity.Anfo>(p, PrimedBombEntity.Anfo::new))
        .initialProperties(() -> Blocks.TNT)
        .properties(p -> p
            .mapColor(MapColor.COLOR_PINK)
        ).blockstate(DestroyBlockStateGen.cubeBottomTop("anfo"))
        .item()
        .tag(DestroyTags.Items.LIABLE_TO_CHANGE.tag)
        .onRegister(registerPrimeableBombDispenserBehaviour())
        .build()
        .register();

    public static final BlockEntry<PrimeableBombBlock<PrimedBombEntity.Cordite>> CORDITE = REGISTRATE.block("cordite", p -> new PrimeableBombBlock<PrimedBombEntity.Cordite>(p, PrimedBombEntity.Cordite::new))
        .initialProperties(() -> Blocks.TNT)
        .properties(p -> p
            .mapColor(MapColor.COLOR_ORANGE)
        ).blockstate(DestroyBlockStateGen.cubeBottomTop("cordite"))
        .item()
        .tag(DestroyTags.Items.LIABLE_TO_CHANGE.tag)
        .onRegister(registerPrimeableBombDispenserBehaviour())
        .build()
        .register();

    public static final BlockEntry<DynamiteBlock> DYNAMITE_BLOCK = REGISTRATE.block("dynamite_block", DynamiteBlock::new)
        .initialProperties(() -> Blocks.TNT)
        .properties(p -> p
            .mapColor(MapColor.COLOR_MAGENTA)
        ).blockstate(DestroyBlockStateGen.cubeBottomTop("dynamite"))
        .item()
        .build()
        .register();

    public static final BlockEntry<PrimeableBombBlock<PrimedBombEntity.Nitrocellulose>> NITROCELLULOSE_BLOCK = REGISTRATE.block("nitrocellulose_block", p -> new PrimeableBombBlock<PrimedBombEntity.Nitrocellulose>(p, PrimedBombEntity.Nitrocellulose::new))
        .initialProperties(() -> Blocks.TNT)
        .properties(p -> p
            .mapColor(MapColor.COLOR_LIGHT_GREEN)
        ).blockstate(DestroyBlockStateGen.cubeBottomTop("nitrocellulose"))
        .item()
        .tag(DestroyTags.Items.LIABLE_TO_CHANGE.tag)
        .tag(DestroyTags.Items.OBLITERATION_EXPLOSIVES.tag)
        .onRegister(registerPrimeableBombDispenserBehaviour())
        .build()
        .register();

    public static final BlockEntry<PrimeableBombBlock<PrimedBombEntity.PicricAcid>> PICRIC_ACID_BLOCK = REGISTRATE.block("picric_acid_block", (p) -> new PrimeableBombBlock<PrimedBombEntity.PicricAcid>(p, PrimedBombEntity.PicricAcid::new))
        .initialProperties(() -> Blocks.TNT)
        .properties(p -> p
            .mapColor(MapColor.COLOR_YELLOW)
        ).blockstate(DestroyBlockStateGen.cubeBottomTop("picric_acid"))
        .item()
        .tag(DestroyTags.Items.LIABLE_TO_CHANGE.tag)
        .onRegister(registerPrimeableBombDispenserBehaviour())
        .build()
        .register();

    // STORAGE BLOCKS

    public static final BlockEntry<Block> CARBON_FIBER_BLOCK = REGISTRATE.block("carbon_fiber_block", Block::new)
        .initialProperties(() -> Blocks.OBSIDIAN)
        .properties(p -> p
            .strength(40f, 800f)
        ).transform(TagGen.pickaxeOnly())
        .tag(BlockTags.NEEDS_IRON_TOOL)
        .tag(DestroyTags.Blocks.ARC_FURNACE_TRANSFORMABLE.tag)
        .transform(TagGen.tagBlockAndItem(DestroyTags.Blocks.CARBON_FIBER_STORAGE_BLOCKS.tag, DestroyTags.Blocks.CARBON_FIBER_STORAGE_BLOCKS.itemTag.get()))
        .tag(Tags.Items.STORAGE_BLOCKS)
        .build()
        .register(),

    FLUORITE_BLOCK = REGISTRATE.block("fluorite_block", Block::new)
        .initialProperties(() -> Blocks.IRON_BLOCK)
        .properties(p -> p
            .mapColor(MapColor.COLOR_PURPLE)
            .requiresCorrectToolForDrops()
            .strength(6f, 6f)
        ).transform(TagGen.pickaxeOnly())
        .tag(BlockTags.NEEDS_IRON_TOOL)
        .tag(Tags.Blocks.STORAGE_BLOCKS)
        .tag(BlockTags.BEACON_BASE_BLOCKS)
        .transform(TagGen.tagBlockAndItem(forgeBlockTag("storage_blocks/fluorite"), forgeItemTag("storage_blocks/fluorite")))
        .tag(Tags.Items.STORAGE_BLOCKS)
        .build()
        .register(),
        
    RAW_NICKEL_BLOCK = REGISTRATE.block("raw_nickel_block", Block::new)
        .initialProperties(() -> Blocks.RAW_IRON_BLOCK)
        .properties(p -> p
            .mapColor(MapColor.SAND)
            .requiresCorrectToolForDrops()
            .strength(5f, 6f)
        ).transform(TagGen.pickaxeOnly())
        .tag(BlockTags.NEEDS_STONE_TOOL)
        .tag(Tags.Blocks.STORAGE_BLOCKS)
        .tag(BlockTags.BEACON_BASE_BLOCKS)
        .transform(TagGen.tagBlockAndItem(forgeBlockTag("storage_blocks/raw_nickel"), forgeItemTag("storage_blocks/raw_nickel")))
        .tag(Tags.Items.STORAGE_BLOCKS)
        .build()
        .register(),
        
    CHROMIUM_BLOCK = REGISTRATE.block("chromium_block", Block::new)
        .initialProperties(() -> Blocks.IRON_BLOCK)
        .properties(p -> p
            .requiresCorrectToolForDrops()
            .strength(5f, 6f)
        ).transform(TagGen.pickaxeOnly())
        .tag(BlockTags.NEEDS_STONE_TOOL)
        .tag(Tags.Blocks.STORAGE_BLOCKS)
        .tag(BlockTags.BEACON_BASE_BLOCKS)
        .transform(TagGen.tagBlockAndItem(forgeBlockTag("storage_blocks/chromium"), forgeItemTag("storage_blocks/chromium")))
        .tag(Tags.Items.STORAGE_BLOCKS)
        .build()
        .register(),
        
    IODINE_BLOCK = REGISTRATE.block("iodine_block", Block::new)
        .initialProperties(() -> Blocks.RAW_IRON_BLOCK)
        .properties(p -> p
            .mapColor(MapColor.COLOR_GRAY)
            .strength(2f, 2f)
        ).transform(TagGen.pickaxeOnly())
        .tag(BlockTags.NEEDS_STONE_TOOL, Tags.Blocks.STORAGE_BLOCKS)
        .transform(TagGen.tagBlockAndItem(forgeBlockTag("storage_blocks/iodine"), forgeItemTag("storage_blocks/iodine")))
        .tag(Tags.Items.STORAGE_BLOCKS)
        .build()
        .register(),

    NETHER_CROCOITE_BLOCK = REGISTRATE.block("nether_crocoite_block", Block::new)
        .initialProperties(() -> Blocks.RAW_IRON_BLOCK)
        .properties(p -> p
            .mapColor(MapColor.COLOR_ORANGE)
            .strength(2f, 2f)
        ).transform(TagGen.pickaxeOnly())
        .tag(BlockTags.NEEDS_IRON_TOOL, Tags.Blocks.STORAGE_BLOCKS)
        .item()
        .tag(Tags.Items.STORAGE_BLOCKS)
        .build()
        .register(),
        
    NICKEL_BLOCK = REGISTRATE.block("nickel_block", Block::new)
        .initialProperties(() -> Blocks.IRON_BLOCK)
        .properties(p -> p
            .mapColor(MapColor.SAND)
            .requiresCorrectToolForDrops()
            .strength(5f, 6f)
        ).transform(TagGen.pickaxeOnly())
        .tag(BlockTags.NEEDS_STONE_TOOL)
        .tag(Tags.Blocks.STORAGE_BLOCKS)
        .tag(BlockTags.BEACON_BASE_BLOCKS)
        .transform(TagGen.tagBlockAndItem(forgeBlockTag("storage_blocks/nickel"), forgeItemTag("storage_blocks/nickel")))
        .tag(Tags.Items.STORAGE_BLOCKS)
        .build()
        .register(),
        
    PALLADIUM_BLOCK = REGISTRATE.block("palladium_block", Block::new)
        .initialProperties(() -> Blocks.NETHERITE_BLOCK)
        .properties(p -> p
            .mapColor(MapColor.DIRT)
            .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
            .requiresCorrectToolForDrops()
            .strength(6f, 6f)
        ).transform(TagGen.pickaxeOnly())
        .tag(BlockTags.NEEDS_DIAMOND_TOOL)
        .tag(Tags.Blocks.STORAGE_BLOCKS)
        .tag(BlockTags.BEACON_BASE_BLOCKS)
        .transform(TagGen.tagBlockAndItem(forgeBlockTag("storage_blocks/palladium"), forgeItemTag("storage_blocks/palladium")))
        .tag(Tags.Items.STORAGE_BLOCKS)
        .build()
        .register(),
        
    PLATINUM_BLOCK = REGISTRATE.block("platinum_block", Block::new)
        .initialProperties(() -> Blocks.DIAMOND_BLOCK)
        .properties(p -> p
            .requiresCorrectToolForDrops()
            .instrument(NoteBlockInstrument.BELL)
            .strength(6f, 6f)
        ).transform(TagGen.pickaxeOnly())
        .tag(BlockTags.NEEDS_IRON_TOOL)
        .tag(Tags.Blocks.STORAGE_BLOCKS)
        .tag(BlockTags.BEACON_BASE_BLOCKS)
        .transform(TagGen.tagBlockAndItem(forgeBlockTag("storage_blocks/platinum"), forgeItemTag("storage_blocks/platinum")))
        .tag(Tags.Items.STORAGE_BLOCKS)
        .build()
        .register(),
    
    RHODIUM_BLOCK = REGISTRATE.block("rhodium_block", Block::new)
        .initialProperties(() -> Blocks.NETHERITE_BLOCK)
        .properties(p -> p
            .mapColor(MapColor.TERRACOTTA_LIGHT_BLUE)
            .instrument(NoteBlockInstrument.BELL)
            .requiresCorrectToolForDrops()
            .strength(6f, 6f)
        ).transform(TagGen.pickaxeOnly())
        .tag(BlockTags.NEEDS_DIAMOND_TOOL)
        .tag(Tags.Blocks.STORAGE_BLOCKS)
        .tag(BlockTags.BEACON_BASE_BLOCKS)
        .transform(TagGen.tagBlockAndItem(forgeBlockTag("storage_blocks/rhodium"), forgeItemTag("storage_blocks/rhodium")))
        .tag(Tags.Items.STORAGE_BLOCKS)
        .build()
        .register(),

    LEAD_BLOCK = REGISTRATE.block("lead_block", Block::new)
        .initialProperties(() -> Blocks.IRON_BLOCK)
        .properties(p -> p
            .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
            .requiresCorrectToolForDrops()
            .strength(7f, 6f)
        ).transform(TagGen.pickaxeOnly())
        .tag(BlockTags.NEEDS_STONE_TOOL)
        .tag(Tags.Blocks.STORAGE_BLOCKS)
        .tag(BlockTags.BEACON_BASE_BLOCKS)
        .transform(TagGen.tagBlockAndItem(forgeBlockTag("storage_blocks/lead"), forgeItemTag("storage_blocks/lead")))
        .tag(Tags.Items.STORAGE_BLOCKS)
        .build()
        .register();
        
    public static final BlockEntry<CasingBlock> STAINLESS_STEEL_BLOCK = REGISTRATE.block("stainless_steel_block", CasingBlock::new)
        .initialProperties(() -> Blocks.IRON_BLOCK)
        .properties(p -> p
            .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
            .sound(SoundType.METAL)
            .strength(7f, 8f)
        ).transform(TagGen.pickaxeOnly())
        .properties(p -> p.sound(SoundType.COPPER))
        .blockstate((c, p) -> p.simpleBlock(c.get()))
        .onRegister(connectedTextures(() -> new EncasedCTBehaviour(DestroySpriteShifts.STAINLESS_STEEL_BLOCK)))
        .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, DestroySpriteShifts.STAINLESS_STEEL_BLOCK)))
        .tag(BlockTags.NEEDS_STONE_TOOL)
        .tag(Tags.Blocks.STORAGE_BLOCKS)
        .tag(BlockTags.BEACON_BASE_BLOCKS)
        .tag(AllBlockTags.CASING.tag)
        .transform(TagGen.tagBlockAndItem(forgeBlockTag("storage_blocks/stainless_steel"), forgeItemTag("storage_blocks/stainless_steel")))
        .tag(Tags.Items.STORAGE_BLOCKS)
        .build()
        .register();

    public static final BlockEntry<RotatedPillarBlock> CHISELED_RHODIUM_BLOCK = REGISTRATE.block("chiseled_rhodium_block", RotatedPillarBlock::new)
        .initialProperties(RHODIUM_BLOCK)
        .transform(TagGen.pickaxeOnly())
        .blockstate((c, p) -> p.axisBlock(c.get(),
            p.modLoc("block/chiseled_rhodium_block"),
            p.modLoc("block/chiseled_rhodium_block_end")))
        .tag(BlockTags.NEEDS_DIAMOND_TOOL)
        .tag(Tags.Blocks.STORAGE_BLOCKS)
        .tag(BlockTags.BEACON_BASE_BLOCKS)
        .transform(TagGen.tagBlockAndItem(forgeBlockTag("storage_blocks/rhodium"), forgeItemTag("storage_blocks/rhodium")))
        .tag(Tags.Items.STORAGE_BLOCKS)
        .recipe((c, p) -> {
            DataIngredient source = DataIngredient.tag(forgeItemTag("storage_blocks/rhodium"));
            SingleItemRecipeBuilder.stonecutting(source, RecipeCategory.MISC, c.get(), 1)
                .unlockedBy("has_rhodium_block", RegistrateRecipeProvider.has(forgeItemTag("storage_blocks/rhodium")))
                .save(p, Destroy.asResource("stonecutting/" + c.getName()));
        })
        .build()
        .register();

    // ORES

    public static final BlockEntry<Block> FLUORITE_ORE = REGISTRATE.block("fluorite_ore", Block::new)
        .initialProperties(() -> Blocks.GOLD_ORE)
        .properties(p -> p
            .mapColor(MapColor.COLOR_PURPLE)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .strength(3f, 3f)
        ).transform(TagGen.pickaxeOnly())
        .loot((lt, b) -> lt.add(b, RegistrateBlockLootTables.createSilkTouchDispatchTable(b, lt.applyExplosionDecay(b, LootItem.lootTableItem(DestroyItems.FLUORITE.get()).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))))
        .tag(BlockTags.NEEDS_IRON_TOOL)
        .tag(Tags.Blocks.ORES)
        .transform(TagGen.tagBlockAndItem(Map.of(
            forgeBlockTag("ores/fluorite"), AllTags.forgeItemTag("ores/fluorite"),
            AllBlockTags.STONE_ORES_IN_GROUND.tag, AllItemTags.STONE_ORES_IN_GROUND.tag
            )))
        .tag(Tags.Items.ORES)
        .build()
        .register();

    public static final BlockEntry<Block> DEEPSLATE_FLUORITE_ORE = REGISTRATE.block("deepslate_fluorite_ore", Block::new)
        .initialProperties(() -> Blocks.DEEPSLATE_GOLD_ORE)
        .properties(p -> p
            .mapColor(MapColor.COLOR_PURPLE)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .sound(SoundType.DEEPSLATE)
            .strength(4.5f, 3f)
        ).transform(TagGen.pickaxeOnly())
        .loot((lt, b) -> lt.add(b, RegistrateBlockLootTables.createSilkTouchDispatchTable(b, lt.applyExplosionDecay(b, LootItem.lootTableItem(DestroyItems.FLUORITE.get()).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))))
        .tag(BlockTags.NEEDS_IRON_TOOL)
        .tag(Tags.Blocks.ORES)
        .transform(TagGen.tagBlockAndItem(Map.of(
            forgeBlockTag("ores/fluorite"), AllTags.forgeItemTag("ores/fluorite"),
            AllBlockTags.DEEPSLATE_ORES_IN_GROUND.tag, AllItemTags.DEEPSLATE_ORES_IN_GROUND.tag
        )))
        .tag(Tags.Items.ORES)
        .build()
        .register();

    public static final BlockEntry<Block> END_FLUORITE_ORE = REGISTRATE.block("end_fluorite_ore", Block::new)
        .initialProperties(() -> Blocks.END_STONE)
        .properties(p -> p
            .mapColor(MapColor.COLOR_PURPLE)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .strength(4f, 9f)
        ).transform(TagGen.pickaxeOnly())
        .loot((lt, b) -> lt.add(b, RegistrateBlockLootTables.createSilkTouchDispatchTable(b, lt.applyExplosionDecay(b, LootItem.lootTableItem(DestroyItems.FLUORITE.get()).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))))
        .tag(BlockTags.NEEDS_IRON_TOOL)
        .tag(Tags.Blocks.ORES)
        .transform(TagGen.tagBlockAndItem(Map.of(
            forgeBlockTag("ores/fluorite"), AllTags.forgeItemTag("ores/fluorite"),
            forgeBlockTag("ores_in_ground/end_stone"), AllTags.forgeItemTag("ores_in_ground/end_stone")
        )))
        .tag(Tags.Items.ORES)
        .build()
        .register();

    public static final BlockEntry<Block> NICKEL_ORE = REGISTRATE.block("nickel_ore", Block::new)
        .initialProperties(() -> Blocks.GOLD_ORE)
        .properties(p -> p
            .mapColor(MapColor.SAND)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .strength(3f, 3f)
        ).transform(TagGen.pickaxeOnly())
        .loot((lt, b) -> lt.add(b, RegistrateBlockLootTables.createSilkTouchDispatchTable(b, lt.applyExplosionDecay(b, LootItem.lootTableItem(DestroyItems.RAW_NICKEL.get()).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))))
        .tag(BlockTags.NEEDS_IRON_TOOL)
        .tag(Tags.Blocks.ORES)
        .transform(TagGen.tagBlockAndItem(Map.of(
            forgeBlockTag("ores/nickel"), AllTags.forgeItemTag("ores/nickel"),
            AllBlockTags.STONE_ORES_IN_GROUND.tag, AllItemTags.STONE_ORES_IN_GROUND.tag
        )))
        .tag(Tags.Items.ORES)
        .build()
        .register();

    public static final BlockEntry<Block> DEEPSLATE_NICKEL_ORE = REGISTRATE.block("deepslate_nickel_ore", Block::new)
        .initialProperties(() -> Blocks.DEEPSLATE_GOLD_ORE)
        .properties(p -> p
            .mapColor(MapColor.SAND)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .sound(SoundType.DEEPSLATE)
            .strength(4.5f, 3f)
        ).transform(TagGen.pickaxeOnly())
        .loot((lt, b) -> lt.add(b, RegistrateBlockLootTables.createSilkTouchDispatchTable(b, lt.applyExplosionDecay(b, LootItem.lootTableItem(DestroyItems.RAW_NICKEL.get()).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))))
        .tag(BlockTags.NEEDS_IRON_TOOL)
        .tag(Tags.Blocks.ORES)
        .transform(TagGen.tagBlockAndItem(Map.of(
            forgeBlockTag("ores/nickel"), AllTags.forgeItemTag("ores/nickel"),
            AllBlockTags.DEEPSLATE_ORES_IN_GROUND.tag, AllItemTags.DEEPSLATE_ORES_IN_GROUND.tag
        )))
        .tag(Tags.Items.ORES)
        .build()
        .register();

    public static final BlockEntry<Block> NETHER_CROCOITE_ORE = REGISTRATE.block("nether_crocoite_ore", Block::new)
        .initialProperties(() -> Blocks.NETHER_QUARTZ_ORE)
        .properties(p -> p
            .mapColor(MapColor.COLOR_ORANGE)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .sound(SoundType.NETHERRACK)
            .requiresCorrectToolForDrops()
        ).onRegister(connectedTextures(() -> new SimpleCTBehaviour(DestroySpriteShifts.NETHER_CROCOITE_BLOCK)))
        .transform(TagGen.pickaxeOnly())
        .loot((lt, b) -> lt.add(b, RegistrateBlockLootTables.createSilkTouchDispatchTable(b, lt.applyExplosionDecay(b, LootItem.lootTableItem(DestroyItems.NETHER_CROCOITE.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 5.0f))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))))
        .tag(BlockTags.NEEDS_IRON_TOOL)
        .tag(Tags.Blocks.ORES, Tags.Blocks.ORES_IN_GROUND_NETHERRACK, Tags.Blocks.ORE_BEARING_GROUND_NETHERRACK)
        .item()
        .tag(Tags.Items.ORES)
        .build()
        .register();

    // CROPS

    @SuppressWarnings("removal")
    public static final BlockEntry<MagicBeetrootShootsBlock>

    MAGIC_BEETROOT_SHOOTS = REGISTRATE.block("magic_beetroot_shoots", MagicBeetrootShootsBlock::new)
        .addLayer(() -> RenderType::cutout)
        .initialProperties(() -> Blocks.BEETROOTS)
        .blockstate(DestroyBlockStateGen.simpleBlock(new ResourceLocation("block/beetroots_stage0")))
        .register();

    public static final BlockEntry<FullyGrownCropBlock>

    GOLDEN_CARROTS = REGISTRATE.block("golden_carrots", p -> new FullyGrownCropBlock(p, () -> Items.GOLDEN_CARROT))
        .loot((lt, b) ->
            lt.add(b, lt.createCropDrops(b, Items.GOLDEN_CARROT, Items.GOLDEN_CARROT, LootItemBlockStatePropertyCondition.hasBlockStateProperties(b)))
        ).initialProperties(() -> Blocks.CARROTS)
        .blockstate(DestroyBlockStateGen.simpleBlock())
        .tag(BlockTags.CROPS)
        .register();

    public static final BlockEntry<HeftyBeetrootBlock> 

    HEFTY_BEETROOT = REGISTRATE.block("hefty_beetroot", p -> new HeftyBeetrootBlock(p, DestroyItems.HEFTY_BEETROOT))
        .initialProperties(() -> Blocks.BEETROOTS)
        .blockstate(DestroyBlockStateGen.simpleBlock())
        .tag(DestroyTags.Blocks.BEETROOTS.tag)
        .register(),

    COAL_INFUSED_BEETROOT = REGISTRATE.block("coal_infused_beetroot", p -> new HeftyBeetrootBlock(p, DestroyItems.COAL_INFUSED_BEETROOT))
        .initialProperties(HEFTY_BEETROOT)
        .blockstate(DestroyBlockStateGen.simpleBlock())
        .tag(DestroyTags.Blocks.BEETROOTS.tag)
        .register(),

    COPPER_INFUSED_BEETROOT = REGISTRATE.block("copper_infused_beetroot", p -> new HeftyBeetrootBlock(p, DestroyItems.COPPER_INFUSED_BEETROOT))
        .initialProperties(HEFTY_BEETROOT)
        .blockstate(DestroyBlockStateGen.simpleBlock())
        .tag(DestroyTags.Blocks.BEETROOTS.tag)
        .register(),

    DIAMOND_INFUSED_BEETROOT = REGISTRATE.block("diamond_infused_beetroot", p -> new HeftyBeetrootBlock(p, DestroyItems.DIAMOND_INFUSED_BEETROOT))
        .initialProperties(HEFTY_BEETROOT)
        .blockstate(DestroyBlockStateGen.simpleBlock())
        .tag(DestroyTags.Blocks.BEETROOTS.tag)
        .register(),

    EMERALD_INFUSED_BEETROOT = REGISTRATE.block("emerald_infused_beetroot", p -> new HeftyBeetrootBlock(p, DestroyItems.EMERALD_INFUSED_BEETROOT))
        .initialProperties(HEFTY_BEETROOT)
        .blockstate(DestroyBlockStateGen.simpleBlock())
        .tag(DestroyTags.Blocks.BEETROOTS.tag)
        .register(),

    FLUORITE_INFUSED_BEETROOT = REGISTRATE.block("fluorite_infused_beetroot", p -> new HeftyBeetrootBlock(p, DestroyItems.FLUORITE_INFUSED_BEETROOT))
        .initialProperties(HEFTY_BEETROOT)
        .blockstate(DestroyBlockStateGen.simpleBlock())
        .tag(DestroyTags.Blocks.BEETROOTS.tag)
        .register(),

    GOLD_INFUSED_BEETROOT = REGISTRATE.block("gold_infused_beetroot", p -> new HeftyBeetrootBlock(p, DestroyItems.GOLD_INFUSED_BEETROOT))
        .initialProperties(HEFTY_BEETROOT)
        .blockstate(DestroyBlockStateGen.simpleBlock())
        .tag(DestroyTags.Blocks.BEETROOTS.tag)
        .register(),

    IRON_INFUSED_BEETROOT = REGISTRATE.block("iron_infused_beetroot", p -> new HeftyBeetrootBlock(p, DestroyItems.IRON_INFUSED_BEETROOT))
        .initialProperties(HEFTY_BEETROOT)
        .blockstate(DestroyBlockStateGen.simpleBlock())
        .tag(DestroyTags.Blocks.BEETROOTS.tag)
        .register(),

    LAPIS_INFUSED_BEETROOT = REGISTRATE.block("lapis_infused_beetroot", p -> new HeftyBeetrootBlock(p, DestroyItems.LAPIS_INFUSED_BEETROOT))
        .initialProperties(HEFTY_BEETROOT)
        .blockstate(DestroyBlockStateGen.simpleBlock())
        .tag(DestroyTags.Blocks.BEETROOTS.tag)
        .register(),

    NICKEL_INFUSED_BEETROOT = REGISTRATE.block("nickel_infused_beetroot", p -> new HeftyBeetrootBlock(p, DestroyItems.NICKEL_INFUSED_BEETROOT))
        .initialProperties(HEFTY_BEETROOT)
        .blockstate(DestroyBlockStateGen.simpleBlock())
        .tag(DestroyTags.Blocks.BEETROOTS.tag)
        .register(),

    NETHER_CROCOITE_INFUSED_BEETROOT = REGISTRATE.block("nether_crocoite_infused_beetroot", p -> new HeftyBeetrootBlock(p, DestroyItems.NETHER_CROCOITE_INFUSED_BEETROOT))
        .initialProperties(HEFTY_BEETROOT)
        .blockstate(DestroyBlockStateGen.simpleBlock())
        .tag(DestroyTags.Blocks.BEETROOTS.tag)
        .register(),

    QUARTZ_INFUSED_BEETROOT = REGISTRATE.block("quartz_infused_beetroot", p -> new HeftyBeetrootBlock(p, DestroyItems.QUARTZ_INFUSED_BEETROOT))
        .initialProperties(HEFTY_BEETROOT)
        .blockstate(DestroyBlockStateGen.simpleBlock())
        .tag(DestroyTags.Blocks.BEETROOTS.tag)
        .register(),

    REDSTONE_INFUSED_BEETROOT = REGISTRATE.block("redstone_infused_beetroot", p -> new HeftyBeetrootBlock(p, DestroyItems.REDSTONE_INFUSED_BEETROOT))
        .initialProperties(HEFTY_BEETROOT)
        .blockstate(DestroyBlockStateGen.simpleBlock())
        .tag(DestroyTags.Blocks.BEETROOTS.tag)
        .register(),

    ZINC_INFUSED_BEETROOT = REGISTRATE.block("zinc_infused_beetroot", p -> new HeftyBeetrootBlock(p, DestroyItems.ZINC_INFUSED_BEETROOT))
        .initialProperties(HEFTY_BEETROOT)
        .blockstate(DestroyBlockStateGen.simpleBlock())
        .tag(DestroyTags.Blocks.BEETROOTS.tag)
        .register();

    // Periodic Table blocks
    public static BlockEntry<PeriodicTableBlock> solidPeriodicTableBlock(String name, NonNullSupplier<? extends Block> block, ResourceLocation sideTexture, String storageTag) {
        TagKey<Block> blockTag = forgeBlockTag("storage_blocks/"+storageTag);
        TagKey<Item> itemTag = forgeItemTag("storage_blocks/"+storageTag);
        return REGISTRATE.block(name + "_periodic_table_block", PeriodicTableBlock::new)
            .initialProperties(block)
            .transform(TagGen.pickaxeOnly())
            .tag(Tags.Blocks.STORAGE_BLOCKS, blockTag)
            .blockstate(DestroyBlockStateGen.periodicTableSolidBlock(Destroy.asResource("block/periodic_table/" + name), sideTexture))
            .item(PeriodicTableBlockItem::new)
            .tag(Tags.Items.STORAGE_BLOCKS, itemTag)
            .recipe((c, p) -> {
                DataIngredient source = DataIngredient.tag(itemTag);
                SingleItemRecipeBuilder.stonecutting(source, RecipeCategory.MISC, c.get(), 1)
                    .unlockedBy("has_" + name + "_block", RegistrateRecipeProvider.has(itemTag))
                    .save(p, Destroy.asResource("stonecutting/" + c.getName()));
            })
            .build()
            .register();
    }

    public static BlockEntry<PeriodicTableBlock> solidPeriodicTableBlock(String name, NonNullSupplier<? extends Block> block, ResourceLocation sideTexture) {
        return solidPeriodicTableBlock(name, block, sideTexture, name);
    }

    public static BlockEntry<PeriodicTableBlock> solidPeriodicTableBlock(String name, NonNullSupplier<? extends Block> block) {
        return solidPeriodicTableBlock(name, block, Destroy.asResource("block/" + name + "_block"));
    }

    public static BlockEntry<TankPeriodicTableBlock> fluidPeriodicTableBlock(String name, int color, ResourceLocation fluidTexture) {
        return REGISTRATE.block(name + "_periodic_table_block", p -> new TankPeriodicTableBlock(p, color))
            .initialProperties(SharedProperties::stone)
            .properties(p -> p
                .strength(2f)
                .sound(SoundType.GLASS)
                .noOcclusion()
                .isValidSpawn(DestroyBlocks::never)
                .isRedstoneConductor(DestroyBlocks::never)
                .isSuffocating(DestroyBlocks::never)
                .isViewBlocking(DestroyBlocks::never)
            ).transform(TagGen.pickaxeOnly())
            .blockstate(DestroyBlockStateGen.periodicTableFluidBlock(Destroy.asResource("block/periodic_table/" + name), fluidTexture))
            .item(TankPeriodicTableBlockItem::new)
            .build()
            .register();
    }

    public static BlockEntry<TankPeriodicTableBlock> fluidPeriodicTableBlock(String name, int color, boolean isLiquid) {
        return fluidPeriodicTableBlock(name, color, Destroy.asResource(isLiquid ? "fluid/mixture_still" : "fluid/gas"));
    }

    public static final BlockEntry<PeriodicTableBlock>
        IRON_PERIODIC_TABLE_BLOCK = solidPeriodicTableBlock("iron", () -> Blocks.IRON_BLOCK, new ResourceLocation("block/iron_block")),
        GOLD_PERIODIC_TABLE_BLOCK = solidPeriodicTableBlock("gold", () -> Blocks.GOLD_BLOCK, new ResourceLocation("block/gold_block")),
        COPPER_PERIODIC_TABLE_BLOCK = solidPeriodicTableBlock("copper", () -> Blocks.COPPER_BLOCK, new ResourceLocation("block/copper_block")),
        CARBON_PERIODIC_TABLE_BLOCK = solidPeriodicTableBlock("carbon", CARBON_FIBER_BLOCK, Destroy.asResource("block/carbon_fiber_block"), "carbon_fiber"),
        ZINC_PERIODIC_TABLE_BLOCK = solidPeriodicTableBlock("zinc", AllBlocks.ZINC_BLOCK, Create.asResource("block/zinc_block")),
        CHROMIUM_PERIODIC_TABLE_BLOCK = solidPeriodicTableBlock("chromium", CHROMIUM_BLOCK),
        NICKEL_PERIODIC_TABLE_BLOCK = solidPeriodicTableBlock("nickel", NICKEL_BLOCK),
        PLATINUM_PERIODIC_TABLE_BLOCK = solidPeriodicTableBlock("platinum", PLATINUM_BLOCK),
        PALLADIUM_PERIODIC_TABLE_BLOCK = solidPeriodicTableBlock("palladium", PALLADIUM_BLOCK),
        RHODIUM_PERIODIC_TABLE_BLOCK = solidPeriodicTableBlock("rhodium", RHODIUM_BLOCK),
        IODINE_PERIODIC_TABLE_BLOCK = solidPeriodicTableBlock("iodine", IODINE_BLOCK),
        LEAD_PERIODIC_TABLE_BLOCK = solidPeriodicTableBlock("lead", LEAD_BLOCK);

    public static final BlockEntry<TankPeriodicTableBlock>
        HYDROGEN_PERIODIC_TABLE_BLOCK = fluidPeriodicTableBlock("hydrogen", 0x20FFFFFF, false),
        NITROGEN_PERIODIC_TABLE_BLOCK = fluidPeriodicTableBlock("nitrogen", 0x20FFFFFF, false),
        OXYGEN_PERIODIC_TABLE_BLOCK = fluidPeriodicTableBlock("oxygen", 0x20FFFFFF, false),
        FLUORINE_PERIODIC_TABLE_BLOCK = fluidPeriodicTableBlock("fluorine", 0x40F8F9A7, false),
        CHLORINE_PERIODIC_TABLE_BLOCK = fluidPeriodicTableBlock("chlorine", 0x40C0F9A7, false),
        MERCURY_PERIODIC_TABLE_BLOCK = fluidPeriodicTableBlock("mercury", 0xFFB3B3B3, true);

    // FOOD

    public static final BlockEntry<Block> MASHED_POTATO_BLOCK = REGISTRATE.block("mashed_potato_block", Block::new)
        .initialProperties(() -> Blocks.CLAY)
        .properties(p -> p
            .mapColor(MapColor.COLOR_YELLOW)
            .sound(SoundType.SLIME_BLOCK)
            .strength(0.2f)
        ).tag(BlockTags.MINEABLE_WITH_SHOVEL, BlockTags.MINEABLE_WITH_HOE)
        .item()
        .build()
        .register();

    public static final BlockEntry<RotatedPillarBlock> RAW_FRIES_BLOCK = REGISTRATE.block("raw_fries_block", RotatedPillarBlock::new)
        .initialProperties(() -> Blocks.CLAY)
        .properties(p -> p
            .mapColor(MapColor.COLOR_YELLOW)
            .sound(SoundType.SLIME_BLOCK)
            .strength(0.2f)
        ).loot((lt, b) -> lt.add(b, RegistrateBlockLootTables.createSilkTouchDispatchTable(b, LootItem.lootTableItem(DestroyItems.RAW_FRIES).apply(SetItemCountFunction.setCount(ConstantValue.exactly(5f))))))
        .tag(BlockTags.MINEABLE_WITH_SHOVEL, BlockTags.MINEABLE_WITH_HOE)
        .blockstate((c, p) -> p.axisBlock(c.get(),
            p.modLoc("block/raw_fries_block_side"),
            p.modLoc("block/raw_fries_block_end")))
        .item()
        .build()
        .register();

    // UNCATEGORISED

    public static final BlockEntry<Block> FIBERGLASS_BLOCK = REGISTRATE.block("fiberglass_block", Block::new)
        .initialProperties(() -> Blocks.GLASS)
        .properties(p -> p
            .strength(6f)
            .mapColor(MapColor.SNOW)
        ).tag(Tags.Blocks.GLASS, BlockTags.MINEABLE_WITH_PICKAXE)
        .item()
        .tag(Tags.Items.GLASS)
        .build()
        .register();

    public static final BlockEntry<GlassBlock>
    
    BOROSILICATE_GLASS = REGISTRATE.block("borosilicate_glass", GlassBlock::new)
        .initialProperties(() -> Blocks.GLASS)
        .properties(p -> p
            .strength(2f)
        ).tag(Tags.Blocks.GLASS, Tags.Blocks.GLASS_COLORLESS, BlockTags.MINEABLE_WITH_PICKAXE)
        .addLayer(() -> RenderType::cutoutMipped)
        .onRegister(connectedTextures(() -> new SimpleCTBehaviour(DestroySpriteShifts.BOROSILICATE_GLASS)))
        .item()
        .tag(Tags.Items.GLASS, Tags.Items.GLASS_COLORLESS)
        .build()
        .register();

    public static final BlockEntry<FlippableRotatedPillarBlock>
    
    PLYWOOD = REGISTRATE.block("plywood", FlippableRotatedPillarBlock::new)
        .properties(p -> p
            .mapColor(MapColor.WOOD)
            .sound(SoundType.WOOD)
            .instrument(NoteBlockInstrument.BASS)
            .strength(4.0f, 6.0f)
        ).tag(BlockTags.MINEABLE_WITH_AXE, BlockTags.PLANKS)
        .blockstate(DestroyBlockStateGen.flippableRotatedPillar("plywood"))
        .item()
        .tag(ItemTags.PLANKS)
        .build()
        .register(),

    UNVARNISHED_PLYWOOD = REGISTRATE.block("unvarnished_plywood", FlippableRotatedPillarBlock::new)
        .properties(p -> p
            .mapColor(MapColor.WOOD)
            .sound(SoundType.WOOD)
            .instrument(NoteBlockInstrument.BASS)
            .strength(3.0f, 5.0f)
            .ignitedByLava()
        ).tag(BlockTags.MINEABLE_WITH_AXE, BlockTags.PLANKS)
        .blockstate(DestroyBlockStateGen.flippableRotatedPillar("unvarnished_plywood"))
        .item(CombustibleBlockItem::new)
        .tag(ItemTags.PLANKS)
        .onRegister(i -> i.setBurnTime(2000))
        .build()
        .register();

    public static final BlockEntry<MoltenStainlessSteelBlock> MOLTEN_STAINLESS_STEEL = REGISTRATE.block("molten_stainless_steel", MoltenStainlessSteelBlock::new)
        .properties(p -> p
            .mapColor(MapColor.COLOR_ORANGE)
            .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
            .lightLevel(state -> 15)
            .noLootTable()
            .dynamicShape()
        ).tag(AllBlockTags.MOVABLE_EMPTY_COLLIDER.tag)
        .register();

    public static final BlockEntry<MoltenBorosilicateGlassBlock> MOLTEN_BOROSILICATE_GLASS = REGISTRATE.block("molten_borosilicate_glass", MoltenBorosilicateGlassBlock::new)
        .properties(p -> p
            .mapColor(MapColor.COLOR_ORANGE)
            .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
            .lightLevel(state -> 15)
            .noLootTable()
            .dynamicShape()
        ).tag(AllBlockTags.MOVABLE_EMPTY_COLLIDER.tag)
        .register();

    public static final BlockEntry<FastCoolingMoltenPillarBlock>
    
    STAINLESS_STEEL_RODS = REGISTRATE.block("stainless_steel_rods_block", FastCoolingMoltenPillarBlock::new)
        .initialProperties(STAINLESS_STEEL_BLOCK)
        .properties(p -> p
            .mapColor(state -> state.getValue(FastCoolingMoltenPillarBlock.MOLTEN) ? MapColor.COLOR_ORANGE : MapColor.METAL)
            .lightLevel(state -> state.getValue(FastCoolingMoltenPillarBlock.MOLTEN) ? 15 : 0)
        ).tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .blockstate(DestroyBlockStateGen.moltenRotatedPillar("stainless_steel_rods_block"))
        .item()
        .build()
        .register();

    public static final BlockEntry<BorosilicateGlassFiberBlock>

    BOROSILICATE_GLASS_FIBER = REGISTRATE.block("borosilicate_glass_fiber", BorosilicateGlassFiberBlock::new)
        .initialProperties(MOLTEN_BOROSILICATE_GLASS)
        .properties(p -> p
            .mapColor(state -> state.getValue(FastCoolingMoltenPillarBlock.MOLTEN) ? MapColor.COLOR_RED : MapColor.NONE)
            .lightLevel(state -> state.getValue(FastCoolingMoltenPillarBlock.MOLTEN) ? 15 : 0)
            .sound(SoundType.WOOL)
        ).tag(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.WOOL)
        .blockstate(DestroyBlockStateGen.moltenRotatedPillar("borosilicate_glass_fiber"))
        .item()
        .tag(ItemTags.WOOL)
        .build()
        .register();

    public static final BlockEntry<Block> CORDITE_BLOCK = REGISTRATE.block("cordite_block", Block::new)
        .initialProperties(() -> Blocks.CLAY)
        .properties(p -> p
            .mapColor(MapColor.COLOR_ORANGE)
            .sound(SoundType.SLIME_BLOCK)
            .strength(0.2f)
        ).tag(BlockTags.MINEABLE_WITH_SHOVEL)
        .tag(BlockTags.MINEABLE_WITH_HOE)
        .item()
        .build()
        .register();

    public static final BlockEntry<RotatedPillarBlock>

    INSULATED_STAINLESS_STEEL_BLOCK = REGISTRATE.block("insulated_stainless_steel_block", RotatedPillarBlock::new)
        .initialProperties(STAINLESS_STEEL_BLOCK)
        .onRegister(connectedTextures(() -> new EncasedCTBehaviour(DestroySpriteShifts.STAINLESS_STEEL_BLOCK)))
        .onRegister(casingConnectivity((block, cc) -> cc.make(block, DestroySpriteShifts.STAINLESS_STEEL_BLOCK,
			(s, f) -> f.getAxis() == s.getValue(RotatedPillarBlock.AXIS)))
        ).transform(TagGen.pickaxeOnly())
        .blockstate(DestroyBlockStateGen.rotatedPillar(Destroy.asResource("block/insulated_stainless_steel_block"), Destroy.asResource("block/stainless_steel_block")))
        .item()
        .build()
        .register(),
    
    EXTRUDED_CORDITE_BLOCK = REGISTRATE.block("extruded_cordite_block", RotatedPillarBlock::new)
        .initialProperties(() -> Blocks.CLAY)
        .properties(p -> p
            .mapColor(MapColor.COLOR_ORANGE)
            .sound(SoundType.SLIME_BLOCK)
            .strength(0.2f)
        ).loot((lt, b) -> lt.add(b, RegistrateBlockLootTables.createSilkTouchDispatchTable(b, LootItem.lootTableItem(DestroyItems.CORDITE).apply(SetItemCountFunction.setCount(ConstantValue.exactly(5f))))))
        .tag(BlockTags.MINEABLE_WITH_SHOVEL)
        .tag(BlockTags.MINEABLE_WITH_HOE)
        .blockstate(DestroyBlockStateGen.rotatedPillar("extruded_cordite_block"))
        .item()
        .build()
        .register(),

    CLAY_MONOLITH = REGISTRATE.block("clay_monolith", RotatedPillarBlock::new)
        .initialProperties(() -> Blocks.CLAY)
        .tag(BlockTags.MINEABLE_WITH_SHOVEL)
        .blockstate(DestroyBlockStateGen.rotatedPillar(new ResourceLocation("block/clay"), Destroy.asResource("block/clay_monolith_end")))
        .item()
        .build()
        .register(),

    CERAMIC_MONOLITH = REGISTRATE.block("ceramic_monolith", RotatedPillarBlock::new)
        .initialProperties(() -> Blocks.TERRACOTTA)
        .tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .blockstate(DestroyBlockStateGen.rotatedPillar("ceramic_monolith"))
        .item()
        .build()
        .register();

    public static NonNullConsumer<? super BlockItem> registerPrimeableBombDispenserBehaviour() {
        return item -> DispenserBlock.registerBehavior(item, ((PrimeableBombBlock<?>)item.getBlock()).new PrimeableBombDispenseBehaviour());
    };

    public static boolean never(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    };

    public static boolean never(BlockState state, BlockGetter level, BlockPos pos, EntityType<?> entity) {
        return false;
    };

    public static void register() {};
};