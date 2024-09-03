package eu.camonetwork.dailyreward.infrastructure;

import eu.camonetwork.dailyreward.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.function.BiConsumer;

public abstract class InventoryMenu implements Listener {
    private String name = "default";
    public boolean readOnly = true;
    public int lines = 3;
    public Inventory inventory;
    public Player player;
    public HashMap<Integer, Runnable> clickable = new HashMap<>();
    public HashMap<Integer, ItemStack> swappable = new HashMap<>();
    public HashMap<Integer, BiConsumer<ClickType, Player>> clickableWithType = new HashMap<>();

    public void open(Player player) {
        this.setName(name);

        Inventory inventory = Bukkit.getServer().createInventory(player, (9 * lines), Text.colorize(name));

        player.openInventory(inventory);

        this.player = player;
        this.inventory = inventory;

        this.onOpen();

        Bukkit.getServer().getPluginManager().registerEvents(this, Main.instance);

        this.update();
    }

    public void onOpen() {

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInteract(PlayerInteractEvent event) {
        InventoryHolder holder = event.getPlayer().getOpenInventory().getTopInventory().getHolder();
        if (holder != null && holder.equals(this.inventory.getHolder())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void clickEvent(InventoryClickEvent event) {
        if (event.getInventory().getHolder() != inventory.getHolder()) {
            return;
        }

        int slot = event.getRawSlot();
        ItemStack cursorItem = event.getCursor();

        if (swappable.containsKey(slot)) {
            ItemStack inventoryItem = inventory.getItem(slot);
            inventory.setItem(slot, cursorItem);
            swappable.put(slot, cursorItem);
            event.getWhoClicked().setItemOnCursor(inventoryItem);
            event.setCancelled(true);
            return;
        }

        if (readOnly && event.getClickedInventory() != null && event.getClickedInventory().equals(this.inventory)) {
            if (event.getRawSlot() >= event.getView().getTopInventory().getSize()) {
                return;
            }
            event.setCancelled(true);
        }

        if (event.getCurrentItem() == null) return;

        ClickType clickType = ClickType.UNKNOWN;
        switch (event.getClick()) {
            case LEFT: case SHIFT_LEFT:
                clickType = ClickType.LEFT_CLICK;
                break;
            case RIGHT: case SHIFT_RIGHT:
                clickType = ClickType.RIGHT_CLICK;
                break;
            default: break;
        }

        if (clickable.containsKey(event.getRawSlot())) {
            clickable.get(event.getRawSlot()).run();
            this.update();
        }

        if (clickableWithType.containsKey(event.getRawSlot())) {
            clickableWithType.get(event.getRawSlot()).accept(clickType, (Player) event.getWhoClicked());
        }

        this.onClick(event);
    }

    public void sendMessage(String string) {
        player.sendMessage(Text.colorize(string));
    }

    public void onClick(InventoryClickEvent event) {

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void closeEvent(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() != inventory.getHolder()) {
            return;
        }
        HandlerList.unregisterAll(this);

        this.onClose(event);
    }

    public void onClose(InventoryCloseEvent event) {

    }

    protected abstract void update();

    public void drawLine(int line, Material item) {
        int start = (9 * line) - 9;
        int end = start + 9;
        for (int i = start; i < end; i++) {
            this.inventory.setItem(i, new ItemStack(item));
        }
    }

    public void drawLine(int line, ItemStack item) {
        int start = (9 * line) - 9;
        int end = start + 9;
        for (int i = start; i < end; i++) {
            this.inventory.setItem(i, item);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void rename(String newName) {
        this.name = newName;
        if (this.inventory != null) {
            this.inventory = Bukkit.getServer().createInventory(this.inventory.getHolder(), this.inventory.getSize(), Text.colorize(newName));
            this.open(player);
        }
    }

    public ItemStack fillerItem() {
        return ItemBuilder.create()
                .setType(Material.GRAY_STAINED_GLASS_PANE)
                .setDisplayName(" ")
                .setNonUpdateItem()
                .build();
    }

    public void fillBackground() {
        for (int i = 1; i <= lines; i++) {
            this.drawLine(i, fillerItem());
        }
    }

    public void SwappableItem(Integer index, ItemStack item) {
        swappable.put(index, item);
        inventory.setItem(index, item);
    }

    public void clickableItem(Integer index, ItemStack item, Player player, Runnable task) {
        inventory.setItem(index, item);
        clickable.put(index, () -> {
            task.run();
            player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 1.0f, 1.0f);
        });
    }

    public void clickableItem(ItemStack item, Player player, Runnable task) {
        inventory.addItem(item);
        int index = inventory.firstEmpty();
        if (index == -1) return;

        clickable.put(index, () -> {
            task.run();
            player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 1.0f, 1.0f);
        });
    }

    public void clickableItem(Integer index, ItemStack item, Runnable leftClickTask, Runnable rightClickTask) {
        inventory.setItem(index, item);
        clickableWithType.put(index, (clickType, player) -> {
            if (clickType == ClickType.LEFT_CLICK) {
                leftClickTask.run();
                player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 1.0f, 1.0f);
            } else if (clickType == ClickType.RIGHT_CLICK) {
                rightClickTask.run();
                player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 1.0f, 1.0f);
            }
        });
    }

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
            if (d <= 0) return false;
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public enum ClickType {
        LEFT_CLICK,
        RIGHT_CLICK,
        UNKNOWN
    }
}
