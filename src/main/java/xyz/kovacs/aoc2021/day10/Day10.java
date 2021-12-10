package xyz.kovacs.aoc2021.day10;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import static xyz.kovacs.util.AocUtils.getAllLines;
import static xyz.kovacs.util.AocUtils.getLogger;

public class Day10 {
	
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
		getLogger(u -> u).info(getAllLines(() -> inputFile).stream().mapToInt(Day10::getSyntaxScore).sum());
	}
	
	/**
	 * Solution for puzzle 2.
	 */
	public static void doPuzzle2(String inputFile) {
		long[] scores = getAllLines(() -> inputFile).stream().filter(line -> getSyntaxScore(line) == 0)
		                                            .mapToLong(Day10::getCompletionScore).sorted().toArray();
		getLogger(u -> u).debug(Arrays.toString(scores));
		getLogger(u -> u).info(scores[scores.length / 2]);
	}
	
	public static int getSyntaxScore(String line) {
		Deque<Character> stack = new ArrayDeque<>(line.length());
		
		getLogger(u -> u).debug("Line: {}", line);
		for (char c : line.toCharArray()) {
			int points = switch (c) {
				case '(', '[', '{', '<' -> {
					stack.push(c);
					yield 0;
				}
				case ')' -> stack.pop() == '(' ? 0 : 3;
				case ']' -> stack.pop() == '[' ? 0 : 57;
				case '}' -> stack.pop() == '{' ? 0 : 1197;
				case '>' -> stack.pop() == '<' ? 0 : 25137;
				default -> throw new IllegalArgumentException("No bueno, I don't know about '" + c + "'");
			};
			getLogger(u -> u).debug("Char was {}, point is {}", c, points);
			
			if (points != 0) {
				return points;
			}
		}
		
		return 0;
	}
	
	public static long getCompletionScore(String line) {
		Deque<Character> stack = new ArrayDeque<>(line.length());
		
		getLogger(u -> u).debug("Line: {}", line);
		for (char c : line.toCharArray()) {
			switch (c) {
				case '(', '[', '{', '<' -> stack.push(c);
				case ')', ']', '}', '>' -> stack.pop();
			}
		}
		
		long points = 0;
		do {
			char c = stack.pop();
			points = points * 5 + switch (c) {
				case '(' -> 1;
				case '[' -> 2;
				case '{' -> 3;
				case '<' -> 4;
				default -> throw new IllegalArgumentException("No bueno, I don't know about '" + c + "'");
			};
			getLogger(u -> u).debug("Char was {}, point is {}", c, points);
		} while (!stack.isEmpty());
		
		return points;
	}
	
}
