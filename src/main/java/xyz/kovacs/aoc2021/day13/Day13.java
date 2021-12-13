package xyz.kovacs.aoc2021.day13;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;

import java.util.List;
import java.util.stream.Stream;

import static xyz.kovacs.util.AocUtils.*;

@SuppressWarnings("NonAsciiCharacters")
public class Day13 {
	
	public static void main(String[] args) {
		doPuzzle("sample"); // 17
		doPuzzle("input"); // ?
	}
	
	/**
	 * Combined solution of:
	 * <ul>
	 *   <li>puzzle 1 (look for the message about how many dots are there are 1 fold)</li>
	 *   <li>puzzle 2 (look for the last message and decipher it by brainpower)</li>
	 * </ul>
	 */
	public static void doPuzzle(String inputFile) {
		List<String> input = getAllLines(() -> inputFile);
		
		int ṽ = input.stream().filter(l -> StringUtils.isNotBlank(l)).filter(l -> Character.isDigit(l.charAt(0)))
		             .flatMap(l -> Stream.of(l.split(",")[0])).mapToInt(Integer::parseInt).max()
		             .orElseThrow(() -> new IllegalStateException("No bueno, cannot determine max")) + 1;
		int ħ = input.stream().filter(l -> StringUtils.isNotBlank(l)).filter(l -> Character.isDigit(l.charAt(0)))
		             .flatMap(l -> Stream.of(l.split(",")[1])).mapToInt(Integer::parseInt).max()
		             .orElseThrow(() -> new IllegalStateException("No bueno, cannot determine max")) + 1;
		
		boolean[][] map = new boolean[ħ][ṽ];
		int ł = 0;
		for (; ; ++ł) {
			String line = input.get(ł);
			if (StringUtils.isBlank(line)) {
				break;
			}
			
			String[] split = line.split(",");
			map[Integer.parseInt(split[1])][Integer.parseInt(split[0])] = true;
		}
		logMap(map, getLogger(u -> u), Level.DEBUG);
		
		int beforeFolds = ł;
		for (int folds = ++ł; folds < input.size(); ++folds) {
			String[] split = input.get(folds).split(" ")[2].split("=");
			int foldAt = Integer.parseInt(split[1]);
			
			boolean[][] mapʹ;
			if ("x".equals(split[0])) {
				mapʹ = new boolean[ħ][ṽ / 2];
				for (int h = 0; h < ħ; ++h) {
					for (int v = 1; foldAt + v < ṽ; ++v) {
						mapʹ[h][foldAt - v] = map[h][foldAt - v] || map[h][foldAt + v];
					}
				}
				ṽ /= 2;
			} else { // "y".equals(split[0])
				mapʹ = new boolean[ħ / 2][ṽ];
				for (int h = 1; foldAt + h < ħ; ++h) {
					for (int v = 0; v < ṽ; ++v) {
						mapʹ[foldAt - h][v] = map[foldAt - h][v] || map[foldAt + h][v];
					}
				}
				ħ /= 2;
			}
			map = mapʹ;
			logMap(map, getLogger(u -> u), Level.DEBUG);
			
			int count = 0;
			for (int h = 0; h < ħ; ++h) {
				for (int v = 0; v < ṽ; ++v) {
					count += map[h][v] ? 1 : 0;
				}
			}
			getLogger(u -> u).info("There are {} dot(s) after {} fold(s)", count, folds - beforeFolds);
		}
		
		logMap(map, getLogger(u -> u), Level.INFO);
	}
	
}
