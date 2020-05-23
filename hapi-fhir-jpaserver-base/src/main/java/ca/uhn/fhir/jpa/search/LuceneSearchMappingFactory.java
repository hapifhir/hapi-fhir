package ca.uhn.fhir.jpa.search;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import org.apache.lucene.analysis.core.*;
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
			.analyzerDef("exactAnalyzer", KeywordTokenizerFactory.class)
			.analyzerDef("conceptParentPidsAnalyzer", WhitespaceTokenizerFactory.class)
			.analyzerDef("termConceptPropertyAnalyzer", WhitespaceTokenizerFactory.class);

		return mapping;
	}
}
