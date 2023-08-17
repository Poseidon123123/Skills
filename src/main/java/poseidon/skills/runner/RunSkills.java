package poseidon.skills.runner;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import poseidon.skills.Skills;

public class RunSkills {
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
