package poseidon.skills.Klassen;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class KlassChoose {

    private static final HashMap<String, Players> chosenChat = new HashMap<>();
    private static final HashMap<UUID, Players> UUID_PLAYERS_HASH_MAP = new HashMap<>();
    public static boolean securityCheck(Player player){
        if(chosenChat.containsKey(player.getName())){
            return true;
        }
        Players players = new Players(player.getUniqueId(), Berufklasse.Unchosed, Kampfklassen.Unchosed);
        chosenChat.putIfAbsent(player.getName(), players);
        UUID_PLAYERS_HASH_MAP.put(player.getUniqueId(),players);
        return false;
    }

    public static void resetPlayer(Player player){
        chosenChat.get(player.getName()).getManaBar().deleteBar();
        Players players = chosenChat.replace(player.getName(), new Players(player.getUniqueId(),Berufklasse.Unchosed,Kampfklassen.Unchosed));
        UUID_PLAYERS_HASH_MAP.replace(player.getUniqueId(), players);
        players.getManaBar();
        players.getBerufBar();
        players.getKampfBar();
    }

    public static void setPlayers(Players players){
        chosenChat.replace(players.getPlayer().getName(), players);
        UUID_PLAYERS_HASH_MAP.replace(players.getUUID(), players);
    }

    public static void addPlayers(Players players, Player player){
        chosenChat.put(player.getName(),players);
        UUID_PLAYERS_HASH_MAP.put(players.getUUID(),players);
    }
    public static Players getPlayers(Player player) {
        return chosenChat.get(player.getName());
    }
    public static Players getPlayer(UUID uuid){
        return UUID_PLAYERS_HASH_MAP.get(uuid);
    }

}
