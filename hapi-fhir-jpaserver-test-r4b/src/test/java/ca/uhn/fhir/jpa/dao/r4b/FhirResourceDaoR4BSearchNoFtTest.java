package ca.uhn.fhir.jpa.dao.r4b;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.r4b.model.Bundle;
import org.hl7.fhir.r4b.model.Composition;
import org.hl7.fhir.r4b.model.Enumerations;
import org.hl7.fhir.r4b.model.IdType;
import org.hl7.fhir.r4b.model.Patient;
import org.hl7.fhir.r4b.model.Reference;
import org.hl7.fhir.r4b.model.SearchParameter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FhirResourceDaoR4BSearchNoFtTest extends BaseJpaR4BTest {

	/**
	 * Index for
	 * [base]/Bundle?composition.patient.identifier=foo
	 */
	@ParameterizedTest
	@ValueSource(strings = {"urn:uuid:5c34dc2c-9b5d-4ec1-b30b-3e2d4371508b", "Patient/ABC"})
	public void testCreateAndSearchForFullyChainedSearchParameter(String thePatientId) {
		// Setup 1

		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);

		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/Bundle-composition-patient-identifier");
		sp.setCode("composition.patient.identifier");
		sp.setName("composition.patient.identifier");
		sp.setUrl("http://example.org/SearchParameter/Bundle-composition-patient-identifier");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setExpression("Bundle.entry[0].resource.as(Composition).subject.resolve().as(Patient).identifier");
		sp.addBase("Bundle");
		ourLog.info("SP: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(sp));
		mySearchParameterDao.update(sp, mySrd);

		mySearchParamRegistry.forceRefresh();

		// Test 1

		Composition composition = new Composition();
		composition.setSubject(new Reference(thePatientId));

		Patient patient = new Patient();
		patient.setId(new IdType(thePatientId));
		patient.addIdentifier().setSystem("http://foo").setValue("bar");

		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.DOCUMENT);
		bundle.addEntry().setResource(composition);
		bundle.addEntry().setResource(patient);

		myBundleDao.create(bundle, mySrd);

		Bundle bundle2 = new Bundle();
		bundle2.setType(Bundle.BundleType.DOCUMENT);
		myBundleDao.create(bundle2, mySrd);

		// Verify 1
		runInTransaction(() -> {
			logAllTokenIndexes();

			List<String> params = myResourceIndexedSearchParamTokenDao
				.findAll()
				.stream()
				.filter(t -> t.getParamName().contains("."))
				.map(t -> t.getParamName() + " " + t.getSystem() + "|" + t.getValue())
				.toList();
			assertThat(params).as(params.toString()).containsExactlyInAnyOrder("composition.patient.identifier http://foo|bar");
		});

		// Test 2
		IBundleProvider outcome;

		SearchParameterMap map = SearchParameterMap
			.newSynchronous("composition.patient.identifier", new TokenParam("http://foo", "bar"));
		outcome = myBundleDao.search(map, mySrd);
		assertEquals(1, outcome.size());

		map = SearchParameterMap
			.newSynchronous("composition", new ReferenceParam("patient.identifier", "http://foo|bar"));
		outcome = myBundleDao.search(map, mySrd);
		assertEquals(1, outcome.size());
	}

}
