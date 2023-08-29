package poseidon.skills.GUI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class tradeGUI {
    private static Inventory inventory;
    public static ItemStack p = GUIMaker.getPLACEHOLDER;
    public static ItemStack no = GUIMaker.createGUIItems(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "Nicht zu frieden");
    public static ItemStack yes = GUIMaker.createGUIItems(Material.GREEN_STAINED_GLASS_PANE, ChatColor.GREEN + "Zufrieden");
    public static ItemStack loadred = GUIMaker.createGUIItems(Material.RED_STAINED_GLASS_PANE, " ");
    public static ItemStack loadgreen = GUIMaker.createGUIItems(Material.GREEN_STAINED_GLASS_PANE, " ");
    public static ItemStack placer = GUIMaker.createGUIItems(Material.LIGHT_GRAY_STAINED_GLASS_PANE, " ");
    public static ItemStack head1 = GUIMaker.createGUIItems(Material.LIGHT_GRAY_STAINED_GLASS_PANE, "Head1");
    public static ItemStack head2 = GUIMaker.createGUIItems(Material.LIGHT_GRAY_STAINED_GLASS_PANE, "Head2");
    public static ItemStack air = GUIMaker.createGUIItems(Material.DIRT, "Error");
    public static ItemStack playerHead1;
    public static ItemStack playerHead2;
    public static ItemStack moneyOther = GUIMaker.createGUIItems(Material.GOLD_BLOCK, "0");
    public static ItemStack m1 = GUIMaker.createGUIItems(Material.GOLD_NUGGET, "+/-1");
    public static ItemStack m2 = GUIMaker.createGUIItems(Material.GOLD_INGOT, "+/-10");
    public static ItemStack m3 = GUIMaker.createGUIItems(Material.GOLD_BLOCK, "+/-100");
    public static ItemStack money = GUIMaker.createGUIItems(Material.GOLD_INGOT, "0");
    public static final ItemStack[] contens = {
            p, p, head1, p, p, p, head2, p, p,
            p, air, air, air, loadred, placer, placer, placer, p,
            p, air, air, air, loadred, placer, placer, placer, p,
            p, air, air, air, loadred, placer, placer, placer, p,
            money, m1, m2, m3, loadred, moneyOther, p, p, p,
            p, no, no, no, p, no, no, no, p
    };
    public static Inventory getTradeGUI(Player trader, Player trader1){
        inventory = Bukkit.createInventory(trader,54,ChatColor.BLUE + "Trade with " + trader1.getName());
        inventory.setContents(contens);
        if(inventory.first(head1) > 0){
            playerHead1 =  createPlayerSkull(trader);
            inventory.setItem(inventory.first(head1),playerHead1);
        }
        if(inventory.first(head2) > 0){
            playerHead2 = createPlayerSkull(trader1);
            inventory.setItem(inventory.first(head2), playerHead2);
        }
        inventory.all(air).forEach((integer, itemStack) -> inventory.remove(air));
        return inventory;
    }
    public static ItemStack createPlayerSkull(OfflinePlayer player) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(player.getName());
            meta.setOwningPlayer(player);
        }
        item.setItemMeta(meta);
        return item;
    }
}
