package xyz.kovacs.aoc2021.day01;

import one.util.streamex.StreamEx;

import java.util.OptionalDouble;

import static xyz.kovacs.util.AocUtils.getAllLines;
import static xyz.kovacs.util.AocUtils.getLogger;

public class Day01 {
	
	public static void main(String[] args) {
		doPuzzle2("sample", 1);
		doPuzzle2("input", 1);
		doPuzzle2("sample", 3);
		doPuzzle2("input", 3);
	}
	
	/**
	 * Solution for puzzle 1.
	 */
	@SuppressWarnings("unused")
	public static void doPuzzle1(String inputFile) {
		getLogger(u -> u).info(StreamEx.of(getAllLines(() -> inputFile).stream().map(Integer::valueOf))
		                               .pairMap((current, next) -> next.compareTo(current)).filter(diff -> diff > 0)
		                               .count());
	}
	
	/**
	 * Solution for puzzle 1 (window = 1), and puzzle 2 (window = 3).
	 */
	public static void doPuzzle2(String inputFile, int window) {
		getLogger(u -> u).info("{} (window of {}): {}",
		                       inputFile,
		                       window,
		                       StreamEx.ofSubLists(getAllLines(() -> inputFile).stream()
		                                                                       .map(Integer::valueOf)
		                                                                       .toList(), window, 1)
		                               .map(l -> l.stream()
		                                          .mapToInt(Integer::intValue)
		                                          .average())
		                               .filter(OptionalDouble::isPresent) // prevent warning
		                               .map(OptionalDouble::getAsDouble)
		                               .pairMap((current, next) -> next.compareTo(current))
		                               .filter(diff -> diff > 0).count());
	}
}