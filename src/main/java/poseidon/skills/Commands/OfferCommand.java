package poseidon.skills.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import poseidon.skills.UIs.TradeUI;

import java.util.ArrayList;
import java.util.List;

import static poseidon.skills.Skills.getInstance;

public class OfferCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)){
            sender.sendMessage("Du musst ein Spieler sein");
            return true;
        }
        if(args.length == 1) {
            Player answer = Bukkit.getPlayerExact(args[0]);
            if (answer == null) {
                player.sendMessage(getInstance().message("Messages.General.noPlayerFound").replace("{name}", args[0]));
                return true;
            }
            TradeUI.aktiveTrade(player,answer);
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tabCompleteList = new ArrayList<>();
        if(args.length == 1){
            Bukkit.getOnlinePlayers().forEach(player -> tabCompleteList.add(player.getName()));
        }
        return tabCompleteList;
    }
}
