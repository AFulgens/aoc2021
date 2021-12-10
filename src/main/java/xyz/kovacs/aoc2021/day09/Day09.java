package xyz.kovacs.aoc2021.day09;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static xyz.kovacs.util.AocUtils.getAllLines;
import static xyz.kovacs.util.AocUtils.getLogger;

@SuppressWarnings("NonAsciiCharacters")
public class Day09 {
	
	public static void main(String[] args) {
		doPuzzle1("sample");
		doPuzzle1("input");
		doPuzzle2("sample");
		doPuzzle2("input");
	}
	
	/**
	 * Solution for puzzle 1. Why so many coördinate-based puzzles, I dislike these 😭
	 */
	public static void doPuzzle1(String inputFile) {
		int[][] map = getMap(getAllLines(() -> inputFile));
		
		int dangerZone = 0; // https://tenor.com/view/archer-coroca-dangerzone-gif-18530024
		for (int xₒ = 0; xₒ < map.length; ++xₒ) { // for all rows
			cell:
			for (int yₒ = 0; yₒ < map[xₒ].length; ++yₒ) { // for all columns (i.e. cells)
				for (int ḥ = -1; ḥ <= 1; ++ḥ) { // for all horizontally adjacent cells
					for (int ṿ = -1; ṿ <= 1; ++ṿ) { // for all vertically adjacent cells
						if (ḥ == 0 && ṿ == 0) { // but not for the cell itself
							continue;
						}
						int xʹ = xₒ + ḥ;
						int yʹ = yₒ + ṿ;
						if (xʹ < 0 || xʹ >= map.length || yʹ < 0 || yʹ >= map[xₒ].length) { // but not outside the map
							continue;
						}
						if (map[xₒ][yₒ] >= map[xʹ][yʹ]) {
							continue cell; // this cell is not a sink, continue with next cell
						}
					}
				}
				// this cell is a sink
				dangerZone += map[xₒ][yₒ] + 1;
			}
		}
		
		getLogger(u -> u).info(dangerZone);
	}
	
	/**
	 * Solution for puzzle 2. I didn't write graph-walkers since the university. You have been warned.
	 */
	public static void doPuzzle2(String inputFile) {
		int[][] map = getMap(getAllLines(() -> inputFile));
		
		List<Integer> basinSizes = new ArrayList<>();
		for (int xₙ = 0; xₙ < map.length; ++xₙ) {
			for (int yₙ = 0; yₙ < map[xₙ].length; ++yₙ) {
				basinSizes.add(getBasinSize(map, xₙ, yₙ));
			}
		}
		getLogger(u -> u).debug("Basin sizes: {}", basinSizes.stream().filter(i -> i > 0).toList());
		getLogger(u -> u).debug("There are {} basins in total", basinSizes.stream().filter(i -> i > 0).count());
		getLogger(u -> u).debug("The 10 biggest basins are: {}", basinSizes.stream().filter(i -> i > 0)
		                                                                   .sorted(Collections.reverseOrder()).limit(10)
		                                                                   .toList());
		
		getLogger(u -> u).info(basinSizes.stream().sorted(Collections.reverseOrder()).mapToInt(i -> i).limit(3)
		                                 .reduce(1, (a, b) -> a * b));
	}
	
	/**
	 * Yes, this is a breadth-first search copied from the pseudocode on Wikipedia.
	 */
	public static int getBasinSize(int[][] map, int xₙ, int yₙ) {
		if (!isBasinPart(map[xₙ][yₙ])) {
			return -1;
		}
		
		List<Pair<Integer, Integer>> basin = new ArrayList<>();
		List<Pair<Integer, Integer>> queue = new ArrayList<>();
		queue.add(Pair.of(xₙ, yₙ));
		
		while (!queue.isEmpty()) {
			Pair<Integer, Integer> node = queue.remove(0);
			int xₒ = node.getLeft();
			int yₒ = node.getRight();
			if (!isBasinPart(map[xₒ][yₒ])) {
				continue;
			}
			map[xₒ][yₒ] = -1;
			basin.add(node);
			
			for (int ḥ = -1; ḥ <= 1; ++ḥ) {
				for (int ṿ = -1; ṿ <= 1; ++ṿ) {
					if (Math.abs(ḥ) == Math.abs(ṿ)) { // basins do not continue on the diagonal
						continue;
					}
					int xʹ = xₒ + ḥ;
					int yʹ = yₒ + ṿ;
					if (xʹ < 0 || xʹ >= map.length || yʹ < 0 || yʹ >= map[xₙ].length) { // do not check outside the map
						continue;
					}
					if (isBasinPart(map[xʹ][yʹ])) {
						queue.add(Pair.of(xʹ, yʹ));
					}
				}
			}
		}
		
		getLogger(u -> u).debug("Basin of size {} explored, coördinates are: {}", basin.size(), basin);
		return basin.size();
	}
	
	public static int[][] getMap(List<String> input) {
		int x = input.size();
		int y = input.get(0).length();
		int[][] map = new int[x][y];
		for (int xₙ = 0; xₙ < x; ++xₙ) {
			String line = input.get(xₙ);
			for (int yₙ = 0; yₙ < y; ++yₙ) {
				map[xₙ][yₙ] = line.charAt(yₙ) - '0';
			}
		}
		return map;
	}
	
	public static boolean isBasinPart(int height) {
		return height != 9 && height >= 0;
	}
	
}