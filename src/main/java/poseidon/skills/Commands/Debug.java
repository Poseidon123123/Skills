package poseidon.skills.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import poseidon.skills.*;
import poseidon.skills.CustomItems.CustomItem;
import poseidon.skills.CustomItems.CustomShapedRecipe;
import poseidon.skills.CustomItems.CustomShapelessRecipe;
import poseidon.skills.JSON.JSONSave;
import poseidon.skills.Klassen.Berufklasse;
import poseidon.skills.Klassen.Kampfklassen;
import poseidon.skills.Klassen.KlassChoose;
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
            if (args[0].equalsIgnoreCase("CustomItems")) {
                if(!sender.hasPermission("Skills.command.DB.CustomItems")) {
                    player.sendMessage(ChatColor.RED + "Du hast die Rechte dafür nicht!");
                    return false;
                }
                CustomItem.customItemHashMap.forEach((s, customItem) -> {
                    player.getInventory().addItem(customItem.getCustomItem());
                });
            }
            if(args[0].equalsIgnoreCase("Cooldown")){
                CooldownSystem cooldownSystem = CooldownSystem.getbyPlayer(player);
                cooldownSystem.getKampfCooldown().forEach((kampfSkills, integer) -> player.sendMessage(kampfSkills.getName() + " " + integer));
                cooldownSystem.getBerufCooldown().forEach((berufSkills, integer) -> player.sendMessage(berufSkills.getName() + " " + integer));
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
                    sender.sendMessage("ShapedRecipes");
                    CustomShapedRecipe.getCustomShapedRecipeHashMap().forEach((namespacedKey, shapedRecipe) -> player.sendMessage(namespacedKey.toString() + " " + shapedRecipe.getBerufklasse().getDisplayName() + " " + shapedRecipe.getNeededBerufLevel() + " " + shapedRecipe.getKampfklassen().getDisplayName() + " " + shapedRecipe.getNeededKampfLevel()));
                    sender.sendMessage("ShapelessRecipes");
                    CustomShapelessRecipe.getCustomShapedRecipeHashMap().forEach((key, shapelessRecipe) -> sender.sendMessage(key.toString() + " " + shapelessRecipe.getBerufklasse().getDisplayName() + " " + shapelessRecipe.getNeededBerufLevel() + " " + shapelessRecipe.getKampfklassen().getDisplayName() + " " + shapelessRecipe.getNeededKampfLevel()));
                }
                if(args[1].equalsIgnoreCase("CustomItems")){
                    CustomItem.customItemHashMap.forEach((s, customItem) -> {
                        player.sendMessage(s);
                        player.sendMessage(customItem.getCustomItem().toString());
                        player.sendMessage(customItem.getKampfklassen().getDisplayName());
                        player.sendMessage(customItem.getBerufklasse().getDisplayName());
                    });
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
                if(args[1].equalsIgnoreCase("ShapedRecipes")){
                    CustomShapedRecipe.example();
                    sender.sendMessage("ShapedRezepte geladen");
                }
                if(args[1].equalsIgnoreCase("ShapelessRecipes")){
                    CustomShapelessRecipe.example();
                    sender.sendMessage("ShapelessRecipes geladen");
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
                if(args[1].equalsIgnoreCase("ShapedRecipes")){
                    JSONSave.saveShapedRecipes();
                    sender.sendMessage("ShapedRezepte wurden geschrieben");
                }
                if(args[1].equalsIgnoreCase("ShapelessRecipes")){
                    JSONSave.saveShapelessRecipes();
                    sender.sendMessage("ShapelessRecipes wurden geschrieben");
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
            if(sender.hasPermission("Skills.command.DB.CustomItems")){
                tabCompleteList.add("CustomItems");
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
                tabCompleteList.add("Chunks");
                tabCompleteList.add("Recipes");
                tabCompleteList.add("CustomItems");
            } else if (args[0].equalsIgnoreCase("generateFile") && sender.hasPermission("Skills.command.DB.dic")) {
                tabCompleteList.add("BerufKlassen");
                tabCompleteList.add("KampfKlassen");
                tabCompleteList.add("KampfSkill");
                tabCompleteList.add("BerufSkill");
                tabCompleteList.add("Mobs");
                tabCompleteList.add("AllXP");
                tabCompleteList.add("CustomItems");
                tabCompleteList.add("ShapedRecipes");
                tabCompleteList.add("ShapelessRecipes");
            } else if (args[0].equalsIgnoreCase("addList") && sender.hasPermission("Skills.command.DB.loadList")) {
                tabCompleteList.add("BerufKlassen");
                tabCompleteList.add("KampfKlassen");
                tabCompleteList.add("KampfSkills");
                tabCompleteList.add("BerufSkills");
                tabCompleteList.add("AllXP");
                tabCompleteList.add("CustomItems");
                tabCompleteList.add("ShapedRecipes");
                tabCompleteList.add("ShapelessRecipes");
            }
            else if (args[0].equalsIgnoreCase("deletePlayers") && sender.hasPermission("Skills.command.DB.deletePlayer")) {
                Bukkit.getOnlinePlayers().forEach(player -> tabCompleteList.add(player.getName()));
            }
        }
        Collections.sort(tabCompleteList);
        return tabCompleteList;
    }
}

