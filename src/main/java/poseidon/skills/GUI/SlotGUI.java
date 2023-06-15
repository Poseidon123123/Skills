package poseidon.skills.GUI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SlotGUI {
    public static ItemStack B1 = GUIMaker.createGUIItems(Material.PURPLE_STAINED_GLASS_PANE, ChatColor.DARK_PURPLE +  "Slot 2");
    public static ItemStack B2 = GUIMaker.createGUIItems(Material.PURPLE_STAINED_GLASS_PANE, ChatColor.DARK_PURPLE +  "Slot 3");
    public static ItemStack B3 = GUIMaker.createGUIItems(Material.PURPLE_STAINED_GLASS_PANE, ChatColor.DARK_PURPLE +  "Slot 4");
    public static ItemStack B4 = GUIMaker.createGUIItems(Material.PURPLE_STAINED_GLASS_PANE, ChatColor.DARK_PURPLE +  "Slot 5");
    public static ItemStack B5 = GUIMaker.createGUIItems(Material.PURPLE_STAINED_GLASS_PANE, ChatColor.DARK_PURPLE +  "Slot 6");
    public static ItemStack B6 = GUIMaker.createGUIItems(Material.PURPLE_STAINED_GLASS_PANE, ChatColor.DARK_PURPLE +  "Slot 7");
    public static ItemStack B7 = GUIMaker.createGUIItems(Material.PURPLE_STAINED_GLASS_PANE, ChatColor.DARK_PURPLE +  "Slot 8");
    public static ItemStack B8 = GUIMaker.createGUIItems(Material.PURPLE_STAINED_GLASS_PANE, ChatColor.DARK_PURPLE +  "Slot 9");
    public static ItemStack Back = GUIMaker.createGUIItems(Material.RED_STAINED_GLASS_PANE, ChatColor.DARK_RED + "Zurück");
    public static ItemStack[] Slotlist = {
            B1, B2, B3, B4, B5, B6, B7, B8
    };
    private static final ItemStack[] Contents = {
            B1, B2, B3, B4, B5, B6, B7, B8, Back
    };


    public static Inventory getSlotGUI(Player player){
        Inventory slotGUI = Bukkit.createInventory(player, 9, "Wähle einen Slot");
        slotGUI.setContents(Contents);
        return slotGUI;
    }
}
