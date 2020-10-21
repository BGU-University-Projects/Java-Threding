package bgu.spl.mics.application.subscribers;
import bgu.spl.mics.Future;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Report;
import java.util.List;
/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
	private Diary diary;
	private int time;
	int maxMissions;

	public M(String name, Diary diary, int maxMissions) {
		super(name);
		this.diary = diary;
		this.maxMissions = maxMissions;
	}

	@Override
	protected void initialize() {
		Thread.currentThread().setName(this.getName());
		subscribeBroadcast(TerminatorEvent.class, message -> {
			terminate();
		});
		subscribeBroadcast(TickBroadcast.class, message -> {
			time = message.getTime();
			if (message.getTime() == message.getMaxTime()) {
				terminate();
				diary.printToFile("OutputDiary.json");
			}
		});
		subscribeEvent(MissionReceivedEvent.class, message -> {
					if (time <= message.getMission().getTimeExpired()) {
						List<String> agents = message.getMission().getSerialAgentsNumbers();
						Future<String> areAvilable = MessageBrokerImpl.getInstance().sendEvent(new AgentsAvailableEvent(agents));
						if ((areAvilable != null) && (areAvilable.get() != "false")) {
							Future<String> isGadget = MessageBrokerImpl.getInstance().sendEvent(new GadgetAvailableEvent(message.getMission().getGadget()));
							if ((isGadget.get() != "false")) {
								Future<List<String>> SendThem = MessageBrokerImpl.getInstance().sendEvent(new SendAgentsEvent(agents, message.getMission().getDuration()));
								Report report = new Report();
								report.setMissionName(message.getMission().getMissionName());
								report.setM(Integer.parseInt(this.getName().substring(2)));
								report.setMoneypenny(Integer.parseInt(areAvilable.get().substring(12)));
								report.setAgentsSerialNumbersNumber(agents);
								report.setAgentsNames(SendThem.get());
								report.setGadgetName(message.getMission().getGadget());
								report.setTimeIssued(time);
								report.setQTime(Integer.parseInt(isGadget.get()));
								report.setTimeCreated(time);
								diary.addReport(report);
							} else if (isGadget.get() == "false")
								MessageBrokerImpl.getInstance().sendEvent(new AgentsRealseEvent(agents));
						}
					}

					diary.incrementTotal();
					if (diary.getTotal() == maxMissions) {
						diary.printToFile("diaryOutputFile.json");
						MessageBrokerImpl.getInstance().sendBroadcast(new TerminatorEvent());
					}
				}
		);

	}
}
