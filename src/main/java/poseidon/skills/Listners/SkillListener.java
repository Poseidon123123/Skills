package poseidon.skills.Listners;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import poseidon.skills.CooldownSystem;
import poseidon.skills.CustomItems.CustomItem;
import poseidon.skills.CustomItems.CustomShapedRecipe;
import poseidon.skills.CustomItems.CustomShapelessRecipe;
import poseidon.skills.Klassen.Berufklasse;
import poseidon.skills.Klassen.Kampfklassen;
import poseidon.skills.Klassen.KlassChoose;
import poseidon.skills.Klassen.Players;
import poseidon.skills.ManaMap;
import poseidon.skills.Skills;
import poseidon.skills.runner.RunSkills;
import poseidon.skills.skill.BerufSkills;
import poseidon.skills.skill.KampfSkills;
import poseidon.skills.skill.SkillMapper;

import java.util.List;
import java.util.Objects;

public class SkillListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void pickUp(EntityPickupItemEvent event){
        if(!(event.getEntity() instanceof Player player)){
            return;
        }
        ItemStack item = event.getItem().getItemStack();
        if (item.getType().isAir()) {
            return;
        }
        CustomItem customItem = CustomItem.getByName(Objects.requireNonNull(item.getItemMeta()).getDisplayName());
        if(customItem.isKampfItem()){
            Kampfklassen kampfklassen = customItem.getKampfklassen();
            if(!KlassChoose.getPlayers(player).getKampfklasse().equals(kampfklassen)){
                event.setCancelled(true);
            }
        }
        if(customItem.isBerufItem()){
            Berufklasse kampfklassen = customItem.getBerufklasse();
            if(!KlassChoose.getPlayers(player).getBerufklasse().equals(kampfklassen)){
                event.setCancelled(true);
            }
        }
    }
    @EventHandler(ignoreCancelled = true)
    public void inventoryScroll(PlayerItemHeldEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItem(event.getNewSlot());
        if (item == null || item.getType().isAir()) {
            return;
        }
        for (KampfSkills b : SkillMapper.getKampfSkills()) {
            if (item.isSimilar(b.getIcon())) {
                event.setCancelled(true);
                CooldownSystem cooldownSystem = CooldownSystem.getbyPlayer(event.getPlayer());
                if(!cooldownSystem.hasCoolDown(b)) {
                    ManaMap map = new ManaMap(event.getPlayer());
                    if(map.useMana(b.getConsumedMana())) {
                        RunSkills.decode(event.getPlayer(), b.getCommand());
                        event.getPlayer().sendMessage(ChatColor.BLUE + "Der Skill " + b.getName() + " wurde ausgeführt");
                        ItemStack item1 = event.getPlayer().getInventory().getItem(event.getNewSlot());
                        cooldownSystem.addCoolDown(b, b.getCooldown(), item1, event.getPlayer());
                    }
                }
                return;
            }
        }
        for (BerufSkills b : SkillMapper.getBerufSkills()) {
            if (item.isSimilar(b.getIcon())) {
                CooldownSystem cooldownSystem = CooldownSystem.getbyPlayer(event.getPlayer());
                if(!cooldownSystem.hasCoolDown(b)) {
                    ManaMap map = new ManaMap(event.getPlayer());
                    if(map.useMana(b.getConsumedMana())) {
                        RunSkills.decode(event.getPlayer(), b.getCommand());
                        event.getPlayer().sendMessage(ChatColor.BLUE + "Der Skill " + b.getName() + " wurde ausgeführt");
                        ItemStack item1 = event.getPlayer().getInventory().getItem(event.getNewSlot());
                        cooldownSystem.addCoolDown(b, b.getCooldown(), item1, event.getPlayer());
                    }
                }
                return;
            }
        }
        CustomItem customItem = CustomItem.getByName(Objects.requireNonNull(item.getItemMeta()).getDisplayName());
        if(customItem == null){
            return;
        }
        if(customItem.isKampfItem()){
            Kampfklassen kampfklassen = customItem.getKampfklassen();
            if(!KlassChoose.getPlayers(event.getPlayer()).getKampfklasse().equals(kampfklassen)){
                event.getPlayer().getInventory().remove(item);
                event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), item).setPickupDelay(100);
            }
        }
        if(customItem.isBerufItem()){
            Berufklasse kampfklassen = customItem.getBerufklasse();
            if(!KlassChoose.getPlayers(event.getPlayer()).getBerufklasse().equals(kampfklassen)){
                event.getPlayer().getInventory().remove(item);
                event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), item).setPickupDelay(100);
            }
        }
    }
    @EventHandler(ignoreCancelled = true)
    public void explosion(EntityExplodeEvent event){
        if(event.getEntity() instanceof Fireball){
            if(Objects.requireNonNull(event.getEntity().getCustomName()).equalsIgnoreCase("SFB")){
                List<Entity> list = event.getEntity().getNearbyEntities(5,5,5);
                list.forEach(entity -> {
                    if(entity instanceof LivingEntity mob) {
                        mob.setFireTicks(100);
                        double health = mob.getHealth();
                        if (health - 5 <= 0) {
                            mob.setHealth(0);
                        } else {
                            mob.setHealth(health - 5);
                        }
                    }
                });
                event.setCancelled(true);
            }
        }
    }
    @EventHandler(ignoreCancelled = false)
    public void onRightClick(PlayerInteractEvent event){
        if(event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        Player player = event.getPlayer();
        Players players = KlassChoose.getPlayers(player);
        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (players.getKampfItemSkill() != null && players.getKampfItemSkill().isSimilar(event.getItem()) && players.getBoundKampf() != null) {
                KampfSkills b = players.getBoundKampf();
                CooldownSystem cooldownSystem = CooldownSystem.getbyPlayer(event.getPlayer());
                if (!cooldownSystem.hasCoolDown(b)) {
                    ManaMap map = new ManaMap(event.getPlayer());
                    if (map.useMana(b.getConsumedMana())) {
                        RunSkills.decode(event.getPlayer(), b.getCommand());
                        event.getPlayer().sendMessage(ChatColor.BLUE + "Der Skill " + b.getName() + " wurde ausgeführt");
                        cooldownSystem.addCoolDown(b, b.getCooldown());
                    }
                }
                else {
                    player.sendMessage(ChatColor.RED + "Cooldown noch " +  cooldownSystem.getCooldown(b) + " Sekunden");
                }
            }
            if (players.getBerufItemSkill() != null && players.getBerufItemSkill().isSimilar(event.getItem()) && players.getBoundBeruf() != null) {
                System.out.println(6);
                BerufSkills b = players.getBoundBeruf();
                CooldownSystem cooldownSystem = CooldownSystem.getbyPlayer(event.getPlayer());
                if (!cooldownSystem.hasCoolDown(b)) {
                    System.out.println(7);
                    ManaMap map = new ManaMap(event.getPlayer());
                    if (map.useMana(b.getConsumedMana())) {
                        System.out.println(8);
                        RunSkills.decode(event.getPlayer(), b.getCommand());
                        event.getPlayer().sendMessage(ChatColor.BLUE + "Der Skill " + b.getName() + " wurde ausgeführt");
                        cooldownSystem.addCoolDown(b, b.getCooldown());
                    }
                }
                else {
                    player.sendMessage(ChatColor.RED + "Cooldown noch" + cooldownSystem.getCooldown(b) + " Sekunden");
                }
            }
        }
    }



    @EventHandler(ignoreCancelled = true)
    public void onCraft(CraftItemEvent event){
        NamespacedKey ShapedKey = CustomShapedRecipe.getKeyByItem(event.getRecipe().getResult());
        NamespacedKey ShapelessKey = CustomShapelessRecipe.getKeyByItem(event.getRecipe().getResult());
        Berufklasse recipeBeruf = null;
        Kampfklassen recipeKampf = null;
        int kampfLevel = 0;
        int berufLevel = 0;
        if(ShapedKey != null){
            CustomShapedRecipe recipe = CustomShapedRecipe.getByKey(ShapedKey);
            recipeBeruf = recipe.getBerufklasse();
            recipeKampf = recipe.getKampfklassen();
            kampfLevel = recipe.getNeededKampfLevel();
            berufLevel = recipe.getNeededBerufLevel();
            if(recipe.noRestriction()){
                return;
            }
        }
        if(ShapelessKey != null){
            CustomShapelessRecipe recipe = CustomShapelessRecipe.getByKey(ShapelessKey);
            recipeBeruf = recipe.getBerufklasse();
            recipeKampf = recipe.getKampfklassen();
            kampfLevel = recipe.getNeededKampfLevel();
            berufLevel = recipe.getNeededBerufLevel();
            if(recipe.noRestriction()){
                return;
            }
        }
        Player player = (Player) event.getView().getPlayer();
        Players players = KlassChoose.getPlayers(player);

        if(restrict(players, recipeKampf,recipeBeruf,kampfLevel,berufLevel)) {
            player.sendMessage(ChatColor.RED + "Du hast nicht die nötige Erfahrung um das zu Craften!");
            event.setCancelled(true);
        }
    }
    public static boolean restrict(Players players, Kampfklassen recipeKampf, Berufklasse recipeBeruf, int kampfLevel, int berufLevel){
        if(recipeKampf != Kampfklassen.Unchosed && recipeBeruf != Berufklasse.Unchosed ){
            if(players.getKampfklasse() != recipeKampf || players.getBerufklasse() != recipeBeruf){
                return true;
            }
        }
        if(recipeBeruf != Berufklasse.Unchosed && players.getBerufklasse() != recipeBeruf){
            return true;
        }
        if(recipeKampf != Kampfklassen.Unchosed && players.getKampfklasse() != recipeKampf){
            return true;
        }
        if(kampfLevel > 0 && berufLevel > 0){
            if(players.getKampfLevel() < kampfLevel || players.getBerufLevel() < berufLevel){
                return true;
            }
        }
        if(kampfLevel > 0 && players.getKampfLevel() < kampfLevel){
            return true;
        }
        return berufLevel > 0 && players.getBerufLevel() < berufLevel;
    }

    public static void cooldown(ItemStack item, Player player, int countDown){
        int pos = player.getInventory().first(item);
        if(pos < 1){
            return;
        }
        while (countDown > 0){
            int delay = countDown * 20;
            Skills.getInstance().getServer().getScheduler().runTaskLater(Skills.getInstance(), () -> item.setAmount(item.getAmount() - 1), delay);
            countDown--;
        }

    }
}
