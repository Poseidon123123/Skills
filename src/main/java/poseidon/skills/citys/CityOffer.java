package poseidon.skills.citys;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CityOffer {
    public static HashMap<UUID, List<City>> offedCitys = new HashMap<>();
    private final Player player;
    private final UUID playerUUID;
    public CityOffer(Player player){
        this.player = player;
        this.playerUUID = player.getUniqueId();
    }
    public void addOffer(City city){
        if(offedCitys.containsKey(this.playerUUID)){
            List<City> a = offedCitys.get(this.playerUUID);
            a.add(city);
            offedCitys.replace(this.playerUUID, a);
        }
        else{
            List<City> b = List.of(city);
            offedCitys.put(this.playerUUID, b);
        }
        Objects.requireNonNull(Bukkit.getPlayer(this.playerUUID)).sendMessage(ChatColor.GREEN + "Dir wurde eine Einladung zur Stadt " + city.getCityName() + " gib /city accept " + city.getCityName() + " ein um die Einladung anzunehmen");

    }
    public boolean hasOffer(){
        return offedCitys.containsKey(this.playerUUID);
    }
    public List<City> getOffers(){
        return offedCitys.get(this.playerUUID);
    }
    public void offersDone(){
        offedCitys.remove(this.playerUUID);
    }

    public Player getPlayer() {
        return player;
    }
}
