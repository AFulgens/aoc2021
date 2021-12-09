package xyz.kovacs.aoc2021.day8;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.plexus.util.CollectionUtils;

import java.util.*;

import static xyz.kovacs.util.AocUtils.getAllLines;
import static xyz.kovacs.util.AocUtils.getLogger;

public class Day8 {
	
	public static void main(String[] args) {
		doPuzzle1("example");
		doPuzzle1("sample");
		doPuzzle1("input");
		doPuzzle2("example");
		doPuzzle2("sample");
		doPuzzle2("input");
	}
	
	/**
	 * Solution for puzzle 1.
	 */
	public static void doPuzzle1(String inputFile) {
		getLogger(u -> u).info(getAllLines(() -> inputFile).stream()
		                                                   .map(line -> StringUtils.substringAfterLast(line, "|"))
		                                                   .flatMap(digits -> Arrays.stream(StringUtils.split(digits, " ")))
		                                                   .map(StringUtils::length)
		                                                   .filter(s -> s == 2 || s == 3 || s == 4 || s == 7)
		                                                   .count());
	}
	
	/**
	 * Solution for puzzle 2. I hate such puzzles.
	 * <p>
	 * I'm sure, that there is a very succinct solution for it, but I'm just too dumb to think of it.
	 * <p>
	 * And yes, I know that this is a tangled mess with a lot of landmines, but this is just a puzzle, nothing to be
	 * maintained in the long run ¯\_(ツ)_/¯
	 */
	public static void doPuzzle2(String inputFile) {
		getLogger(u -> u).info(getAllLines(() -> inputFile).stream().mapToInt(Day8::decode).sum());
	}
	
	public static int decode(String line) {
		String[] split = StringUtils.split(line, "|");
		getLogger(u -> u).debug("Line: {}", Arrays.toString(split));
		List<String> oracle = new ArrayList<>(Arrays.stream(StringUtils.split(split[0], " ")).map(StringUtils::upperCase)
		                                            .toList());
		getLogger(u -> u).debug("Oracle: {}", oracle);
		assert (oracle.size() == 10);
		List<String> challenge = new ArrayList<>(Arrays.stream(StringUtils.split(split[1], " ")).map(StringUtils::upperCase)
		                                               .toList());
		getLogger(u -> u).debug("Challenge: {}", challenge);
		assert (challenge.size() == 4);
		
		Set<Wire> top = EnumSet.allOf(Wire.class);
		Set<Wire> leftTop = EnumSet.allOf(Wire.class);
		Set<Wire> rightTop = EnumSet.allOf(Wire.class);
		Set<Wire> middle = EnumSet.allOf(Wire.class);
		Set<Wire> leftBottom = EnumSet.allOf(Wire.class);
		Set<Wire> rightBottom = EnumSet.allOf(Wire.class);
		Set<Wire> bottom = EnumSet.allOf(Wire.class);
		
		final String one = oracle.stream().filter(o -> o.length() == 2).findFirst()
		                         .orElseThrow(() -> new IllegalStateException("No bueno, I need a 1"));
		oracle.removeIf(one::equals);
		Set<Wire> segmentsOfOne = Wire.wiresOf(one);
		getLogger(u -> u).debug("1 has: {}", segmentsOfOne);
		rightTop.retainAll(segmentsOfOne);
		rightBottom.retainAll(segmentsOfOne);
		
		final String seven = oracle.stream().filter(o -> o.length() == 3).findFirst()
		                           .orElseThrow(() -> new IllegalStateException("No bueno, I need a 7"));
		oracle.removeIf(seven::equals);
		Set<Wire> segmentsOfSeven = Wire.wiresOf(seven);
		getLogger(u -> u).debug("7 has: {}", segmentsOfSeven);
		top.retainAll(segmentsOfSeven);
		top.removeAll(segmentsOfOne); // top is now deciphered
		getLogger(u -> u).debug("=> Top: {}", top);
		assert (top.size() == 1);
		
		final String four = oracle.stream().filter(o -> o.length() == 4).findFirst()
		                          .orElseThrow(() -> new IllegalStateException("No bueno, I need a 4"));
		oracle.removeIf(four::equals);
		Set<Wire> segmentsOfFour = Wire.wiresOf(four);
		getLogger(u -> u).debug("4 has: {}", segmentsOfFour);
		leftTop.retainAll(segmentsOfFour);
		leftTop.removeAll(segmentsOfOne);
		middle.retainAll(segmentsOfFour);
		middle.removeAll(segmentsOfOne);
		leftBottom.removeAll(segmentsOfSeven);
		leftBottom.removeAll(segmentsOfFour);
		bottom.removeAll(segmentsOfSeven);
		bottom.removeAll(segmentsOfFour);
		
		final String eight = oracle.stream().filter(o -> o.length() == 7).findFirst()
		                           .orElseThrow(() -> new IllegalStateException("No bueno, I need an 8"));
		oracle.removeIf(eight::equals);
		Set<Wire> segmentsOfEight = Wire.wiresOf(eight);
		getLogger(u -> u).debug("8 has: {}", segmentsOfEight); // 🤣
		
		final List<String> twoThreeFive = oracle.stream().filter(o -> o.length() == 5).toList();
		oracle.removeIf(twoThreeFive::contains);
		List<Set<Wire>> segmentsOfTwoThreeFive = new ArrayList<>(twoThreeFive.stream().map(Wire::wiresOf).toList());
		segmentsOfTwoThreeFive.forEach(s -> s.removeAll(top));
		Set<Wire> middleOrBottom = segmentsOfTwoThreeFive.stream().reduce(EnumSet.allOf(Wire.class), (a, b) -> {
			a.retainAll(b);
			return a;
		});
		getLogger(u -> u).debug("Middle or bottom: {}", middleOrBottom);
		middle = EnumSet.copyOf(middleOrBottom);
		middle.retainAll(segmentsOfFour); // middle is now deciphered
		getLogger(u -> u).debug("=> Middle: {}", middle);
		assert (middle.size() == 1);
		
		bottom = EnumSet.copyOf(middleOrBottom);
		bottom.removeAll(middle); // bottom is now deciphered
		getLogger(u -> u).debug("=> Bottom: {}", bottom);
		assert (bottom.size() == 1);
		
		final Set<Wire> captureMiddle = middle;
		segmentsOfTwoThreeFive.forEach(s -> s.removeAll(captureMiddle));
		final Set<Wire> captureBottom = bottom;
		segmentsOfTwoThreeFive.forEach(s -> s.removeAll(captureBottom));
		
		Set<Wire> candidate1 = segmentsOfTwoThreeFive.get(0);
		Set<Wire> candidate2 = segmentsOfTwoThreeFive.get(1);
		Set<Wire> candidate3 = segmentsOfTwoThreeFive.get(2);
		Set<Wire> segmentsOfThree;
		if (CollectionUtils.intersection(candidate1, candidate2).isEmpty()) {
			segmentsOfThree = candidate3;
			segmentsOfTwoThreeFive.remove(2);
		} else if (CollectionUtils.intersection(candidate1, candidate3).isEmpty()) {
			segmentsOfThree = candidate2;
			segmentsOfTwoThreeFive.remove(1);
		} else {
			segmentsOfThree = candidate1;
			segmentsOfTwoThreeFive.remove(0);
		}
		getLogger(u -> u).debug("3 has: {}", segmentsOfThree);
		
		leftTop.removeAll(segmentsOfThree);
		leftTop.removeAll(middle);
		leftTop.removeAll(bottom); // left top is now deciphered
		getLogger(u -> u).debug("=> Left-Top: {}", leftTop);
		assert (leftTop.size() == 1);
		
		if (segmentsOfTwoThreeFive.get(0).containsAll(leftTop)) { // 5
			rightBottom.retainAll(segmentsOfTwoThreeFive.get(0));
		} else { // 2
			rightBottom.retainAll(segmentsOfTwoThreeFive.get(1));
		}
		rightBottom.removeAll(top);
		rightBottom.removeAll(middle);
		rightBottom.removeAll(bottom);
		rightBottom.removeAll(leftTop); // right bottom is now deciphered
		getLogger(u -> u).debug("=> Right-Bottom: {}", rightBottom);
		assert (rightBottom.size() == 1);
		
		rightTop.removeAll(rightBottom); // right top is now deciphered
		getLogger(u -> u).debug("=> Right-Top: {}", rightTop);
		assert (rightTop.size() == 1);
		
		leftBottom.removeAll(middle);
		leftBottom.removeAll(bottom); // leftBottom is now deciphered
		getLogger(u -> u).debug("=> Left-Bottom: {}", leftBottom);
		assert (leftBottom.size() == 1);
		
		StringBuilder result = new StringBuilder(4);
		for (String digit : challenge) {
			getLogger(u -> u).debug("Digit: {}", digit);
			if (digit.length() == 2) {
				result.append("1");
			} else if (digit.length() == 3) {
				result.append("7");
			} else if (digit.length() == 4) {
				result.append("4");
			} else if (digit.length() == 7) {
				result.append("8");
			} else if (digit.length() == 5) {
				Set<Wire> wiresOfDigit = Wire.wiresOf(digit);
				if (wiresOfDigit.containsAll(leftBottom)) {
					result.append("2");
				} else if (wiresOfDigit.containsAll(leftTop)) {
					result.append("5");
				} else {
					result.append("3");
				}
			} else if (digit.length() == 6) {
				Set<Wire> wiresOfDigit = Wire.wiresOf(digit);
				if (!wiresOfDigit.containsAll(middle)) {
					result.append("0");
				} else if (wiresOfDigit.containsAll(leftBottom)) {
					result.append("6");
				} else {
					result.append("9");
				}
			}
		}
		
		getLogger(u -> u).debug("-> Result: {}", result);
		return Integer.parseInt(result.toString());
	}
	
	// See: https://youtrack.jetbrains.com/issue/IDEA-228033
	@SuppressWarnings("unused")
	public enum Wire {
		A, B, C, D, E, F, G;
		
		public static Set<Wire> wiresOf(String digit) {
			Set<Wire> wires = EnumSet.noneOf(Wire.class);
			for (char c : digit.toCharArray()) {
				wires.add(Wire.valueOf("" + c));
			}
			return wires;
		}
	}
}
