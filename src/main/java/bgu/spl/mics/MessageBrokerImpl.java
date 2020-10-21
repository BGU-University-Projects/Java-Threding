package bgu.spl.mics;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.SendAgentsEvent;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {
	private static MessageBrokerImpl messageBrokerImpl = new MessageBrokerImpl();
	private ConcurrentHashMap<Subscriber, ConcurrentLinkedQueue<Message>> subscribers;
	private ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<Subscriber>> messages;
	private ConcurrentHashMap<Event, Future> futureToEvent;


	private static class SingletonHolder {
		private static MessageBrokerImpl instance = new MessageBrokerImpl();
	}

	private MessageBrokerImpl() {
		this.subscribers = new ConcurrentHashMap<>();
		this.messages = new ConcurrentHashMap<>();
		this.futureToEvent = new ConcurrentHashMap<>();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static MessageBrokerImpl getInstance() {
		return messageBrokerImpl;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		synchronized (messages) {
			if (!messages.containsKey(type))
				messages.put(type, new ConcurrentLinkedQueue<Subscriber>());
		}
		messages.get(type).add(m);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		synchronized (messages) {
			if (!messages.containsKey(type))
				messages.put(type, new ConcurrentLinkedQueue<Subscriber>());
		}
		messages.get(type).add(m);
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		Future<T> newFuture = (Future<T>) futureToEvent.get(e);
		synchronized (newFuture) {
			newFuture.resolve(result);
			futureToEvent.remove(e);
			futureToEvent.put(e, newFuture);
		}

	}

	@Override
	public void sendBroadcast(Broadcast b) {
		if (messages.containsKey(b.getClass()))
			for (Subscriber A : messages.get(b.getClass())) {
				subscribers.get(A).add(b);
				synchronized (A) {
					A.notifyAll();
				}
			}
	}

	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		if (messages.containsKey(e.getClass())) {
			Future F = new Future<>();
			Subscriber cursub = messages.get(e.getClass()).poll();
			if (cursub != null) {
				messages.get(e.getClass()).add(cursub);
				subscribers.get(cursub).add(e);
				futureToEvent.put(e, F);
				synchronized (cursub) {
					cursub.notifyAll();
				}
			} else {
				F.resolve("false");
				futureToEvent.put(e, F);
			}
			return F;
		}
		return null;
	}

	@Override
	public void register(Subscriber m) {
		if (!subscribers.containsKey(m))
			subscribers.put(m, new ConcurrentLinkedQueue<Message>());
	}

	@Override
	public void unregister(Subscriber m) {
		if (subscribers.containsKey(m))
			subscribers.remove(m);
		for (Class<? extends Message> curmsg : messages.keySet())
			messages.get(curmsg).remove(m);
	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		if (!subscribers.containsKey(m))
			throw new InterruptedException("not registred subscriber");
		synchronized (m) {
			while (subscribers.get(m).isEmpty())
				try {
					m.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
		return subscribers.get(m).poll();
	}


}