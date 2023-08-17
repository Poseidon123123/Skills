package poseidon.skills.UIs;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import poseidon.skills.GUI.tradeGUI;
import poseidon.skills.Skills;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class TradeUI extends UI {
    private final HashMap<Player, TradeUI> map = new HashMap<>();
    private final Player trade;
    private boolean isOpen;
    private boolean isTimer;
    private final List<Integer> freeSlots = List.of(10,11,12,19,20,21,28,29,30,37,38,39);
    private final List<Integer> otherSlots = List.of(14,15,16,23,24,25,32,33,34,41,42,43);


    public TradeUI(Player player, Player trader) {
        super(player);
        this.inv = tradeGUI.getTradeGUI(player,trader);
        this.trade = trader;
        this.isOpen = true;
        this.isTimer = false;
        player.openInventory(this);
        map.put(player, this);

    }
    public static void aktiveTrade(Player trader1, Player trader2){
        new TradeUI(trader1,trader2);
        new TradeUI(trader2,trader1);
    }

    @Override
    public String getTitle() {
        return "null";
    }

    @Override
    public String getOriginalTitle() {
        return "null";
    }

    @Override
    public void setTitle(String title) {
    }

    public void toggleAccept(){
        List<Integer> freeSlots1 = List.of(50,51,52);
        if(Objects.equals(inv.getItem(50), tradeGUI.no)) {
            for (int a : freeSlots1) {
                inv.setItem(a, tradeGUI.yes);
            }
            if(Objects.equals(inv.getItem(46), tradeGUI.yes)){
                this.isTimer = true;
            }
        }
        else {
            for (int a : freeSlots1) {
                inv.setItem(a, tradeGUI.no);
                this.isTimer = false;
            }
        }
    }

    public void stopTrade(){
        if(isOpen) {
            this.isOpen = false;
            player.closeInventory();
            player.sendMessage(ChatColor.RED + "Der Handel wurde abgebrochen!");
            List<ItemStack> needToAdd = new ArrayList<>();
            freeSlots.forEach(integer -> needToAdd.add(inv.getItem(integer)));
            if(!needToAdd.isEmpty()) {
                while (needToAdd.contains(tradeGUI.placer)){
                    needToAdd.remove(tradeGUI.placer);
                }
                if(!needToAdd.isEmpty()) {
                    needToAdd.forEach(itemStack -> player.getInventory().addItem(itemStack));
                }
            }
        }
    }

    @Override
    public void onClick(@NotNull InventoryClickEvent event) {
        event.setCancelled(true);
        if(event.getView().getBottomInventory().equals(event.getClickedInventory())){
            event.setCancelled(false);
        }
        else{
            int slot = event.getSlot();
            if(freeSlots.contains(slot)){
                event.setCancelled(false);
                if(event.getCursor() != null || event.getCurrentItem() != null){
                    other().removeDisplayItem(slot);
                    other().displayItem(slot);
                }
                else if(event.getCurrentItem() != null){
                    other().displayItem(slot);
                }
                else if(event.getCursor() != null){
                    other().removeDisplayItem(slot);
                }
                return;
            }
            List<Integer> freeSlots1 = List.of(46,47,48);
            if(freeSlots1.contains(slot)){
                if(Objects.requireNonNull(event.getCurrentItem()).equals(tradeGUI.no)) {
                    for (int a : freeSlots1) {
                        inv.setItem(a, tradeGUI.yes);
                        other().toggleAccept();
                    }
                }
                else {
                    for (int a : freeSlots1) {
                        inv.setItem(a, tradeGUI.no);
                        other().toggleAccept();
                        this.isTimer = false;
                    }
                }
            }
            if(isTimer){
                int x = 0;
                List<Integer> taskIdList = new ArrayList<>();
                while (x<6) {
                    taskIdList.add(Skills.getInstance().getServer().getScheduler().runTaskLater(Skills.getInstance(), () -> {
                        if (isTimer) {
                            if(inv.first(tradeGUI.loadred) > 0){
                                inv.setItem(inv.first(tradeGUI.loadred),tradeGUI.loadgreen);
                            }
                            else {
                                successTrade();
                            }
                        }
                        else {
                            taskIdList.forEach(integer -> Skills.getInstance().getServer().getScheduler().cancelTask(integer));
                            while (inv.first(tradeGUI.loadgreen) > 0){
                                inv.setItem(inv.first(tradeGUI.loadgreen), tradeGUI.loadred);
                            }
                        }
                    }, x* 20L).getTaskId());
                    x++;
                }
            }
        }
    }

    public void successTrade(){
        if(isOpen){
            this.isOpen = false;
            other().successTrade();
            player.closeInventory();
            List<ItemStack> needToAdd = new ArrayList<>();
            otherSlots.forEach(integer -> needToAdd.add(inv.getItem(integer)));
            while (needToAdd.contains(tradeGUI.placer)){
                needToAdd.remove(tradeGUI.placer);
            }
            if(!needToAdd.isEmpty()) {
                needToAdd.forEach(itemStack -> player.getInventory().addItem(itemStack));
            }
            System.out.println("Success");
        }
    }
    @Override
    public void invClose(InventoryCloseEvent event) {
        if(event.getView() instanceof TradeUI ui){
            if(ui.other().isOpen()) {
                ui.other().stopTrade();
            }
        }
    }
    public void displayItem(int a){
        Skills.getInstance().getServer().getScheduler().runTaskLater(Skills.getInstance(), () -> {
            int b = a + 4;
            ItemStack iv = other().inv.getItem(a);
            inv.setItem(b,iv);
        },1);
    }
    public void removeDisplayItem(int i){
        int b = i + 4;
        inv.setItem(b, tradeGUI.placer);
    }

    public TradeUI other(){
        return map.get(trade);
    }

    public boolean isOpen() {
        return isOpen;
    }
}
