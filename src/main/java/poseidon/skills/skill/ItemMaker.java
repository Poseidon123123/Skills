package poseidon.skills.skill;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Objects;

public class ItemMaker {
    protected static ItemStack createGUIItems(final Material material, final String name, final String... Lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(Lore));
        item.setItemMeta(meta);
        return item;
    }
    protected static ItemStack createGUIItems(final Material material, final String name, ChatColor color, final String... Lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(color + name);
        meta.setLore(Arrays.asList(Lore));
        item.setItemMeta(meta);
        return item;
    }

    protected static ItemStack makeSkillItem(final ItemStack itemStack){
        final ItemMeta meta = itemStack.getItemMeta();
        Objects.requireNonNull(meta).addEnchant(Enchantment.RIPTIDE, 10, true);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
