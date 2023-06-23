package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
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
		final int tokenMaxLength = ResourceIndexedSearchParamToken.MAX_LENGTH;
		final String identifierValue = "Exclusive_Provider_Organization_(EPO)Exclusive_Provider_Organization(EPO)Exclusive_Provider_Organization(EPO)Exclusive_Provider_Organization(EPO)NICHOLAS_FITTANTE_ACT_FAMILY_01234567890_01234567890_01234567890_01234567890_01234567890_01234567890_01234567890_01234567890_01234567890_01234567890_01234567890";
		final Identifier identifier = new Identifier().setSystem("http://www.acme.org.au/units").setValue(identifierValue);
		final Organization organization = new Organization().addIdentifier(identifier);

		// execute
		myOrganizationDao.create(organization, mySrd);

		// verify token
		final ResourceIndexedSearchParamToken resultSearchTokens = myResourceIndexedSearchParamTokenDao.findAll().get(0);
		final String tokenValue = resultSearchTokens.getValue();
		final Long tokensHashValue = resultSearchTokens.getHashValue();

		final String expectedValue = StringUtil.truncate(identifierValue, tokenMaxLength);
		final long expectedHashValue = ResourceIndexedSearchParamToken.calculateHashValue(new PartitionSettings(), RequestPartitionId.defaultPartition(), "Organization", "identifier", identifierValue);

		assertEquals(expectedValue, tokenValue);
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
		final int tokenMaxLength = ResourceIndexedSearchParamToken.MAX_LENGTH;
		final String identifierSystem = "http://www.acme.org.au/units/Exclusive_Provider_Organization_(EPO)Exclusive_Provider_Organization(EPO)Exclusive_Provider_Organization(EPO)Exclusive_Provider_Organization(EPO)NICHOLAS_FITTANTE_ACT_FAMILY_01234567890_01234567890_01234567890_01234567890_01234567890_01234567890_01234567890_01234567890_01234567890_01234567890_01234567890";
		final Identifier identifier = new Identifier().setSystem(identifierSystem).setValue("1234");
		final Organization organization = new Organization().addIdentifier(identifier);

		// execute
		myOrganizationDao.create(organization, mySrd);

		// verify token
		final ResourceIndexedSearchParamToken resultSearchTokens = myResourceIndexedSearchParamTokenDao.findAll().get(0);
		final String tokenSystem = resultSearchTokens.getSystem();
		final Long tokensHashSystem = resultSearchTokens.getHashSystem();

		final String expectedValue = StringUtil.truncate(identifierSystem, tokenMaxLength);
		final long expectedHashValue = ResourceIndexedSearchParamToken.calculateHashValue(new PartitionSettings(), RequestPartitionId.defaultPartition(), "Organization", "identifier", identifierSystem);

		assertEquals(expectedValue, tokenSystem);
		assertEquals(expectedHashValue, tokensHashSystem);

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
