package poseidon.skills.citys;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class City {
    private String cityName;
    private int cityMoney;
    private UUID buergermeister;
    private List<UUID> buerger;
    private List<Chunk> claimedChunks;

    public City(String cityName, Player player){
        this.cityName = cityName;
        this.cityMoney = 0;
        this.buergermeister = player.getUniqueId();
        this.buerger = List.of(buergermeister);
        this.claimedChunks = List.of(player.getLocation().getChunk());
    }
    public City(String cityName, UUID uuid, Chunk chunk){
        this.cityName = cityName;
        this.cityMoney = 0;
        this.buergermeister = uuid;
        this.buerger = List.of(buergermeister);
        this.claimedChunks = List.of(chunk);
    }
    public City(String cityName, OfflinePlayer player, int Money, List<UUID> buerger, List<Chunk> chunkList){
        this.cityName = cityName;
        this.buergermeister = player.getUniqueId();
        this.cityMoney = Money;
        this.buerger = buerger;
        this.claimedChunks = chunkList;
    }
    public City(String cityName, UUID player, int Money, List<UUID> buerger, List<Chunk> chunkList){
        this.cityName = cityName;
        this.buergermeister = player;
        this.cityMoney = Money;
        this.buerger = buerger;
        this.claimedChunks = chunkList;
    }

    public List<Chunk> getClaimedChunks() {
        return claimedChunks;
    }

    public void setClaimedChunks(List<Chunk> claimedChunks) {
        this.claimedChunks = claimedChunks;
    }

    public void addClaimedCHunk(Chunk chunk){
        claimedChunks.add(chunk);
    }

    public List<UUID> getBuergerUUID() {
        return buerger;
    }
    public List<Player> getBuerger(){
        List<Player> buergerlist= new ArrayList<>();
        buerger.forEach(buerger -> buergerlist.add(Bukkit.getPlayer(buerger)));
        return buergerlist;
    }

    public void adBuerger(UUID buerger) {
        this.buerger.add(buerger);
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityMoney() {
        return cityMoney;
    }

    public void addCityMoney(int cityMoney) {
        this.cityMoney = cityMoney + this.cityMoney;
    }
    public UUID getBuergermeisterUUID(){
        return buergermeister;
    }
    public OfflinePlayer getBuergermeister(){
        return Bukkit.getOfflinePlayer(buergermeister);
    }
    public void setBuergermeister(UUID players){
        this.buergermeister = players;
    }
}
