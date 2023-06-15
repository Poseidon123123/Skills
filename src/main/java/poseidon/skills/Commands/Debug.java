package poseidon.skills.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import poseidon.skills.JSON.JSONSave;
import poseidon.skills.Klassen.Berufklasse;
import poseidon.skills.Klassen.Kampfklassen;
import poseidon.skills.XPMapper;
import poseidon.skills.skill.BerufSkills;
import poseidon.skills.skill.KampfSkills;
import poseidon.skills.skill.SkillMapper;

public class Debug implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println(ChatColor.RED + "You need to be a Player");
        }
        if (args.length == 1) {
            assert sender instanceof Player;
            Player player = (Player) sender;
            if (args[0].equalsIgnoreCase("Berufklassen")) {
                Berufklasse.getTest().forEach((klassen) -> player.sendMessage(klassen.getDisplayName()));
                sender.sendMessage("done");
            }
            if (args[0].equalsIgnoreCase("Kampfklassen")) {
                Kampfklassen.getKlassen().forEach((klassen) -> player.sendMessage(klassen.getDisplayName()));
                sender.sendMessage("done");
            }
            if(args[0].equalsIgnoreCase("Kampfskills")){
                SkillMapper.getKampfSkills().forEach(kampfSkills -> player.sendMessage(kampfSkills.getName()));
                sender.sendMessage("done");
            }
            if(args[0].equalsIgnoreCase("Berufskills")){
                SkillMapper.getBerufSkills().forEach(kampfSkills -> player.sendMessage(kampfSkills.getName()));
                sender.sendMessage("done");
            }
            if(args[0].equalsIgnoreCase("LoadKampfSkills")){
                KampfSkills.addBaseSkills();
                sender.sendMessage("done");
            }
            if(args[0].equalsIgnoreCase("LoadBerufSkills")){
                BerufSkills.addBaseMain();
                sender.sendMessage("done");
            }
            if(args[0].equalsIgnoreCase("LoadKampfKlassen")){
                Kampfklassen.addMainKlass();
                sender.sendMessage("done");
            }
            if(args[0].equalsIgnoreCase("LoadBerufKlassen")){
                Berufklasse.addMainKlass();
                sender.sendMessage("done");
            }
            if (args[0].equalsIgnoreCase("KampfKlassenwrite")) {
                JSONSave.kampfKlassenSave();
                sender.sendMessage("done");
            }
            if(args[0].equalsIgnoreCase("KampfSkillWrite")){
                JSONSave.kampfSkillSave();
                sender.sendMessage("done");
            }
            if(args[0].equalsIgnoreCase("BerufSkillWrite")){
                JSONSave.berufSkillSave();
                sender.sendMessage("done");
            }
            if(args[0].equalsIgnoreCase("BerufKassenWrite")){
                JSONSave.berufKlassenSave();
                sender.sendMessage("done");
            }
            if(args[0].equalsIgnoreCase("Mobs")){
                player.sendMessage("Test");
                player.sendMessage(String.valueOf(XPMapper.getXP().size()));
                XPMapper.getXP().forEach((entityType, integer) -> player.sendMessage(entityType.getTranslationKey() + " " + integer));
                sender.sendMessage("done");
            }
            if(args[0].equalsIgnoreCase("Mobswrite")){
                JSONSave.generateHostileMob();
                sender.sendMessage("done");
            }
        }
        return true;
    }
}

