package com.petrolpark.destroy.mixin.client;

import net.minecraft.client.resources.language.ClientLanguage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.BiConsumer;

@Mixin(ClientLanguage.class)
public class ClientLanguageMixin {
    /*
    * A duct tape solution, not good.
    * The server can't know whether a molecule has an IUPAC name or not because a molecule may have a defined IUPAC name in one language, but not another,
    * therefore all molecules should have a defined IUPAC name in all languages, even if said name would be identical to their regular name.
    * The correct fix would be to add default IUPAC entries to all existing language files but I can't be bothered to do that right now.
    * */
    @ModifyArg(
        method="appendFrom",
        at=@At(
            value="INVOKE",
            target="Lnet/minecraft/locale/Language;loadFromJson(Ljava/io/InputStream;Ljava/util/function/BiConsumer;)V"
        ),
        index=1
    )
    private static BiConsumer<String, String> addDefaultChemicalIUPACKeys(BiConsumer<String, String> pOutput) {
        // Also this assumes IUPAC names are always declared after regular names in language files, wow good job me.
        return (k, v) -> {
            if(k.matches("^[^.]+\\.chemical\\.[^.]+$"))
                pOutput.accept(k+".iupac", v);
            pOutput.accept(k, v);
        };
    }
}
