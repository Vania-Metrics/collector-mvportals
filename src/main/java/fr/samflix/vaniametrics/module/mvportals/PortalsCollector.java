package fr.samflix.vaniametrics.module.mvportals;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import org.mvplugins.multiverse.portals.event.MVPortalEvent;

import fr.samflix.vaniametrics.api.Collector;
import fr.samflix.vaniametrics.api.Counter;
import fr.samflix.vaniametrics.api.MetricRegistry;

/**
 * Multiverse-Portals — who goes where.
 *
 * <p>A traffic counter: it tells which portals are used and which are dead. On a
 * multi-world network, it's the only metric that tells the story of how players move around.
 *
 * <p>A single label, the departure world, for two distinct reasons.
 *
 * <p>The first is cardinality: a portal's NAME is free-form and multiplies without bound
 * — one time series per built portal, forever. The world, on the other hand, is a
 * closed set.
 *
 * <p>The second is the API. {@code getPortalType()} and {@code getDestination()} are both
 * deprecated in Multiverse-Portals 5.3, the latter marked for removal — checked one by one. The
 * only accessors still sound are {@code getTeleportee()}, {@code getFrom()} and
 * {@code getSendingPortal()}. Rather than build a label on something about to disappear, this
 * counts what can be counted durably.
 */
public final class PortalsCollector implements Collector, Listener {

	private Counter uses;

	@Override
	public String name() {
		return "portal";
	}

	@Override
	public String source() {
		return "Multiverse-Portals";
	}

	@Override
	public void declare(MetricRegistry r) {
		uses = r.counter("portal_uses_total",
				"Portal traversals, by departure world.", "from_world");
	}

	@Override
	public void collect(MetricRegistry r) {
		// Everything is counted in the listener.
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPortal(MVPortalEvent e) {
		String world = e.getFrom() == null || e.getFrom().getWorld() == null
				? "unknown"
				: e.getFrom().getWorld().getName();
		uses.inc(world);
	}
}
