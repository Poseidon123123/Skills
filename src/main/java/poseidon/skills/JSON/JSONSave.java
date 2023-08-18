package poseidon.skills.JSON;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.json.simple.JSONObject;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import poseidon.skills.CustomItems.CustomItem;
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
import poseidon.skills.skill.BerufSkills;
import poseidon.skills.skill.KampfSkills;
import poseidon.skills.skill.SkillMapper;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class JSONSave {


    public static void start() {
        new File(getPlayerDic()).mkdirs();
        new File(getBerufKlassDic()).mkdirs();
        new File(getKampfKlassDic()).mkdirs();
        new File(getKampfSkillDic()).mkdirs();
        new File(getBerufSkillDic()).mkdirs();
        new File(getHostileMobDic()).mkdirs();
        new File(getFishingPath()).mkdirs();
        new File(getMiningPath()).mkdirs();
        new File(getWoodingPath()).mkdirs();
        new File(getFarmingPath()).mkdirs();
        new File(getMobsPath()).mkdirs();
        new File((getCityDic())).mkdirs();
        new File(getCustomRecipeDic()).mkdirs();
        new File(getCustomItemListDic()).mkdirs();
        new File(getCustomRecipe1Dic()).mkdirs();
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
        String s = "null";
        if (players.getHometown() != null) {
            s = players.getHometown().getCityName();
        }
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


    public static void saveRecipes() {
        CustomItem.shapedRecipeList.forEach((shapedRecipe, berufklasse) -> {
            JSONObject object = new JSONObject();
            object.put("beruf", berufklasse.getDisplayName());
            object.put("Category", shapedRecipe.getCategory().toString());
            object.put("shape", convertToString(shapedRecipe.getShape()));
            Map<Character, ItemStack> a = shapedRecipe.getIngredientMap();
            int b = 0;
            for (Map.Entry<Character, ItemStack> entry : a.entrySet()) {
                Character character = entry.getKey();
                ItemStack itemStack = entry.getValue();
                b++;
                object.put(b + "char", character.toString());
                object.put(b + "ItemData", itemTo64(itemStack));
            }
            object.put("Key", shapedRecipe.getKey().toString());
            ItemStack item = shapedRecipe.getResult();
            object.put("ResultItemData", itemTo64(item));
            try (FileWriter file = new FileWriter(getCustomRecipePath(shapedRecipe))) {
                file.write(object.toJSONString());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
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
            object.put("Entiy", entityType.getTranslationKey());
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

        public static String getPluginpath() {
        String Pluginpath = Skills.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (Pluginpath.startsWith("/")) {
            Pluginpath = Pluginpath.substring(1);
        }
        return Pluginpath;
    }

    //TODO Paths neu Organisieren !!

    public static String getBerufKlassDic() {
        String dic1Path = Paths.get(getPluginpath()).toFile().getParent();
        return Paths.get(dic1Path).toFile().getParent() + "/Berufklassen/";
    }

    public static String getCityDic() {
        String dic1Path = Paths.get(getPluginpath()).toFile().getParent();
        return Paths.get(dic1Path).toFile().getParent() + "/Citys/";
    }

    public static String getHostileMobDic() {
        String dic1Path = Paths.get(getPluginpath()).toFile().getParent();
        return Paths.get(dic1Path).toFile().getParent() + "/XP/";
    }

    public static String getKampfKlassDic() {
        String dic1Path = Paths.get(getPluginpath()).toFile().getParent();
        return Paths.get(dic1Path).toFile().getParent() + "/Kampfklasse/";
    }

    public static String getPlayerDic() {
        String dic1Path = Paths.get(getPluginpath()).toFile().getParent();
        return Paths.get(dic1Path).toFile().getParent() + "/players/";
    }

    public static String getKampfSkillDic() {
        String dic1Path = Paths.get(getPluginpath()).toFile().getParent();
        return Paths.get(dic1Path).toFile().getParent() + "/KampfSkills/";
    }

    public static String getBerufSkillDic() {
        String dic1Path = Paths.get(getPluginpath()).toFile().getParent();
        return Paths.get(dic1Path).toFile().getParent() + "/BerufSkills/";
    }

    public static String getFishingPath() {
        String path = getHostileMobDic() + "/Fishing/";
        return path;
    }

    public static String getMiningPath() {
        String path = getHostileMobDic() + "/Mining/";
        return path;
    }

    public static String getWoodingPath() {
        String path = getHostileMobDic() + "/Chopping/";
        return path;
    }

    public static String getFarmingPath() {
        String path = getHostileMobDic() + "/Farming/";
        return path;
    }

    public static String getMobsPath() {
        String path = getHostileMobDic() + "/Mobs/";
        return path;
    }

    public static String getCustomItemDic() {
        String dic1Path = Paths.get(getPluginpath()).toFile().getParent();
        return Paths.get(dic1Path).toFile().getParent() + "/CustomItems/";
    }

    public static String getCustomItemListDic() {
        return getCustomItemDic() + "/Items/";
    }

    public static String getCustomRecipeDic() {
        return getCustomItemDic() + "/ShapedRecipes/";
    }

    public static String getCustomRecipe1Dic() {
        return getCustomItemDic() + "/ShapelessRecipes/";
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

    public static String getCustomRecipePath(ShapelessRecipe recipe) {
        return getCustomRecipe1Dic() + "/" + recipe.getKey() + ".json";
    }

    public static String getCustomRecipePath(ShapedRecipe recipe) {
        return getCustomRecipeDic() + "/" + recipe.getKey().getKey() + ".json";
    }


    public static String getMobPath(String string) {
        return getMobsPath() + "/" + string + ".json";
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
}