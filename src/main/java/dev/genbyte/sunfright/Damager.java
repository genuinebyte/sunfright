package dev.genbyte.sunfright;

import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.World;
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
		World sunnedWorld = sf.sunnedWorld;
		Collection<? extends Player> players = sunnedWorld.getPlayers();

		players.forEach((player) -> {
			byte skylight = player.getLocation().getBlock().getLightFromSky();

			if (skylight > 3 && timeShouldDamage()) {
				new DoDamage(player, sf.damagaPerSecond).runTask(sf);
			} else if (player.getInventory().getHelmet() != null
					&& player.getInventory().getHelmet().getEnchantmentLevel(Enchantment.VANISHING_CURSE) == 2) {
				player.getInventory().setHelmet(new ItemStack(Material.AIR));
			}
		});
	}

	private boolean timeShouldDamage() {
		World sunnedWorld = sf.sunnedWorld;

		long time = sunnedWorld.getTime();
		boolean storming = sunnedWorld.hasStorm();
		boolean thundering = sunnedWorld.isThundering();

		// Times are pulled from Minecraft Gamepedia page on Light, specifically the internal light
		// level section. https://minecraft.gamepedia.com/Light
		// Times correspond to when the light level is over 8.
		if (!storming && !thundering) {
			if (time >= 13027 && time <= 22973) {
				return false;
			}
		} else if (storming && !thundering) {
			if (time >= 12734 && time <= 23266) {
				return false;
			}
		} else if (storming && thundering) {
			if (time >= 12300 && time <= 23700) {
				return false;
			}
		}
		return true;
	}

	private class DoDamage extends BukkitRunnable {
		private final Player player;
		private final int damage;

		public DoDamage(Player player, int damage) {
			this.player = player;
			this.damage = damage;
		}

		public void run() {
			ItemStack helmet = player.getInventory().getHelmet();

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
						helmetDamageable.setDamage(helmetDamage + (damage/2));
						helmet.setItemMeta((ItemMeta) helmetDamageable);
					}
				}
			} else {
				applyDamage();
			}
		}

		private void applyDamage() {
			player.damage(damage);
		}
	}
}
