package poseidon.skills.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import poseidon.skills.Klassen.Berufklasse;
import poseidon.skills.Klassen.Kampfklassen;
import poseidon.skills.Klassen.KlassChoose;
import poseidon.skills.Klassen.Players;
import poseidon.skills.SkillListener;
import poseidon.skills.UIs.SkillUI;



public class SkillCommand implements CommandExecutor {
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
                SkillListener.xpAdd(KlassChoose.getPlayers(player), 100, true);
                SkillListener.xpAdd(KlassChoose.getPlayers(player), 100, false);
            }
        }
        if(strings.length == 2){
            if(strings[0].equalsIgnoreCase("add")){
                Player p = Bukkit.getPlayer(strings[1]);
                assert p != null;
                if(KlassChoose.securityCheck(p)) {
                    if(!KlassChoose.getPlayers(p).getBerufklasse().equals(Berufklasse.Unchosed)) {
                        SkillListener.xpAdd(KlassChoose.getPlayers(player), 100, true);
                    }
                    if(!KlassChoose.getPlayers(p).getKampfklasse().equals(Kampfklassen.Unchosed)) {
                        SkillListener.xpAdd(KlassChoose.getPlayers(player), 100, false);
                    }
                }
                else {
                    commandSender.sendMessage(ChatColor.RED + "Spieler neu registiert!");
                }
            }
        }
        if(strings.length == 0){
            Players p = KlassChoose.getPlayers(player);
            int x = p.getBerufLevel() - 1;
            int need = (x+10)*(x+10)+50;
            int y = p.getKampfLevel() - 1;
            int need2 = (y+10)*(y+10)+50;
            System.out.println(x);
            player.sendMessage(ChatColor.DARK_GRAY + "=======================\n" +
                    ChatColor.GOLD +"Player: " + player.getName() + "\n" +
                    "Playtime: " + player.getPlayerTime() + "\n" +
                    "Beruf: " + p.getBerufklasse().getDisplayName() + "\n" +
                    "Beruflevel: " + p.getBerufLevel() + "\n" +
                    "BerufXP: " + p.getBerufXP() + "/" + need + "\n" +
                    "Kampfklasse: " + p.getKampfklasse().getDisplayName() + "\n" +
                    "Kampflevel: " + p.getKampfLevel() + "\n" +
                    "KampfXP: " + p.getKampfXP() + "/" + need2 + "\n" +
                    ChatColor.DARK_GRAY + "=======================");
        }
        return true;
    }
}
