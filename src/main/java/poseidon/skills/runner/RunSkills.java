package poseidon.skills.runner;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import poseidon.skills.Skills;

public class RunSkills {
    public static void decode(Player player, String string){
        RunSkills runSkills = new RunSkills(player);
        switch (string) {
            case "B1" -> runSkills.B1();
            case "B2" -> runSkills.B2();
            case "BB1" -> runSkills.BB1();
            case "BK1" -> runSkills.BK1();
        }
    }
    private final Player player;
    public RunSkills(Player player){
        this.player = player;
    }
    public void B1(){
        Vector a = player.getEyeLocation().getDirection();
        a.multiply(2.5);
        player.setVelocity(a);
    }
    public void B2(){
        Vector a = player.getEyeLocation().getDirection();
        a.multiply(1.5);
        SmallFireball fireball = (SmallFireball) player.getWorld().spawnEntity(player.getEyeLocation().add(a), EntityType.SMALL_FIREBALL);
        fireball.setCustomName("SFB");
        Skills.getInstance().getServer().getScheduler().runTaskLater(Skills.getInstance(), fireball::remove, 40);
    }
    public void BB1(){
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 400, 2));
    }
    public void BK1(){
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 1));
    }

}
