package poseidon.skills.skill;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import poseidon.skills.Klassen.Kampfklassen;

import java.util.ArrayList;

public class KampfSkills {
        private final ItemStack icon;
        private final String name;
        private final String command;
        private final int cooldown;
        private final Kampfklassen berufklasse;
        private final int neededLevel;
        private final int consumedMana;


        public KampfSkills(ItemStack icon, String name, String command, int cooldown, Kampfklassen kampfklasse, int neededLevel, int consumedMana){
            this.icon = icon;
            this.name = name;
            this.command = command;
            this.cooldown = cooldown;
            this.berufklasse = kampfklasse;
            this.neededLevel = neededLevel;
            this.consumedMana = consumedMana;
        }

    public static ArrayList<KampfSkills> getKlassSkills(Kampfklassen berufklasse) {
            ArrayList<KampfSkills> kampfSkills = new ArrayList<>();
            for(KampfSkills k : SkillMapper.getKampfSkills()){
                if(k.berufklasse.equals(berufklasse)){
                    kampfSkills.add(k);
                }
            }
            return kampfSkills;
    }

    public static KampfSkills getSkills(ItemStack item) {
            for(KampfSkills b : SkillMapper.getKampfSkills()){
                if(b.getIcon().equals(item)){
                    return b;
                }
            }
            return null;
    }

    public ItemStack getIcon() {
            return icon;
        }

    public String getCommand() {
        return command;
    }

    public int getCooldown() {
        return cooldown;
    }

    public Kampfklassen getKampfKlasse() {
        return berufklasse;
    }

    public int getNeededLevel() {
        return neededLevel;
    }

    public String getName() {
        return name;
    }

    public int getConsumedMana() {
        return consumedMana;
    }

        public static void addBaseSkills(){
            SkillMapper.addKampfSkill(new KampfSkills(ItemMaker.createGUIItems(Material.FIRE_CHARGE, "Feuerstrahl"), "Feuerstrahl","B2",10, Kampfklassen.getOfArray("Techniker"), 10, 5));
            SkillMapper.addKampfSkill(new KampfSkills(ItemMaker.createGUIItems(Material.STONE, "Kampfgeschrei"), "Kampfgeschrei","B1",5, Kampfklassen.getOfArray("Techniker"), 5, 10));
            SkillMapper.addKampfSkill(new KampfSkills(ItemMaker.createGUIItems(Material.FIRE_CHARGE, "Feuer"), "Feuer","B2",5, Kampfklassen.getOfArray("Heiler"), 10, 10));
            SkillMapper.addKampfSkill(new KampfSkills(ItemMaker.createGUIItems(Material.STONE, "Kampf"), "Kampfgi","B1",5, Kampfklassen.getOfArray("Heiler"), 5,5));
            SkillMapper.addKampfSkill(new KampfSkills(ItemMaker.createGUIItems(Material.FIRE_CHARGE, "Feuerball"), "Feuerball","B2",10, Kampfklassen.getOfArray("Messerwerfer"), 10,10));
            SkillMapper.addKampfSkill(new KampfSkills(ItemMaker.createGUIItems(Material.STONE, "Kampfgeschreierei"), "Kampfgeschreierei","B1",5, Kampfklassen.getOfArray("Messerwerfer"), 5,5));
            SkillMapper.addKampfSkill(new KampfSkills(ItemMaker.createGUIItems(Material.FIRE_CHARGE, "Feuerstrahler"), "Feuerstrahler","B2",10, Kampfklassen.getOfArray("Soldat"), 10,10));
            SkillMapper.addKampfSkill(new KampfSkills(ItemMaker.createGUIItems(Material.STONE, "Kampfgeschreiler"), "Kampfgeschreiler","B1",5, Kampfklassen.getOfArray("Soldat"), 5,5));
        }
}
