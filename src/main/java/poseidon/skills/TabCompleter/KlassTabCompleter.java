package poseidon.skills.TabCompleter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import poseidon.skills.Klassen.Berufklasse;
import poseidon.skills.Klassen.Kampfklassen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KlassTabCompleter implements TabCompleter {
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
                Kampfklassen.getKlassen().forEach(kampfjsk -> tabCompleteList.add(kampfjsk.getDisplayName()));
            }
        }
        Collections.sort(tabCompleteList);
        return tabCompleteList;
    }
}