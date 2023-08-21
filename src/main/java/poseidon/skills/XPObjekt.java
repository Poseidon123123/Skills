package poseidon.skills;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import poseidon.skills.Klassen.Berufklasse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class XPObjekt {
    private final Berufklasse.XPSource xpSource;
    private final Material material;
    private final int xp;
    private final int money;
    private static final HashMap<Material,XPObjekt> mapper = new HashMap<>();
    @Contract(pure = true)
    public XPObjekt(Material material, Berufklasse.XPSource xpSource, int xp, int money){
        this.material = material;
        this.money = money;
        this.xpSource = xpSource;
        this.xp = xp;
    }
    public Berufklasse.XPSource getXpSource() {
        return xpSource;
    }

    public Material getMaterial() {
        return material;
    }

    public int getXp() {
        return xp;
    }

    public int getMoney() {
        return money;
    }
    @Contract(pure = true)
    public static HashMap<Material,XPObjekt> mapper(){
        return mapper;
    }

    public static void register(XPObjekt xpObjekt){
        mapper.putIfAbsent(xpObjekt.getMaterial(), xpObjekt);
    }
    public static @Nullable XPObjekt getByMaterial(Material material){
        if(!mapper().containsKey(material)){
            return null;
        }
        return mapper.get(material);
    }
    public static List<XPObjekt> regsterdObjekts(){
        List<XPObjekt> a = new ArrayList<>();
        mapper().forEach((material1, xpObjekt) -> a.add(xpObjekt));
        return a;
    }

    public static void addMainBase(){
        XPObjekt.register(new XPObjekt(Material.COD, Berufklasse.XPSource.Fishing,5,1));
        XPObjekt.register(new XPObjekt(Material.SALMON, Berufklasse.XPSource.Fishing,10,2));
        XPObjekt.register(new XPObjekt(Material.STONE, Berufklasse.XPSource.Mining,1,2));
        XPObjekt.register(new XPObjekt(Material.DEEPSLATE, Berufklasse.XPSource.Mining,1,2));
        XPObjekt.register(new XPObjekt(Material.OAK_LOG, Berufklasse.XPSource.Wooding,1,2));
        XPObjekt.register(new XPObjekt(Material.BIRCH_LOG, Berufklasse.XPSource.Wooding, 1,2));
        XPObjekt.register(new XPObjekt(Material.WHEAT, Berufklasse.XPSource.Farming,1,2));
        XPObjekt.register(new XPObjekt(Material.CARROTS, Berufklasse.XPSource.Farming,1,2));
        XPMapper.addMob(EntityType.ZOMBIE, 1);
        XPMapper.addMob(EntityType.SKELETON, 2);
        XPMapper.addMob(EntityType.AXOLOTL, 100);
    }
}
