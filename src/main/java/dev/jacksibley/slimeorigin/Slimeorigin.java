package dev.jacksibley.slimeorigin;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeReference;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Slimeorigin implements ModInitializer {
    public static final String MOD_ID = "slimeorigin";

    public static final PowerType<Power> BOUNCY = new PowerTypeReference<Power>(new Identifier(MOD_ID, "bouncy"));
    public static final PowerType<Power> FRAGMENTATION = new PowerTypeReference<Power>(new Identifier(MOD_ID, "fragmentation"));
//    public static  final EntityAttribute SLIME_SIZE = Registry.register(Registry.ATTRIBUTE, new Identifier("slimeorigin", "slime_size"), new ClampedEntityAttribute("attribute.name.generic.slimeorigin.slime_size", 0, 0,3));

    public static final EntityAttribute SLIME_SIZE = make("slime_size", 0, 0 ,3);

    public static double getSlimeSize(final LivingEntity entity){
        final EntityAttributeInstance size = entity.getAttributeInstance(SLIME_SIZE);
        return size.getValue();
    }


    private static EntityAttribute make(final String name, final double base, final double min, final double max) {
        return new ClampedEntityAttribute("attribute.name.generic." + MOD_ID + '.'  + name, base, min, max).setTracked(true);
    }

    @Override
    public void onInitialize() {
        Registry.register(Registry.ATTRIBUTE, new Identifier(MOD_ID, "slime_size"), SLIME_SIZE);
    }
}
