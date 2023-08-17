package poseidon.skills.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import poseidon.skills.Klassen.Berufklasse;
import poseidon.skills.Klassen.Kampfklassen;
import poseidon.skills.Klassen.KlassChoose;
import poseidon.skills.Klassen.Players;
import poseidon.skills.UIs.SkillUI;

import java.util.ArrayList;
import java.util.List;

import static poseidon.skills.XPMapper.xpAdd;

//TODO customMessages
public class SkillCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player player)){
            System.out.println(ChatColor.RED + "You need to be a Player");
            return true;
        }
        if(strings.length == 1){
            if(strings[0].equalsIgnoreCase("Bind")) {
                new SkillUI(player);
                return true;
            }
            if(strings[0].equalsIgnoreCase("add")){
                xpAdd(KlassChoose.getPlayers(player), 100, true);
                xpAdd(KlassChoose.getPlayers(player), 100, false);
            }
        }
        if(strings.length == 2){
            if(strings[0].equalsIgnoreCase("add")){
                Player p = Bukkit.getPlayer(strings[1]);
                if(p == null){
                    return true;
                }
                if(KlassChoose.securityCheck(p)) {
                    if(!KlassChoose.getPlayers(p).getBerufklasse().equals(Berufklasse.Unchosed)) {
                        xpAdd(KlassChoose.getPlayers(player), 100, true);
                    }
                    if(!KlassChoose.getPlayers(p).getKampfklasse().equals(Kampfklassen.Unchosed)) {
                        xpAdd(KlassChoose.getPlayers(player), 100, false);
                    }
                }
                else {
                    commandSender.sendMessage(ChatColor.RED + "Spieler neu registiert!");
                }
            }
        }
        if(strings.length == 0){
            Players p = KlassChoose.getPlayers(player);
            int x = p.getBerufLevel();
            int need = (x+10)*(x+10)+50;
            int y = p.getKampfLevel();
            int need2 = (y+10)*(y+10)+50;
            String cityname = "Keine";
            if(p.getHometown() != null){
                cityname = p.getHometown().getCityName();
            }
            player.sendMessage(ChatColor.DARK_GRAY + "=======================\n" +
                    ChatColor.GOLD +"Player: " + player.getName() + "\n" +
                    "Playtime: " + player.getStatistic(Statistic.PLAY_ONE_MINUTE)/20/60/60+ " Stunden\n" +
                    "Beruf: " + p.getBerufklasse().getDisplayName() + "\n" +
                    "Beruflevel: " + p.getBerufLevel() + "\n" +
                    "BerufXP: " + p.getBerufXP() + "/" + need + "\n" +
                    "Kampfklasse: " + p.getKampfklasse().getDisplayName() + "\n" +
                    "Kampflevel: " + p.getKampfLevel() + "\n" +
                    "KampfXP: " + p.getKampfXP() + "/" + need2 + "\n" +
                    "Money: " + p.getMoney() + "\n" +
                    "City: "+ cityname + "\n" +
                    ChatColor.DARK_GRAY + "=======================");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> strings = new ArrayList<>();
        strings.add("Bind");

        return strings;
    }
}
