package ca.uhn.fhir.jpa.search;

import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterFilterFactory;
import org.apache.lucene.analysis.ngram.EdgeNGramFilterFactory;
import org.apache.lucene.analysis.ngram.NGramFilterFactory;
import org.apache.lucene.analysis.pattern.PatternTokenizerFactory;
import org.apache.lucene.analysis.phonetic.PhoneticFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.annotations.Factory;
import org.hibernate.search.cfg.SearchMapping;

/**
 * Factory for defining the analysers.
 */
public class LuceneSearchMappingFactory {
	@Factory
	public SearchMapping getSearchMapping() {
		SearchMapping mapping = new SearchMapping();

		mapping.analyzerDef("autocompleteEdgeAnalyzer", PatternTokenizerFactory.class)
				.tokenizerParam("pattern", "(.*)")
				.tokenizerParam("group", "1")
				.filter(LowerCaseFilterFactory.class)
				.filter(StopFilterFactory.class)
				.filter(EdgeNGramFilterFactory.class)
				.param("minGramSize", "3")
				.param("maxGramSize", "50")
			.analyzerDef("autocompletePhoneticAnalyzer", StandardTokenizerFactory.class)
				.filter(StandardFilterFactory.class)
				.filter(StopFilterFactory.class)
				.filter(PhoneticFilterFactory.class)
				.param("encoder", "DoubleMetaphone")
				.filter(SnowballPorterFilterFactory.class)
				.param("language", "English")
			.analyzerDef("autocompleteNGramAnalyzer", StandardTokenizerFactory.class)
				.filter(WordDelimiterFilterFactory.class)
				.filter(LowerCaseFilterFactory.class)
				.filter(NGramFilterFactory.class)
				.param("minGramSize", "3")
				.param("maxGramSize", "20")
			.analyzerDef("standardAnalyzer", StandardTokenizerFactory.class)
				.filter(LowerCaseFilterFactory.class)
			.analyzerDef("exactAnalyzer", StandardTokenizerFactory.class)
			.analyzerDef("conceptParentPidsAnalyzer", WhitespaceTokenizerFactory.class);

		return mapping;
	}
}
