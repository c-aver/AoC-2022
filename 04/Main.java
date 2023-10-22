import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String filePath;
        if (args.length < 1) {
            filePath = "./04/sample.txt";
        } else {
            filePath = args[0];
        }
        ArrayList<String> lines = readLinesFromFile(filePath);
        // System.out.println("Part 1: " + parse(lines, 1));
        System.out.println("Part 2: " + parse(lines, 2));
    }

    public static int parse(ArrayList<String> lines, int part) {
        int result = 0;
        for (String line : lines) {
            int min1, max1, min2, max2;
            String nums[] = line.split("[,-]");
            // System.out.println("Nums: (\"" + nums[0] + "\", \"" + nums[1] + "\", \"" + nums[2] + "\", \"" + nums[3] + "\")");
            min1 = Integer.parseInt(nums[0]);
            max1 = Integer.parseInt(nums[1]);
            min2 = Integer.parseInt(nums[2]);
            max2 = Integer.parseInt(nums[3]);
            boolean condition = false;
            if (part == 1) {
                condition = contains(min1, max1, min2, max2);
            } else if (part == 2) {
                condition = overlaps(min1, max1, min2, max2);
            } else assert false;
            if (condition) {
                result += 1;
            }
        }
        return result;
    }

    public static boolean contains(int min1, int max1, int min2, int max2) {
        if ((min1 <= min2 && max1 >= max2) || (min2 <= min1 && max2 >= max1)) {
            return true;
        }
        return false;
    }

    public static boolean overlaps(int min1, int max1, int min2, int max2) {
        if (contains(min1, max1, min2, max2) || (min1 <= min2 && max1 <= max2 && max1 >= min2) || (min2 <= min1 && max2 <= max1 && max2 >= min1)) {
            return true;
        }
        return false;
    }

    public static ArrayList<String> readLinesFromFile(String filePath) {
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line = reader.readLine();
			while (line != null && line.length() > 0) {
                lines.add(line);
				line = reader.readLine();
			}
		} catch (IOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
        return lines;
    }
}
