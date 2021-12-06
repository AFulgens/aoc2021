package xyz.kovacs.aoc2021.day6;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static xyz.kovacs.util.AocUtils.getAllLines;
import static xyz.kovacs.util.AocUtils.getLogger;

public class Day6 {
	
	public static void main(String[] args) {
		doPuzzle("sample", 80);
		doPuzzle("input", 80);
		doPuzzle("sample", 256);
		doPuzzle("input", 256);
	}
	
	/**
	 * Solution for puzzle 1 (days = 80) and puzzle 2 (days = 256).
	 */
	public static void doPuzzle(String inputFile, int days) {
		Map<Integer, Long> input = Arrays.stream(getAllLines(() -> inputFile).get(0).split(",")).map(Integer::valueOf)
		                                 .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		
		// get initial state
		long[] clusters = new long[9];
		for (Map.Entry<Integer, Long> entry : input.entrySet()) { // unbox once, shame on you, unbox twice, shame on me.
			clusters[entry.getKey()] = entry.getValue();
		}
		
		for (int day = 0; day < days; ++day) {
			// keep track of newborns
			long newborns = clusters[0];
			
			// every non-newborn is ticked down by 1
			// for (int i = 0; i < 8; ++i) {
			// 	clusters[i] = clusters[i + 1];
			// }
			// Thanks IntelliJ 😎
			//noinspection SuspiciousSystemArraycopy
			System.arraycopy(clusters, 1, clusters, 0, 8);
			
			// those who just gave birth are back to 6
			clusters[6] += newborns;
			
			// and we have the newborns
			clusters[8] = newborns;
			
			getLogger(u -> u).debug("Day {}: {} fish", day, Arrays.stream(clusters).sum());
		}
		
		getLogger(u -> u).info("Day {}: {} fish", days, Arrays.stream(clusters).sum());
	}
}