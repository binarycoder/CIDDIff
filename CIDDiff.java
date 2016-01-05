/*/////////////////////////////////
 * CIDDiff V0.2
 * Creates readable difference reports for ClienItemDefinitions files from PS2.
 * Created by: Alek "binarycoder" Bollig, 2016
*//////////////////////////////////
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;


public class CIDDiff {
	public static void main(String[] args) {
		System.out.println("CIDDiff v 0.2");
		System.out.println("Scanning ClientItemDefinitions.txt");
		ArrayList<ArrayList<String>> oldData = parseString("ClientItemDefinitions.txt");
		System.out.println("Finished.");
		System.out.println("Scanning ClientItemDefinitions_new.txt");
		ArrayList<ArrayList<String>> newData = parseString("ClientItemDefinitions_new.txt");
		System.out.println("Finished");
		System.out.println("Reading Differences");
		ArrayList<String> resultantData = compareData(oldData, newData);
		System.out.println("Differences Found: ");
		System.out.println("---------------------------------------------------------");
		for (int i = 0; i < resultantData.size(); i++) {
			System.out.println(resultantData.get(i));
		}
		System.out.println("---------------------------------------------------------");
		
	}
	static ArrayList<ArrayList<String>> parseString (String fileLocation) {
		File file = new File(fileLocation);
		ArrayList<ArrayList<String>> outputData = new ArrayList<ArrayList<String>>();
		try {
			Scanner fileRead = new Scanner(file);
			int dataPosition = 0;
			while (fileRead.hasNextLine()) {
				outputData.add(new ArrayList<String>());
				String input = fileRead.nextLine();
				String output = "";
				for (int i = 0; i < input.length(); i++) {
					if (input.charAt(i) == '^') {
						outputData.get(dataPosition).add(output);
						output = "";
					} else {
						output += input.charAt(i);
					}
				}
				dataPosition++;
			}
			return outputData;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Error: " + fileLocation
					+ " not found in local directory. Exiting.");
			e.printStackTrace();
		}
		return null;
		
	}
	static ArrayList<String> compareData (ArrayList<ArrayList<String>> oldData, ArrayList<ArrayList<String>> newData) {
		ArrayList<String> output = new ArrayList<String>();
		
		while (newData.size() > oldData.size()) {
			String outputString = "New Entry: ";
			for (int i =0; i < newData.get(newData.size() - 1).size(); i++) {
				outputString += "\n * " + newData.get(0).get(i) + ": "
						+ newData.get(newData.size() - 1).get(i);
			}
			output.add(outputString);
			newData.remove(newData.size() - 1);
		}
		for (int i = 0; i < newData.size(); i ++) {
			for (int j = 0; j < newData.get(i).size(); j++) {
				if (!(oldData.get(i).get(j).equals(newData.get(i).get(j)))) {
					output.add(newData.get(i).get(0) + "- " + newData.get(0).get(j) + " changed from "
							+ oldData.get(i).get(j) + " to " + newData.get(i).get(j));
				}
			}
		}
		return output;
	}
}
