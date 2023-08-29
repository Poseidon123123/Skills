package poseidon.skills.UIs;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import poseidon.skills.GUI.BerufSkillGUI;
import poseidon.skills.GUI.KampfSkillsGUI;
import poseidon.skills.Klassen.KlassChoose;
import poseidon.skills.Klassen.Players;
import poseidon.skills.skill.BerufSkills;

import java.util.Objects;

public class BerufSkillUI extends UI{

    public BerufSkillUI(Player player) {
        super(player);
        this.inv = BerufSkillGUI.getBerufSkills(player);
        player.openInventory(this);
    }


    @Override
    public String getTitle() {
        return "BerufSkills";
    }

    @Override
    public String getOriginalTitle() {
        return "BerufSkills";
    }

    @Override
    public void setTitle(String s) {
    }

    public void onClick(InventoryClickEvent event) {
        if(event.getView() instanceof BerufSkillUI){
            event.setCancelled(true);
            if(Objects.equals(event.getCurrentItem(), KampfSkillsGUI.BACKBUTTON)){
                new SkillUI((Player) event.getWhoClicked());
            }
            ItemStack item = event.getCurrentItem();
            BerufSkills skill = BerufSkills.getSkills(item);
            Players players = KlassChoose.getPlayers(player);
            BerufSkills kampfSkills = players.getBoundBeruf();
            if(kampfSkills != null){
                if(kampfSkills.equals(skill)){
                    player.sendMessage(ChatColor.RED + "Itembindung gelöscht");
                    players.setBerufItemSkill(null);
                    players.setBoundBeruf(null);
                }
            }
            if(skill != null){
                if(Objects.requireNonNull(skill).getBerufklasse().equals(KlassChoose.getPlayers(player).getBerufklasse())) {
                    if(skill.getNeededLevel() <= KlassChoose.getPlayers(player).getBerufLevel()) {
                        new BerufSlotUI(player, skill);
                    }
                    else {
                        player.sendMessage(ChatColor.RED + "Zu niedriges Level");
                    }
                }
            }
        }
    }

    @Override
    public void invClose(InventoryCloseEvent event) {

    }
}
