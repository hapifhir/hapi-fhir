package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
import org.apache.commons.lang3.RandomStringUtils;
import org.eclipse.jetty.util.StringUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Organization;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FhirSystemDaoR4SearchTest extends BaseJpaR4SystemTest {

	@Test
	public void testSearchParameter_WithIdentifierValueUnder200Chars_ShouldSuccess() {
		// setup
		final String identifierValue = "identifierValue";
		final Organization organization = new Organization();
		final Identifier identifier = new Identifier();
		identifier.setSystem("http://www.acme.org.au/units");
		identifier.setValue(identifierValue);
		organization.addIdentifier(identifier);

		myOrganizationDao.create(organization, mySrd);

		final List<ResourceIndexedSearchParamToken> allSearchTokens = myResourceIndexedSearchParamTokenDao.findAll();
		assertTrue(allSearchTokens.stream().anyMatch(token -> identifierValue.equals(token.getValue())));

		final SearchParameterMap searchParameterMap = SearchParameterMap.newSynchronous(Observation.SP_IDENTIFIER, new TokenParam(identifierValue));

		final IBundleProvider bundle = myOrganizationDao.search(searchParameterMap, mySrd);
		final List<IBaseResource> allResources = bundle.getAllResources();

		assertEquals(1, allResources.size());

		final IBaseResource resource = allResources.get(0);
		assertTrue(resource instanceof Organization);

		final List<Identifier> identifiersFromResults = ((Organization) resource).getIdentifier();
		assertEquals(1, identifiersFromResults.size());
		assertEquals(identifierValue, identifiersFromResults.get(0).getValue());
	}

	@Test
	public void testSearchParameter_WithIdentifierValueUnder200Chars_ShouldHashBeforeTruncate() {
		// setup
		final String identifierValue = RandomStringUtils.randomAlphanumeric(ResourceIndexedSearchParamToken.MAX_LENGTH + 100);
		final Identifier identifier = new Identifier().setSystem("http://www.acme.org.au/units").setValue(identifierValue);
		final Organization organization = new Organization().addIdentifier(identifier);

		// execute
		myOrganizationDao.create(organization, mySrd);

		// verify token value
		final ResourceIndexedSearchParamToken resultSearchTokens = myResourceIndexedSearchParamTokenDao.findAll().get(0);
		final String tokenValue = resultSearchTokens.getValue();
		final String expectedValue = StringUtil.truncate(identifierValue, ResourceIndexedSearchParamToken.MAX_LENGTH);
		assertEquals(expectedValue, tokenValue);

		// verify hash
		final Long tokensHashValue = resultSearchTokens.getHashValue();
		final long expectedHashValue = ResourceIndexedSearchParamToken.calculateHashValue(new PartitionSettings(), RequestPartitionId.defaultPartition(), "Organization", "identifier", identifierValue);
		assertEquals(expectedHashValue, tokensHashValue);

		// verify resource
		final SearchParameterMap searchParameterMap = SearchParameterMap.newSynchronous(Observation.SP_IDENTIFIER, new TokenParam(identifierValue));
		final List<IBaseResource> allResources = myOrganizationDao.search(searchParameterMap, mySrd).getAllResources();
		final IBaseResource resource = allResources.get(0);
		assertTrue(resource instanceof Organization);

		final List<Identifier> identifiersFromResults = ((Organization) resource).getIdentifier();
		assertEquals(1, allResources.size());
		assertEquals(1, identifiersFromResults.size());
		assertEquals(identifierValue, identifiersFromResults.get(0).getValue());
	}

	@Test
	public void testSearchParameter_WithIdentifierSystemUnder200Chars_ShouldHashBeforeTruncate() {
		// setup
		final String identifierSystem = RandomStringUtils.randomAlphanumeric(ResourceIndexedSearchParamToken.MAX_LENGTH + 100);;
		final Identifier identifier = new Identifier().setSystem(identifierSystem).setValue("1234");
		final Organization organization = new Organization().addIdentifier(identifier);

		// execute
		myOrganizationDao.create(organization, mySrd);

		// verify token system
		final ResourceIndexedSearchParamToken resultSearchTokens = myResourceIndexedSearchParamTokenDao.findAll().get(0);
		final String tokenSystem = resultSearchTokens.getSystem();
		final String expectedSystem = StringUtil.truncate(identifierSystem, ResourceIndexedSearchParamToken.MAX_LENGTH);
		assertEquals(expectedSystem, tokenSystem);

		// verify hash
		final Long tokensHashSystem = resultSearchTokens.getHashSystem();
		final long expectedHashSystem = ResourceIndexedSearchParamToken.calculateHashValue(new PartitionSettings(), RequestPartitionId.defaultPartition(), "Organization", "identifier", identifierSystem);
		assertEquals(expectedHashSystem, tokensHashSystem);

		// verify resource
		final SearchParameterMap searchParameterMap = SearchParameterMap.newSynchronous(Observation.SP_IDENTIFIER, new TokenParam("1234"));
		final List<IBaseResource> allResources = myOrganizationDao.search(searchParameterMap, mySrd).getAllResources();
		final IBaseResource resource = allResources.get(0);
		assertTrue(resource instanceof Organization);

		final List<Identifier> identifiersFromResults = ((Organization) resource).getIdentifier();
		assertEquals(1, allResources.size());
		assertEquals(1, identifiersFromResults.size());
		assertEquals(identifierSystem, identifiersFromResults.get(0).getSystem());
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
