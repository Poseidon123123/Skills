package poseidon.skills.GUI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import poseidon.skills.Klassen.KlassChoose;

import static poseidon.skills.GUI.GUIMaker.createGUIItems;
import static poseidon.skills.GUI.GUIMaker.getPLACEHOLDER;

public class SkillGUI {
    public static ItemStack PLACEHOLDER = getPLACEHOLDER;
    public static ItemStack SIGN1 = createGUIItems(Material.DIAMOND,"Skillmenu");
    public static ItemStack SIGN2 = createGUIItems(Material.DIRT,"Error");
    public static ItemStack SIGN21 = createGUIItems(Material.DIRT, "Error");
    public static ItemStack SIGN3 = createGUIItems(Material.DIAMOND_AXE,"Berufsskills");
    public static ItemStack SIGN4 = createGUIItems(Material.SHIELD,"Kampfskills");
    public static ItemStack done;
    public static ItemStack done1;
    private static final ItemStack[] CONTENT =  new ItemStack[]{
            PLACEHOLDER, PLACEHOLDER,PLACEHOLDER, PLACEHOLDER, SIGN1, PLACEHOLDER, PLACEHOLDER, PLACEHOLDER, PLACEHOLDER,
            PLACEHOLDER, PLACEHOLDER,PLACEHOLDER, PLACEHOLDER, PLACEHOLDER, PLACEHOLDER, PLACEHOLDER, PLACEHOLDER, PLACEHOLDER,
            PLACEHOLDER, PLACEHOLDER,PLACEHOLDER, SIGN2, PLACEHOLDER, SIGN21, PLACEHOLDER, PLACEHOLDER, PLACEHOLDER,
            PLACEHOLDER, PLACEHOLDER,PLACEHOLDER, SIGN3, PLACEHOLDER, SIGN4, PLACEHOLDER, PLACEHOLDER, PLACEHOLDER,
            PLACEHOLDER, PLACEHOLDER,PLACEHOLDER, PLACEHOLDER, PLACEHOLDER, PLACEHOLDER, PLACEHOLDER, PLACEHOLDER, PLACEHOLDER,
            PLACEHOLDER, PLACEHOLDER,PLACEHOLDER, PLACEHOLDER, PLACEHOLDER, PLACEHOLDER, PLACEHOLDER, PLACEHOLDER, PLACEHOLDER
    };

    public static Inventory getSkillGUI(Player player){
        Inventory skillGUI = Bukkit.createInventory(player, 54, "Skills");
        skillGUI.setContents(CONTENT);
        int i = skillGUI.first(SIGN2);
        done = createGUIItems(Material.EMERALD, KlassChoose.getPlayers(player).getBerufklasse().getDisplayName(), ChatColor.BLUE);
        if(i >= 0){
            skillGUI.setItem(i, done);
        }
        done1 = createGUIItems(Material.LAPIS_LAZULI, KlassChoose.getPlayers(player).getKampfklasse().getDisplayName(), ChatColor.BLUE);
        int b = skillGUI.first(SIGN21);
        if(b > 0){
            skillGUI.setItem( b, done1);
        }
        return skillGUI;
    }
}
