package bgu.spl.mics.application.subscribers;


import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TerminatorEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A Publisher\Subscriber.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {
	private ConcurrentLinkedQueue<MissionInfo> missions;

	public Intelligence(String name) {
		super(name);
		missions = new ConcurrentLinkedQueue<>();
	}

	public void addMission(MissionInfo newmission) {
		missions.add(newmission);
	}

	@Override
	protected void initialize() {
		Thread.currentThread().setName(this.getName());
		subscribeBroadcast(TerminatorEvent.class, message -> {
			terminate();
		});
		subscribeBroadcast(TickBroadcast.class, message -> {
			for (MissionInfo A : missions) {
				if ((A.getTimeIssued() <= message.getTime()) & (A.getTimeExpired() >= message.getTime())) {
					missions.remove(A);
					MessageBrokerImpl.getInstance().sendEvent(new MissionReceivedEvent(A));
				}
			}
		});
	}
}


