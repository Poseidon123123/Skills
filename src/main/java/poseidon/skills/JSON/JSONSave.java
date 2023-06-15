package poseidon.skills.JSON;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import poseidon.skills.Klassen.Berufklasse;
import poseidon.skills.Klassen.Kampfklassen;
import poseidon.skills.Klassen.KlassChoose;
import poseidon.skills.Klassen.Players;
import poseidon.skills.Skills;
import poseidon.skills.skill.BerufSkills;
import poseidon.skills.skill.KampfSkills;
import poseidon.skills.skill.SkillMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

public class JSONSave {
    public static String path = getHostileMobDic() + "/Mobs.json";
    public static void start(){
        new File(getPlayerDic()).mkdirs();
        new File(getBerufKlassDic()).mkdirs();
        new File(getKampfKlassDic()).mkdirs();
        new File(getKampfSkillDic()).mkdirs();
        new File(getBerufSkillDic()).mkdirs();
        new File(getHostileMobDic()).mkdirs();
    }

    public static void playerSave(Player player){
        JSONObject obj = new JSONObject();
        Players players = KlassChoose.getPlayers(player);
        String name = player.getName();
        String dicPath = getPlayerDic();
        String filePath = getPlaerPath(player);
        obj.put("Name", name );
        obj.put("Berufklasse", players.getBerufklasse().getDisplayName());
        obj.put("Kampfklasse", players.getKampfklasse().getDisplayName());
        obj.put("Beruflevel", players.getBerufLevel());
        obj.put("BerufXP", players.getBerufXP());
        obj.put("Kampflevel", players.getKampfLevel());
        obj.put("KampfXP", players.getKampfXP());
        File fileer = new File(dicPath);
        fileer.mkdir();
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(obj.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateHostileMob(){
        JSONObject object = new JSONObject();
        if(new File(path).exists()){
            System.out.println("Exists");
            return;
        }
        for(EntityType entityType : EntityType.values()){
            if(entityType.equals(EntityType.UNKNOWN)){
                continue;
            }
            object.put(entityType.getTranslationKey(), 10);
        }
        try (FileWriter file = new FileWriter(path)) {
            file.write(object.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public static void kampfKlassenSave(){
        Kampfklassen.getKlassen().forEach((klassen) -> {
            JSONObject object = new JSONObject();
            object.put("Klasse", klassen.getDisplayName());
            try (FileWriter file = new FileWriter(getkampfKlassenPath(klassen))) {
                file.write(object.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void kampfSkillSave(){
        for (KampfSkills kampfSkills : SkillMapper.getKampfSkills()){
            JSONObject object = new JSONObject();
            object.put("Name", kampfSkills.getName());
            object.put("Kampfklasse", kampfSkills.getKampfKlasse().getDisplayName());
            object.put("Material", kampfSkills.getIcon().getType().toString());
            object.put("Command", kampfSkills.getCommand());
            object.put("Cooldown", kampfSkills.getCooldown());
            object.put("neededLevel", kampfSkills.getNeededLevel());
            try(FileWriter file = new FileWriter(getKampfSkillPath(kampfSkills))){
                file.write(object.toJSONString());
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static void berufSkillSave(){
        for (BerufSkills kampfSkills : SkillMapper.getBerufSkills()){
            JSONObject object = new JSONObject();
            object.put("Name", kampfSkills.getName());
            object.put("Kampfklasse", kampfSkills.getBerufklasse().getDisplayName());
            object.put("Material", kampfSkills.getIcon().getType().toString());
            object.put("Command", kampfSkills.getCommand());
            object.put("Cooldown", kampfSkills.getCooldown());
            object.put("neededLevel", kampfSkills.getNeededLevel());
            try(FileWriter file = new FileWriter(getBerufSkillPath(kampfSkills))){
                file.write(object.toJSONString());
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static void berufKlassenSave(){
        Berufklasse.getTest().forEach((klassen) -> {
            JSONObject object = new JSONObject();
            object.put("Klasse", klassen.getDisplayName());
            try (FileWriter file = new FileWriter(getBerufKlassenPath(klassen))) {
                file.write(object.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }





    public static String getPluginpath(){
        String Pluginpath = Skills.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (Pluginpath.startsWith("/")) {
            Pluginpath = Pluginpath.substring(1);
        }
        return Pluginpath;
    }


    public static String getBerufKlassDic(){
        String dic1Path = Paths.get(getPluginpath()).toFile().getParent();
        return Paths.get(dic1Path).toFile().getParent() + "/Berufklassen/";
    }

    public static String getHostileMobDic(){
        String dic1Path = Paths.get(getPluginpath()).toFile().getParent();
        return Paths.get(dic1Path).toFile().getParent() + "/Mobs/";
    }

    public static String getKampfKlassDic(){
        String dic1Path = Paths.get(getPluginpath()).toFile().getParent();
        return Paths.get(dic1Path).toFile().getParent() + "/Kampfklasse/";
    }

    public static String getPlayerDic(){
        String dic1Path = Paths.get(getPluginpath()).toFile().getParent();
        return Paths.get(dic1Path).toFile().getParent() + "/players/";
    }

    public static String getKampfSkillDic(){
        String dic1Path = Paths.get(getPluginpath()).toFile().getParent();
        return Paths.get(dic1Path).toFile().getParent() + "/KampfSkills/";
    }

    public static String getBerufSkillDic(){
        String dic1Path = Paths.get(getPluginpath()).toFile().getParent();
        return Paths.get(dic1Path).toFile().getParent() + "/BerufSkills/";
    }



    public static String getBerufKlassenPath(Berufklasse klassen){
        return getBerufKlassDic() + "/" + klassen.getDisplayName() + ".json";
    }

    public static String getkampfKlassenPath(Kampfklassen klassen){
        return getKampfKlassDic() + "/" + klassen.getDisplayName() + ".json";
    }

    public static String getPlaerPath(Player player){
        return getPlayerDic() + "/" + player.getName() + ".json";
    }

    public static String getKampfSkillPath(KampfSkills kampfSkills){
        return getKampfSkillDic() + "/" + kampfSkills.getName() + ".json";
    }

    public static String getBerufSkillPath(BerufSkills kampfSkills){
        return getBerufSkillDic() + "/" + kampfSkills.getName() + ".json";
    }
}
