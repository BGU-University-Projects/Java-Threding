package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.TerminatorEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;

import java.util.List;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {
	private  int time=0;
	private Inventory inventory;
	public Q(String name, Inventory inve) {
		super(name);
		inventory=inve;
	}
	@Override
	protected void initialize() {
		Thread.currentThread().setName(this.getName());
		subscribeBroadcast(TerminatorEvent.class, message -> {
			terminate();
			inventory.printToFile("inventoryOutputFile.json");
		});
		subscribeBroadcast(TickBroadcast.class, message -> {
			time =message.getTime();
		});
		subscribeEvent(GadgetAvailableEvent.class, message -> {
				if (inventory.getItem(message.getGadget()))
					complete(message, Integer.toString(time));
				else
					complete(message, "false");
		});
	}
}
