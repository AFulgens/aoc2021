package xyz.kovacs.util;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
	 * Magic for providing loggers for static contexts
	 */
	public static Logger getLogger(Function<Object, Object> contextSupplier) {
		return LogManager.getLogger(StringUtils.substringBefore(contextSupplier.getClass()
		                                                                       .getName(), "$$Lambda$"));
	}
	
	/**
	 * Provides the contents of a given file, found in resources
	 *
	 * @param pathSupplier supplies the name of the file within resources (looking for the file in the same package 😉)
	 * @return all the lines of the file (UTF-8) given as param
	 */
	public static List<String> getAllLines(Supplier<String> pathSupplier) {
		try {
			StringBuilder path = new StringBuilder("/");
			String packageName = StringUtils.substringBeforeLast(StringUtils.substringBefore(pathSupplier.getClass()
			                                                                                             .getName(), "$$Lambda$"),
			                                                     ".");
			path.append(RegExUtils.replaceAll(packageName, "\\.", "/"))
			    .append("/")
			    .append(pathSupplier.get());
			
			//noinspection ConstantConditions
			return Files.readAllLines(Paths.get(pathSupplier.getClass()
			                                                .getResource(path.toString())
			                                                .toURI()));
		} catch (URISyntaxException | IOException | NullPointerException e) {
			getLogger(u -> u).error("No bueno, cannot open '{}'", pathSupplier.get(), e);
			System.exit(1);
			return null; // keeping the compiler happy
		}
	}
}
