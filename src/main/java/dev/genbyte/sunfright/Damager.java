package dev.genbyte.sunfright;

import java.util.Collection;
import java.util.Random;

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

public class Damager extends BukkitRunnable {
	private final Sunfright sf;
	private final Random rand;

	public Damager(Sunfright sf) {
		this.sf = sf;
		this.rand = new Random();
	}

	public void run() {
		if (!timeShouldDamage()) {
			return;
		}

		World sunnedWorld = sf.sunnedWorld;
		Collection<? extends Player> players = sunnedWorld.getPlayers();

		players.forEach((player) -> {
			Location loc = player.getLocation();
			int x = loc.getBlockX();
			int y = loc.getBlockY();
			int z = loc.getBlockZ();
			// Returns one lower than it should?
			int highestY = sunnedWorld.getHighestBlockAt(loc).getLocation().getBlockY()+1;

			for (int i = y; i < highestY; ++i) {
				Block current = sunnedWorld.getBlockAt(x, i, z);

				if (!blockShouldDamage(current.getType())) {
					/* player rulled to be safe. Remove their helmet if it's the one we gave, but
					   only do so if the skylight is less than three. This will keep us from
					   removing the starter helmet if they're just chopping down a tree */
					if (player.getInventory().getHelmet() != null &&
					    player.getLocation().getBlock().getLightFromSky() > 3 &&
					    player.getInventory().getHelmet().getEnchantmentLevel(Enchantment.VANISHING_CURSE) == 2)
				   	{
					   player.getInventory().setHelmet(new ItemStack(Material.AIR));
				   	}

					return;
				}
			}

			new DoDamage(player, sf.damagaPerSecond).runTask(sf);
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

	/*
	Material.isTransparent() is buggy and awful and only gives true for some things. This function
	checks if a material lets light pass and should damage the player.
	I've never seen it give a false positive, only a false negative, so it is one of the first
	things we check.
	*/
	@SuppressWarnings("deprecation")
	private boolean blockShouldDamage(Material mat) {
		String key = mat.getKey().getKey().toLowerCase();
		
		if (mat == Material.BLACK_STAINED_GLASS) {
			return false;
		}

		return mat.isTransparent() || key.indexOf("glass") != -1 || key.indexOf("leaves") != -1 || key.indexOf("sign") != -1 || key.indexOf("trapdoor") != -1 || key.indexOf("fence") != -1 || key.indexOf("bed") != -1 || mat == Material.ICE || mat == Material.HOPPER || mat == Material.COBWEB;
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
					int fireProtLevel = helmet.getEnchantmentLevel(Enchantment.PROTECTION_FIRE);

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
						int chanceToDamage = 60 + (40 / (fireProtLevel));
						
						if (rand.nextInt(99)+1 <= chanceToDamage) {
							int calculatedDamage = (int) Math.ceil(damage / 2);
							helmetDamageable.setDamage(helmetDamage + calculatedDamage);
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
