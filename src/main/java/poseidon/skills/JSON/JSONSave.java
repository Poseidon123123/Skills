package poseidon.skills.JSON;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.json.simple.JSONObject;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import poseidon.skills.CustomItems.CustomItem;
import poseidon.skills.CustomItems.CustomShapedRecipe;
import poseidon.skills.CustomItems.CustomShapelessRecipe;
import poseidon.skills.Klassen.Berufklasse;
import poseidon.skills.Klassen.Kampfklassen;
import poseidon.skills.Klassen.KlassChoose;
import poseidon.skills.Klassen.Players;
import poseidon.skills.ManaMap;
import poseidon.skills.Skills;
import poseidon.skills.XPMapper;
import poseidon.skills.XPObjekt;
import poseidon.skills.citys.City;
import poseidon.skills.citys.CityMapper;
import poseidon.skills.citys.Nation;
import poseidon.skills.executeSkills.Funktions;
import poseidon.skills.executeSkills.Type;
import poseidon.skills.executeSkills.Type.types;
import poseidon.skills.skill.BerufSkills;
import poseidon.skills.skill.KampfSkills;
import poseidon.skills.skill.SkillMapper;

import java.io.*;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("unchecked")
public class JSONSave {


    public static void start() {
        new File(getPlayerDic()).mkdirs();
        new File(getBerufKlassDic()).mkdirs();
        new File(getKampfKlassDic()).mkdirs();
        new File(getKampfSkillDic()).mkdirs();
        new File(getBerufSkillDic()).mkdirs();
        new File(getXPPath()).mkdirs();
        new File(getFishingPath()).mkdirs();
        new File(getMiningPath()).mkdirs();
        new File(getWoodingPath()).mkdirs();
        new File(getFarmingPath()).mkdirs();
        new File(getMobsPath()).mkdirs();
        new File((getCityDic())).mkdirs();
        new File(getCustomShapedRecipeDic()).mkdirs();
        new File(getCustomItemListDic()).mkdirs();
        new File(getCustomShapelessRecipeDic()).mkdirs();
        new File(getPluginDic()).mkdirs();
        new File(getNationDic()).mkdirs();
        new File(getTypeDic()).mkdirs();
    }
    public static void playerSave(Player player) {
        JSONObject obj = new JSONObject();
        Players players = KlassChoose.getPlayers(player);
        String name = player.getName();
        String dicPath = getPlayerDic();
        String filePath = getPlaerPath(player);
        obj.put("Name", name);
        obj.put("Berufklasse", players.getBerufklasse().getDisplayName());
        obj.put("Kampfklasse", players.getKampfklasse().getDisplayName());
        obj.put("Beruflevel", players.getBerufLevel());
        obj.put("BerufXP", players.getBerufXP());
        obj.put("Kampflevel", players.getKampfLevel());
        obj.put("KampfXP", players.getKampfXP());
        obj.put("Mana", new ManaMap(player).getManaValue());
        obj.put("Money", players.getMoney());
        obj.put("BerufItem", itemTo64(players.getBerufItemSkill()));
        obj.put("KampfItem", itemTo64(players.getKampfItemSkill()));
        if(players.getBoundBeruf() != null) {
            obj.put("BerufSkill", players.getBoundBeruf().getName());
        }
        if(players.getBoundKampf() != null) {
            obj.put("KampfSkill", players.getBoundKampf().getName());
        }
        if(players.getBerufChange() != null) {
            obj.put("BerufChange", localDateTimeToString(players.getBerufChange()));
        }
        if(players.getKampfChange() != null){
            obj.put("KampfChange", localDateTimeToString(players.getKampfChange()));
        }
        String s = "null";
        if (players.getHometown() != null) {
            s = players.getHometown().getCityName();
        }
        obj.put("Chat", players.getChat().getKurz());
        obj.put("City", s);
        File fileer = new File(dicPath);
        fileer.mkdir();
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(obj.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void generateDestroyFIles(List<XPObjekt> map) {
        map.forEach(xpObjekt -> {
            for (JSONSave.location location : location.values()) {
                if (xpObjekt.getXpSource().equals(location.xpSource)) {
                    generateDestroyFIles(xpObjekt, location);
                    break;
                }
            }
        });
    }
    public static void generateDestroyFIles(XPObjekt xpObjekt, location location) {
        JSONObject object = new JSONObject();
        object.put("Item", xpObjekt.getMaterial().toString());
        object.put("XP", xpObjekt.getXp());
        object.put("Money", xpObjekt.getMoney());
        String a = location.getPath() + xpObjekt.getMaterial() + ".json";
        try (FileWriter file = new FileWriter(a)) {
            file.write(object.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void generateDestroyFIles(List<XPObjekt> map, location location) {
        map.forEach((xpObjekt) -> {
            JSONObject object = new JSONObject();
            object.put("Item", xpObjekt.getMaterial().toString());
            object.put("XP", xpObjekt.getXp());
            object.put("Money", xpObjekt.getMoney());
            String a = location.getPath() + xpObjekt.getMaterial() + ".json";
            try (FileWriter file = new FileWriter(a)) {
                file.write(object.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void saveTypes(){
        Funktions.getTypes().forEach((s, type) -> {
            JSONObject object = new JSONObject();
            object.put("Name", type.getName());
            if (type.getType().contains(types.projectile)) {
                object.put("projetile", type.getEntityType().getTranslationKey());
                object.put("proMulti", type.getProjectilePara());
            }
            if (type.getType().contains(types.remember)) {
                int a = 1;
                for(Material m : type.getRememberAction()){
                    object.put("rememgerMaterial" + a, m.toString());
                    a++;
                }
                object.put("rememberDuration", type.getRememberDuration());
            }
            if(type.getType().contains(types.teleport)){
                object.put("tpPara", type.getTpPara());
            }
            if(type.getType().contains(types.leap)){
                object.put("leapPara", type.getLeapPara());
            }
            if(type.getType().contains(types.self)){
                object.put("selfPotion", type.getSelfEffect().getKey().toString());
                object.put("selfDur", type.getSelfDur());
                object.put("selfAmpli", type.getSelfAmpli());
            }
            if(type.getType().contains(types.nearest)){
                object.put("nearPotion", type.getNearestEffect().getKey().toString());
                object.put("nearRadius", type.getNearRadius());
                object.put("nearMax", type.getNearMax());
                object.put("nearDur", type.getNearDur());
                object.put("nearAmpli", type.getNearAmpli());
            }
            try (FileWriter file = new FileWriter(getCustomTypePath(type))){
                file.write(object.toJSONString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public static void kampfKlassenSave() {
        Kampfklassen.getKlassen().forEach((klassen) -> {
            JSONObject object = new JSONObject();
            object.put("Klasse", klassen.getDisplayName());
            object.put("Mana", klassen.getBaseMana());
            try (FileWriter file = new FileWriter(getkampfKlassenPath(klassen))) {
                file.write(object.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    public static void kampfSkillSave() {
        for (KampfSkills kampfSkills : SkillMapper.getKampfSkills()) {
            JSONObject object = new JSONObject();
            object.put("Name", kampfSkills.getName());
            object.put("Kampfklasse", kampfSkills.getKampfKlasse().getDisplayName());
            object.put("Material", kampfSkills.getIcon().getType().toString());
            object.put("Command", kampfSkills.getCommand());
            object.put("Cooldown", kampfSkills.getCooldown());
            object.put("neededLevel", kampfSkills.getNeededLevel());
            object.put("Mana", kampfSkills.getConsumedMana());
            try (FileWriter file = new FileWriter(getKampfSkillPath(kampfSkills))) {
                file.write(object.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static String convertToString(String[] array) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < array.length; i++) {
            result.append(array[i]);
            if (i < array.length - 1) {
                result.append(" ");
            }
        }

        return result.toString();
    }
    public static void saveShapelessRecipes(){
        CustomShapelessRecipe.getCustomShapedRecipeHashMap().forEach((key, shapelessRecipe) -> {
            JSONObject object = new JSONObject();
            if (shapelessRecipe.getBerufklasse() != Berufklasse.Unchosed) {
                object.put("beruf", shapelessRecipe.getBerufklasse().getDisplayName());
            }
            if (shapelessRecipe.getKampfklassen() != Kampfklassen.Unchosed) {
                object.put("kampf", shapelessRecipe.getKampfklassen().getDisplayName());
            }
            if(shapelessRecipe.getNeededBerufLevel() > 0){
                object.put("berufLevel", shapelessRecipe.getNeededBerufLevel());
            }
            if(shapelessRecipe.getNeededKampfLevel() > 0){
                object.put("kampfLevel", shapelessRecipe.getNeededKampfLevel());
            }
            int b = 0;
            for(ItemStack itemStack : shapelessRecipe.getRecipe().getIngredientList()){
                b++;
                object.put(b + "ItemData", itemTo64(itemStack));
            }
            object.put("Category", shapelessRecipe.getRecipe().getCategory().toString());
            object.put("Key", key.toString());
            object.put("ResultItemData", itemTo64(shapelessRecipe.getRecipe().getResult()));
            try (FileWriter file = new FileWriter(getCustomShapelessRecipePath(shapelessRecipe.getRecipe()))){
                file.write(object.toJSONString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public static void saveShapedRecipes() {
        CustomShapedRecipe.getCustomShapedRecipeHashMap().forEach((namespacedKey, shapedRecipe) -> {
            JSONObject object = new JSONObject();
            if (shapedRecipe.getBerufklasse() != Berufklasse.Unchosed) {
                object.put("beruf", shapedRecipe.getBerufklasse().getDisplayName());
            }
            if (shapedRecipe.getKampfklassen() != Kampfklassen.Unchosed) {
                object.put("kampf", shapedRecipe.getKampfklassen().getDisplayName());
            }
            if(shapedRecipe.getNeededBerufLevel() > 0){
                object.put("berufLevel", shapedRecipe.getNeededBerufLevel());
            }
            if(shapedRecipe.getNeededKampfLevel() > 0){
                object.put("kampfLevel", shapedRecipe.getNeededKampfLevel());
            }
            object.put("Category", shapedRecipe.getRecipe().getCategory().toString());
            object.put("shape", convertToString(shapedRecipe.getRecipe().getShape()));
            Map<Character, ItemStack> a = shapedRecipe.getRecipe().getIngredientMap();
            int b = 0;
            for (Map.Entry<Character, ItemStack> entry : a.entrySet()) {
                Character character = entry.getKey();
                ItemStack itemStack = entry.getValue();
                b++;
                object.put(b + "char", character.toString());
                object.put(b + "ItemData", itemTo64(itemStack));
            }
            object.put("Key", namespacedKey.toString());
            object.put("ResultItemData", itemTo64(shapedRecipe.getRecipe().getResult()));
            try (FileWriter file = new FileWriter(getCustomRecipePath(shapedRecipe.getRecipe()))) {
                file.write(object.toJSONString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public static void saveNations(){
        for(Nation nation : CityMapper.getNationList()){
            JSONObject object = new JSONObject();
            object.put("Name", nation.getNationName());
            object.put("Money", nation.getNationMoney());
            object.put("Meister", nation.getKing().toString());
            int x = 1;
            for (Map.Entry<City, List<UUID>> entry : nation.getCityList().entrySet()) {
                City city = entry.getKey();
                object.put("CityName" + x, city.getCityName());
                x++;
            }
            int y = 1;
            for (Chunk chunk : nation.getChunkList()) {
                object.put("XChunk" + y, chunk.getX());
                object.put("ZChunk" + y, chunk.getZ());
                object.put("ChunkWolrd" + y, chunk.getWorld().getName());
                y++;
            }
            object.put("MainCity", nation.getMainCity().getCityName());
            int z = 1;
            for(UUID player : nation.getVizeList()){
                object.put("Vize" + z, player);
                z++;
            }
            try (FileWriter fileWriter = new FileWriter(getNationPath(nation))){
                fileWriter.write(object.toJSONString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void unSaveCity(City city){
        File f = new File(getCityPath(city));
        f.deleteOnExit();
    }
    public static void unSaveNation(Nation nation){
        File f = new File(getNationPath(nation));
        f.deleteOnExit();
    }
    public static void berufSkillSave() {
        for (BerufSkills kampfSkills : SkillMapper.getBerufSkills()) {
            JSONObject object = new JSONObject();
            object.put("Name", kampfSkills.getName());
            object.put("Kampfklasse", kampfSkills.getBerufklasse().getDisplayName());
            object.put("Material", kampfSkills.getIcon().getType().toString());
            object.put("Command", kampfSkills.getCommand());
            object.put("Cooldown", kampfSkills.getCooldown());
            object.put("neededLevel", kampfSkills.getNeededLevel());
            object.put("Mana", kampfSkills.getConsumedMana());
            try (FileWriter file = new FileWriter(getBerufSkillPath(kampfSkills))) {
                file.write(object.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void berufKlassenSave() {
        Berufklasse.getTest().forEach((klassen) -> {
            JSONObject object = new JSONObject();
            object.put("Klasse", klassen.getDisplayName());
            object.put("Source", klassen.getSource().toString());
            try (FileWriter file = new FileWriter(getBerufKlassenPath(klassen))) {
                file.write(object.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    public static void mobSave() {
        XPMapper.getMobKillXP().forEach((entityType, integer) -> {
            JSONObject object = new JSONObject();
            object.put("Entity", entityType.getTranslationKey());
            object.put("XP", integer);
            try (FileWriter fileWriter = new FileWriter(getMobPath(entityType.getTranslationKey()))) {
                fileWriter.write(object.toJSONString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public static void saveCitys() {
        CityMapper.getCityList().forEach(city -> {
            JSONObject object = new JSONObject();
            object.put("Name", city.getCityName());
            object.put("Money", city.getCityMoney());
            object.put("Meister", city.getBuergermeisterUUID().toString());
            int x = 1;
            for (UUID player : city.getBuergerUUID()) {
                object.put("Bürger" + x, player.toString());
                x++;
            }
            int y = 1;
            for (Chunk chunk : city.getClaimedChunks()) {
                object.put("XChunk" + y, chunk.getX());
                object.put("ZChunk" + y, chunk.getZ());
                object.put("ChunkWolrd" + y, chunk.getWorld().getName());
                y++;
            }
            int z = 1;
            for(UUID player : city.getVizeList()){
                object.put("Vize" + z, player);
                z++;
            }
            if(city.getNation() != null){
                object.put("Nation", city.getNation().getNationName());
            }
            try (FileWriter fileWriter = new FileWriter(getCityPath(city))) {
                fileWriter.write(object.toJSONString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public static void safeCustomItems() {
        for (Map.Entry<String, CustomItem> entry : CustomItem.customItemHashMap.entrySet()) {
            CustomItem customItem = entry.getValue();
            ItemStack itemStack = customItem.getCustomItem();
            JSONObject object = new JSONObject();
            object.put("Item", itemTo64(itemStack));
            if (customItem.isBerufItem()) {
                object.put("Beruf", customItem.getBerufklasse().getDisplayName());
            }
            if (customItem.isKampfItem()) {
                object.put("Kampf", customItem.getKampfklassen().getDisplayName());
            }
            try (FileWriter fileWriter = new FileWriter(getCustomItemPath(itemStack))) {
                fileWriter.write(object.toJSONString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static String getParentPath(String subPath) {
        return Paths.get(getPluginpath()).getParent().getParent().resolve(subPath).toString();
    }

    public static String getPluginpath() {
        String Pluginpath = Skills.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (Pluginpath.startsWith("/")) {
            Pluginpath = Pluginpath.substring(1);
        }
        return Pluginpath;
    }
    public static String getPluginDic(){
        return getParentPath("SkillData");
    }
    public static String getBerufKlassDic() {
        return getPluginDic() + "/Berufklassen/";
    }

    public static String getCityDic() {
        return getPluginDic() + "/Citys/";
    }

    public static String getNationDic(){
        return getPluginDic() + "/Nation/";
    }

    public static String getXPPath() {
        return getPluginDic() + "/XP/";
    }

    public static String getKampfKlassDic() {
        return getPluginDic() + "/Kampfklasse/";
    }

    public static String getPlayerDic() {
        return getPluginDic() + "/players/";
    }

    public static String getKampfSkillDic() {
        return getPluginDic() + "/KampfSkills/";
    }

    public static String getBerufSkillDic() {
        return getPluginDic() + "/BerufSkills/";
    }

    public static String getFishingPath() {
        return getXPPath() + "/Fishing/";
    }

    public static String getMiningPath() {
        return getXPPath() + "/Mining/";
    }

    public static String getWoodingPath() {
        return getXPPath() + "/Chopping/";
    }

    public static String getFarmingPath() {
        return getXPPath() + "/Farming/";
    }

    public static String getMobsPath() {
        return getXPPath() + "/Mobs/";
    }

    public static String getCustomItemDic() {
        return getPluginDic() + "/CustomItems/";
    }

    public static String getCustomItemListDic() {
        return getCustomItemDic() + "/Items/";
    }

    public static String getCustomShapedRecipeDic() {
        return getCustomItemDic() + "/ShapedRecipes/";
    }

    public static String getCustomShapelessRecipeDic() {
        return getCustomItemDic() + "/ShapelessRecipes/";
    }

    public static String getTypeDic(){
        return getPluginDic() + "/SkillFunktions/";
    }



    public static String getBerufKlassenPath(Berufklasse klassen) {
        return getBerufKlassDic() + "/" + klassen.getDisplayName() + ".json";
    }

    public static String getCityPath(City city) {
        return getCityDic() + "/" + city.getCityName() + ".json";
    }

    public static String getkampfKlassenPath(Kampfklassen klassen) {
        return getKampfKlassDic() + "/" + klassen.getDisplayName() + ".json";
    }

    public static String getPlaerPath(Player player) {
        return getPlayerDic() + "/" + player.getName() + ".json";
    }

    public static String getKampfSkillPath(KampfSkills kampfSkills) {
        return getKampfSkillDic() + "/" + kampfSkills.getName() + ".json";
    }

    public static String getBerufSkillPath(BerufSkills kampfSkills) {
        return getBerufSkillDic() + "/" + kampfSkills.getName() + ".json";
    }

    public static String getCustomItemPath(ItemStack itemStack) {
        return getCustomItemListDic() + "/" + Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName() + ".json";
    }

    public static String getCustomShapelessRecipePath(ShapelessRecipe recipe) {
        return getCustomShapelessRecipeDic() + "/" + recipe.getKey().getKey() + ".json";
    }

    public static String getCustomRecipePath(ShapedRecipe recipe) {
        return getCustomShapedRecipeDic() + "/" + recipe.getKey().getKey() + ".json";
    }

    public static String getCustomTypePath(Type type){
        return getTypeDic() + "/" + type.getName() + ".json";
    }


    public static String getMobPath(String string) {
        return getMobsPath() + "/" + string + ".json";
    }

    public static String getNationPath(Nation nation){
        return getNationDic() + "/" + nation.getNationName() + ".json";
    }

    public enum location {
        Wooding(getWoodingPath(), Berufklasse.XPSource.Wooding),
        Farming(getFarmingPath(), Berufklasse.XPSource.Farming),
        Fishing(getFishingPath(), Berufklasse.XPSource.Fishing),
        Mining(getMiningPath(), Berufklasse.XPSource.Mining);
        private final String path;
        private final Berufklasse.XPSource xpSource;

        location(String str, Berufklasse.XPSource xpSource) {
            this.path = str;
            this.xpSource = xpSource;
        }

        public String getPath() {
            return path;
        }

        public Berufklasse.XPSource getXpSource() {
            return xpSource;
        }
    }

    public static String itemTo64(ItemStack stack) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(stack);

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stack.", e);
        }
    }

    public static ItemStack itemFrom64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            try (BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {
                return (ItemStack) dataInput.readObject();
            }
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);

        }
    }
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String localDateTimeToString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return dateTime.format(formatter);
    }

    public static LocalDateTime stringToLocalDateTime(String strDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return LocalDateTime.parse(strDateTime, formatter);
    }

}