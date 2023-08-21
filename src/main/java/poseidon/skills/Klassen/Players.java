package poseidon.skills.Klassen;

import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import poseidon.skills.Bar;
import poseidon.skills.Skills;
import poseidon.skills.citys.City;
import poseidon.skills.skill.BerufSkills;
import poseidon.skills.skill.KampfSkills;

import java.time.LocalDateTime;

import static poseidon.skills.Chat.ChatAPI.Chats;

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
    private ItemStack BerufItemSkill;
    private ItemStack KampfItemSkill;
    private BerufSkills boundBeruf;
    private KampfSkills boundKampf;
    private LocalDateTime berufChange;
    private LocalDateTime kampfChange;
    private Chats chat = Chats.GlobalChat;

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
        this.boundBeruf = null;
        this.boundKampf = null;
        this.BerufItemSkill = null;
        this.KampfItemSkill = null;
    }

    public Players(Player player, Berufklasse berufklasse, Kampfklassen kampfklasse, int berufLevel, int berufXP, int kampfLevel,
                   int kampfXP, int money, City hometown, ItemStack berufSkillItem, BerufSkills berufSkills,
                   ItemStack kampfItemSkill, KampfSkills kampfSkills, LocalDateTime berufChange, LocalDateTime kampfChange, Chats chat){
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
        this.KampfItemSkill = kampfItemSkill;
        this.BerufItemSkill = berufSkillItem;
        this.boundKampf = kampfSkills;
        this.boundBeruf = berufSkills;
        this.berufChange = berufChange;
        this.kampfChange = kampfChange;
        this.chat = chat;
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

    @Nullable
    public ItemStack getBerufItemSkill() {
        return BerufItemSkill;
    }
    @Nullable
    public ItemStack getKampfItemSkill() {
        return KampfItemSkill;
    }
    @Nullable
    public BerufSkills getBoundBeruf() {
        return boundBeruf;
    }
    @Nullable
    public KampfSkills getBoundKampf() {
        return boundKampf;
    }
    public void setBoundKampf(KampfSkills boundKampf) {
        this.boundKampf = boundKampf;
    }
    public void setBerufItemSkill(ItemStack berufItemSkill) {
        BerufItemSkill = berufItemSkill;
    }
    public void setKampfItemSkill(ItemStack kampfItemSkill) {
        KampfItemSkill = kampfItemSkill;
    }
    public void setBoundBeruf(BerufSkills boundBeruf) {
        this.boundBeruf = boundBeruf;
    }
    public boolean berufChangable(){
        if(berufChange == null){
            return true;
        }
        return berufChange.compareTo(LocalDateTime.now()) > Skills.getInstance().value("Values.Cooldown.BerufChange");
    }
    public boolean kampfChangable(){
        if(kampfChange == null){
            return true;
        }
        return kampfChange.compareTo(LocalDateTime.now()) > Skills.getInstance().value("Values.Cooldown.KampfChange");
    }
    public void berufChanged(){
        berufChange = LocalDateTime.now();
    }
    public void kampfChanged(){
        kampfChange = LocalDateTime.now();
    }
    public LocalDateTime getBerufChange(){
        return berufChange;
    }
    public LocalDateTime getKampfChange(){
        return kampfChange;
    }

    public Chats getChat() {
        return chat;
    }

    public void setChat(Chats chat) {
        this.chat = chat;
    }
}
