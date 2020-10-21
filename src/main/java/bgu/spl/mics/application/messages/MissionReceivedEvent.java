package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.MissionInfo;


public class MissionReceivedEvent implements Event<String>{
    private MissionInfo curmission;
    public MissionReceivedEvent(MissionInfo cur) {
        this.curmission = cur;
    }
    public MissionInfo getMission() {
        return curmission;
    }
}
