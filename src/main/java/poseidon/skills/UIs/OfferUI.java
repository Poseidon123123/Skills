package poseidon.skills.UIs;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import poseidon.skills.GUI.OfferGUI;
import poseidon.skills.Klassen.KlassChoose;
import poseidon.skills.Skills;

import java.util.Objects;

public class OfferUI extends UI{
    private boolean isOpen;
    private final Player user;
    private final ItemStack tradeOffer;
    private final int offer;
    public OfferUI(Player answer, Player user, ItemStack tradeOffer, int offer) {
        super(answer);
        this.tradeOffer = tradeOffer;
        this.offer = offer;
        this.user = user;
        this.inv = OfferGUI.getOfferGUI(tradeOffer,offer, user.getName());
        this.isOpen = true;
        answer.openInventory(this);
    }

    @Override
    public String getTitle() {
        return "Test";
    }

    @Override
    public String getOriginalTitle() {
        return getTitle();
    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        if(Objects.requireNonNull(event.getCurrentItem()).isSimilar(OfferGUI.no)){
            cancelFunktion();
        }
        if(event.getCurrentItem().isSimilar(OfferGUI.yes)){
            isOpen = false;
            this.player.closeInventory();
            if(KlassChoose.getPlayers(player).removeMoney(offer)) {
                user.sendMessage(Objects.requireNonNull(Skills.getInstance().getConfig().getString("Messages.Offer.OfferAccepted")));
                user.getInventory().remove(getTradeOffer());
                player.getInventory().addItem(getTradeOffer());
                KlassChoose.getPlayers(user).addMoney(offer);
            }
            else {
                player.sendMessage(Objects.requireNonNull(Skills.getInstance().getConfig().getString("Messages.Offer.noMoney")));
                cancelFunktion();
            }
        }
    }

    @Override
    public void invClose(InventoryCloseEvent event) {
        if(event.getView() instanceof OfferUI) {
            if (isOpen) {
                cancelFunktion();
            }
        }
    }

    public ItemStack getTradeOffer() {
        return tradeOffer;
    }
    private void cancelFunktion(){
        isOpen = false;
        this.player.closeInventory();
        user.sendMessage(Objects.requireNonNull(Skills.getInstance().getConfig().getString("Messages.Offer.OfferRejected")));
    }

    public int getOffer() {
        return offer;
    }
}
