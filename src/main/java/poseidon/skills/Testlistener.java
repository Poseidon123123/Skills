package poseidon.skills;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import poseidon.skills.JSON.JSONLoad;
import poseidon.skills.JSON.JSONSave;
import poseidon.skills.Klassen.*;
import poseidon.skills.UIs.UI;
import poseidon.skills.skill.*;

import java.util.Objects;

import static poseidon.skills.Klassen.KlassChoose.getPlayers;

public class Testlistener implements Listener {

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        Players players;
        if(KlassChoose.getPlayers(player) == null) {
            players = JSONLoad.load(player);
        }
        else {
            players = KlassChoose.getPlayers(player);
        }
        KlassChoose.addPlayers(players, player);
        if(getPlayers(player).getBerufklasse().equals(Berufklasse.Unchosed)){
            player.sendMessage(ChatColor.BLUE + "Willkommen auf dem Server, " + player.getName());
            event.setJoinMessage(ChatColor.DARK_RED + "Heißt " + player.getName() + " Herzlich Willkommen");
        }
        else {
            event.setJoinMessage(ChatColor.BLUE + "Willkommen zurück, " + player.getName());
            player.performCommand("Sk");
        }
    }

    @EventHandler
    public void PlayerLeave(PlayerQuitEvent event){
        JSONSave.playerSave(event.getPlayer());
    }

    @EventHandler
    public void PlayerLeave(PlayerKickEvent event){
        JSONSave.playerSave(event.getPlayer());
    }


    @EventHandler(ignoreCancelled = true)
    public void onClickSkillGUI(InventoryClickEvent event) {
        if(event.getView() instanceof UI ui){
            ui.onClick(event);
            return;
        }
        if(Objects.equals(event.getClickedInventory(), event.getWhoClicked().getInventory())){
            for(KampfSkills k : SkillMapper.getKampfSkills()){
                if(k.getIcon().isSimilar(event.getCurrentItem())){
                    event.setCancelled(true);
                    if(event.getClick().isRightClick()) {
                        int b = Objects.requireNonNull(event.getClickedInventory()).first(Objects.requireNonNull(event.getCurrentItem()));
                        if (b != -1) {
                            Objects.requireNonNull(event.getClickedInventory()).remove(k.getIcon());
                            Objects.requireNonNull(event.getClickedInventory()).setItem(b, new ItemStack(Material.AIR));
                        }
                    }
                    break;
                }
            }
            for(BerufSkills k : SkillMapper.getBerufSkills()){
                if(k.getIcon().isSimilar(event.getCurrentItem())){
                    event.setCancelled(true);
                    if(event.getClick().isRightClick()) {
                        int b = Objects.requireNonNull(event.getClickedInventory()).first(Objects.requireNonNull(event.getCurrentItem()));
                        if (b != -1) {
                            Objects.requireNonNull(event.getClickedInventory()).remove(k.getIcon());
                            Objects.requireNonNull(event.getClickedInventory()).setItem(b, new ItemStack(Material.AIR));
                        }
                    }
                    break;
                }
            }
        }
    }




    @EventHandler(ignoreCancelled = true)
    public void onDrag(InventoryDragEvent event) {
        if (event.getView() instanceof UI) {
            event.setCancelled(true);
            return;
        }
        if (Objects.equals(event.getInventory(), event.getWhoClicked().getInventory())) {
            for (KampfSkills k : SkillMapper.getKampfSkills()) {
                if (k.getIcon().isSimilar(event.getCursor())) {
                    event.setCancelled(true);
                    return;
                }
            }
            for (BerufSkills k : SkillMapper.getBerufSkills()) {
                if (k.getIcon().isSimilar(event.getCursor())) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
}
