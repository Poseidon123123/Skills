package poseidon.skills.JSON;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import poseidon.skills.Klassen.Berufklasse;
import poseidon.skills.Klassen.Kampfklassen;
import poseidon.skills.Klassen.Players;
import poseidon.skills.XPMapper;
import poseidon.skills.skill.BerufSkills;
import poseidon.skills.skill.KampfSkills;
import poseidon.skills.skill.SkillMapper;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class JSONLoad {

    public static Players load(Player player){
        String st = JSONSave.getPlaerPath(player);
        if(new File(st).exists()){
            JSONParser parser = new JSONParser();
            try(Reader reader = new FileReader(st)){
                JSONObject object = (JSONObject) parser.parse(reader);
                String berufklasse = (String) object.get("Berufklasse");
                Berufklasse klassen1 = Berufklasse.getOfArray(berufklasse);
                String kampfklasse = (String) object.get("Kampfklasse");
                Kampfklassen kampfklassen = Kampfklassen.getOfArray(kampfklasse);
                long beruflevel = (long) object.get("Beruflevel");
                long berufXP = (long) object.get("BerufXP");
                long kampflevel = (long) object.get("Kampflevel");
                long kampfXP = (long) object.get("KampfXP");

                return new Players(player, klassen1, kampfklassen, (int) beruflevel, (int) berufXP, (int) kampflevel, (int) kampfXP);
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            return new Players(player, Berufklasse.Unchosed, Kampfklassen.Unchosed);
        }
    }


    public static void loadKlassen(){
        File f = new File(JSONSave.getBerufKlassDic());
        File[] files = f.listFiles();
        if(files == null){
            return;
        }
        for(File file : files){
            JSONParser parser = new JSONParser();
            try(Reader reader = new FileReader(file)){
                JSONObject object = (JSONObject) parser.parse(reader);
                String display = (String) object.get("Klasse");
                Berufklasse.addToTest(new Berufklasse(display));
            }
            catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }
        File g = new File(JSONSave.getKampfKlassDic());
        File[] files1 = g.listFiles();
        assert files1 != null;
        for(File file : files1){
            JSONParser parser = new JSONParser();
            try(Reader reader = new FileReader(file)){
                JSONObject object = (JSONObject) parser.parse(reader);
                String display = (String) object.get("Klasse");
                Kampfklassen.addKlasse(new Kampfklassen(display));
            }
            catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static void loadKampfSkills(){
        File f = new File(JSONSave.getKampfSkillDic());
        File[] files = f.listFiles();
        if(files == null){
            return;
        }
        for (File file : files){
            JSONParser parser = new JSONParser();
            try(Reader reader = new FileReader(file)){
                JSONObject object = (JSONObject) parser.parse(reader);
                String name = (String) object.get("Name");
                String kampfklasse= (String) object.get("Kampfklasse");
                Kampfklassen kampfklassen = Kampfklassen.getOfArray(kampfklasse);
                String material = (String) object.get("Material");
                Material m = Material.matchMaterial(material);
                String command = (String) object.get("Command");
                long cooldown = (long) object.get("Cooldown");
                long level = (long) object.get("neededLevel");
                SkillMapper.addKampfSkill(new KampfSkills(ItemMaker.createGUIItems(m, name), name, command, (int) cooldown, kampfklassen, (int) level));
            }
            catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static void loadBerufSkills(){
        File f = new File(JSONSave.getBerufSkillDic());
        File[] files = f.listFiles();
        if(files == null){
            return;
        }
        for (File file : files){
            JSONParser parser = new JSONParser();
            try(Reader reader = new FileReader(file)){
                JSONObject object = (JSONObject) parser.parse(reader);
                String name = (String) object.get("Name");
                String kampfklasse = (String) object.get("Kampfklasse");
                Berufklasse kampfklassen = Berufklasse.getOfArray(kampfklasse);
                String material = (String) object.get("Material");
                Material m = Material.matchMaterial(material);
                String command = (String) object.get("Command");
                long cooldown = (long) object.get("Cooldown");
                long level = (long) object.get("neededLevel");
                SkillMapper.addBerufSkill(new BerufSkills(ItemMaker.createGUIItems(m, name), name, command, (int) cooldown, kampfklassen, (int) level));
            }
            catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void loadMobs(){
        JSONParser parser = new JSONParser();
        if(!new File(JSONSave.path).exists()){
            return;
        }
        try(Reader reader = new FileReader(JSONSave.path)){
            JSONObject object = (JSONObject) parser.parse(reader);
            for (EntityType entityType : EntityType.values()){
                if(entityType.equals(EntityType.UNKNOWN)){
                    continue;
                }
                if(object.get(entityType.getTranslationKey()) == null){
                    continue;
                }
                long xp = (long) object.get(entityType.getTranslationKey());
                if(xp <= 0){
                    continue;
                }
                XPMapper.addMob(entityType, (int) xp);
            }
        }
        catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
