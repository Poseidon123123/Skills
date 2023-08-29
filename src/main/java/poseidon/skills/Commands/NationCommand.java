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
import poseidon.skills.Klassen.KlassChoose;
import poseidon.skills.Klassen.Players;
import poseidon.skills.Skills;
import poseidon.skills.citys.*;

import java.util.*;

import static poseidon.skills.Skills.getInstance;


public class NationCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)){
            sender.sendMessage("Du musst ein Spieler sein!");
            return true;
        }
        Players players = KlassChoose.getPlayers(player);
        if(args.length == 0){
            if(players.getHometown() == null || players.getHometown().getNation() == null){
                player.sendMessage(Skills.getInstance().message("Messages.Nation.NoNation"));
                return true;
            }
            Nation nation = players.getHometown().getNation();
            String a = Skills.getInstance().message("Messages.Nation.Info");
            String b = a.replace("{nation}", nation.getNationName());
            String c = b.replace("{king}", Objects.requireNonNull(Bukkit.getPlayer(nation.getKing())).getName());
            String d = c.replace("{money}", String.valueOf(nation.getNationMoney()));
            String e = d.replace("{listburger}", processCityPlayers(nation.getCityList()));
            player.sendMessage(e);
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("claim")) {
                if (players.getHometown() == null || players.getHometown().getNation() == null) {
                    player.sendMessage(Objects.requireNonNull(getInstance().getConfig().getString("Messages.Nation.NoNation")));
                    return true;
                }
                Nation nation = players.getHometown().getNation();
                if (!nation.isAdmin(player.getUniqueId())) {
                    player.sendMessage(Objects.requireNonNull(getInstance().getConfig().getString("Messages.Nation.noVize")));
                    return true;
                }
                Chunk chunk = player.getLocation().getChunk();
                if(canClaimChunk(chunk, player)){
                    nation.claimChunk(chunk);
                    player.sendMessage(Objects.requireNonNull(getInstance().getConfig().getString("Messages.Nation.claim.success")));
                    ChatAPI.sendServerMessage(nation,Skills.getInstance().message("Messages.Nation.claim.cityMessage").replace("{name}",player.getName()).replace("{chuk}", chunk.getX() + ", " + chunk.getZ()));

                }
            }
            if(args[0].equalsIgnoreCase("delete")){
                if (players.getHometown() == null || players.getHometown().getNation() == null) {
                    player.sendMessage(Objects.requireNonNull(getInstance().getConfig().getString("Messages.Nation.NoNation")));
                    return true;
                }
                Nation nation = players.getHometown().getNation();
                if (!nation.isAdmin(player.getUniqueId())) {
                    player.sendMessage(Objects.requireNonNull(getInstance().getConfig().getString("Messages.Nation.noVize")));
                    return true;
                }
                int split = 0;
                if(nation.getNationMoney() > 0){
                    split = nation.getNationMoney()/nation.getCityList().size();
                    nation.setCityMoney(0);
                }
                for(Map.Entry<City,List<UUID>> entry : nation.getCityList().entrySet()){
                    City city = entry.getKey();
                    city.addCityMoney(split);
                    city.setNation(null);
                }
                CityMapper.removeNation(nation);
            }
        }
        if(args.length == 2){
            if(args[0].equalsIgnoreCase("create")){
                if(players.getHometown() == null){
                    player.sendMessage(Skills.getInstance().message("Messages.City.noCity"));
                    return true;
                }
                if(!players.getHometown().getBuergermeisterUUID().equals(player.getUniqueId())){
                    player.sendMessage(Skills.getInstance().message("Messages.City.noMeister"));
                    return true;
                }
                if(players.getHometown().getNation() != null){
                    player.sendMessage(Skills.getInstance().message("Messages.Nation.create.isInNation"));
                    return true;
                }
                if(CityMapper.validName(args[1])) {
                    int needed = Skills.getInstance().value("Values.Money.CreateNation");
                    if(players.getHometown().removeCityMoney(needed)) {
                        if(canClaimChunk(player.getLocation().getChunk(),player)) {
                            Nation nation = CityMapper.addNation(new Nation(args[1], players.getHometown(), player.getUniqueId()));
                            players.getHometown().setNation(nation.getNationName());
                            String a = Skills.getInstance().message("Messages.Nation.create.succsess");
                            player.sendMessage(a.replace("{nation}", args[1]));
                        }
                    }
                    else {
                        player.sendMessage(Skills.getInstance().message("Messages.Nation.create.noMoney").replace("{value}",String.valueOf(needed)));
                    }
                }
                else {
                    player.sendMessage(Skills.getInstance().message("Messages.Nation.create.inValidName"));
                }

            }
            if(args[0].equalsIgnoreCase("deposit")){
                if(!(players.getHometown() != null && players.getHometown().getNation() != null)){
                    player.sendMessage(Skills.getInstance().message("Messages.Nation.NoNation"));
                    return true;
                }
                if(!players.getHometown().hasAdmin(player.getUniqueId())){
                    player.sendMessage(Skills.getInstance().message("Messages.City.noVize"));
                    return true;
                }
                int value = Integer.parseInt(args[1]);
                if(value <= 0){
                    player.sendMessage(Skills.getInstance().message("Messages.Nation.deposit.under0"));
                    return true;
                }
                if(players.getHometown().removeCityMoney(value)){
                    players.getHometown().getNation().addCityMoney(value);
                    player.sendMessage(Skills.getInstance().message("Messages.Nation.deposit.success").replace("{money}", String.valueOf(value)));
                    ChatAPI.sendServerMessage(players.getHometown().getNation(), Skills.getInstance().message("Messages.Nation.deposit.nationMessage").replace("{money}", String.valueOf(value)).replace("{nation}", players.getHometown().getNation().getNationName()));
                }
                else {
                    player.sendMessage(Skills.getInstance().message("Messages.Nation.deposit.toLowCityMoney"));
                }
            }
            if(args[0].equalsIgnoreCase("addCity")){
                if(!(players.getHometown() != null && players.getHometown().getNation() != null)){
                    player.sendMessage(Skills.getInstance().message("Messages.Nation.NoNation"));
                    return true;
                }
                if(!players.getHometown().getNation().isAdmin(player.getUniqueId())){
                    player.sendMessage(Skills.getInstance().message("Messages.Nation.noVize"));
                    return true;
                }
                City offerd = CityMapper.getByName(args[1]);
                if(offerd == null){
                    player.sendMessage(Skills.getInstance().message("Messages.Nation.noCity").replace("{name}", args[1]));
                    return true;
                }
                if(offerd.getNation() != null){
                    player.sendMessage(Skills.getInstance().message("Messages.Nation.addCity.isInNation"));
                    return true;
                }
                if(!offerd.adminOnline()){
                    player.sendMessage(Skills.getInstance().message("Messages.Nation.addCity.noAdmin"));
                    return true;
                }
                new nationOffer(offerd).addOffer(players.getHometown().getNation());
            }
            if(args[0].equalsIgnoreCase("accept")) {
                if (players.getHometown() == null) {
                    player.sendMessage(getInstance().message("Messages.City.noCity"));
                    return true;
                }
                if(players.getHometown().getNation() != null){
                    player.sendMessage(getInstance().message("Messages.Nation.create.isInNation"));
                }
                City city = players.getHometown();
                nationOffer cityMapper = new nationOffer(players.getHometown());
                if (cityMapper.hasOffer()) {
                    for (Nation nation : cityMapper.getOffers()) {
                        if (nation.getNationName().equals(args[1])) {
                            nation.addCity(city);
                            cityMapper.offersDone();
                            ChatAPI.sendServerMessage(nation, Skills.getInstance().message("Messages.Nation.accept.nationMessage").replace("{city}", city.getCityName()));
                        }
                    }
                }
            }
            if(args[0].equalsIgnoreCase("kick")) {
                if (players.getHometown() == null || players.getHometown().getNation() == null) {
                    player.sendMessage(getInstance().message("Messages.Nation.NoNation"));
                    return true;
                }
                Nation nation = players.getHometown().getNation();
                if (!nation.isAdmin(player.getUniqueId())) {
                    player.sendMessage(getInstance().message("Messages.Nation.noVize"));
                    return true;
                }
                City city = CityMapper.getByName(args[1]);
                if (city == null) {
                    player.sendMessage(getInstance().message("Messages.Nation.noCity").replace("{name}", args[1]));
                    return true;
                }
                if (!nation.getCityList().containsKey(city)) {
                    player.sendMessage(Skills.getInstance().message("Messages.Nation.kick.cityNotInNation").replace("{city}",city.getCityName()));
                    return true;
                }
                city.setNation(null);
                nation.removeCity(city);
                ChatAPI.sendServerMessage(nation, Skills.getInstance().message("Messages.Nation.kick.nationMessage").replace("{city}", city.getCityName()));
                return true;
            }
            if(args[0].equalsIgnoreCase("leave")){
                if (players.getHometown() == null || players.getHometown().getNation() == null) {
                    player.sendMessage(getInstance().message("Messages.Nation.NoNation"));
                    return true;
                }
                if(!players.getHometown().hasAdmin(player.getUniqueId())){
                    player.sendMessage(Skills.getInstance().message("Messages.City.noVize"));
                }
                if(CityMapper.getNByName(args[1]) != null && CityMapper.getNByName(args[1]) == players.getHometown().getNation()){
                    City city = players.getHometown();
                    Nation nation = players.getHometown().getNation();
                    city.setNation(null);
                    nation.removeCity(city);
                    ChatAPI.sendServerMessage(city, Skills.getInstance().message("Messages.Nation.leave.cityMessage").replace("{nation}",nation.getNationName()));
                    ChatAPI.sendServerMessage(nation,Skills.getInstance().message("Messages.Nation.leave.nationMessage").replace("{city}", city.getCityName()));
                    return true;
                }
                else {
                    player.sendMessage(Skills.getInstance().message("Messages.Nation.leave.wrongCity"));
                }
            }
            if(args[0].equalsIgnoreCase("vize")) {
                if (players.getHometown() == null || players.getHometown().getNation() == null) {
                    player.sendMessage(getInstance().message("Messages.Nation.NoNation"));
                    return true;
                }
                Nation nation = players.getHometown().getNation();
                if (!nation.getKing().equals(player.getUniqueId())) {
                    player.sendMessage(Skills.getInstance().message("Messages.Nation.noKing"));
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
                    String a = Skills.getInstance().message("Messages.Nation.vize.playerNotInNation");
                    player.sendMessage(a.replace("{name}", player1.getName()));
                    return true;
                }
                if (nation.isVize(player1.getUniqueId())) {
                    nation.remobeVize(player1.getUniqueId());
                    String a = Skills.getInstance().message("Messages.Nation.vize.take");
                    player.sendMessage(a.replace("{name}", player1.getName()));
                    ChatAPI.sendServerMessage(nation, Skills.getInstance().message("Messages.Nation.vize.cityMessage.remove").replace("{name}", player1.getName()));
                } else {
                    nation.addVize(player1.getUniqueId());
                    String a = Skills.getInstance().message("Messages.Nation.vize.set");
                    player.sendMessage(a.replace("{name}", player1.getName()));
                    ChatAPI.sendServerMessage(nation, Skills.getInstance().message("Messages.Nation.vize.cityMessage.add").replace("{name}", player1.getName()));
                    return true;
                }

            }
            if(args[0].equalsIgnoreCase("withdraw")){
                if (players.getHometown() == null || players.getHometown().getNation() == null) {
                    player.sendMessage(getInstance().message("Messages.Nation.NoNation"));
                    return true;
                }
                Nation nation = players.getHometown().getNation();
                if (!nation.isAdmin(player.getUniqueId())) {
                    player.sendMessage(getInstance().message("Messages.Nation.noVize"));
                    return true;
                }
                int value = Integer.parseInt(args[1]);
                if(value <= 0){
                    player.sendMessage(Skills.getInstance().message("Messages.City.withdraw.illegalValue"));
                    return true;
                }
                if (players.getHometown().removeCityMoney(value)) {
                    players.addMoney(value);
                    player.sendMessage(Skills.getInstance().message("Messages.Nation.withdraw.success").replace("{value}", String.valueOf(value)));
                    ChatAPI.sendServerMessage(players.getHometown(), Skills.getInstance().message("Messages.Nation.withdraw.cityMessage").replace("{value}", String.valueOf(value)).replace("{name}", player.getName()));
                } else {
                    player.sendMessage(Skills.getInstance().message("Messages.Nation.withdraw.lowMoney"));
                    return true;
                }
            }
            if(args[0].equalsIgnoreCase("makeOwner")){
                if (players.getHometown() == null || players.getHometown().getNation() == null) {
                    player.sendMessage(getInstance().message("Messages.Nation.NoNation"));
                    return true;
                }
                Nation nation = players.getHometown().getNation();
                if (!nation.getKing().equals(player.getUniqueId())) {
                    player.sendMessage(Skills.getInstance().message("Messages.Nation.noKing"));
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
                    String a = Skills.getInstance().message("Messages.Nation.vize.playerNotInNation");
                    player.sendMessage(a.replace("{name}", player1.getName()));
                    return true;
                }
                if(!nation.isVize(player1.getUniqueId())){
                    player.sendMessage(Skills.getInstance().message("Messages.Nation.makeOwner.playerNotVize").replace("{name}", player1.getName()));
                }
                nation.setKing(players1.getUUID());
                ChatAPI.sendServerMessage(nation, Skills.getInstance().message("Messages.Nation.makeOwner.nationMessage").replace("{name}", player1.getName()));
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
            return onTabComplete;
        }
        Players players = KlassChoose.getPlayers(player);
        City hometown = players.getHometown();
        Nation nation = hometown != null ? hometown.getNation() : null;
        if (args.length == 1) {
            if (hometown != null) {
                UUID playerUUID = player.getUniqueId();

                if (nation == null && hometown.getBuergermeisterUUID().equals(playerUUID)) {
                    onTabComplete.addAll(Arrays.asList("create", "accept"));
                } else if (nation != null) {
                    if (hometown.hasAdmin(playerUUID)) {
                        onTabComplete.addAll(Arrays.asList("deposit", "leave"));
                    }
                    if (nation.isAdmin(playerUUID)) {
                        onTabComplete.addAll(Arrays.asList("addCity", "withdraw", "claim"));
                    }
                    if (nation.getKing().equals(playerUUID)) {
                        onTabComplete.addAll(Arrays.asList("makeOwner", "vize", "delete"));
                    }
                }
            }
        }
        if (args.length == 2) {
            if (hometown != null) {
                if (nation == null) {
                    if (args[0].equals("create")) {
                        onTabComplete.add("<Name>");
                    }
                    if(args[0].equals("accept")) {
                        List<Nation> nationOffers = new nationOffer(hometown).getOffers();
                        if (nationOffers != null) {
                            for (Nation offer : nationOffers) {
                                onTabComplete.add(offer.getNationName());
                            }
                        }
                    }
                } else {
                    UUID playerUUID = player.getUniqueId();

                    if (hometown.hasAdmin(playerUUID) && args[0].equals("deposit")) {
                        onTabComplete.add("<Betrag>");
                    }
                    if (nation.isAdmin(playerUUID) && args[0].equals("addCity")) {
                        for (City city : CityMapper.getCityList()) {
                            onTabComplete.add(city.getCityName());
                        }
                    }
                    if (args[0].equals("withdraw") && nation.isAdmin(playerUUID)) {
                        onTabComplete.add("<Betrag>");
                    }
                    if (nation.getKing().equals(playerUUID) && args[0].equals("makeowner")) {
                        nation.getVizeList().stream()
                                .map(Bukkit::getOfflinePlayer)
                                .map(OfflinePlayer::getName)
                                .forEach(onTabComplete::add);
                    }

                }
            }
        }
        return onTabComplete;
    }

    public static String processCityPlayers(HashMap<City, List<UUID>> cityUUIDMap) {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<City, List<UUID>> entry : cityUUIDMap.entrySet()) {
            City city = entry.getKey();
            List<UUID> uuids = entry.getValue();

            result.append(city.getCityName()).append("(");
            for (int i = 0; i < uuids.size(); i++) {
                UUID uuid = uuids.get(i);
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                String playerName = offlinePlayer.getName();

                result.append(playerName);
                if(i + 1 < uuids.size()){
                    result.append(", ");
                }
            }
            result.append(") ");
        }

        return result.toString();
    }

    public static boolean canClaimChunk(Chunk chunk, Player player){
        Players players = KlassChoose.getPlayers(player);
        Nation nation = players.getHometown().getNation();
        boolean isNearTown = false;
        boolean hasEnemyTownNear = false;
        if (CityMapper.ChunkClaimed(chunk)) {
            if(Objects.equals(CityMapper.whichNationClaimed(chunk), nation)){
                player.sendMessage(Skills.getInstance().message("Messages.Nation.claim.selfClaim"));
            }
            else if(nation.isNationChunk(chunk)){
                player.sendMessage(Skills.getInstance().message("Messages.Nation.claim.NCityClaim"));
            }
            else {
                player.sendMessage(Objects.requireNonNull(getInstance().getConfig().getString("Messages.City.claim.ChunkClaimedOther")));
            }
            return false;
        }
        for (Chunk nearChunck : CityCommand.koordinatenImUmkreis(chunk, Skills.getInstance().getConfig().getInt("Values.Radius.NearNationEnemy"))) {
            if (CityMapper.ChunkClaimed(nearChunck)) {
                if (!Objects.requireNonNull(CityMapper.whoClaimed(nearChunck)).getNation().equals(nation) || !Objects.equals(CityMapper.whichNationClaimed(nearChunck), nation)) {
                    player.sendMessage(Objects.requireNonNull(getInstance().getConfig().getString("Messages.City.claim.ChunkNearOther")));
                    hasEnemyTownNear = true;
                    break;
                }
            }
        }
        for (Chunk nearChunk : CityCommand.koordinatenImUmkreis(chunk, Skills.getInstance().getConfig().getInt("Values.Radius.NearNationTown"))) {
            if (CityMapper.ChunkClaimed(nearChunk)) {
                if (nation.isNationChunk(chunk)) {
                    isNearTown = true;
                }
            }
        }
        if (hasEnemyTownNear) {
            return false;
        } else {
            if (isNearTown) {
                if(!nation.payMoney(getInstance().value("Values.Money.claimChunk"))){
                    player.sendMessage(getInstance().message("Messages.Nation.claim.notEnoughMoney"));
                }
                else {
                    return true;
                    }
            } else {
                String a = getInstance().message("Messages.Nation.claim.tooFar");
                a = a.replace("{chunk}", String.valueOf(getInstance().value("Values.Radius.NearNationTown")));
                player.sendMessage(a);
            }
        }
        return false;
    }
}
