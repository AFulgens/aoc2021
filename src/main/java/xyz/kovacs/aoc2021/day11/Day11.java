package xyz.kovacs.aoc2021.day11;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;

import static xyz.kovacs.util.AocUtils.*;

@SuppressWarnings("NonAsciiCharacters")
public class Day11 {
	
	public static void main(String[] args) {
		doPuzzle("sample", 100);
		doPuzzle("input", 100);
		doPuzzle("sample", Integer.MAX_VALUE);
		doPuzzle("input", Integer.MAX_VALUE);
	}
	
	/**
	 * Solution for puzzles 1 (rounds ≔ 100) and 2 (rounds > ∀ first synchronized flashes). Another graph-based one, eh?
	 */
	public static void doPuzzle(String inputFile, int rounds) {
		int[][] map = getMap(() -> inputFile);
		
		long flashes = 0L;
		int n = 0;
		for (; n < rounds; ++n) {
			// Yes, this will work, but only because the order of resolution is well-defined in Java
			flashes += discharge(increaseEnergyLevelsByOne(map), chargedOctopuses(map));
			
			if (smoothen(map)) {
				getLogger(u -> u).info("First synchronized flash at step {}", ++n);
				break;
			}
			
			logMap(map, getLogger(u -> u), Level.DEBUG);
		}
		
		getLogger(u -> u).info("Sum of flashes in {} steps: {}", n, flashes);
	}
	
	/**
	 * Increase each octopuses' energy-level by 1. This constitutes sub-step 1.
	 * <p>
	 * <b>Warning:</b> This method has a return value (the changed map) as well as side effects (changes the map).
	 */
	private static int[][] increaseEnergyLevelsByOne(int[][] map) {
		for (int xᵢ = 0; xᵢ < map.length; ++xᵢ) {
			for (int yᵢ = 0; yᵢ < map[xᵢ].length; ++yᵢ) {
				map[xᵢ][yᵢ]++;
			}
		}
		return map;
	}
	
	/**
	 * Gather all octopuses with energy-level above 9, i.e., who will flash this round. This is the first part of sub-step
	 * 2.
	 */
	private static List<Pair<Integer, Integer>> chargedOctopuses(int[][] map) {
		List<Pair<Integer, Integer>> queue = new ArrayList<>(map.length * map[0].length);
		for (int xᵢ = 0; xᵢ < map.length; ++xᵢ) {
			for (int yᵢ = 0; yᵢ < map[xᵢ].length; ++yᵢ) {
				if (map[xᵢ][yᵢ] > 9) {
					queue.add(Pair.of(xᵢ, yᵢ));
				}
			}
		}
		return queue;
	}
	
	/**
	 * All octopuses above 9 flash and increase their neighbour's energy-level by 1.
	 * <p>
	 * This propagates through the whole map, with the additional constraint, that in a single round, every octopus can
	 * flash at most once.
	 * <p>
	 * <b>Warning:</b> This method has a return value (sum of all flashes observed in this step) as well as side effects
	 * (changes the map and empties the queue).
	 * <p>
	 * This is the second part of sub-step 2.
	 */
	private static long discharge(int[][] map, List<Pair<Integer, Integer>> queue) {
		long flashes = 0L;
		while (!queue.isEmpty()) {
			Pair<Integer, Integer> node = queue.remove(0);
			int xₒ = node.getLeft();
			int yₒ = node.getRight();
			if (map[xₒ][yₒ] < 0) { // already flashed this round
				continue;
			}
			map[xₒ][yₒ] = Integer.MIN_VALUE;
			++flashes;
			
			for (int xʹ = xₒ - 1; xʹ <= xₒ + 1; ++xʹ) {
				for (int yʹ = yₒ -1; yʹ <= yₒ + 1; ++yʹ) {
					if (xʹ < 0 || xʹ >= map.length || yʹ < 0 || yʹ >= map[xʹ].length) { // do not leave the map
						continue;
					}
					
					if (++map[xʹ][yʹ] > 9) { // yes, this will add 1 for the flashed octopus too, but we will never reach 0, so...
						queue.add(Pair.of(xʹ, yʹ));
					}
				}
			}
		}
		
		return flashes;
	}
	
	/**
	 * "Smoothens" the map. I.e., all octopuses that flashed this round are returned to energy-level 0. This constitutes
	 * sub-step 3.
	 * <p>
	 * <b>Warning:</b> This method has a return value (whether this step produced a synchronized flash) as well as side
	 * effects (changes the map).
	 */
	private static boolean smoothen(int[][] map) {
		boolean synchronizedFlash = true;
		for (int xᵢ = 0; xᵢ < map.length; ++xᵢ) {
			for (int yᵢ = 0; yᵢ < map[xᵢ].length; ++yᵢ) {
				// &= is an eager-operator in Java, i.e., yes, this will change all the values in the map
				synchronizedFlash &= (map[xᵢ][yᵢ] = Math.max(0, map[xᵢ][yᵢ])) == 0;
			}
		}
		return synchronizedFlash;
	}
}