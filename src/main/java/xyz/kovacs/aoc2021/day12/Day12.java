package xyz.kovacs.aoc2021.day12;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

import static xyz.kovacs.util.AocUtils.getAllLines;
import static xyz.kovacs.util.AocUtils.getLogger;

public class Day12 {
	
	public static void main(String[] args) {
		doPuzzle1("example1"); // 10
		doPuzzle1("example2"); // 19
		doPuzzle1("sample"); // 226
		doPuzzle1("input"); // ?
		doPuzzle2("example1"); // 36
		doPuzzle2("example2"); // 103
		doPuzzle2("sample"); // 3509
		doPuzzle2("input"); // ?
	}
	
	public static void doPuzzle1(String inputFile) {
		List<String> input = getAllLines(() -> inputFile);
		
		Set<Pair<String, String>> edges = getEdges(input);
		Set<String> nodes = getNodes(edges);
		
		// Yes, this is a blatant misuse of toString, but hey, it works!
		getLogger(u -> u).info("Paths start → end with visiting lower-cases at most once: {}",
		                       findPaths("start",
		                                 null,
		                                 new ArrayList<>(nodes.size()),
		                                 new TreeSet<>(nodes),
		                                 edges)
				                       .stream()
				                       .map(Objects::toString)
				                       .distinct()
				                       .count());
	}
	
	public static void doPuzzle2(String inputFile) {
		List<String> input = getAllLines(() -> inputFile);
		
		Set<Pair<String, String>> edges = getEdges(input);
		Set<String> nodes = getNodes(edges);
		
		Collection<List<String>> paths = new ArrayList<>((int) Math.pow(2, nodes.size() - 2));
		for (String smallCaveToRevisit : nodes.stream().filter(StringUtils::isAllLowerCase).toList()) {
			if (StringUtils.equalsAny(smallCaveToRevisit, "start", "end")) {
				continue;
			}
			paths.addAll(findPaths("start",
			                       smallCaveToRevisit,
			                       new ArrayList<>(nodes.size()),
			                       new TreeSet<>(nodes),
			                       edges));
		}
		
		// Yes, this is a blatant misuse of toString, but hey, it works!
		getLogger(u -> u).info("Paths start → end with visiting lower-cases at most once (revisiting once small at most twice): {}",
		                       paths.stream()
		                            .map(Objects::toString)
		                            .distinct()
		                            .count());
	}
	
	public static Set<Pair<String, String>> getEdges(List<String> input) {
		Set<Pair<String, String>> edges = new TreeSet<>();
		for (String line : input) {
			String from = StringUtils.substringBefore(line, "-");
			String to = StringUtils.substringAfter(line, "-");
			edges.add(Pair.of(from, to));
			edges.add(Pair.of(to, from));
		}
		getLogger(u -> u).debug("Edges in the graph are: {}", edges);
		return edges;
	}
	
	public static Set<String> getNodes(Set<Pair<String, String>> edges) {
		Set<String> nodes = new TreeSet<>();
		for (Pair<String, String> edge : edges) {
			nodes.add(edge.getLeft());
			nodes.add(edge.getRight());
		}
		getLogger(u -> u).debug("Nodes in the graph are: {}", nodes);
		return nodes;
	}
	
	/**
	 * This is essentially a modified recursive depth-first search.
	 */
	public static Collection<List<String>> findPaths(String currentNode, String revisitedSmallCave, List<String> path, Set<String> availableNodes, Set<Pair<String, String>> edges) {
		path.add(currentNode);
		getLogger(u -> u).debug("Current node: {}", currentNode);
		if (StringUtils.isAllLowerCase(currentNode)) {
			if (StringUtils.equals(revisitedSmallCave, currentNode)) {
				if (path.stream().filter(n -> StringUtils.equals(n, revisitedSmallCave)).count() == 2) {
					availableNodes.remove(currentNode);
				}
			} else {
				availableNodes.remove(currentNode);
			}
		}
		
		Collection<List<String>> paths = new ArrayList<>((int) Math.pow(2, availableNodes.size() - 2));
		for (String nextNode : edges.stream().filter(e -> e.getLeft().equals(currentNode)).map(Pair::getRight).toList()) {
			getLogger(u -> u).debug("Next node: {}", nextNode);
			if (StringUtils.equals("end", nextNode)) {
				path.add("end");
				getLogger(u -> u).debug("Path found: {}", path);
				paths.add(path);
				continue;
			}
			
			if (availableNodes.contains(nextNode)) {
				paths.addAll(findPaths(nextNode, revisitedSmallCave, new ArrayList<>(path), new TreeSet<>(availableNodes), edges));
			}
		}
		
		return paths;
	}
}
