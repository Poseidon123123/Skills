package poseidon.skills.UIs;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import poseidon.skills.GUI.BerufSkillGUI;
import poseidon.skills.GUI.KampfSkillsGUI;
import poseidon.skills.Klassen.KlassChoose;
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
        if(event.getView() instanceof BerufSkillUI){
            event.setCancelled(true);
            if(Objects.equals(event.getCurrentItem(), KampfSkillsGUI.BACKBUTTON)){
                new SkillUI((Player) event.getWhoClicked());
            }
            ItemStack item = event.getCurrentItem();
            if(BerufSkills.getSkills(item) != null){
                BerufSkills skill = BerufSkills.getSkills(item);
                if(Objects.requireNonNull(skill).getBerufklasse().equals(KlassChoose.getPlayers(player).getBerufklasse())) {
                    if(skill.getNeededLevel() <= KlassChoose.getPlayers(player).getBerufLevel()) {
                        new BerufSlotUI(player, skill.getIcon());
                    }
                    else {
                        player.sendMessage(ChatColor.RED + "Zu niedriges Level");
                    }
                }
            }
        }
    }
}
