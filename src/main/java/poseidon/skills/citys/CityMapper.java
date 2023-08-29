package poseidon.skills.citys;


import org.bukkit.Chunk;
import org.jetbrains.annotations.Nullable;
import poseidon.skills.JSON.JSONSave;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CityMapper {
    //City
    private static final List<City> cityList = new ArrayList<>();
    private static final HashMap<Chunk, City> claimedChunks= new HashMap<>();
    public static List<City> getCityList(){
        return cityList;
    }
    public static void addCity(City city){
        cityList.add(city);
        for(Chunk chunk : city.getClaimedChunks()){
            claimedChunks.put(chunk,city);
        }
    }
    public static boolean validNameCity(String s){
        for(City city : getCityList()){
            if(city.getCityName().equals(s)){
                return false;
            }
        }
        return true;
    }
    public static boolean validNameNation(String s){
        for(Nation city : nationList){
            if(city.getNationName().equals(s)){
                return false;
            }
        }
        return true;
    }
    public static void addChunk(City city, Chunk chunk){
        claimedChunks.put(chunk,city);
    }
    public static void removeCity(City city){
        List<Chunk> chunkList = city.getClaimedChunks();
        chunkList.forEach(NationChunks::remove);
        chunkList.forEach(claimedChunks::remove);
        cityList.remove(city);
        JSONSave.unSaveCity(city);
    }
    public static City getByName(String name){
        return cityList.stream().filter(a -> a.getCityName().equals(name)).findFirst().orElse(null);
    }
    @Nullable
    public static City whoClaimed(Chunk chunk){
        return claimedChunks.get(chunk);
    }
    @Nullable
    public static Nation whichNationClaimed(Chunk chunk){
        return NationChunks.get(chunk);
    }
    public static HashMap<Chunk, City> getclaimedChunks(){
        return claimedChunks;
    }
    //Nation
    private static final List<Nation> nationList = new ArrayList<>();
    public static final HashMap<Chunk,Nation> NationChunks = new HashMap<>();
    public static Nation addNation(Nation nation){
        nationList.add(nation);
        if(nation.getChunkList() != null && !nation.getChunkList().isEmpty()){
            for (Chunk c: nation.getChunkList()) {
                NationChunks.put(c, nation);
            }
        }
        return nation;
        
    }
    public static void removeNation(Nation nation){
        nationList.remove(nation);
        nation.getChunkList().forEach(NationChunks::remove);
        JSONSave.unSaveNation(nation);
    }
    public static void addChunk(Nation nation, Chunk chunk){
        NationChunks.put(chunk, nation);
    }
    public static Nation getNByName(String s){
        return nationList.stream().filter(n -> n.getNationName().equals(s)).findFirst().orElse(null);
    }
    public static List<Nation> getNationList(){
        return nationList;
    }

    //Gerneral
    public static boolean validName(String string){
        return !string.equals(" ") && (validNameCity(string) || validNameNation(string));
    }
    public static boolean ChunkClaimed(Chunk chunk){
        return claimedChunks.containsKey(chunk) || NationChunks.containsKey(chunk);
    }
}