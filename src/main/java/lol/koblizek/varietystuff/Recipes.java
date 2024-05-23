package lol.koblizek.varietystuff;

import io.papermc.paper.event.player.PlayerChangeBeaconEffectEvent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

public final class Recipes {
    private Recipes() {}

    public static void notchApple() {
        ItemStack notchApple = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1);
        NamespacedKey goldenAppleKey = new NamespacedKey(VarietyStuff.instance, "notch_apple");
        ShapedRecipe goldenAppleRecipe = new ShapedRecipe(goldenAppleKey, notchApple);
        goldenAppleRecipe.shape("***", "*a*", "***");
        goldenAppleRecipe.setIngredient('a', Material.WITHER_SKELETON_SKULL);
        goldenAppleRecipe.setIngredient('*', Material.GOLD_INGOT);
        VarietyStuff.instance.getServer().addRecipe(goldenAppleRecipe);
    }

    public static void glisteringMelon() {
        // Modify glistering melon recipe
        Recipe melonRecipe = VarietyStuff.instance.getServer().getRecipe(new NamespacedKey("minecraft", "glistering_melon_slice"));
        if (melonRecipe instanceof ShapedRecipe shapedMelonRecipe) {
            shapedMelonRecipe.shape("ggg", "gmg", "ggg");
            shapedMelonRecipe.setIngredient('g', Material.GOLD_INGOT);
            shapedMelonRecipe.setIngredient('m', Material.MELON_SLICE);
            VarietyStuff.instance.getServer().removeRecipe(new NamespacedKey("minecraft", "glistering_melon_slice"));
            VarietyStuff.instance.getServer().addRecipe(shapedMelonRecipe, true);
        }
    }
}
