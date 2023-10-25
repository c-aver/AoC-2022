import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String filePath;
        if (args.length < 1) {
            filePath = "./08/sample.txt";
        } else {
            filePath = args[0];
        }
        
        ArrayList<String> lines = readLinesFromFile(filePath);
        int size = lines.size();
        assert lines.get(0).length() == size;

        int[][] arr = convertToInts(lines);

        // System.out.println("Part 1: " + part1(arr));
        System.out.println("Part 2: " + part2(arr));
    }

    public static int[][] convertToInts(ArrayList<String> lines) {
        int size = lines.size();
        int[][] result = new int[size][size];
        for (int i = 0; i < size; ++i) {
            String line = lines.get(i);
            for (int j = 0; j < size; ++j) {
                result[j][i] = line.charAt(j) - '0';
            }
        }
        return result;
    }

    public static int part1(int[][] arr) {
        int result = 0;
        int size = arr.length;
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (isVisible(i, j, arr)) result += 1;
            }
        }
        return result;
    }

    public static boolean isVisible(int x, int y, int[][] arr) {
        int size = arr.length;
        boolean top = true, bottom = true, left = true, right = true;
        int height = arr[x][y];
        for (int i = 0; left && i < x; ++i) {
            if (arr[i][y] >= height) left = false;
        }
        for (int i = x + 1; right && i < size; ++i) {
            if (arr[i][y] >= height) right = false;
        }
        for (int i = 0; top && i < y; ++i) {
            if (arr[x][i] >= height) top = false;
        }
        for (int i = y + 1; bottom && i < size; ++i) {
            if (arr[x][i] >= height) bottom = false;
        }
        return top || bottom || left || right;
    }

    public static int scenicScore(int x, int y, int[][] arr) {
        int height = arr[x][y];
        int size = arr.length;
        int top = 0, bottom = 0, left = 0, right = 0;

        for (int i = x - 1; i >= 0; --i) {
            left += 1;
            if (arr[i][y] >= height) break;
        }
        for (int i = x + 1; i < size; ++i) {
            right += 1;
            if (arr[i][y] >= height) break;
        }
        for (int i = y - 1; i >= 0; --i) {
            top += 1;
            if (arr[x][i] >= height) break;
        }
        for (int i = y + 1; i < size; ++i) {
            bottom += 1;
            if (arr[x][i] >= height) break;
        }

        return top * bottom * left * right;
    }

    public static int part2(int[][] arr) {
        int result = 0;
        int size = arr.length;
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                int sS = scenicScore(i, j, arr);
                if (sS > result) {
                    result = sS;
                }
            }
        }
        return result;
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
