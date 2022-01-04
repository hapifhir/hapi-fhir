package ca.uhn.fhir.jpa.search.elastic;

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

import ca.uhn.fhir.jpa.search.HapiLuceneAnalysisConfigurer;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurationContext;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurer;

public class HapiElasticsearchAnalysisConfigurer implements ElasticsearchAnalysisConfigurer{

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
	}
}
