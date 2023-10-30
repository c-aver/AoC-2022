import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Main {
    private static interface Value {

    }

    public static class IntegerValue implements Value {
        public int value;
        public IntegerValue(int v) { value = v; }
    }

    public static class ListValue implements Value {
        public List<Value> value;
        public ListValue(List<Value> l) { value = l; }
        public ListValue(int v) {
            value = new ArrayList<>();
            value.add(new IntegerValue(v));
        }
        public ListValue(Value v) {
            if (v instanceof ListValue) {
                value = ((ListValue) v).value;
            } else {
                value = new ArrayList<>();
                value.add((IntegerValue) v);
            }
        }
        public ListValue(String s) {
            value = new ArrayList<>();
            assert s.charAt(0) == '[' && s.charAt(s.length() - 1) == ']';
            if (s.length() == 2) return;
            String noBracks = s.substring(1, s.length() - 1);
            String[] vals = splitByCommas(noBracks);
            for (String sval : vals) {
                Value val;
                try {
                    val = new IntegerValue(Integer.parseInt(sval));
                } catch (NumberFormatException e) {
                    val = new ListValue(sval);
                }
                value.add(val);
            }
        }

        // 1,[2,[3,[4,[5,6,7]]]],8,[9,3]
        //                         ^

        private static String[] splitByCommas(String s) {
            ArrayList<String> result = new ArrayList<>();
            int depth = 0;
            int curStart = 0;
            for (int i = 0; i < s.length(); ++i) {
                char c = s.charAt(i);
                if (c == '[') depth++;
                else if (c == ']') depth--;
                else if (depth == 0 && c == ',') {
                    result.add(s.substring(curStart, i));
                    curStart = i + 1;
                }
                assert depth >= 0;
            }
            result.add(s.substring(curStart, s.length()));
            return result.toArray(new String[0]);
        }
    }

    private static class ValueComparator implements Comparator<Value> {
        public int compare(Value v1, Value v2) {
            if (v1 instanceof IntegerValue && v2 instanceof IntegerValue) {
                return Integer.compare(((IntegerValue) v1).value, ((IntegerValue) v2).value);
            }
            if (v1 instanceof ListValue && v2 instanceof ListValue) {
                List<Value> l1 = ((ListValue) v1).value;
                List<Value> l2 = ((ListValue) v2).value;
                int i = 0;
                while (i < l1.size() && i < l2.size()) {
                    int cmp = this.compare(l1.get(i), l2.get(i));
                    if (cmp != 0) return cmp;
                    i += 1;
                }
                return l1.size() - l2.size();
            }
            v1 = new ListValue(v1);
            v2 = new ListValue(v2);
            return this.compare(v1, v2);
        }
    }
    
    public static void main(String[] args) {
        String filePath;
        if (args.length < 1) {
            filePath = "./13/sample.txt";
        } else {
            filePath = args[0];
        }
        
        ArrayList<String> lines = readLinesFromFile(filePath);

        System.out.println("Part 1: " + part1(lines));
        System.out.println("Part 2: " + part2(lines));
    }

    public static int part1(ArrayList<String> lines) {
        ValueComparator comp = new ValueComparator();
        int result = 0;
        for (int i = 0; i < (lines.size() + 1) / 3; ++i) {
            Value v1 = new ListValue(lines.get(i*3));
            Value v2 = new ListValue(lines.get(i*3 + 1));
            if (comp.compare(v1, v2) < 0) result += i + 1;
            // System.out.println("Finished comparing pair " + i);
        }

        return result;
    }

    public static int part2(ArrayList<String> lines) {
        ArrayList<Value> lists = new ArrayList<>();
        for (String line : lines) {
            if (line.length() > 0) lists.add(new ListValue(line));
        }
        Value divider1 = new ListValue("[[2]]");
        Value divider2 = new ListValue("[[6]]");
        lists.add(divider1);
        lists.add(divider2);

        Collections.sort(lists, new ValueComparator());

        int div1i = -1, div2i = -1;
        for (int i = 0; i < lists.size(); ++i) {
            if (lists.get(i) == divider1) div1i = i + 1;
            if (lists.get(i) == divider2) div2i = i + 1;
        }

        return div1i * div2i;
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