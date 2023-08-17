package poseidon.skills.GUI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class OfferGUI {
    private static Inventory offerGUI;
    public static final ItemStack yes = GUIMaker.createGUIItems(Material.GREEN_STAINED_GLASS_PANE, "Annehmen", ChatColor.GREEN);
    public static final ItemStack p = GUIMaker.getPLACEHOLDER;
    public static final ItemStack no = GUIMaker.createGUIItems(Material.RED_STAINED_GLASS_PANE, "Ablehnen", ChatColor.RED);
    public static final ItemStack error = GUIMaker.createGUIItems(Material.DIRT, "Error");
    public static final ItemStack error1 = GUIMaker.createGUIItems(Material.DIRT, "Error1");
    private static final ItemStack[] contens = {
            p,error1,error,p,p,yes,p,no,p
    };
    public static Inventory getOfferGUI(ItemStack itemStack, int Money, String name){
        offerGUI = Bukkit.createInventory(null, 9, "Dir wurde ein Angebot gemacht");
        offerGUI.setContents(contens);
        if(offerGUI.first(error) > 0){
            offerGUI.setItem(offerGUI.first(error), itemStack);
        }
        ItemStack a = GUIMaker.createGUIItems(Material.DIAMOND,  ChatColor.GREEN + name + " will mit dir Handeln.",  ChatColor.DARK_PURPLE + "Es wird ein Angebot von " + Money + "$ für den Folgenden Itemstack geboten", ChatColor.GOLD + "Drücke " + ChatColor.GREEN + "Annehmen" + ChatColor.GOLD +" um den Handel abzuschließen oder drücke "+ ChatColor.RED + "Ablehnen" + ChatColor.GOLD + " um das Angebot abzulehnen");
        if(offerGUI.first(error1) > 0){
            offerGUI.setItem(offerGUI.first(error1), a);
        }
        return offerGUI;
    }
}
