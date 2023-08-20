package poseidon.skills.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import poseidon.skills.Klassen.Berufklasse;
import poseidon.skills.Klassen.Kampfklassen;
import poseidon.skills.Klassen.KlassChoose;
import poseidon.skills.Klassen.Players;
import poseidon.skills.Skills;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ClassCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(!(commandSender instanceof Player player)){
            System.out.println(ChatColor.RED + "You need to be a Player");
        }
        else {
            if((args.length == 2)){
                Players players = KlassChoose.getPlayers(player);
                if(args[0].equalsIgnoreCase("Beruf")) {
                    if(players.berufChangable()) {
                        if (Berufklasse.isOnArray(args[1])) {
                            Berufklasse klassen = Berufklasse.getOfArray(args[1]);
                            players.setBerufklasse(klassen);
                            players.berufChanged();
                            String a = Objects.requireNonNull(Skills.getInstance().getConfig().getString("Messages.Class.BerufSuccsess"));
                            a = a.replace("{Beruf}", klassen.getDisplayName());
                            player.sendMessage(a);
                        } else {
                            player.sendMessage(Objects.requireNonNull(Skills.getInstance().getConfig().getString("Messages.Class.noClassFouned")));
                        }
                    }
                    else {
                        player.sendMessage(Skills.getInstance().message("Messages.Class.NoTime"));
                    }
                }
                else if(args[0].equalsIgnoreCase("Kampf")) {
                    if(players.kampfChangable()) {
                        if (Kampfklassen.isOnArray(args[1])) {
                            Kampfklassen klasse = Kampfklassen.getOfArray(args[1]);
                            players.setKampfklasse(klasse);
                            players.kampfChanged();
                            String a = Objects.requireNonNull(Skills.getInstance().getConfig().getString("Messages.Class.KampfSuccsess"));
                            a = a.replace("{Kampf}", klasse.getDisplayName());
                            player.sendMessage(a);
                        } else {
                            player.sendMessage(Objects.requireNonNull(Skills.getInstance().getConfig().getString("Messages.Class.noClassFouned")));
                        }
                    }
                    else {
                        player.sendMessage(Skills.getInstance().message("Messages.Class.NoTime"));
                    }
                }
            }
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
        List<String> tabCompleteList = new ArrayList<>();
        if(args.length == 1){
            tabCompleteList.add("Beruf");
            tabCompleteList.add("Kampf");
        }
        else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("Beruf")) {
                Berufklasse.getTest().forEach((klassen) -> tabCompleteList.add(klassen.getDisplayName()));
            }
            else if(args[0].equalsIgnoreCase("Kampf")){
                Kampfklassen.getKlassen().forEach(kampfleck -> tabCompleteList.add(kampfleck.getDisplayName()));
            }
        }
        Collections.sort(tabCompleteList);
        return tabCompleteList;
    }
}
