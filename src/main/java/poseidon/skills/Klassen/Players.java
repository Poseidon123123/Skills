package poseidon.skills.Klassen;

import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;
import poseidon.skills.Bar;
import poseidon.skills.Skills;
import poseidon.skills.citys.City;

public class Players {

    private final Player player;
    private int berufLevel;
    private int berufXP;
    private Berufklasse berufklasse;
    private Kampfklassen kampfklasse;
    private int kampfLevel;
    private int kampfXP;
    private Bar berufBar;
    private Bar kampfBar;
    private Bar manaBar;
    private int money;
    private City hometown;

    public Players(Player player, Berufklasse berufklasse, Kampfklassen kampfklasse){
        this.player = player;
        this.berufklasse = berufklasse;
        this.kampfklasse = kampfklasse;
        this.money = 0;
        this.berufXP = 0;
        this.berufLevel = 1;
        this.kampfXP = 0;
        this.kampfLevel = 1;
        this.berufBar = null;
        this.kampfBar = null;
        this.manaBar = null;
    }

    public Players(Player player, Berufklasse berufklasse, Kampfklassen kampfklasse, int berufLevel, int berufXP, int kampfLevel, int kampfXP, int money, City hometown){
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
        this.berufBar = null;
        this.kampfBar = null;
        this.manaBar = null;
        this.money = money;
        this.hometown = hometown;
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
        this.berufLevel = 1;
        this.berufXP = 0;
        this.berufklasse = berufklasse;
    }

    public Kampfklassen getKampfklasse() {
        return kampfklasse;
    }

    public void setKampfklasse(Kampfklassen kampfklasse) {
        this.kampfLevel = 1;
        this.kampfXP = 0;
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

    public Bar getKampfBar() {
        if(kampfBar == null){
            Bar bar = new Bar(Skills.getInstance());
            bar.createBar("Kampfbar", BarColor.RED);
            kampfBar = bar;
            return bar;
        }
        return kampfBar;
    }

    public Bar getBerufBar() {
        if(berufBar == null){
            Bar bar = new Bar(Skills.getInstance());
            bar.createBar("Berufbar", BarColor.BLUE);
            this.berufBar = bar;
            return bar;
        }
        return berufBar;
    }
    public Bar getManaBar() {
        if(manaBar == null){
            Bar bar = new Bar(Skills.getInstance());
            bar.createBar("ManaBar", BarColor.GREEN);
            this.manaBar = bar;
            return bar;
        }
        return manaBar;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
    public void addMoney(int money){
        setMoney(getMoney() + money);
        player.sendMessage(ChatColor.GREEN + "+"+ money + "$");
    }
    public boolean removeMoney(int money){
        int x = getMoney() - money;
        if(x <= 0){
            return false;
        }
        setMoney(x);
        player.sendMessage(ChatColor.DARK_RED + "-" + money + "§");
        return true;
    }

    public City getHometown() {
        return hometown;
    }

    public int getMaxMana(){
        return getKampfklasse().getBaseMana() + getKampfLevel();
    }

    public void setHometown(City hometown) {
        this.hometown = hometown;
    }
}
