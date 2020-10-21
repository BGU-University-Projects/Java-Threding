package bgu.spl.mics.application;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ConcurrentLinkedQueue;

import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {

    public static void main(String[] args) throws IOException, ParseException {

            JSONParser jsonParser = new JSONParser();
            FileReader reader = new FileReader(args[0]);
            Object obj = jsonParser.parse(reader);
            JSONObject jo = (JSONObject) obj;
            int countOfMissions = 0;

            //reading inventory
            JSONArray jaInventory = (JSONArray) jo.get("inventory");
            String[] loadInventory = new String[jaInventory.size()];
            for (int i = 0; i < jaInventory.size(); i++)
                loadInventory[i] = (String) jaInventory.get(i);
            Inventory inventory = new Inventory();
            inventory.load(loadInventory);

            // reading squad
            JSONArray jaSquad = (JSONArray) jo.get("squad");
            Agent[] agents = new Agent[jaSquad.size()];
            for (int i = 0; i < jaSquad.size(); i++) {
                JSONObject agentObj = (JSONObject) jaSquad.get(i);
                String name = (String) agentObj.get("name");
                String sn = (String) agentObj.get("serialNumber");
                agents[i] = new Agent();
                agents[i].setName(name);
                agents[i].setSerialNumber(sn);
            }
            Squad squad = new Squad();
            squad.load(agents);

            // reading services
            JSONObject joServices = (JSONObject) jo.get("services");

            int moneyPennysize = (int) ((long) joServices.get("Moneypenny"));
            Moneypenny[] moneyPenny = new Moneypenny[moneyPennysize];
            for (int i = 0; i < moneyPennysize - 1; i++)
                moneyPenny[i] = new Moneypenny("MonneyPenny " + (String.valueOf(i + 1)), squad);
            moneyPenny[moneyPennysize - 1] = new Moneypenny("JOKER", squad);
            long time = (long) joServices.get("time");
            //getting intelligence missions
            JSONArray jaIntelligence = (JSONArray) joServices.get("intelligence");
            Intelligence[] intelligence = new Intelligence[jaIntelligence.size()];
            for (int i = 0; i < jaIntelligence.size(); i++) {
                JSONObject ja = (JSONObject) jaIntelligence.get(i);
                intelligence[i] = new Intelligence(String.valueOf(i + 1));
                //reading missions
                JSONArray jaMissions = (JSONArray) ja.get("missions");
                for (int j = 0; j < jaMissions.size(); j++) {
                    JSONObject mission = (JSONObject) jaMissions.get(j);
                    List<String> agentSerials = new LinkedList<>();
                    JSONArray serialNumbers = (JSONArray) mission.get("serialAgentsNumbers");
                    for (int k = 0; k < serialNumbers.size(); k++) {
                        String serial = (String) serialNumbers.get(k);
                        agentSerials.add(serial);
                    }
                    String missionName = (String) mission.get("missionName");
                    String missionGadget = (String) mission.get("gadget");
                    int missionTimeExpired = (int) ((long) mission.get("timeExpired"));
                    int missionTimeIssued = (int) ((long) mission.get("timeIssued"));
                    int missionDuration = (int) ((long) mission.get("duration"));
                    MissionInfo toAdd = new MissionInfo();
                    Collections.sort(agentSerials);
                    toAdd.setSerialAgentsNumbers(agentSerials);
                    toAdd.setMissionName(missionName);
                    toAdd.setTimeExpired(missionTimeExpired);
                    toAdd.setTimeIssued(missionTimeIssued);
                    toAdd.setDuration(missionDuration);
                    toAdd.setGadget(missionGadget);
                    intelligence[i].addMission(toAdd);
                    countOfMissions++;
                }
            }
            int msize = (int) ((long) joServices.get("M"));
            Diary diary = new Diary();
            M[] m = new M[msize];
            for (int i = 0; i < msize; i++)
                m[i] = new M("M " + String.valueOf(i + 1), diary, countOfMissions);
            for (int i = 0; i < intelligence.length; i++)
                new Thread(intelligence[i]).start();
            for (int i = 0; i < moneyPenny.length; i++)
                new Thread(moneyPenny[i]).start();
            for (int i = 0; i < m.length; i++)
                new Thread(m[i]).start();
            new Thread(new Q("q", inventory)).start();
            new Thread(new TimeService((int) time)).start();

    } //main
} //class