package poseidon.skills.Klassen;

import org.bukkit.entity.Player;

public class Players {

    private final Player player;
    private int berufLevel;
    private int berufXP;
    private Berufklasse berufklasse;
    private Kampfklassen kampfklasse;
    private int kampfLevel;
    private int kampfXP;

    public Players(Player player, Berufklasse berufklasse, Kampfklassen kampfklasse){
        this.player = player;
        this.berufklasse = berufklasse;
        this.kampfklasse = kampfklasse;
        this.berufXP = 0;
        this.berufLevel = 1;
        this.kampfXP = 0;
        this.kampfLevel = 0;
    }

    public Players(Player player, Berufklasse berufklasse, Kampfklassen kampfklasse, int berufLevel, int berufXP, int kampfLevel, int kampfXP){
        this.player = player;
        this.berufklasse = berufklasse;
        this.kampfklasse = kampfklasse;
        if(berufLevel <= 0){
            this.berufLevel = 1;
        }
        else this.berufLevel = Math.min(berufLevel, 100);
        this.berufXP = Math.max(berufXP, 0);
        if(kampfLevel <= 0){
            this.kampfLevel = 1;
        }
        else this.kampfLevel = Math.min(kampfLevel, 100);
        this.kampfXP = Math.max(kampfXP, 0);

    }

    public Player getPlayer() {
        return player;
    }

    public int getBerufLevel() {
        return berufLevel;
    }

    public void setBerufLevel(int berufLevel) {
        this.berufLevel = berufLevel;
    }

    public int getBerufXP() {
        return berufXP;
    }

    public void setBerufXP(int berufXP) {
        this.berufXP = berufXP;
    }

    public Berufklasse getBerufklasse() {
        return berufklasse;
    }

    public void setBerufklasse(Berufklasse berufklasse) {
        this.berufklasse = berufklasse;
    }

    public Kampfklassen getKampfklasse() {
        return kampfklasse;
    }

    public void setKampfklasse(Kampfklassen kampfklasse) {
        this.kampfklasse = kampfklasse;
    }

    public int getKampfLevel() {
        return kampfLevel;
    }

    public void setKampfLevel(int kampfLevel) {
        this.kampfLevel = kampfLevel;
    }

    public int getKampfXP() {
        return kampfXP;
    }

    public void setKampfXP(int kampfXP) {
        this.kampfXP = kampfXP;
    }
}
