import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please provide input file");
            System.exit(1);
        }
        ArrayList<String> lines = readLinesFromFile(args[0]);
        // System.out.println("Part 1: " + part1(lines));
        System.out.println("Part 2: " + part2(lines));
    }

    public static int part1(ArrayList<String> lines) {
        int result = 0;
        for (int i = 0; i < lines.size() && lines.get(i) != null; ++ i) {
            int pri = convertToPriority(findDuplicate(lines.get(i)));
            result += pri;
        }
        return result;
    }

    public static int part2(ArrayList<String> lines) {
        int result = 0;
        for (int i = 0; i < lines.size() - 1; i += 3) {
            result += convertToPriority(findCommon(lines.get(i), lines.get(i + 1), lines.get(i + 2)));
        }
        return result;
    }

    public static char findCommon(String s1, String s2, String s3) {
        String chars = s1;
        for (int i = 0; i < chars.length(); ++i) {
            char c = chars.charAt(i);
            if (s2.indexOf(c) == -1 || s3.indexOf(c) == -1) {
                chars = chars.replace(c, ' ');
            }
        }
        chars = chars.replace(" ", "");
        assert chars.length() == 1;
        return chars.charAt(0);
    }

    public static int convertToPriority(char c) {
        if ('a' <= c && c <= 'z') {
            return c - 'a' + 1;
        } else if ('A' <= c && c <= 'Z') { 
            return c - 'A' + 27;
        }
        assert false;
        return 0;
    }

    public static char findDuplicate(String line) {
        int n = line.length();
        assert n % 2 == 0;
        for (int i = 0; i < n/2; ++i) {
            for (int j = n/2; j < n; ++j) {
                if (line.charAt(i) == line.charAt(j)) {
                    return line.charAt(i);
                }
            }
        }
        assert false;
        return 0;
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