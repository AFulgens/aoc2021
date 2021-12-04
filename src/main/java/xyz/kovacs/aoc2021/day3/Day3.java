package xyz.kovacs.aoc2021.day3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static xyz.kovacs.util.AocUtils.getAllLines;
import static xyz.kovacs.util.AocUtils.getLogger;

@SuppressWarnings("NonAsciiCharacters")
public class Day3 {
	
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
		List<String> lines = getAllLines(() -> inputFile);
		
		// Yes, yes, this should be done with StringBuilder, but I'm lazy to type
		// https://stackoverflow.com/a/28285243/5471574
		long γ = Long.parseLong(Arrays.stream(getData(lines))
		                              .map(i -> i > lines.size() / 2 ? 1 : 0)
		                              .mapToObj(Integer::toString)
		                              .collect(Collectors.joining()), 2);
		long ε = Long.parseLong(String.format("%" + lines.get(0)
		                                                 .length() + "s", Long.toBinaryString(γ))
		                              .chars()
		                              .map(i -> i == '1' ? 0 : 1)
		                              .mapToObj(Integer::toString)
		                              .collect(Collectors.joining()), 2);
		getLogger(u -> u).info(γ * ε);
	}
	
	/**
	 * Solution for puzzle 2.
	 */
	public static void doPuzzle2(String inputFile) {
		List<String> lines = getAllLines(() -> inputFile);
		
		// Interestingly, ₂ (subscript 2, U+2082) is not a valid Java identifier part, but ᵦ (subscript beta, U+1D66) is 😎
		long oᵦ = getReport(lines, (data, dataPoints) -> data >= dataPoints / 2);
		long coᵦ = getReport(lines, (data, dataPoints) -> data < dataPoints / 2);
		
		getLogger(u -> u).info(oᵦ * coᵦ);
	}
	
	/**
	 * Counting 1's in columns.
	 */
	public static int[] getData(List<String> lines) {
		int l = lines.get(0)
		             .length();
		int[] data = new int[l];
		
		for (String line : lines) {
			for (int i = 0; i < l; ++i) {
				data[i] += line.charAt(i) - '0';
			}
		}
		
		return data;
	}
	
	/**
	 * Yes, this is O(n²) in the worst case, you get a brownie point.
	 */
	public static long getReport(List<String> lines, BiFunction<Integer, Integer, Boolean> removeZeroesIf) {
		List<String> linesForReport = new ArrayList<>(lines);
		int l = linesForReport.get(0)
		                      .length();
		
		for (int i = 0; i < l; ++i) {
			int[] data = getData(linesForReport);
			
			int ii = i; // capture 'i' as effectively final
			if (removeZeroesIf.apply(data[i], linesForReport.size() + 1)) {
				linesForReport.removeIf(line -> line.charAt(ii) == '0');
			} else {
				linesForReport.removeIf(line -> line.charAt(ii) == '1');
			}
			
			if (linesForReport.size() == 1) {
				return Long.parseLong(linesForReport.get(0), 2);
			}
		}
		
		throw new IllegalStateException("No bueno, no value could be determined 😕");
	}
	
}