package dev.genbyte.sunfright.events;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class HelmetHandler implements Listener {
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		PlayerInventory inv = event.getPlayer().getInventory();

		ItemStack stack = new ItemStack(Material.LEATHER_HELMET);
		stack.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
		stack.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 2);

		inv.setHelmet(stack);
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getClickedInventory() instanceof PlayerInventory
				&& event.getCurrentItem().getEnchantmentLevel(Enchantment.VANISHING_CURSE) == 2) {

			event.setCancelled(true);
			event.getView().setItem(event.getRawSlot(), new ItemStack(Material.AIR));
		}
	}
}