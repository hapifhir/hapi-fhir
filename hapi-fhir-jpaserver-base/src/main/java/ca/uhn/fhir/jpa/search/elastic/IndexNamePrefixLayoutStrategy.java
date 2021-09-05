package ca.uhn.fhir.jpa.search.elastic;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.backend.elasticsearch.index.layout.IndexLayoutStrategy;
import org.hibernate.search.backend.elasticsearch.logging.impl.Log;
import org.hibernate.search.util.common.logging.impl.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class instructs hibernate search on how to create index names for indexed entities.
 * In our case, we use this class to add an optional prefix to all indices which are created, which can be controlled via
 * {@link DaoConfig#setElasticSearchIndexPrefix(String)}.
 */
@Service
public class IndexNamePrefixLayoutStrategy implements IndexLayoutStrategy {

//	@Autowired
//	private DaoConfig myDaoConfig;

	static final Log log = (Log) LoggerFactory.make(Log.class, MethodHandles.lookup());
	public static final String NAME = "prefix";
	public static final Pattern UNIQUE_KEY_EXTRACTION_PATTERN = Pattern.compile("(.*)-\\d{6}");

	public String createInitialElasticsearchIndexName(String hibernateSearchIndexName) {
		return addPrefixIfNecessary(hibernateSearchIndexName + "-000001");
	}

	public String createWriteAlias(String hibernateSearchIndexName) {
		return addPrefixIfNecessary(hibernateSearchIndexName +"-write");
	}

	public String createReadAlias(String hibernateSearchIndexName) {
		return addPrefixIfNecessary(hibernateSearchIndexName + "-read");
	}

	private String addPrefixIfNecessary(String theCandidateName) {
		String myDaoConfig = "zoop";
		if (!StringUtils.isBlank(myDaoConfig)) {
			return myDaoConfig + "-" + theCandidateName;
		} else {
			return theCandidateName;
		}
	}

	public String extractUniqueKeyFromHibernateSearchIndexName(String hibernateSearchIndexName) {
		return hibernateSearchIndexName;
	}

	public String extractUniqueKeyFromElasticsearchIndexName(String elasticsearchIndexName) {
		Matcher matcher = UNIQUE_KEY_EXTRACTION_PATTERN.matcher(elasticsearchIndexName);
		if (!matcher.matches()) {
			throw log.invalidIndexPrimaryName(elasticsearchIndexName, UNIQUE_KEY_EXTRACTION_PATTERN);
		} else {
			String candidateUniqueKey= matcher.group(1);
			return removePrefixIfNecessary(candidateUniqueKey);
		}
	}

	private String removePrefixIfNecessary(String theCandidateUniqueKey) {
		String myDaoConfig = "zoop";
		if (!StringUtils.isBlank(myDaoConfig)) {
			return theCandidateUniqueKey.replace(myDaoConfig+ "-", "");
		} else {
			return theCandidateUniqueKey;
		}
	}
}
