package poseidon.skills;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import poseidon.skills.Klassen.KlassChoose;
import poseidon.skills.Klassen.Players;

import java.util.HashMap;

public class ManaMap {
    private static final HashMap<Player, Integer> Mana = new HashMap<>();
    public static HashMap<Player, Integer> getMana() {
        return Mana;
    }
    private final Player player;
    public ManaMap(Player player){
        this.player = player;
        getMana().putIfAbsent(player, 0);
    }
    public int getManaValue(){
        return Mana.get(player);
    }
    public void setManaValue(int mana){
        getMana().replace(player,mana);
    }

    public boolean useMana(int Mana){
        int a = getMana().get(player);
        if(a-Mana < 0){
            return false;
        }
        setManaValue(a-Mana);
        refreshManaBar();
        return true;
    }
    public int getMaxManaValue(){
        Players players = KlassChoose.getPlayers(player);
        return players.getKampfLevel() + players.getKampfklasse().getBaseMana();
    }

    public void refreshManaBar(){
        Players players = KlassChoose.getPlayers(player);
        players.getManaBar().addPlayer(players.getPlayer(), ChatColor.GREEN + "Mana: " + getManaValue() + "/" + getMaxManaValue(), (double) getManaValue() / getMaxManaValue());
    }

    public static void startRegenerateMana(){
        Skills.getInstance().getServer().getScheduler().runTaskTimer(Skills.getInstance(), () -> getMana().forEach((name, integer) -> {
            if(getMana().isEmpty()){
                return;
            }
            Players players = KlassChoose.getPlayers(name);
            int a = players.getMaxMana();
            ManaMap map = new ManaMap(name);
            if(a > integer){
                map.setManaValue(Mana.get(name) + 1);
                map.refreshManaBar();
            }
            else if(a < integer){
                map.setManaValue(a);
            }
        }), 0, 40);
    }
}
