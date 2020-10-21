package bgu.spl.mics.application.subscribers;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Squad;
import java.util.List;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {
	private Squad squad;
	private int time=0;
	public Moneypenny(String name,Squad squad) {
		super(name);
		this.squad=squad;
	}
	@Override
	protected void initialize() {
		Thread.currentThread().setName(this.getName());
		subscribeBroadcast(TerminatorEvent.class, message -> {
			terminate();
		});
		subscribeBroadcast(TickBroadcast.class, message -> {
			time = message.getTime();
		});
		if (this.getName() != "JOKER") {
			subscribeEvent(AgentsAvailableEvent.class, message -> {
				List<String> agents = message.getAgentsneeded();
				if (squad.getAgents(agents)) {
					complete(message, this.getName());
				} else
					complete(message, "false");
			});
		} else {
			subscribeEvent(SendAgentsEvent.class, message -> {
				synchronized (squad) {
					List<String> agents = message.getAgentstosend();
					squad.sendAgents(agents, message.getTime());
					complete(message, squad.getAgentsNames(agents));
				}
			});
			subscribeEvent(AgentsRealseEvent.class, message -> {
				synchronized (squad) {
					List<String> agents = message.getAgentsneeded();
					squad.releaseAgents(agents);
					squad.notifyAll();
				}
			});

		}
	}

}
