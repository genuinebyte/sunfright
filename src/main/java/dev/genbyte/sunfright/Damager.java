package dev.genbyte.sunfright;

import java.util.Collection;
import java.util.Random;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class Damager extends BukkitRunnable {
	private final Sunfright sf;
	private final Random rand;

	public Damager(Sunfright sf) {
		this.sf = sf;
		this.rand = new Random();
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
		 if (storming && !thundering) {
			if (time >= 12734 && time <= 23266) {
				return false;
			}
		} else if (storming && thundering) {
			if (time >= 12300 && time <= 23700) {
				return false;
			}
		} else if (time >= 13027 && time <= 22973) {
			return false;
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
			Location loc = player.getLocation();
			World world = loc.getWorld();
			RayTraceResult rtr = player.getWorld().rayTraceBlocks(
				loc,
				new Vector(0, 1, 0),
				world.getMaxHeight()-loc.getY()
			);
	
			if (rtr != null) {
				Block topBlock = rtr.getHitBlock();
				if (topBlock != null && topBlock.getLocation().getY() > player.getLocation().getY()
					&& topBlock.getType().equals(Material.BLACK_STAINED_GLASS)
				) {
					return;
				}
			}

			ItemStack helmet = player.getInventory().getHelmet();

			if (helmet != null) {
				ItemMeta helmetMeta = helmet.getItemMeta();

				if (helmetMeta instanceof Damageable) {
					Damageable helmetDamageable = (Damageable) helmetMeta;
					int helmetDamage = helmetDamageable.getDamage();
					int fireProtLevel = helmet.getEnchantmentLevel(Enchantment.PROTECTION_FIRE);
					int unbrLevel = helmet.getEnchantmentLevel(Enchantment.DURABILITY);

					if (fireProtLevel < 1) {
						damagePlayer();
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
						// Formula from https://minecraft.gamepedia.com/Unbreaking
						// Origintal is 60 + (40 / (level+1)) but we subtract one from fireProtLevel
						// so the +1 cancels
						int chanceToDamage = 60 + (40 / (fireProtLevel+unbrLevel));
						
						if (rand.nextInt(99)+1 <= chanceToDamage) {
							helmetDamageable.setDamage(helmetDamage + (damage/2));
							helmet.setItemMeta((ItemMeta) helmetDamageable);
						}
					}
				}
			} else {
				damagePlayer();
			}
		}

		private void damagePlayer() {
			player.damage(damage);
		}
	}
}
