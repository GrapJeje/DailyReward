package eu.camonetwork.dailyreward.infrastructure;

import eu.camonetwork.dailyreward.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class ItemBuilder {
    private ItemStack item = new ItemStack(Material.AIR);

    public ItemStack build() {
        return item;
    }

    public static ItemBuilder fromItem(ItemStack itemStack) {
        ItemBuilder itemBuilder = new ItemBuilder();
        itemBuilder.setItem(itemStack.clone());

        return itemBuilder;
    }

    public static ItemBuilder fromType(Material type) {
        ItemBuilder itemBuilder = new ItemBuilder();
        itemBuilder.setType(type);
        return itemBuilder;
    }

    public static ItemBuilder create() {
        return new ItemBuilder();
    }

    public OfflinePlayer getSkullPlayer() {
        SkullMeta skullMeta = (SkullMeta) item.getItemMeta();

        if (skullMeta == null) {
            return null;
        }

        return skullMeta.getOwningPlayer();
    }

    public ItemBuilder setSkullPlayer(OfflinePlayer skullPlayer) {
        item.setType(Material.PLAYER_HEAD);

        if (item.getItemMeta() != null) {
            SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
            skullMeta.setOwningPlayer(skullPlayer);

            item.setItemMeta(skullMeta);
        }

        return this;
    }

    public Material getType() {
        return item.getType();
    }

    public ItemBuilder setType(Material type) {
        item.setType(type);

        return this;
    }

    public String getDisplayName() {
        if (item.getItemMeta() == null) {
            return "";
        }
        return item.getItemMeta().getDisplayName();
    }

    public ItemBuilder setDisplayName(String displayName) {
        if (item.getItemMeta() == null) {
            return this;
        }

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Text.colorize(displayName));
        item.setItemMeta(itemMeta);

        return this;
    }

    public List<String> getLores() {

        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta == null) {
            return new ArrayList<>();
        }

        return itemMeta.getLore();
    }

    public ItemBuilder setLores(List<String> lores) {
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta == null) {
            return this;
        }

        List<String> coloredLores = new ArrayList<>();
        for (String lore : lores) {
            coloredLores.add(Text.colorize(lore));
        }

        itemMeta.setLore(coloredLores);
        item.setItemMeta(itemMeta);

        return this;
    }

    public ItemBuilder addLore(String lore) {
        List<String> lores = this.getLores();

        if (lores == null) {
            lores = new ArrayList<>();
        }

        lores.add(Text.colorize(lore));

        return this.setLores(lores);
    }

    public Integer getAmount() {
        return item.getAmount();
    }

    public ItemBuilder setAmount(Integer amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemStack toBukkit() {
        return item;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public String getCustomDataString(String key) {
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta == null) return null;

        NamespacedKey namespacedKey = new NamespacedKey(Main.instance, key);

        return item.getItemMeta().getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);
    }

    public ItemBuilder setCustomDataString(String key, String value) {
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta == null) return this;

        NamespacedKey namespacedKey = new NamespacedKey(Main.instance, key);
        itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, value);

        item.setItemMeta(itemMeta);

        return this;
    }

    public ItemBuilder setNonUpdateItem() {
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta == null) return this;

        NamespacedKey namespacedKey = new NamespacedKey(Main.instance, "itemtype");
        itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, "NON_UPDATE");

        item.setItemMeta(itemMeta);

        return this;
    }

    public boolean isNonUpdateItem() {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return false;

        NamespacedKey namespacedKey = new NamespacedKey(Main.instance, "itemtype");
        String Type = itemMeta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);
        return "NON_UPDATE".equals(Type);
    }

    public boolean hasCustomDataString(String key, String value) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return false;

        NamespacedKey namespacedKey = new NamespacedKey(Main.instance, key);
        String Type = itemMeta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);
        return value.equals(Type);
    }

    public int getCustomModelData() {
        if (this.item.getItemMeta() == null) {
            return 0;
        }
        return this.item.getItemMeta().getCustomModelData();
    }

    public boolean hasCustomModelData() {
        return this.getCustomModelData() > 0;
    }

    public ItemBuilder setCustomModelData(int customModelData) {
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta == null) {
            return this;
        }

        itemMeta.setCustomModelData(customModelData);
        item.setItemMeta(itemMeta);

        return this;
    }

    public Map<Enchantment, Integer> getEnchantments() {
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta == null) {
            return null;
        }

        return itemMeta.getEnchants();
    }

    public boolean hasEnchantment(Enchantment enchantment) {
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta == null) {
            return false;
        }

        return itemMeta.hasEnchant(enchantment);
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta == null) {
            return this;
        }

        itemMeta.addEnchant(enchantment, level, true);
        item.setItemMeta(itemMeta);

        return this;
    }

    public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments) {
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta == null) {
            return this;
        }

        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            itemMeta.addEnchant(entry.getKey(), entry.getValue(), true);
        }
        item.setItemMeta(itemMeta);

        return this;
    }

    public void addGlowItem() {
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta == null) {
            return;
        }

        ItemBuilder itemBuilder = ItemBuilder.fromItem(item);

        itemBuilder.addEnchantment(Enchantment.DIG_SPEED, 1)
                .setHideFlags(ItemFlag.HIDE_ENCHANTS)
                .setCustomDataString("glow", "true")
                .build();
    }

    public boolean isGlowItem() {
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta == null) {
            return false;
        }

        return ItemBuilder.fromItem(item).hasCustomDataString("glow", "true");
    }

    public ItemBuilder removeEnchantment(Enchantment enchantment) {
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta == null) {
            return this;
        }

        itemMeta.removeEnchant(enchantment);
        item.setItemMeta(itemMeta);

        return this;
    }

    public Set<ItemFlag> getHideFlags() {
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta == null) {
            return Collections.emptySet();
        }

        return itemMeta.getItemFlags();
    }

    public ItemBuilder setHideFlags(ItemFlag... hideFlags) {
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta == null) {
            return this;
        }

        itemMeta.addItemFlags(hideFlags);
        item.setItemMeta(itemMeta);

        return this;
    }

    public ItemBuilder setDamage(int damage) {
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta == null || !(itemMeta instanceof Damageable)) {
            return this;
        }

        ((Damageable) itemMeta).setDamage(damage);
        item.setItemMeta(itemMeta);

        return this;
    }

    public int getDamage() {
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta == null || !(itemMeta instanceof Damageable)) {
            return 0;
        }

        return ((Damageable) itemMeta).getDamage();
    }

}
