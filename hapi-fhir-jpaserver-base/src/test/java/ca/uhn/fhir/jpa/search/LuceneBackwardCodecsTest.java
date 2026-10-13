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
 * A Lucene index written to disk by an earlier HAPI FHIR release has to stay readable after a Lucene upgrade.
 * When Lucene introduces a new default codec, the previous one moves out of {@code lucene-core} into
 * {@code lucene-backward-codecs}, so that jar must be on the classpath of this module.
 * <p>
 * The fixture {@code lucene/lucene-9.11.1-index.zip} is an index written by Lucene 9.11.1 (HAPI FHIR 8.12.1),
 * whose default codec is {@code Lucene99}. It holds the files {@code _0.cfe}, {@code _0.cfs}, {@code _0.si} and
 * {@code segments_1} at the archive root, and was generated with {@code lucene-core} and
 * {@code lucene-analysis-common} 9.11.1 on the classpath by the code below, then archived with {@code zip -X -j}
 * (leaving out {@code write.lock}). Regenerating it with a newer Lucene defeats the test, which is why the test
 * also asserts the codec name.
 * </p>
 * <pre>{@code
 * try (FSDirectory directory = FSDirectory.open(dir);
 * 		IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()))) {
 * 	for (int i = 0; i < 3; i++) {
 * 		Document doc = new Document();
 * 		doc.add(new StringField("id", "doc-" + i, Field.Store.YES));
 * 		doc.add(new TextField("text", "patient narrative number " + i, Field.Store.NO));
 * 		writer.addDocument(doc);
 * 	}
 * 	writer.commit();
 * }
 * }</pre>
 */
// Created by Claude Opus 5.5
class LuceneBackwardCodecsTest {

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
