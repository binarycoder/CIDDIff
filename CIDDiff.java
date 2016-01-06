/*/////////////////////////////////
 * CIDDiff V1.2
 * Creates readable difference reports for ClienItemDefinitions files from PS2.
 * Created by: Alek "binarycoder" Bollig, 2016
*//////////////////////////////////
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;


public class CIDDiff {
	public static void main(String[] args) {
		System.out.println("CIDDiff v 1.2");
		System.out.println("Scanning ClientItemDefinitions.txt");
		ArrayList<ArrayList<String>> oldData = parseString("ClientItemDefinitions.txt");
		System.out.println("Finished.");
		System.out.println("Scanning ClientItemDefinitions_new.txt");
		ArrayList<ArrayList<String>> newData = parseString("ClientItemDefinitions_new.txt");
		System.out.println("Finished..");
		System.out.println("Scanning en_us_data.dat");
		ArrayList<String[]> nameData = parseLocale("en_us_data.dat");
		System.out.println("Finished...");
		System.out.println("Reading Differences");
		ArrayList<String> resultantData = compareData(oldData, newData, nameData);
		System.out.println("Differences Found: ");
		System.out.println("---------------------------------------------------------");
		for (int i = 0; i < resultantData.size(); i++) {
			System.out.println(resultantData.get(i));
		}
		System.out.println("---------------------------------------------------------");
		Path file = Paths.get("output.txt");
		try {
			Files.write(file, resultantData, Charset.forName("UTF-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Results written to \"output.txt\"");
	}
	private static ArrayList<String[]> parseLocale(String fileLocation) {
		File file = new File(fileLocation);
		ArrayList<String[]> data = new ArrayList<String[]>();
		try {
			Scanner in = new Scanner(file);
			while (in.hasNextLine()) {
				String input = in.nextLine();
				if (input.indexOf("u") != -1) {
					String index = input.substring(0,(input.indexOf("u"))).trim();
				String text = input.substring((input.indexOf("u")+4)).trim();
				String[] addToArray = {index,text};
				data.add(addToArray);
				}		
			}
			in.close();
			return data;
		} catch (FileNotFoundException e) {
			System.out.println("Error: " + fileLocation
					+ " not found in local directory. Exiting.");
			e.printStackTrace();
			System.exit(1);
		}
		return null;
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
			fileRead.close();
			return outputData;
		} catch (FileNotFoundException e) {
			System.out.println("Error: " + fileLocation
					+ " not found in local directory. Exiting.");
			e.printStackTrace();
			System.exit(1);
		}
		return null;
		
	}
	static ArrayList<String> compareData (ArrayList<ArrayList<String>> oldData,
			ArrayList<ArrayList<String>> newData, ArrayList<String[]> nameData) {
		ArrayList<String> output = new ArrayList<String>();
		while (newData.size() > oldData.size()) {
			String outputString = "New Entry: ";
			for (int i =0; i < newData.get(newData.size() - 1).size(); i++) {
				if (i == 2 || i == 3) {
					String realName = "Global.Text." + (newData.get(newData.size() - 1).get(i));
					JenkinsHash jh = new JenkinsHash();
					long realLong = jh.hash(realName.getBytes());
					for (int j = 0; j < nameData.size(); j++) {
						String[] tempArray = nameData.get(j);
						if (tempArray[0].equals(String.valueOf(realLong))) {
							realName = tempArray[1];
						}
					}
					outputString += "\n * " + newData.get(0).get(i) + ": \""
							+ realName + "\" (" + newData.get(newData.size() - 1).get(i)
							+ ")";
				} else {
					outputString += "\n * " + newData.get(0).get(i) + ": "
						+ newData.get(newData.size() - 1).get(i);
				}
			}
			output.add(outputString);
			newData.remove(newData.size() - 1);
		}
		for (int i = 0; i < newData.size(); i ++) {
			for (int j = 0; j < newData.get(i).size(); j++) {
				if (!(oldData.get(i).get(j).equals(newData.get(i).get(j)))) {
					String realName = "Global.Text." + (newData.get(i).get(2));
					JenkinsHash jh = new JenkinsHash();
					long realLong = jh.hash(realName.getBytes());
					for (int k = 0; k < nameData.size(); k++) {
						String[] tempArray = nameData.get(k);
						if (tempArray[0].equals(String.valueOf(realLong))) {
							realName = tempArray[1];
						}
					}
					output.add(" \"" + realName + "\" " + "(" + newData.get(i).get(0) + ")- " + newData.get(0).get(j) + " changed from "
							+ oldData.get(i).get(j) + " to " + newData.get(i).get(j));
				}
			}
		}
		return output;
	}
}
