package dev.vedcodee.it.kit;

import dev.vedcodee.it.Main;
import dev.vedcodee.it.utils.GameFile;
import dev.vedcodee.it.utils.ItemFactory;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Getter
public class DuelKit {

    @Getter
    private static final List<DuelKit> kits = new ArrayList<>();

    private final GameFile kitFile;
    private final FileConfiguration config;
    private final String name;
    private String permission;
    private boolean enable;
    private ItemStack[] content;

    public DuelKit(String name) {
        this.kitFile = new GameFile("kits/" + name + ".yml");
        this.config = kitFile.getFileConfiguration();
        this.name = name;
        // {kit_name} -> name
        this.permission = Main.getInstance().getConfiguration().getString("kit.permission").replace("{kit_name}", name);
        this.enable = true;
        config.set("enable", true);
        loadContent();
        kitFile.save();
        kits.add(this);
    }

    public void loadContent() {
        if (!enable) return;
        List<ItemStack> inventoryContent = new ArrayList<>();
        if (config.isSet("inventory")) {
            ConfigurationSection section = config.getConfigurationSection("inventory");
            for (String key : section.getKeys(false)) {
                ConfigurationSection kitSel = section.getConfigurationSection(key);
                ItemFactory item = ItemFactory.load(kitSel);
                inventoryContent.add(item.build());
                System.out.printf(key);
            }
        }
        content = inventoryContent.toArray(new ItemStack[0]);
    }


    public void loadContent(Player player) {
        if (!enable) return;
        content = player.getInventory().getContents();
        save();
    }

    public void delete() {
        kitFile.getFile().delete();
        kits.remove(this);
    }


    private void save() {
        int index = 0;
        for (ItemStack itemStack : content) {
            ConfigurationSection itemSel = config.createSection("inventory." + index);
            ItemFactory.set(itemStack, itemSel);
            index++;
        }
        kitFile.save();
    }



    public static DuelKit getKitByName(String name) {
        return kits.stream().filter(kit -> kit.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static void loadKits() {
        File kits = new File(Main.getInstance().getDataFolder(), "kits");
        kits.getParentFile().mkdirs();
        kits.mkdir();
        for (File file : kits.listFiles())
            new DuelKit(file.getName().replace(".yml", ""));
    }
}
