package poseidon.skills.CustomItems;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import org.jetbrains.annotations.Nullable;
import poseidon.skills.Klassen.Berufklasse;
import poseidon.skills.Klassen.Kampfklassen;
import poseidon.skills.Skills;

import java.util.HashMap;
import java.util.Map;

public class CustomShapelessRecipe {
    private final ShapelessRecipe recipe;
    private Berufklasse berufklasse = Berufklasse.Unchosed;
    private Kampfklassen kampfklassen = Kampfklassen.Unchosed;
    private int neededBerufLevel = -1;
    private int neededKampfLevel = -1;
    public CustomShapelessRecipe(ShapelessRecipe recipe){
        this.recipe = recipe;
    }
    public CustomShapelessRecipe(ShapelessRecipe recipe, Berufklasse berufklasse){
        this.recipe = recipe;
        this.berufklasse = berufklasse;
    }
    public CustomShapelessRecipe(ShapelessRecipe recipe, Berufklasse berufklasse, int berufLevel){
        this.recipe = recipe;
        this.berufklasse = berufklasse;
        this.neededBerufLevel = berufLevel;
    }
    public CustomShapelessRecipe(ShapelessRecipe recipe, Kampfklassen kampfklassen){
        this.recipe = recipe;
        this.kampfklassen = kampfklassen;
    }
    public CustomShapelessRecipe(ShapelessRecipe recipe, Kampfklassen kampfklassen, int kampfLevel){
        this.recipe = recipe;
        this.kampfklassen = kampfklassen;
        this.neededKampfLevel = kampfLevel;
    }
    public CustomShapelessRecipe(ShapelessRecipe recipe, Kampfklassen kampfklassen, Berufklasse berufklasse){
        this.recipe = recipe;
        this.kampfklassen = kampfklassen;
        this.berufklasse = berufklasse;
    }
    public CustomShapelessRecipe(ShapelessRecipe recipe, Kampfklassen kampfklassen, int kampfLevel, Berufklasse berufklasse){
        this.recipe = recipe;
        this.kampfklassen = kampfklassen;
        this.berufklasse = berufklasse;
        this.neededKampfLevel = kampfLevel;
    }
    public CustomShapelessRecipe(ShapelessRecipe recipe, Kampfklassen kampfklassen, Berufklasse berufklasse, int berufLevel){
        this.recipe = recipe;
        this.kampfklassen = kampfklassen;
        this.berufklasse = berufklasse;
        this.neededBerufLevel = berufLevel;
    }
    public CustomShapelessRecipe(ShapelessRecipe recipe, Kampfklassen kampfklassen, int kampfLevel, Berufklasse berufklasse, int berufLevel){
        this.recipe = recipe;
        this.kampfklassen = kampfklassen;
        this.berufklasse = berufklasse;
        this.neededBerufLevel = berufLevel;
        this.neededKampfLevel = kampfLevel;
    }
    public ShapelessRecipe getRecipe() {
        return recipe;
    }

    public Berufklasse getBerufklasse() {
        return berufklasse;
    }

    public boolean noRestriction(){
        return this.getKampfklassen() != Kampfklassen.Unchosed && this.getBerufklasse() != Berufklasse.Unchosed && this.getNeededBerufLevel() > 0 && this.neededKampfLevel > 0;
    }
    public Kampfklassen getKampfklassen() {
        return kampfklassen;
    }

    private static HashMap<NamespacedKey, CustomShapelessRecipe> customShapelessRecipeHashMap = new HashMap<>();
    private static HashMap<ItemStack, NamespacedKey> keyHashMap = new HashMap<>();
    public static void registerRecipe(CustomShapelessRecipe recipe){
        customShapelessRecipeHashMap.put(recipe.getRecipe().getKey(), recipe);
        keyHashMap.put(recipe.getRecipe().getResult(), recipe.getRecipe().getKey());
        Bukkit.getServer().addRecipe(recipe.getRecipe());
    }
    @Nullable
    public static NamespacedKey getKeyByItem(ItemStack itemStack){
        for (Map.Entry<ItemStack, NamespacedKey> entry : keyHashMap.entrySet()) {
            ItemStack itemStack1 = entry.getKey();
            NamespacedKey key = entry.getValue();
            if(itemStack1.isSimilar(itemStack)){
                return key;
            }
        }
        return null;
    }
    public static CustomShapelessRecipe getByKey(NamespacedKey key){
        return customShapelessRecipeHashMap.get(key);
    }
    public static HashMap<NamespacedKey, CustomShapelessRecipe> getCustomShapedRecipeHashMap(){
        return customShapelessRecipeHashMap;
    }
    public static void example(){
        ItemStack goldblock = new ItemStack(Material.GOLD_BLOCK);
        ItemStack apple = new ItemStack(Material.APPLE);
        ItemStack result = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE);
        NamespacedKey key = new NamespacedKey(Skills.getInstance(), "GoldApple");
        ShapelessRecipe recipe1 = new ShapelessRecipe(key, result);
        recipe1.addIngredient(new RecipeChoice.ExactChoice(goldblock));
        recipe1.addIngredient(new RecipeChoice.ExactChoice(apple));
        recipe1.setCategory(CraftingBookCategory.EQUIPMENT);
        registerRecipe(new CustomShapelessRecipe(recipe1, Kampfklassen.getOfArray("Messerwerfer"), 30));
    }

    public int getNeededBerufLevel() {
        return neededBerufLevel;
    }

    public int getNeededKampfLevel() {
        return neededKampfLevel;
    }
}
