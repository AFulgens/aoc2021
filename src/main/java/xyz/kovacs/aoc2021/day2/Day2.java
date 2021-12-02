package xyz.kovacs.aoc2021.day2;

import static xyz.kovacs.aoc2021.util.AocUtils.getAllLines;
import static xyz.kovacs.aoc2021.util.AocUtils.getLogger;

public class Day2 {
	
	public static void main(String[] args) {
		doPuzzle1("sample");
		doPuzzle1("input");
		doPuzzle2("sample");
		doPuzzle2("input");
	}
	
	/**
	 * Solution for puzzle 1.
	 */
	public static void doPuzzle1(String inputFile) {
		long forward = 0L;
		long depth = 0L;
		
		for (String line : getAllLines(() -> inputFile)) {
			String[] split = line.split(" ");
			int value = Integer.parseInt(split[1]);
			switch (split[0]) {
				case "forward" -> forward += value;
				case "down" -> depth += value;
				case "up" -> {
					depth -= value;
					depth = Math.max(0L, depth); // the puzzle doesn't mention this, but a submarine cannot fly, that's the Helicarrier.
				}
				default -> throw new IllegalArgumentException("No bueno, I don't know about " + split[0]);
			}
		}
		getLogger(u -> u).info(forward * depth);
	}
	
	/**
	 *
	 */
	public static void doPuzzle2(String inputFile) {
		long forward = 0L;
		long aim = 0L;
		long depth = 0L;
		
		for (String line : getAllLines(() -> inputFile)) {
			String[] split = line.split(" ");
			int value = Integer.parseInt(split[1]);
			switch (split[0]) {
				case "forward" -> {
					forward += value;
					depth += aim * value;
					depth = Math.max(0L, depth); // the puzzle doesn't mention this, but a submarine cannot fly, that's the Helicarrier.
				}
				case "down" -> aim += value;
				case "up" -> aim -= value;
				default -> throw new IllegalArgumentException("No bueno, I don't know about " + split[0]);
			}
		}
		getLogger(u -> u).info(forward * depth);
	}
}