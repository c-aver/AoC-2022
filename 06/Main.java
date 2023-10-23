import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        String filePath;
        if (args.length < 1) {
            filePath = "./06/sample.txt";
        } else {
            filePath = args[0];
        }
        ArrayList<String> lines = readLinesFromFile(filePath);
        System.out.println("Part 1: ");
        for (String line : lines) {
            System.out.println(part1(line));
        }

        System.out.println("Part 2: ");
        for (String line : lines) {
            System.out.println(part2(line));
        }
    }

    public static int part1(String line) {
        for (int i = 3; i < line.length(); ++i) {
            if (allDifferent(line.substring(i - 3, i + 1))) return i + 1;
        }
        return -1;
    }

    public static int part2(String line) {
        for (int i = 13; i < line.length(); ++i) {
            if (allDifferent(line.substring(i - 13, i + 1))) return i + 1;
        }
        return -1;
    }

    public static boolean allDifferent(String buf) {
        Set<Character> seen = new HashSet<>();
        for (int i = 0; i < buf.length(); ++i) {
            char c = buf.charAt(i);
            if (seen.contains(c)) return false;
            seen.add(c);
        }
        return true;
    }


    public static ArrayList<String> readLinesFromFile(String filePath) {
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line = reader.readLine();
			while (line != null) {
                lines.add(line);
				line = reader.readLine();
			}
		} catch (IOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
        return lines;
    }
}
