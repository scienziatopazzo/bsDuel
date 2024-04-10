package dev.vedcodee.it.utils.gui;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class AbstractGUI implements Listener{

    @Getter
    @Setter
    private static List<AbstractGUI> guiRegistered = new ArrayList<>();

    private final Inventory inventory;
    private final String title;
    private final int size;

    public AbstractGUI(String title, int size) {
        this.title = title;
        this.size = size;
        this.inventory = Bukkit.createInventory(null, size, title);
        guiRegistered.add(this);
    }

    public void setItem(ItemStack itemStack, int slot) {
        inventory.setItem(slot, itemStack);
    }

    public void addItem(ItemStack itemStack) {
        inventory.addItem(itemStack);
    }

    public void open(Player player) {
        player.closeInventory();
        player.openInventory(inventory);
    }

    public abstract boolean onClick(int slot, ItemStack itemStack, Player player);

    public boolean isInventory(InventoryClickEvent event) {
        if (event.getClickedInventory() == null
                || event.getCurrentItem() == null
                || event.getCurrentItem().getItemMeta() == null
        ) return false;


        if(getInventory().getSize() == event.getClickedInventory().getSize() &&  getTitle().equalsIgnoreCase(event.getView().getTitle()))
            return true;
        return false;
    }

    public boolean isInventory(InventoryCloseEvent event) {
        if(getInventory().getSize() == event.getInventory().getSize() &&  getTitle().equalsIgnoreCase(event.getView().getTitle()))
            return true;
        return false;
    }

    public void sound(Player player, Sound sound) {
        player.playSound(player.getLocation(), sound, 1f, 1f);
    }

    public void delete() {
        guiRegistered.remove(this);
    }




}
