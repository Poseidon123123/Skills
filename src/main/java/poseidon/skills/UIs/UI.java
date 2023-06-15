package poseidon.skills.UIs;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public abstract class UI extends InventoryView {
    protected Player player;
    protected Inventory inv;
    public UI(Player player){
        this.player = player;
    }

    @Override
    public Inventory getTopInventory() {
        return inv;
    }

    @Override
    public Inventory getBottomInventory() {
        return player.getInventory();
    }

    @Override
    public HumanEntity getPlayer() {
        return player;
    }

    @Override
    public InventoryType getType() {
        return InventoryType.CHEST;
    }

    @Override
    public abstract String getTitle();

    @Override
    public abstract String getOriginalTitle();
    @Override
    public abstract void setTitle(String title);

    public abstract void onClick(InventoryClickEvent event);

}
