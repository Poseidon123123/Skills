package poseidon.skills.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import poseidon.skills.Klassen.KlassChoose;
import poseidon.skills.Klassen.Players;
import poseidon.skills.Skills;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static poseidon.skills.Skills.getInstance;

public class PayCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)){
            sender.sendMessage("Du musst ein Spieler sein!");
            return true;
        }
        if(args.length == 3) {
            if (args[0].equalsIgnoreCase("addMoney")) {
                Player player1 = Bukkit.getPlayerExact(args[1]);
                if (player1 != null) {
                    KlassChoose.getPlayer(player1.getUniqueId()).addMoney(Integer.parseInt(args[2]));
                    return true;
                }
            }
        }
        Player receiver = Bukkit.getPlayerExact(args[0]);
        if(receiver != null){
            int payment = MakeCommand.stringToInt(args[1]);
            if(payment <= 0){
                player.sendMessage(Objects.requireNonNull(getInstance().getConfig().getString("Messages.Pay.lowMoney")));
                return true;
            }
            Players players = KlassChoose.getPlayers(player);
            Players revicers = KlassChoose.getPlayers(receiver);
            if(players.removeMoney(payment)) {
                String b = Objects.requireNonNull(getInstance().getConfig().getString("Messages.Pay.giveMoney"));
                b = b.replace("{name}", receiver.getName());
                b = b.replace("{money}", String.valueOf(payment));
                player.sendMessage(b);
                revicers.addMoney(payment);
                String a = Objects.requireNonNull(getInstance().getConfig().getString("Messages.Pay.reciveMoney"));
                a = a.replace("{name}", player.getName());
                a = a.replace("{money}", String.valueOf(payment));
                receiver.sendMessage(a);
            }
            else {;
                player.sendMessage(Objects.requireNonNull(getInstance().getConfig().getString("Messages.Pay.noMoney")));
            }
        }
        else {
            player.sendMessage(Skills.getInstance().message("Messages.General.noPlayerFound").replace("{name}", args[0]));
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
        if(args.length == 2){
            tabCompleteList.add("<Betrag>");
        }
        return tabCompleteList;
    }
}
