package poseidon.skills.Listners;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import poseidon.skills.Klassen.Berufklasse;
import poseidon.skills.Klassen.KlassChoose;
import poseidon.skills.Klassen.Players;
import poseidon.skills.XPMapper;

import java.util.Objects;

import static poseidon.skills.Klassen.Berufklasse.Unchosed;
import static poseidon.skills.Klassen.Berufklasse.XPSource;
import static poseidon.skills.XPMapper.*;


public class XPListener implements Listener {


    @EventHandler(ignoreCancelled = true)
    public static void killEvent(EntityDeathEvent event){
        LivingEntity entity = event.getEntity();
        if(entity.getKiller() != null) {
            Players p = KlassChoose.getPlayers(Objects.requireNonNull(entity.getKiller()));
            if (p.getBerufklasse().equals(Unchosed)) {
                return;
            }
            int xp = XPMapper.getMobXP(entity.getType());
            xpAdd(p, xp, false);
        }
    }

    @EventHandler
    public static void fishEvent(PlayerFishEvent event){
        if(event.getState() == PlayerFishEvent.State.CAUGHT_FISH){
            if(KlassChoose.getPlayers(event.getPlayer()).getBerufklasse().getSource().equals(XPSource.Fishing)) {
                Item item = (Item) event.getCaught();
                assert item != null;
                if (XPMapper.getFishXP(item.getItemStack().getType()) > 0) {
                    xpAdd(KlassChoose.getPlayers(event.getPlayer()), getFishXP(item.getItemStack().getType()), true);
                }
            }
        }
    }

    @EventHandler
    public static void breakEvent(BlockBreakEvent event){
        Player player = event.getPlayer();
        Berufklasse.XPSource xpSource = KlassChoose.getPlayers(player).getBerufklasse().getSource();
        Material m = event.getBlock().getType();
        Players p = KlassChoose.getPlayers(player);
        switch (xpSource){
            case Wooding -> {
                if(getWoodXP(m) > 0){
                    xpAdd(p, getWoodXP(m), true);
                }
            }
            case Mining ->{
                if(getMineXP(m) > 0){
                    xpAdd(p, getMineXP(m), true);
                }
            }
            case Farming -> {
                if(getFarmXP(m) > 0){
                    xpAdd(p, getFishXP(m), true);
                }
            }

        }
    }

}
