package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import java.util.List;

public class SendAgentsEvent implements Event<List<String>>{
    private List<String> agentstosend;
    private int time;
    public SendAgentsEvent(List<String> agents,int time) {
        agentstosend=agents;
        this.time=time;
    }
    public List<String> getAgentstosend(){
        return  agentstosend;
    }
    public int getTime(){
        return  time;
    }




}
