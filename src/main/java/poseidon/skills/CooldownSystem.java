package poseidon.skills;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import poseidon.skills.Klassen.KlassChoose;
import poseidon.skills.Klassen.Players;
import poseidon.skills.skill.BerufSkills;
import poseidon.skills.skill.KampfSkills;

import java.util.*;

public class CooldownSystem {
    private static final HashMap<UUID, CooldownSystem> cooldownSystemHashMap = new HashMap<>();
    public static void registerPlayer(Player player){
        if(!cooldownSystemHashMap.containsKey(player.getUniqueId())) {
            cooldownSystemHashMap.put(player.getUniqueId(), new CooldownSystem(player));
        }
    }
    public static CooldownSystem getbyPlayer(Player player){
        return cooldownSystemHashMap.get(player.getUniqueId());
    }
    public static void performActionAndCleanup() {
        int delay = Skills.getInstance().value("Values.Cooldown.TimerCheck");
        Bukkit.getScheduler().runTaskTimer(Skills.getInstance(), () -> {
            List<UUID> uuidsToRemove = new ArrayList<>();
            List<UUID> uuidList = new ArrayList<>();
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                uuidList.add(p.getUniqueId());
            }
            for (Map.Entry<UUID, CooldownSystem> entry : cooldownSystemHashMap.entrySet()) {
                UUID uuid = entry.getKey();
                CooldownSystem obj = entry.getValue();
                if (!uuidList.contains(uuid)) {
                    uuidsToRemove.add(uuid);
                }
                else {
                    obj.restartCooldowns();
                }
            }
            for (UUID uuidToRemove : uuidsToRemove) {
                cooldownSystemHashMap.remove(uuidToRemove);
            }
        }, 200, delay);
    }
    public void restartCooldowns(){
        stopCooldowns();
        startCooldowns();
    }
    public void startCooldowns(){
        BerufID = Bukkit.getScheduler().runTaskTimer(Skills.getInstance(), () -> {
            if (BerufCooldown.isEmpty()) {
                return;
            }

            Iterator<Map.Entry<BerufSkills, Integer>> berufIterator = BerufCooldown.entrySet().iterator();
            while (berufIterator.hasNext()) {
                Map.Entry<BerufSkills, Integer> entry = berufIterator.next();
                BerufSkills berufSkills = entry.getKey();
                int remainingCooldown = entry.getValue();

                if (remainingCooldown - 1 > 0) {
                    entry.setValue(remainingCooldown - 1);
                } else {
                    berufIterator.remove();
                }
            }
        }, 0, 20).getTaskId();

        KampfID = Bukkit.getScheduler().runTaskTimer(Skills.getInstance(), () -> {
            if (KampfCooldown.isEmpty()) {
                return;
            }
            Iterator<Map.Entry<KampfSkills, Integer>> kampfIterator = KampfCooldown.entrySet().iterator();
            while (kampfIterator.hasNext()) {
                Map.Entry<KampfSkills, Integer> entry = kampfIterator.next();
                KampfSkills berufSkills = entry.getKey();
                int remainingCooldown = entry.getValue();

                if (remainingCooldown - 1 > 0) {
                    entry.setValue(remainingCooldown - 1);
                } else {
                    kampfIterator.remove();
                }
            }
        }, 0, 20).getTaskId();

    }
    public void stopCooldowns(){
        if(BerufID != 0) {
            Bukkit.getScheduler().cancelTask(BerufID);
        }
        if(KampfID != 0) {
            Bukkit.getScheduler().cancelTask(KampfID);
        }
    }
    private int BerufID = 0;
    private int KampfID = 0;
    private final Players players;
    private final HashMap<BerufSkills, Integer> BerufCooldown = new HashMap<>();
    public HashMap<BerufSkills, Integer> getBerufCooldown(){
        return BerufCooldown;
    }
    private final HashMap<KampfSkills, Integer> KampfCooldown = new HashMap<>();
    public  HashMap<KampfSkills, Integer> getKampfCooldown(){
        return KampfCooldown;
    }
    public CooldownSystem(Player player){
        this.players = KlassChoose.getPlayers(player);
    }
    public Players getPlayers() {
        return players;
    }
    public void addCoolDown(BerufSkills berufSkills, int delay){
        BerufCooldown.put(berufSkills, delay);
    }
    public void addCoolDown(BerufSkills berufSkills, int delay, ItemStack itemStack, Player player){
        BerufCooldown.put(berufSkills, delay);
        cooldown(itemStack,player,delay);
    }
    public void addCoolDown(KampfSkills kampfSkills, int delay){
        KampfCooldown.put(kampfSkills, delay);
    }
    public void addCoolDown(KampfSkills kampfSkills, int delay, ItemStack itemStack, Player player){
        KampfCooldown.put(kampfSkills, delay);
        cooldown(itemStack,player,delay);
    }
    public boolean hasCoolDown(BerufSkills berufSkills){
       return BerufCooldown.containsKey(berufSkills);
    }
    public boolean hasCoolDown(KampfSkills kampfSkills){
        return KampfCooldown.containsKey(kampfSkills);
    }
    public int getCooldown(BerufSkills berufSkills){
        return BerufCooldown.get(berufSkills);
    }
    public int getCooldown(KampfSkills kampfSkills){
        return KampfCooldown.get(kampfSkills);
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
