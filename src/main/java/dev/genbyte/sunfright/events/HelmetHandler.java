package dev.genbyte.sunfright.events;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import dev.genbyte.sunfright.Sunfright;

public class HelmetHandler implements Listener {
	private final Sunfright sf;

	public HelmetHandler(Sunfright sf) {
		this.sf = sf;
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		setRespawnHelmet(event.getPlayer());
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		if (!player.hasPlayedBefore()) {
			setRespawnHelmet(player);
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getClickedInventory() instanceof PlayerInventory
				&& event.getCurrentItem().getEnchantmentLevel(Enchantment.VANISHING_CURSE) == 2) {

			event.setCancelled(true);
			event.getView().setItem(event.getRawSlot(), new ItemStack(Material.AIR));
		}
	}

	private void setRespawnHelmet(Player player) {
		if (!player.getWorld().equals(sf.sunnedWorld)) {
			return;
		}

		PlayerInventory inv = player.getInventory();

		ItemStack stack = new ItemStack(Material.LEATHER_HELMET);
		stack.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
		stack.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 2);
		stack.addEnchantment(Enchantment.PROTECTION_FIRE, 1);

		inv.setHelmet(stack);
	}
}
