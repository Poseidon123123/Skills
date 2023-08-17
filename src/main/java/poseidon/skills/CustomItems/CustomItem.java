package poseidon.skills.CustomItems;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import poseidon.skills.Klassen.Berufklasse;
import poseidon.skills.Klassen.Kampfklassen;
import poseidon.skills.Skills;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CustomItem {
    private static HashMap<String, ItemStack> itemMap = new HashMap<>();
    public static void registerItem(ItemStack itemStack){
        itemMap.put(Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName(),itemStack);
    }
    public static ItemStack getByName(String name){
        return itemMap.get(name);
    }
    public static HashMap<String, ItemStack> getItemMap(){
        return itemMap;
    }
    public static HashMap<ItemStack, Berufklasse> berufItemMap = new HashMap<>();
    public static HashMap<ItemStack, Kampfklassen> kampfItemMap = new HashMap<>();
    public static HashMap<ShapedRecipe, Berufklasse> shapedRecipeList = new HashMap<>();
    public static HashMap<ShapelessRecipe, Berufklasse> shapelessRecipeList = new HashMap<>();
    public static ItemStack makeExamples(){
        ItemStack a = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = a.getItemMeta();
        assert meta != null;
        meta.setDisplayName("§2EmeraldSword");
        meta.setLore(List.of("§9Nur für die Tapfersten Krieger", "§eVon den mächtigesten Villagern geschaffen"));
        meta.addEnchant(Enchantment.DAMAGE_ALL, 500, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        meta.setCustomModelData(1);
        a.setItemMeta(meta);
        registerItem(a);
        ItemStack b = new ItemStack(Material.SHIELD);
        ItemMeta meta1 = b.getItemMeta();
        assert meta1 != null;
        meta1.setDisplayName("§4Messerschild");
        meta1.setLore(List.of("§9Nur für die geübetesten Messerwerfer"));
        b.setItemMeta(meta1);
        registerItem(b);
        kampfItemMap.put(b, Kampfklassen.getOfArray("Messerwerfer"));
        return a;
    }

    public static void registerRecipe(ShapedRecipe recipe, Berufklasse berufklasse){
        if(Bukkit.getRecipe(recipe.getKey()) == null) {
            Bukkit.getServer().addRecipe(recipe);
            shapedRecipeList.put(recipe, berufklasse);
        }
    }
    public static void registerRecipe(ShapelessRecipe recipe, Berufklasse berufklasse){
        if(Bukkit.getRecipe(recipe.getKey()) == null) {
            shapelessRecipeList.put(recipe, berufklasse);
            Bukkit.getServer().addRecipe(recipe);
        }
    }

    public static Berufklasse getBerufRecipe(ShapedRecipe key){
        if(shapedRecipeList.containsKey(key)){
            Berufklasse berufklasse = shapedRecipeList.get(key);
            if(berufklasse.equals(Berufklasse.Unchosed)){
                return null;
            }
            return berufklasse;
        }
        return null;
    }

    public static Berufklasse getBerufRecipe(ShapelessRecipe key){
        if(shapelessRecipeList.containsKey(key)){
            Berufklasse berufklasse = shapelessRecipeList.get(key);
            if(berufklasse.equals(Berufklasse.Unchosed)){
                return null;
            }
            return berufklasse;
        }
        return null;
    }

    public static void example(){
        ItemStack itemStack = makeExamples();
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(Skills.getInstance(),"Test"), itemStack);
        recipe.shape("AEA","AEA","SSS");
        recipe.setIngredient('E', Material.EMERALD);
        recipe.setIngredient('S', Material.STICK);
        recipe.setIngredient('A', Material.AIR);
        registerRecipe(recipe, Berufklasse.Unchosed);
    }
    //TODO MAKE EXAMPLE RECIPE
    //TODO ABFRAGE für berufsklasse



}
