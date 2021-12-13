package xyz.kovacs.util;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class AocUtils {
	
	/**
	 * Convenience over all!
	 */
	public static Logger getLogger(Object context) {
		return LogManager.getLogger(context.getClass());
	}
	
	/**
	 * Magic for providing loggers for static contexts.
	 */
	public static Logger getLogger(Function<Object, Object> contextSupplier) {
		return LogManager.getLogger(StringUtils.substringBefore(contextSupplier.getClass().getName(), "$$Lambda$"));
	}
	
	/**
	 * Provides the contents of a given file, found in resources.
	 *
	 * @param pathSupplier supplies the name of the file within resources (looking for the file in the same package 😉).
	 * @return all the lines of the file (UTF-8) given as param.
	 */
	public static List<String> getAllLines(Supplier<String> pathSupplier) {
		try {
			StringBuilder path = new StringBuilder("/");
			String packageName = StringUtils.substringBeforeLast(StringUtils.substringBefore(pathSupplier.getClass()
			                                                                                             .getName(), "$$Lambda$"), ".");
			path.append(RegExUtils.replaceAll(packageName, "\\.", "/")).append("/").append(pathSupplier.get());
			
			//noinspection ConstantConditions
			return Files.readAllLines(Paths.get(pathSupplier.getClass().getResource(path.toString()).toURI()));
		} catch (URISyntaxException | IOException | NullPointerException e) {
			getLogger(u -> u).error("No bueno, cannot open '{}'", pathSupplier.get(), e);
			System.exit(1);
			return null; // keeping the compiler happy
		}
	}
	
	/**
	 * Provides the "map" found in the given file, found in resources.
	 * <p>
	 * Take care that this method does not do any kind of input validation. I.e., if the contents of the given file do not
	 * describe a 1-digit integer-based n × m map, you will get jumbled output (ShIShO = Shit-In-Shit-Out).
	 *
	 * @param pathSupplier supplies the name of the file within resources (looking for the file in the same package 😉)
	 * @return the "map", i.e. a two-dimensional integer array.
	 */
	@SuppressWarnings("NonAsciiCharacters")
	public static int[][] getMap(Supplier<String> pathSupplier) {
		List<String> input = getAllLines(pathSupplier);
		int x = input.size();
		int y = input.stream().mapToInt(String::length).max()
		             .orElseThrow(() -> new IllegalStateException("No bueno, could not determine second dimension"));
		int[][] map = new int[x][y];
		for (int xₙ = 0; xₙ < x; ++xₙ) {
			String line = input.get(xₙ);
			int length = line.length();
			for (int yₙ = 0; yₙ < length; ++yₙ) {
				map[xₙ][yₙ] = line.charAt(yₙ) - '0';
			}
		}
		return map;
	}
	
	/**
	 * Logs the given map with the given logger on the given level.
	 */
	public static void logMap(boolean[][] map, Logger logger, Level level) {
		logger.atLevel(level).log("Map is:");
		for (int i = 0; i < map.length; ++i) {
			StringBuilder row = new StringBuilder(map[i].length + 2);
			row.append("[");
			for (int j = 0; j < map[i].length; ++j) {
				row.append(map[i][j] ? "⊕" : " ");
			}
			row.append("]");
			logger.atLevel(level).log(row.toString());
		}
	}
	
	/**
	 * Logs the given map with the given logger on the given level.
	 */
	public static void logMap(int[][] map, Logger logger, Level level) {
		logger.atLevel(level).log("Map is:");
		for (int i = 0; i < map.length; ++i) {
			logger.atLevel(level).log(Arrays.toString(map[i]));
		}
	}
}
