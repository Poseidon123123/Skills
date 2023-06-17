package poseidon.skills.Listners;

import org.bukkit.Bukkit;
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
import poseidon.skills.Skills;
import poseidon.skills.skill.BerufSkills;
import poseidon.skills.skill.KampfSkills;
import poseidon.skills.skill.SkillMapper;

import java.util.List;
import java.util.Objects;

public class SkillListener implements Listener {

    private static void aktivator(PlayerItemHeldEvent event, String command, int cooldown){
        ItemStack item = event.getPlayer().getInventory().getItem(event.getNewSlot());
        event.getPlayer().performCommand("ASC " + command);
        Objects.requireNonNull(item).setAmount(cooldown);
        cooldown(item, event.getPlayer(),cooldown-1);
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
                    aktivator(event, b.getCommand(), b.getCooldown());
                    event.getPlayer().sendMessage(ChatColor.BLUE + "Der Skill " + b.getName() + " wurde ausgeführt");
                }
                return;
            }
        }
        for (BerufSkills b : SkillMapper.getBerufSkills()) {
            if (item.isSimilar(b.getIcon())) {
                event.setCancelled(true);
                if(item.getAmount() == 1) {
                    aktivator(event, b.getCommand(), b.getCooldown());
                    event.getPlayer().sendMessage(ChatColor.BLUE + "Der Skill " + b.getName() + " wurde ausgeführt");
                }
                return;
            }
        }

    }
    @EventHandler(ignoreCancelled = true)
    public void explosion(EntityExplodeEvent event){
        if(event.getEntity() instanceof Fireball){
            if(Objects.requireNonNull(event.getEntity().getCustomName()).equalsIgnoreCase("SFB")){
                List<Entity> list = event.getEntity().getNearbyEntities(5,5,5);
                System.out.println(list);
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
            Bukkit.getScheduler().runTaskLaterAsynchronously(Skills.getInstance(), () -> item.setAmount(item.getAmount() - 1), delay);
            countDown--;
        }
    }




}
