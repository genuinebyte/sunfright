package dev.genbyte.sunfright;

import java.util.Collection;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
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
			if (skylight > 3 && skylight < 13) {
				new DoDamage(player, (byte) 1).runTask(sf);
			} else if (skylight >= 13) {
				new DoDamage(player, (byte) 2).runTask(sf);
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
			if (helmet != null && helmet instanceof Damageable) {
				Damageable damageable = (Damageable) helmet;
				damageable.setDamage(damageable.getDamage() - damage);
			} else {
				player.damage(damage);
			}
		}
	}
}