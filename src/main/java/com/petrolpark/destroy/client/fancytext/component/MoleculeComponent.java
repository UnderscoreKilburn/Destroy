package com.petrolpark.destroy.client.fancytext.component;

import java.util.function.UnaryOperator;

import com.petrolpark.destroy.chemistry.legacy.LegacySpecies;
import com.petrolpark.destroy.config.DestroyAllConfigs;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class MoleculeComponent extends FancyComponent.Simple {

    public final LegacySpecies molecule;

    public static final FancyComponent.Type<MoleculeComponent> TYPE = new FancyComponent.Type<MoleculeComponent>(){};

    public MoleculeComponent(Component component, LegacySpecies molecule) {
        super(component, false);
        this.molecule = molecule;
    };

    @Override
    public void render(GuiGraphics graphics, Font font, double mouseX, double mouseY) {
        super.render(graphics, font, mouseX, mouseY);
    };

    @Override
    public Type<MoleculeComponent> getType() {
        return TYPE;
    };

    public class Maker extends FancyComponent.Simple.Maker {
        
        private final LegacySpecies molecule;

        public Maker(String delimitedText, UnaryOperator<Component> componentTransform, boolean shadow) {
            super(delimitedText, componentTransform, shadow);
            molecule = LegacySpecies.getMolecule(delimitedText);
        };

        @Override
        public String getPlainText() {
            if (molecule == null) return "Unknown molecule";
            return molecule.getName(DestroyAllConfigs.CLIENT.chemistry.iupacNames.get()).getString();
        };

        @Override
        public FancyComponent<?> make(String plainText) {
            if (molecule == null) return FancyComponent.ERROR;
            return new MoleculeComponent(Component.literal(plainText), molecule);
        };

    };
    
};
