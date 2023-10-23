import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    private static Dir root = new Dir(null, "/");
    private static Dir currDir = root;
    private static final int totalSpace = 70000000;
    private static final int requiredSpace = 30000000;

    private static abstract class Node {
        public Dir parent;
        public String name;
        public abstract int size();
    }

    private static class File extends Node {
        public int size;

        public File(Dir parent, String name, int size) {
            this.size = size;
            this.name = name;
            this.parent = parent;
        }

        public int size() {
            return size;
        }
    }

    private static class Dir extends Node {
        ArrayList<Node> children;

        public Dir(Dir parent, String name) {
            children = new ArrayList<>();
            this.name = name;
            this.parent = parent;
        }

        public void addChild(Node child) {
            children.add(child);
        }

        public int size() {
            int result = 0;
            for (Node child : children) {
                result += child.size();
            }
            return result;
        }
    }

    public static void parseLine(String line) {
        String[] splits = line.split(" ");
        if (line.charAt(0) == '$') {    // command
            String cmd = splits[1];
            if (cmd.compareTo("cd") == 0) {
                String newDirName = splits[2];
                if (newDirName.compareTo("/") == 0) {
                    currDir = root;
                } else if (newDirName.compareTo("..") == 0) {
                    currDir = currDir.parent;
                } else {
                    currDir = (Dir) currDir.children.stream().filter(d -> (d instanceof Dir && d.name.compareTo(newDirName) == 0)).findAny().get();
                }
            } else if (cmd.compareTo("ls") == 0) {
                // do nothing?
            }
        } else {    // node inside current directory
            Node newNode;
            if (splits[0].compareTo("dir") == 0) {  // directory
                String newDirName = splits[1];
                newNode = new Dir(currDir, newDirName);
            } else {
                int newFileSize = Integer.parseInt(splits[0]);
                String newFileName = splits[1];
                newNode = new File(currDir, newFileName, newFileSize);
            }
            if (!currDir.children.stream().anyMatch(n -> n.name.compareTo(newNode.name) == 0)) {
                currDir.addChild(newNode);
            }
        }
    }

    public static void main(String[] args) {
        String filePath;
        if (args.length < 1) {
            filePath = "./07/sample.txt";
        } else {
            filePath = args[0];
        }
        ArrayList<String> lines = readLinesFromFile(filePath);
        for (String line : lines) {
            parseLine(line);
        }
        System.out.println("Done parsing tree");

        System.out.println("Part 1: " + part1());
        System.out.println("Part 2: " + part2());
    }

    private static int part1Result = 0;
    private static void checkDir(Dir d) {
        if (d.size() <= 100_000) {
            part1Result += d.size();
        }
        for (Node n : d.children) {
            if (n instanceof Dir) {
                checkDir((Dir) n);
            }
        }
    }
    public static int part1() {
        checkDir(root);
        return part1Result;
    }

    private static ArrayList<Dir> findDirs(Dir d) {
        ArrayList<Dir> result = new ArrayList<>();
        result.add(d);
        for (Node n : d.children) {
            if (n instanceof Dir) {
                result.addAll(findDirs((Dir) n));
            }
        }
        return result;
    }

    public static int part2() {
        int neededToFree = root.size() - (totalSpace - requiredSpace);
        ArrayList<Dir> dirs = findDirs(root);
        return dirs.stream().map(Dir::size).filter(i -> i >= neededToFree).min(Integer::compare).get();
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
