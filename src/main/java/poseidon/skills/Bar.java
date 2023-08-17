package poseidon.skills;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Bar {
    private final Plugin plugin;
    private BossBar bossBar;
    private int ID = 0;

    public Bar(Plugin plugin) {
        this.plugin = plugin;
    }

    public void addPlayer(Player player,String title, double progress, int delay){
        if(!bossBar.getPlayers().contains(player)) {
            bossBar.addPlayer(player);
        }
        bossBar.setVisible(true);
        bossBar.setTitle(title);
        if(ID == 0) {
            Bukkit.getScheduler().cancelTask(ID);
        }
        cast(progress,delay);
    }
    public void addPlayer(Player player,String title, double progress){
        if(!bossBar.getPlayers().contains(player)) {
            bossBar.addPlayer(player);
        }
        bossBar.setVisible(true);
        bossBar.setTitle(title);
        if(ID == 0) {
            Bukkit.getScheduler().cancelTask(ID);
        }
        cast(progress);
    }

    public BossBar getBar(){
        return bossBar;
    }
    public void cast(double progress, int delay){
        bossBar.setProgress(progress);
        ID = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> bossBar.setVisible(false),  delay);
    }
    public void cast(double progress){
        bossBar.setProgress(progress);
        ID = 0;
    }

    public void createBar(String Name, BarColor color){
        bossBar = Bukkit.createBossBar(Name, color, BarStyle.SOLID);
        bossBar.setVisible(true);
    }

    public void deleteBar(){
        bossBar.setVisible(false);
    }
}
