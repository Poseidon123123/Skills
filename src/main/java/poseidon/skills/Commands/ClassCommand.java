package poseidon.skills.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import poseidon.skills.Klassen.Berufklasse;
import poseidon.skills.Klassen.Kampfklassen;
import poseidon.skills.Klassen.KlassChoose;

public class ClassCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(!(commandSender instanceof Player player)){
            System.out.println(ChatColor.RED + "You need to be a Player");
        }
        else {
            if(!(args.length == 2)){
                player.sendMessage(ChatColor.RED + "Zum richtigen Benutzen des Commands /class [Beruf/Kampf] [Klasse] eingeben");
            }
            else {
                if(args[0].equalsIgnoreCase("Beruf")) {
                    if (KlassChoose.getPlayers(player).getBerufklasse().equals(Berufklasse.Unchosed)) {
                        if (Berufklasse.isOnArray(args[1])) {
                            Berufklasse klassen = Berufklasse.getOfArray(args[1]);
                            KlassChoose.getPlayers(player).setBerufklasse(klassen);
                            player.sendMessage(ChatColor.BLUE + "Du bist nun ein " + klassen.getDisplayName());
                        } else{
                            player.sendMessage(args[0]);
                            player.sendMessage(args[1]);
                            player.sendMessage("Error");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Du hast schon eine Klasse ausgewählt");
                    }
                }
                else if(args[0].equalsIgnoreCase("Kampf")) {
                    if (KlassChoose.getPlayers(player).getKampfklasse().equals(Kampfklassen.Unchosed)) {
                        if (Kampfklassen.isOnArray(args[1])) {
                            Kampfklassen klasse = Kampfklassen.getOfArray(args[1]);
                            KlassChoose.getPlayers(player).setKampfklasse(klasse);
                            player.sendMessage(ChatColor.BLUE + "Du bist nun ein " + klasse.getDisplayName());
                        } else {
                            player.sendMessage(args[0]);
                            player.sendMessage(args[1]);
                            player.sendMessage("Error");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Du hast schon eine Klasse ausgewählt");
                    }
                }
            }
        }
        return true;
    }
}
