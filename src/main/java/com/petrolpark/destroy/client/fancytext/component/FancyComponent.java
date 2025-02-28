package com.petrolpark.destroy.client.fancytext.component;

import java.util.function.UnaryOperator;

import com.ibm.icu.text.BreakIterator;
import com.petrolpark.destroy.MoveToPetrolparkLibrary;
import com.petrolpark.destroy.client.fancytext.Paragraph;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

@MoveToPetrolparkLibrary
/**
 * An extension of regular Minecraft Components.
 */
public interface FancyComponent<C extends FancyComponent<C>> {

    /**
     * Draw this Component.
     * @param graphics
     * @param font
     * @param mouseX The x-coordinate of the cursor relative to the top left of this Component, or 0 if not in a situation with a visible cursor
     * @param mouseY The y-coordinate
     */
    public void render(GuiGraphics graphics, Font font, double mouseX, double mouseY);

    /**
     * The height of this Component, used to determine the height of the whole {@link Paragraph.Line Line} of text.
     * This is only accessed once, when the {@link Paragraph} is being generated.
     * For reference the height of the typical Minecraft font is {@code 9}.
     */
    public default int getHeight() {
        return 9;
    };

    public Type<? extends C> getType();

    /**
     * A wrapper for a vanilla Component.
     */
    public static abstract class Maker {

        protected final String delimitedText;

        public Maker(String delimitedText) {
            this.delimitedText = delimitedText;
        };

        /**
         * Calculate the width of the resultant "fancified" text so the {@link Paragraph} generator knows where to insert line breaks.
         * @param plainText 
         * @param maxWidthPerLine
         * @param font The Font in which the text will be rendered
         */
        public int getWidth(String plainText, Font font) {
            return font.width(Component.literal(plainText));
        };

        /**
         * Get the BreakIterator which will split text into the smallest possible raw Strings into which the text between the delimiters can be split.
         */
        public BreakIterator getLineBreaker() {
            return BreakIterator.getLineInstance(Minecraft.getInstance().getLocale());
        };

        /**
         * Get the plain text which we will use the {@link Maker#getLineBreaker() line breaker} to try and separate into lines.
         */
        public abstract String getPlainText();
    
        /**
         * Generate the {@link FancyComponent} from raw text.
         * @param plainText A single {@link Maker#getPlainTextWords() raw String}.
         */
        public abstract FancyComponent<?> make(String plainText);
        
    };

    @FunctionalInterface
    public interface MakerMaker {

        public Maker make(String delimitedText);
    };

    public interface Type<C extends FancyComponent<?>> {};

    public static class Simple implements FancyComponent<Simple> {

        private final Component component;
        private final boolean shadow;

        public static final Type<Simple> TYPE = new Type<>(){};

        public static MakerMaker maker() {
            return with(component -> component);
        };

        public static MakerMaker with(UnaryOperator<Component> componentTransform) {
            return with(componentTransform, false);
        };

        public static MakerMaker with(UnaryOperator<Component> componentTransform, boolean shadow) {
            return (s) -> new Maker(s, componentTransform, shadow);
        };

        public Simple(Component component, boolean shadow) {
            this.component = component;
            this.shadow = shadow;
        };

        @Override
        public void render(GuiGraphics graphics, Font font, double mouseX, double mouseY) {
            graphics.drawString(font, component, 0, 0, 0xFFFFFF, shadow);
        };

        @Override
        public Type<? extends Simple> getType() {
            return TYPE;
        };

        public static class Maker extends FancyComponent.Maker {

            private final Component component;
            private final boolean shadow;

            protected Maker(String delimitedText, UnaryOperator<Component> componentTransform, boolean shadow) {
                super(delimitedText);
                component = componentTransform.apply(Component.literal(delimitedText));
                this.shadow = shadow;
            };

            @Override
            public int getWidth(String plainText, Font font) {
                return font.width(component);
            };

            @Override
            public String getPlainText() {
                return delimitedText;
            };

            @Override
            public FancyComponent<?> make(String plainText) {
                return new Simple(component, shadow);
            };

        };

    };

    public static final Error ERROR = new Error();

    public static class Error implements FancyComponent<Error> {

        public static final Type<Error> TYPE = new Type<>(){};

        @Override
        public void render(GuiGraphics graphics, Font font, double mouseX, double mouseY) {
            graphics.drawString(font, "error", 0, 0, 0xFFFFFF);
        };

        @Override
        public Type<? extends Error> getType() {
            return TYPE;
        };

    };
    
};
