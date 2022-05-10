package ca.uhn.fhir.util.bundle;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.ExplanationOfBenefit;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.UriType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hl7.fhir.r4.model.Bundle.HTTPVerb.DELETE;
import static org.hl7.fhir.r4.model.Bundle.HTTPVerb.GET;
import static org.hl7.fhir.r4.model.Bundle.HTTPVerb.POST;
import static org.hl7.fhir.r4.model.Bundle.HTTPVerb.PUT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class BundleUtilTest {

	private static FhirContext ourCtx = FhirContext.forR4();

	@Test
	public void testGetLink() {
		Bundle b = new Bundle();
		b.getLinkOrCreate("prev").setUrl("http://bar");
		b.getLinkOrCreate("next").setUrl("http://foo");
		assertEquals("http://foo", BundleUtil.getLinkUrlOfType(ourCtx, b, "next"));
	}

	@Test
	public void testGetLinkDoesntExist() {
		Bundle b = new Bundle();
		b.getLinkOrCreate("prev").setUrl("http://bar");
		assertEquals(null, BundleUtil.getLinkUrlOfType(ourCtx, b, "next"));
	}

	@Test
	public void testGetTotal() {
		Bundle b = new Bundle();
		b.setTotal(999);
		assertEquals(999, BundleUtil.getTotal(ourCtx, b).intValue());
	}

	@Test
	public void testGetTotalNull() {
		Bundle b = new Bundle();
		assertEquals(null, BundleUtil.getTotal(ourCtx, b));
	}

	@Test
	public void testSetType() {
		Bundle b = new Bundle();
		BundleUtil.setBundleType(ourCtx, b, "transaction");
		assertEquals(Bundle.BundleType.TRANSACTION, b.getType());
		assertEquals("transaction", b.getTypeElement().getValueAsString());
	}


	@Test
	public void toListOfResourcesOfTypeTest() {
		Bundle bundle = new Bundle();
		for (int i = 0; i < 5; i++) {
			bundle.addEntry(new Bundle.BundleEntryComponent().setResource(new Patient()));
		}
		List<Patient> list = BundleUtil.toListOfResourcesOfType(ourCtx, bundle, Patient.class);
		assertEquals(5, list.size());
	}

	@Test
	public void testProcessEntriesSetRequestUrl() {
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);
		bundle.addEntry().getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("Patient");

		Consumer<ModifiableBundleEntry> consumer = e -> e.setFullUrl("http://hello/Patient/123");
		BundleUtil.processEntries(ourCtx, bundle, consumer);

		assertEquals("http://hello/Patient/123", bundle.getEntryFirstRep().getFullUrl());
	}

	@Test
	public void testProcessEntriesSetFullUrl() {
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);
		bundle.addEntry().getRequest().setMethod(Bundle.HTTPVerb.GET).setUrl("Observation");

		Consumer<ModifiableBundleEntry> consumer = e -> e.setRequestUrl(ourCtx, e.getRequestUrl() + "?foo=bar");
		BundleUtil.processEntries(ourCtx, bundle, consumer);
		assertEquals("Observation?foo=bar", bundle.getEntryFirstRep().getRequest().getUrl());
	}

	@Test
	public void testTopologicalTransactionSortForCreates() {

		Bundle b = new Bundle();
		Bundle.BundleEntryComponent bundleEntryComponent = b.addEntry();
		final Observation obs1 = new Observation();
		obs1.setStatus(Observation.ObservationStatus.FINAL);
		obs1.setSubject(new Reference("Patient/P1"));
		obs1.setValue(new Quantity(4));
		obs1.setId("Observation/O1");
		bundleEntryComponent.setResource(obs1);
		bundleEntryComponent.getRequest().setMethod(POST).setUrl("Observation");

		bundleEntryComponent = b.addEntry();
		final Observation obs2 = new Observation();
		obs2.setStatus(Observation.ObservationStatus.FINAL);
		obs2.setValue(new Quantity(4));
		obs2.setId("Observation/O2");
		bundleEntryComponent.setResource(obs2);
		bundleEntryComponent.getRequest().setMethod(POST).setUrl("Observation");

		Bundle.BundleEntryComponent patientComponent = b.addEntry();
		Patient pat1 = new Patient();
		pat1.setId("Patient/P1");
		pat1.setManagingOrganization(new Reference("Organization/Org1"));
		patientComponent.setResource(pat1);
		patientComponent.getRequest().setMethod(POST).setUrl("Patient");

		Bundle.BundleEntryComponent organizationComponent = b.addEntry();
		Organization org1 = new Organization();
		org1.setId("Organization/Org1");
		organizationComponent.setResource(org1);
		organizationComponent.getRequest().setMethod(POST).setUrl("Patient");

		BundleUtil.sortEntriesIntoProcessingOrder(ourCtx, b);

		assertThat(b.getEntry(), hasSize(4));

		List<Bundle.BundleEntryComponent> entry = b.getEntry();
		int observationIndex = getIndexOfEntryWithId("Observation/O1", b);
		int patientIndex = getIndexOfEntryWithId("Patient/P1", b);
		int organizationIndex = getIndexOfEntryWithId("Organization/Org1", b);

		assertTrue(organizationIndex < patientIndex);
		assertTrue(patientIndex < observationIndex);
	}

	@Test
	public void testTransactionSorterFailsOnCyclicReference() {
		Bundle b = new Bundle();
		Bundle.BundleEntryComponent bundleEntryComponent = b.addEntry();
		final Observation obs1 = new Observation();
		obs1.setStatus(Observation.ObservationStatus.FINAL);
		obs1.setSubject(new Reference("Patient/P1"));
		obs1.setValue(new Quantity(4));
		obs1.setId("Observation/O1/_history/1");
		obs1.setHasMember(Collections.singletonList(new Reference("Observation/O2")));
		bundleEntryComponent.setResource(obs1);
		bundleEntryComponent.getRequest().setMethod(POST).setUrl("Observation");

		bundleEntryComponent = b.addEntry();
		final Observation obs2 = new Observation();
		obs2.setStatus(Observation.ObservationStatus.FINAL);
		obs2.setValue(new Quantity(4));
		obs2.setId("Observation/O2/_history/1");
		//We use a random history version here to ensure cycles are counted without versions.
		obs2.setHasMember(Collections.singletonList(new Reference("Observation/O1/_history/300")));
		bundleEntryComponent.setResource(obs2);
		bundleEntryComponent.getRequest().setMethod(POST).setUrl("Observation");
		try {
			BundleUtil.sortEntriesIntoProcessingOrder(ourCtx, b);
			fail();
		} catch (IllegalStateException e ) {

		}
	}

	@Test
	public void testTransactionSortingReturnsOperationsInCorrectOrder() {

		Bundle b = new Bundle();

		//UPDATE patient
		Bundle.BundleEntryComponent patientUpdateComponent= b.addEntry();
		final Patient p2 = new Patient();
		p2.setId("Patient/P2");
		p2.getNameFirstRep().setFamily("Test!");
		patientUpdateComponent.setResource(p2);
		patientUpdateComponent.getRequest().setMethod(PUT).setUrl("Patient/P2");

		//CREATE observation
		Bundle.BundleEntryComponent bundleEntryComponent = b.addEntry();
		final Observation obs1 = new Observation();
		obs1.setStatus(Observation.ObservationStatus.FINAL);
		obs1.setSubject(new Reference("Patient/P1"));
		obs1.setValue(new Quantity(4));
		obs1.setId("Observation/O1");
		bundleEntryComponent.setResource(obs1);
		bundleEntryComponent.getRequest().setMethod(POST).setUrl("Observation");

		//DELETE medication
		Bundle.BundleEntryComponent medicationComponent= b.addEntry();
		final Medication med1 = new Medication();
		med1.setId("Medication/M1");
		medicationComponent.setResource(med1);
		medicationComponent.getRequest().setMethod(DELETE).setUrl("Medication");

		//GET medication
		Bundle.BundleEntryComponent searchComponent = b.addEntry();
		searchComponent.getRequest().setMethod(GET).setUrl("Medication?code=123");

		//CREATE patient
		Bundle.BundleEntryComponent patientComponent = b.addEntry();
		Patient pat1 = new Patient();
		pat1.setId("Patient/P1");
		pat1.setManagingOrganization(new Reference("Organization/Org1"));
		patientComponent.setResource(pat1);
		patientComponent.getRequest().setMethod(POST).setUrl("Patient");

		//CREATE organization
		Bundle.BundleEntryComponent organizationComponent = b.addEntry();
		Organization org1 = new Organization();
		org1.setId("Organization/Org1");
		organizationComponent.setResource(org1);
		organizationComponent.getRequest().setMethod(POST).setUrl("Organization");

		//DELETE ExplanationOfBenefit
		Bundle.BundleEntryComponent explanationOfBenefitComponent= b.addEntry();
		final ExplanationOfBenefit eob1 = new ExplanationOfBenefit();
		eob1.setId("ExplanationOfBenefit/E1");
		explanationOfBenefitComponent.setResource(eob1);
		explanationOfBenefitComponent.getRequest().setMethod(DELETE).setUrl("ExplanationOfBenefit");

		BundleUtil.sortEntriesIntoProcessingOrder(ourCtx, b);

		assertThat(b.getEntry(), hasSize(7));

		List<Bundle.BundleEntryComponent> entry = b.getEntry();

		// DELETEs first
		assertThat(entry.get(0).getRequest().getMethod(), is(equalTo(DELETE)));
		assertThat(entry.get(1).getRequest().getMethod(), is(equalTo(DELETE)));
		// Then POSTs
		assertThat(entry.get(2).getRequest().getMethod(), is(equalTo(POST)));
		assertThat(entry.get(3).getRequest().getMethod(), is(equalTo(POST)));
		assertThat(entry.get(4).getRequest().getMethod(), is(equalTo(POST)));
		// Then PUTs
		assertThat(entry.get(5).getRequest().getMethod(), is(equalTo(PUT)));
		// Then GETs
		assertThat(entry.get(6).getRequest().getMethod(), is(equalTo(GET)));
	}

	@Test
	public void testBundleSortsCanHandlesDeletesThatContainNoResources() {
		Patient p = new Patient();
		p.setId("Patient/123");
		BundleBuilder builder = new BundleBuilder(ourCtx);
		builder.addTransactionDeleteEntry(p);
		BundleUtil.sortEntriesIntoProcessingOrder(ourCtx, builder.getBundle());
	}

	@Test
	public void testBundleToSearchBundleEntryParts() {
		//Given
		String bundleString = "{\n" +
			"  \"resourceType\": \"Bundle\",\n" +
			"  \"id\": \"bd194b7f-ac1e-429a-a206-ee2c470f23b5\",\n" +
			"  \"meta\": {\n" +
			"    \"lastUpdated\": \"2021-10-18T16:25:55.330-07:00\"\n" +
			"  },\n" +
			"  \"type\": \"searchset\",\n" +
			"  \"total\": 1,\n" +
			"  \"link\": [\n" +
			"    {\n" +
			"      \"relation\": \"self\",\n" +
			"      \"url\": \"http://localhost:8000/Patient?_count=1&_id=pata&_revinclude=Condition%3Asubject%3APatient\"\n" +
			"    }\n" +
			"  ],\n" +
			"  \"entry\": [\n" +
			"    {\n" +
			"      \"fullUrl\": \"http://localhost:8000/Patient/pata\",\n" +
			"      \"resource\": {\n" +
			"        \"resourceType\": \"Patient\",\n" +
			"        \"id\": \"pata\",\n" +
			"        \"meta\": {\n" +
			"          \"versionId\": \"1\",\n" +
			"          \"lastUpdated\": \"2021-10-18T16:25:48.954-07:00\",\n" +
			"          \"source\": \"#rnEjIucr8LR6Ze3x\"\n" +
			"        },\n" +
			"        \"name\": [\n" +
			"          {\n" +
			"            \"family\": \"Simpson\",\n" +
			"            \"given\": [\n" +
			"              \"Homer\",\n" +
			"              \"J\"\n" +
			"            ]\n" +
			"          }\n" +
			"        ]\n" +
			"      },\n" +
			"      \"search\": {\n" +
			"        \"mode\": \"match\"\n" +
			"      }\n" +
			"    },\n" +
			"    {\n" +
			"      \"fullUrl\": \"http://localhost:8000/Condition/1626\",\n" +
			"      \"resource\": {\n" +
			"        \"resourceType\": \"Condition\",\n" +
			"        \"id\": \"1626\",\n" +
			"        \"meta\": {\n" +
			"          \"versionId\": \"1\",\n" +
			"          \"lastUpdated\": \"2021-10-18T16:25:51.672-07:00\",\n" +
			"          \"source\": \"#gSOcGAdA3acaaNq1\"\n" +
			"        },\n" +
			"        \"identifier\": [\n" +
			"          {\n" +
			"            \"system\": \"urn:hssc:musc:conditionid\",\n" +
			"            \"value\": \"1064115000.1.5\"\n" +
			"          }\n" +
			"        ],\n" +
			"        \"subject\": {\n" +
			"          \"reference\": \"Patient/pata\"\n" +
			"        }\n" +
			"      },\n" +
			"      \"search\": {\n" +
			"        \"mode\": \"include\"\n" +
			"      }\n" +
			"    }\n" +
			"  ]\n" +
			"}";

		Bundle bundle = ourCtx.newJsonParser().parseResource(Bundle.class, bundleString);

		//When
		List<SearchBundleEntryParts> searchBundleEntryParts = BundleUtil.getSearchBundleEntryParts(ourCtx, bundle);

		//Then
		assertThat(searchBundleEntryParts, hasSize(2));
		assertThat(searchBundleEntryParts.get(0).getSearchMode(), is(equalTo(BundleEntrySearchModeEnum.MATCH)));
		assertThat(searchBundleEntryParts.get(0).getFullUrl(), is(containsString("Patient/pata")));
		assertThat(searchBundleEntryParts.get(0).getResource(), is(notNullValue()));
		assertThat(searchBundleEntryParts.get(1).getSearchMode(), is(equalTo(BundleEntrySearchModeEnum.INCLUDE)));
		assertThat(searchBundleEntryParts.get(1).getFullUrl(), is(containsString("Condition/")));
		assertThat(searchBundleEntryParts.get(1).getResource(), is(notNullValue()));
	}

	@Test
	public void testTransactionSorterReturnsDeletesInCorrectProcessingOrder() {
		Bundle b = new Bundle();
		Bundle.BundleEntryComponent bundleEntryComponent = b.addEntry();
		final Observation obs1 = new Observation();
		obs1.setStatus(Observation.ObservationStatus.FINAL);
		obs1.setSubject(new Reference("Patient/P1"));
		obs1.setValue(new Quantity(4));
		obs1.setId("Observation/O1");
		bundleEntryComponent.setResource(obs1);
		bundleEntryComponent.getRequest().setMethod(DELETE).setUrl("Observation");

		bundleEntryComponent = b.addEntry();
		final Observation obs2 = new Observation();
		obs2.setStatus(Observation.ObservationStatus.FINAL);
		obs2.setValue(new Quantity(4));
		obs2.setId("Observation/O2");
		bundleEntryComponent.setResource(obs2);
		bundleEntryComponent.getRequest().setMethod(DELETE).setUrl("Observation");

		Bundle.BundleEntryComponent patientComponent = b.addEntry();
		Patient pat1 = new Patient();
		pat1.setId("Patient/P1");
		pat1.setManagingOrganization(new Reference("Organization/Org1"));
		patientComponent.setResource(pat1);
		patientComponent.getRequest().setMethod(DELETE).setUrl("Patient");

		Bundle.BundleEntryComponent organizationComponent = b.addEntry();
		Organization org1 = new Organization();
		org1.setId("Organization/Org1");
		organizationComponent.setResource(org1);
		organizationComponent.getRequest().setMethod(DELETE).setUrl("Organization");

		BundleUtil.sortEntriesIntoProcessingOrder(ourCtx, b);

		assertThat(b.getEntry(), hasSize(4));

		int observationIndex = getIndexOfEntryWithId("Observation/O1", b);
		int patientIndex = getIndexOfEntryWithId("Patient/P1", b);
		int organizationIndex = getIndexOfEntryWithId("Organization/Org1", b);

		assertTrue(patientIndex < organizationIndex);
		assertTrue(observationIndex < patientIndex);
	}

	@Test
	public void testCreateNewBundleEntryWithSingleField() {
		Patient pat1 = new Patient();
		pat1.setId("Patient/P1");
		IBase bundleEntry = BundleUtil.createNewBundleEntryWithSingleField(ourCtx,"resource", pat1);
		assertThat(((Bundle.BundleEntryComponent)bundleEntry).getResource().getIdElement().getValue(), is(equalTo(pat1.getId())));

		UriType testUri = new UriType("http://foo");
		bundleEntry = BundleUtil.createNewBundleEntryWithSingleField(ourCtx,"fullUrl", testUri);
		assertThat(((Bundle.BundleEntryComponent)bundleEntry).getFullUrl(), is(equalTo(testUri.getValue())));
	}

	private int getIndexOfEntryWithId(String theResourceId, Bundle theBundle) {
		List<Bundle.BundleEntryComponent> entries = theBundle.getEntry();
		for (int i = 0; i < entries.size(); i++) {
			String id = entries.get(i).getResource().getIdElement().toUnqualifiedVersionless().toString();
			if (id.equals(theResourceId)) {
				return i;
			}
		}
		fail("Didn't find resource with ID " + theResourceId);
		return -1;
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
