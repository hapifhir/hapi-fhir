// Created by claude-opus-4-7
package ca.uhn.fhir.interceptor.model;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Regression guard for GL-8692: forbids new callers of the deprecated
 * {@link RequestPartitionId#defaultPartition()} no-arg overload (and the
 * {@code defaultPartition(LocalDate)} overload) inside any {@code hapi-fhir-*\/src/main}
 * tree.
 *
 * <p>This is a cross-module check, so it uses source scanning rather than ArchUnit's
 * classpath-based analysis (a single test does not have all 60+ modules on its classpath).
 * The current set of call sites is recorded as a count per file in
 * {@code no-deprecated-default-partition-allowlist.txt}; the test fails on any divergence,
 * which catches both new violations and stale allowlist entries left behind after a cleanup.
 *
 * <p>Per CLAUDE.md, calls inside {@code ca.uhn.fhir.jpa.migrate} are restricted from
 * automated rewriting and remain allowlisted until Stage R of GL-8692 is owner-driven.
 */
public class NoDeprecatedDefaultPartitionTest {

	private static final String ALLOWLIST_RESOURCE = "no-deprecated-default-partition-allowlist.txt";

	private static final Pattern NO_ARG_CALL =
			Pattern.compile("RequestPartitionId\\s*\\.\\s*defaultPartition\\s*\\(\\s*\\)");

	private static final Pattern LOCAL_DATE_ARG_CALL =
			Pattern.compile("RequestPartitionId\\s*\\.\\s*defaultPartition\\s*\\(\\s*(?:LocalDate\\b|null\\s*\\))");

	private static final Pattern BLOCK_COMMENT = Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL);
	private static final Pattern LINE_COMMENT = Pattern.compile("//[^\\n]*");

	@Test
	void deprecatedDefaultPartitionCallSitesMatchAllowlist() throws IOException {
		Path repoRoot = locateRepoRoot();
		Map<String, Integer> actual = scanForDeprecatedCalls(repoRoot);
		Map<String, Integer> expected = loadAllowlist();

		assertThat(actual)
				.as(
						"Deprecated RequestPartitionId.defaultPartition() / defaultPartition(LocalDate) call sites "
								+ "under hapi-fhir-*/src/main must match the GL-8692 allowlist ("
								+ ALLOWLIST_RESOURCE
								+ "). If you removed a deprecated call, decrement or delete its allowlist entry "
								+ "in the same commit. New deprecated calls are not permitted; rewrite the call "
								+ "to use IDefaultPartitionSettings.getDefaultRequestPartitionId().")
				.containsExactlyInAnyOrderEntriesOf(expected);
	}

	private Map<String, Integer> scanForDeprecatedCalls(Path repoRoot) throws IOException {
		Map<String, Integer> counts = new TreeMap<>();
		try (Stream<Path> modules = Files.list(repoRoot)) {
			modules.filter(Files::isDirectory)
					.filter(p -> p.getFileName().toString().startsWith("hapi-fhir-"))
					.forEach(module -> scanModule(repoRoot, module, counts));
		}
		return counts;
	}

	private void scanModule(Path repoRoot, Path module, Map<String, Integer> counts) {
		Path srcMain = module.resolve("src").resolve("main").resolve("java");
		if (!Files.isDirectory(srcMain)) {
			return;
		}
		try (Stream<Path> files = Files.walk(srcMain)) {
			files.filter(p -> p.toString().endsWith(".java")).forEach(file -> {
				int hits = countDeprecatedCalls(file);
				if (hits > 0) {
					String relative =
							repoRoot.relativize(file).toString().replace('\\', '/');
					counts.put(relative, hits);
				}
			});
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	private int countDeprecatedCalls(Path file) {
		String source;
		try {
			source = Files.readString(file, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		String stripped = LINE_COMMENT
				.matcher(BLOCK_COMMENT.matcher(source).replaceAll(""))
				.replaceAll("");
		int hits = 0;
		Matcher noArg = NO_ARG_CALL.matcher(stripped);
		while (noArg.find()) {
			hits++;
		}
		Matcher localDate = LOCAL_DATE_ARG_CALL.matcher(stripped);
		while (localDate.find()) {
			hits++;
		}
		return hits;
	}

	private Map<String, Integer> loadAllowlist() throws IOException {
		Map<String, Integer> result = new TreeMap<>();
		InputStream stream = getClass().getClassLoader().getResourceAsStream(ALLOWLIST_RESOURCE);
		if (stream == null) {
			throw new IllegalStateException("Missing allowlist resource: " + ALLOWLIST_RESOURCE);
		}
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String trimmed = line.trim();
				if (trimmed.isEmpty() || trimmed.startsWith("#")) {
					continue;
				}
				int sep = trimmed.lastIndexOf(' ');
				if (sep < 0) {
					throw new IllegalStateException("Malformed allowlist line: " + line);
				}
				String path = trimmed.substring(0, sep).trim();
				int count = Integer.parseInt(trimmed.substring(sep + 1).trim());
				result.merge(path, count, Integer::sum);
			}
		}
		return result;
	}

	private Path locateRepoRoot() {
		Path candidate = Paths.get("").toAbsolutePath();
		for (int i = 0; i < 6; i++) {
			if (looksLikeRepoRoot(candidate)) {
				return candidate;
			}
			Path parent = candidate.getParent();
			if (parent == null) {
				break;
			}
			candidate = parent;
		}
		throw new IllegalStateException(
				"Could not locate HAPI FHIR repo root starting from " + Paths.get("").toAbsolutePath());
	}

	private boolean looksLikeRepoRoot(Path dir) {
		return Files.isDirectory(dir.resolve("hapi-fhir-base"))
				&& Files.isDirectory(dir.resolve("hapi-fhir-storage"))
				&& Files.isDirectory(dir.resolve("hapi-fhir-jpaserver-base"));
	}
}
