package poseidon.skills.citys;

import org.bukkit.Chunk;

import java.util.*;

public class Nation{
    private final String nationName;
    private final HashMap<City, List<UUID>> cityList;
    private UUID King;
    private final List<Chunk> chunkList;
    private int CityMoney;
    private City mainCity;
    private List<UUID> vizeList;
    private final List<Chunk> allNationChunkList;

    public Nation(String nationName,City mainCity, UUID king) {
        this.mainCity = mainCity;
        this.nationName = nationName;
        this.cityList = new HashMap<>();
        cityList.put(mainCity, mainCity.getBuergerUUID());
        King = king;
        this.chunkList = new ArrayList<>();
        CityMoney = 0;
        this.vizeList = new ArrayList<>();
        this.allNationChunkList = new ArrayList<>();
        allNationChunkList.addAll(mainCity.getClaimedChunks());
    }
    public Nation(String nationName, City mainCity, UUID king, List<Chunk> chunkList, List<City> cityList, int Money, List<UUID> vizeList){
        this.mainCity = mainCity;
        this.nationName = nationName;
        this.CityMoney = Money;
        this.cityList = new HashMap<>();
        this.allNationChunkList = new ArrayList<>();
        for(City city : cityList){
            this.cityList.put(city, city.getBuergerUUID());
            allNationChunkList.addAll(city.getClaimedChunks());
        }
        allNationChunkList.addAll(chunkList);
        this.chunkList = chunkList;
        this.King = king;
        this.vizeList = vizeList;
    }
    public boolean isNationChunk(Chunk chunk){
        return  allNationChunkList.contains(chunk);
    }

    public List<Chunk> getChunkList() {
        return chunkList;
    }

    public void setKing(UUID uuid){
        this.King = uuid;
    }
    public UUID getKing() {
        return King;
    }

    public HashMap<City, List<UUID>> getCityList() {
        return cityList;
    }

    public int getNationMoney() {
        return CityMoney;
    }

    public void setCityMoney(int cityMoney) {
        CityMoney = cityMoney;
    }
    public void addCityMoney(int cityMoney) {
        if(cityMoney > 0) {
            CityMoney = getNationMoney() + cityMoney;
        }
        else {
            throw new IllegalArgumentException();
        }
    }
    public boolean payMoney(int cityMoney){
        if(CityMoney - cityMoney > 0){
            CityMoney = CityMoney - cityMoney;
            return true;
        }
        return false;
    }

    public void claimChunk(Chunk chunk){
        CityMapper.addChunk(this,chunk);
        this.allNationChunkList.add(chunk);
        this.chunkList.add(chunk);
    }

    public String getNationName() {
        return nationName;
    }

    public City getMainCity() {
        return mainCity;
    }

    public void setMainCity(City mainCity) {
        this.mainCity = mainCity;
    }

    public List<UUID> getVizeList() {
        return vizeList;
    }

    public void setVizeList(List<UUID> vizeList) {
        this.vizeList = vizeList;
    }
    public boolean isAdmin(UUID uuid){
        return vizeList.contains(uuid) || this.King.equals(uuid);
    }

    public List<UUID> getBuergerUUID() {
        List<UUID> uuids = new ArrayList<>();
        for(Map.Entry<City, List<UUID>> entry : this.getCityList().entrySet()){
            List<UUID> value = entry.getValue();
            uuids.addAll(value);
        }
        return uuids;
    }

    public void addCity(City city){
        cityList.put(city,city.getBuergerUUID());
    }
    public void removeCity(City city){
        cityList.remove(city);
        allNationChunkList.removeAll(city.getClaimedChunks());
    }

    public boolean isVize(UUID uniqueId) {
        return vizeList.contains(uniqueId);
    }

    public void remobeVize(UUID uniqueId) {
        vizeList.remove(uniqueId);
    }

    public void addVize(UUID uniqueId) {
        vizeList.add(uniqueId);
    }
}
