package poseidon.skills.citys;


import org.bukkit.Chunk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CityMapper {
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
    public static void addChunk(City city, Chunk chunk){
        claimedChunks.put(chunk,city);
    }
    public static void removeCity(City city){
        cityList.remove(city);
    }
    public static City getByName(String name){
        City a = null;
        for (City city : cityList) {
            if (city.getCityName().equals(name)) {
                a = city;
            }
        }
        return a;
    }
    public static boolean ChunkClaimed(Chunk chunk){
        return claimedChunks.containsKey(chunk);
    }
    public static City whoClaimed(Chunk chunk){
        return claimedChunks.get(chunk);
    }
    public static HashMap<Chunk, City> getclaimedChunks(){
        return claimedChunks;
    }

}