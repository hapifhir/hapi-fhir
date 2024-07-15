package ca.uhn.fhir.util.bundle;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.TestUtil;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Claim;
import org.hl7.fhir.r4.model.Coverage;
import org.hl7.fhir.r4.model.ExplanationOfBenefit;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.UriType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static ca.uhn.fhir.util.BundleUtil.DIFFERENT_LINK_ERROR_MSG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hl7.fhir.r4.model.Bundle.HTTPVerb.DELETE;
import static org.hl7.fhir.r4.model.Bundle.HTTPVerb.GET;
import static org.hl7.fhir.r4.model.Bundle.HTTPVerb.POST;
import static org.hl7.fhir.r4.model.Bundle.HTTPVerb.PUT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class BundleUtilTest {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	public static final String PATIENT_REFERENCE = "Patient/123";


	@Nested
	public class TestGetLinkUrlOfType {

		@Test
		public void testGetNextExists() {
			Bundle b = new Bundle();
			b.getLinkOrCreate("prev").setUrl("http://bar");
			b.getLinkOrCreate("next").setUrl("http://foo");
			assertEquals("http://foo", BundleUtil.getLinkUrlOfType(ourCtx, b, "next"));
		}

		@Test
		public void testGetNextDoesntExist() {
			Bundle b = new Bundle();
			b.getLinkOrCreate("prev").setUrl("http://bar");
			assertNull(BundleUtil.getLinkUrlOfType(ourCtx, b, "next"));
		}

		@Nested
		public class TestGetPreviousLink {

			@Test
			public void testGetPrevious_exists_and_no_prev() {
				Bundle b = new Bundle();
				b.getLinkOrCreate(IBaseBundle.LINK_PREV).setUrl("http://bar");
				assertEquals("http://bar", BundleUtil.getLinkUrlOfType(ourCtx, b, IBaseBundle.LINK_PREV));
			}

			@Test
			public void testGetPrevious_exists_and_prev_also_and_equals() {
				Bundle b = new Bundle();
				b.getLinkOrCreate(IBaseBundle.LINK_PREV).setUrl("http://bar");
				b.getLinkOrCreate("prev").setUrl("http://bar");
				assertEquals("http://bar", BundleUtil.getLinkUrlOfType(ourCtx, b, IBaseBundle.LINK_PREV));
			}

			@Test
			public void testGetPrevious_exists_and_prev_also_and_different_must_throw() {
				Bundle b = new Bundle();
				b.getLinkOrCreate(IBaseBundle.LINK_PREV).setUrl("http://bar");
				b.getLinkOrCreate("prev").setUrl("http://foo");
				InternalErrorException thrown = assertThrows(InternalErrorException.class,
					() -> BundleUtil.getLinkUrlOfType(ourCtx, b, IBaseBundle.LINK_PREV));

				String expectedExceptionMsg = Msg.code(2368) + DIFFERENT_LINK_ERROR_MSG
					.replace("$PREVIOUS", "http://bar").replace("$PREV", "http://foo");
				assertEquals(expectedExceptionMsg, thrown.getMessage());
			}

			@Test
			public void testGetPrevious_doesnt_exist_neither_prev() {
				Bundle b = new Bundle();
				assertNull(BundleUtil.getLinkUrlOfType(ourCtx, b, IBaseBundle.LINK_PREV));
			}

			@Test
			public void testGetPrevious_doesnt_exist_but_prev_does() {
				Bundle b = new Bundle();
				b.getLinkOrCreate("prev").setUrl("http://bar");
				assertEquals("http://bar", BundleUtil.getLinkUrlOfType(ourCtx, b, IBaseBundle.LINK_PREV));
			}

			@Test
			public void testGetPrev_exists_and_no_previous() {
				Bundle b = new Bundle();
				b.getLinkOrCreate("prev").setUrl("http://bar");
				assertEquals("http://bar", BundleUtil.getLinkUrlOfType(ourCtx, b, "prev"));
			}

			@Test
			public void testGetPrev_exists_and_previous_also_and_equals() {
				Bundle b = new Bundle();
				b.getLinkOrCreate(IBaseBundle.LINK_PREV).setUrl("http://bar");
				b.getLinkOrCreate("prev").setUrl("http://bar");
				assertEquals("http://bar", BundleUtil.getLinkUrlOfType(ourCtx, b, "prev"));
			}

			@Test
			public void testGetPrev_exists_and_previous_also_and_different_must_throw() {
				Bundle b = new Bundle();
				b.getLinkOrCreate(IBaseBundle.LINK_PREV).setUrl("http://bar");
				b.getLinkOrCreate("prev").setUrl("http://foo");
				InternalErrorException thrown = assertThrows(InternalErrorException.class,
					() -> BundleUtil.getLinkUrlOfType(ourCtx, b, "prev"));

				String expectedExceptionMsg = Msg.code(2368) + DIFFERENT_LINK_ERROR_MSG
					.replace("$PREVIOUS", "http://bar").replace("$PREV", "http://foo");
				assertEquals(expectedExceptionMsg, thrown.getMessage());
			}

			@Test
			public void testGetPrev_doesnt_exist_neither_previous() {
				Bundle b = new Bundle();
				assertNull(BundleUtil.getLinkUrlOfType(ourCtx, b, "prev"));
			}

			@Test
			public void testGetPrev_doesnt_exist_but_previous_does() {
				Bundle b = new Bundle();
				b.getLinkOrCreate(IBaseBundle.LINK_PREV).setUrl("http://bar");
				assertEquals("http://bar", BundleUtil.getLinkUrlOfType(ourCtx, b, "prev"));
			}
		}
	}

	@Test
	public void testGetTotal() {
		Bundle b = new Bundle();
		b.setTotal(999);
		Integer total = BundleUtil.getTotal(ourCtx, b);
		assertNotNull(total);
		assertEquals(999, total.intValue());
	}

	@Test
	public void testGetTotalNull() {
		Bundle b = new Bundle();
		assertNull(BundleUtil.getTotal(ourCtx, b));
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
		assertThat(list).hasSize(5);
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

		assertThat(b.getEntry()).hasSize(4);

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
			fail();		} catch (IllegalStateException ignored) {

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

		assertThat(b.getEntry()).hasSize(7);

		List<Bundle.BundleEntryComponent> entry = b.getEntry();

		// DELETEs first
		assertEquals(DELETE, entry.get(0).getRequest().getMethod());
		assertEquals(DELETE, entry.get(1).getRequest().getMethod());
		// Then POSTs
		assertEquals(POST, entry.get(2).getRequest().getMethod());
		assertEquals(POST, entry.get(3).getRequest().getMethod());
		assertEquals(POST, entry.get(4).getRequest().getMethod());
		// Then PUTs
		assertEquals(PUT, entry.get(5).getRequest().getMethod());
		// Then GETs
		assertEquals(GET, entry.get(6).getRequest().getMethod());
	}

	@Test
	public void testBundleSortsCanHandlesDeletesThatContainNoResources() {
		Patient p = new Patient();
		p.setId(PATIENT_REFERENCE);
		BundleBuilder builder = new BundleBuilder(ourCtx);
		builder.addTransactionDeleteEntry(p);
		BundleUtil.sortEntriesIntoProcessingOrder(ourCtx, builder.getBundle());
	}

	@Test
	public void testBundleToSearchBundleEntryParts() {
		//Given
		String bundleString = """
			{
			  "resourceType": "Bundle",
			  "id": "bd194b7f-ac1e-429a-a206-ee2c470f23b5",
			  "meta": {
			    "lastUpdated": "2021-10-18T16:25:55.330-07:00"
			  },
			  "type": "searchset",
			  "total": 1,
			  "link": [
			    {
			      "relation": "self",
			      "url": "http://localhost:8000/Patient?_count=1&_id=pata&_revinclude=Condition%3Asubject%3APatient"
			    }
			  ],
			  "entry": [
			    {
			      "fullUrl": "http://localhost:8000/Patient/pata",
			      "resource": {
			        "resourceType": "Patient",
			        "id": "pata",
			        "meta": {
			          "versionId": "1",
			          "lastUpdated": "2021-10-18T16:25:48.954-07:00",
			          "source": "#rnEjIucr8LR6Ze3x"
			        },
			        "name": [
			          {
			            "family": "Simpson",
			            "given": [
			              "Homer",
			              "J"
			            ]
			          }
			        ]
			      },
			      "search": {
			        "mode": "match"
			      }
			    },
			    {
			      "fullUrl": "http://localhost:8000/Condition/1626",
			      "resource": {
			        "resourceType": "Condition",
			        "id": "1626",
			        "meta": {
			          "versionId": "1",
			          "lastUpdated": "2021-10-18T16:25:51.672-07:00",
			          "source": "#gSOcGAdA3acaaNq1"
			        },
			        "identifier": [
			          {
			            "system": "urn:hssc:musc:conditionid",
			            "value": "1064115000.1.5"
			          }
			        ],
			        "subject": {
			          "reference": "Patient/pata"
			        }
			      },
			      "search": {
			        "mode": "include"
			      }
			    }
			  ]
			}""";

		Bundle bundle = ourCtx.newJsonParser().parseResource(Bundle.class, bundleString);

		//When
		List<SearchBundleEntryParts> searchBundleEntryParts = BundleUtil.getSearchBundleEntryParts(ourCtx, bundle);

		//Then
		assertThat(searchBundleEntryParts).hasSize(2);
		assertEquals(BundleEntrySearchModeEnum.MATCH, searchBundleEntryParts.get(0).getSearchMode());
		assertThat(searchBundleEntryParts.get(0).getFullUrl()).contains("Patient/pata");
		assertNotNull(searchBundleEntryParts.get(0).getResource());
		assertEquals(BundleEntrySearchModeEnum.INCLUDE, searchBundleEntryParts.get(1).getSearchMode());
		assertThat(searchBundleEntryParts.get(1).getFullUrl()).contains("Condition/");
		assertNotNull(searchBundleEntryParts.get(1).getResource());
	}
	@Test
	public void testConvertingToSearchBundleEntryPartsRespectsMissingMode() {

		//Given
		String bundleString = """
			{
			  "resourceType": "Bundle",
			  "id": "bd194b7f-ac1e-429a-a206-ee2c470f23b5",
			  "type": "searchset",
			  "total": 1,
			  "link": [
			    {
			      "relation": "self",
			      "url": "http://localhost:8000/Condition?_count=1"
			    }
			  ],
			  "entry": [
			    {
			      "fullUrl": "http://localhost:8000/Condition/1626",
			      "resource": {
			        "resourceType": "Condition",
			        "id": "1626",
			        "identifier": [
			          {
			            "system": "urn:hssc:musc:conditionid",
			            "value": "1064115000.1.5"
			          }
			        ]
			      }
			    }
			  ]
			}""";
		Bundle bundle = ourCtx.newJsonParser().parseResource(Bundle.class, bundleString);

		//When
		List<SearchBundleEntryParts> searchBundleEntryParts = BundleUtil.getSearchBundleEntryParts(ourCtx, bundle);

		//Then
		assertThat(searchBundleEntryParts).hasSize(1);
		assertNull(searchBundleEntryParts.get(0).getSearchMode());
		assertThat(searchBundleEntryParts.get(0).getFullUrl()).contains("Condition/1626");
		assertNotNull(searchBundleEntryParts.get(0).getResource());
	}

	@Test
	public void testConvertingToSearchBundleEntryPartsRespectsOutcomeMode() {

		//Given
		String bundleString = """
			{
			  "resourceType": "Bundle",
			  "id": "bd194b7f-ac1e-429a-a206-ee2c470f23b5",
			  "type": "searchset",
			  "total": 1,
			  "link": [
			    {
			      "relation": "self",
			      "url": "http://localhost:8000/Condition?_count=1"
			    }
			  ],
			  "entry": [
			    {
			      "fullUrl": "http://localhost:8000/Condition/1626",
			      "resource": {
			        "resourceType": "Condition",
			        "id": "1626",
			        "identifier": [
			          {
			            "system": "urn:hssc:musc:conditionid",
			            "value": "1064115000.1.5"
			          }
			        ]
			      },
			      "search": {
			        "mode": "outcome"
			      }
			    }
			  ]
			}""";
		Bundle bundle = ourCtx.newJsonParser().parseResource(Bundle.class, bundleString);

		//When
		List<SearchBundleEntryParts> searchBundleEntryParts = BundleUtil.getSearchBundleEntryParts(ourCtx, bundle);

		//Then
		assertThat(searchBundleEntryParts).hasSize(1);
		assertEquals(BundleEntrySearchModeEnum.OUTCOME, searchBundleEntryParts.get(0).getSearchMode());
		assertThat(searchBundleEntryParts.get(0).getFullUrl()).contains("Condition/1626");
		assertNotNull(searchBundleEntryParts.get(0).getResource());
	}

	@Test
	public void testConvertingToSearchBundleEntryPartsReturnsScore() {

		//Given
		String bundleString = """
			{
			  "resourceType": "Bundle",
			  "id": "bd194b7f-ac1e-429a-a206-ee2c470f23b5",
			  "type": "searchset",
			  "total": 1,
			  "link": [
			    {
			      "relation": "self",
			      "url": "http://localhost:8000/Condition?_count=1"
			    }
			  ],
			  "entry": [
			    {
			      "fullUrl": "http://localhost:8000/Condition/1626",
			      "resource": {
			        "resourceType": "Condition",
			        "id": "1626",
			        "identifier": [
			          {
			            "system": "urn:hssc:musc:conditionid",
			            "value": "1064115000.1.5"
			          }
			        ]
			      },
			      "search": {
			        "mode": "match",
			        "score": 1
			      }
			    }
			  ]
			}""";
		Bundle bundle = ourCtx.newJsonParser().parseResource(Bundle.class, bundleString);

		//When
		List<SearchBundleEntryParts> searchBundleEntryParts = BundleUtil.getSearchBundleEntryParts(ourCtx, bundle);

		//Then
		assertThat(searchBundleEntryParts).hasSize(1);
		assertEquals(BundleEntrySearchModeEnum.MATCH, searchBundleEntryParts.get(0).getSearchMode());
		assertEquals(new BigDecimal(1), searchBundleEntryParts.get(0).getSearchScore());
		assertThat(searchBundleEntryParts.get(0).getFullUrl()).contains("Condition/1626");
		assertNotNull(searchBundleEntryParts.get(0).getResource());
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

		assertThat(b.getEntry()).hasSize(4);

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
		assertEquals(pat1.getId(), ((Bundle.BundleEntryComponent) bundleEntry).getResource().getIdElement().getValue());

		UriType testUri = new UriType("http://foo");
		bundleEntry = BundleUtil.createNewBundleEntryWithSingleField(ourCtx,"fullUrl", testUri);
		assertEquals(testUri.getValue(), ((Bundle.BundleEntryComponent) bundleEntry).getFullUrl());
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

	@Test
	public void testGetResourceByReferenceAndResourceTypeReturnsResourceIfFound() {
		// setup
		final Patient expected = withPatient("123");
		final Bundle bundle = withBundle(expected);
		final Reference reference = new Reference(PATIENT_REFERENCE);
		// execute
		final IBaseResource actual = BundleUtil.getResourceByReferenceAndResourceType(ourCtx, bundle, reference);
		// validate
		assertEquals(expected, actual);
	}

	@Test
	public void testGetResourceByReferenceAndResourceTypeReturnsNullIfResourceNotFound() {
		// setup
		final Patient patient = withPatient("ABC");
		final Bundle bundle = withBundle(patient);
		final Reference reference = new Reference(PATIENT_REFERENCE);
		// execute
		final IBaseResource actual = BundleUtil.getResourceByReferenceAndResourceType(ourCtx, bundle, reference);
		// validate
		assertNull(actual);
	}

	@ParameterizedTest
	@CsvSource({
		 // Actual BundleType            Expected BundleTypeEnum
		 "TRANSACTION,                   TRANSACTION",
		 "DOCUMENT,                      DOCUMENT",
		 "MESSAGE,                       MESSAGE",
		 "BATCHRESPONSE,                 BATCH_RESPONSE",
		 "TRANSACTIONRESPONSE,           TRANSACTION_RESPONSE",
		 "HISTORY,                       HISTORY",
		 "SEARCHSET,                     SEARCHSET",
		 "COLLECTION,                    COLLECTION"
	})
	public void testGetBundleTypeEnum_withKnownBundleTypes_returnsCorrectBundleTypeEnum(Bundle.BundleType theBundleType, BundleTypeEnum theExpectedBundleTypeEnum){
		Bundle bundle = new Bundle();
		bundle.setType(theBundleType);
		assertEquals(theExpectedBundleTypeEnum, BundleUtil.getBundleTypeEnum(ourCtx, bundle));
	}

	@Test
	public void testGetBundleTypeEnum_withNullBundleType_returnsNull(){
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.NULL);
		assertNull(BundleUtil.getBundleTypeEnum(ourCtx, bundle));
	}

	@Test
	public void testGetBundleTypeEnum_withNoBundleType_returnsNull(){
		Bundle bundle = new Bundle();
		assertNull(BundleUtil.getBundleTypeEnum(ourCtx, bundle));
	}

	@Test
	public void testConvertBundleIntoTransaction() {
		Bundle input = createBundleWithPatientAndObservation();

		Bundle output = BundleUtil.convertBundleIntoTransaction(ourCtx, input, null);
		assertEquals(Bundle.BundleType.TRANSACTION, output.getType());
		assertEquals("Patient/123", output.getEntry().get(0).getFullUrl());
		assertEquals("Patient/123", output.getEntry().get(0).getRequest().getUrl());
		assertEquals(Bundle.HTTPVerb.PUT, output.getEntry().get(0).getRequest().getMethod());
		assertTrue(((Patient) output.getEntry().get(0).getResource()).getActive());
		assertEquals("Observation/456", output.getEntry().get(1).getFullUrl());
		assertEquals("Observation/456", output.getEntry().get(1).getRequest().getUrl());
		assertEquals(Bundle.HTTPVerb.PUT, output.getEntry().get(1).getRequest().getMethod());
		assertEquals("Patient/123", ((Observation)output.getEntry().get(1).getResource()).getSubject().getReference());
		assertEquals(Observation.ObservationStatus.AMENDED, ((Observation)output.getEntry().get(1).getResource()).getStatus());
	}

	@Test
	public void testConvertBundleIntoTransaction_WithPrefix() {
		Bundle input = createBundleWithPatientAndObservation();

		Bundle output = BundleUtil.convertBundleIntoTransaction(ourCtx, input, "A");
		assertEquals(Bundle.BundleType.TRANSACTION, output.getType());
		assertEquals("Patient/A123", output.getEntry().get(0).getFullUrl());
		assertEquals("Patient/A123", output.getEntry().get(0).getRequest().getUrl());
		assertEquals(Bundle.HTTPVerb.PUT, output.getEntry().get(0).getRequest().getMethod());
		assertTrue(((Patient) output.getEntry().get(0).getResource()).getActive());
		assertEquals("Observation/A456", output.getEntry().get(1).getFullUrl());
		assertEquals("Observation/A456", output.getEntry().get(1).getRequest().getUrl());
		assertEquals(Bundle.HTTPVerb.PUT, output.getEntry().get(1).getRequest().getMethod());
		assertEquals("Patient/A123", ((Observation)output.getEntry().get(1).getResource()).getSubject().getReference());
		assertEquals(Observation.ObservationStatus.AMENDED, ((Observation)output.getEntry().get(1).getResource()).getStatus());
	}

	private static @Nonnull Bundle createBundleWithPatientAndObservation() {
		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.COLLECTION);
		Patient patient = new Patient();
		patient.setActive(true);
		patient.setId("123");
		input.addEntry().setResource(patient);
		Observation observation = new Observation();
		observation.setId("456");
		observation.setStatus(Observation.ObservationStatus.AMENDED);
		observation.setSubject(new Reference("Patient/123"));
		input.addEntry().setResource(observation);
		return input;
	}


	@Nonnull
	private static Bundle withBundle(Resource theResource) {
		final Bundle bundle = new Bundle();
		bundle.addEntry(withBundleEntryComponent(theResource));
		bundle.addEntry(withBundleEntryComponent(new Coverage()));
		bundle.addEntry(withBundleEntryComponent(new Claim()));
		return bundle;
	}

	@Nonnull
	private static Bundle.BundleEntryComponent withBundleEntryComponent(Resource theResource) {
		final Bundle.BundleEntryComponent bundleEntryComponent = new Bundle.BundleEntryComponent();
		bundleEntryComponent.setResource(theResource);
		return bundleEntryComponent;
	}

	@Nonnull
	private static Patient withPatient(@Nonnull String theResourceId) {
		final Patient patient = new Patient();
		patient.setId(theResourceId);
		return patient;
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
