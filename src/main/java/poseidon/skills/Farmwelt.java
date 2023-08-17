package poseidon.skills;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.io.File;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class Farmwelt {
    public static World farmWorld;
    public static World backWorld = Bukkit.getWorld(Skills.getInstance().message("Funktions.FarmWorld.MainWorldName"));
    private Player player;
    private static HashMap<Player, Farmwelt> farmweltHashMap = new HashMap<>();
    public Farmwelt(Player player){
        this.player = player;
        farmweltHashMap.put(player,this);
    }
    public static void generateNewFarmwelt(){
        if(farmWorld != null) {
            farmWorld.getPlayers().forEach(player1 -> player1.performCommand("farm tp"));
            File folder = farmWorld.getWorldFolder();
            Bukkit.unloadWorld(farmWorld,false);
            deleteFolder(folder);
        }
        farmWorld = farmCreator(true).createWorld();

    }
    public static WorldCreator farmCreator(boolean neu) {

        WorldCreator worldCreator;
        if(neu) {
            worldCreator = new WorldCreator(getRandomString(5));
        }
        else {
            String a = Skills.getInstance().getConfig().getString("Funktions.FarmWorld.Name");
            worldCreator = new WorldCreator(Objects.requireNonNullElseGet(a, () -> getRandomString(5)));
        }
        worldCreator.generateStructures(false);
        worldCreator.hardcore(true);
        Random random = new Random();
        worldCreator.seed(random.nextLong());
        worldCreator.environment(World.Environment.NORMAL);
        return worldCreator;
    }

    public void tpFarm(){
        if (!player.getWorld().equals(farmWorld)) {
            World world = farmWorld;
            if(world != null) {
                player.teleport(world.getSpawnLocation());
            }
            else {
                System.out.println("NoFarm");
            }
        } else {
            player.teleport(backWorld.getSpawnLocation());
        }
    }

    public static Farmwelt getFromMap(Player player){
        return farmweltHashMap.get(player);
    }

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();

    public static String getRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

    public static void deleteFolder(File folder) {
        if (folder == null || !folder.exists()) {
            return;
        }

        if (folder.isDirectory()) {
            File[] subFiles = folder.listFiles();
            if (subFiles != null) {
                for (File subFile : subFiles) {
                    deleteFolder(subFile);
                }
            }
        }

        folder.delete();
    }

}
