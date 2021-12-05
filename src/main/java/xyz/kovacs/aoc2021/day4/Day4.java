package xyz.kovacs.aoc2021.day4;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static xyz.kovacs.util.AocUtils.getAllLines;
import static xyz.kovacs.util.AocUtils.getLogger;

public class Day4 {
	
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
		
		List<int[][]> boards = getBoards(lines.subList(2, lines.size()));
		
		int lastNumber = Integer.MAX_VALUE;
		int[][] winningBoard;
		while ((winningBoard = getWinner(boards)) == null) {
			lastNumber = drawNumberAndMarkBoards(lines.get(0), boards);
			lines.set(0, StringUtils.substringAfter(lines.get(0), ","));
		}
		
		getLogger(u -> u).info(lastNumber * (Arrays.stream(winningBoard).flatMapToInt(Arrays::stream).filter(i -> i > 0)
		                                           .sum()));
	}
	
	/**
	 * Solution for puzzle 2.
	 */
	public static void doPuzzle2(String inputFile) {
		List<String> lines = getAllLines(() -> inputFile);
		
		List<int[][]> boards = getBoards(lines.subList(2, lines.size()));
		
		int lastNumber = Integer.MAX_VALUE;
		while (boards.size() > 1) {
			lastNumber = drawNumberAndMarkBoards(lines.get(0), boards);
			lines.set(0, StringUtils.substringAfter(lines.get(0), ","));
			boards.removeIf(Day4::isWinner);
		}
		
		// although we already have the winning board, the puzzle is not optimised, so have to keep going
		// until the last board wins too
		int[][] winningBoard = boards.get(0);
		while (!isWinner(winningBoard)) {
			lastNumber = drawNumberAndMarkBoards(lines.get(0), boards);
			lines.set(0, StringUtils.substringAfter(lines.get(0), ","));
		}
		
		getLogger(u -> u).info(lastNumber * (Arrays.stream(boards.get(0)).flatMapToInt(Arrays::stream).filter(i -> i > 0)
		                                           .sum()));
	}
	
	public static List<int[][]> getBoards(List<String> lines) {
		lines.removeIf(line -> StringUtils.isBlank(line));
		List<int[][]> result = new ArrayList<>(lines.size() / 5);
		int[][] current = new int[5][5];
		for (int i = 0; i < lines.size(); ++i) {
			if (i != 0 && i % 5 == 0) {
				result.add(current.clone());
				current = new int[5][5];
			}
			
			current[i % 5] = Arrays.stream((StringUtils.removeStart(lines.get(i), " ").split("\\s+")))
			                       .mapToInt(Integer::valueOf).toArray();
		}
		result.add(current);
		return result;
	}
	
	public static boolean isWinner(int[][] board) {
		return getCompleteRow(board) != null || getCompleteColumn(board) != null;
	}
	
	public static int[][] getWinner(List<int[][]> boards) {
		for (int[][] board : boards) {
			if (isWinner(board)) {
				return board;
			}
		}
		return null;
	}
	
	public static int[] getCompleteRow(int[][] board) {
		for (int row = 0; row < 5; ++row) {
			boolean rowComplete = true;
			for (int column = 0; rowComplete && column < 5; ++column) {
				rowComplete &= board[row][column] < 0;
			}
			if (rowComplete) {
				return board[row];
			}
		}
		return null;
	}
	
	public static int[] getCompleteColumn(int[][] board) {
		for (int column = 0; column < 5; ++column) {
			boolean columnComplete = true;
			for (int row = 0; columnComplete && row < 5; ++row) {
				columnComplete &= board[row][column] < 0;
			}
			if (columnComplete) {
				return new int[]{board[0][column], board[1][column], board[2][column], board[3][column], board[4][column]};
			}
		}
		return null;
	}
	
	public static int drawNumberAndMarkBoards(String line, List<int[][]> boards) {
		int next = Integer.parseInt(StringUtils.substringBefore(line, ","));
		
		for (int[][] board : boards) {
			for (int row = 0; row < 5; ++row) {
				for (int column = 0; column < 5; ++column) {
					if (board[row][column] == next) {
						board[row][column] = -1; // in this puzzle we only need the non-marked numbers
					}
				}
			}
		}
		
		if (getLogger(u -> u).isDebugEnabled()) {
			getLogger(u -> u).debug("Drawn: {}", next);
			for (int[][] board : boards) {
				for (int i = 0; i < 5; ++i) {
					getLogger(u -> u).debug(Arrays.toString(board[i]));
				}
				getLogger(u -> u).debug("");
			}
		}
		
		return next;
	}
}