package dev.jacksibley.slimeorigin;

import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.PowerType;
import io.github.apace100.origins.power.PowerTypeReference;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Slimeorigin implements ModInitializer {
    public static final PowerType<Power> BOUNCY = new PowerTypeReference<Power>(new Identifier("slimeorigin", "bouncy"));
    public static final PowerType<Power> FRAGMENTATION = new PowerTypeReference<Power>(new Identifier("slimeorigin", "fragmentation"));
    public static  final EntityAttribute SLIME_SIZE = Registry.register(Registry.ATTRIBUTE, new Identifier("slimeorigin", "slime_size"), new ClampedEntityAttribute("attribute.name.generic.slimeorigin.slime_size", 0, 0,3));

    @Override
    public void onInitialize() {


    }
}
