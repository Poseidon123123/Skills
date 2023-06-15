package poseidon.skills.GUI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import poseidon.skills.Klassen.KlassChoose;
import poseidon.skills.skill.BerufSkills;

import java.util.ArrayList;

public class BerufSkillGUI {
    private static Inventory kampfSkills;
    public static ItemStack PlaceHolder = GUIMaker.getPLACEHOLDER;
    public static ItemStack AIR = GUIMaker.createGUIItems(Material.DIRT, "Error");
    public static ItemStack BACKBUTTON = GUIMaker.createGUIItems(Material.RED_STAINED_GLASS_PANE, "Zurück", ChatColor.DARK_RED);
    //TODO Erklärungen in die unteren nicht benutzten Felder packen
    private static final ItemStack[] CONTENT = {
            PlaceHolder,PlaceHolder,PlaceHolder,PlaceHolder,PlaceHolder,PlaceHolder,PlaceHolder,PlaceHolder,BACKBUTTON,
            PlaceHolder,AIR,AIR,AIR,AIR,AIR,AIR,AIR,PlaceHolder,
            PlaceHolder,AIR,AIR,AIR,AIR,AIR,AIR,AIR,PlaceHolder,
            PlaceHolder,AIR,AIR,AIR,AIR,AIR,AIR,AIR,PlaceHolder,
            PlaceHolder,AIR,AIR,AIR,AIR,AIR,AIR,AIR,PlaceHolder,
            PlaceHolder,PlaceHolder,PlaceHolder,PlaceHolder,PlaceHolder,PlaceHolder,PlaceHolder,PlaceHolder,PlaceHolder,
    };

    public static Inventory getBerufSkills(Player  player){
        kampfSkills = Bukkit.createInventory(player, 54, "Skills");
        kampfSkills.setContents(CONTENT);
        for (BerufSkills item : BerufSkills.getKlassSkills(KlassChoose.getPlayers(player).getBerufklasse())) {
            setter(item);
        }
        while (kampfSkills.first(AIR) > 0){
            int y = kampfSkills.first(AIR);
            kampfSkills.setItem(y, PlaceHolder);
        }
        return kampfSkills;
    }



    private static void setter(BerufSkills item){
        ItemStack skill = item.getIcon();
        ItemMeta meta = skill.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        lore.add("Benötigtes Level: " + item.getNeededLevel());
        lore.add("Cooldown: " + item.getCooldown());
        if (meta != null) {
            meta.setLore(lore);
        }
        skill.setItemMeta(meta);
        int i = kampfSkills.first(AIR);
        System.out.println(i);
        if (i >= 0) {
            kampfSkills.setItem(i, skill);
        }
    }
}
