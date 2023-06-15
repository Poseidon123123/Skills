package poseidon.skills;

import org.bukkit.entity.EntityType;

import java.util.HashMap;

public class XPMapper {
    private static HashMap<EntityType, Integer> XP = new HashMap<>();
    public static void addMob(EntityType mob, int x){
        XP.putIfAbsent(mob,x);
    }
    public static void removeMob(EntityType mob){
        XP.remove(mob);
    }
    public static int getXP(EntityType mob){
        return XP.get(mob);
    }
    public static HashMap<EntityType, Integer> getXP(){
        return XP;
    }
}
