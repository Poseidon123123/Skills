package poseidon.skills.Listners;

import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
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
import poseidon.skills.XPObjekt;

import java.util.Objects;

import static poseidon.skills.Klassen.Berufklasse.Unchosed;
import static poseidon.skills.Klassen.Berufklasse.XPSource;
import static poseidon.skills.XPMapper.xpAdd;


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
                XPObjekt xpObjekt = XPObjekt.getByMaterial(item.getItemStack().getType());
                if(xpObjekt == null){
                    return;
                }
                if(!xpObjekt.getXpSource().equals(XPSource.Fishing)){
                    return;
                }
                if (xpObjekt.getXp() > 0) {
                    xpAdd(KlassChoose.getPlayers(event.getPlayer()), xpObjekt.getXp(), true);
                }
                if(xpObjekt.getMoney() > 0){
                    KlassChoose.getPlayers(event.getPlayer()).addMoney(xpObjekt.getMoney());
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
        XPObjekt xpObjekt = XPObjekt.getByMaterial(m);
        if(xpObjekt == null){
            return;
        }
        if(xpObjekt.getXpSource() != xpSource){
            return;
        }
        if(xpObjekt.getXp() > 0) {
            if(event.getBlock() instanceof Ageable ageable){
                if(ageable.getAge() == ageable.getMaximumAge()){
                    xpAdd(p, xpObjekt.getXp(), true);
                    if(xpObjekt.getMoney() > 0){
                        p.addMoney(xpObjekt.getMoney());
                    }
                }
            }
            else {
                xpAdd(p, xpObjekt.getXp(), true);
                if(xpObjekt.getMoney() > 0){
                    p.addMoney(xpObjekt.getMoney());
                }
            }
        }
    }

}
