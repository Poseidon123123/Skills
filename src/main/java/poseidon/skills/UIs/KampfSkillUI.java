package poseidon.skills.UIs;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import poseidon.skills.GUI.KampfSkillsGUI;
import poseidon.skills.Klassen.KlassChoose;
import poseidon.skills.skill.KampfSkills;

import java.util.Objects;

public class  KampfSkillUI extends UI{

    public KampfSkillUI(Player player) {
        super(player);
        this.inv = KampfSkillsGUI.getKampfSkills(player);
        player.openInventory(this);
    }


    @Override
    public String getTitle() {
        return "KampfSkills";
    }

    @Override
    public String getOriginalTitle() {
        return "KampfSkills";
    }

    @Override
    public void setTitle(String s) {
    }

    public void onClick(InventoryClickEvent event) {
        if(event.getView() instanceof KampfSkillUI){
            event.setCancelled(true);
            if(Objects.equals(event.getCurrentItem(), KampfSkillsGUI.BACKBUTTON)){
                new SkillUI((Player) event.getWhoClicked());
            }
            ItemStack item = event.getCurrentItem();
            if(KampfSkills.getSkills(item) != null){
                KampfSkills skill = KampfSkills.getSkills(item);
                if(Objects.requireNonNull(skill).getKampfKlasse().equals(KlassChoose.getPlayers(player).getKampfklasse())) {
                    if(skill.getNeededLevel() <= KlassChoose.getPlayers(player).getBerufLevel()) {
                        new KampfSlotUI(player, skill.getIcon());
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
