package poseidon.skills.skill;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import poseidon.skills.Klassen.Berufklasse;

import java.util.ArrayList;

public class BerufSkills {
    private final ItemStack icon;
    private final String name;
    private final String command;
    private final int cooldown;
    private final Berufklasse berufklasse;
    private final int neededLevel;

    public BerufSkills(ItemStack icon, String name, String command, int cooldown, Berufklasse berufklasse, int neededLevel){
        this.icon = icon;
        this.name = name;
        this.command = command;
        this.cooldown = cooldown;
        this.berufklasse = berufklasse;
        this.neededLevel = neededLevel;
    }

    public static ArrayList<BerufSkills> getKlassSkills(Berufklasse berufklasse) {
        ArrayList<BerufSkills> kampfSkills = new ArrayList<>();
        for(BerufSkills k : SkillMapper.getBerufSkills()){
            if(k.berufklasse.equals(berufklasse)){
                kampfSkills.add(k);
            }
        }
        return kampfSkills;
    }

    public static BerufSkills getSkills(ItemStack item) {
        for(BerufSkills b : SkillMapper.getBerufSkills()){
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

    public Berufklasse getBerufklasse() {
        return berufklasse;
    }

    public int getNeededLevel() {
        return neededLevel;
    }

    public String getName() {
        return name;
    }

    public static void addBaseMain(){
        SkillMapper.addBerufSkill(new BerufSkills(ItemMaker.createGUIItems(Material.FARMLAND, "Pflege"), "Pflanzenpflege", "BB1", 60, Berufklasse.getOfArray("Bauer"), 5));
        SkillMapper.addBerufSkill(new BerufSkills(ItemMaker.createGUIItems(Material.SHIELD, "Schutz"), "PflanzenSchutz", "BK1", 40, Berufklasse.getOfArray("Bauer"), 10));
        SkillMapper.addBerufSkill(new BerufSkills(ItemMaker.createGUIItems(Material.FARMLAND, "Pflege"), "TestPflege", "BB1", 60, Berufklasse.getOfArray("Fischer"), 5));
        SkillMapper.addBerufSkill(new BerufSkills(ItemMaker.createGUIItems(Material.SHIELD, "Schutz"), "Schutzwall", "BK1", 40, Berufklasse.getOfArray("Fischer"), 10));
        SkillMapper.addBerufSkill(new BerufSkills(ItemMaker.createGUIItems(Material.FARMLAND, "Pflege"), "Pflege", "BB1", 60, Berufklasse.getOfArray("Holzfäller"), 5));
        SkillMapper.addBerufSkill(new BerufSkills(ItemMaker.createGUIItems(Material.SHIELD, "Schutz"), "Schutz", "BK1", 40, Berufklasse.getOfArray("Holzfäller"), 10));
        SkillMapper.addBerufSkill(new BerufSkills(ItemMaker.createGUIItems(Material.FARMLAND, "Pflege"), "Pflegebombe", "BB1", 60, Berufklasse.getOfArray("Minenarbeiter"), 5));
        SkillMapper.addBerufSkill(new BerufSkills(ItemMaker.createGUIItems(Material.SHIELD, "Schutz"), "Schutzbombe", "BK1", 40, Berufklasse.getOfArray("Minenarbeiter"), 10));
    }
}
