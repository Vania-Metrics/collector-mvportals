package fr.samflix.vaniametrics.module.mvportals;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import org.mvplugins.multiverse.portals.event.MVPortalEvent;

import fr.samflix.vaniametrics.api.Collector;
import fr.samflix.vaniametrics.api.Counter;
import fr.samflix.vaniametrics.api.MetricRegistry;

/**
 * Multiverse-Portals — qui passe où.
 *
 * <p>Un compteur de CIRCULATION : il dit quels portails servent et lesquels sont morts. Sur un
 * réseau à plusieurs mondes, c'est la seule mesure qui raconte comment les joueurs se déplacent.
 *
 * <p>UNE SEULE ÉTIQUETTE, LE MONDE DE DÉPART, et deux raisons distinctes à cela.
 *
 * <p>La première tient à la cardinalité : le NOM d'un portail est libre et se multiplie sans
 * borne — une série temporelle par portail construit, pour toujours. Le monde, lui, est un
 * ensemble fermé.
 *
 * <p>La seconde tient à l'API. {@code getPortalType()} et {@code getDestination()} sont tous deux
 * dépréciés dans Multiverse-Portals 5.3, le second marqué pour retrait — vérifié un par un. Les
 * seuls accesseurs encore sains sont {@code getTeleportee()}, {@code getFrom()} et
 * {@code getSendingPortal()}. Plutôt que de bâtir une étiquette sur ce qui va disparaître, on
 * compte ce qu'on sait compter durablement.
 */
public final class PortalsCollector implements Collector, Listener {

	private Counter passages;

	@Override
	public String nom() {
		return "portal";
	}

	@Override
	public String origine() {
		return "Multiverse-Portals";
	}

	@Override
	public void declarer(MetricRegistry r) {
		passages = r.counter("portal_uses_total",
				"Passages de portail, par monde de départ.", "from_world");
	}

	@Override
	public void relever(MetricRegistry r) {
		// Tout est compté dans l'écouteur.
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPortal(MVPortalEvent e) {
		String monde = e.getFrom() == null || e.getFrom().getWorld() == null
				? "unknown"
				: e.getFrom().getWorld().getName();
		passages.inc(monde);
	}
}
