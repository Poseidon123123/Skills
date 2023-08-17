package poseidon.skills.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import poseidon.skills.Klassen.Berufklasse;
import poseidon.skills.Klassen.Kampfklassen;
import poseidon.skills.Klassen.KlassChoose;
import poseidon.skills.Klassen.Players;

import java.util.ArrayList;
import java.util.List;
//TODO custom Messages
public class SetClassCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 3){
            Player player = Bukkit.getPlayer(args[0]);
            if(!(player == null)){
                Players players = KlassChoose.getPlayers(player);
                if(players!= null){
                    if(args[1].equalsIgnoreCase("Beruf")){
                        if(!players.getBerufklasse().equals(Berufklasse.Unchosed)){
                            sender.sendMessage("Der Spieler hat schon einen Beruf");
                        }
                        else if(Berufklasse.isOnArray(args[2])){
                            Berufklasse berufklasse = Berufklasse.getOfArray(args[2]);
                            players.setBerufklasse(berufklasse);
                            sender.sendMessage("Die Berufsklasse von " + player.getName() + " ist nun " + berufklasse.getDisplayName());
                        }
                        else {
                            sender.sendMessage("Beruf nicht gefunden");
                        }
                    }
                    else if(args[1].equalsIgnoreCase("Kampf")){
                        if(!players.getKampfklasse().equals(Kampfklassen.Unchosed)) {
                            sender.sendMessage("Der Spieler hat schon eine Kampfklasse");
                        }
                        else if(Kampfklassen.isOnArray(args[2])) {
                            Kampfklassen kampfklassen = Kampfklassen.getOfArray(args[2]);
                            players.setKampfklasse(kampfklassen);
                            sender.sendMessage("Die Kampfklasse von " + player.getName() + " ist nun " + kampfklassen.getDisplayName());
                        }
                        else {
                            sender.sendMessage("Kampfklasse nicht gefunden");
                        }
                    }
                }
                else {
                    sender.sendMessage("Command falsch benutzt nutze /setClass [Name] Beruf/Kampf [Klasse]");
                }
            }
            else {
                sender.sendMessage("Spieler nicht auf dem Server regisiteriert");
            }
        }
        else {
            sender.sendMessage("Spieler nicht gefunden");
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ArrayList<String> tabComplete = new ArrayList<>();
        if(args.length == 1) {
            Bukkit.getOnlinePlayers().forEach(player -> tabComplete.add(player.getName()));
        }
        if(args.length == 2){
            tabComplete.add("Kampf");
            tabComplete.add("Beruf");
        }
        if(args.length == 3){
            if(args[1].equalsIgnoreCase("Kampf")){
                Kampfklassen.getKlassen().forEach(kampfklassen -> tabComplete.add(kampfklassen.getDisplayName()));
            }
            if(args[1].equalsIgnoreCase("Beruf")){
                Berufklasse.getTest().forEach(kampfklassen -> tabComplete.add(kampfklassen.getDisplayName()));
            }
        }
        return tabComplete;
    }
}
