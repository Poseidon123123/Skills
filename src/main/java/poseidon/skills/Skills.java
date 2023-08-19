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
        getServer().getPluginManager().registerEvents(new Testlistener(), this);
        getServer().getPluginManager().registerEvents(new SkillListener(), this);
        getServer().getPluginManager().registerEvents(new XPListener(), this);
    }

    @Override
    public void onDisable() {
        JSONSave.saveCitys();
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
        addDefault("Funktions.XP", "(x+10)^2+50");
        addDefault("Messages.City.create.isInCity", "§4Du bist schon in einer Stadt!");
        addDefault("Messages.City.create.notMainWorld", "§4Du bist in der Farmwelt, du kannst hier keine Stadt gründen!");
        addDefault("Messages.City.create.succsess", "§2Herzlichen Glückwunsch du hast die Stadt {name} erfolgreich gegründet");
        addDefault("Messages.City.PlayerHasNoTown", "§4Um dass zu tun musst du in einer Stadt sein");
        addDefault("Messages.City.deposit.notOver0", "§4Um Geld in die Stadtkasse einzuzahlen zu können, muss der Betrage über 0 sein!");
        addDefault("Messages.City.deposit.sucsess", "§2Du hast {money} in deine Stadtkasse eingezahlt");
        addDefault("Messages.City.invite.noCity","§4Du musst in einer Stadt sein");
        addDefault("Messages.City.invite.noMeister","§4Du musst Bürgermeister sein!");
        addDefault("Messages.General.noPlayerFound", "§4Es gibt keinen Spieler mit dem Namen {name} auf dem Server");
        addDefault("Messages.City.accept.succsess", "§2Du bist nun Bürger der Stadt {city}");
        addDefault("Messages.City.create.NoMoney", "§4Du hast nicht genug Geld, du braucht {money}");
        addDefault("Messages.City.claim.ChunkClaimedSelf", "§4Dieser Chunk gehört deiner Stadt schon");
        addDefault("Messages.City.claim.ChunkClaimedOther", "§4Dieser Chunk gehört schon einer anderen Stadt");
        addDefault("Messages.City.claim.ChunkNearOther", "§4Ein Chunk in der Nähe gehört schon einer anderen Stadt");
        addDefault("Messages.City.claim.succsess", "§2Du hast diesen Chunk beansprucht");
        addDefault("Messages.City.claim.tooFar", "§2Du kannst nur Chunks im Umkreis von {chunk} Chunk beanspruchen");
        addDefault("Messages.City.setMessage", "§2Du hast die Begrüßungsnachricht {message} gesetzt");
        addDefault("Messages.City.info", "§6Stadt: {city} \nMoney: {money} \nBürgermeister: {bugerMeister} \nBürger: {listburger}");
        addDefault("Messages.Class.BerufSuccsess", "§1Du bist nun ein {Beruf}");
        addDefault("Messages.Class.noClassFouned", "§4Kein Klasse gefunden");
        addDefault("Messages.Class.KampfSuccsess", "§1Du bist nun ein {Kampf}");
        addDefault("Messages.Offer.noItemInHand", "§4Halte ein Item in der Hand");
        addDefault("Messages.Offer.OfferAccepted", "§aDein Angebot wurde angenommen");
        addDefault("Messages.Offer.noMoney", "§4Du hast nicht genug Money");
        addDefault("Messages.Offer.OfferRejected", "§4Dein Angebot wurde abgelehnt");
        addDefault("Messages.Pay.lowMoney", "§4Du kannst einem Spieler nur einen positiven Betrage Zahlen");
        addDefault("Messages.Pay.reciveMoney", "§6Der Spieler {name} hat dir {money} Money gegeben");
        addDefault("Messages.Pay.giveMoney", "§6Du hast dem Spieler {name} {money} Money gegeben");
        addDefault("Messages.Pay.noMoney", "§4Du hast nicht genug Geld");
        addDefault("Messages.City.CityExists", "§4Es exisitert schon eine Stadt mit dem Namen {city}");
        addDefault("Event.NotInCity.Break", "§8Du darfst hier nicht abbauen");
        addDefault("Event.NotInCity.Place", "§8Du darfst hier nicht platzieren");
        addDefault("Values.Radius.NearTown", 1);
        addDefault("Values.Radius.NearEnemy", 5);
        addDefault("Values.Money.CreateCity", 5000);
        addDefault("Values.Money.claimChunk", 1000);
        addDefault("Values.Cooldown.BerufChange", 2);
        addDefault("Values.Cooldown.KampfChange", 2);
        addDefault("Values.Cooldown.TimerCheck", 12000);
        addDefault("Funktions.FarmWorld.MainWorldName", "");
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
    }
}
