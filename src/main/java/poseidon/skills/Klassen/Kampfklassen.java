package poseidon.skills.Klassen;

import java.util.ArrayList;

public class Kampfklassen {
    public static Kampfklassen Unchosed = new Kampfklassen("Unchosed", 0);
    private final static Kampfklassen Bauer = new Kampfklassen("Soldat", 20);
    private static final Kampfklassen Mienenarbeiter = new Kampfklassen("Messerwerfer", 20);
    private static final Kampfklassen Holzfaeller = new Kampfklassen("Techniker", 20);
    private static final Kampfklassen Fischer = new Kampfklassen("Heiler", 20);
    private static final ArrayList<Kampfklassen> klasse = new ArrayList<>();
    private final String displayName;
    private final int baseMana;

    public Kampfklassen(String displayName, int baseMana){
        this.displayName = displayName;
        this.baseMana = baseMana;
    }

    public static ArrayList<Kampfklassen> getKlassen() {
        return klasse;
    }

    public static boolean addKlasse(Kampfklassen klassen){
        if(!isOnArray(klassen.getDisplayName())) {
            return klasse.add(klassen);
        }
        return false;
    }
    public static boolean isOnArray(String displayName){
        for(Kampfklassen berufklasse : getKlassen()) {
            if (berufklasse.getDisplayName().equalsIgnoreCase(displayName)) {
                return true;
            }
        }
        return false;
    }

    public static Kampfklassen getOfArray(String displayName) {
        if (isOnArray(displayName)) {
            for (Kampfklassen berufklasse: getKlassen()) {
                if (berufklasse.getDisplayName().equalsIgnoreCase(displayName)) {
                    return berufklasse;
                }
            }
        }
        return Kampfklassen.Unchosed;
    }

    public String getDisplayName() {
        return displayName;
    }


    public static void klassStartup(){
        addKlasse(Unchosed);
    }

    public static void addMainKlass(){
        addKlasse(Bauer); //Feld + Tiere
        addKlasse(Mienenarbeiter); //Stein/Erze
        addKlasse(Holzfaeller); //Holz
        addKlasse(Fischer); //Fischen
    }


    public int getBaseMana() {
        return baseMana;
    }
}
