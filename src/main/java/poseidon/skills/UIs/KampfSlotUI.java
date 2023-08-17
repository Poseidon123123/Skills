package poseidon.skills.UIs;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import poseidon.skills.GUI.SlotGUI;
import poseidon.skills.skill.BerufSkills;
import poseidon.skills.skill.KampfSkills;
import poseidon.skills.skill.SkillMapper;

public class KampfSlotUI extends UI{

    public KampfSlotUI(Player player, ItemStack itemStack) {
        super(player);
        this.inv = SlotGUI.getSlotGUI(player);
        player.openInventory(this);
        item = itemStack;
    }

    private static ItemStack item;

    @Override
    public String getTitle() {
        return "Wähle einen Slot";
    }

    @Override
    public String getOriginalTitle() {
        return "Wähle einen Slot";
    }

    @Override
    public void setTitle(String title) {

    }

    public void onClick(InventoryClickEvent event) {
        if(event.getView() instanceof KampfSlotUI){
            event.setCancelled(true);
            ItemStack itemStack = event.getCurrentItem();
            assert itemStack != null;
            if(itemStack.equals(SlotGUI.Back)){
                new KampfSkillUI(player);
            }

            int i = 0;
            for (ItemStack a : SlotGUI.Slotlist) {
                i ++;
                if(a.equals(itemStack)){
                    for (KampfSkills b : SkillMapper.getKampfSkills()) {
                        if(b.getIcon().equals(player.getInventory().getItem(i))){
                            player.sendMessage("Hier liegt schon ein Skill");
                            new KampfSkillUI(player);
                            return;
                        }
                    }
                    for(BerufSkills b : SkillMapper.getBerufSkills()){
                        if(b.getIcon().equals(player.getInventory().getItem(i))){
                            player.sendMessage("Hier liegt schon ein Skill");
                            new BerufSkillUI(player);
                            return;
                        }
                    }
                   for(ItemStack c : player.getInventory().getContents()){
                       if(c == null){
                           continue;
                       }
                        if(c.equals(item)){
                            player.sendMessage("Du hast diesen Skill schon belegt");
                            new KampfSkillUI(player);
                            return;
                        }
                    }
                    player.getInventory().setItem(i, item);
                    new KampfSkillUI(player);
                    break;
                }

            }
        }
    }

    @Override
    public void invClose(InventoryCloseEvent event) {
    }
}
