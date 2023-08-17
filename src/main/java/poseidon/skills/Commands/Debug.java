package poseidon.skills.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import poseidon.skills.Bar;
import poseidon.skills.CustomItems.CustomItem;
import poseidon.skills.JSON.JSONSave;
import poseidon.skills.Klassen.Berufklasse;
import poseidon.skills.Klassen.Kampfklassen;
import poseidon.skills.Klassen.KlassChoose;
import poseidon.skills.Skills;
import poseidon.skills.XPMapper;
import poseidon.skills.XPObjekt;
import poseidon.skills.citys.CityMapper;
import poseidon.skills.skill.BerufSkills;
import poseidon.skills.skill.KampfSkills;
import poseidon.skills.skill.SkillMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static poseidon.skills.XPMapper.evaluateMathExpression;

public class Debug implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println(ChatColor.RED + "You need to be a Player");
        }
        assert sender instanceof Player;
        Player player = (Player) sender;
        if(args.length == 1) {
            if (args[0].equalsIgnoreCase("getCustomItems")) {
                CustomItem.getItemMap().forEach((s, itemStack) -> player.getInventory().addItem(itemStack));
            }
            if(args[0].equalsIgnoreCase("DBCustomItems")){
                System.out.println("KampfMap");
                CustomItem.kampfItemMap.forEach((itemStack, kampfklassen) -> {
                    System.out.println(itemStack);
                    System.out.println(kampfklassen.getDisplayName());
                });
                System.out.println("BerufMap");
                CustomItem.berufItemMap.forEach((itemStack, berufklasse) -> {
                    System.out.println(itemStack);
                    System.out.println(berufklasse.getDisplayName());
                });
            }
        }
        if(args.length == 2) {
            if (args[0].equalsIgnoreCase("List")) {
                if(!sender.hasPermission("Skills.command.DB.List")) {
                    player.sendMessage(ChatColor.RED + "Du hast die Rechte dafür nicht!");
                    return false;
                }
                if (args[1].equalsIgnoreCase("Berufklassen")) {
                    player.sendMessage("Alle geladenen Berufklassen:");
                    Berufklasse.getTest().forEach((klassen) -> player.sendMessage(klassen.getDisplayName()));
                    player.sendMessage("Done");
                }
                if (args[1].equalsIgnoreCase("Kampfklassen")) {
                    player.sendMessage("Alle geladenen Kampfklassen:");
                    Kampfklassen.getKlassen().forEach((klassen) -> player.sendMessage(klassen.getDisplayName()));
                    player.sendMessage("done");
                }
                if (args[1].equalsIgnoreCase("Kampfskills")) {
                    player.sendMessage("Alle geladenen Kampfskills:");
                    SkillMapper.getKampfSkills().forEach(kampfSkills -> player.sendMessage(kampfSkills.getName()));
                    player.sendMessage("done");
                }
                if (args[1].equalsIgnoreCase("Berufskills")) {
                    player.sendMessage("Alle geladenen Berufskills:");
                    SkillMapper.getBerufSkills().forEach(kampfSkills -> player.sendMessage(kampfSkills.getName()));
                    player.sendMessage("done");
                }
                if (args[1].equalsIgnoreCase("Mobs")) {
                    player.sendMessage("Alle geladenen Mobs: ");
                    XPMapper.getMobKillXP().forEach((entityType, integer) -> player.sendMessage(entityType.getTranslationKey() + " " + integer));
                    player.sendMessage("done");
                }
                if(args[1].equalsIgnoreCase("AllBeruf")){
                    XPObjekt.mapper().forEach(((material, xpObjekt) -> player.sendMessage(material + " " + xpObjekt.getXp() + " " + xpObjekt.getMoney() + " " + xpObjekt.getXpSource())));
                }
                if(args[1].equalsIgnoreCase("City")) {
                    CityMapper.getCityList().forEach(city -> player.sendMessage(city.getCityName() + " " + city.getCityMoney() + " " + city.getBuergermeister().getName()));
                }
                if(args[1].equalsIgnoreCase("Chunks")){
                    CityMapper.getclaimedChunks().forEach((chunk, city) -> player.sendMessage(city.getCityName() + " + " + chunk.getX() + " / " + chunk.getZ()));
                }
                if(args[1].equalsIgnoreCase("Recipes")){
                    CustomItem.shapedRecipeList.forEach((shapedRecipe, berufklasse) -> player.sendMessage(shapedRecipe.getKey().toString()));
                }
            }
            if (args[0].equalsIgnoreCase("addList")) {
                if(!sender.hasPermission("Skills.command.DB.loadList")) {
                    player.sendMessage(ChatColor.RED + "Du hast die Rechte dafür nicht!");
                    return false;
                }
                if(args[1].equalsIgnoreCase("CustomItems")){
                    CustomItem.makeExamples();
                    sender.sendMessage("CustomItems geladen");
                }
                if (args[1].equalsIgnoreCase("KampfSkills")) {
                    KampfSkills.addBaseSkills();
                    sender.sendMessage("Kampfskills geladen");
                }
                if (args[1].equalsIgnoreCase("BerufSkills")) {
                    BerufSkills.addBaseMain();
                    sender.sendMessage("BerufSkills geladen");
                }
                if (args[1].equalsIgnoreCase("KampfKlassen")) {
                    Kampfklassen.addMainKlass();
                    sender.sendMessage("KampfKlassen geladen");
                }
                if (args[1].equalsIgnoreCase("BerufKlassen")) {
                    Berufklasse.addMainKlass();
                    sender.sendMessage("BerufKlassen geladen");
                }
                if(args[1].equalsIgnoreCase("AllXP")){
                    XPObjekt.addMainBase();
                    sender.sendMessage("XP-Quellen geladen");
                }
            }
            if (args[0].equalsIgnoreCase("generateFile")) {
                if(!player.hasPermission("Skills.command.DB.dic")){
                    player.sendMessage(ChatColor.RED + "Du hast die Rechte dafür nicht!");
                    return false;
                }
                if(args[1].equalsIgnoreCase("Recipes")){
                    CustomItem.example();
                    JSONSave.saveRecipes();
                }
                if (args[1].equalsIgnoreCase("KampfKlassen")) {
                    JSONSave.kampfKlassenSave();
                    sender.sendMessage("KampfKlassen geschrieben");
                }
                if (args[1].equalsIgnoreCase("KampfSkill")) {
                    JSONSave.kampfSkillSave();
                    sender.sendMessage("KampfSkill geschrieben");
                }
                if (args[1].equalsIgnoreCase("BerufSkill")) {
                    JSONSave.berufSkillSave();
                    sender.sendMessage("BerufSkill geschrieben");
                }
                if (args[1].equalsIgnoreCase("BerufKlassen")) {
                    JSONSave.berufKlassenSave();
                    sender.sendMessage("BerufKlassen geschrieben");
                }
               if (args[1].equalsIgnoreCase("Mobs")) {
                    JSONSave.mobSave();
                    sender.sendMessage("Mobs geschrieben");
                }
                if(args[1].equalsIgnoreCase("AllXP")){
                    JSONSave.generateDestroyFIles(XPObjekt.regsterdObjekts());
                    sender.sendMessage("XP-Quellen gespeichert");
                }
                if(args[1].equalsIgnoreCase("CustomItems")){
                    JSONSave.safeCustomItems();
                    sender.sendMessage("CustomItems gespeichert");
                }
            }
            if (args[0].equalsIgnoreCase("deletePlayers")) {
                if(!sender.hasPermission("Skills.command.DB.deletePlayer")){
                    player.sendMessage(ChatColor.RED + "Du hast die Rechte dafür nicht!");
                    return false;
                }
                Player player1 = Bukkit.getPlayerExact(args[1]);
                if (player1 != null) {
                    KlassChoose.resetPlayer(player1);
                    player.sendMessage("Alle informationen zu: " + Objects.requireNonNull(Bukkit.getPlayer(args[1])).getName() + " wurden erfolgreich gelöscht");
                } else {
                    player.sendMessage("Dieser Spieler existiert nicht");
                }
            }
        }
        return true;
    }

    public static void berufBar(Player player) {
        int i1 = KlassChoose.getPlayers(player).getBerufLevel();
        int b = (int) evaluateMathExpression(Objects.requireNonNull(Skills.getInstance().getConfig().getString("Funktions.XP")), i1);
        double c = KlassChoose.getPlayers(player).getBerufXP();
        double process = c / b;
        Bar bar = KlassChoose.getPlayers(player).getBerufBar();
        bar.addPlayer(player, ChatColor.BLUE + KlassChoose.getPlayers(player).getBerufklasse().getDisplayName() + ": " + c + "/" + b, process, 100);
    }

    public static void kampfBar(Player player) {
        int i1 = KlassChoose.getPlayers(player).getKampfLevel();
        int b = ((int) evaluateMathExpression(Objects.requireNonNull(Skills.getInstance().getConfig().getString("Funktions.XP")), i1));
        double c = KlassChoose.getPlayers(player).getKampfXP();
        double process = c / b;
        Bar bar = KlassChoose.getPlayers(player).getKampfBar();
        bar.addPlayer(player, ChatColor.RED + KlassChoose.getPlayers(player).getKampfklasse().getDisplayName() + ": " + c + "/" + b, process, 100);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> tabCompleteList = new ArrayList<>();
        if(args.length == 1){
            if(sender.hasPermission("Skills.command.DB.List")) {
                tabCompleteList.add("List");
            }
            if(sender.hasPermission("Skills.command.DB.loadList")) {
                tabCompleteList.add("addList");
            }
            if(sender.hasPermission("Skills.command.DB.dic")) {
                tabCompleteList.add("generateFile");
            }
            if(sender.hasPermission("Skills.command.DB.deletePlayer")) {
                tabCompleteList.add("deletePlayers");
            }
        }
        else if (args.length == 2){
            if (args[0].equalsIgnoreCase("List") && sender.hasPermission("Skills.command.DB.List")) {
                tabCompleteList.add("BerufKlassen");
                tabCompleteList.add("KampfKlassen");
                tabCompleteList.add("KampfSkills");
                tabCompleteList.add("BerufSkills");
                tabCompleteList.add("Mobs");
                tabCompleteList.add("AllBeruf");
                tabCompleteList.add("City");
            } else if (args[0].equalsIgnoreCase("generateFile") && sender.hasPermission("Skills.command.DB.dic")) {
                tabCompleteList.add("BerufKlassen");
                tabCompleteList.add("KampfKlassen");
                tabCompleteList.add("KampfSkill");
                tabCompleteList.add("BerufSkill");
                tabCompleteList.add("Mobs");
                tabCompleteList.add("AllXP");
            } else if (args[0].equalsIgnoreCase("addList") && sender.hasPermission("Skills.command.DB.loadList")) {
                tabCompleteList.add("BerufKlassen");
                tabCompleteList.add("KampfKlassen");
                tabCompleteList.add("KampfSkills");
                tabCompleteList.add("BerufSkills");
                tabCompleteList.add("AllXP");
            }
            else if (args[0].equalsIgnoreCase("deletePlayers") && sender.hasPermission("Skills.command.DB.deletePlayer")) {
                Bukkit.getOnlinePlayers().forEach(player -> tabCompleteList.add(player.getName()));
            }
        }
        Collections.sort(tabCompleteList);
        return tabCompleteList;
    }
}

