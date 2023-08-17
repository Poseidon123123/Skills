package poseidon.skills.Klassen;

import java.util.ArrayList;
import java.util.HashMap;

public class Berufklasse {
    public static Berufklasse Unchosed = new Berufklasse("Unchosed", XPSource.None );
    private static final Berufklasse Bauer = new Berufklasse("Bauer", XPSource.Farming);
    private static final Berufklasse Mienenarbeiter = new Berufklasse("Minenarbeiter", XPSource.Mining);
    private static final Berufklasse Holzfaeller = new Berufklasse("Holzfäller", XPSource.Wooding);
    private static final Berufklasse Fischer = new Berufklasse("Fischer", XPSource.Fishing );

    private static final ArrayList<Berufklasse> test = new ArrayList<>();
    private static final HashMap<Berufklasse, Berufklasse> advanced = new HashMap<>();
    private String displayName;
    private XPSource source;

    public Berufklasse(String displayName, XPSource xpSource){
        this.displayName = displayName;
        this.source = xpSource;
    }
    //AdvancedBerufklasse
    public Berufklasse(String displayName, XPSource xpSource, Berufklasse base){
        Berufklasse berufklasse = new Berufklasse(displayName,xpSource);
        advanced.put(berufklasse, base);
    }

    public static boolean isAdvanced(Berufklasse berufklasse){
        return advanced.containsKey(berufklasse);
    }

    public static Berufklasse getBase(Berufklasse berufklasse){
        if(isAdvanced(berufklasse)){
            return advanced.get(berufklasse);
        }
        else {
            return berufklasse;
        }
    }


    public static boolean addToTest(Berufklasse berufklasse){
        if(!isOnArray(berufklasse.getDisplayName())){
            return test.add(berufklasse);
        }
        return false;
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

    public XPSource getSource() {
        return source;
    }

    public void setSource(XPSource source) {
        this.source = source;
    }

    public enum XPSource {
        None,
        Mining,
        Wooding,
        Farming,
        Fishing
    }

    public static XPSource getSourceByName(String name){
        return XPSource.valueOf(name);
    }

}
