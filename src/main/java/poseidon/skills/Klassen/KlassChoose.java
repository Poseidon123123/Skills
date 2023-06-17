package poseidon.skills.Klassen;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class KlassChoose {

    private static final HashMap<String, Players> chosenChat = new HashMap<>();

    public static boolean securityCheck(Player player){
        if(chosenChat.containsKey(player.getName())){
            return true;
        }
        chosenChat.putIfAbsent(player.getName(), new Players(player, Berufklasse.Unchosed, Kampfklassen.Unchosed));
        return false;
    }

    public static void resetPlayer(Player player){
        chosenChat.replace(player.getName(), new Players(player,Berufklasse.Unchosed,Kampfklassen.Unchosed));
    }

    public static void setPlayers(Players players){
        chosenChat.replace(players.getPlayer().getName(), players);
    }

    public static void addPlayers(Players players, Player player){
        chosenChat.put(player.getName(),players);
    }
    public static Players getPlayers(Player player) {
        return chosenChat.get(player.getName());
    }

}
