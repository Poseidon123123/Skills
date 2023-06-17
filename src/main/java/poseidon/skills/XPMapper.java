package poseidon.skills;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import poseidon.skills.Klassen.Players;

import java.util.ArrayList;
import java.util.HashMap;

public class XPMapper {
    private static final HashMap<EntityType, Integer> mobKillXP = new HashMap<>();
    public static void addMob(EntityType mob, int x){
        mobKillXP.putIfAbsent(mob,x);
    }
    public static void removeMob(EntityType mob){
        mobKillXP.remove(mob);
    }
    public static int getMobXP(EntityType mob){
        return mobKillXP.get(mob);
    }
    public static HashMap<EntityType, Integer> getMobKillXP(){
        return mobKillXP;
    }

    public static void xpAdd(Players p, int xp, boolean beruf){
        if(beruf) {
            int i1 = p.getBerufLevel();
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
            int i1 = p.getKampfLevel();
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

    private static final HashMap<Material, Integer> fishXP = new HashMap<>();
    public static void addFish(Material fish, int x){
        fishXP.putIfAbsent(fish, x);
    }
    public static int getFishXP(Material fish){
        return fishXP.getOrDefault(fish, 0);
    }
    public static HashMap<Material, Integer> getFishXP(){
        return fishXP;
    }

    private static final HashMap<Material, Integer> mineXP = new HashMap<>();
    public static void addMine(Material mine, int x){
        mineXP.put(mine, x);
    }
    public static int getMineXP(Material mine){
        return mineXP.get(mine);
    }
    public static HashMap<Material,Integer> getMineXP(){
        return mineXP;
    }

    private static final HashMap<Material, Integer> woodXP = new HashMap<>();
    private static final HashMap<Material, Integer> farmXP = new HashMap<>();
    public static void addFarm(Material mine, int x){
        farmXP.put(mine, x);
    }
    public static int getFarmXP(Material mine){
        return farmXP.get(mine);
    }
    public static HashMap<Material,Integer> getFarmXP(){
        return farmXP;
    }
    public static void addWood(Material mine, int x){
        woodXP.put(mine, x);
    }
    public static int getWoodXP(Material mine){
        return woodXP.get(mine);
    }
    public static HashMap<Material,Integer> getWoodXP(){
        return woodXP;
    }
}
