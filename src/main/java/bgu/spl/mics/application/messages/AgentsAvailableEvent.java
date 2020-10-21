package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;

import java.util.List;


public class AgentsAvailableEvent implements Event<String> {
    private List<String> agentsneeded;
    public AgentsAvailableEvent(List<String> agents) {
        agentsneeded=agents;
    }
    public List<String> getAgentsneeded(){
        return  agentsneeded;
    }

}
