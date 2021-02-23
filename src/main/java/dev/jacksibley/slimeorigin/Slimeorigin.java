package dev.jacksibley.slimeorigin;

import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.PowerType;
import io.github.apace100.origins.power.PowerTypeReference;
import io.github.apace100.origins.power.factory.condition.ConditionFactory;
import io.github.apace100.origins.registry.ModRegistries;
import io.github.apace100.origins.util.Comparison;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
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
        //Register Temperature Condition
        Registry.register(
                ModRegistries.PLAYER_CONDITION,
                new Identifier("slimeorigin", "temperature"),
                new ConditionFactory<>(
                        new Identifier("slimeorigin", "temperature"),
                        new SerializableData().add("comparison", SerializableDataType.COMPARISON).add("compare_to", SerializableDataType.FLOAT),
                        (data, player) -> ((Comparison)data.get("comparison")).compare(player.world.getBiome(player.getBlockPos()).getTemperature(), data.getFloat("compare_to"))
                        )
                );
    }
}
