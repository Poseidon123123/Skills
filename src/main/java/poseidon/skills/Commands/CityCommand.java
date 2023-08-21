package poseidon.skills.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import poseidon.skills.Farmwelt;
import poseidon.skills.Klassen.KlassChoose;
import poseidon.skills.Klassen.Players;
import poseidon.skills.Skills;
import poseidon.skills.citys.City;
import poseidon.skills.citys.CityMapper;
import poseidon.skills.citys.CityOffer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static poseidon.skills.Skills.getInstance;

public class CityCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Du musst ein Spieler sein!");
            return true;
        }
        Players players = KlassChoose.getPlayers(player);
        if (args.length == 0) {
            if (players.getHometown() == null) {
                player.sendMessage(Objects.requireNonNull(getInstance().getConfig().getString("Messages.City.invite.noCity")));
                return true;
            }
            City city = players.getHometown();
            String a = Objects.requireNonNull(getInstance().getConfig().getString("Messages.City.info"));
            String b = a.replace("{city}", city.getCityName());
            String c = b.replace("{bugerMeister}", Objects.requireNonNull(city.getBuergermeister().getName()));
            String d = c.replace("{money}", String.valueOf(city.getCityMoney()));
            String e = d.replace("{listburger}", extractNames(city.getBuerger()));
            player.sendMessage(e);
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("claim")) {
                if (players.getHometown() == null) {
                    player.sendMessage(Objects.requireNonNull(getInstance().getConfig().getString("Messages.City.create.isInCity")));
                    return true;
                }
                City city = players.getHometown();
                if (!players.getHometown().getBuergermeisterUUID().equals(player.getUniqueId())) {
                    player.sendMessage(Objects.requireNonNull(getInstance().getConfig().getString("Messages.City.invite.noMeister")));
                    return true;
                }
                boolean isNearTown = false;
                boolean hasEnemyTownNear = false;
                Chunk chunk = player.getLocation().getChunk();
                if (CityMapper.ChunkClaimed(chunk)) {
                    if (CityMapper.whoClaimed(chunk).equals(city)) {
                        player.sendMessage(Objects.requireNonNull(getInstance().getConfig().getString("Messages.City.claim.ChunkClaimedSelf")));
                    } else {
                        player.sendMessage(Objects.requireNonNull(getInstance().getConfig().getString("Messages.City.claim.ChunkClaimedOther")));
                    }
                    return true;
                }
                for (Chunk nearChunck : koordinatenImUmkreis(chunk, Skills.getInstance().getConfig().getInt("Values.Radius.NearEnemy"))) {
                    if (CityMapper.ChunkClaimed(nearChunck)) {
                        if (!CityMapper.whoClaimed(nearChunck).equals(city)) {
                            player.sendMessage(Objects.requireNonNull(getInstance().getConfig().getString("Messages.City.claim.ChunkNearOther")));
                            hasEnemyTownNear = true;
                            break;
                        }
                    }
                }
                for (Chunk nearChunk : koordinatenImUmkreis(chunk, Skills.getInstance().getConfig().getInt("Values.Radius.NearTown"))) {
                    if (CityMapper.ChunkClaimed(nearChunk)) {
                        if (CityMapper.whoClaimed(nearChunk).equals(city)) {
                            isNearTown = true;
                        }
                    }
                }
                if (hasEnemyTownNear) {
                    return true;
                } else {
                    if (isNearTown) {
                        if(!players.removeMoney(getInstance().value("Values.Money.claimChunk"))){
                            String a = getInstance().message("Messages.City.create.NoMoney")   ;
                            a = a.replace("{money}", String.valueOf(getInstance().value("Values.Money.claimChunk")));
                            player.sendMessage(a);
                        }
                        else {
                            city.addClaimedCHunk(chunk);
                            player.sendMessage(Objects.requireNonNull(getInstance().getConfig().getString("Messages.City.claim.succsess")));
                            CityMapper.addChunk(city, chunk);
                        }
                    } else {
                        String a = getInstance().message("Messages.City.claim.tooFar");
                        a = a.replace("{chunk}", String.valueOf(getInstance().value("Values.Radius.NearTown")));
                        player.sendMessage(a);
                    }
                }
            }
            if(args[0].equalsIgnoreCase("leave")){
                if (players.getHometown() == null) {
                    String a = getInstance().message("Messages.City.invite.noCity");
                    System.out.println(a);
                    player.sendMessage(Objects.requireNonNull(a));
                    return true;
                }
                player.sendMessage(Skills.getInstance().message("Messages.City.leave.noCity"));
            }
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("create")) {
                if (players.getHometown() != null) {
                    player.sendMessage(getInstance().message("Messages.City.create.isInCity"));
                    return true;
                }
                if(player.getWorld().equals(Farmwelt.farmWorld)){
                    player.sendMessage(Skills.getInstance().message("Messages.City.create.notMainWorld"));
                    return true;
                }
                City city;
                if (CityMapper.getByName(args[1]) != null) {
                    player.sendMessage(getInstance().message("Messages.City.CityExists"));
                    return true;
                }
                if (KlassChoose.getPlayers(player).removeMoney(getInstance().value("Values.Money.CreateCity"))) {
                    if (args[1].equalsIgnoreCase("Dummy")) {
                        city = new City("Dummy", UUID.randomUUID(), player.getLocation().getChunk());
                        players.addMoney(getInstance().value("Values.Money.CreateCity"));
                    } else {
                        city = new City(args[1], player);
                        players.setHometown(city);
                    }
                    CityMapper.addCity(city);
                    String message = getInstance().message("Messages.City.create.succsess");
                    message.replace("{name}", args[1]);
                    player.sendMessage(message);
                }
                else {
                    String a = getInstance().message("Messages.City.create.NoMoney");
                    a = a.replace("{money}", String.valueOf(getInstance().value("Values.Money.CreateCity")));
                    player.sendMessage(a);
                }
            }
            if (args[0].equalsIgnoreCase("deposit")) {
                if (players.getHometown() == null) {
                    player.sendMessage(getInstance().message("Messages.City.PlayerHasNoTown"));
                    return true;
                }
                    int money = MakeCommand.stringToInt(args[1]);
                    if (money <= 0) {
                        player.sendMessage(getInstance().message("Messages.City.deposit.notOver0"));
                    }
                    if (players.removeMoney(money)) {
                        players.getHometown().addCityMoney(money);
                        String a = getInstance().message("Messages.City.deposit.sucsess");
                        a.replace("{money}", String.valueOf(money));
                        player.sendMessage(a);
                    }
                }
            if (args[0].equalsIgnoreCase("addPlayer")) {
                if (players.getHometown() == null) {
                    String a = getInstance().message("Messages.City.invite.noCity");
                    player.sendMessage(Objects.requireNonNull(a));
                    return true;
                }
                if (!players.getHometown().getBuergermeisterUUID().equals(player.getUniqueId())) {
                    player.sendMessage(getInstance().message("Messages.City.invite.noMeister"));
                    return true;
                }
                Player player1 = Bukkit.getPlayerExact(args[1]);
                if (player1 == null) {
                    String a = getInstance().message("Messages.noPlayerFound");
                    a.replace("{name}", args[1]);
                    player.sendMessage(a);
                    return true;
                }
                CityOffer cityMapper = new CityOffer(player1);
                cityMapper.addOffer(players.getHometown());
                }
            if (args[0].equalsIgnoreCase("Accept")) {
                if (players.getHometown() != null) {
                    player.sendMessage(getInstance().message("Messages.City.create.isInCity"));
                    return true;
                }
                CityOffer cityMapper = new CityOffer(player);
                if (cityMapper.hasOffer()) {
                    for (City city : cityMapper.getOffers()) {
                        if (city.getCityName().equals(args[1])) {
                            city.addBuerger(player.getUniqueId());
                            players.setHometown(city);
                            cityMapper.offersDone();
                            String a = getInstance().message("Messages.City.deposit.sucsess");
                            a.replace("{city}", city.getCityName());
                            player.sendMessage(a);
                        }
                    }
                }
            }
            if (args[0].equalsIgnoreCase("kick")){
                if (players.getHometown() == null) {
                    String a = getInstance().message("Messages.City.invite.noCity");
                    System.out.println(a);
                    player.sendMessage(Objects.requireNonNull(a));
                    return true;
                }
                if (!players.getHometown().getBuergermeisterUUID().equals(player.getUniqueId())) {
                    player.sendMessage(getInstance().message("Messages.City.invite.noMeister"));
                    return true;
                }
                Player player1 = Bukkit.getPlayerExact(args[1]);
                if (player1 == null) {
                    String a = getInstance().message("Messages.noPlayerFound");
                    a.replace("{name}", args[1]);
                    player.sendMessage(a);
                    return true;
                }
                if(!players.getHometown().hasPlayer(player1)){
                    String a = Skills.getInstance().message("Messages.City.kick.PlayerNotInCity");
                    a.replace("{player}", player1.getName());
                    player.sendMessage(a);
                    return true;
                }
                Players players1 = KlassChoose.getPlayers(player1);
                players1.setHometown(null);
                players.getHometown().removeBuerger(player1.getUniqueId());
                return true;
            }
            if(args[0].equalsIgnoreCase("leave")){
                if (players.getHometown() == null) {
                    String a = getInstance().message("Messages.City.invite.noCity");
                    System.out.println(a);
                    player.sendMessage(Objects.requireNonNull(a));
                    return true;
                }
                if(CityMapper.getByName(args[1]) != null && CityMapper.getByName(args[1]) == players.getHometown()){
                    players.getHometown().removeBuerger(player.getUniqueId());
                    players.setHometown(null);
                    return true;
                }
                else {
                    player.sendMessage(Skills.getInstance().message("Messages.City.leave.wrongCity"));
                }
            }
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> onTabComplete = new ArrayList<>();
        if(!(sender instanceof Player player)){
            sender.sendMessage("Du musst ein Spieler sein!");
            return onTabComplete;
        }
        if (args.length == 1) {
            onTabComplete.add("create");
            onTabComplete.add("deposit");
            onTabComplete.add("accept");
            onTabComplete.add("addPlayer");
            onTabComplete.add("claim");
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("create")) {
                onTabComplete.add("<Stadtname>");
            }
            else if(args[0].equalsIgnoreCase("deposit")){
                onTabComplete.add("<Betrag>");
            }
            else if(args[0].equalsIgnoreCase("addPlayer")){
                for(Player player1 : Bukkit.getOnlinePlayers()){
                    onTabComplete.add(player1.getName());
                }
            }
            else if(args[0].equalsIgnoreCase("accept")){
                List<City> a = new CityOffer(player).getOffers();
                if(a == null){
                    return onTabComplete;
                }
                for(City city : a){
                    onTabComplete.add(city.getCityName());
                }
            }
        }
        return onTabComplete;
    }

    public static String extractNames(List<Player> objectList) {
        return objectList.stream()
                .map(OfflinePlayer::getName)
                .collect(Collectors.joining(", "));
    }

    public static List<Chunk> koordinatenImUmkreis(Chunk mittelpunkt, int radius) {
        List<Chunk> koordinatenListe = new ArrayList<>();

        for (int x = (mittelpunkt.getX() - radius); x <= (mittelpunkt.getX() + radius); x++) {
            for (int y = (mittelpunkt.getZ() - radius); y <= (mittelpunkt.getZ() + radius); y++) {
                koordinatenListe.add(mittelpunkt.getWorld().getChunkAt(x,y));
            }
        }

        return koordinatenListe;
    }
}
