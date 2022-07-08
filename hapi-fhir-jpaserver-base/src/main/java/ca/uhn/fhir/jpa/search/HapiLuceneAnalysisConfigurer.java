package ca.uhn.fhir.jpa.search;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurationContext;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurer;
import org.springframework.stereotype.Component;

/**
 * Factory for defining the analysers.
 */
@Component
public class HapiLuceneAnalysisConfigurer implements LuceneAnalysisConfigurer {

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
	}
}
