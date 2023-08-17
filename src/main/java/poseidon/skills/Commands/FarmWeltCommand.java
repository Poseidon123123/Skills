package poseidon.skills.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import poseidon.skills.Farmwelt;

import java.util.ArrayList;
import java.util.List;

public class FarmWeltCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player))      {
            sender.sendMessage("Du musst ein Spieler sein");
            return true;
        }
        Farmwelt farmwelt;
        if(Farmwelt.getFromMap(player) == null) {
            farmwelt = new Farmwelt(player);
        }
        else {
            farmwelt = Farmwelt.getFromMap(player);
        }
        if(args.length == 1) {
            if(player.hasPermission("Skills.command.farmwelt.create")) {
                if (args[0].equalsIgnoreCase("generate")) {
                    Farmwelt.generateNewFarmwelt();
                }
            }
        }
        if(args.length == 1) {
            if (args[0].equalsIgnoreCase("tp")) {
                farmwelt.tpFarm();
            }
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tabComplete = new ArrayList<>();
        if(args.length == 1){
            tabComplete.add("tp");
        }
        return tabComplete;
    }
}
