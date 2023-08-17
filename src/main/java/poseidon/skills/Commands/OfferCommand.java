package poseidon.skills.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import poseidon.skills.UIs.OfferUI;
import poseidon.skills.UIs.TradeUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static poseidon.skills.Skills.getInstance;

public class OfferCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)){
            sender.sendMessage("Du musst ein Spieler sein");
            return true;
        }
        if(args.length >= 2) {
            Player answer = Bukkit.getPlayerExact(args[1]);
            if (answer == null) {
                String a = Objects.requireNonNull(getInstance().getConfig().getString("Messages.noPlayerFound"));
                a.replace("{name}", args[1]);
                player.sendMessage(a);
                return false;
            }
            if(args.length == 3) {
                if (args[0].equalsIgnoreCase("Money")) {
                    if (player.getInventory().getItem(EquipmentSlot.HAND) == null || Objects.requireNonNull(player.getInventory().getItem(EquipmentSlot.HAND)).getType() == Material.AIR) {
                        player.sendMessage(Objects.requireNonNull(getInstance().getConfig().getString("Messages.Offer.noItemInHand")));
                    } else {
                        int a = MakeCommand.stringToInt(args[2], player);
                        ItemStack tradeOffer = player.getInventory().getItem(EquipmentSlot.HAND);
                        new OfferUI(answer, player, tradeOffer, a);
                    }
                }
            }
            if(args.length == 2) {
                if (args[0].equalsIgnoreCase("Item")) {
                    TradeUI.aktiveTrade(player,answer);
                }
            }
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tabCompleteList = new ArrayList<>();
        if(args.length == 1) {
            tabCompleteList.add("Money");
            tabCompleteList.add("Item");
        }
        if(args.length == 2){
            Bukkit.getOnlinePlayers().forEach(player -> tabCompleteList.add(player.getName()));
        }
        if(args.length == 3 && args[0].equalsIgnoreCase("Money")){
            tabCompleteList.add("<Betrag>");
        }
        return tabCompleteList;
    }
}
