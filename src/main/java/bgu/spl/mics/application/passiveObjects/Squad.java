package bgu.spl.mics.application.passiveObjects;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Agent;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {
	private static class SingletonHolder {
		private static Squad instance = new Squad();
		}
	private Map<String, Agent> agents = new HashMap<>();
	//private Semaphore semaphore=new Semaphore(1);
	/**
	 * Retrieves the single instance of this class.
	 */
	public static Squad getInstance() {
		return SingletonHolder.instance;
	}
	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
	 * @param agents 	Data structure containing all data necessary for initialization
	 * 						of the squad.
	 */
	public void load ( Agent[] agents) {
		for (int i = 0; i < agents.length; i++) {
			this.agents.put(agents[i].getSerialNumber(),agents[i]);
		}
	}

	/**
	 * Releases agents.
	 */
	public void releaseAgents(List<String> serials){
		for (int i=0;i<serials.size();i++) {
				agents.get(serials.get(i)).release();
		}
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   milliseconds to sleep
	 */
	public void sendAgents(List<String> serials, int time)throws InterruptedException {
			boolean finish = false;
			while (!finish) {
				try {
					finish = true;
					TimeUnit.MILLISECONDS.sleep((time * 100));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			this.releaseAgents(serials);

		this.notifyAll();
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public synchronized boolean getAgents(List<String> serials) throws InterruptedException {
		for (int i = 0; i < serials.size(); i++)
			if (!agents.containsKey(serials.get(i)))
				return false;
		Collections.sort(serials);
		for (int i = 0; i < serials.size(); i++) {
			while (!agents.get(serials.get(i)).isAvailable()) {
					try {
							this.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			agents.get(serials.get(i)).acquire();
		}
		return true;
	}


    /**
     * gets the agents names
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
	public List<String> getAgentsNames(List<String> serials) {
		List <String> output = new LinkedList<String>();
		for (int i=0;i<serials.size();i++)
			output.add(agents.get(serials.get(i)).getName());
		return output;
	}

}
