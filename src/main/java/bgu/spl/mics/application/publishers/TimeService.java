package bgu.spl.mics.application.publishers;
import java.util.concurrent.TimeUnit;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Publisher;
import bgu.spl.mics.application.messages.TickBroadcast;
/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link Tick Broadcast}.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {
	private boolean terminated = false;
	private int curTime = 0;
	private int maxTime;
	private static class SingletonHolder {
		private static TimeService instance = new TimeService();
	}

	public TimeService() {
		super("myTimer");
	}

	public TimeService(int duration) {
		super("myTimer");
		maxTime = duration;
	}

	@Override
	protected void initialize() {
	}

	@Override
	public void run() {
		initialize();
		while (!terminated) {
			try {
				TimeUnit.MILLISECONDS.sleep(100);
				curTime++;
				MessageBrokerImpl.getInstance().sendBroadcast(new TickBroadcast(curTime, maxTime));
				if (curTime == maxTime) {
					terminated = true;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}


	}

}
