package poseidon.skills.Klassen;

import java.util.ArrayList;

public class Kampfklassen {
    public static Kampfklassen Unchosed = new Kampfklassen("Unchosed");
    private final static Kampfklassen Bauer = new Kampfklassen("Soldat");
    private static final Kampfklassen Mienenarbeiter = new Kampfklassen("Messerwerfer");
    private static final Kampfklassen Holzfaeller = new Kampfklassen("Techniker");
    private static final Kampfklassen Fischer = new Kampfklassen("Heiler");
    private static final ArrayList<Kampfklassen> klasse = new ArrayList<>();
    private final String displayName;

    public Kampfklassen(String displayName){
        this.displayName = displayName;
    }

    public static ArrayList<Kampfklassen> getKlassen() {
        return klasse;
    }

    public static void addKlasse(Kampfklassen klassen){
        if(!isOnArray(klassen.getDisplayName())) {
            klasse.add(klassen);
        }
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


}
