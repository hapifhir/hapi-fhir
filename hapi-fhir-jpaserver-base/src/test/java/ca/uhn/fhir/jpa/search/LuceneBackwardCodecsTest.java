package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.util.ClasspathUtil;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.SegmentReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.FSDirectory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A Lucene index written to disk by an earlier HAPI FHIR release has to stay readable after a Lucene version upgrade,
 * which needs {@code lucene-backward-codecs} on the classpath.
 */
// Created by Claude Opus 5.5
class LuceneBackwardCodecsTest {

	/**
	 * A three-document index fixture written by an older Lucene version (9.11.1 with default codec {@code Lucene99}).
	 * Contains a {@code StandardAnalyzer} with a stored {@code StringField} {@code id} and a {@code TextField} {@code text}.
	 */
	private static final String FIXTURE_INDEX = "lucene/lucene-9.11.1-index.zip";

	@Test
	void indexWrittenByLucene9_11_1_canBeOpenedAndSearched(@TempDir Path theTempDir) throws IOException {
		unzipFixtureIndex(theTempDir);

		try (FSDirectory directory = FSDirectory.open(theTempDir);
				DirectoryReader reader = DirectoryReader.open(directory)) {
			assertThat(reader.numDocs()).isEqualTo(3);

			IndexSearcher searcher = new IndexSearcher(reader);
			assertThat(searcher.count(new TermQuery(new Term("text", "patient")))).isEqualTo(3);
			assertThat(searcher.count(new TermQuery(new Term("id", "doc-1")))).isEqualTo(1);

			List<String> segmentCodecs = reader.leaves().stream()
					.map(t -> ((SegmentReader) t.reader()).getSegmentInfo().info.getCodec().getName())
					.toList();
			assertThat(segmentCodecs).containsExactly("Lucene99");
		}
	}

	private static void unzipFixtureIndex(Path theTargetDir) throws IOException {
		try (InputStream fixture = ClasspathUtil.loadResourceAsStream(FIXTURE_INDEX);
				ZipInputStream zip = new ZipInputStream(fixture)) {
			ZipEntry entry;
			while ((entry = zip.getNextEntry()) != null) {
				Files.copy(zip, theTargetDir.resolve(entry.getName()));
			}
		}
	}
}
