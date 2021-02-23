package dev.jacksibley.slimeorigin;

import com.sun.org.apache.xpath.internal.operations.Mod;
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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class Slimeorigin implements ModInitializer {
    public static final PowerType<Power> BOUNCY = new PowerTypeReference<Power>(new Identifier("slimeorigin", "bouncy"));
    public static final PowerType<Power> FRAGMENTATION = new PowerTypeReference<Power>(new Identifier("slimeorigin", "fragmentation"));
    public static  final EntityAttribute SLIME_SIZE = Registry.register(Registry.ATTRIBUTE, new Identifier("slimeorigin", "slime_size"), new ClampedEntityAttribute("attribute.name.generic.slimeorigin.slime_size", 0, 0,3));

    private static final Map<ConditionFactory<PlayerEntity>, Identifier> PLAYER_CONDITIONS = new LinkedHashMap<>();

    public static final ConditionFactory<PlayerEntity> TEMPERATURE = createPlayerCondition(
            new ConditionFactory<>(
                    new Identifier("slimeorigin", "temperature"),
                    new SerializableData().add("comparison", SerializableDataType.COMPARISON).add("compare_to", SerializableDataType.FLOAT),
                    (data, player) -> ((Comparison)data.get("comparison")).compare(player.world.getBiome(player.getBlockPos()).getTemperature(), data.getFloat("compare_to"))
            )
    );

    private static ConditionFactory<PlayerEntity> createPlayerCondition(ConditionFactory<PlayerEntity> factory) {
        PLAYER_CONDITIONS.put(factory, factory.getSerializerId());
        return factory;
    }

    @Override
    public void onInitialize() {
        PLAYER_CONDITIONS.keySet().forEach(condition -> Registry.register(ModRegistries.PLAYER_CONDITION, PLAYER_CONDITIONS.get(condition), condition));
    }
}
