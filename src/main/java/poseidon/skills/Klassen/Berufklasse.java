package poseidon.skills.Klassen;

import java.util.ArrayList;

public class Berufklasse {
    public static Berufklasse Unchosed = new Berufklasse("Unchosed");
    private static final Berufklasse Bauer = new Berufklasse("Bauer");
    private static final Berufklasse Mienenarbeiter = new Berufklasse("Minenarbeiter");
    private static final Berufklasse Holzfaeller = new Berufklasse("Holzfäller");
    private static final Berufklasse Fischer = new Berufklasse("Fischer");

    private static final ArrayList<Berufklasse> test = new ArrayList<>();
    private final String displayName;

    public Berufklasse(String displayName){
        this.displayName = displayName;
    }


    public static void addToTest(Berufklasse berufklasse){
        if(!isOnArray(berufklasse.getDisplayName())){
            test.add(berufklasse);
        }
    }

    public static boolean isOnArray(String displayName){
        for(Berufklasse berufklasse : getTest()) {
            if (berufklasse.getDisplayName().equalsIgnoreCase(displayName)) {
                return true;
            }
        }
        return false;
    }

    public static Berufklasse getOfArray(String displayName) {
        if (isOnArray(displayName)) {
            for (Berufklasse berufklasse : getTest()) {
                if (berufklasse.getDisplayName().equalsIgnoreCase(displayName)) {
                    return berufklasse;
                }
            }
        }
        return Berufklasse.Unchosed;
    }

    public static ArrayList<Berufklasse> getTest() {
        return test;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static void klassStartup(){
        addToTest(Unchosed);
    }

    public static void addMainKlass(){
        addToTest(Bauer); //Feld + Tiere
        addToTest(Mienenarbeiter); //Stein/Erze
        addToTest(Holzfaeller); //Holz
        addToTest(Fischer); //Fischen
    }

}
