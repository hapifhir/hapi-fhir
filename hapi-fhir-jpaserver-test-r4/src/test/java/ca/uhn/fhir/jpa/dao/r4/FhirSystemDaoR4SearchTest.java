package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Organization;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

public class FhirSystemDaoR4SearchTest extends BaseJpaR4SystemTest {

	@Test
	public void testSearchParameterIdentifierWithUnder200Chars() {
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
	public void testSearchParameterIdentifierWithOver200Chars() {
		final String identifierValue = "Exclusive_Provider_Organization_(EPO)Exclusive_Provider_Organization(EPO)Exclusive_Provider_Organization(EPO)Exclusive_Provider_Organization(EPO)NICHOLAS_FITTANTE_ACT_FAMILY_ddddddddddddddddddddddddddddddd";

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
