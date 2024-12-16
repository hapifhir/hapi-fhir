package ca.uhn.fhir.jpa.provider.r5;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.SqlQuery;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.util.ExtensionConstants;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.CapabilityStatement;
import org.hl7.fhir.r5.model.CapabilityStatement.CapabilityStatementRestResourceComponent;
import org.hl7.fhir.r5.model.CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.DateTimeType;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Extension;
import org.hl7.fhir.r5.model.Identifier;
import org.hl7.fhir.r5.model.Observation;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Quantity;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class ServerR5Test extends BaseResourceProviderR5Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ServerR5Test.class);

	@Autowired
	private IFhirResourceDao<CapabilityStatement> myCapabilityStatementDao;

	@Test
	@Disabled
	public void testCapabilityStatementValidates() throws IOException {
		HttpGet get = new HttpGet(myServerBase + "/metadata?_pretty=true&_format=json");
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {
			assertEquals(200, resp.getStatusLine().getStatusCode());
			String respString = IOUtils.toString(resp.getEntity().getContent(), StandardCharsets.UTF_8);

			ourLog.debug(respString);

			CapabilityStatement cs = myFhirCtx.newJsonParser().parseResource(CapabilityStatement.class, respString);

			try {
				myCapabilityStatementDao.validate(cs, null, respString, EncodingEnum.JSON, null, null, null);
			} catch (PreconditionFailedException e) {
				ourLog.debug(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome()));
				fail();
			}
		}
	}


	/**
	 * See #519
	 */
	@Test
	public void saveIdParamOnlyAppearsOnce() throws IOException {
		HttpGet get = new HttpGet(myServerBase + "/metadata?_pretty=true&_format=xml");
		CloseableHttpResponse resp = ourHttpClient.execute(get);
		try {
			ourLog.info(resp.toString());
			assertEquals(200, resp.getStatusLine().getStatusCode());

			String respString = IOUtils.toString(resp.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.debug(respString);

			CapabilityStatement cs = myFhirCtx.newXmlParser().parseResource(CapabilityStatement.class, respString);

			for (CapabilityStatementRestResourceComponent nextResource : cs.getRest().get(0).getResource()) {
				ourLog.info("Testing resource: " + nextResource.getType());
				Set<String> sps = new HashSet<String>();
				for (CapabilityStatementRestResourceSearchParamComponent nextSp : nextResource.getSearchParam()) {
					if (sps.add(nextSp.getName()) == false) {
						fail("Duplicate search parameter " + nextSp.getName() + " for resource " + nextResource.getType());
					}
				}

				if (!sps.contains("_id")) {
					fail("No search parameter _id for resource " + nextResource.getType());
				}
			}
		} finally {
			IOUtils.closeQuietly(resp.getEntity().getContent());
		}
	}


	@Test
	public void testMetadataIncludesResourceCounts() {
		Patient p = new Patient();
		p.setActive(true);
		myClient.create().resource(p).execute();

		/*
		 * Initial fetch after a clear should return
		 * no results
		 */
		myResourceCountsCache.clear();

		CapabilityStatement capabilityStatement = myClient
			.capabilities()
			.ofType(CapabilityStatement.class)
			.execute();

		Extension patientCountExt = capabilityStatement
			.getRest()
			.get(0)
			.getResource()
			.stream()
			.filter(t -> t.getType().equals("Patient"))
			.findFirst()
			.orElseThrow(() -> new InternalErrorException("No patient"))
			.getExtensionByUrl(ExtensionConstants.CONF_RESOURCE_COUNT);
		assertNull(patientCountExt);

		/*
		 * Now run a background pass (the update
		 * method is called by the scheduler normally)
		 */
		myResourceCountsCache.update();

		capabilityStatement = myClient
			.capabilities()
			.ofType(CapabilityStatement.class)
			.execute();

		patientCountExt = capabilityStatement
			.getRest()
			.get(0)
			.getResource()
			.stream()
			.filter(t -> t.getType().equals("Patient"))
			.findFirst()
			.orElseThrow(() -> new InternalErrorException("No patient"))
			.getExtensionByUrl(ExtensionConstants.CONF_RESOURCE_COUNT);
		assertEquals("1", patientCountExt.getValueAsPrimitive().getValueAsString());

	}

	@Test
	public void updateOrCreate_batchesSearchUrlDelete() {
		int numOfObs = 10;
		int txSize = 5;
		List<SqlQuery> deleteQueries;
		myCaptureQueriesListener.clear();

		String prefix = "p1";

		// initial insert
		doUpdateOrCreate(prefix, numOfObs, txSize);

		// verify we have created the observations (sanity check)
		@SuppressWarnings("unchecked")
		IFhirResourceDao<Observation> obsDao = myDaoRegistry.getResourceDao("Observation");
		IBundleProvider result = obsDao.search(new SearchParameterMap().setLoadSynchronous(true), new SystemRequestDetails());
		assertFalse(result.isEmpty());

		// creates create the initial search urls, so we expect no deletes
		deleteQueries = myCaptureQueriesListener.getDeleteQueries();
		assertTrue(deleteQueries.isEmpty());
		myCaptureQueriesListener.clear();

		// update
		doUpdateOrCreate(prefix, numOfObs, txSize);

		// the searchURLs should be deleted now, so we expect some deletes
		// specifically, as many deletes as there were "transaction bundles" to process
		deleteQueries = myCaptureQueriesListener.getDeleteQueries();
		assertFalse(deleteQueries.isEmpty());
		assertEquals(numOfObs / txSize, deleteQueries.size());
		myCaptureQueriesListener.clear();
	}

	private void doUpdateOrCreate(String prefix, int numOfObs, int txSize) {
		for (int i = 0; i < numOfObs / txSize; i++) {
			Bundle bundle = new Bundle();
			bundle.setType(Bundle.BundleType.TRANSACTION);
			List<Bundle.BundleEntryComponent> bundleEntryComponents = createObservations(prefix, 10000 + i * txSize, txSize);
			bundle.setEntry(bundleEntryComponents);
			mySystemDao.transaction(new SystemRequestDetails(),
				bundle);
		}
	}

	private List<Bundle.BundleEntryComponent> createObservations(String prefix, int observationId, int num) {
		List<Bundle.BundleEntryComponent> list = new ArrayList<>();
		for (int i = 0; i < num; i++) {
			Bundle.BundleEntryComponent bundleEntryComponent = new Bundle.BundleEntryComponent();
			Observation obs = new Observation();
			List<Identifier> identifierList = new ArrayList<>();
			identifierList.add(new Identifier().setValue(prefix + observationId + i));
			obs.setIdentifier(identifierList);
			obs.setStatus(Enumerations.ObservationStatus.FINAL);
			Coding code = new Coding("http://loinc.org", "85354-9", "Blood pressure");
			obs.setCode(new CodeableConcept().addCoding(code));
			obs.setEffective(createDateTime("2020-01-01T12:00:00-05:00"));
			Coding code1 = new Coding("http://loinc.org", "8480-6", "Systolic blood pressure");
			List<Observation.ObservationComponentComponent> obsccList = new ArrayList<>();
			Observation.ObservationComponentComponent obsvC = new Observation.ObservationComponentComponent();
			CodeableConcept cc = new CodeableConcept().addCoding(code1);
			obsvC.setValue(cc);
			Quantity quantity = new Quantity();
			quantity.setUnit("mmHg");
			quantity.setValue(170);
			quantity.setSystem("http://unitsofmeasure.org");
			quantity.setCode("mm[Hg]");
			obsvC.setValue(quantity);
			obsccList.add(obsvC);

			Observation.ObservationComponentComponent obsvC1 = new Observation.ObservationComponentComponent();
			CodeableConcept cc1 = new CodeableConcept(new Coding("http://loinc.org", "8462-4", "Diastolic blood pressure"));
			Quantity quantity1 = new Quantity();
			quantity1.setValue(110);
			quantity1.setUnit("mmHg");
			quantity1.setSystem("http://unitsofmeasure.org");
			quantity1.setCode("mm[Hg]");
			obsvC1.setCode(cc1);
			obsvC1.setValue(quantity1);
			obsccList.add(obsvC1);

			bundleEntryComponent.setResource(obs);
			Bundle.BundleEntryRequestComponent bundleEntryRequestComponent = new Bundle.BundleEntryRequestComponent();
			bundleEntryRequestComponent.setMethod(Bundle.HTTPVerb.PUT);
			bundleEntryRequestComponent.setUrl("Observation?identifier=" + prefix + observationId + i);
			bundleEntryComponent.setRequest(bundleEntryRequestComponent);
			list.add(bundleEntryComponent);

		}
		return list;
	}

	private DateTimeType createDateTime(String theDateString) {
		return new DateTimeType(theDateString);
	}
}
