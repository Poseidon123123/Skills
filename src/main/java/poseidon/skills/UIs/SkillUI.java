package poseidon.skills.UIs;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import poseidon.skills.GUI.SkillGUI;

import java.util.Objects;


public class SkillUI extends UI {

    public SkillUI(Player player) {
        super(player);
        this.inv = SkillGUI.getSkillGUI(player);
        player.openInventory(this);
    }

    @Override
    public String getTitle() {
        return "Skills";
    }

    @Override
    public String getOriginalTitle() {
        return getTitle();
    }

    @Override
    public void setTitle(String s) {

    }

    public void onClick(InventoryClickEvent event){
        if(event.getView() instanceof SkillUI){
            event.setCancelled(true);
            ItemStack Clicked = event.getCurrentItem();
            ItemStack button1 = SkillGUI.SIGN3;
            ItemStack button2 = SkillGUI.SIGN4;
            if(Objects.equals(Clicked, button1)){
                new BerufSkillUI((Player) event.getWhoClicked());
                return;
            }
            if(Objects.equals(Clicked, button2)){
                new KampfSkillUI((Player) event.getWhoClicked());
            }
        }
    }

    @Override
    public void invClose(InventoryCloseEvent event) {

    }
}
