package poseidon.skills.GUI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import poseidon.skills.Klassen.KlassChoose;
import poseidon.skills.skill.KampfSkills;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class KampfSkillsGUI {
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

    public static Inventory getKampfSkills(Player  player){
        kampfSkills = Bukkit.createInventory(player, 54, "Skills");
        kampfSkills.setContents(CONTENT);
        ArrayList<KampfSkills> b = KampfSkills.getKlassSkills(KlassChoose.getPlayers(player).getKampfklasse());
        Collections.sort(b, Comparator.comparing(KampfSkills::getNeededLevel));
        for (KampfSkills item : b) {
            setter(item);
        }
        while (kampfSkills.first(AIR) > 0){
            int y = kampfSkills.first(AIR);
            kampfSkills.setItem(y, PlaceHolder);
        }
        return kampfSkills;
    }

    private static void setter(KampfSkills item){
        ItemStack skill = item.getIcon();
        int i = kampfSkills.first(AIR);
        if (i >= 0) {
            kampfSkills.setItem(i, skill);
        }
    }
}
