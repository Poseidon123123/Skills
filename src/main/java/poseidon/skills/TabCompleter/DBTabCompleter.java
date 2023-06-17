package poseidon.skills.TabCompleter;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DBTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> tabCompleteList = new ArrayList<>();
        if(args.length == 1){
            tabCompleteList.add("List");
            tabCompleteList.add("addList");
            tabCompleteList.add("generateFile");
            tabCompleteList.add("deletePlayers");
            tabCompleteList.add("Reload");
        }
        else if (args.length == 2){
            if(args[0].equalsIgnoreCase("List") || args[0].equalsIgnoreCase("generateFile")){
                tabCompleteList.add("BerufKlassen");
                tabCompleteList.add("KampfKlassen");
                tabCompleteList.add("KampfSkill");
                tabCompleteList.add("BerufSkill");
                tabCompleteList.add("Mobs");
                tabCompleteList.add("Fish");
                tabCompleteList.add("Mine");
                tabCompleteList.add("Wood");
                tabCompleteList.add("Farm");
            }
            else if (args[0].equalsIgnoreCase("addList")){
                tabCompleteList.add("BerufKlassen");
                tabCompleteList.add("KampfKlassen");
                tabCompleteList.add("KampfSkills");
                tabCompleteList.add("BerufSkills");
            }
            else if(args[0].equalsIgnoreCase("deletePlayer")){
                for (Player player : Bukkit.getServer().getOnlinePlayers()){
                    tabCompleteList.add(player.getName());
                }
            }
        }
        Collections.sort(tabCompleteList);
        return tabCompleteList;
    }
}
