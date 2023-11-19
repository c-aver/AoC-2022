import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    private static int minX;
    private static int maxX;
    private static int minY;
    private static int maxY;

    record Point(int x, int y) {
        public Point(String s) {
            this(Integer.parseInt(s.split(", y=")[0]), Integer.parseInt(s.split(", y=")[1]));
        }
        public Point(Point p) {
            this(p.x, p.y);
        }
        public long tuningFrequency() {
            return x*4000000 + y;
        }
    }
    record Pair(Point sensor, Point beacon) {
        int manDist() {
            return Main.manDist(this);
        }
    }

    private static int manDist(Point p1, Point p2) {
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }
    public static int manDist(Pair p) {
        return manDist(p.sensor, p.beacon);
    }

    public static void main(String[] args) {
        String filePath;
        int requiredY = 10;
        if (args.length < 2) {
            filePath = "./15/input.txt";
            minX = minY = 0;
            maxX = maxY = 4000000;
        } else {
            filePath = args[0];
            minX = minY = 0;
            requiredY = maxX = maxY = Integer.parseInt(args[1]);
        }
        
        ArrayList<String> lines = readLinesFromFile(filePath);

        ArrayList<Pair> pairs = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("Sensor at x=|: closest beacon is at x=");
            Point sensor = new Point(parts[1]);
            Point beacon = new Point(parts[2]);
            pairs.add(new Pair(sensor, beacon));
        }
        
        System.out.println("Part 1: " + part1(pairs, requiredY));
        System.out.println("Part 2: " + part2(pairs));
    }

    private static int part1(ArrayList<Pair> pairs, int requiredY) {
        int result = 0;
        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
        
        for (Pair pair : pairs) {
            int pMinX = pair.sensor.x - pair.manDist(); 
            if (pMinX < minX) minX = pMinX;
            int pMaxX = pair.sensor.x + pair.manDist(); 
            if (pMaxX > maxX) maxX = pMaxX;
        }
        for (int x = minX; x <= maxX; ++x) {
            Point p = new Point(x, requiredY);
            if (isPossibleBeaconLocation(pairs, p)) result += 1;
        }
        return result;
    }

//     1    1    2    2
//     0    5    0    5    0    5
// -3 ..........o.................
// -2 .........o#o................
// -1 ........o###o...............
//  0 ....S..o#####o..............
//  1 ......o#######o.......S.....
//  2 .....o#########S............
//  3 ....o###########SB..........
//  4 ...o#############o..........
//  5 ..o###############o.........
//  6 .o#################o........
//  7 o#########S#######S#o.......
//  8 .o#################o........
//  9 ..o###############o.........
// 10 ...oB############o..........
// 11 ..S.o###########o...........
// 12 .....o#########o............
// 13 ......o#######o.............
// 14 .......o#####oS.......S.....
// 15 B.......o###o...............
// 16 .........o#SB...............
// 17 ..........o.....S..........B
// 18 ....S.......................
// 19 ............................
// 20 ............S......S........
// 21 ............................
// 22 .......................B....

    private static long part2(ArrayList<Pair> pairs) {
        for (Pair p : pairs) {
            int dist = p.manDist() + 1;
            Point sensor = p.sensor;
            for (int i = 0; i <= dist; ++i) {   // top left
                Point loc = new Point(sensor.x - i, sensor.y - dist + i);
                if (isPossibleBeaconLocation(pairs, loc)) {
                    isPossibleBeaconLocation(pairs, loc);
                    return loc.tuningFrequency();
                }
            }
            for (int i = 0; i <= dist; ++i) {   // bottom left
                Point loc = new Point(sensor.x - dist + i, sensor.y + i);
                if (isPossibleBeaconLocation(pairs, loc)) {
                    isPossibleBeaconLocation(pairs, loc);
                    return loc.tuningFrequency();
                }
            }
            for (int i = 0; i <= dist; ++i) {   // bottom right
                Point loc = new Point(sensor.x + i, sensor.y + dist - i);
                if (isPossibleBeaconLocation(pairs, loc)) {
                    isPossibleBeaconLocation(pairs, loc);
                    return loc.tuningFrequency();
                }
            }
            for (int i = 0; i <= dist; ++i) {   // top right
                Point loc = new Point(sensor.x + dist - i, sensor.y - i);
                if (isPossibleBeaconLocation(pairs, loc)) {
                    isPossibleBeaconLocation(pairs, loc);
                    return loc.tuningFrequency();
                }
            }
        }

        return -1;
    }

    private static boolean isPossibleBeaconLocation(ArrayList<Pair> pairs, Point loc) {
        if (loc.x < minX || loc.x > maxX || loc.y < minY || loc.y > maxY
            || pairs.stream().map(Pair::beacon).anyMatch(beacon -> beacon.equals(loc)))
            return false;
        boolean marked = false;
        // if (loc.tuningFrequency() == 2001151616) {
        //     for (Pair pair : pairs) {
        //         System.out.println(manDist(pair.sensor, loc) + " <= " + pair.manDist() + ": " + (manDist(loc, pair.sensor) <= pair.manDist()));
        //     }
        // }
        for (int i = 0; i < pairs.size(); ++i) {
            Pair pair = pairs.get(i);
            if (manDist(loc, pair.sensor) <= pair.manDist()) {
                marked = true;
            }
        }
        return !marked;
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
