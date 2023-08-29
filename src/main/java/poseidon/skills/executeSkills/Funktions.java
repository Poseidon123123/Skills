package poseidon.skills.executeSkills;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import poseidon.skills.Skills;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
@SuppressWarnings({"might be null"})
public class Funktions {
    private static final HashMap<String,Type> types = new HashMap<>();
    public static void registerType(Type type){
        types.put(type.getName(), type);
    }
    public static HashMap<String,Type> getTypes(){
        return types;
    }
    public static void runByName(String string, Player player){
        Type type = types.get(string);
        if(type != null && type.getType() != null) {
            if (type.getType().contains(Type.types.projectile)) {
                EntityType entityType = type.getEntityType();
                int vector = type.getProjectilePara();
                Vector a = player.getEyeLocation().getDirection();
                a.multiply(vector);
                Entity fireball = player.getWorld().spawnEntity(player.getEyeLocation().add(a), entityType);
                fireball.setCustomName("SFB");
                Skills.getInstance().getServer().getScheduler().runTaskLater(Skills.getInstance(), fireball::remove, 40);
            }
            if (type.getType().contains(Type.types.self)) {
                PotionEffectType effect = type.getSelfEffect();
                int a = type.getSelfDur();
                int b = type.getSelfAmpli();
                player.addPotionEffect(new PotionEffect(effect, a, b));
            }
            if (type.getType().contains(Type.types.leap)) {
                Vector a = player.getEyeLocation().getDirection();
                int b = type.getLeapPara();
                a.multiply(b);
                player.setVelocity(a);
            }
            if (type.getType().contains(Type.types.teleport)) {
                Location loc = player.getLocation();
                Vector dir = loc.getDirection();
                dir.normalize();
                dir.multiply(type.getTpPara());
                loc.add(dir);
                player.teleport(loc);
            }
            if (type.getType().contains(Type.types.nearest)) {
                PotionEffectType effectType = type.getNearestEffect();
                int a = type.getNearDur();
                int b = type.getNearAmpli();
                int limit = type.getNearMax();
                int radius = type.getNearRadius();
                for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
                    if (limit - 1 > 0) {
                        if (entity instanceof LivingEntity player1) {
                            if (player1.equals(player)) {
                                continue;
                            }
                            player1.addPotionEffect(new PotionEffect(effectType, a, b));
                            limit--;
                        }
                    }
                }
            }
            if (type.getType().contains(Type.types.remember)) {
                listPlayerRemember(type, player);
            }
        }
    }
    private static final HashMap<UUID, Type> playerRemember = new HashMap<>();
    public static void listPlayerRemember(Type type, Player player){
        if(type.getRememberAction() != null){
            playerRemember.put(player.getUniqueId(), type);
            Bukkit.getScheduler().runTaskLater(Skills.getInstance(), () -> playerRemember.remove(player.getUniqueId()), type.getRememberDuration());
        }
        else {
            throw new IllegalArgumentException();
        }
    }
    public static boolean hasRemember(Player player){
        return playerRemember.containsKey(player.getUniqueId());
    }
    public static List<Material> getRemember(Player player){
        return playerRemember.get(player.getUniqueId()).getRememberAction();
    }
    public static void remmoveRemember(Player player){
        playerRemember.remove(player.getUniqueId());
    }
    public static void generateExample(){
        Type b1 = new Type("B1");
        b1.setParameters(EntityType.ARROW, 20);
        registerType(b1);
        Type b2 = new Type("B2");
        b2.setParameters(5, true);
        registerType(b2);
        Type bk1 = new Type("BK1");
        bk1.setParameters(PotionEffectType.DAMAGE_RESISTANCE, 100, 200);
        registerType(bk1);
        Type bb1 = new Type("BB1");
        bb1.setParameters(List.of(Material.OAK_LOG, Material.OAK_WOOD, Material.BIRCH_LOG), 2000);
        registerType(bb1);
    }
}
