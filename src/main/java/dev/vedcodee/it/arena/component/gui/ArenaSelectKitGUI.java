package dev.vedcodee.it.arena.component.gui;

import dev.vedcodee.it.Main;
import dev.vedcodee.it.kit.DuelKit;
import dev.vedcodee.it.utils.ItemFactory;
import dev.vedcodee.it.utils.gui.AbstractGUI;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
public class ArenaSelectKitGUI extends AbstractGUI {

    private DuelKit kitSelected;

    public ArenaSelectKitGUI(Player player) {
        super("GUI Select Kit " + player.getName(), Main.getInstance().getConfiguration().getInt("kit.inventory_slot"));
        reloadGUI();
    }

    @Override
    public boolean onClick(int slot, ItemStack itemStack, Player player) {
        ConfigurationSection selection = Main.getInstance().getConfiguration().getConfigurationSection("kit.inventory");
        for (String key : selection.getKeys(false)) {
            ConfigurationSection itemSelection = selection.getConfigurationSection(key);
            if(itemSelection.contains("select")) {
                if(Integer.parseInt(key) == slot) {
                    String kitName = itemSelection.getString("select");
                    DuelKit kit = DuelKit.getKitByName(kitName);
                    if(!player.hasPermission(kit.getPermission())) return true;
                    this.kitSelected = kit;
                    reloadGUI();
                    return true;
                }
            }
        }
        return true;
    }


    private void reloadGUI() {
        ConfigurationSection selection = Main.getInstance().getConfiguration().getConfigurationSection("kit.inventory");
        for (String key : selection.getKeys(false)) {
            ConfigurationSection itemSelection = selection.getConfigurationSection(key);
            if(itemSelection.contains("select")) {
                String kit = itemSelection.getString("select");
                if(kitSelected == null || !kitSelected.getName().equalsIgnoreCase(kit)) {
                    setItem(
                            Main.getInstance().getConfiguration().getConfigurationSection("kit.kits." + kit) == null ?
                                    new ItemFactory(Material.DIAMOND_ORE).name(kit).build() :
                                    ItemFactory.load(Main.getInstance().getConfiguration().getConfigurationSection("kit.kits." + kit)).build(),
                            Integer.parseInt(key)
                    );
                } else {
                    setItem(
                            Main.getInstance().getConfiguration().getConfigurationSection("kit.kits." + kit) == null ?
                                    new ItemFactory(Material.DIAMOND_ORE).name(kit).setGlowing(true).build() :
                                    ItemFactory.load(Main.getInstance().getConfiguration().getConfigurationSection("kit.kits." + kit)).setGlowing(true).build(),
                            Integer.parseInt(key)
                    );
                }
            } else if (itemSelection.contains("preview")) {
                if(kitSelected == null) {
                    setItem(
                            ItemFactory.load(Main.getInstance().getConfiguration().getConfigurationSection("kit.preview")).build(),
                            Integer.parseInt(key)
                    );
                } else {
                    setItem(
                            Main.getInstance().getConfiguration().getConfigurationSection("kit.kits." + kitSelected.getName()) == null ?
                                    new ItemFactory(Material.DIAMOND_ORE).name(kitSelected.getName()).build() :
                                    ItemFactory.load(Main.getInstance().getConfiguration().getConfigurationSection("kit.kits." + kitSelected.getName())).build(),
                            Integer.parseInt(key)
                    );
                }
            } else {
                setItem(
                        ItemFactory.load(itemSelection).build(),
                        Integer.parseInt(key)
                );
            }
        }
    }
}
