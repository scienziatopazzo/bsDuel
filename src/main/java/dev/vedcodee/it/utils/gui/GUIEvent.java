package dev.vedcodee.it.utils.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.stream.Collectors;

public class GUIEvent implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        try {
            AbstractGUI.setGuiRegistered(new ArrayList<>(AbstractGUI.getGuiRegistered().stream()
                    .collect(Collectors.toMap(AbstractGUI::getTitle, gui -> gui, (existing, replacement) -> existing))
                    .values()));
            AbstractGUI.getGuiRegistered().forEach(gui -> {
                if(gui.isInventory(event)) {
                    if(gui.onClick(event.getSlot(), event.getCurrentItem(), (Player) event.getView().getPlayer())) {
                        event.setCancelled(true);
                    }
                }
            });
        }catch (ConcurrentModificationException e) {
            // I know but it works
        }

    }




}
