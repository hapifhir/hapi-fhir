package ca.uhn.fhir.jpa.search;

import org.hibernate.search.elasticsearch.analyzer.definition.ElasticsearchAnalysisDefinitionRegistryBuilder;
import org.hibernate.search.elasticsearch.analyzer.definition.spi.ElasticsearchAnalysisDefinitionProvider;

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
			.withTokenFilters("standard", "stop", "snowball_english", "phonetic_doublemetaphone");
		builder.tokenFilter("phonetic_doublemetaphone")
			.type("phonetic")
			.param("encoder", "double_metaphone");
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
	}
}
