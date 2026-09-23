package fr.samflix.vaniametrics.module.mvportals;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import fr.samflix.vaniametrics.api.VaniaMetrics;
import fr.samflix.vaniametrics.api.VaniaMetricsProvider;

/**
 * Métriques de passage de portail.
 *
 * <p>Un compteur de circulation : il dit quels portails servent et lesquels sont morts.
 */
public final class PortalsPaper extends JavaPlugin {

	private PortalsCollector collecteur;

	@Override
	public void onEnable() {
		VaniaMetrics metriques = VaniaMetricsProvider.get();
		collecteur = new PortalsCollector();
		metriques.enregistrer(collecteur);
		Bukkit.getPluginManager().registerEvents(collecteur, this);
	}

	@Override
	public void onDisable() {
		if (collecteur != null) {
			VaniaMetricsProvider.chercher().ifPresent(m -> m.retirer(collecteur));
		}
	}
}
