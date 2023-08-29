package poseidon.skills;

import org.bukkit.Bukkit;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import poseidon.skills.Commands.*;
import poseidon.skills.JSON.JSONLoad;
import poseidon.skills.JSON.JSONSave;
import poseidon.skills.Klassen.Berufklasse;
import poseidon.skills.Klassen.Kampfklassen;
import poseidon.skills.Listners.SkillListener;
import poseidon.skills.Listners.Testlistener;
import poseidon.skills.Listners.XPListener;

import java.util.Objects;

public final class Skills extends JavaPlugin {
    private static Skills instances;
    @Override
    public void onEnable() {
        // Plugin startup logic
        instances = this;
        starter();
        makeConfig();
        Farmwelt.farmWorld = Farmwelt.farmCreator(false).createWorld();
        registerCommand("Class", new ClassCommand());
        registerCommand("Skills", new SkillCommand());
        registerCommand("Sk", new SkillCommand());
        registerCommand("DB",new Debug());
        registerCommand("setClass",new SetClassCommand());
        registerCommand("pay", new PayCommand());
        registerCommand("Make", new MakeCommand());
        registerCommand("offer", new OfferCommand());
        registerCommand("City", new CityCommand());
        registerCommand("Farm", new FarmWeltCommand());
        registerCommand("Chat", new ChatCommand());
        registerCommand("Nation", new NationCommand());
        getServer().getPluginManager().registerEvents(new Testlistener(), this);
        getServer().getPluginManager().registerEvents(new SkillListener(), this);
        getServer().getPluginManager().registerEvents(new XPListener(), this);
    }

    @Override
    public void onDisable() {
        JSONSave.saveCitys();
        JSONSave.saveNations();
        if (!this.getServer().getOnlinePlayers().isEmpty()) {
            for (Player player : this.getServer().getOnlinePlayers()) {
                Testlistener.PlayerLeaveAction(player);
            }
        }
        getConfig().set("Funktions.FarmWorld.Name", Farmwelt.farmWorld.getName());
        Bukkit.unloadWorld(Farmwelt.farmWorld,true);
        saveConfig();
    }

    public static void registerCommand(String name, TabExecutor executor){
        Objects.requireNonNull(getInstance().getCommand(name)).setExecutor(executor);
        Objects.requireNonNull(getInstance().getCommand(name)).setTabCompleter(executor);
    }

    public static Skills getInstance() {
        return instances;
    }

    public void makeConfig(){

        //Funktions
        addDefault("Funktions.XP", "(x+10)^2+50");
        addDefault("Funktions.FarmWorld.MainWorldName", "");
        //General
        addDefault("Messages.General.noPlayerFound", "§4Es gibt keinen Spieler mit dem Namen {name} auf dem Server");
        //City
        addDefault("Messages.City.create.isInCity", "§4Du bist schon in einer Stadt!");
        addDefault("Messages.City.create.notMainWorld", "§4Du bist in der Farmwelt, du kannst hier keine Stadt gründen!");
        addDefault("Messages.City.create.succsess", "§2Herzlichen Glückwunsch du hast die Stadt {name} erfolgreich gegründet");
        addDefault("Messages.City.deposit.notOver0", "§4Um Geld in die Stadtkasse einzuzahlen zu können, muss der Betrage über 0 sein!");
        addDefault("Messages.City.deposit.succsess", "§2Du hast {money} in deine Stadtkasse eingezahlt");
        addDefault("Messages.City.deposit.cityMessage", "§2Der Spieler {name} hat {money} in die Stadtkasse eingezahlt!");
        addDefault("Messages.City.accept.cityMessage", "§2Der Spieler {name} ist jetzt Teil dieser Stadt");
        addDefault("Messages.City.vize.cityMessage.add", "§2Der Spieler {name} ist nun Stellvertreter");
        addDefault("Messages.City.vize.cityMessage.remove","§4Der Spieler {name} ist nun kein Stellvertreter mehr");
        addDefault("Messages.City.noCity","§4Du musst in einer Stadt sein");
        addDefault("Messages.City.noMeister","§4Du musst Bürgermeister sein!");
        addDefault("Messages.City.noVize", "$4Du musst Bürgermeister oder Stellvertreter sein!");
        addDefault("Messages.City.accept.succsess", "§2Du bist nun Bürger der Stadt {city}");
        addDefault("Messages.City.create.NoMoney", "§4Du hast nicht genug Geld, du braucht {money}");
        addDefault("Messages.City.claim.ChunkClaimedSelf", "§4Dieser Chunk gehört deiner Stadt schon");
        addDefault("Messages.City.claim.ChunkClaimedOther", "§4Dieser Chunk gehört schon einer anderen Stadt oder Nation");
        addDefault("Messages.City.claim.ChunkNearOther", "§4Ein Chunk in der Nähe gehört schon einer anderen Stadt oder Nation");
        addDefault("Messages.City.claim.succsess", "§2Du hast diesen Chunk beansprucht");
        addDefault("Messages.City.claim.claimedByNation","§9Dieser Chunk gehört schon deiner Nation");
        addDefault("Messages.City.claim.tooFar", "§4Du kannst nur Chunks im Umkreis von {chunk} Chunk um deine Stadt beanspruchen");
        addDefault("Messages.City.claim.cityMessage", "§2Der Spieler {name} hat den Chunk {cords} für die Stadt beansprucht");
        addDefault("Messages.City.setMessage", "§2Du hast die Begrüßungsnachricht {message} gesetzt");
        addDefault("Messages.City.leave.noCity", "§2Schreibe deinen Stadtnamen hinter 'leave' um zu bestätigen dass du die Stadt verlassen willst");
        addDefault("Messages.City.leave.wrongCity", "§4Stadtname falsch versuches es nochmal");
        addDefault("Messages.City.leave.success", "§4Du hast die Stadt {cityName} verlassen");
        addDefault("Messages.City.leave.cityMessage", "§4Der Spieler {name} hat die Stadt verlassen");
        addDefault("Messages.City.toggleVize.set", "§2Du hast den Spieler {name} zum Stellvertreter gemacht");
        addDefault("Messages.City.toggleVize.take", "§2Du hast dem Spieler {name} den Stellvertreter-Titel entfernt");
        addDefault("Messages.City.kick.PlayerNotInCity", "$4Der Spieler {player} ist nicht in deiner Stadt!");
        addDefault("Messages.City.kick.cityMessage", "§4Der Spieler {name} wurde aus der Stadt geworfen");
        addDefault("Messages.City.CityExists", "§4Der Name {city} ist schon vergeben!");
        addDefault("Messages.City.withdraw.cityMessage", "§2Der Spieler {name} hat {value} aus der Stadtkasse genommen");
        addDefault("Messages.City.withdraw.success", "§2Du hast {value} aus der Stadtkasse genommen");
        addDefault("Messages.City.withdraw.lowMoney", "§4Deine Stadt hat nich genug Geld in der Stadtkasse dafür");
        addDefault("Messages.City.withdraw.illegalValue", "§4Der Betrag zum Abheben muss über 0 sein");
        addDefault("Messages.City.info", "§7===============\n§6Stadt: {city} \nMoney: {money} \nBürgermeister: {bugerMeister} \nStellvertreter: {vize} \nBürger: {listburger}\nNation: {nation}\n§7===============");
        addDefault("Messages.City.makeOwner.playerNotVize", "Der Spieler {name} muss Stellvertreter sein, bevor er Bürgermeister werden kann");
        addDefault("Messages.City.makeOwner.cityMessage", "Der Spieler {name} ist nun der neue Bürgermeister");
        //Class
        addDefault("Messages.Class.BerufSuccsess", "§1Du bist nun ein {Beruf}");
        addDefault("Messages.Class.noClassFouned", "§4Kein Klasse gefunden");
        addDefault("Messages.Class.NoTime", "§4Dein ChangeCooldown ist noch nicht abgelaufen!");
        addDefault("Messages.Class.KampfSuccsess", "§1Du bist nun ein {Kampf}");
        //Offer
        addDefault("Messages.Offer.noItemInHand", "§4Halte ein Item in der Hand");
        addDefault("Messages.Offer.OfferAccepted", "§aDein Angebot wurde angenommen");
        addDefault("Messages.Offer.noMoney", "§4Du hast nicht genug Money");
        addDefault("Messages.Offer.OfferRejected", "§4Dein Angebot wurde abgelehnt");
        //Pay
        addDefault("Messages.Pay.lowMoney", "§4Du kannst einem Spieler nur einen positiven Betrage Zahlen");
        addDefault("Messages.Pay.reciveMoney", "§6Der Spieler {name} hat dir {money} Money gegeben");
        addDefault("Messages.Pay.giveMoney", "§6Du hast dem Spieler {name} {money} Money gegeben");
        addDefault("Messages.Pay.noMoney", "§4Du hast nicht genug Geld");
        //Nation
        addDefault("Messages.Nation.NoNation", "§4Du musst einer Stadt angehören und deine Stadt muss einer Nation angehören um das Verwenden zu können!");
        addDefault("Messages.Nation.noVize", "§4Du musst Nationsanführer oder Stellvertreter sein um das machen zu können");
        addDefault("Messages.Nation.noKing", "§4Du musst Nationsanführer sein um das machen zu können");
        addDefault("Messages.Nation.create.succsess", "§2Du hast die Nation {nation} gegründet!");
        addDefault("Messages.Nation.create.inValidName", "§4Der Name deiner Nation ist schon vergeben!");
        addDefault("Messages.Nation.create.isInNation", "§4Du deine Stadt ist schon Teil einer Nation");
        addDefault("Messages.Nation.create.noMoney", "§4Deine Stadt hat nicht genug Geld, eine Nation kostet {value}");
        addDefault("Messages.Nation.Info", "§7===============\n§6Nation: {nation} \nMoney: {money} \nKönig: {king} \nBürger: {listburger}\n§7===============");
        addDefault("Messages.Nation.deposit.under0", "§4Um Geld einzahlen zu können muss der Betrag über 0 sein");
        addDefault("Messages.Nation.deposit.toLowCityMoney", "§4Es ist zuwenig Geld in der Stadtkasse!");
        addDefault("Messages.Nation.deposit.success", "§2Du hast {money} in die Nationskasse von deiner Stadtkasse übertragen");
        addDefault("Messages.Nation.deposit.nationMessage", "§9Es wurde {money} von der Nation {nation} in die Nationkasse eingezahlt");
        addDefault("Messages.Nation.noCity", "§4Es gibt keine Stadt mit dem Namen {name}");
        addDefault("Messages.Nation.addCity.noAdmin", "§4Es ist weder der Bürgermeister noch ein Stellvertreter zu finden");
        addDefault("Messages.Nation.addCity.reciveOffer", "§9Die Nation {nation} möchte, dass ihr Teil deren Reiches werdet");
        addDefault("Messages.Nation.addCity.isInNation", "§4Diese Stadt ist schon in einer Nation");
        addDefault("Messages.Nation.accept.nationMessage", "§2Die Stadt {city} ist der Nation beigetreten!");
        addDefault("Messages.Nation.kick.cityNotInNation", "§4Die Stadt {city} ist nicht in dieser Nation");
        addDefault("Messages.Nation.kick.nationMessage", "$2Die Stadt {city} wurde aus der Nation geworfen");
        addDefault("Messages.Nation.leave.nationMessage", "§2Die Stadt {city} hat die Nation verlassen");
        addDefault("Messages.Nation.leave.cityMessage", "§2Diese Stadt hat sich von der Nation {nation} freigesagt");
        addDefault("Messages.Nation.leave.wrongCity", "§2Der Name der Nation ist falsch versuche es erneut");
        addDefault("Messages.Nation.vize.playerNotInNation","§4Der Spieler {name} ist nicht in deiner Nation");
        addDefault("Messages.Nation.vize.set", "§2Du hast den Spieler {name} zum Nationsstellvertreter gemacht");
        addDefault("Messages.Nation.vize.take", "§2Du hast dem Spieler {name} den Nationsstellvertreter-Titel entfernt");
        addDefault("Messages.Nation.vize.cityMessage.add", "§2Der Spieler {name} ist nun Nationsstellvertreter");
        addDefault("Messages.Nation.vize.cityMessage.remove","§4Der Spieler {name} ist nun kein Nationsstellvertreter mehr");
        addDefault("Messages.Nation.withdraw.lowMoney", "§4Deine Nation hat nich genug Geld in der Stadtkasse dafür");
        addDefault("Messages.Nation.withdraw.success", "§2Du hast {value} aus der Nationskasse genommen");
        addDefault("Messages.Nation.withdraw.cityMessage", "§2Der Spieler {name} hat {value} aus der Nationskasse genommen");
        addDefault("Messages.Nation.claim.selfClaim", "§4Dieser der Chunk gehört schon deiner Nation");
        addDefault("Messages.Nation.claim.NCityClaim", "§4Dieser Chunk wurde einer Stadt eurer Nation beansprucht");
        addDefault("Messages.Nation.claim.notEnoughMoney", "§4Deine Nation hat nicht genug Geld um diesen Chunk zu beanspruchen");
        addDefault("Messages.Nation.claim.success", "§2Du hast diesen Chunk für deine Nation beansprucht");
        addDefault("Messages.Nation.claim.cityMessage", "§2Der Spieler {name} hat den Chunk {chunk} für die Nation beansprucht");
        addDefault("Messages.Nation.claim.tooFar", "§4Du kannst nur Chunks im Umkreis von {chunk} Chunk einen Chunk, der zu deiner Nation gehört, beanspruchen");
        addDefault("Messages.Nation.makeOwner.playerNotVize","$4Der Spieler {name} muss erst Stellvertreter sein bevor er Anführer werden kann");
        addDefault("Messages.Nation.makeOwner.nationMessage", "§2Der Spieler {name} ist nun der neue Anführer");
        //Format
        addDefault("Format.Chat.GlobalChat", "§2[G]{name}: {message}");
        addDefault("Format.Chat.NationChat", "$9[N]{name}: {message}");
        addDefault("Format.Chat.CityChat", "§6[S]{name}: {message}");
        addDefault("Format.Chat.LocalChat", "[L]{name}: {message}");
        addDefault("Format.Chat.WispherChat", "§8[W]{name}: {message}");
        addDefault("Format.Chat.ScreamChat", "§l[S]{name}: {message}");
        addDefault("Format.Chat.HelpChat", "§c[Help]{name}: {message}");
        addDefault("Format.Chat.AdminChat", "§5[Admin]{name}: {message}");
        addDefault("Format.Chat.TradeChat", "§b[H]{name}: {message}");
        //Event
        addDefault("Event.NotInCity.Break", "§8Du darfst hier nicht abbauen");
        addDefault("Event.NotInCity.Place", "§8Du darfst hier nicht platzieren");
        //Values
        addDefault("Values.Radius.NearTown", 1);
        addDefault("Values.Radius.NearEnemy", 5);
        addDefault("Values.Radius.NearNationTown", 1);
        addDefault("Values.Radius.NearNationEnemy", 10);
        addDefault("Values.Money.CreateCity", 5000);
        addDefault("Values.Money.claimChunk", 1000);
        addDefault("Values.Money.CreateNation", 60000);
        addDefault("Values.Cooldown.BerufChange", 2);
        addDefault("Values.Cooldown.KampfChange", 2);
        addDefault("Values.Cooldown.TimerCheck", 12000);
        addDefault("Values.Chat.Range.LocalChat", 10);
        addDefault("Values.Chat.Range.WispherChat", 5);
        addDefault("Values.Chat.Range.ScreamChat", 60);
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private void addDefault(String path, String value){
        getConfig().addDefault(path,value);
    }
    private void addDefault(String path, int value){
        getConfig().addDefault(path,value);
    }

    public String message(String path){
        return Objects.requireNonNull(getConfig().getString(path));
    }
    public int value(String path){
        return getConfig().getInt(path);
    }

    public static void starter() {
        JSONSave.start();
        Berufklasse.klassStartup();
        Kampfklassen.klassStartup();
        JSONLoad.loadKlassen();
        JSONLoad.loadBerufSkills();
        JSONLoad.loadKampfSkills();
        JSONLoad.loadMobXP();
        JSONLoad.loadXP();
        ManaMap.startRegenerateMana();
        JSONLoad.loadCitys();
        JSONLoad.loadCustomItems();
        JSONLoad.loadRecipes();
        CooldownSystem.performActionAndCleanup();
        JSONLoad.loadNations();
        JSONLoad.loadTyps();
    }
}
