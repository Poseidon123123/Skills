package poseidon.skills;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import poseidon.skills.Klassen.Berufklasse;
import poseidon.skills.Klassen.KlassChoose;
import poseidon.skills.Klassen.Players;
import poseidon.skills.skill.BerufSkills;
import poseidon.skills.skill.KampfSkills;
import poseidon.skills.skill.SkillMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SkillListener implements Listener {

    private static void aktivator(PlayerItemHeldEvent event, String command, int cooldown){
        ItemStack item = event.getPlayer().getInventory().getItem(event.getNewSlot());
        event.setCancelled(true);
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
            if (item.equals(b.getIcon())) {
                aktivator(event, b.getCommand(), b.getCooldown());
                event.getPlayer().sendMessage(ChatColor.BLUE + "Der Skill " + b.getName() + " wurde ausgeführt");
                return;
            }
        }
        for (BerufSkills b : SkillMapper.getBerufSkills()) {
            if (item.equals(b.getIcon())) {
                aktivator(event, b.getCommand(), b.getCooldown());
                event.getPlayer().sendMessage(ChatColor.BLUE + "Der Skill " + b.getName() + " wurde ausgeführt");
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

    @EventHandler(ignoreCancelled = true)
    public static void killEvent(EntityDeathEvent event){
        LivingEntity entity = event.getEntity();
        //TODO mehr mobs hinzufügen
        if(entity instanceof Zombie) {
            if(entity.getKiller() != null) {
                Players p = KlassChoose.getPlayers(Objects.requireNonNull(entity.getKiller()));
                if (p.getBerufklasse().equals(Berufklasse.Unchosed)) {
                    return;
                }
                int xp = KillXP.valueOf(entity.getName()).getXp();
                xpAdd(p, xp, false);
            }
        }

    }

    public static void xpAdd(Players p, int xp, boolean beruf){
        if(beruf) {
            int i1 = p.getBerufLevel() - 1;
            int i2 = 0;
            int xp2 = xp + p.getBerufXP();
            ArrayList<Integer> neededxp = new ArrayList<>();
            int level = 0;
            while (i2 <= 100 && i1 < 101) {
                neededxp.add(((i1 + 10) * (i1 + 10)) + 50);
                i1++;
                i2++;
            }
            for (int i : neededxp) {
                if (xp2 >= i) {
                    xp2 = xp2 - i;
                    level++;
                } else break;
            }
            p.setBerufLevel(p.getBerufLevel() + level);
            p.setBerufXP(xp2);
            p.getPlayer().sendMessage(ChatColor.GREEN + String.valueOf(xp) + " XP");
            if (level >= 1) {
                p.getPlayer().sendMessage(ChatColor.GREEN + "Herzlichen Glückwunsch, du bist " + level + " Level aufgestiegen\n" +
                        "Du bist jetzt Level: " + p.getBerufLevel());
            }
        }
        else {
            int i1 = p.getKampfLevel() - 1;
            int i2 = 0;
            int xp2 = xp + p.getKampfXP();
            ArrayList<Integer> neededxp = new ArrayList<>();
            int level = 0;
            while (i2 <= 100 && i1 < 101) {
                neededxp.add(((i1 + 10) * (i1 + 10)) + 50);
                System.out.println(i1 + " " + i2);
                i1++;
                i2++;
            }
            for (int i : neededxp) {
                if (xp2 >= i) {
                    xp2 = xp2 - i;
                    level++;
                } else break;
            }
            p.setKampfLevel(p.getKampfLevel() + level);
            p.setKampfXP(xp2);
            p.getPlayer().sendMessage(ChatColor.GREEN + String.valueOf(xp) + " XP");
            if (level >= 1) {
                p.getPlayer().sendMessage(ChatColor.GREEN + "Herzlichen Glückwunsch, du bist " + level + " Level aufgestiegen\n" +
                        "Du bist jetzt Level: " + p.getKampfLevel());
            }
        }
    }
}
