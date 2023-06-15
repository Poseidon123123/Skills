package poseidon.skills.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;

public class AktivSkillCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player player)){
            System.out.println(ChatColor.RED + "You need to be a Player");
            return true;
        }
        if(!(args.length == 1)){
            System.out.println("Ungenügend Argumente");
            return true;
        }
        switch (args[0]) {
            case "B1" -> {
                System.out.println("B1");
                //TODO Schwächer für nach oben
                player.setVelocity(player.getLocation().getDirection().multiply(2.5));
            }
            case "B2" -> {
                System.out.println("B2");
                SmallFireball a = player.launchProjectile(SmallFireball.class, player.getEyeLocation().getDirection());
                a.setVelocity(player.getLocation().getDirection().multiply(1.5));
                a.setIsIncendiary(false);
                a.setCustomName("SFB");
                a.setCustomNameVisible(false);
            }
            case "K1" -> System.out.println("K1");
            case "K2" -> System.out.println("K2");
        }
        return true;
    }
}
