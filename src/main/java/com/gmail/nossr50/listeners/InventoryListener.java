package com.gmail.nossr50.listeners;

import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;

import com.gmail.nossr50.mcMMO;
import com.gmail.nossr50.skills.smelting.SmeltingManager;
import com.gmail.nossr50.util.ItemChecks;

public class InventoryListener implements Listener{
    private final mcMMO plugin;

    public InventoryListener(final mcMMO plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryOpen(InventoryOpenEvent event) {
        InventoryType inventoryType = event.getInventory().getType();

        if (inventoryType == InventoryType.FURNACE) {
            FurnaceInventory inventory = (FurnaceInventory) event.getInventory();
            Furnace furnace = inventory.getHolder();

            if (furnace == null) {
                return;
            }

            Block furnaceBlock = furnace.getBlock();

            if (furnace.getBurnTime() == 0 && !plugin.furnaceIsTracked(furnaceBlock)) {
                plugin.addToOpenFurnaceTracker(furnaceBlock, event.getPlayer().getName());
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClose(InventoryCloseEvent event) {
        InventoryType inventoryType = event.getInventory().getType();

        if (inventoryType == InventoryType.FURNACE) {
            FurnaceInventory inventory = (FurnaceInventory) event.getInventory();
            Furnace furnace = inventory.getHolder();

            if (furnace == null) {
                return;
            }

            Block furnaceBlock = furnace.getBlock();

            if (furnace.getBurnTime() == 0 && plugin.furnaceIsTracked(furnaceBlock)) {
                plugin.removeFromFurnaceTracker(furnaceBlock);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFurnaceBurnEvent(FurnaceBurnEvent event) {
        Block furnaceBlock = event.getBlock();
        FurnaceInventory inventory = ((Furnace)furnaceBlock.getState()).getInventory();
        ItemStack smelting = inventory.getSmelting();

        if (plugin.furnaceIsTracked(furnaceBlock) && smelting != null && ItemChecks.isSmeltable(smelting)) {
            Player player = plugin.getFurnacePlayer(furnaceBlock);

            if (player != null) {
                SmeltingManager smeltingManager = new SmeltingManager(player);
                smeltingManager.fuelEfficiency(event);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFurnaceSmeltEvent(FurnaceSmeltEvent event) {
        Block furnaceBlock = event.getBlock();
        FurnaceInventory inventory = ((Furnace)furnaceBlock.getState()).getInventory();
        ItemStack smelting = inventory.getSmelting();

        if (plugin.furnaceIsTracked(furnaceBlock) && smelting != null && ItemChecks.isSmeltable(smelting)) {
            Player player = plugin.getFurnacePlayer(furnaceBlock);

            if (player != null) {
                SmeltingManager smeltingManager = new SmeltingManager(player);
                smeltingManager.smeltProcessing(event);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFurnaceExtractEvent(FurnaceExtractEvent event) {
        Block furnaceBlock = event.getBlock();
        FurnaceInventory inventory = ((Furnace)furnaceBlock.getState()).getInventory();
        ItemStack result = inventory.getResult();

        if (plugin.furnaceIsTracked(furnaceBlock) && result != null && ItemChecks.isSmelted(result)) {
            SmeltingManager smeltingManager = new SmeltingManager(plugin.getFurnacePlayer(furnaceBlock));
            smeltingManager.vanillaXPBoost(event);
        }
    }
}
