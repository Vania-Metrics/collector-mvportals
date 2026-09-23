package fr.samflix.vaniametrics.module.mvportals;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import fr.samflix.vaniametrics.api.VaniaMetrics;
import fr.samflix.vaniametrics.api.VaniaMetricsProvider;

/**
 * Portal traversal metrics.
 *
 * <p>A traffic counter: it tells which portals are used and which are dead.
 */
public final class PortalsPaper extends JavaPlugin {

	private PortalsCollector collector;

	@Override
	public void onEnable() {
		VaniaMetrics metrics = VaniaMetricsProvider.get();
		collector = new PortalsCollector();
		metrics.register(collector);
		Bukkit.getPluginManager().registerEvents(collector, this);
	}

	@Override
	public void onDisable() {
		if (collector != null) {
			VaniaMetricsProvider.find().ifPresent(m -> m.unregister(collector));
		}
	}
}
