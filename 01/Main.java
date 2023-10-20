import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Main {
	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("ERROR: Please provide input file path in arguments!");
			System.exit(1);
		}
		String filePath = args[0];
		System.out.println(part2(filePath));
	}
	public static int part1(String filePath) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String line = reader.readLine();
			ArrayList<Integer> cals = new ArrayList<Integer>();
			int elf = 0;
			while (line != null){
				if (line.length() == 0) {
					cals.add(elf);
					elf = 0;
				} else {
					elf += Integer.parseInt(line);
				}
				line = reader.readLine();
			}
			reader.close();
			return Collections.max(cals);
		} catch (IOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
		return -1;
	}
	public static int part2(String filePath) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String line = reader.readLine();
			ArrayList<Integer> cals = new ArrayList<Integer>();
			int elf = 0;
			while (line != null){
				if (line.length() == 0) {
					cals.add(elf);
					elf = 0;
				} else {
					elf += Integer.parseInt(line);
				}
				line = reader.readLine();
			}
			int[] topThree = new int[3];
			for (int contender : cals) {
				if (contender > topThree[0]) { // #1
					topThree[2] = topThree[1];
					topThree[1] = topThree[0];
					topThree[0] = contender;
				}
				else if(contender > topThree[1]) { // #2
					topThree[2] = topThree[1];
					topThree[1] = contender;
				}
				else if (contender > topThree[2]) { // #3
					topThree[2] = contender;
				}
			}
			reader.close();
			return topThree[0] + topThree[1] + topThree[2];
		} catch (IOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
		return -1;
	}
}
