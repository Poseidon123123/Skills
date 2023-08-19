package poseidon.skills.CustomItems;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.Nullable;
import poseidon.skills.Klassen.Berufklasse;
import poseidon.skills.Klassen.Kampfklassen;
import poseidon.skills.Skills;

import java.util.HashMap;
import java.util.Map;

import static poseidon.skills.CustomItems.CustomItem.makeExamples;

public class CustomShapedRecipe {
    private final ShapedRecipe recipe;
    private Berufklasse berufklasse = Berufklasse.Unchosed;
    private Kampfklassen kampfklassen = Kampfklassen.Unchosed;
    private int neededBerufLevel = -1;
    private int neededKampfLevel = -1;
    public CustomShapedRecipe(ShapedRecipe recipe){
        this.recipe = recipe;
    }
    public CustomShapedRecipe(ShapedRecipe recipe, Berufklasse berufklasse){
        this.recipe = recipe;
        this.berufklasse = berufklasse;
    }
    public CustomShapedRecipe(ShapedRecipe recipe, Berufklasse berufklasse, int berufLevel){
        this.recipe = recipe;
        this.berufklasse = berufklasse;
        this.neededBerufLevel = berufLevel;
    }
    public CustomShapedRecipe(ShapedRecipe recipe, Kampfklassen kampfklassen){
        this.recipe = recipe;
        this.kampfklassen = kampfklassen;
    }
    public CustomShapedRecipe(ShapedRecipe recipe, Kampfklassen kampfklassen, int kampfLevel){
        this.recipe = recipe;
        this.kampfklassen = kampfklassen;
        this.neededKampfLevel = kampfLevel;
    }
    public CustomShapedRecipe(ShapedRecipe recipe, Kampfklassen kampfklassen, Berufklasse berufklasse){
        this.recipe = recipe;
        this.kampfklassen = kampfklassen;
        this.berufklasse = berufklasse;
    }
    public CustomShapedRecipe(ShapedRecipe recipe, Kampfklassen kampfklassen, int kampfLevel, Berufklasse berufklasse){
        this.recipe = recipe;
        this.kampfklassen = kampfklassen;
        this.berufklasse = berufklasse;
        this.neededKampfLevel = kampfLevel;
    }
    public CustomShapedRecipe(ShapedRecipe recipe, Kampfklassen kampfklassen, Berufklasse berufklasse, int berufLevel){
        this.recipe = recipe;
        this.kampfklassen = kampfklassen;
        this.berufklasse = berufklasse;
        this.neededBerufLevel = berufLevel;
    }
    public CustomShapedRecipe(ShapedRecipe recipe, Kampfklassen kampfklassen, int kampfLevel, Berufklasse berufklasse, int berufLevel){
        this.recipe = recipe;
        this.kampfklassen = kampfklassen;
        this.berufklasse = berufklasse;
        this.neededBerufLevel = berufLevel;
        this.neededKampfLevel = kampfLevel;
    }
    public ShapedRecipe getRecipe() {
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

    private static HashMap<NamespacedKey, CustomShapedRecipe> customShapedRecipeHashMap = new HashMap<>();
    private static HashMap<ItemStack, NamespacedKey> keyHashMap = new HashMap<>();
    public static void registerRecipe(CustomShapedRecipe recipe){
        customShapedRecipeHashMap.put(recipe.getRecipe().getKey(), recipe);
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
    public static CustomShapedRecipe getByKey(NamespacedKey key){
        return customShapedRecipeHashMap.get(key);
    }
    public static HashMap<NamespacedKey, CustomShapedRecipe> getCustomShapedRecipeHashMap(){
        return customShapedRecipeHashMap;
    }
    public static void example(){
        ItemStack itemStack = makeExamples();
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(Skills.getInstance(),"Test"), itemStack);
        recipe.shape("AEA","AEA","SSS");
        recipe.setIngredient('E', Material.EMERALD);
        recipe.setIngredient('S', Material.STICK);
        recipe.setIngredient('A', Material.AIR);
        registerRecipe(new CustomShapedRecipe(recipe, Berufklasse.getOfArray("Holzfäller"), 5));
    }

    public int getNeededBerufLevel() {
        return neededBerufLevel;
    }

    public int getNeededKampfLevel() {
        return neededKampfLevel;
    }
}
