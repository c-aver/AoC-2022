import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    record Point(int x, int y) {
        public Point(String s) {
            this(Integer.parseInt(s.split(",")[0]), Integer.parseInt(s.split(",")[1]));
        }
        public Point(Point p) {
            this(p.x, p.y);
        }
    }
    public static void main(String[] args) {
        String filePath;
        if (args.length < 1) {
            filePath = "./14/sample.txt";
        } else {
            filePath = args[0];
        }
        
        ArrayList<String> lines = readLinesFromFile(filePath);
        ArrayList<List<Point>> paths = new ArrayList<>();
        int minX, maxX, minY, maxY, offset;
        {
            for (String line : lines) {
                paths.add(parsePath(line));
            }

            int[] limits = findLimits(paths);
            offset = -limits[0] + limits[3] + 2;
            paths = offsetX(paths, offset);
            limits = findLimits(paths);
            minX = limits[0]; maxX = limits[1]; minY = limits[2]; maxY = limits[3];
        }
        System.gc();

        char[][] map = new char[maxX + 1 + maxY + 2][maxY + 3];
        fillMap(map);
        drawPaths(map, paths);
        printMap(map);

        // System.out.println("" + minX + " " + maxX + " " + minY + " " + maxY);

        // System.out.println("Part 1: " + part1(map, new Point(500 + offset, 0)));
        fillMap(map);
        drawPaths(map, paths);
        System.out.println("Part 2: " + part2(map, new Point(500 + offset, 0)));
    }

    private static int part1(char[][] map, Point sandOrigin) {
        int result = 0;
        while (dropSand(map, sandOrigin)) {
            result += 1;
            // printMap(map);
        }
        System.out.println("-----------------------------------------------");
        // printMap(map);
        return result;
    }
    private static int part2(char[][] map, Point sandOrigin) {
        int result = 1;
        while (dropSand2(map, sandOrigin)) {
            result += 1;
            // printMap(map);
        }
        System.out.println("-----------------------------------------------");
        printMap(map);
        return result;
    }

    private static boolean dropSand(char[][] map, Point origin) {
        int x = origin.x, y = origin.y;
        while (y < map[0].length - 2 && x >= 0 && x < map.length) {
            if (map[x][y + 1] == '.') {
                y = y + 1;
                continue;
            }
            if (x == 0) return false;
            if (map[x - 1][y + 1] == '.') {
                x = x - 1;
                y = y + 1;
                continue;
            }
            if (x == map.length - 1) return false;
            if (map[x + 1][y + 1] == '.') {
                x = x + 1;
                y = y + 1;
                continue;
            }
            map[x][y] = 'o';
            return true;
        }
        return false;
    }

    private static boolean dropSand2(char[][] map, Point origin) {
        int x = origin.x, y = origin.y;
        while (true) {
            if (y == map[0].length - 2) {
                map[x][y] = 'o';
                return true;
            }
            if (map[x][y + 1] == '.') {
                y = y + 1;
                continue;
            }
            if (x == 0) return false;
            if (map[x - 1][y + 1] == '.') {
                x = x - 1;
                y = y + 1;
                continue;
            }
            if (x == map.length - 1) return false;
            if (map[x + 1][y + 1] == '.') {
                x = x + 1;
                y = y + 1;
                continue;
            }
            map[x][y] = 'o';
            return x != origin.x || y != origin.y;
        }
    }

    private static void printMap(char[][] map) {
        for (int y = 0; y < map[0].length; ++y) {
            for (int x = 0; x < map.length; ++x) {
                if (y == 0 && x == 500 - 462) System.out.print('+');
                else if (y == map[0].length - 1 && map[x][y] == '.') System.out.print('#');
                else System.out.print(map[x][y]);
            }
            System.out.println();
        }
    }

    private static void fillMap(char[][] map) {
        for (int i = 0; i < map.length; ++i) {
            for (int j = 0; j < map[0].length; ++j) {
                map[i][j] = '.';
            }
        }
    }

    private static void drawPaths(char[][] map, ArrayList<List<Main.Point>> paths) {
        for (List<Point> path : paths) {
            for (int i = 0; i < path.size() - 1; ++i) {
                Point start = path.get(i), end = path.get(i + 1);
                if (start.x == end.x) {
                    int x = start.x, minY = end.y, maxY = start.y;
                    if (start.y < end.y) {
                        minY = start.y;
                        maxY = end.y;
                    }
                    for (int curY = minY; curY <= maxY; ++curY) {
                        map[x][curY] = '#';
                    }
                } else if (start.y == end.y) {
                    int y = start.y, minX = end.x, maxX = start.x;
                    if (start.x < end.x) {
                        minX = start.x;
                        maxX = end.x;
                    }
                    for (int curX = minX; curX <= maxX; ++curX) {
                        map[curX][y] = '#';
                    }
                } else assert false;
            }
        }
    }

    // minX, maxX, minY, maxY
    private static int[] findLimits(ArrayList<List<Point>> paths) {
        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE, minY = 0, maxY = 0;
        for (List<Point> path : paths) {
            for (Point point : path) {
                if (point.x < minX) minX = point.x;
                if (point.x > maxX) maxX = point.x;
                if (point.y < minY) minY = point.y;
                if (point.y > maxY) maxY = point.y;
            }
        }
        return new int[] {minX, maxX, minY, maxY};
    }

    private static ArrayList<List<Point>> offsetX(ArrayList<List<Point>> paths, int offset) {
        ArrayList<List<Point>> newPaths = new ArrayList<>();
        for (List<Point> path : paths) {
            List<Point> newPath = new ArrayList<>();
            for (Point point : path) {
                newPath.add(new Point(point.x + offset, point.y));
            }
            newPaths.add(newPath);
        }
        return newPaths;
    }
    
    private static List<Point> parsePath(String s) {
        List<Point> result = new ArrayList<>();
        for (String pointString : s.split(" -> ")) {
            result.add(new Point(pointString));
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
