package xyz.kovacs.aoc2021.day9;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static xyz.kovacs.util.AocUtils.getAllLines;
import static xyz.kovacs.util.AocUtils.getLogger;

public class Day9 {
	
	public static void main(String[] args) {
		doPuzzle1("sample");
		doPuzzle1("input");
		doPuzzle2("sample");
		doPuzzle2("input");
	}
	
	/**
	 * Solution for puzzle 1. Why so many coördinate based puzzle, I dislike these 😭
	 */
	public static void doPuzzle1(String inputFile) {
		List<String> input = getAllLines(() -> inputFile);
		
		int x = input.size();
		int y = input.get(0).length();
		int[][] map = new int[x][y];
		for (int i = 0; i < x; ++i) {
			String line = input.get(i);
			for (int j = 0; j < y; ++j) {
				map[i][j] = line.charAt(j) - '0';
			}
		}
		
		int dangerZone = 0; // https://tenor.com/view/archer-coroca-dangerzone-gif-18530024
		// "proper" solution
		/* for (int i = 0; i < x; ++i) {
			for (int j = 0; j < y; ++j) {
				int curr = map[i][j];
				if (i > 0 && i < x - 1) {
					if (j > 0 && j < y - 1) { // non-edge
						if (curr < map[i - 1][j - 1] && curr < map[i - 1][j] && curr < map[i - 1][j + 1] && curr < map[i][j - 1] && curr < map[i][j + 1] && curr < map[i + 1][j - 1] && curr < map[i + 1][j] && curr < map[i + 1][j + 1]) {
							dangerZone += curr + 1;
						}
					} else if (j == 0) { // top-edge, non-corner
						if (curr < map[i - 1][j] && curr < map[i - 1][j + 1] && curr < map[i][j + 1] && curr < map[i + 1][j] && curr < map[i + 1][j + 1]) {
							dangerZone += curr + 1;
						}
					} else if (j == y - 1) { // bottom-edge, non-corner
						if (curr < map[i - 1][j - 1] && curr < map[i - 1][j] && curr < map[i][j - 1] && curr < map[i + 1][j - 1] && curr < map[i + 1][j]) {
							dangerZone += curr + 1;
						}
					}
				} else if (i == 0) {
					if (j == 0) { // top-left corner
						if (curr < map[i][j + 1] && curr < map[i + 1][j] && curr < map[i + 1][j + 1]) {
							dangerZone += curr + 1;
						}
					} else if (j == y - 1) { // bottom-left corner
						if (curr < map[i][j - 1] && curr < map[i + 1][j - 1] && curr < map[i + 1][j]) {
							dangerZone += curr + 1;
						}
					} else { // left-edge, non-corner
						if (curr < map[i][j - 1] && curr < map[i][j + 1] && curr < map[i + 1][j - 1] && curr < map[i + 1][j] && curr < map[i + 1][j + 1]) {
							dangerZone += curr + 1;
						}
					}
				} else if (i == x - 1) {
					if (j == 0) { // top-right corner
						if (curr < map[i - 1][j] && curr < map[i + 1][j - 1] && curr < map[i + 1][j + 1]) {
							dangerZone += curr + 1;
						}
					} else if (j == y - 1) { // bottom-right corner
						if (curr < map[i - 1][j - 1] && curr < map[i - 1][j] && curr < map[i][j - 1]) {
							dangerZone += curr + 1;
						}
					} else { // right-edge, non-corner
						if (curr < map[i - 1][j - 1] && curr < map[i - 1][j] && curr < map[i - 1][j + 1] && curr < map[i][j - 1] && curr < map[i][j + 1]) {
							dangerZone += curr + 1;
						}
					}
				} else {
					throw new IllegalStateException("¯\\_(ツ)_/¯");
				}
			}
		} */
		
		// "readable" solution
		for (int i = 0; i < x; ++i) { // for all rows
			for (int j = 0; j < y; ++j) { // for all column (i.e. cells)
				boolean isSink = true;
				for (int h = -1; h <= 1; ++h) { // for all horizontally adjacent cells
					for (int v = -1; v <= 1; ++v) { // for all vertically adjacent cells
						if (h == 0 && v == 0) { // but not for the cell itself
							continue;
						}
						try {
							if (map[i][j] >= map[i + h][j + v]) {
								isSink = false;
							}
						} catch (ArrayIndexOutOfBoundsException e) {
							// it's an edge or a corner, so what?
						}
					}
				}
				dangerZone += isSink ? map[i][j] + 1 : 0;
			}
		}
		
		getLogger(u -> u).info(dangerZone);
	}
	
	/**
	 * Solution for puzzle 2. I didn't write graph-walkers since the university. You have been warned.
	 */
	public static void doPuzzle2(String inputFile) {
		List<String> input = getAllLines(() -> inputFile);
		
		int x = input.size();
		int y = input.get(0).length();
		int[][] map = new int[x][y];
		for (int i = 0; i < x; ++i) {
			String line = input.get(i);
			for (int j = 0; j < y; ++j) {
				map[i][j] = line.charAt(j) - '0';
			}
		}
		
		List<Integer> basinSizes = new ArrayList<>();
		for (int i = 0; i < x; ++i) {
			for (int j = 0; j < y; ++j) {
				basinSizes.add(getBasinSize(map, i, j));
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
	public static int getBasinSize(int[][] map, int i, int j) {
		if (!isBasinPart(map[i][j])) {
			return -1;
		}
		
		List<Pair<Integer, Integer>> basin = new ArrayList<>();
		List<Pair<Integer, Integer>> q = new ArrayList<>();
		q.add(Pair.of(i, j));
		
		while (!q.isEmpty()) {
			Pair<Integer, Integer> v = q.remove(0);
			if (!isBasinPart(map[v.getLeft()][v.getRight()])) {
				continue;
			}
			map[v.getLeft()][v.getRight()] = -1;
			basin.add(v);
			
			for (int x = -1; x <= 1; ++x) {
				for (int y = -1; y <= 1; ++y) {
					if (Math.abs(x) == Math.abs(y)) { // basins do not continue on the diagonal
						continue;
					}
					try {
						if (isBasinPart(map[v.getLeft() + x][v.getRight() + y])) {
							q.add(Pair.of(v.getLeft() + x, v.getRight() + y));
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						// I don't care that this is an anti-pattern, sue me
					}
				}
			}
		}
		
		getLogger(u -> u).debug("Basin of size {} explored, coördinates are: {}", basin.size(), basin);
		return basin.size();
	}
	
	public static boolean isBasinPart(int height) {
		return height != 9 && height >= 0;
	}
	
}