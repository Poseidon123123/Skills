package poseidon.skills.Listners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import poseidon.skills.CooldownSystem;
import poseidon.skills.JSON.JSONLoad;
import poseidon.skills.JSON.JSONSave;
import poseidon.skills.Klassen.Berufklasse;
import poseidon.skills.Klassen.Kampfklassen;
import poseidon.skills.Klassen.KlassChoose;
import poseidon.skills.Klassen.Players;
import poseidon.skills.ManaMap;
import poseidon.skills.Skills;
import poseidon.skills.UIs.UI;
import poseidon.skills.citys.City;
import poseidon.skills.citys.CityMapper;
import poseidon.skills.skill.BerufSkills;
import poseidon.skills.skill.KampfSkills;
import poseidon.skills.skill.SkillMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static poseidon.skills.Klassen.KlassChoose.getPlayers;

public class Testlistener implements Listener {
    private void playerJoinFunktion(Players players){
        ManaMap.getMana().putIfAbsent(players.getPlayer(), players.getKampfLevel() + players.getKampfklasse().getBaseMana());
        invReset(players.getPlayer());
        new ManaMap(players.getPlayer()).refreshManaBar();
        CooldownSystem cooldownSystem = CooldownSystem.registerPlayer(players.getPlayer());
        if(cooldownSystem != null){
            cooldownSystem.startCooldowns();
        }
    }

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
        playerJoinFunktion(players);
        if(!getPlayers(player).getBerufklasse().equals(Berufklasse.Unchosed)){
            event.setJoinMessage(ChatColor.BLUE + "Willkommen zurück, " + player.getName());
            player.performCommand("Sk");
        }
        else if(!getPlayers(player).getKampfklasse().equals(Kampfklassen.Unchosed)){
            event.setJoinMessage(ChatColor.BLUE + "Willkommen zurück, " + player.getName());
            player.performCommand("Sk");
        }
        else {
            event.setJoinMessage(ChatColor.DARK_RED + "Heißt " + player.getName() + " Herzlich Willkommen");
            player.sendMessage(ChatColor.BLUE + "Willkommen auf dem Server, " + player.getName());
        }
    }

    public void invReset(Player player){
        for(ItemStack item : player.getInventory()){
            if(item == null){
                continue;
            }
            for (KampfSkills b : SkillMapper.getKampfSkills()) {
                if (item.isSimilar(b.getIcon())) {
                    if(item.getAmount() != 1){
                        item.setAmount(1);
                    }
                }
            }
            for (BerufSkills b : SkillMapper.getBerufSkills()) {
                if (item.isSimilar(b.getIcon())) {
                    if(item.getAmount() != 1){
                        item.setAmount(1);
                    }
                }
            }
        }
    }

    public static void PlayerLeaveAction(Player player){
        JSONSave.playerSave(player);

    }

    @EventHandler
    public void PlayerLeave(PlayerQuitEvent event){
        PlayerLeaveAction(event.getPlayer());
    }

    @EventHandler
    public void PlayerLeave(PlayerKickEvent event){
        PlayerLeaveAction(event.getPlayer());
    }


    @EventHandler
    public void PlayerDeath(PlayerDeathEvent event){
        List<ItemStack> deleteList = new ArrayList<>();
        for(ItemStack item : event.getDrops()){
            if(item == null){
                continue;
            }
            boolean find = false;
            for (KampfSkills b : SkillMapper.getKampfSkills()) {
                if (item.isSimilar(b.getIcon())) {
                    deleteList.add(item);
                    find = true;
                    break;
                }
            }
            if(find){
                continue;
            }
            for (BerufSkills b : SkillMapper.getBerufSkills()) {
                if (item.isSimilar(b.getIcon())) {
                    deleteList.add(item);
                    break;
                }
            }
        }
        if(!deleteList.isEmpty()){
            for (ItemStack itemStack : deleteList){
                event.getDrops().remove(itemStack);
            }
        }
    }

    @EventHandler
    public void invClose(InventoryCloseEvent event){
        if(event.getView() instanceof UI ui){
            ui.invClose(event);
        }
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


    @EventHandler
    public void onBreak(BlockBreakEvent event){
        if(CityMapper.ChunkClaimed(event.getBlock().getChunk())){
            event.setCancelled(true);
            City city = CityMapper.whoClaimed(event.getBlock().getChunk());
            for(UUID uuid : city.getBuergerUUID()){
                if(uuid.equals(event.getPlayer().getUniqueId())){
                    event.setCancelled(false);
                    break;
                }
            }
            if(event.isCancelled()){
                event.getPlayer().sendMessage(Objects.requireNonNull(Skills.getInstance().getConfig().getString("Event.NotInCity.Break")));
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        if(CityMapper.ChunkClaimed(event.getBlock().getChunk())){
            event.setCancelled(true);
            City city = CityMapper.whoClaimed(event.getBlock().getChunk());
            for(UUID uuid : city.getBuergerUUID()){
                if(uuid.equals(event.getPlayer().getUniqueId())){
                    event.setCancelled(false);
                    break;
                }
            }
            if(event.isCancelled()){
                event.getPlayer().sendMessage(Objects.requireNonNull(Skills.getInstance().getConfig().getString("Event.NotInCity.Place")));
            }
        }
    }
}
