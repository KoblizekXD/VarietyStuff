package lol.koblizek.varietystuff;

import com.destroystokyo.paper.event.player.PlayerAdvancementCriterionGrantEvent;
import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import io.papermc.paper.event.player.PlayerChangeBeaconEffectEvent;
import org.apache.commons.lang3.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.PigZombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public final class VarietyStuff extends JavaPlugin implements Listener {

    public static VarietyStuff instance;

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("ruleset").setExecutor(new RuleSetCommand());
        Recipes.notchApple();
        Recipes.glisteringMelon();
        getServer().updateRecipes();
    }

    @EventHandler
    void onVillagerAcquireTrade(VillagerAcquireTradeEvent e) {
        if (e.getRecipe().getResult().getType() == Material.GLISTERING_MELON_SLICE) {
            MerchantRecipe offer = e.getRecipe();
            MerchantRecipe newOffer = new MerchantRecipe(new ItemStack(Material.MELON_SEEDS, 1), offer.getUses(), offer.getMaxUses(), offer.hasExperienceReward(), offer.getVillagerExperience(), offer.getPriceMultiplier());
            newOffer.setIngredients(offer.getIngredients());
            e.setRecipe(newOffer);
        } else if (e.getRecipe().getResult().getType() == Material.TIPPED_ARROW) {
            PotionMeta meta = (PotionMeta) e.getRecipe().getResult().getItemMeta();
            if (meta.getBasePotionType() == PotionType.HEALING
                    || meta.getBasePotionType() == PotionType.STRONG_HEALING
                    || meta.getBasePotionType() == PotionType.REGENERATION
                    || meta.getBasePotionType() == PotionType.STRONG_REGENERATION) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    void onBeaconActivate(PlayerChangeBeaconEffectEvent e) {
        if (e.getSecondary() == PotionEffectType.REGENERATION) {
            // Get beacon inv
            InventoryView view = e.getPlayer().getOpenInventory();

            e.getPlayer().sendMessage(view.getTopInventory().getItem(0).getType().toString());
            if (view.getTopInventory().getItem(0).getType() != Material.NETHERITE_INGOT
                || !blocksUnder4AreGold(e.getBeacon().getLocation())) {
                e.setCancelled(true);
            }
        }
    }

    private boolean blocksUnder4AreGold(Location location) {
        for (int y = 1; y <= 4; y++) {
            if (location.subtract(0, 1, 0).getBlock().getType() != Material.GOLD_BLOCK) {
                return false;
            }
        }
        return true;
    }

    @EventHandler
    void onPlayerRespawn(PlayerPostRespawnEvent e) {
        e.getPlayer().setHealth(1f);
    }

    @EventHandler
    void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity() instanceof Ghast) {
            int h = hasMaterial(e.getDrops().toArray(ItemStack[]::new), Material.GHAST_TEAR);
            if (h != -1) {
                e.getDrops().remove(h);
            }   
            if (randomInt(1, 20) == 1) {
                e.getDrops().add(new ItemStack(Material.GHAST_TEAR, 1));
            }
        } else if (e.getEntity() instanceof PigZombie) {
            e.getDrops().removeIf(drop -> drop.getType() == Material.GOLD_NUGGET
                    || drop.getType() == Material.GOLD_INGOT
                    || drop.getType() == Material.GOLDEN_SWORD);
            e.getDrops().add(new ItemStack(Material.GOLD_NUGGET, 1));
        }
    }

    private int hasMaterial(ItemStack[] drops, Material material) {
        for (int i = 0; i < drops.length; i++) {
            if (drops[i].getType() == material) {
                return i;
            }
        }
        return -1;
    }

    private int randomInt(int min, int max) {
        return RandomUtils.nextInt(min, max + 1);
    }

    @EventHandler
    void advancementGot(PlayerAdvancementDoneEvent e) {
        if (e.getAdvancement().equals(Bukkit.getAdvancement(new NamespacedKey("minecraft", "nether/return_to_sender")))) {
            e.getPlayer().
                    getWorld().dropItemNaturally(e.getPlayer().getLocation().add(0, 1, 0), new ItemStack(Material.GOLDEN_APPLE, 1));
        }
    }
}
