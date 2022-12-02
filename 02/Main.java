import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Main {
	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("ERROR: Please provide input file path in arguments!");
			System.exit(1);
		}
		String filePath = args[0];
		System.out.println(part2(filePath));
	}
	public static int part1(String filePath) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String line = reader.readLine();
			int opponentChoice = 0, myChoice = 0;
			int score = 0;
			while (line != null){
				String[] choices = line.split(" ");
				switch (choices[0]) { // opponent's choice
					case "A": // rock
						opponentChoice = 1;
						break;
					case "B": // paper
						opponentChoice = 2;
						break;
					case "C": // scissors
						opponentChoice = 3;
						break;
				}
				switch (choices[1]) { // my choice
					case "X": // rock
						myChoice = 1;
						break;
					case "Y": // paper
						myChoice = 2;
						break;
					case "Z": // scissors
						myChoice = 3;
						break;
				}
				score += myChoice;
				if (myChoice == opponentChoice)
					score += 3;
				else if(   ((myChoice == 1) && (opponentChoice == 3))
					|| ((myChoice == 2) && (opponentChoice == 1))
					|| ((myChoice == 3) && (opponentChoice == 2)))
					score += 6;
				line = reader.readLine();
			}
			return score;
		} catch (IOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
		return -1;
	}
	public static int part2(String filePath) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String line = reader.readLine();
			int opponentChoice = 0, myChoice = 0;
			int score = 0;
			while (line != null){
				String[] choices = line.split(" ");
				switch (choices[0]) { // opponent's choice
					case "A": // rock
						opponentChoice = 1;
						break;
					case "B": // paper
						opponentChoice = 2;
						break;
					case "C": // scissors
						opponentChoice = 3;
						break;
				}
				switch (choices[1]) { // my choice
					case "X": // lose
						switch (opponentChoice) {
							case 1:
								myChoice = 3;
								break;
							case 2:
								myChoice = 1;
								break;
							case 3:
								myChoice = 2;
								break;
						}
						break;
					case "Y": // draw
						myChoice = opponentChoice;
						break;
					case "Z": // win
						switch (opponentChoice) {
							case 1:
								myChoice = 2;
								break;
							case 2:
								myChoice = 3;
								break;
							case 3:
								myChoice = 1;
								break;
						}
						break;
				}
				score += myChoice;
				if (myChoice == opponentChoice)
					score += 3;
				else if(   ((myChoice == 1) && (opponentChoice == 3))
					|| ((myChoice == 2) && (opponentChoice == 1))
					|| ((myChoice == 3) && (opponentChoice == 2)))
					score += 6;
				line = reader.readLine();
			}
			return score;
		} catch (IOException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
		return -1;
	}
}
