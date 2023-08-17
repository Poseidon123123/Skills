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

    public static boolean addBerufSkill(BerufSkills klassen){
        if(!isOnBerufArray(klassen)) {
            if(Berufklasse.isOnArray(klassen.getBerufklasse().getDisplayName())) {
                return berufSkills.add(klassen);
            }
            else {
                System.out.println("Der Skill " + Objects.requireNonNull(klassen.getIcon().getItemMeta()).getDisplayName() + " konnte keiner Klasse zugeordnet werden!");
            }
        }
        return false;
    }
    public static boolean removeSkills(BerufSkills berufSkills){
        if(isOnBerufArray(berufSkills)){
            return getBerufSkills().remove(berufSkills);
        }
        return false;
    }
    public static boolean removeSkills(KampfSkills kampfSkills){
        if(isOnKampfArray(kampfSkills)){
            return getKampfSkills().remove(kampfSkills);
        }
        return false;
    }

    public static boolean isOnBerufArray(BerufSkills berufSkills){
        for(BerufSkills berufskill : getBerufSkills()) {
            if(berufskill.getBerufklasse().equals(berufSkills.getBerufklasse()) && berufskill.getCommand().equals(berufSkills.getCommand()) && berufskill.getIcon().equals(berufSkills.getIcon())) {
                return true;
            }
        }
        return false;
    }
    public static boolean isOnBerufArray(String name){
        for(BerufSkills berufskill : getBerufSkills()) {
            if(berufskill.getName().equalsIgnoreCase(name)) {
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
    public static BerufSkills getOfBerufArray(String name) {
        for (BerufSkills berufskill: getBerufSkills()) {
            if(berufskill.getName().equalsIgnoreCase(name)) {
                return berufskill;
            }
        }
        return null;
    }

    public static ArrayList<KampfSkills> getKampfSkills() {
        return kampfSkills;
    }

    public static boolean addKampfSkill(KampfSkills klassen){
        if(!isOnKampfArray(klassen)) {
            if(Kampfklassen.isOnArray(klassen.getKampfKlasse().getDisplayName())) {
                return kampfSkills.add(klassen);
            }
            else {
                System.out.println("Der Skill " + klassen.getName() + " konnte keiner Klasse zugeordnet werden!");
            }
        }
        return false;
    }

    public static boolean isOnKampfArray(KampfSkills berufSkills){
        for(KampfSkills berufskill : getKampfSkills()) {
            if(berufskill.getKampfKlasse().equals(berufSkills.getKampfKlasse()) && berufskill.getCommand().equals(berufSkills.getCommand()) && berufskill.getIcon().equals(berufSkills.getIcon()) && berufskill.getName().equals(berufSkills.getName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isOnKampfArray(String name){
        for(KampfSkills berufskill : getKampfSkills()) {
            if(berufskill.getName().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }

    public static KampfSkills getOfKampfArray(String name) {
        for (KampfSkills berufskill: getKampfSkills()) {
            if(berufskill.getName().equalsIgnoreCase(name)) {
                return berufskill;
            }
        }
        return null;
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

