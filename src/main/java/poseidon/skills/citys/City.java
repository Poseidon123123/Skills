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
    private  List<UUID> buerger;
    private List<Chunk> claimedChunks;
    private String nation;
    private List<UUID> vizeList;

    public City(String cityName, Player player){
        this.cityName = cityName;
        this.cityMoney = 0;
        this.buergermeister = player.getUniqueId();
        this.buerger = List.of(buergermeister);
        this.claimedChunks = List.of(player.getLocation().getChunk());
        this.vizeList = new ArrayList<>();
    }
    public City(String cityName, UUID uuid, Chunk chunk){
        this.cityName = cityName;
        this.cityMoney = 0;
        this.buergermeister = uuid;
        this.buerger = List.of(buergermeister);
        this.claimedChunks = List.of(chunk);
        this.vizeList = new ArrayList<>();
    }
    public City(String cityName, OfflinePlayer player, int Money, List<UUID> buerger, List<Chunk> chunkList, String nation, List<UUID> vizeList){
        this.cityName = cityName;
        this.buergermeister = player.getUniqueId();
        this.cityMoney = Money;
        this.buerger = buerger;
        this.claimedChunks = chunkList;
        this.nation = nation;
        this.vizeList = vizeList;
    }
    public City(String cityName, UUID player, int Money, List<UUID> buerger, List<Chunk> chunkList, String nation, List<UUID> vizeList){
        this.cityName = cityName;
        this.buergermeister = player;
        this.cityMoney = Money;
        this.buerger = buerger;
        this.claimedChunks = chunkList;
        this.nation = nation;
        this.vizeList = vizeList;
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
    public boolean hasPlayer(Player player){
        return buerger.contains(player.getUniqueId());
    }

    public void addBuerger(UUID buerger) {
        this.buerger.add(buerger);
    }
    public void removeBuerger(UUID buerger){
        this.buerger.remove(buerger);
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
        this.cityMoney += cityMoney;
    }
    public boolean removeCityMoney(int cityMoney){
        if(this.cityMoney - cityMoney < 0){
            return false;
        }
        else {
            this.cityMoney -= cityMoney;
            return true;
        }
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
    public Nation getNation(){
        return this.nation != null ? CityMapper.getNByName(this.nation) : null;
    }

    public List<UUID> getVizeList() {
        return vizeList;
    }

    public void setVizeList(List<UUID> vizeList) {
        this.vizeList = vizeList;
    }
    public boolean hasAdmin(UUID uuid){
        return vizeList.contains(uuid) || uuid.equals(getBuergermeisterUUID());
    }
    public boolean isVize(UUID uuid){
        return vizeList.contains(uuid);
    }
    public void addVize(UUID uuid){
        vizeList.add(uuid);
    }
    public void remobeVize(UUID uuid){
        vizeList.remove(uuid);
    }
    public void setNation(String s){
        this.nation = s;
    }
    public boolean adminOnline(){
        for (Player player : Bukkit.getOnlinePlayers()){
            if(vizeList.contains(player.getUniqueId()) || getBuergermeisterUUID().equals(player.getUniqueId())){
                return true;
            }
        }
        return false;
    }
}
