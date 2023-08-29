package poseidon.skills.executeSkills;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Type {
    public Type(String name){
        this.name = name;
    }
    private final String name;
    private final List<types> type = new ArrayList<>();
    private EntityType entityType;
    private int projectilePara;
    private int leapPara;
    private int tpPara;
    private int selfDur;
    private int selfAmpli;
    private int nearDur;
    private int nearAmpli;
    private int nearMax;
    private int nearRadius;
    private int rememberDuration;
    private PotionEffectType selfEffect;
    private PotionEffectType nearestEffect;
    private List<Material> rememberAction;
    private final List<EntityType> projektileList = List.of(EntityType.ARROW, EntityType.FIREBALL, EntityType.SMALL_FIREBALL, EntityType.LLAMA_SPIT, EntityType.DRAGON_FIREBALL, EntityType.SHULKER_BULLET, EntityType.SPECTRAL_ARROW);

    // Konstruktor für PROJECTILE
    public void setParameters(EntityType entityType, int projectilePara) {
        if(projektileList.contains(entityType)) {
            this.type.add(types.projectile);
            this.entityType = entityType;
            this.projectilePara = projectilePara;
        }
        else {
            throw new IllegalArgumentException();
        }
    }
    public void setParameters(int intParameter1, boolean tp){
        if(tp) {
            this.tpPara = intParameter1;
            this.type.add(types.teleport);
        }
        else {
            this.leapPara = intParameter1;
            this.type.add(types.leap);
        }
    }

    // Konstruktor für SELF
    public void setParameters(PotionEffectType selfEffect, int selfDur, int selfAmpli) {
        this.type.add(types.self);
        this.selfEffect = selfEffect;
        this.selfDur = selfDur;
        this.selfAmpli = selfAmpli;
    }

    // Konstruktor für NEAREST
    public void setParameters(PotionEffectType nearestEffect, int nearDur, int nearAmpli, int nearMax, int nearRadius) {
        this.type.add(types.nearest);
        this.nearestEffect = nearestEffect;
        this.nearDur = nearDur;
        this.nearAmpli = nearAmpli;
        this.nearMax = nearMax;
        this.nearRadius = nearRadius;
    }

    // Konstruktor für REMEMBER
    public void setParameters(List<Material> rememberAction, int rememberDuration) {
        this.type.add(types.remember);
        this.rememberAction = rememberAction;
        this.rememberDuration = rememberDuration;
    }
    public EntityType getEntityType() {
        return entityType;
    }
    public PotionEffectType getSelfEffect() {
        return selfEffect;
    }
    public PotionEffectType getNearestEffect() {
        return nearestEffect;
    }
    public List<Material> getRememberAction() {
        return rememberAction;
    }
    public String getName() {
        return name;
    }
    public List<types> getType() {
        return type;
    }
    public int getProjectilePara() {
        return projectilePara;
    }
    public int getLeapPara() {
        return leapPara;
    }
    public int getTpPara() {
        return tpPara;
    }
    public int getSelfDur() {
        return selfDur;
    }
    public int getSelfAmpli() {
        return selfAmpli;
    }
    public int getNearDur() {
        return nearDur;
    }
    public int getNearAmpli() {
        return nearAmpli;
    }
    public int getNearMax() {
        return nearMax;
    }
    public int getNearRadius() {
        return nearRadius;
    }
    public int getRememberDuration() {
        return rememberDuration;
    }
    public enum types {
        projectile,
        self,
        leap,
        teleport,
        nearest,
        remember
    }
}


