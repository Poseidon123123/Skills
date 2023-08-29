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
import poseidon.skills.Chat.ChatAPI;
import poseidon.skills.Farmwelt;
import poseidon.skills.Klassen.KlassChoose;
import poseidon.skills.Klassen.Players;
import poseidon.skills.Skills;
import poseidon.skills.citys.City;
import poseidon.skills.citys.CityMapper;
import poseidon.skills.citys.CityOffer;
import poseidon.skills.citys.Nation;

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
                player.sendMessage(Objects.requireNonNull(getInstance().getConfig().getString("Messages.City.noCity")));
                return true;
            }
            City city = players.getHometown();
            String a = Objects.requireNonNull(getInstance().getConfig().getString("Messages.City.info"));
            String b = a.replace("{city}", city.getCityName());
            String c = b.replace("{bugerMeister}", Objects.requireNonNull(city.getBuergermeister().getName()));
            String d = c.replace("{money}", String.valueOf(city.getCityMoney()));
            String e = d.replace("{listburger}", extractNames( city.getBuergerUUID()));
            String f = e.replace("{vize}", extractNames(city.getVizeList()));
            String g = f.replace("{nation}", city.getNation() !=null ? city.getNation().getNationName() : "Keine");
            player.sendMessage(g);
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("claim")) {
                if (players.getHometown() == null) {
                    player.sendMessage(Objects.requireNonNull(getInstance().getConfig().getString("Messages.City.create.isInCity")));
                    return true;
                }
                if (!players.getHometown().hasAdmin(player.getUniqueId())) {
                    player.sendMessage(Objects.requireNonNull(getInstance().getConfig().getString("Messages.City.noVize")));
                    return true;
                }
                City city = players.getHometown();
                Chunk chunk = player.getLocation().getChunk();
                if(canClaimChunk(player.getLocation().getChunk(),player)){
                    city.addClaimedCHunk(chunk);
                    player.sendMessage(Objects.requireNonNull(getInstance().getConfig().getString("Messages.City.claim.succsess")));
                    CityMapper.addChunk(city, chunk);
                    ChatAPI.sendServerMessage(city, Skills.getInstance().message("Messages.City.claim.cityMessage").replace("{name}", player.getName()).replace("{cords}", chunk.getX() + ", " + chunk.getZ()));
                }
            }
            if(args[0].equalsIgnoreCase("delete")){
                if (players.getHometown() == null) {
                    player.sendMessage(getInstance().message("Messages.City.noCity"));
                    return true;
                }
                Nation nation = players.getHometown().getNation();
                if (!nation.getKing().equals(player.getUniqueId())) {
                    player.sendMessage(Skills.getInstance().message("Messages.City.noMeister"));
                    return true;
                }
                City city = players.getHometown();
                if(city.getCityMoney() > 0){
                    int money = city.getCityMoney();
                    int each = money/city.getBuergerUUID().size();
                    for(UUID uuid : city.getBuergerUUID()){
                        KlassChoose.getPlayer(uuid).addMoney(each);
                    }
                }
                if(city.getNation() != null){
                    city.getNation().removeCity(city);
                }
                for(UUID uuid : city.getBuergerUUID()){
                    KlassChoose.getPlayer(uuid).setHometown(null);
                }
                CityMapper.removeCity(city);
            }
        }
        if (args.length == 2) {
            if(args[0].equalsIgnoreCase("create")) {
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
                    if(canClaimChunk(player.getLocation().getChunk(),player)) {
                        if (args[1].equalsIgnoreCase("Dummy")) {
                            city = new City("Dummy", UUID.randomUUID(), player.getLocation().getChunk());
                            players.addMoney(getInstance().value("Values.Money.CreateCity"));
                        } else {
                            city = new City(args[1], player);
                            players.setHometown(city);
                        }
                        CityMapper.addCity(city);
                        player.sendMessage(getInstance().message("Messages.City.create.succsess").replace("{name}", args[1]));
                    }
                }
                else {
                    player.sendMessage(getInstance().message("Messages.City.create.NoMoney").replace("{money}", String.valueOf(getInstance().value("Values.Money.CreateCity"))));
                }
            }
            if(args[0].equalsIgnoreCase("deposit")) {
                if (players.getHometown() == null) {
                    player.sendMessage(getInstance().message("Messages.City.noCity"));
                    return true;
                }
                    int money = MakeCommand.stringToInt(args[1]);
                    if (money <= 0) {
                        player.sendMessage(getInstance().message("Messages.City.deposit.notOver0"));
                    }
                    if (players.removeMoney(money)) {
                        players.getHometown().addCityMoney(money);
                        player.sendMessage(getInstance().message("Messages.City.deposit.succsess").replace("{money}", String.valueOf(money)));
                        ChatAPI.sendServerMessage(players.getHometown(), Skills.getInstance().message("Messages.City.deposit.cityMessage").replace("{name}", player.getName()).replace("{money}", String.valueOf(money)));
                    }
                }
            if(args[0].equalsIgnoreCase("addPlayer")) {
                if (players.getHometown() == null) {
                    player.sendMessage(getInstance().message("Messages.City.noCity"));
                    return true;
                }
                City city = players.getHometown();
                if (!city.hasAdmin(player.getUniqueId())) {
                    player.sendMessage(getInstance().message("Messages.City.noVize"));
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
            if(args[0].equalsIgnoreCase("accept")) {
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
                            String a = getInstance().message("Messages.City.accept.succsess");
                            a.replace("{city}", city.getCityName());
                            player.sendMessage(a);
                            ChatAPI.sendServerMessage(city, Skills.getInstance().message("Messages.City.accept.cityMessage").replace("{name}", player.getName()));
                        }
                    }
                }
            }
            if(args[0].equalsIgnoreCase("kick")){
                if (players.getHometown() == null) {
                    player.sendMessage(getInstance().message("Messages.City.noCity"));
                    return true;
                }
                City city = players.getHometown();
                if (!city.hasAdmin(player.getUniqueId())) {
                    player.sendMessage(getInstance().message("Messages.City.noVize"));
                    return true;
                }
                Player player1 = Bukkit.getPlayerExact(args[1]);
                if (player1 == null) {
                    player.sendMessage(getInstance().message("Messages.General.noPlayerFound").replace("{name}", args[1]));
                    return true;
                }
                if(!players.getHometown().hasPlayer(player1)){
                    player.sendMessage(Skills.getInstance().message("Messages.City.kick.PlayerNotInCity").replace("{player}", player1.getName()));
                    return true;
                }
                Players players1 = KlassChoose.getPlayers(player1);
                players1.setHometown(null);
                players.getHometown().removeBuerger(player1.getUniqueId());
                ChatAPI.sendServerMessage(city, Skills.getInstance().message("Messages.City.kick.cityMessage").replace("{name}", player1.getName()));
                return true;
            }
            if(args[0].equalsIgnoreCase("leave")){
                if (players.getHometown() == null) {
                    player.sendMessage(getInstance().message("Messages.City.noCity"));
                    return true;
                }
                if(CityMapper.getByName(args[1]) != null && CityMapper.getByName(args[1]) == players.getHometown()){
                    City city = players.getHometown();
                    players.getHometown().removeBuerger(player.getUniqueId());
                    players.setHometown(null);
                    player.sendMessage(Skills.getInstance().message("Messages.City.leave.success").replace("{cityName}", city.getCityName()));
                    ChatAPI.sendServerMessage(city,Skills.getInstance().message("Messages.City.leave.cityMessage").replace("{name}", player.getName()));
                    return true;
                }
                else {
                    player.sendMessage(Skills.getInstance().message("Messages.City.leave.wrongCity"));
                }
            }
            if(args[0].equalsIgnoreCase("Vize")){
                if (players.getHometown() == null) {
                    player.sendMessage(getInstance().message("Messages.City.noCity"));
                    return true;
                }
                City city = players.getHometown();
                if(!city.getBuergermeisterUUID().equals(player.getUniqueId())){
                    player.sendMessage(Skills.getInstance().message("Messages.City.noMeister"));
                    return true;
                }
                if(Bukkit.getPlayerExact(args[1]) == null){
                    String a = Skills.getInstance().message("Messages.General.noPlayerFound");
                    player.sendMessage(a.replace("{name}", args[1]));
                    return true;
                }
                Player player1 = Bukkit.getPlayerExact(args[1]);
                if (player1 == null) {
                    return true;
                }
                Players players1 = KlassChoose.getPlayers(player1);
                if(players1.getHometown() == null || players1.getHometown() != players.getHometown()){
                    String a = Skills.getInstance().message("Messages.City.kick.PlayerNotInCity");
                    player.sendMessage(a.replace("{player}", player1.getName()));
                    return true;
                }
                if(city.isVize(player1.getUniqueId())) {
                    city.remobeVize(player1.getUniqueId());
                    String a = Skills.getInstance().message("Messages.City.toggleVize.take");
                    player.sendMessage(a.replace("{name}", player1.getName()));
                    ChatAPI.sendServerMessage(city, Skills.getInstance().message("Messages.City.vize.cityMessage.remove").replace("{name}", player1.getName()));
                }
                else {
                    city.addVize(player1.getUniqueId());
                    String a = Skills.getInstance().message("Messages.City.toggleVize.set");
                    player.sendMessage(a.replace("{name}", player1.getName()));
                    ChatAPI.sendServerMessage(city, Skills.getInstance().message("Messages.City.vize.cityMessage.add").replace("{name}", player1.getName()));
                    return true;
                }

            }
            if(args[0].equalsIgnoreCase("withdraw")){
                if (players.getHometown() == null) {
                    String a = getInstance().message("Messages.City.noCity");
                    player.sendMessage(Objects.requireNonNull(a));
                    return true;
                }
                City city = players.getHometown();
                if (!city.hasAdmin(player.getUniqueId())) {
                    player.sendMessage(getInstance().message("Messages.City.noVize"));
                    return true;
                }
                int value = Integer.parseInt(args[1]);
                if(value <= 0){
                    player.sendMessage(Skills.getInstance().message("Messages.City.withdraw.illegalValue"));
                    return true;
                }
                if(!players.getHometown().removeCityMoney(value)){
                    player.sendMessage(Skills.getInstance().message("Messages.City.withdraw.lowMoney"));
                    return true;
                }
                else {
                    players.addMoney(value);
                    player.sendMessage(Skills.getInstance().message("Messages.City.withdraw.success").replace("{value}", String.valueOf(value)));
                    ChatAPI.sendServerMessage(players.getHometown(), Skills.getInstance().message("Messages.City.withdraw.cityMessage").replace("{value}", String.valueOf(value)).replace("{name}", player.getName()));
                }
            }
            if(args[0].equalsIgnoreCase("makeOwner")){
                if (players.getHometown() == null) {
                    player.sendMessage(getInstance().message("Messages.City.noCity"));
                    return true;
                }
                City nation = players.getHometown();
                if (!nation.getBuergermeisterUUID().equals(player.getUniqueId())) {
                    player.sendMessage(Skills.getInstance().message("Messages.City.noMeister"));
                    return true;
                }
                if (Bukkit.getPlayerExact(args[1]) == null) {
                    String a = Skills.getInstance().message("Messages.General.noPlayerFound");
                    player.sendMessage(a.replace("{name}", args[1]));
                    return true;
                }
                Player player1 = Bukkit.getPlayerExact(args[1]);
                if (player1 == null) {
                    return true;
                }
                Players players1 = KlassChoose.getPlayers(player1);
                if (players1.getHometown() == null || players1.getHometown().getNation() == null || players1.getHometown().getNation() != players.getHometown().getNation()) {
                    String a = Skills.getInstance().message("Messages.City.kick.PlayerNotInCity");
                    player.sendMessage(a.replace("{name}", player1.getName()));
                    return true;
                }
                if(!nation.isVize(player1.getUniqueId())){
                    player.sendMessage(Skills.getInstance().message("Messages.City.makeOwner.playerNotVize").replace("{name}", player1.getName()));
                }
                nation.setBuergermeister(players1.getUUID());
                ChatAPI.sendServerMessage(nation, Skills.getInstance().message("Messages.City.makeOwner.cityMessage").replace("{name}", player1.getName()));
            }
        }
        if(args.length >= 3){
            player.sendMessage("§4Zu viele Argumente!");
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
        Players players = KlassChoose.getPlayers(player);
        if (args.length == 1) {
            if (players.getHometown() == null) {
                onTabComplete.add("create");
                onTabComplete.add("accept");
            } else {
                if (players.getHometown().hasAdmin(player.getUniqueId())) {
                    onTabComplete.add("addPlayer");
                    onTabComplete.add("claim");
                    onTabComplete.add("withdraw");
                }
                if (players.getHometown().getBuergermeisterUUID().equals(player.getUniqueId())) {
                    onTabComplete.add("Vize");
                    onTabComplete.add("makeOwner");
                    onTabComplete.add("delete");
                }
                onTabComplete.add("deposit");
            }
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("create") && players.getHometown() == null) {
                onTabComplete.add("<Stadtname>");
            }
            else if(args[0].equalsIgnoreCase("deposit")){
                onTabComplete.add("<Betrag>");
            }
            else if(args[0].equalsIgnoreCase("addPlayer") && players.getHometown().hasAdmin(player.getUniqueId())){
                for(Player player1 : Bukkit.getOnlinePlayers()){
                    onTabComplete.add(player1.getName());
                }
            }
            else if(args[0].equalsIgnoreCase("withdraw") && players.getHometown().hasAdmin(player.getUniqueId())){
                onTabComplete.add("<Betrag>");
            }
            else if(args[0].equalsIgnoreCase("accept") && players.getHometown() == null){
                List<City> a = new CityOffer(player).getOffers();
                if(a == null){
                    return onTabComplete;
                }
                for(City city : a){
                    onTabComplete.add(city.getCityName());
                }
            }
            else if(players.getHometown().getBuergermeisterUUID().equals(player.getUniqueId()) && args[0].equalsIgnoreCase("Vize")){
                for(Player player1 : players.getHometown().getBuerger()){
                    onTabComplete.add(player1.getName());
                }
            }
        }
        return onTabComplete;
    }

    public static String extractNames(List<UUID> objectList) {
        return objectList.stream()
                .map(Bukkit::getOfflinePlayer)
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
    public static boolean canClaimChunk(Chunk chunk, Player player) {
        boolean isNearTown = false;
        boolean hasEnemyTownNear = false;
        Players players = KlassChoose.getPlayers(player);
        City city = players.getHometown();

        if (CityMapper.ChunkClaimed(chunk)) {
            if (Objects.equals(CityMapper.whoClaimed(chunk), city)) {
                player.sendMessage(Objects.requireNonNull(getInstance().getConfig().getString("Messages.City.claim.ChunkClaimedSelf")));
            } else if (Objects.equals(CityMapper.whichNationClaimed(chunk), city.getNation())) {
                player.sendMessage(Skills.getInstance().message("Messages.City.claim.claimedByNation"));
            } else {
                player.sendMessage(Objects.requireNonNull(getInstance().getConfig().getString("Messages.City.claim.ChunkClaimedOther")));
            }
            return false;
        }

        for (Chunk nearChunck : koordinatenImUmkreis(chunk, Skills.getInstance().getConfig().getInt("Values.Radius.NearEnemy"))) {
            if (CityMapper.ChunkClaimed(nearChunck)) {
                if (!Objects.equals(CityMapper.whoClaimed(nearChunck), city) || !Objects.equals(CityMapper.whichNationClaimed(nearChunck), city.getNation())) {
                    player.sendMessage(Objects.requireNonNull(getInstance().getConfig().getString("Messages.City.claim.ChunkNearOther")));
                    hasEnemyTownNear = true;
                    break;
                }
            }
        }

        for (Chunk nearChunk : koordinatenImUmkreis(chunk, Skills.getInstance().getConfig().getInt("Values.Radius.NearTown"))) {
            if (CityMapper.ChunkClaimed(nearChunk)) {
                if (Objects.equals(CityMapper.whoClaimed(nearChunk), city)) {
                    isNearTown = true;
                }
            }
        }

        if (hasEnemyTownNear) {
            return false;
        } else {
            if (isNearTown) {
                if (!players.removeMoney(getInstance().value("Values.Money.claimChunk"))) {
                    String a = getInstance().message("Messages.City.create.NoMoney");
                    a = a.replace("{money}", String.valueOf(getInstance().value("Values.Money.claimChunk")));
                    player.sendMessage(a);
                    return false;
                } else {
                    return true;
                }
            } else {
                String a = getInstance().message("Messages.City.claim.tooFar");
                a = a.replace("{chunk}", String.valueOf(getInstance().value("Values.Radius.NearTown")));
                player.sendMessage(a);
                return false;
            }
        }
    }
}
