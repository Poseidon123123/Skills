package poseidon.skills.skill;

import poseidon.skills.Klassen.Berufklasse;
import poseidon.skills.Klassen.Kampfklassen;

import java.util.ArrayList;
import java.util.Objects;

public class SkillMapper {
    private static final ArrayList<BerufSkills> berufSkills = new ArrayList<>();
    private static final ArrayList<KampfSkills> kampfSkills = new ArrayList<>();

    public static ArrayList<BerufSkills> getBerufSkills() {
        return berufSkills;
    }

    public static void addBerufSkill(BerufSkills klassen){
        if(!isOnBerufArray(klassen)) {
            if(Berufklasse.isOnArray(klassen.getBerufklasse().getDisplayName())) {
                berufSkills.add(klassen);
            }
            else {
                System.out.println("Der Skill " + Objects.requireNonNull(klassen.getIcon().getItemMeta()).getDisplayName() + " konnte keiner Klasse zugeordnet werden!");
            }
        }
    }

    public static boolean isOnBerufArray(BerufSkills berufSkills){
        for(BerufSkills berufskill : getBerufSkills()) {
            if(berufskill.getBerufklasse().equals(berufSkills.getBerufklasse()) && berufskill.getCommand().equals(berufSkills.getCommand()) && berufskill.getIcon().equals(berufSkills.getIcon())) {
                return true;
            }
        }
        return false;
    }

    public static BerufSkills getOfBerufArray(BerufSkills berufSkills) {
        if (isOnBerufArray(berufSkills)) {
            for (BerufSkills berufskill: getBerufSkills()) {
                if(berufskill.getBerufklasse().equals(berufSkills.getBerufklasse()) && berufskill.getCommand().equals(berufSkills.getCommand()) && berufskill.getIcon().equals(berufSkills.getIcon())) {
                    return berufskill;
                }
            }
        }
        return null;
    }

    public static ArrayList<KampfSkills> getKampfSkills() {
        return kampfSkills;
    }

    public static void addKampfSkill(KampfSkills klassen){
        if(!isOnKampfArray(klassen)) {
            if(Kampfklassen.isOnArray(klassen.getKampfKlasse().getDisplayName())) {
                kampfSkills.add(klassen);
            }
            else {
                System.out.println("Der Skill " + klassen.getName() + " konnte keiner Klasse zugeordnet werden!");
            }
        }
    }

    public static boolean isOnKampfArray(KampfSkills berufSkills){
        for(KampfSkills berufskill : getKampfSkills()) {
            if(berufskill.getKampfKlasse().equals(berufSkills.getKampfKlasse()) && berufskill.getCommand().equals(berufSkills.getCommand()) && berufskill.getIcon().equals(berufSkills.getIcon()) && berufskill.getName().equals(berufSkills.getName())) {
                return true;
            }
        }
        return false;
    }

    public static KampfSkills getOfKampfArray(KampfSkills berufSkills) {
        if (isOnKampfArray(berufSkills)) {
            for (KampfSkills berufskill: getKampfSkills()) {
                if(berufskill.getKampfKlasse().equals(berufSkills.getKampfKlasse()) && berufskill.getCommand().equals(berufSkills.getCommand()) && berufskill.getIcon().equals(berufSkills.getIcon()) && berufskill.getName().equals(berufSkills.getName())) {
                    return berufskill;
                }
            }
        }
        return null;
    }
}

