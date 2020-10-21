package bgu.spl.mics.application.passiveObjects;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
/**
 *  That's where Q holds his gadget (e.g. an explosive pen was used in GoldenEye, a geiger counter in Dr. No, etc).
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Inventory {
	private static class SingletonHolder {
		private static Inventory instance = new Inventory();
	}
	private List<String> gadgets= new LinkedList<>();
	/**
	 * Retrieves the single instance of this class.
	 */
	public static Inventory getInstance() {
		return SingletonHolder.instance;
	}
	/**
	 * Initializes the inventory. This method adds all the items given to the gadget
	 * inventory.
	 * <p>
	 * @param inventory 	Data structure containing all data necessary for initialization
	 * 						of the inventory.
	 */
	public void load (String[] inventory) {
		for (int i = 0; i<inventory.length;i++)
			gadgets.add(inventory[i]);
	}
	/**
	 * acquires a gadget and returns 'true' if it exists.
	 * <p>
	 * @param gadget 		Name of the gadget to check if available
	 * @return 	‘false’ if the gadget is missing, and ‘true’ otherwise
	 */
	public boolean getItem(String gadget) {
		for (int i=0;i<gadgets.size();i++)
			if (gadgets.get(i).equals(gadget)) {
				gadgets.remove(i);
				return true;
			}
		return false;
	}
	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<Gadget> which is a
	 * List of all the reports in the diary.
	 * This method is called by the main method in order to generate the output.
	 */
	public void printToFile(String filename) {
		JSONObject JoInventory = new JSONObject();
		JoInventory.put("inventory",gadgets);
		try (FileWriter file = new FileWriter(filename)) {
			file.write(JoInventory.toJSONString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}}
