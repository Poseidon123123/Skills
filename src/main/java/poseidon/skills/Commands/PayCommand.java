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
            return false;
        }
        Player receiver = Bukkit.getPlayerExact(args[0]);
        if(receiver != null){
            int payment = MakeCommand.stringToInt(args[1]);
            if(payment <= 0){
                player.sendMessage(Objects.requireNonNull(getInstance().getConfig().getString("Messages.Pay.lowMoney")));
                return false;
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
            String a = Objects.requireNonNull(Skills.getInstance().getConfig().getString("Messages.noPlayerFound"));
            a = a.replace("{name}", args[0]);
            player.sendMessage(a);
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
