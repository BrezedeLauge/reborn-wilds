package de.laugenbreze.dungeonsalive.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

/** Appends at most one fresh gear item while preserving every generated vanilla stack. */
public final class ExplorationGearModifier extends LootModifier {
    private static final Codec<List<Item>> POOL = BuiltInRegistries.ITEM.byNameCodec().listOf().validate(items ->
            items.size() <= 32 ? DataResult.success(items) : DataResult.error(() -> "Exploration gear pools are limited to 32 items"));
    private static final Codec<List<Item>> REQUIRED_POOL = POOL.validate(items ->
            items.isEmpty() ? DataResult.error(() -> "Exploration gear needs a nonempty ordinary pool") : DataResult.success(items));
    public static final MapCodec<ExplorationGearModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> codecStart(instance)
            .and(instance.group(
                    Codec.floatRange(0, 1).fieldOf("chance").forGetter(value -> value.chance),
                    REQUIRED_POOL.fieldOf("items").forGetter(value -> value.items),
                    POOL.optionalFieldOf("rare_items", List.of()).forGetter(value -> value.rareItems),
                    Codec.floatRange(0, 1).optionalFieldOf("rare_chance", 0f).forGetter(value -> value.rareChance)))
            .apply(instance, ExplorationGearModifier::new));
    private final float chance;
    private final List<Item> items;
    private final List<Item> rareItems;
    private final float rareChance;

    private ExplorationGearModifier(LootItemCondition[] conditions, float chance, List<Item> items, List<Item> rareItems, float rareChance) {
        super(conditions);
        this.chance = chance; this.items = List.copyOf(items);
        this.rareItems = List.copyOf(rareItems); this.rareChance = rareChance;
    }

    @Override protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        var random = context.getRandom();
        if (random.nextFloat() >= chance) return generatedLoot;
        List<Item> pool = !rareItems.isEmpty() && random.nextFloat() < rareChance ? rareItems : items;
        generatedLoot.add(new ItemStack(pool.get(random.nextInt(pool.size()))));
        return generatedLoot;
    }

    @Override public MapCodec<? extends IGlobalLootModifier> codec() { return CODEC; }
}
