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
    private static HashMap<UUID, CooldownSystem> cooldownSystemHashMap = new HashMap<>();
    public static CooldownSystem registerPlayer(Player player){
        if(!cooldownSystemHashMap.containsKey(player.getUniqueId())) {
            return cooldownSystemHashMap.putIfAbsent(player.getUniqueId(), new CooldownSystem(player));
        }
        return null;
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
                    obj.stopCooldowns();
                    obj.startCooldowns();
                }
            }
            for (UUID uuidToRemove : uuidsToRemove) {
                cooldownSystemHashMap.remove(uuidToRemove);
            }
        }, 200, delay);
    }
    public void startCooldowns(){
        BerufID = Bukkit.getScheduler().runTaskTimer(Skills.getInstance(), () -> {
            BerufCooldown.forEach((berufSkills, integer) -> {
                if(integer - 1 > 0 ) {
                    BerufCooldown.replace(berufSkills, integer, integer - 1);
                }
                else {
                    BerufCooldown.remove(berufSkills);
                }
            } );
        }, 0,20).getTaskId();
        KampfID = Bukkit.getScheduler().runTaskTimer(Skills.getInstance(), () -> {
            KampfCooldown.forEach((berufSkills, integer) -> {
                if(integer - 1 > 0) {
                    KampfCooldown.replace(berufSkills, integer, integer - 1);
                }
                else {
                    KampfCooldown.remove(berufSkills);
                }
            } );
        }, 0,20).getTaskId();
    }
    public void stopCooldowns(){
        Bukkit.getScheduler().cancelTask(BerufID);
        Bukkit.getScheduler().cancelTask(KampfID);
    }
    private int BerufID;
    private int KampfID;
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
