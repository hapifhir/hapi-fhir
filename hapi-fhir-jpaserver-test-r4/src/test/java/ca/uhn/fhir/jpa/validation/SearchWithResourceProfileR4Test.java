package ca.uhn.fhir.jpa.validation;

import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.param.UriParamQualifierEnum;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchWithResourceProfileR4Test extends BaseResourceProviderR4Test {
	private static final String myResourceType = "Patient";
	private final String myProfile = "http://example.org/fhir/StructureDefinition/TestPatient";

	@Test
	public void testSearchPatient_withProfileBelowCriteria_returnsAllMatches() {
		createProfile();
		IIdType id1 = createPatient(withProfile(myProfile));
		IIdType id2 = createPatient(withProfile(myProfile + "|1"));

		SearchParameterMap params = new SearchParameterMap();
		params.add("_profile", new UriParam(myProfile).setQualifier(UriParamQualifierEnum.BELOW));
		IBundleProvider bundleProvider = myPatientDao.search(params, mySrd);
		assertFalse(bundleProvider.isEmpty());
		assertThat(toUnqualifiedVersionlessIdValues(bundleProvider),
			containsInAnyOrder(id1.toUnqualifiedVersionless().getValue(), id2.toUnqualifiedVersionless().getValue()));
	}

	@Test
	public void testSearchPatient_withProfileExactCriteriaWithoutVersionAndPatientProfileWithoutVersion_returnsExactMatch() {
		createProfile();
		IIdType id1 = createPatient(withProfile(myProfile));
		IIdType id2 = createPatient(withProfile(myProfile));

		Bundle outcome = myClient.search().forResource(myResourceType)
			.where(new TokenClientParam("_profile").exactly().code(myProfile))
			.returnBundle(Bundle.class).execute();
		assertThat(toUnqualifiedVersionlessIdValues(outcome),
			containsInAnyOrder(id1.toUnqualifiedVersionless().getValue(), id2.toUnqualifiedVersionless().getValue()));
	}

	@Test
	public void testSearchPatient_withProfileExactCriteriaWithVersionAndPatientProfileWithVersion_returnsExactMatch() {
		createProfile();
		IIdType id1 = createPatient(withProfile(myProfile + "|1"));

		Bundle outcome = myClient.search().forResource(myResourceType)
			.where(new TokenClientParam("_profile").exactly().code(myProfile + "|1"))
			.returnBundle(Bundle.class).execute();
		assertThat(toUnqualifiedVersionlessIdValues(outcome),
			containsInAnyOrder(id1.toUnqualifiedVersionless().getValue()));
	}

	@Test
	public void testSearchPatient_withProfileExactCriteria_returnsNoMatch() {
		createProfile();
		createPatient(withProfile(myProfile + "|1"));

		Bundle outcome = myClient.search().forResource(myResourceType)
			.where(new TokenClientParam("_profile").exactly().code(myProfile))
			.returnBundle(Bundle.class).execute();
		assertTrue(outcome.getEntryFirstRep().isEmpty());
	}

	private void createProfile() {
		final String baseProfile = "http://hl7.org/fhir/StructureDefinition/Patient";
		final String profileId = "TestProfile";
		final String version = "1";
		final String rulePath = "Patient.identifier";

		StructureDefinition sd = new StructureDefinition()
			.setUrl(myProfile).setVersion(version)
			.setBaseDefinition(baseProfile)
			.setType(myResourceType)
			.setDerivation(StructureDefinition.TypeDerivationRule.CONSTRAINT);
		sd.setId(profileId);
		sd.getDifferential().addElement()
			.setPath(rulePath)
			.setMin(1)
			.setId(rulePath);

		DaoMethodOutcome outcome = myStructureDefinitionDao.update(sd, new SystemRequestDetails());
		assertNotNull(outcome.getResource());
	}
}
