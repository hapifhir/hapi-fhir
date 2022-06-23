package ca.uhn.fhir.jpa.search;

import org.apache.lucene.analysis.core.KeywordTokenizerFactory;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterFilterFactory;
import org.apache.lucene.analysis.ngram.EdgeNGramFilterFactory;
import org.apache.lucene.analysis.ngram.NGramFilterFactory;
import org.apache.lucene.analysis.pattern.PatternTokenizerFactory;
import org.apache.lucene.analysis.phonetic.PhoneticFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurationContext;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurer;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurationContext;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurer;
import org.springframework.stereotype.Component;

import static ca.uhn.fhir.jpa.model.search.SearchParamTextPropertyBinder.LOWERCASE_ASCIIFOLDING_NORMALIZER;

/**
 * Class includes configuration classes for both Lucene and Elasticsearch as they usually need to be updated
 * simultaneously, and otherwise is very easy to miss the second
 */
@Component
public class HSearchAnalysisConfigurers {

	/**
	 * Factory for defining the analysers.
	 */
	public static class HapiLuceneAnalysisConfigurer implements LuceneAnalysisConfigurer {

		public static final String STANDARD_ANALYZER = "standardAnalyzer";
		public static final String NORM_STRING_ANALYZER = "normStringAnalyzer";
		public static final String EXACT_ANALYZER = "exactAnalyzer";

		@Override
		public void configure(LuceneAnalysisConfigurationContext theLuceneCtx) {
			theLuceneCtx.analyzer("autocompleteEdgeAnalyzer").custom()
				.tokenizer(PatternTokenizerFactory.class).param("pattern", "(.*)").param("group", "1")
				.tokenFilter(LowerCaseFilterFactory.class)
				.tokenFilter(StopFilterFactory.class)
				.tokenFilter(EdgeNGramFilterFactory.class)
				.param("minGramSize", "3")
				.param("maxGramSize", "50");

			theLuceneCtx.analyzer("autocompletePhoneticAnalyzer").custom()
				.tokenizer(StandardTokenizerFactory.class)
				.tokenFilter(StopFilterFactory.class)
				.tokenFilter(PhoneticFilterFactory.class).param("encoder", "DoubleMetaphone")
				.tokenFilter(SnowballPorterFilterFactory.class).param("language", "English");

			theLuceneCtx.analyzer("autocompleteNGramAnalyzer").custom()
				.tokenizer(StandardTokenizerFactory.class)
				.tokenFilter(WordDelimiterFilterFactory.class)
				.tokenFilter(LowerCaseFilterFactory.class)
				.tokenFilter(NGramFilterFactory.class)
				.param("minGramSize", "3")
				.param("maxGramSize", "20");

			theLuceneCtx.analyzer("autocompleteWordEdgeAnalyzer").custom()
				.tokenizer(StandardTokenizerFactory.class)
				.tokenFilter(LowerCaseFilterFactory.class)
				.tokenFilter(StopFilterFactory.class)
				.tokenFilter(EdgeNGramFilterFactory.class)
				.param("minGramSize", "3")
				.param("maxGramSize", "20");

			theLuceneCtx.analyzer(STANDARD_ANALYZER).custom()
				.tokenizer(StandardTokenizerFactory.class)
				.tokenFilter(LowerCaseFilterFactory.class)
				.tokenFilter(ASCIIFoldingFilterFactory.class);

			theLuceneCtx.analyzer(NORM_STRING_ANALYZER).custom()
				.tokenizer(KeywordTokenizerFactory.class)
				.tokenFilter(LowerCaseFilterFactory.class)
				.tokenFilter(ASCIIFoldingFilterFactory.class);

			theLuceneCtx.analyzer(EXACT_ANALYZER).custom()
				.tokenizer(KeywordTokenizerFactory.class);

			theLuceneCtx.analyzer("conceptParentPidsAnalyzer").custom()
				.tokenizer(WhitespaceTokenizerFactory.class);

			theLuceneCtx.analyzer("termConceptPropertyAnalyzer").custom()
				.tokenizer(WhitespaceTokenizerFactory.class);

			theLuceneCtx.normalizer(LOWERCASE_ASCIIFOLDING_NORMALIZER).custom()
				.tokenFilter(LowerCaseFilterFactory.class)
				.tokenFilter(ASCIIFoldingFilterFactory.class);

		}
	}


	public static class HapiElasticsearchAnalysisConfigurer implements ElasticsearchAnalysisConfigurer {

		@Override
		public void configure(ElasticsearchAnalysisConfigurationContext theConfigCtx) {

			theConfigCtx.analyzer("autocompleteEdgeAnalyzer").custom()
				.tokenizer("pattern_all")
				.tokenFilters("lowercase", "stop", "edgengram_3_50");

			theConfigCtx.tokenizer("pattern_all")
				.type("pattern")
				.param("pattern", "(.*)")
				.param("group", "1");

			theConfigCtx.tokenFilter("edgengram_3_50")
				.type("edgeNGram")
				.param("min_gram", "3")
				.param("max_gram", "50");


			theConfigCtx.analyzer("autocompleteWordEdgeAnalyzer").custom()
				.tokenizer("standard")
				.tokenFilters("lowercase", "stop", "wordedgengram_3_50");

			theConfigCtx.tokenFilter("wordedgengram_3_50")
				.type("edgeNGram")
				.param("min_gram", "3")
				.param("max_gram", "20");

			theConfigCtx.analyzer("autocompletePhoneticAnalyzer").custom()
				.tokenizer("standard")
				.tokenFilters("stop", "snowball_english");

			theConfigCtx.tokenFilter("snowball_english")
				.type("snowball")
				.param("language", "English");

			theConfigCtx.analyzer("autocompleteNGramAnalyzer").custom()
				.tokenizer("standard")
				.tokenFilters("word_delimiter", "lowercase", "ngram_3_20");

			theConfigCtx.tokenFilter("ngram_3_20")
				.type("nGram")
				.param("min_gram", "3")
				.param("max_gram", "20");


			theConfigCtx.analyzer(HapiLuceneAnalysisConfigurer.STANDARD_ANALYZER).custom()
				.tokenizer("standard")
				.tokenFilters("lowercase", "asciifolding");

			theConfigCtx.analyzer(HapiLuceneAnalysisConfigurer.NORM_STRING_ANALYZER).custom()
				.tokenizer("keyword") // We need the whole string to match, including whitespace.
				.tokenFilters("lowercase", "asciifolding");

			theConfigCtx.analyzer("exactAnalyzer")
				.custom()
				.tokenizer("keyword")
				.tokenFilters("unique");

			theConfigCtx.analyzer("conceptParentPidsAnalyzer").custom()
				.tokenizer("whitespace");

			theConfigCtx.analyzer("termConceptPropertyAnalyzer").custom()
				.tokenizer("whitespace");

			theConfigCtx.normalizer( LOWERCASE_ASCIIFOLDING_NORMALIZER ).custom()
				.tokenFilters( "lowercase", "asciifolding" );

		}
	}

}
