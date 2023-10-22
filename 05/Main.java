import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        String filePath;
        if (args.length < 1) {
            filePath = "./05/sample.txt";
        } else {
            filePath = args[0];
        }
        ArrayList<String> lines = readLinesFromFile(filePath);
        parseInitialStacks(lines);
        System.out.println("Part 1: " + parse(lines, 1));
        System.out.println("Part 2: " + parse(lines, 2));
    }

    public static String parse(ArrayList<String> lines, int part) {
        ArrayList<Stack<Character>> stacks = parseInitialStacks(lines);
        for (String line : lines) {
            performAction(stacks, line, part);
        }
        StringBuilder result = new StringBuilder();
        for (Stack<Character> stack : stacks) {
            if (stack.size() > 0) result.append(stack.peek());
        }
        return result.toString();
    }

    public static ArrayList<Stack<Character>> parseInitialStacks(ArrayList<String> lines) {
        ArrayList<Stack<Character>> result = new ArrayList<>();
        int h = 0, w = 0;
        while (h < lines.size() && lines.get(h).length() > 0) {
            h += 1;
        }
        h -= 1;
        w = (lines.get(0).length() + 1)/4;
        for (int i = 0; i < w; ++i) {
            result.add(new Stack<>());
        }
        // System.out.println(h);
        // System.out.println(w);
        for (int l = h - 1; l >= 0; --l) {
            String line = lines.get(l);
            for (int r = 0; r < w; ++r) {
                char c = line.charAt(4*r+1);
                if (c != ' ') result.get(r).push(line.charAt(4*r+1));
            }
        }
        return result;
    }

    public static void performAction(ArrayList<Stack<Character>> stacks, String action, int part) {
        String[] splits = action.split(" ");
        if (splits[0].compareTo("move") != 0) return;
        int numMoved = Integer.parseInt(splits[1]);
        Stack<Character> src = stacks.get(Integer.parseInt(splits[3]) - 1);
        Stack<Character> dst = stacks.get(Integer.parseInt(splits[5]) - 1);
        if (part == 1) {
            for (int i = 0; i < numMoved; ++i) {
                dst.push(src.pop());
            }
        } else if (part == 2) {
            Stack<Character> crane = new Stack<>();
            for (int i = 0; i < numMoved; ++i) {
                crane.push(src.pop());
            }
            for (int i = 0; i < numMoved; ++i) {
                dst.push(crane.pop());
            }
        } else assert false;
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
