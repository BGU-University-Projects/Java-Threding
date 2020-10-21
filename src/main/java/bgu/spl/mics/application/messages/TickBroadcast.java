package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {
    private int time;
    private int maxtime;

    public TickBroadcast(int curtime,int maxt) {
        this.time = curtime;
        this.maxtime=maxt;
    }
    public int getTime() {
        return time;
    }
    public int getMaxTime() {
        return maxtime;
    }
}
