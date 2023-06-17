package poseidon.skills.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import poseidon.skills.JSON.JSONSave;
import poseidon.skills.Klassen.Berufklasse;
import poseidon.skills.Klassen.Kampfklassen;
import poseidon.skills.Klassen.KlassChoose;
import poseidon.skills.Skills;
import poseidon.skills.XPMapper;
import poseidon.skills.skill.BerufSkills;
import poseidon.skills.skill.KampfSkills;
import poseidon.skills.skill.SkillMapper;

import java.util.HashMap;
import java.util.Objects;

public class Debug implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println(ChatColor.RED + "You need to be a Player");
        }
        assert sender instanceof Player;
        Player player = (Player) sender;
        if(args.length == 1){
            if(args[0].equalsIgnoreCase("Reload")){

                Skills.starter();
            }
        }
        if(args.length == 2){
            if(args[0].equalsIgnoreCase("List")){
                if(args[1].equalsIgnoreCase("Berufklassen")){
                    player.sendMessage("Alle geladenen Berufklassen:");
                    Berufklasse.getTest().forEach((klassen) -> player.sendMessage(klassen.getDisplayName()));
                    player.sendMessage("Done");
                }
                if(args[1].equalsIgnoreCase("Kampfklassen")){
                    player.sendMessage("Alle geladenen Kampfklassen:");
                    Kampfklassen.getKlassen().forEach((klassen) -> player.sendMessage(klassen.getDisplayName()));
                    player.sendMessage("done");
                }
                if(args[1].equalsIgnoreCase("Kampfskills")){
                    player.sendMessage("Alle geladenen Kampfskills:");
                    SkillMapper.getKampfSkills().forEach(kampfSkills -> player.sendMessage(kampfSkills.getName()));
                    player.sendMessage("done");
                }
                if(args[1].equalsIgnoreCase("Berufskills")){
                    player.sendMessage("Alle geladenen Berufskills:");
                    SkillMapper.getBerufSkills().forEach(kampfSkills -> player.sendMessage(kampfSkills.getName()));
                    player.sendMessage("done");
                }
                if(args[1].equalsIgnoreCase("Mobs")){
                    player.sendMessage("Alle geladenen Mobs: ");
                    XPMapper.getMobKillXP().forEach((entityType, integer) -> player.sendMessage(entityType.getTranslationKey() + " " + integer));
                    player.sendMessage("done");
                }
                if(args[1].equalsIgnoreCase("Fish")){
                    player.sendMessage("Alle geladenen Fische: ");
                    XPMapper.getFishXP().forEach((itemStack, integer) -> player.sendMessage(itemStack + " " + integer));
                    player.sendMessage("done");
                }
                if(args[1].equalsIgnoreCase("Mine")){
                    player.sendMessage("Alle geladenen Blöcke: ");
                    XPMapper.getMineXP().forEach((itemStack, integer) -> player.sendMessage(itemStack + " " + integer));
                    player.sendMessage("done");
                }
                if(args[1].equalsIgnoreCase("Wood")){
                    player.sendMessage("Alle geladenen Blöcke: ");
                    XPMapper.getWoodXP().forEach((itemStack, integer) -> player.sendMessage(itemStack + " " + integer));
                    player.sendMessage("done");
                }
                if(args[1].equalsIgnoreCase("Farm")){
                    player.sendMessage("Alle geladenen Blöcke: ");
                    XPMapper.getFarmXP().forEach((itemStack, integer) -> player.sendMessage(itemStack + " " + integer));
                    player.sendMessage("done");
                }
            }
            if(args[0].equalsIgnoreCase("addList")){
                if(args[1].equalsIgnoreCase("KampfSkills")){
                    KampfSkills.addBaseSkills();
                    sender.sendMessage("Kampfskills geladen");
                }
                if(args[1].equalsIgnoreCase("BerufSkills")){
                    BerufSkills.addBaseMain();
                    sender.sendMessage("BerufSkills geladen");
                }
                if(args[1].equalsIgnoreCase("KampfKlassen")){
                    Kampfklassen.addMainKlass();
                    sender.sendMessage("KampfKlassen geladen");
                }
                if(args[1].equalsIgnoreCase("BerufKlassen")){
                    Berufklasse.addMainKlass();
                    sender.sendMessage("BerufKlassen geladen");
                }
            }
            if(args[0].equalsIgnoreCase("generateFile")){
                if (args[1].equalsIgnoreCase("KampfKlassen")) {
                    JSONSave.kampfKlassenSave();
                    sender.sendMessage("KampfKlassen geschrieben");
                }
                if(args[1].equalsIgnoreCase("KampfSkill")){
                    JSONSave.kampfSkillSave();
                    sender.sendMessage("KampfSkill geschrieben");
                }
                if(args[1].equalsIgnoreCase("BerufSkill")){
                    JSONSave.berufSkillSave();
                    sender.sendMessage("BerufSkill geschrieben");
                }
                if(args[1].equalsIgnoreCase("BerufKlassen")){
                    JSONSave.berufKlassenSave();
                    sender.sendMessage("BerufKlassen geschrieben");
                }
                if(args[1].equalsIgnoreCase("Mobs")){
                    JSONSave.generateHostileMob();
                    sender.sendMessage("Mobs geschrieben");
                }
                if(args[1].equalsIgnoreCase("Fish")){
                    HashMap<Material, Integer> a = new HashMap<>();
                    a.put(Material.COD, 5);
                    a.put(Material.SALMON, 10);
                    JSONSave.generateDestroyFIles(a, JSONSave.location.Fishing);
                    sender.sendMessage("Fischliste geschrieben");
                }
                if(args[1].equalsIgnoreCase("Mine")){
                    HashMap<Material, Integer> a = new HashMap<>();
                    a.put(Material.STONE,1);
                    a.put(Material.DEEPSLATE,1);
                    JSONSave.generateDestroyFIles(a, JSONSave.location.Mining);
                    sender.sendMessage("MineListe geschrieben");
                }
                if(args[1].equalsIgnoreCase("Wood")){
                    HashMap<Material, Integer> a = new HashMap<>();
                    a.put(Material.OAK_LOG, 1);
                    a.put(Material.BIRCH_LOG, 1);
                    JSONSave.generateDestroyFIles(a, JSONSave.location.Wooding);
                    sender.sendMessage("WoodListe geschrieben");
                }
                if(args[1].equalsIgnoreCase("Farm")){
                    HashMap<Material, Integer> a = new HashMap<>();
                    a.put(Material.WHEAT, 1);
                    a.put(Material.CARROTS, 1);
                    JSONSave.generateDestroyFIles(a, JSONSave.location.Farming);
                    sender.sendMessage("Farmlist geschrieben");
                }
            }
            if(args[0].equalsIgnoreCase("deletePlayers")){
                if(Bukkit.getPlayer(args[1]) != null){
                    KlassChoose.resetPlayer(Objects.requireNonNull(Bukkit.getPlayer(args[1])));
                }
                else {
                    player.sendMessage("Dieser Spieler exsistiert nicht");
                }
            }
        }
        return true;
    }
}

