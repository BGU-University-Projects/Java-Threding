package bgu.spl.mics.application.passiveObjects;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Passive object representing the diary where all reports are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Diary {
	private static class SingletonHolder {
		private static Diary instance = new Diary();
	}
	private LinkedList<Report> reports=new LinkedList<>();
	private int total=0;

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Diary getInstance() { return SingletonHolder.instance; }
	public List<Report> getReports() { return reports; }
	/**
	 * adds a report to the diary
	 * @param reportToAdd - the report to add
	 */
	public void addReport(Report reportToAdd){ reports.add(reportToAdd); }
	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<Report> which is a
	 * List of all the reports in the diary.
	 * This method is called by the main method in order to generate the output.
	 */
	public void printToFile(String filename) {
		JSONObject JoDiary = new JSONObject();
		JoDiary.put("total", total);
		JSONArray JaReports = new JSONArray();
		for (int i=0;i<reports.size();i++) {
			Report report = reports.get(i);
			JSONObject JoReport = new JSONObject();
			JoReport.put("missionName",report.getMissionName());
			JoReport.put("m",report.getM());
			JoReport.put("moneypenny",report.getMoneypenny());
			JoReport.put("agentsSerialNumbers",report.getAgentsSerialNumbersNumber());
			JoReport.put("agentsNames",report.getAgentsNames());
			JoReport.put("gadgetName",report.getGadgetName());
			JoReport.put("timeCreated",report.getTimeCreated());
			JoReport.put("timeIssued",report.getTimeIssued());
			JoReport.put("qTime",report.getQTime());
			JaReports.add(JoReport);
		}
		JoDiary.put("Reports", JaReports);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(String.valueOf(JoDiary));
		String s = gson.toJson(je);
		try (FileWriter file = new FileWriter(filename)) {
			file.write(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Gets the total number of received missions (executed / aborted) be all the M-instances.
	 * @return the total number of received missions (executed / aborted) be all the M-instances.
	 */
	public int getTotal(){ return total; }
	/**
	 * Increments the total number of received missions by 1
	 */
	public void incrementTotal(){ this.total ++; }


}
