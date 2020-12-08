package dev.genbyte.sunfright;

import java.util.Collection;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;
import org.bukkit.material.MaterialData;
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
			Optional<Block> highestBlock = actualHighestBlock(sunnedWorld, loc);

			if (highestBlock.isPresent()) {
				int highestY = highestBlock.get().getY();

				for (int i = y; i <= highestY; ++i) {
					Block current = sunnedWorld.getBlockAt(x, i, z);

					if (!blockShouldDamage(current)) {
						/* player rulled to be safe. Remove their helmet if it's the one we gave, but
						only do so if the skylight is less than three. This will keep us from
						removing the starter helmet if they're just chopping down a tree */
						if (player.getInventory().getHelmet() != null &&
							player.getLocation().getBlock().getLightFromSky() < 3 &&
							player.getInventory().getHelmet().getEnchantmentLevel(Enchantment.VANISHING_CURSE) == 2)
						{
							player.getInventory().setHelmet(new ItemStack(Material.AIR));
						}

						return;
					}
				}
			}

			new DoDamage(player, sf.damagaPerSecond).runTask(sf);
		});
	}

	// 1.12.2 getHighetBlockAt method only returns the highest non-transparent.
	// Glass is transparent so I guess we have to do this ourselves
	private Optional<Block> actualHighestBlock(World world, Location location) {
		int lowerBound = world.getHighestBlockYAt(location);
		int upperBound = world.getMaxHeight();

		for (int i = upperBound; i > lowerBound; --i) {
			Block block = world.getBlockAt(location.getBlockX(), i, location.getBlockZ());
			if (!block.isEmpty()) {
				return Optional.of(block);
			}
		}

		return Optional.empty();
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
	private boolean blockShouldDamage(Block block) {
		Material mat = block.getType();
		MaterialData matData = block.getState().getData();
		String key = mat.toString().toLowerCase();

		// 15 is the magic value for black
		if (mat == Material.STAINED_GLASS && matData.getData() == 15) {
			return false;
		}

		return mat.isTransparent() || key.indexOf("glass") != -1 || key.indexOf("leaves") != -1 || key.indexOf("sign") != -1 || key.indexOf("trapdoor") != -1 || key.indexOf("fence") != -1 || key.indexOf("bed") != -1 || mat == Material.ICE || mat == Material.HOPPER || mat == Material.WEB;
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
				short helmetDamage = helmet.getDurability();
				int fireProtLevel = helmet.getEnchantmentLevel(Enchantment.PROTECTION_FIRE);

				if (fireProtLevel < 1) {
					damagePlayer();
					return;
				}

				if (helmetDamage + damage >= helmet.getType().getMaxDurability()) {
					if (helmet.getEnchantmentLevel(Enchantment.VANISHING_CURSE) == 2) {
						int bindLevel = helmet.getEnchantmentLevel(Enchantment.BINDING_CURSE);

						if (bindLevel < 5) {
							helmet.setDurability((short) 0);
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
						helmet.setDurability((short) (helmetDamage + calculatedDamage));
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
