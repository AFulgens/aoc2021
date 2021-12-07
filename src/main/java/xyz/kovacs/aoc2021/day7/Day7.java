package xyz.kovacs.aoc2021.day7;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import static xyz.kovacs.util.AocUtils.getAllLines;
import static xyz.kovacs.util.AocUtils.getLogger;

public class Day7 {
	
	public static void main(String[] args) {
		doPuzzle("sample", (a, b) -> Math.abs(a - b));
		doPuzzle("input", (a, b) -> Math.abs(a - b));
		doPuzzle("sample", (a, b) -> Math.abs(a - b) * (Math.abs(a - b) + 1) / 2);
		doPuzzle("input", (a, b) -> Math.abs(a - b) * (Math.abs(a - b) + 1) / 2);
	}
	
	/**
	 * Solution for puzzles 1 & 2.
	 * <p>
	 * I'm sure that puzzle 1 is somehow solvable with a statistical calculation (like a regression or such), statistics
	 * was, however, never my strong suite, so let's just brute-force it.
	 * <p>
	 * (And I'm almost sure that the second puzzle has to be brute-forced anyway.)
	 */
	public static void doPuzzle(String inputFile, BiFunction<Integer, Integer, Integer> diff) {
		List<Integer> input = Arrays.stream(getAllLines(() -> inputFile).get(0).split(",")).map(Integer::valueOf).toList();
		
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		for (Integer i : input) {
			min = Math.min(min, i);
			max = Math.max(max, i);
		}
		
		// traditional Java
		// int sum = Integer.MAX_VALUE;
		// for (int middle = min; middle < max; ++middle) {
		// 	int current = 0;
		//	for (Integer i : input) {
		//		current += diff.apply(i, middle);
		//	}
		//
		//  sum = Math.min(sum, current);
		// }
		
		// functional Java
		int sum = IntStream.range(min, max)
		                   .map(middle -> input.stream()
		                                       .mapToInt(i -> diff.apply(i, middle))
		                                       .sum())
		                   .min()
		                   .orElseThrow(() -> new IllegalStateException("No bueno, could not determine min"));
		
		getLogger(u -> u).info(sum);
	}
}