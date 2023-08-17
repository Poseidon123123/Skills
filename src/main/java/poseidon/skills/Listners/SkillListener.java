package poseidon.skills.Listners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import poseidon.skills.CustomItems.CustomItem;
import poseidon.skills.Klassen.Berufklasse;
import poseidon.skills.Klassen.Kampfklassen;
import poseidon.skills.Klassen.KlassChoose;
import poseidon.skills.ManaMap;
import poseidon.skills.Skills;
import poseidon.skills.runner.RunSkills;
import poseidon.skills.skill.BerufSkills;
import poseidon.skills.skill.KampfSkills;
import poseidon.skills.skill.SkillMapper;

import java.util.List;
import java.util.Objects;

public class SkillListener implements Listener {

    private static boolean aktivator(PlayerItemHeldEvent event, String command, int cooldown, int mana){
        ItemStack item = event.getPlayer().getInventory().getItem(event.getNewSlot());
        ManaMap map = new ManaMap(event.getPlayer());
        boolean a = map.useMana(mana);
        if(a) {
            RunSkills runSkills = new RunSkills(event.getPlayer());
            switch (command) {
                case "B1" -> runSkills.B1();
                case "B2" -> runSkills.B2();
                case "BB1" -> runSkills.BB1();
                case "BK1" -> runSkills.BK1();
            }
            Objects.requireNonNull(item).setAmount(cooldown);
            cooldown(item, event.getPlayer(), cooldown - 1);
        }
        return a;
    }

    @EventHandler(ignoreCancelled = true)
    public void inventoryScroll(PlayerItemHeldEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItem(event.getNewSlot());
        if (item == null) {
            return;
        }
        for (KampfSkills b : SkillMapper.getKampfSkills()) {
            if (item.isSimilar(b.getIcon())) {
                event.setCancelled(true);
                if(item.getAmount() == 1) {
                    if(aktivator(event, b.getCommand(), b.getCooldown(), b.getConsumedMana())) {
                        event.getPlayer().sendMessage(ChatColor.BLUE + "Der Skill " + b.getName() + " wurde ausgeführt");
                    }
                }
                return;
            }
        }
        for (BerufSkills b : SkillMapper.getBerufSkills()) {
            if (item.isSimilar(b.getIcon())) {
                event.setCancelled(true);
                if(item.getAmount() == 1) {
                    if(aktivator(event, b.getCommand(), b.getCooldown(), b.getConsumedMana())) {
                        event.getPlayer().sendMessage(ChatColor.BLUE + "Der Skill " + b.getName() + " wurde ausgeführt");
                    }
                }
                return;
            }
        }
        if(CustomItem.kampfItemMap.containsKey(item)){
            Kampfklassen kampfklassen = CustomItem.kampfItemMap.get(item);
            if(!KlassChoose.getPlayers(event.getPlayer()).getKampfklasse().equals(kampfklassen)){
                event.getPlayer().dropItem(true);
            }
        }
        if(CustomItem.berufItemMap.containsKey(item)){
            Berufklasse kampfklassen = CustomItem.berufItemMap.get(item);
            if(!KlassChoose.getPlayers(event.getPlayer()).getBerufklasse().equals(kampfklassen)){
                event.getPlayer().dropItem(true);
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
