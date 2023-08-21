package poseidon.skills.Chat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import poseidon.skills.Klassen.KlassChoose;
import poseidon.skills.Klassen.Players;
import poseidon.skills.Skills;
import poseidon.skills.citys.City;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatAPI {
    public enum Chats {
        NationChat("NC", "Format.Chat.NationChat"),
        CityChat("CC", "Format.Chat.CityChat"),
        GlobalChat("GC", "Format.Chat.GlobalChat"),
        LocalChat("LC", "Format.Chat.LocalChat"),
        WispherChat("WC", "Format.Chat.WispherChat"),
        ScreamChat("SC", "Format.Chat.ScreamChat"),
        HelpChat("HC", "Format.Chat.HelpChat", "Skills.chat.Help"),
        AdminChat("AC", "Format.Chat.AdminChat", "Skills.chat.Admin"),
        TradeChat("TC", "Format.Chat.TradeChat", "Skills.chat.trade");
        private final String kurz;
        private final String formatPath;
        @Nullable
        private final String permission;
        Chats(String kurz, String path) {
            this.kurz = kurz;
            this.formatPath = path;
            this.permission = null;
        }
        Chats(String kurz, String path, String perm) {
            this.kurz = kurz;
            this.formatPath = path;
            this.permission = perm;
        }
        public static Chats getByKurz(String s){
            for(Chats chats : Chats.values()){
                if(chats.getKurz().equals(s)){
                    return chats;
                }
            }
            return GlobalChat;
        }
        public String getFormatPath(){
            return formatPath;
        }
        public String getKurz() {
            return kurz;
        }

        public String getPermission() {
            return permission;
        }
    }
    private static String formateFromSkills(Chats chats, String message, Player player){
        String b = Skills.getInstance().message(chats.getFormatPath());
        b = b.replace("{name}", player.getName());
        b = b.replace("{message}", message);
        return b;
    }
    public static void sendMessageInChat(Player player, Chats chats, String message){
        Players players = KlassChoose.getPlayers(player);
        String a = formateFromSkills(chats,message,player);
        switch (chats){
            case GlobalChat -> {
                Bukkit.broadcastMessage(a);
            }
            case NationChat -> {
                if(players.getHometown() == null){
                    player.sendMessage(ChatColor.RED + "Um hier schreiben zu können musst du Teil einer Stadt sein!");
                }
                //TODO Nations
            }
            case CityChat -> {
                if(players.getHometown() == null){
                    player.sendMessage(ChatColor.RED + "Um hier schreiben zu können musst du Teil einer Stadt sein!");
                    return;
                }
                City city = players.getHometown();
                List<Player> boradCastList = new ArrayList<>();
                for(UUID uuid : city.getBuergerUUID()){
                    Player player1 = Bukkit.getPlayer(uuid);
                    if(player1 != null){
                        boradCastList.add(player1);
                    }
                }
                for(Player burger : boradCastList){
                    burger.sendMessage(a);
                }
            }
            case LocalChat -> {
                List<Player> boradCast = new ArrayList<>();
                int x = Skills.getInstance().value("Values.Chat.Range.LocalChat");
                boradCast.add(player);
                for(Entity entity : player.getNearbyEntities(x, x,x)){
                    if(entity instanceof Player player1){
                        boradCast.add(player1);
                    }
                }
                for (Player p: boradCast) {
                    p.sendMessage(a);
                }
            }
            case WispherChat -> {
                List<Player> boradCast = new ArrayList<>();
                int x = Skills.getInstance().value("Values.Chat.Range.WispherChat");
                boradCast.add(player);
                for(Entity entity : player.getNearbyEntities(x, x,x)){
                    if(entity instanceof Player player1){
                        boradCast.add(player1);
                    }
                }
                for (Player p: boradCast) {
                    p.sendMessage(a);
                }
            }
            case ScreamChat -> {
                List<Player> boradCast = new ArrayList<>();
                int x = Skills.getInstance().value("Values.Chat.Range.ScreamChat");
                boradCast.add(player);
                for(Entity entity : player.getNearbyEntities(x, x,x)){
                    if(entity instanceof Player player1){
                        boradCast.add(player1);
                    }
                }
                for (Player p: boradCast) {
                    p.sendMessage(a);
                }
            }
            case HelpChat, AdminChat, TradeChat -> {
                List<Player> broadCast = new ArrayList<>();
                for (Player p: Bukkit.getOnlinePlayers()) {
                    if(chats.getPermission() != null && p.hasPermission(chats.getPermission())){
                        broadCast.add(p);
                    }

                }
                for(Player player1 : broadCast){
                    player1.sendMessage(a);
                }
            }
        }
    }
}
