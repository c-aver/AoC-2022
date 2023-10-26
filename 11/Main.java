import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.UnaryOperator;

public class Main {
    private static final boolean relief = false;
    private static final int rounds = 10_000;

    private static int factorProduct = 1;

    private static class Monkey {
        public long inspects = 0;

        Queue<Long> items;
        UnaryOperator<Long> operation;
        int testDivisor;
        int ifTrue;
        int ifFalse;
        private long[] inspectTop() {
            inspects += 1;
            long[] result = new long[2];
            long item = items.remove();
            item = operation.apply(item) % factorProduct;
            if (relief) item /= 3;
            result[1] = item;
            if (item % testDivisor == 0) {
                result[0] = ifTrue;
            } else {
                result[0] = ifFalse;
            }
            return result;
        }
        private void yoink(long item) {
            items.add(item);
        }

        public Monkey(Queue<Long> items, UnaryOperator<Long> operation, int testDivisor, int ifTrue, int ifFalse) {
            this.items = items;
            this.operation = operation;
            this.testDivisor = testDivisor;
            this.ifTrue = ifTrue;
            this.ifFalse = ifFalse;
        }
    }

    private static class Monkeys {
        private ArrayList<Monkey> monkeys;
        private void turn(int which) {
            Monkey monkey = monkeys.get(which);
            while (!monkey.items.isEmpty()) {
                long[] yeet = monkey.inspectTop();
                long yoinker = yeet[0];
                monkeys.get((int) yoinker).yoink(yeet[1]);
            }
        }
        private void round() {
            for (int i = 0; i < monkeys.size(); ++i) {
                turn(i);
            }
        }

        public long[] getInspects() {
            long[] result = new long[monkeys.size()];
            for (int i = 0; i < monkeys.size(); ++i) {
                result[i] = monkeys.get(i).inspects;
            }
            Arrays.sort(result);
            return result;
        }

        public Monkeys(ArrayList<String> lines) {
            this.monkeys = new ArrayList<>();
            int numMonkeys = (lines.size()+1)/7;
            for (int i = 0; i < numMonkeys; ++i) {
                int firstLine = i*7;
                // int place = Integer.parseInt(lines.get(firstLine+0).split("[ :]")[1]);
                Queue<Long> items = new LinkedList<>();
                String[] itemsStrings = lines.get(firstLine+1).split(": ")[1].replace(" ", "").split(",");
                for (String itemString : itemsStrings) {
                    items.add(Long.parseLong(itemString));
                }
                String[] operationStrings = lines.get(firstLine+2).split("= ")[1].split(" ");
                UnaryOperator<Long> operation;
                if (operationStrings[2].compareTo("old") == 0) {
                    operation = (x -> x*x);
                } else {
                    int factor = Integer.parseInt(operationStrings[2]);
                    if (operationStrings[1].charAt(0) == '+') {
                        operation = (x -> x+factor);
                    } else {
                        operation = (x -> x*factor);
                    }
                }
                int testDivisor = Integer.parseInt(lines.get(firstLine+3).split("y ")[1]);
                factorProduct *= testDivisor;
                int ifTrue = Integer.parseInt(lines.get(firstLine+4).split("y ")[1]);
                int ifFalse = Integer.parseInt(lines.get(firstLine+5).split("y ")[1]);
                monkeys.add(new Monkey(items, operation, testDivisor, ifTrue, ifFalse));
            }
        }
    }

    public static void main(String[] args) {
        String filePath;
        if (args.length < 1) {
            filePath = "./11/sample.txt";
        } else {
            filePath = args[0];
        }
        
        ArrayList<String> lines = readLinesFromFile(filePath);

        Monkeys monkeys = new Monkeys(lines);
        System.out.println("Finished parsing monkeys!");
        for (int i = 0; i < rounds; ++i) {
            monkeys.round();
            if (i % 1000 == 0) System.out.println("After " + i + " rounds: " + Arrays.toString(monkeys.getInspects()));
        }
        System.out.println("After 10000 rounds: " + Arrays.toString(monkeys.getInspects()));
        long[] inspects = monkeys.getInspects();
        System.out.println("Monkey business: " + inspects[monkeys.monkeys.size()-2]*(long) inspects[monkeys.monkeys.size()-1]);
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