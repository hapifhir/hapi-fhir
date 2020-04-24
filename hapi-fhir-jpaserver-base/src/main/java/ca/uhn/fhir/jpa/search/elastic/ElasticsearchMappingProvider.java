package ca.uhn.fhir.jpa.search.elastic;

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

import org.hibernate.search.elasticsearch.analyzer.definition.ElasticsearchAnalysisDefinitionProvider;
import org.hibernate.search.elasticsearch.analyzer.definition.ElasticsearchAnalysisDefinitionRegistryBuilder;

public class ElasticsearchMappingProvider implements ElasticsearchAnalysisDefinitionProvider {

	@Override
	public void register(ElasticsearchAnalysisDefinitionRegistryBuilder builder) {
		builder.analyzer("autocompleteEdgeAnalyzer")
			.withTokenizer("pattern_all")
			.withTokenFilters("lowercase", "stop", "edgengram_3_50");
		builder.tokenizer("pattern_all").type("pattern").param("pattern", "(.*)").param("group", "1");
		builder.tokenFilter("edgengram_3_50")
			.type("edgeNGram")
			.param("min_gram", "3")
			.param("max_gram", "50");

		builder.analyzer("autocompletePhoneticAnalyzer")
			.withTokenizer("standard")
			.withTokenFilters("standard", "stop", "snowball_english");
		builder.tokenFilter("snowball_english").type("snowball").param("language", "English");

		builder.analyzer("autocompleteNGramAnalyzer")
			.withTokenizer("standard")
			.withTokenFilters("word_delimiter", "lowercase", "ngram_3_20");
		builder.tokenFilter("ngram_3_20")
			.type("nGram")
			.param("min_gram", "3")
			.param("max_gram", "20");

		builder.analyzer("standardAnalyzer").withTokenizer("standard").withTokenFilters("lowercase");

		builder.analyzer("exactAnalyzer").withTokenizer("standard");

		builder.analyzer("conceptParentPidsAnalyzer").withTokenizer("whitespace");

		builder.analyzer("termConceptPropertyAnalyzer").withTokenizer("whitespace");

	}
}
