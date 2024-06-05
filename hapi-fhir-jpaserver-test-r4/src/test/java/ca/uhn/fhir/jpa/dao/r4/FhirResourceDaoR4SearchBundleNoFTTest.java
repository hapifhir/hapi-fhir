package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.config.TestHSearchAddInConfig;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestHSearchAddInConfig.NoFT.class})
public class FhirResourceDaoR4SearchBundleNoFTTest extends BaseJpaR4Test {
	@Test
	public void searchDocumentBundle_withLocalReference_returnsCorrectly() {
		createBundleSearchParameter("Bundle-composition-patient-identifier",
				Enumerations.SearchParamType.TOKEN,
				"composition.patient.identifier",
				"Bundle.entry[0].resource.as(Composition).subject.resolve().as(Patient).identifier");

		String patientId = "Patient/ABC";
		String patientUrl = "http://example.com/fhir/" + patientId;
		String identifierSystem = "http://foo";
		String identifierValue = "bar";

		Composition composition = new Composition();
		composition.setSubject(new Reference(patientUrl));

		Patient patient = new Patient();
		patient.setId(patientId);
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
	public void searchDocumentBundle_withPlaceholderReference_returnsCorrectly() {
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
	public void searchDocumentBundle_withExternalReference_returnsCorrectly() {
		String searchParamCode = "composition.subject";
		createBundleSearchParameter("Bundle-composition-patient",
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

	@Test
	public void transactionBundle_withFullyChainedLocalReference_referenceResolved() {
		String patientId = "urn:uuid:" + UUID.randomUUID();
		String identifierSystem = "http://foo";
		String identifierValue = "bar";

		Patient patient = new Patient();
		patient.addIdentifier().setSystem(identifierSystem).setValue(identifierValue);

		Observation observation = new Observation();
		observation.getSubject().setReference(patientId);
		observation.setStatus(Observation.ObservationStatus.FINAL);

		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);
		bundle.addEntry().setFullUrl(patientId).setResource(patient)
				.getRequest().setMethod(Bundle.HTTPVerb.PUT).setUrl("Patient?identifier=http://foo|bar");
		bundle.addEntry().setResource(observation)
				.getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("Observation");

		Bundle bundleOutCome = mySystemDao.transaction(mySrd, bundle);
		assertTrue(bundleOutCome.hasEntry());
		patient = myPatientDao.read(new IdType(bundleOutCome.getEntry().get(0).getResponse().getLocation()), mySrd);
		observation = myObservationDao.read(new IdType(bundleOutCome.getEntry().get(1).getResponse().getLocation()), mySrd);
		assertEquals(patient.getIdElement().toUnqualifiedVersionless().getValue(), observation.getSubject().getReference());
	}

	private void createBundleSearchParameter(String id, Enumerations.SearchParamType theType, String theCode, String theExpression) {
		SearchParameter sp = new SearchParameter()
				.setCode(theCode)
				.addBase("Bundle")
				.setType(theType)
				.setExpression(theExpression)
				.setXpathUsage(SearchParameter.XPathUsageType.NORMAL)
				.setStatus(Enumerations.PublicationStatus.ACTIVE);
		Extension upliftRefChain = sp.addExtension().setUrl(HapiExtensions.EXTENSION_SEARCHPARAM_UPLIFT_REFCHAIN);
		upliftRefChain.addExtension(HapiExtensions.EXTENSION_SEARCHPARAM_UPLIFT_REFCHAIN_PARAM_CODE, new CodeType("identifier"));
		sp.setId("SearchParameter/" + id);
		sp.setUrl("http://example.com/fhir/" + sp.getId());
		ourLog.info("SP: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(sp));
		IBaseResource resource = mySearchParameterDao.update(sp, mySrd).getResource();
		assertNotNull(resource);
	}

	private void verifySearchCompositionPatientReturnsBundle(String theIdentifierSystem, String theIdentifierValue, IIdType theBundleId) {
		final String systemAndValue = theIdentifierSystem + "|" + theIdentifierValue;
		verifySearchReturnsBundle(SearchParameterMap.newSynchronous("composition.patient.identifier", new TokenParam(theIdentifierValue)), theBundleId);
		verifySearchReturnsBundle(SearchParameterMap.newSynchronous("composition.patient.identifier", new TokenParam(theIdentifierSystem, theIdentifierValue)), theBundleId);
		verifySearchReturnsBundle(SearchParameterMap.newSynchronous("composition", new ReferenceParam("patient.identifier", theIdentifierValue)), theBundleId);
		verifySearchReturnsBundle(SearchParameterMap.newSynchronous("composition", new ReferenceParam("patient.identifier", systemAndValue)), theBundleId);
	}

	private void verifySearchReturnsBundle(SearchParameterMap theSearchParameterMap, IIdType theBundleId) {
		IBundleProvider searchOutcome = myBundleDao.search(theSearchParameterMap, mySrd);
		assertEquals(1, searchOutcome.size());
		assertEquals(theBundleId, searchOutcome.getAllResources().get(0).getIdElement());
	}
}
