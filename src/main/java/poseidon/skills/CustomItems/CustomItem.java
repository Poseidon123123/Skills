package poseidon.skills.CustomItems;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import poseidon.skills.Klassen.Berufklasse;
import poseidon.skills.Klassen.Kampfklassen;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CustomItem {
    private final ItemStack CustomItem;
    private Berufklasse berufklasse = Berufklasse.Unchosed;
    private Kampfklassen kampfklassen = Kampfklassen.Unchosed;
    public CustomItem(ItemStack customItem){
        this.CustomItem = customItem;
    }
    public CustomItem(ItemStack customItem, Kampfklassen kampfklassen) {
        this.CustomItem = customItem;
        this.kampfklassen = kampfklassen;
    }
    public CustomItem(ItemStack customItem, Berufklasse berufklasse){
        this.CustomItem = customItem;
        this.berufklasse = berufklasse;
    }
    public CustomItem(ItemStack customItem, Kampfklassen kampfklassen, Berufklasse berufklasse){
        this.CustomItem = customItem;
        this.kampfklassen = kampfklassen;
        this.berufklasse = berufklasse;
    }
    public ItemStack getCustomItem(){
        return this.CustomItem;
    }
    public Berufklasse getBerufklasse(){
        return this.berufklasse;
    }
    public Kampfklassen getKampfklassen() {
        return kampfklassen;
    }
    public boolean isKampfItem(){
        return kampfklassen != Kampfklassen.Unchosed;
    }
    public boolean isBerufItem(){
        return berufklasse != Berufklasse.Unchosed;
    }

    public static HashMap<String, CustomItem> customItemHashMap = new HashMap<>();
    public static void registerItem(CustomItem customItem){
        customItemHashMap.put(Objects.requireNonNull(customItem.getCustomItem().getItemMeta()).getDisplayName(),customItem);
    }
    public static CustomItem getByName(String name){
        return customItemHashMap.get(name);
    }


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
        registerItem(new CustomItem(a));
        ItemStack b = new ItemStack(Material.SHIELD);
        ItemMeta meta1 = b.getItemMeta();
        assert meta1 != null;
        meta1.setDisplayName("§4Messerschild");
        meta1.setLore(List.of("§9Nur für die geübetesten Messerwerfer"));
        b.setItemMeta(meta1);
        registerItem(new CustomItem(b,Kampfklassen.getOfArray("Messerwerfer")));
        return a;
    }

    //TODO MAKE EXAMPLE RECIPE Shapless
    //TODO ABFRAGE für berufsklasse Kampfklassen



}
