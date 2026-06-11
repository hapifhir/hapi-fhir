package ca.uhn.fhir.jpa.dao.index;

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamTokenCommon;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamTokenCommonRes;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamTokenIdentifier;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.rest.api.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class TokenIndexEntityConverterTest {

	private static final String RESOURCE_TYPE = "Patient";
	private static final String SYSTEM = "http://sys";
	private static final String VALUE = "value-1";
	private static final long RESOURCE_ID = 123L;
	private static final int PARTITION_ID = 7;
	private static final long SYSTEM_ID = 55L;
	private static final long SYSTEM_URL_ID = 66L;

	@Test
	void toCommon_copiesAllFields() {
		ResourceIndexedSearchParamToken token = newToken("code", PARTITION_ID);

		ResourceIndexedSearchParamTokenCommon result = TokenIndexEntityConverter.toCommon(token, SYSTEM_ID);

		assertThat(result.getHashSystemAndValue()).isEqualTo(token.getHashSystemAndValue());
		assertThat(result.getSystemId()).isEqualTo(SYSTEM_ID);
		assertThat(result.getValue()).isEqualTo(VALUE);
		assertThat(result.getHashIdentity()).isEqualTo(token.getHashIdentity());
		assertThat(result.getHashValue()).isEqualTo(token.getHashValue());
	}

	@Test
	void toCommonRes_buildsCompositeKey() {
		ResourceIndexedSearchParamToken token = newToken("code", PARTITION_ID);
		ResourceTable entity = newEntity();

		ResourceIndexedSearchParamTokenCommonRes result = TokenIndexEntityConverter.toCommonRes(token, entity);

		assertThat(result.getResourceId()).isEqualTo(RESOURCE_ID);
		assertThat(result.getHashSystemAndValue()).isEqualTo(token.getHashSystemAndValue());
		assertThat(result.getPartitionId()).isEqualTo(PARTITION_ID);
	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	void toIdentifier_typeHashPopulatedOnlyForOfType(boolean theOfType) {
		String paramName = theOfType ? "identifier" + Constants.PARAMQUALIFIER_TOKEN_OF_TYPE : "identifier";
		ResourceIndexedSearchParamToken token = newToken(paramName, PARTITION_ID);
		ResourceTable entity = newEntity();

		ResourceIndexedSearchParamTokenIdentifier result =
				TokenIndexEntityConverter.toIdentifier(token, entity, SYSTEM_URL_ID);

		// TYPE_HASH_SYS_AND_VALUE is only meaningful for :of-type tokens
		assertThat(result.getTypeHashSystemAndValue()).isEqualTo(theOfType ? token.getHashSystemAndValue() : null);
		assertThat(result.getResourceId()).isEqualTo(RESOURCE_ID);
		assertThat(result.getPartitionId()).isEqualTo(PARTITION_ID);
		assertThat(result.getHashIdentity()).isEqualTo(token.getHashIdentity());
		assertThat(result.getSystemUrlId()).isEqualTo(SYSTEM_URL_ID);
		assertThat(result.getValue()).isEqualTo(VALUE);
		// :of-type tokens skip partial hashes, so getHashValue() is null in that case
		assertThat(result.getHashValue()).isEqualTo(token.getHashValue());
	}

	private ResourceIndexedSearchParamToken newToken(String theParamName, Integer thePartitionId) {
		ResourceIndexedSearchParamToken token =
				new ResourceIndexedSearchParamToken(new PartitionSettings(), RESOURCE_TYPE, theParamName, SYSTEM, VALUE);
		if (thePartitionId != null) {
			token.setPartitionId(new PartitionablePartitionId(thePartitionId, null));
		}
		token.calculateHashes();
		return token;
	}

	private ResourceTable newEntity() {
		ResourceTable entity = new ResourceTable();
		entity.setResourceType(RESOURCE_TYPE);
		entity.setIdForUnitTest(RESOURCE_ID);
		return entity;
	}
}
