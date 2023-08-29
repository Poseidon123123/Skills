package poseidon.skills.Commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import poseidon.skills.CustomItems.CustomItem;
import poseidon.skills.Klassen.Berufklasse;
import poseidon.skills.Klassen.Kampfklassen;
import poseidon.skills.XPMapper;
import poseidon.skills.XPObjekt;
import poseidon.skills.executeSkills.Funktions;
import poseidon.skills.executeSkills.Type;
import poseidon.skills.skill.BerufSkills;
import poseidon.skills.skill.KampfSkills;
import poseidon.skills.skill.SkillMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MakeCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)){
            sender.sendMessage("Du musst ein Spieler sein!");
            return true;
        }
        if(args.length == 1) {
            if (sender.hasPermission("Skills.command.Customize.create")) {
                if (args[0].equalsIgnoreCase("newCustomItem")) {
                    if (!player.getInventory().getItemInMainHand().getType().isAir()) {
                        player.sendMessage(ChatColor.RED + "Du musst ein Item mit ItemMeta in der Hand halten!");
                        return true;
                    } else {
                        ItemStack itemStack = player.getInventory().getItemInMainHand();
                        CustomItem.registerItem(new CustomItem(itemStack));
                        player.sendMessage(ChatColor.GREEN + "Du hast ein CustomItem hinzugefügt");
                    }
                }
            } else {
                sender.sendMessage("Du hast keine Berechtigung dazu");
            }
        }
        if(args.length == 2){
            if(sender.hasPermission("Skills.command.Customize.create")) {
                if (args[0].equalsIgnoreCase("newCustomItem")) {
                    if (player.getInventory().getItemInMainHand().getType().isAir()) {
                        player.sendMessage(ChatColor.RED + "Du musst ein Item mit ItemMeta in der Hand halten!");
                        return true;
                    } else if(Berufklasse.isOnArray(args[1])){
                        ItemStack itemStack = player.getInventory().getItemInMainHand();
                        CustomItem.registerItem(new CustomItem(itemStack, Berufklasse.getOfArray(args[1])));
                        player.sendMessage(ChatColor.GREEN + "Du hast ein CustomItem dem Beruf " + Berufklasse.getOfArray(args[1]).getDisplayName() + " hinzugefügt");
                    } else if(Kampfklassen.isOnArray(args[1])){
                        ItemStack itemStack = player.getInventory().getItemInMainHand();
                        CustomItem.registerItem(new CustomItem(itemStack, Kampfklassen.getOfArray(args[1])));
                        player.sendMessage(ChatColor.GREEN + "Du hast ein CustomItem der Kampfklasse " + Kampfklassen.getOfArray(args[1]).getDisplayName() + " hinzugefügt");
                    } else if(args[0].equalsIgnoreCase("newTypes")){
                        Type type = new Type(args[1]);
                        Funktions.registerType(type);
                    }
                }
            }
            else if(sender.hasPermission("Skills.command.Customize.remove")) {
                if (args[0].equalsIgnoreCase("deleteBeruf")) {
                    if (Berufklasse.isOnArray(args[1])) {
                        Berufklasse a = Berufklasse.getOfArray(args[1]);
                        Berufklasse.getTest().remove(a);
                        sender.sendMessage("Der Beruf " + a.getDisplayName() + " wurde gelöscht");
                    } else {
                        sender.sendMessage("Keine Beruf gefunden überprüfe Rechtschreibung!");
                    }
                }
                if (args[0].equalsIgnoreCase("deleteKampf")) {
                    if (Kampfklassen.isOnArray(args[1])) {
                        Kampfklassen a = Kampfklassen.getOfArray(args[1]);
                        Kampfklassen.getKlassen().remove(a);
                        sender.sendMessage("Die Kampfklasse " + a.getDisplayName() + " wurde gelöscht");
                    } else {
                        sender.sendMessage("Keine Kampfklasse gefunden überprüfe Rechtschreibung!");
                    }
                }
                if (args[0].equalsIgnoreCase("deleteBerufSkill")) {
                    if (SkillMapper.isOnBerufArray(args[1])) {
                        BerufSkills a = SkillMapper.getOfBerufArray(args[1]);
                        assert a != null;
                        sender.sendMessage("Der Berufskill " + a.getName() + " wurde gelöscht");
                        SkillMapper.removeSkills(a);
                    } else {
                        sender.sendMessage("Keine Berufsklasse gefunden überprüfe Rechtschreibung!");
                    }
                }
                if (args[0].equalsIgnoreCase("deleteKampfSkill")) {
                    if (SkillMapper.isOnKampfArray(args[1])) {
                        KampfSkills a = SkillMapper.getOfKampfArray(args[1]);
                        SkillMapper.removeSkills(a);
                        assert a != null;
                        sender.sendMessage("Der Kampfskill " + a.getName() + " wurde gelöscht");
                    } else {
                        sender.sendMessage("Keine Berufsklasse gefunden überprüfe Rechtschreibung!");
                    }
                }
            }
            else {
                sender.sendMessage("Du hast keine Berechtigung dazu");
            }
        }
        if(args.length == 3) {
            if (sender.hasPermission("Skills.command.Customize.create")) {
                if (args[0].equalsIgnoreCase("newCustomItem")) {
                    if (player.getInventory().getItemInMainHand().getType().isAir()) {
                        player.sendMessage(ChatColor.RED + "Du musst ein Item mit ItemMeta in der Hand halten!");
                        return true;
                    } else if (Berufklasse.isOnArray(args[1]) && Kampfklassen.isOnArray(args[2])) {
                        ItemStack itemStack = player.getInventory().getItemInMainHand();
                        CustomItem.registerItem(new CustomItem(itemStack, Kampfklassen.getOfArray(args[2]), Berufklasse.getOfArray(args[1])));
                        player.sendMessage(ChatColor.GREEN + "Du hast ein CustomItem dem Beruf " + Berufklasse.getOfArray(args[1]).getDisplayName() + " und der Kampfklasse + " + Kampfklassen.getOfArray(args[2]) + " hinzugefügt");
                    }
                }
                if (args[0].equalsIgnoreCase("newBeruf")) {
                    Berufklasse.XPSource b = Berufklasse.XPSource.None;
                    for (Berufklasse.XPSource a : Berufklasse.XPSource.values()) {
                        if (a.name().equalsIgnoreCase(args[2])) {
                            b = a;
                            break;
                        }
                    }
                    if (b.equals(Berufklasse.XPSource.None)) {
                        sender.sendMessage("Keine XPSource erkennt, None wurdezugeordnet");
                    }
                    Berufklasse berufklasse = new Berufklasse(args[1], b);
                    if (Berufklasse.addToTest(berufklasse)) {
                        sender.sendMessage("Die Berufklasse " + berufklasse.getDisplayName() + " wurde hinzugefügt");
                    }
                }
                if (args[0].equalsIgnoreCase("newKampf")) {
                    Kampfklassen kampfklassen = new Kampfklassen(args[1], stringToInt(args[2], player));
                    if (Kampfklassen.addKlasse(kampfklassen)) {
                        sender.sendMessage("Die Kampfklasse " + kampfklassen.getDisplayName() + " wurde hinzugefügt");
                    }

                }
                if (args[0].equalsIgnoreCase("newMobXP")) {
                    EntityType entityType = EntityType.ZOMBIE;
                    boolean changed = false;
                    for (EntityType entityType1 : EntityType.values()) {
                        if (entityType1.equals(EntityType.UNKNOWN)) {
                            continue;
                        }
                        if (entityType1.getTranslationKey().equals(args[1])) {
                            entityType = entityType1;
                            changed = true;
                        }
                    }
                    if (!changed) {
                        player.sendMessage("Keinen Typen erkannt Zombie wurde standartmäßig gesetzt");
                    }
                    int xp = stringToInt(args[2], player);
                    XPMapper.addMob(entityType, xp);
                    player.sendMessage("Du hast einen weiteren MobXp Eintrag gemacht");
                }
            }
            else {
                sender.sendMessage("Du hast keine Berechtigung dafür");
            }
        }
        if (args.length == 5) {
            if (sender.hasPermission("Skills.command.Customize.create")) {
                if (args[0].equalsIgnoreCase("newXPObjekt")) {
                    Material m = Material.getMaterial(args[1]);
                    if (m == null) {
                        m = Material.DIRT;
                        sender.sendMessage("Material ungültig Dirt als Icon genommen!");
                    }
                    Berufklasse berufklasse = Berufklasse.Unchosed;
                    if (Berufklasse.isOnArray(args[2])) {
                        berufklasse = Berufklasse.getOfArray(args[2]);
                    } else {
                        sender.sendMessage("Beruf nicht erkannt Unchosed gewählt");
                    }
                    int xp = stringToInt(args[3], player);
                    int money = stringToInt(args[4], player);
                    XPObjekt.register(new XPObjekt(m, berufklasse.getSource(), xp, money));
                }
            } else {
                sender.sendMessage("Du hast keine Berechtigung dafür");
            }
        }
        if (args.length == 8) {
                if (sender.hasPermission("Skills.command.Customize.create")) {
                    if (args[0].equalsIgnoreCase("newBerufSkill")) {
                        Material m = Material.getMaterial(args[1]);
                        if (m == null) {
                            m = Material.DIRT;
                            sender.sendMessage("Material ungültig Dirt als Icon genommen!");
                        }
                        int cooldown = stringToInt(args[4], player);
                        int neededLevel = stringToInt(args[6], player);
                        int consumedMana = stringToInt(args[7], player);
                        BerufSkills a = new BerufSkills(createGUIItems(m, args[2]), args[2], args[3], cooldown, Berufklasse.getOfArray(args[5]), neededLevel, consumedMana);
                        if (SkillMapper.addBerufSkill(a)) {
                            sender.sendMessage("Der Berufskill " + a.getName() + " wurde mit dem Material " + a.getIcon() + ", dem Cooldown " + a.getCooldown() + ", dem Executer " + a.getCommand() + ", dem Mindestlevel " + a.getNeededLevel() + ", dem Manause " + a.getConsumedMana() + " in der Berufsklasse " + a.getBerufklasse() + " erfolgreich erstellt.");
                        }
                    }
                    if (args[0].equalsIgnoreCase("newKampfSkill")) {
                        Material m = Material.getMaterial(args[1]);
                        if (m == null) {
                            m = Material.DIRT;
                            sender.sendMessage("Material ungültig Dirt als Icon genommen!");
                        }
                        int cooldown = stringToInt(args[4], player);
                        int neededLevel = stringToInt(args[6], player);
                        int consumedMana = stringToInt(args[7], player);
                        KampfSkills a = new KampfSkills(createGUIItems(m, args[2]), args[2], args[3], cooldown, Kampfklassen.getOfArray(args[5]), neededLevel, consumedMana);
                        if (SkillMapper.addKampfSkill(a)) {
                            sender.sendMessage("Der Kampfskill " + a.getName() + " wurde mit dem Material " + a.getIcon() + ", dem Cooldown " + a.getCooldown() + ", dem Executer " + a.getCommand() + ", dem Mindestlevel " + a.getNeededLevel() + ", dem Manause " + a.getConsumedMana() + " in der Berufsklasse " + a.getKampfKlasse() + " erfolgreich erstellt.");
                        }
                    }
                } else {
                    sender.sendMessage("Du hast keine berechtigung dafür");
                }

            }
        return true;
    }


    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tabCompleteList = new ArrayList<>();
        if(args.length == 1) {
            if (sender.hasPermission("Skills.command.Customize.remove")) {
                tabCompleteList.add("deleteBeruf");
                tabCompleteList.add("deleteKampf");
                tabCompleteList.add("deleteBerufSkill");
                tabCompleteList.add("deleteKampfSkill");
            }
            if (sender.hasPermission("Skills.command.Customize.create")) {
                tabCompleteList.add("newBeruf");
                tabCompleteList.add("newKampf");
                tabCompleteList.add("newMobXP");
                tabCompleteList.add("newXPObjekt");
                tabCompleteList.add("newBerufSkill");
                tabCompleteList.add("newKampfSkill");
            }
        }
        if(args.length == 2){
            if(args[0].equalsIgnoreCase("newBeruf") || args[0].equalsIgnoreCase("newKampf") ){
                tabCompleteList.add("<Name>");
            }
            else if(args[0].equalsIgnoreCase("newMobXP")){
                if(args[1].equalsIgnoreCase("")) {
                    for (EntityType entityType : EntityType.values()) {
                        if (!entityType.equals(EntityType.UNKNOWN)) {
                            tabCompleteList.add(entityType.getTranslationKey());
                        }
                    }
                }
                else {
                    for (EntityType type : EntityType.values()) {
                        if (!type.equals(EntityType.UNKNOWN)) {
                            if (type.getTranslationKey().toLowerCase().startsWith(args[1].toLowerCase())) {
                                tabCompleteList.add(type.name());
                            }
                        }
                    }
                }
            }
            else if (args[0].equalsIgnoreCase("newXPObjekt") || args[0].equalsIgnoreCase("newBerufSkill") || args[0].equalsIgnoreCase("newKampfSkill")){
                for(Material material : Material.values()){
                    tabCompleteList.add(material.name());
                }
            }
            else if(args[0].equalsIgnoreCase("deleteBeruf")){
                for(Berufklasse berufklasse : Berufklasse.getTest()){
                    tabCompleteList.add(berufklasse.getDisplayName());
                }
            }
            else if(args[0].equalsIgnoreCase("deleteKampf")){
                for(Kampfklassen kampfklassen : Kampfklassen.getKlassen()){
                    tabCompleteList.add(kampfklassen.getDisplayName());
                }
            }
            else if(args[0].equalsIgnoreCase("deleteBerufSkill")){
                for(BerufSkills kampfklassen : SkillMapper.getBerufSkills()){
                    tabCompleteList.add(kampfklassen.getName());
                }
            }
            else if(args[0].equalsIgnoreCase("deleteKampfSkill")){
                for(KampfSkills kampfklassen : SkillMapper.getKampfSkills()){
                    tabCompleteList.add(kampfklassen.getName());
                }
            }


        }
        if(args.length == 3){
            if(args[0].equalsIgnoreCase("newBeruf")){
                for(Berufklasse.XPSource xpSource : Berufklasse.XPSource.values()){
                    tabCompleteList.add(xpSource.name());
                }
            }
            else if(args[0].equalsIgnoreCase("newKampf")){
                tabCompleteList.add("<BaseMana>");
            }
            else if(args[0].equalsIgnoreCase("newMobXP")){
                tabCompleteList.add("<XP>");
            }
            else if (args[0].equalsIgnoreCase("newXPObjekt")){
                for(Berufklasse berufklasse : Berufklasse.getTest()){
                    tabCompleteList.add(berufklasse.getDisplayName());
                }
            }
            else if(args[0].equalsIgnoreCase("newBerufSkill") || args[0].equalsIgnoreCase("newKampfSkill")){
                tabCompleteList.add("<Name>");
            }
        }
        if(args.length == 4){ //args[3]
            if(args[0].equalsIgnoreCase("newXPObjekt")){
                tabCompleteList.add("<XP>");
            }
            else if(args[0].equalsIgnoreCase("newBerufSkill") || args[0].equalsIgnoreCase("newKampfSkill")){
                tabCompleteList.add("<Execute>");
            }
        }
        if(args.length == 5){ //args[4]
            if(args[0].equalsIgnoreCase("newXPObjekt")){
                tabCompleteList.add("<Money>");
            }
            else if(args[0].equalsIgnoreCase("newBerufSkill") || args[0].equalsIgnoreCase("newKampfSkill")){
                tabCompleteList.add("<Cooldown>");
            }
        }
        if(args.length == 6){//args[5]
            if(args[0].equalsIgnoreCase("newBerufSkill")){
                for(Berufklasse berufklasse : Berufklasse.getTest()){
                    tabCompleteList.add(berufklasse.getDisplayName());
                }
            }
            else if(args[0].equalsIgnoreCase("newKampfSkill")){
                for(Kampfklassen kampfklassen : Kampfklassen.getKlassen()){
                    tabCompleteList.add(kampfklassen.getDisplayName());
                }
            }
        }
        if(args.length == 7){//args[6]
            if(args[0].equalsIgnoreCase("newBerufSkill") || args[0].equalsIgnoreCase("newKampfSkill")){
                tabCompleteList.add("<neededLevel>");
            }
        }
        if(args.length == 8){//args[7]
            if(args[0].equalsIgnoreCase("newBerufSkill") || args[0].equalsIgnoreCase("newKampfSkill")){
                tabCompleteList.add("<ConsumedMana>");
            }
        }
        return tabCompleteList;
    }

    protected static ItemStack createGUIItems(final Material material, final String name, final String... Lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(Lore));
        item.setItemMeta(meta);
        return item;
    }
    public static int stringToInt(String str, Player player) {
        StringBuilder numberString = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isDigit(c)) {
                numberString.append(c);
            }
        }
        try {
            return Integer.parseInt(numberString.toString());
        } catch (NumberFormatException e) {
            player.sendMessage("Keine Nummer erkannt! 0 eingesetzt");
            return 0;
        }
    }
    public static int stringToInt(String str) {
        StringBuilder numberString = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isDigit(c)) {
                numberString.append(c);
            }
        }
        try {
            return Integer.parseInt(numberString.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
