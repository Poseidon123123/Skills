package poseidon.skills.JSON;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import poseidon.skills.Chat.ChatAPI;
import poseidon.skills.CustomItems.CustomItem;
import poseidon.skills.CustomItems.CustomShapedRecipe;
import poseidon.skills.CustomItems.CustomShapelessRecipe;
import poseidon.skills.Klassen.Berufklasse;
import poseidon.skills.Klassen.Kampfklassen;
import poseidon.skills.Klassen.Players;
import poseidon.skills.ManaMap;
import poseidon.skills.Skills;
import poseidon.skills.XPMapper;
import poseidon.skills.XPObjekt;
import poseidon.skills.citys.City;
import poseidon.skills.citys.CityMapper;
import poseidon.skills.skill.BerufSkills;
import poseidon.skills.skill.KampfSkills;
import poseidon.skills.skill.SkillMapper;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static poseidon.skills.JSON.JSONSave.getBerufKlassDic;

public class JSONLoad {

    public static Players load(Player player){
        String st = JSONSave.getPlaerPath(player);
        if(new File(st).exists()){
            JSONParser parser = new JSONParser();
            try(Reader reader = new FileReader(st)){
                JSONObject object = (JSONObject) parser.parse(reader);
                Berufklasse klassen1 = object.containsKey("Berufklasse") ? Berufklasse.getOfArray((String) object.get("Berufklasse")) : Berufklasse.Unchosed;
                Kampfklassen kampfklassen = object.containsKey("Kampfklasse") ? Kampfklassen.getOfArray((String) object.get("Kampfklasse")) : Kampfklassen.Unchosed;
                long beruflevel = object.containsKey("Beruflevel") ? (long) object.get("Beruflevel") : 0;
                long berufXP = object.containsKey("BerufXP") ? (long) object.get("BerufXP") : 0;
                long kampflevel = object.containsKey("Kampflevel") ? (long) object.get("Kampflevel") : 0;
                long kampfXP = object.containsKey("KampfXP") ? (long) object.get("KampfXP") : 0;
                long Mana = object.containsKey("Mana") ? (long) object.get("Mana") : 0;
                new ManaMap(player).setManaValue((int) Mana);
                long money = object.containsKey("Money") ? (long) object.get("Money") : 0;
                String cityName = (String) object.get("City");
                City city = !cityName.equals("null") ? CityMapper.getByName(cityName) : null;
                ItemStack berufItem = object.containsKey("BerufItem") ? JSONSave.itemFrom64((String) object.get("BerufItem")) : null;
                BerufSkills berufSkills = object.containsKey("BerufSkill") ? SkillMapper.getOfBerufArray((String) object.get("BerufSkill")) : null;
                ItemStack kampfItem = object.containsKey("KampfItem") ? JSONSave.itemFrom64((String) object.get("KampfItem")) : null;
                KampfSkills kampfSkills = object.containsKey("KampfSkill") ?  SkillMapper.getOfKampfArray((String) object.get("KampfSkill")) : null;
                LocalDateTime berufChange = object.containsKey("BerufChange") ? JSONSave.stringToLocalDateTime((String) object.get("BerufChange")) : null;
                LocalDateTime kampfChange = (object.containsKey("KampfChange")) ? JSONSave.stringToLocalDateTime((String) object.get("KampfChange")) : null;
                ChatAPI.Chats chats = object.containsKey("Chat") ? ChatAPI.Chats.getByKurz((String) object.get("Chat")) : ChatAPI.Chats.GlobalChat;
                return new Players(player, klassen1, kampfklassen, (int) beruflevel, (int) berufXP, (int) kampflevel, (int) kampfXP, (int) money, city, berufItem,
                        berufSkills, kampfItem, kampfSkills, berufChange, kampfChange, chats);
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            return new Players(player, Berufklasse.Unchosed, Kampfklassen.Unchosed);
        }
    }

    public static void loadCitys(){
        File f = new File(JSONSave.getCityDic());
        File[] files = f.listFiles();
        if(files == null){
            return;
        }
        for(File file : files){
            JSONParser parser = new JSONParser();
            try(Reader reader = new FileReader(file)){
                JSONObject object = (JSONObject) parser.parse(reader);
                String name = (String) object.get("Name");
                long money = (long) object.get("Money");
                String playerName = (String) object.get("Meister");
                UUID uuid = UUID.fromString(playerName);
                int x = 1;
                List<UUID> buergerList = new ArrayList<>();
                while (object.containsKey("Bürger" + x)){
                    String burgername = (String) object.get("Bürger" + x);
                    UUID uuid1 = UUID.fromString(burgername);
                    buergerList.add(uuid1);
                    x++;
                }
                int y = 1;
                List<Chunk> chunkList = new ArrayList<>();
                while (object.containsKey("XChunk" + y) && object.containsKey("ZChunk" + y)){
                    long X = (long) object.get("XChunk" + y);
                    long Z = (long) object.get("ZChunk" + y);
                    String worldName = (String) object.get("ChunkWolrd" + y);
                    World world = Skills.getInstance().getServer().getWorld(worldName);
                    if (world != null) {
                        chunkList.add(world.getChunkAt((int) X, (int) Z));
                    }
                    y++;
                }
                CityMapper.addCity(new City(name, uuid,(int) money, buergerList, chunkList));
            }
            catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }

    }
    public static void loadRecipes(){
        File f = new File(JSONSave.getCustomShapedRecipeDic());
        File[] files = f.listFiles();
        if(files == null){
            return;
        }
        for(File file : files){
            JSONParser parser = new JSONParser();
            try (Reader reader = new FileReader(file)){
                JSONObject object = (JSONObject) parser.parse(reader);
                String  keyString = (String) object.get("Key");
                NamespacedKey key = NamespacedKey.fromString(keyString);
                String resultData = (String) object.get("ResultItemData");
                CraftingBookCategory category = CraftingBookCategory.valueOf(object.containsKey("Category") ? (String) object.get("Category") : CraftingBookCategory.MISC.name());
                String shapeString = (String) object.get("shape");
                ItemStack result = JSONSave.itemFrom64(resultData);
                Berufklasse berufklasse = object.containsKey("beruf") ? Berufklasse.getOfArray((String) object.get("beruf")) : Berufklasse.Unchosed;
                Kampfklassen kampfklassen = object.containsKey("kampf") ? Kampfklassen.getOfArray((String) object.get("kampf")) : Kampfklassen.Unchosed;
                int berufLevel = Math.toIntExact(object.containsKey("berufLevel") ? (long) object.get("berufLevel") : -1);
                int kampfLevel = Math.toIntExact(object.containsKey("kampfLevel") ? (long) object.get("kampfLevel") : -1);
                int b = 1;
                assert key != null;
                ShapedRecipe recipe = new ShapedRecipe(key, result);
                recipe.shape(shapeString.split(" "));
                recipe.setCategory(category);
                while (object.containsKey(b + "char") && object.containsKey(b + "ItemData")){
                    String charasString = (String) object.get(b + "char");
                    char c = charasString.charAt(0);
                    ItemStack itemStack = JSONSave.itemFrom64((String) object.get(b + "ItemData"));
                    RecipeChoice recipeChoice = new RecipeChoice.ExactChoice(itemStack);
                    recipe.setIngredient(c,recipeChoice);
                    b++;
                }
                CustomShapedRecipe.registerRecipe(new CustomShapedRecipe(recipe, kampfklassen,kampfLevel, berufklasse, berufLevel));
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }
        File f1 = new File(JSONSave.getCustomShapelessRecipeDic());
        File[] files1 = f1.listFiles();
        if(files1 == null){
            return;
        }
        for(File file : files1){
            JSONParser parser = new JSONParser();
            try (Reader reader = new FileReader(file)){
                JSONObject object = (JSONObject) parser.parse(reader);
                String  keyString = (String) object.get("Key");
                NamespacedKey key = NamespacedKey.fromString(keyString);
                String resultData = (String) object.get("ResultItemData");
                CraftingBookCategory category = CraftingBookCategory.valueOf((String) object.get("Category"));
                ItemStack result = JSONSave.itemFrom64(resultData);
                Berufklasse berufklasse = object.containsKey("beruf") ? Berufklasse.getOfArray((String) object.get("beruf")) : Berufklasse.Unchosed;
                Kampfklassen kampfklassen = object.containsKey("kampf") ? Kampfklassen.getOfArray((String) object.get("kampf")) : Kampfklassen.Unchosed;
                int berufLevel = Math.toIntExact(object.containsKey("berufLevel") ? (long) object.get("berufLevel") : -1);
                int kampfLevel = Math.toIntExact(object.containsKey("kampfLevel") ? (long) object.get("kampfLevel") : -1);
                int b = 1;
                assert key != null;
                ShapelessRecipe recipe = new ShapelessRecipe(key,result);
                recipe.setCategory(category);
                while (object.containsKey(b + "ItemData")){
                    ItemStack itemStack = JSONSave.itemFrom64((String) object.get(b + "ItemData"));
                    RecipeChoice recipeChoice = new RecipeChoice.ExactChoice(itemStack);
                    recipe.addIngredient(recipeChoice);
                    b++;
                }
                CustomShapelessRecipe.registerRecipe(new CustomShapelessRecipe(recipe, kampfklassen, kampfLevel, berufklasse, berufLevel));
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void loadCustomItems(){
        File f = new File(JSONSave.getCustomItemListDic());
        File[] files = f.listFiles();
        if(files == null){
            return;
        }
        for(File file : files){
            JSONParser parser = new JSONParser();
            try(Reader reader = new FileReader(file)){
                JSONObject object = (JSONObject) parser.parse(reader);
                ItemStack itemStack = JSONSave.itemFrom64((String) object.get("Item"));
                Berufklasse berufklasse = Berufklasse.Unchosed;
                Kampfklassen kampfklassen = Kampfklassen.Unchosed;
                if(object.containsKey("Beruf")){
                    berufklasse = Berufklasse.getOfArray((String) object.get("Beruf"));
                }
                if(object.containsKey("Kampf")){
                    kampfklassen = Kampfklassen.getOfArray((String) object.get("Kampf"));
                }
                CustomItem.registerItem(new CustomItem(itemStack, kampfklassen, berufklasse));
            }
            catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void loadKlassen(){
        File f = new File(getBerufKlassDic());
        File[] files = f.listFiles();
        if(files == null){
            return;
        }
        for(File file : files){
            JSONParser parser = new JSONParser();
            try(Reader reader = new FileReader(file)){
                JSONObject object = (JSONObject) parser.parse(reader);
                String display = (String) object.get("Klasse");
                Berufklasse.XPSource xpSource = Berufklasse.XPSource.valueOf((String) object.get("Source"));
                Berufklasse.addToTest(new Berufklasse(display, xpSource));
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
                long baseMana = (long) object.get("Mana");
                Kampfklassen.addKlasse(new Kampfklassen(display, (int) baseMana));
            }
            catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void loadXP(){
        for(JSONSave.location location : JSONSave.location.values()) {
            File f = new File(location.getPath());
            File[] files = f.listFiles();
            if (files == null) {
                return;
            }
            for (File file : files) {
                JSONParser parser = new JSONParser();
                try (Reader reader = new FileReader(file)) {
                    JSONObject object = (JSONObject) parser.parse(reader);
                    Material m = Material.matchMaterial((String) object.get("Item"));
                    long xp = (long) object.get("XP");
                    long money = (long) object.get("Money");
                    XPObjekt objekt = new XPObjekt(m, location.getXpSource(),(int) xp, (int) money);
                    XPObjekt.register(objekt);
                } catch (IOException | ParseException e) {
                    throw new RuntimeException(e);
                }
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
                long consumedMana = (long) object.get("Mana");
                SkillMapper.addKampfSkill(new KampfSkills(ItemMaker.createGUIItems(m, name), name, command, (int) cooldown, kampfklassen, (int) level, (int) consumedMana));
            }
            catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }

    }
    public static void loadBerufSkills() {
        File f = new File(JSONSave.getBerufSkillDic());
        File[] files = f.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            JSONParser parser = new JSONParser();
            try (Reader reader = new FileReader(file)) {
                JSONObject object = (JSONObject) parser.parse(reader);
                String name = (String) object.get("Name");
                String kampfklasse = (String) object.get("Kampfklasse");
                Berufklasse kampfklassen = Berufklasse.getOfArray(kampfklasse);
                String material = (String) object.get("Material");
                Material m = Material.matchMaterial(material);
                String command = (String) object.get("Command");
                long cooldown = (long) object.get("Cooldown");
                long level = (long) object.get("neededLevel");
                long consumedMana = (long) object.get("Mana");
                SkillMapper.addBerufSkill(new BerufSkills(ItemMaker.createGUIItems(m, name), name, command, (int) cooldown, kampfklassen, (int) level, (int) consumedMana));
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void loadMobXP(){
        File f = new File(JSONSave.getMobsPath());
        File[] files = f.listFiles();
        if(files == null){
            return;
        }
        for (File file : files){
            JSONParser parser = new JSONParser();
            try (Reader reader = new FileReader(file)){
                JSONObject object = (JSONObject) parser.parse(reader);
                String entity = (String) object.get("Entity");
                long integer = (long) object.get("XP");
                EntityType entityType = EntityType.ZOMBIE;
                for(EntityType entityType1 : EntityType.values()){
                    if(entityType1.equals(EntityType.UNKNOWN)){
                        continue;
                    }
                    if(entityType1.getTranslationKey().equals(entity)){
                        entityType = entityType1;
                    }
                }
                XPMapper.addMob(entityType, (int) integer);
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
