package xyz.kovacs.aoc2021.day5;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static xyz.kovacs.util.AocUtils.getAllLines;
import static xyz.kovacs.util.AocUtils.getLogger;

public class Day5 {
	
	public static void main(String[] args) {
		doPuzzle("sample", false);
		doPuzzle("input", false);
		doPuzzle("sample", true);
		doPuzzle("input", true);
	}
	
	/**
	 * Solution for puzzles 1 (considerDiagonals == false) and 2 (considerDiagonals == true)
	 */
	public static void doPuzzle(String inputFile, boolean considerDiagonals) {
		List<Pair<Point, Point>> endpoints = new ArrayList<>();
		
		for (String line : getAllLines(() -> inputFile)) {
			String[] split = StringUtils.split(line, " -> ");
			endpoints.add(Pair.of(getPoint(split[0]), getPoint(split[1])));
		}
		
		int dimension = endpoints.stream().map(pair -> Math.max(Math.max(pair.getLeft().getX(), pair.getLeft()
		                                                                                            .getY()), Math.max(pair.getRight()
		                                                                                                                   .getX(), pair.getRight()
		                                                                                                                                .getY())))
		                         .mapToInt(Double::intValue).max()
		                         .orElseThrow(() -> new IllegalStateException("No bueno, could not determine max"));
		++dimension;
		
		int[][] map = new int[dimension][dimension];
		for (Pair<Point, Point> line : endpoints) {
			if (line.getLeft().getX() == line.getRight().getX()) { // → or ←
				Point left = line.getLeft().getY() < line.getRight().getY() ? line.getLeft() : line.getRight();
				Point right = line.getLeft().getY() < line.getRight().getY() ? line.getRight() : line.getLeft();
				
				for (int i = (int) left.getY(); i <= (int) right.getY(); ++i) {
					map[i][(int) left.getX()]++;
				}
			} else if (line.getLeft().getY() == line.getRight().getY()) { // ↓ or ↑
				Point top = line.getLeft().getX() < line.getRight().getX() ? line.getLeft() : line.getRight();
				Point bottom = line.getLeft().getX() < line.getRight().getX() ? line.getRight() : line.getLeft();
				
				for (int i = (int) top.getX(); i <= (int) bottom.getX(); ++i) {
					map[(int) top.getY()][i]++;
				}
			} else if (considerDiagonals) { // thankfully the puzzle says that we can only have horizontal/vertical/diagonal, nothing else
				Point top = line.getLeft().getX() < line.getRight().getX() ? line.getLeft() : line.getRight();
				Point bottom = line.getLeft().getX() < line.getRight().getX() ? line.getRight() : line.getLeft();
				
				int counter = 0;
				if (top.getY() < bottom.getY()) { // ↘️
					for (int i = (int) top.getX(); i <= (int) bottom.getX(); ++i) {
						map[(int) top.getY() + counter++][i]++;
					}
				} else { // ↙️
					for (int i = (int) top.getX(); i <= (int) bottom.getX(); ++i) {
						map[(int) top.getY() - counter++][i]++;
					}
				}
			}
			
			if (getLogger(u -> u).isDebugEnabled()) {
				getLogger(u -> u).debug("Line: {} -> {}", line.getLeft(), line.getRight());
				getLogger(u -> u).debug("Map: ");
				for (int i = 0; i < dimension; ++i) {
					getLogger(u -> u).debug(Arrays.toString(map[i]));
				}
			}
		}
		
		int dangerous = 0;
		for (int i = 0; i < dimension; ++i) {
			for (int j = 0; j < dimension; ++j) {
				if (map[i][j] > 1) {
					++dangerous;
				}
			}
		}
		
		getLogger(u -> u).info(dangerous);
	}
	
	public static Point getPoint(String input) {
		return new Point(Integer.parseInt(input.split(",")[0]), Integer.parseInt(input.split(",")[1]));
	}
}