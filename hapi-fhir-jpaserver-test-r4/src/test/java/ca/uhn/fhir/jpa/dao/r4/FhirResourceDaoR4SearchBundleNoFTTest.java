package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.config.TestHSearchAddInConfig;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestHSearchAddInConfig.NoFT.class})
public class FhirResourceDaoR4SearchBundleNoFTTest extends BaseJpaR4Test {
	@Test
	public void searchDocumentBundle_withLocalReferenceUsingId_returnsCorrectly() {
		createBundleSearchParameter("Bundle-composition-patient-identifier",
				Enumerations.SearchParamType.TOKEN,
				"composition.patient.identifier",
				"Bundle.entry[0].resource.as(Composition).subject.resolve().as(Patient).identifier");

		String patientId = "Patient/ABC";
		String identifierSystem = "http://foo";
		String identifierValue = "bar";

		String patientUrl = "http://example.com/fhir/" + patientId;

		Composition composition = new Composition();
		composition.setSubject(new Reference(patientUrl));

		Patient patient = new Patient();
		patient.setId(patientId);
		patient.addIdentifier().setSystem(identifierSystem).setValue(identifierValue);

		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.DOCUMENT);
		bundle.addEntry().setResource(composition);
		bundle.addEntry().setResource(patient);

		DaoMethodOutcome createOutcome = myBundleDao.create(bundle, mySrd);
		assertTrue(createOutcome.getCreated());
		IIdType bundleId = createOutcome.getId();

		verifySearchCompositionPatientReturnsBundle(identifierSystem, identifierValue, bundleId);
	}

	@Test
	public void searchDocumentBundle_withPlaceholderReferenceUsingFullUrl_returnsCorrectly() {
		createBundleSearchParameter("Bundle-composition-patient-identifier",
				Enumerations.SearchParamType.TOKEN,
				"composition.patient.identifier",
				"Bundle.entry[0].resource.as(Composition).subject.resolve().as(Patient).identifier");

		String patientUrl = "urn:uuid:" + UUID.randomUUID();
		String identifierSystem = "http://foo";
		String identifierValue = "bar";

		Composition composition = new Composition();
		composition.setSubject(new Reference(patientUrl));

		Patient patient = new Patient();
		patient.addIdentifier().setSystem(identifierSystem).setValue(identifierValue);

		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.DOCUMENT);
		bundle.addEntry().setResource(composition);
		bundle.addEntry().setFullUrl(patientUrl).setResource(patient);

		DaoMethodOutcome createOutcome = myBundleDao.create(bundle, mySrd);
		assertTrue(createOutcome.getCreated());
		IIdType bundleId = createOutcome.getId();

		verifySearchCompositionPatientReturnsBundle(identifierSystem, identifierValue, bundleId);
	}

	@Test
	public void searchDocumentBundle_withPlaceholderReferenceUsingId_returnsCorrectly() {
		createBundleSearchParameter("Bundle-composition-patient-identifier",
				Enumerations.SearchParamType.TOKEN,
				"composition.patient.identifier",
				"Bundle.entry[0].resource.as(Composition).subject.resolve().as(Patient).identifier");

		String patientId = "urn:uuid:" + UUID.randomUUID();
		String identifierSystem = "http://foo";
		String identifierValue = "bar";

		Composition composition = new Composition();
		composition.setSubject(new Reference(patientId));

		Patient patient = new Patient();
		patient.setId(patientId);
		patient.addIdentifier().setSystem(identifierSystem).setValue(identifierValue);

		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.DOCUMENT);
		bundle.addEntry().setResource(composition);
		bundle.addEntry().setResource(patient);

		DaoMethodOutcome createOutcome = myBundleDao.create(bundle, mySrd);
		assertTrue(createOutcome.getCreated());
		IIdType bundleId = createOutcome.getId();

		verifySearchCompositionPatientReturnsBundle(identifierSystem, identifierValue, bundleId);
	}

	@Test
	public void searchDocumentBundle_withExternalReference_returnsCorrectly() {
		String searchParamCode = "composition.subject";
		createBundleSearchParameter("Bundle-composition-subject",
				Enumerations.SearchParamType.REFERENCE,
				searchParamCode,
				"Bundle.entry[0].resource.as(Composition).subject");

		String patientId = "Patient/ABC";
		String identifierSystem = "http://foo";
		String identifierValue = "bar";

		Patient patient = new Patient();
		patient.setId(patientId);
		patient.addIdentifier().setSystem(identifierSystem).setValue(identifierValue);
		DaoMethodOutcome createPatientOutcome = myPatientDao.update(patient, mySrd);
		assertTrue(createPatientOutcome.getCreated());

		Composition composition = new Composition();
		composition.getSubject().setReference(createPatientOutcome.getId().toUnqualifiedVersionless().getValue());

		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.DOCUMENT);
		bundle.addEntry().setResource(composition);

		DaoMethodOutcome createBundleOutcome = myBundleDao.create(bundle, mySrd);
		assertTrue(createBundleOutcome.getCreated());
		IIdType bundleId = createBundleOutcome.getId();

		verifySearchReturnsBundle(SearchParameterMap.newSynchronous(searchParamCode, new ReferenceParam(patientId)), bundleId);
	}

	private void verifySearchCompositionPatientReturnsBundle(String theIdentifierSystem, String theIdentifierValue, IIdType theBundleId) {
		final String systemAndValue = theIdentifierSystem + "|" + theIdentifierValue;
		verifySearchReturnsBundle(SearchParameterMap.newSynchronous("composition.patient.identifier", new TokenParam(theIdentifierValue)), theBundleId);
		verifySearchReturnsBundle(SearchParameterMap.newSynchronous("composition.patient.identifier", new TokenParam(theIdentifierSystem, theIdentifierValue)), theBundleId);
		verifySearchReturnsBundle(SearchParameterMap.newSynchronous("composition", new ReferenceParam("patient.identifier", theIdentifierValue)), theBundleId);
		verifySearchReturnsBundle(SearchParameterMap.newSynchronous("composition", new ReferenceParam("patient.identifier", systemAndValue)), theBundleId);
	}

	@ParameterizedTest
	@CsvSource({
			"/Bundle?composition.patient.identifier=system|value-1&composition.patient.birthdate=1980-01-01, true, correct identifier correct birthdate",
			"/Bundle?composition.patient.birthdate=1980-01-01&composition.patient.identifier=system|value-1, true, correct birthdate correct identifier",
			"/Bundle?composition.patient.identifier=system|value-1&composition.patient.birthdate=2000-01-01, false, correct identifier incorrect birthdate",
			"/Bundle?composition.patient.birthdate=2000-01-01&composition.patient.identifier=system|value-1, false, incorrect birthdate correct identifier",
			"/Bundle?composition.patient.identifier=system|value-2&composition.patient.birthdate=1980-01-01, false, incorrect identifier correct birthdate",
			"/Bundle?composition.patient.birthdate=1980-01-01&composition.patient.identifier=system|value-2, false, correct birthdate incorrect identifier",
			"/Bundle?composition.patient.identifier=system|value-2&composition.patient.birthdate=2000-01-01, false, incorrect identifier incorrect birthdate",
			"/Bundle?composition.patient.birthdate=2000-01-01&composition.patient.identifier=system|value-2, false, incorrect birthdate incorrect identifier",
			// try sort by composition sp
			"/Bundle?composition.patient.identifier=system|value-1&_sort=composition.patient.birthdate, true, correct identifier sort by birthdate",

	})
	public void searchDocumentBundle_withExternalReferenceAndEntryCopy_returnsCorrectly(String theSearchUrl, boolean theShouldMatch, String theMessage) {
		createBundleSearchParameter("bundle-composition-patient-birthdate",
				Enumerations.SearchParamType.DATE,
				"composition.patient.birthdate",
				"Bundle.entry.resource.ofType(Patient).birthDate"
		);

		createBundleSearchParameter("bundle-composition-patient-identifier",
				Enumerations.SearchParamType.TOKEN,
				"composition.patient.identifier",
				"Bundle.entry.resource.ofType(Patient).identifier"
		);

		String identifierSystem = "system";
		String identifierValue = "value-1";
		String birthDateString = "1980-01-01";

		Patient patient = new Patient();
		patient.setBirthDate(Date.valueOf(birthDateString));
		patient.addIdentifier().setSystem(identifierSystem).setValue(identifierValue);

		DaoMethodOutcome createPatientOutcome = myPatientDao.create(patient, mySrd);
		assertTrue(createPatientOutcome.getCreated());

		Composition composition = new Composition();
		composition.setSubject(new Reference(createPatientOutcome.getId().getValue()));

		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.DOCUMENT);
		bundle.addEntry().setResource(composition);
		bundle.addEntry().setResource(patient);

		DaoMethodOutcome createBundleOutcome = myBundleDao.create(bundle, mySrd);
		assertTrue(createBundleOutcome.getCreated());
		IIdType bundleId = createBundleOutcome.getId();

		List<String> ids = myTestDaoSearch.searchForIds(theSearchUrl);
		if (theShouldMatch) {
			assertThat(ids).as(theMessage).containsExactlyInAnyOrder(bundleId.getIdPart());
		} else {
			assertThat(ids).as(theMessage).hasSize(0);
		}
	}

	private void createBundleSearchParameter(String id, Enumerations.SearchParamType theType, String theCode, String theExpression) {
		SearchParameter sp = new SearchParameter()
				.setCode(theCode)
				.addBase("Bundle")
				.setType(theType)
				.setExpression(theExpression)
				.setXpathUsage(SearchParameter.XPathUsageType.NORMAL)
				.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setId("SearchParameter/" + id);
		sp.setUrl("http://example.com/fhir/" + sp.getId());
		ourLog.info("SP: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(sp));
		IBaseResource resource = mySearchParameterDao.update(sp, mySrd).getResource();
		assertNotNull(resource);
	}

	private void verifySearchReturnsBundle(SearchParameterMap theSearchParameterMap, IIdType theBundleId) {
		IBundleProvider searchOutcome = myBundleDao.search(theSearchParameterMap, mySrd);
		assertEquals(1, searchOutcome.size());
		assertEquals(theBundleId, searchOutcome.getAllResources().get(0).getIdElement());
	}
}
