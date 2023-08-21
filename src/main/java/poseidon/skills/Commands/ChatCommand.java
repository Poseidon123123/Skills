package poseidon.skills.Commands;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import poseidon.skills.Chat.ChatAPI.Chats;
import poseidon.skills.Klassen.KlassChoose;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Du musst ein Spieler sein!");
            return false;
        }
        if (args.length == 0) {
            //TODO ChatChoseGUI
        }
        if (args.length == 1) {
            List<String> s = new ArrayList<>();
            for (Chats chats : Chats.values()) {
                s.add(chats.getKurz());
                if (chats.getKurz().equalsIgnoreCase(args[0])) {
                    KlassChoose.getPlayers(player).setChat(chats);
                    player.sendMessage("Dein Chat wurde zu " + chats.getKurz() + " geändert");
                    return true;
                }
            }
            player.sendMessage("Kein Chat erkannt, mögliche Chats sind: " + s);
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("Show")) {
                for (Chats chats : Chats.values()) {
                    if (chats.getPermission() != null && Objects.equals(chats.getKurz(), args[1])) {
                        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
                        if (provider != null) {
                            LuckPerms api = provider.getProvider();
                            Node node = Node.builder(chats.getPermission()).build();
                            api.getUserManager().modifyUser(player.getUniqueId(), (User user) -> {
                                // Try to add the node to the user.
                                DataMutateResult result = user.data().add(node);

                                // Check to see the result of adding the node.
                                if (result.wasSuccessful()) {
                                    player.sendMessage("Du kannst jetzt den Chat:" + chats.getKurz() + " lesen");
                                }
                            });
                        }
                    }
                }
            }
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}

