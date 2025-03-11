package ca.uhn.fhir.jpa.dao.r4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import org.apache.commons.lang3.RandomStringUtils;
import org.eclipse.jetty.util.StringUtil;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Organization;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FhirSystemDaoR4SearchTest extends BaseJpaR4SystemTest {

	@Test
	public void testSearchParameter_WithIdentifierValueUnderMaxLength_ShouldSuccess() {
		// setup
		final String identifierSystem = "http://www.acme.org.au/units";
		final String identifierValue = "identifierValue";
		final Identifier identifier = new Identifier().setSystem(identifierSystem).setValue(identifierValue);
		final Organization organization = new Organization().addIdentifier(identifier);

		// execute
		myOrganizationDao.create(organization, mySrd);

		// verify token value
		final ResourceIndexedSearchParamToken resultSearchTokens = myResourceIndexedSearchParamTokenDao.findAll().get(0);
		final String tokenSystem = resultSearchTokens.getSystem();
		final String tokenValue = resultSearchTokens.getValue();

		assertEquals(identifierSystem, tokenSystem);
		assertEquals(identifierValue, tokenValue);

		// verify hash
		final long tokensHashSystem = resultSearchTokens.getHashSystem();
		final long expectedHashSystem = ResourceIndexedSearchParamToken.calculateHashValue(new PartitionSettings(), RequestPartitionId.defaultPartition(), "Organization", "identifier", identifierSystem);
		final long tokensHashValue = resultSearchTokens.getHashValue();
		final long expectedHashValue = ResourceIndexedSearchParamToken.calculateHashValue(new PartitionSettings(), RequestPartitionId.defaultPartition(), "Organization", "identifier", identifierValue);

		assertEquals(expectedHashSystem, tokensHashSystem);
		assertEquals(expectedHashValue, tokensHashValue);
	}

	@Test
	public void testSearchParameter_WithIdentifierSystemAndValueOverMaxLength_ShouldHashBeforeTruncate() {
		// setup
		final String identifierSystem = RandomStringUtils.randomAlphanumeric(ResourceIndexedSearchParamToken.MAX_LENGTH + 100);
		final String identifierValue = RandomStringUtils.randomAlphanumeric(ResourceIndexedSearchParamToken.MAX_LENGTH + 100);
		final Identifier identifier = new Identifier().setSystem(identifierSystem).setValue(identifierValue);
		final Organization organization = new Organization().addIdentifier(identifier);

		// execute
		myOrganizationDao.create(organization, mySrd);

		// verify token value
		final ResourceIndexedSearchParamToken resultSearchTokens = myResourceIndexedSearchParamTokenDao.findAll().get(0);
		final String tokenSystem = resultSearchTokens.getSystem();
		final String tokenValue = resultSearchTokens.getValue();
		final String expectedSystem = StringUtil.truncate(identifierSystem, ResourceIndexedSearchParamToken.MAX_LENGTH);
		final String expectedValue = StringUtil.truncate(identifierValue, ResourceIndexedSearchParamToken.MAX_LENGTH);

		assertEquals(expectedSystem, tokenSystem);
		assertEquals(expectedValue, tokenValue);

		// verify hash
		final long tokensHashSystem = resultSearchTokens.getHashSystem();
		final long expectedHashSystem = ResourceIndexedSearchParamToken.calculateHashValue(new PartitionSettings(), RequestPartitionId.defaultPartition(), "Organization", "identifier", identifierSystem);
		final long tokensHashValue = resultSearchTokens.getHashValue();
		final long expectedHashValue = ResourceIndexedSearchParamToken.calculateHashValue(new PartitionSettings(), RequestPartitionId.defaultPartition(), "Organization", "identifier", identifierValue);

		assertEquals(expectedHashSystem, tokensHashSystem);
		assertEquals(expectedHashValue, tokensHashValue);
	}
	
	/*//@formatter:off
	 * [ERROR] Search parameter action has conflicting types token and reference
	 * [ERROR] Search parameter source has conflicting types token and reference
	 * [ERROR] Search parameter plan has conflicting types reference and token
	 * [ERROR] Search parameter version has conflicting types token and string
	 * [ERROR] Search parameter source has conflicting types reference and uri
	 * [ERROR] Search parameter location has conflicting types reference and uri
	 * [ERROR] Search parameter title has conflicting types string and token
	 * [ERROR] Search parameter manufacturer has conflicting types string and reference
	 * [ERROR] Search parameter address has conflicting types token and string
	 * [ERROR] Search parameter source has conflicting types reference and string
	 * [ERROR] Search parameter destination has conflicting types reference and string
	 * [ERROR] Search parameter responsible has conflicting types reference and string
	 * [ERROR] Search parameter value has conflicting types token and string
	 * [ERROR] Search parameter address has conflicting types token and string
	 * [ERROR] Search parameter address has conflicting types token and string
	 * [ERROR] Search parameter address has conflicting types token and string
	 * [ERROR] Search parameter address has conflicting types token and string
	 * [ERROR] Search parameter action has conflicting types reference and token
	 * [ERROR] Search parameter version has conflicting types token and string
	 * [ERROR] Search parameter address has conflicting types token and string
	 * [ERROR] Search parameter base has conflicting types reference and token
	 * [ERROR] Search parameter target has conflicting types reference and token
	 * [ERROR] Search parameter base has conflicting types reference and uri
	 * [ERROR] Search parameter contact has conflicting types string and token
	 * [ERROR] Search parameter substance has conflicting types token and reference
	 * [ERROR] Search parameter provider has conflicting types reference and token
	 * [ERROR] Search parameter system has conflicting types token and uri
	 * [ERROR] Search parameter reference has conflicting types reference and uri
	 * //@formatter:off
	 */
}
