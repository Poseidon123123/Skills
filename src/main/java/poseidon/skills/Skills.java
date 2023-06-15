package poseidon.skills;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import poseidon.skills.Commands.AktivSkillCommand;
import poseidon.skills.Commands.ClassCommand;
import poseidon.skills.Commands.Debug;
import poseidon.skills.Commands.SkillCommand;
import poseidon.skills.JSON.JSONLoad;
import poseidon.skills.JSON.JSONSave;
import poseidon.skills.Klassen.Berufklasse;
import poseidon.skills.Klassen.Kampfklassen;
import poseidon.skills.TabCompleter.KlassTabCompleter;
import poseidon.skills.TabCompleter.SkillTabCompleter;

import java.util.Objects;

public final class Skills extends JavaPlugin {
    private static Skills instances;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instances = this;
        starter();
        Objects.requireNonNull(this.getCommand("Class")).setExecutor(new ClassCommand());
        Objects.requireNonNull(this.getCommand("Class")).setTabCompleter(new KlassTabCompleter());
        Objects.requireNonNull(this.getCommand("Skills")).setExecutor(new SkillCommand());
        Objects.requireNonNull(this.getCommand("Skills")).setTabCompleter(new SkillTabCompleter());
        Objects.requireNonNull(this.getCommand("Sk")).setExecutor(new SkillCommand());
        Objects.requireNonNull(this.getCommand("Sk")).setTabCompleter(new SkillTabCompleter());
        Objects.requireNonNull(this.getCommand("ASC")).setExecutor(new AktivSkillCommand());
        Objects.requireNonNull(this.getCommand("DB")).setExecutor(new Debug());
        getServer().getPluginManager().registerEvents(new Testlistener(), this);
        getServer().getPluginManager().registerEvents(new SkillListener(), this);
    }

    @Override
    public void onDisable() {
        if(!this.getServer().getOnlinePlayers().isEmpty()){
            for(Player player: this.getServer().getOnlinePlayers()){
                JSONSave.playerSave(player);
            }
        }
    }

    public static Skills getInstance(){
        return instances;
    }

    public static void starter(){
        JSONSave.start();
        Berufklasse.klassStartup();
        Kampfklassen.klassStartup();
        JSONLoad.loadKlassen();
        JSONLoad.loadBerufSkills();
        JSONLoad.loadKampfSkills();
        JSONLoad.loadMobs();
    }
}
