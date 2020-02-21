package dev.genbyte.sunfright;

import java.util.Collection;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class Damager extends BukkitRunnable {
	private final Sunfright sf;

	public Damager(Sunfright sf) {
		this.sf = sf;
	}

	public void run() {
		Collection<? extends Player> players = sf.getServer().getOnlinePlayers();

		players.forEach((player) -> {
			byte skylight = player.getLocation().getBlock().getLightFromSky();

			if (skylight > 3) {
				new DoDamage(player, (byte) 1).runTask(sf);
			} else if (player.getInventory().getHelmet().getEnchantmentLevel(Enchantment.VANISHING_CURSE) == 2) {
				player.getInventory().setHelmet(new ItemStack(Material.AIR));
			}
		});
	}

	private class DoDamage extends BukkitRunnable {
		private final Player player;
		private final byte damage;

		public DoDamage(Player player, byte damage) {
			this.player = player;
			this.damage = damage;
		}

		public void run() {
			ItemStack helmet = player.getInventory().getHelmet();
			Block topBlock = player.getWorld().getBlockAt(
					player.getWorld().getHighestBlockAt(player.getLocation()).getLocation().subtract(0, 1, 0));

			if (topBlock.getLocation().getY() > player.getLocation().getY()
					&& topBlock.getType().equals(Material.BLACK_STAINED_GLASS)) {
				return;
			}

			if (helmet != null) {
				ItemMeta helmetMeta = helmet.getItemMeta();

				if (helmetMeta instanceof Damageable) {
					Damageable helmetDamageable = (Damageable) helmetMeta;
					int helmetDamage = helmetDamageable.getDamage();

					if (helmet.getEnchantmentLevel(Enchantment.PROTECTION_FIRE) < 1) {
						applyDamage();
						return;
					}

					if (helmetDamage + damage >= helmet.getType().getMaxDurability()) {
						if (helmet.getEnchantmentLevel(Enchantment.VANISHING_CURSE) == 2) {
							int bindLevel = helmet.getEnchantmentLevel(Enchantment.BINDING_CURSE);

							if (bindLevel < 5) {
								helmetDamageable.setDamage(0);
								helmet.setItemMeta((ItemMeta) helmetDamageable);
								helmet.addUnsafeEnchantment(Enchantment.BINDING_CURSE, bindLevel + 1);

								return;
							}
						}

						player.getInventory().setHelmet(new ItemStack(Material.AIR));
					} else {
						helmetDamageable.setDamage(helmetDamage + damage);
						helmet.setItemMeta((ItemMeta) helmetDamageable);
					}
				}
			} else {
				applyDamage();
			}
		}

		private void applyDamage() {
			player.damage(damage * 2);
		}
	}
}
