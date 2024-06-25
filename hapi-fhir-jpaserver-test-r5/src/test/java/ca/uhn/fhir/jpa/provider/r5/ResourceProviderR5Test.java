package ca.uhn.fhir.jpa.provider.r5;

import ca.uhn.fhir.batch2.jobs.reindex.ReindexAppCtx;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.client.interceptor.CapturingInterceptor;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r5.model.CarePlan;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.Condition;
import org.hl7.fhir.r5.model.DateTimeType;
import org.hl7.fhir.r5.model.Extension;
import org.hl7.fhir.r5.model.MedicationRequest;
import org.hl7.fhir.r5.model.MedicinalProductDefinition;
import org.hl7.fhir.r5.model.Observation;
import org.hl7.fhir.r5.model.Observation.ObservationComponentComponent;
import org.hl7.fhir.r5.model.Organization;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Quantity;
import org.hl7.fhir.r5.model.SearchParameter;
import org.hl7.fhir.r5.model.StringType;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.comparator.Comparators;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings("Duplicates")
public class ResourceProviderR5Test extends BaseResourceProviderR5Test {

	private static final Logger ourLog = LoggerFactory.getLogger(ResourceProviderR5Test.class);
	private CapturingInterceptor myCapturingInterceptor = new CapturingInterceptor();

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();

		myStorageSettings.setAllowMultipleDelete(new JpaStorageSettings().isAllowMultipleDelete());
		myStorageSettings.setAllowExternalReferences(new JpaStorageSettings().isAllowExternalReferences());
		myStorageSettings.setReuseCachedSearchResultsForMillis(new JpaStorageSettings().getReuseCachedSearchResultsForMillis());
		myStorageSettings.setCountSearchResultsUpTo(new JpaStorageSettings().getCountSearchResultsUpTo());
		myStorageSettings.setSearchPreFetchThresholds(new JpaStorageSettings().getSearchPreFetchThresholds());
		myStorageSettings.setAllowContainsSearches(new JpaStorageSettings().isAllowContainsSearches());

		myClient.unregisterInterceptor(myCapturingInterceptor);
	}

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();
		myFhirCtx.setParserErrorHandler(new StrictErrorHandler());

		myStorageSettings.setAllowMultipleDelete(true);
		myClient.registerInterceptor(myCapturingInterceptor);
		myStorageSettings.setSearchPreFetchThresholds(new JpaStorageSettings().getSearchPreFetchThresholds());
	}

	@Test
	public void testSearchWithContainsLowerCase() {
		myStorageSettings.setAllowContainsSearches(true);

		Patient pt1 = new Patient();
		pt1.addName().setFamily("Elizabeth");
		String pt1id = myPatientDao.create(pt1).getId().toUnqualifiedVersionless().getValue();

		Patient pt2 = new Patient();
		pt2.addName().setFamily("fghijk");
		String pt2id = myPatientDao.create(pt2).getId().toUnqualifiedVersionless().getValue();

		Patient pt3 = new Patient();
		pt3.addName().setFamily("zzzzz");
		myPatientDao.create(pt3).getId().toUnqualifiedVersionless().getValue();


		Bundle output = myClient
			.search()
			.forResource("Patient")
			.where(org.hl7.fhir.r4.model.Patient.NAME.contains().value("ZAB"))
			.returnBundle(Bundle.class)
			.execute();
		List<String> ids = output.getEntry().stream().map(t -> t.getResource().getIdElement().toUnqualifiedVersionless().getValue()).collect(Collectors.toList());
		assertThat(ids).containsExactlyInAnyOrder(pt1id);

		output = myClient
			.search()
			.forResource("Patient")
			.where(org.hl7.fhir.r4.model.Patient.NAME.contains().value("zab"))
			.returnBundle(Bundle.class)
			.execute();
		ids = output.getEntry().stream().map(t -> t.getResource().getIdElement().toUnqualifiedVersionless().getValue()).collect(Collectors.toList());
		assertThat(ids).containsExactlyInAnyOrder(pt1id);

	}

	@Test
	public void testErroredSearchIsNotReused() {
		Patient pt1 = new Patient();
		pt1.addName().setFamily("Hello");
		myPatientDao.create(pt1);

		// Perform the search
		Bundle response0 = myClient.search()
			.forResource("Patient")
			.where(org.hl7.fhir.r4.model.Patient.NAME.matches().value("Hello"))
			.returnBundle(Bundle.class)
			.execute();
		assertThat(response0.getEntry()).hasSize(1);

		// Perform the search again (should return the same)
		Bundle response1 = myClient.search()
			.forResource("Patient")
			.where(org.hl7.fhir.r4.model.Patient.NAME.matches().value("Hello"))
			.returnBundle(Bundle.class)
			.execute();
		assertThat(response1.getEntry()).hasSize(1);
		assertEquals(response0.getId(), response1.getId());

		// Pretend the search was errored out
		markSearchErrored();

		// Perform the search again (shouldn't return the errored out search)
		Bundle response3 = myClient.search()
			.forResource("Patient")
			.where(org.hl7.fhir.r4.model.Patient.NAME.matches().value("Hello"))
			.returnBundle(Bundle.class)
			.execute();
		assertThat(response3.getEntry()).hasSize(1);
		assertThat(response3.getId()).isNotEqualTo(response0.getId());

	}

	private void markSearchErrored() {
		while (true) {
			try {
				runInTransaction(() -> {
					assertEquals(1L, mySearchEntityDao.count());
					Search search = mySearchEntityDao.findAll().iterator().next();
					search.setStatus(SearchStatusEnum.FAILED);
					search.setFailureMessage("Some Failure Message");
					search.setFailureCode(501);
					mySearchEntityDao.save(search);
				});
				break;
			} catch (ResourceVersionConflictException e) {
				ourLog.warn("Conflict while updating search: " + e);
				continue;
			}
		}
	}

	@Test
	public void testErroredSearchReturnsAppropriateResponse() {
		Patient pt1 = new Patient();
		pt1.addName().setFamily("Hello");
		myPatientDao.create(pt1);

		Patient pt2 = new Patient();
		pt2.addName().setFamily("Hello");
		myPatientDao.create(pt2);

		// Perform a search for the first page
		Bundle response0 = myClient.search()
			.forResource("Patient")
			.where(org.hl7.fhir.r4.model.Patient.NAME.matches().value("Hello"))
			.returnBundle(Bundle.class)
			.count(1)
			.execute();
		assertThat(response0.getEntry()).hasSize(1);

		// Make sure it works for now
		myClient.loadPage().next(response0).execute();

		// Pretend the search was errored out
		markSearchErrored();

		// Request the second page
		try {
			myClient.loadPage().next(response0).execute();
		} catch (NotImplementedOperationException e) {
			assertEquals(501, e.getStatusCode());
			assertThat(e.getMessage()).contains("Some Failure Message");
		}
	}

	@Test
	public void searchForNewerResources_fullTextSearchWithFilterAndCount_shouldReturnAccurateResults() {
		IParser parser = myFhirContext.newJsonParser();
		int count = 10;

		boolean presetFilterParameterEnabled = myStorageSettings.isFilterParameterEnabled();
		boolean presetAdvancedHSearchIndexing = myStorageSettings.isAdvancedHSearchIndexing();

		try {
			// fullTextSearch means Advanced Hibernate Search
			myStorageSettings.setFilterParameterEnabled(true);
			myStorageSettings.setAdvancedHSearchIndexing(true);

			// create custom search parameters - the _filter and _include are needed
			{
				@SuppressWarnings("unchecked")
				IFhirResourceDao<SearchParameter> spDao = myDaoRegistry.getResourceDao("SearchParameter");
				SearchParameter sp;

				@Language("JSON")
				String includeParam = """
						{
						  "resourceType": "SearchParameter",
						  "id": "9905463e-e817-4db0-9a3e-ff6aa3427848",
						  "meta": {
						    "versionId": "2",
						    "lastUpdated": "2024-03-28T12:53:57.874+00:00",
						    "source": "#7b34a4bfa42fe3ae"
						  },
						  "title": "Medicinal Product Manfacturer",
						  "status": "active",
						  "publisher": "MOH-IDMS",
						  "code": "productmanufacturer",
						  "base": [
						    "MedicinalProductDefinition"
						  ],
						  "type": "reference",
						  "expression": "MedicinalProductDefinition.operation.organization"
						}
					""";
				sp = parser.parseResource(SearchParameter.class, includeParam);
				spDao.create(sp, new SystemRequestDetails());
				sp = null;
				@Language("JSON")
				String filterParam = """
						{
						  "resourceType": "SearchParameter",
						  "id": "SEARCH-PARAMETER-MedicinalProductDefinition-SearchableString",
						  "meta": {
						    "versionId": "2",
						    "lastUpdated": "2024-03-27T19:20:25.200+00:00",
						    "source": "#384dd6bccaeafa6c"
						  },
						  "url": "https://health.gov.on.ca/idms/fhir/SearchParameter/MedicinalProductDefinition-SearchableString",
						  "version": "1.0.0",
						  "name": "MedicinalProductDefinitionSearchableString",
						  "status": "active",
						  "publisher": "MOH-IDMS",
						  "description": "Search Parameter for the MedicinalProductDefinition Searchable String Extension",
						  "code": "MedicinalProductDefinitionSearchableString",
						  "base": [
						    "MedicinalProductDefinition"
						  ],
						  "type": "string",
						  "expression": "MedicinalProductDefinition.extension('https://health.gov.on.ca/idms/fhir/StructureDefinition/SearchableExtraString')",
						  "target": [
						    "MedicinalProductDefinition"
						  ]
						}
					""";
				sp = parser.parseResource(SearchParameter.class, filterParam);
				spDao.create(sp, new SystemRequestDetails());
			}
			// create MedicinalProductDefinitions
			MedicinalProductDefinition mdr;
			{
				@Language("JSON")
				String mpdstr = """
					{
					                "resourceType": "MedicinalProductDefinition",
					                "id": "36fb418b-4b1f-414c-bbb1-731bc8744b93",
					                "meta": {
					                    "versionId": "17",
					                    "lastUpdated": "2024-06-10T16:52:23.907+00:00",
					                    "source": "#3a309416d5f52c5b",
					                    "profile": [
					                        "https://health.gov.on.ca/idms/fhir/StructureDefinition/IDMS_MedicinalProductDefinition"
					                    ]
					                },
					                "extension": [
					                    {
					                        "url": "https://health.gov.on.ca/idms/fhir/StructureDefinition/SearchableExtraString",
					                        "valueString": "zahidbrand0610-2up|genupuu|qwewqe2 111|11111115|DF other des|Biologic|Oncology|Private Label"
					                    }
					                ],
					                "status": {
					                    "coding": [
					                        {
					                            "system": "http://hl7.org/fhir/ValueSet/publication-status",
					                            "code": "active",
					                            "display": "Active"
					                        }
					                    ]
					                },
					                "name": [
					                    {
					                        "productName": "zahidbrand0610-2up"
					                    }
					                ]
					            }
					""";
				mdr = parser.parseResource(MedicinalProductDefinition.class, mpdstr);
			}
			IFhirResourceDao<MedicinalProductDefinition> mdrdao = myDaoRegistry.getResourceDao(MedicinalProductDefinition.class);

			/*
			 * We actually want a bunch of non-matching resources in the db
			 * that won't match the filter before we get to the one that will.
			 *
			 * To this end, we're going to insert more than we plan
			 * on retrieving to ensure the _filter is being used in both the
			 * count query and the actual db hit
			 */
			List<MedicinalProductDefinition.MedicinalProductDefinitionNameComponent> productNames = mdr.getName();
			mdr.setName(null);
			List<Extension> extensions = mdr.getExtension();
			mdr.setExtension(null);
			// we need at least 10 of these; 20 should be good
			for (int i = 0; i < 2 * count; i++) {
				mdr.addName(new MedicinalProductDefinition.MedicinalProductDefinitionNameComponent("Product " + i));
				mdr.addExtension()
					.setUrl("https://health.gov.on.ca/idms/fhir/StructureDefinition/SearchableExtraString")
					.setValue(new StringType("Non-matching string " + i));
				mdrdao.create(mdr, new SystemRequestDetails());
			}
			mdr.setName(productNames);
			mdr.setExtension(extensions);
			mdrdao.create(mdr, new SystemRequestDetails());

			// do a reindex
			ReindexJobParameters jobParameters = new ReindexJobParameters();
			jobParameters.setRequestPartitionId(RequestPartitionId.allPartitions());
			JobInstanceStartRequest request = new JobInstanceStartRequest();
			request.setJobDefinitionId(ReindexAppCtx.JOB_REINDEX);
			request.setParameters(jobParameters);
			Batch2JobStartResponse response = myJobCoordinator.startInstance(new SystemRequestDetails(), request);

			myBatch2JobHelper.awaitJobCompletion(response);

			// query like:
			// MedicinalProductDefinition?_getpagesoffset=0&_count=10&_total=accurate&_sort:asc=name&status=active&_include=MedicinalProductDefinition:productmanufacturer&_filter=MedicinalProductDefinitionSearchableString%20co%20%22zah%22
			SearchParameterMap map = new SearchParameterMap();
			map.setCount(10);
			map.setSearchTotalMode(SearchTotalModeEnum.ACCURATE);
			map.setSort(new SortSpec().setOrder(SortOrderEnum.ASC).setParamName("name"));
			map.setIncludes(Set.of(
				new Include("MedicinalProductDefinition:productmanufacturer")
			));
			map.add("_filter", new StringParam("MedicinalProductDefinitionSearchableString co \"zah\""));

			// test
			IBundleProvider result = mdrdao.search(map, new SystemRequestDetails());

			// validate
			// we expect to find our 1 matching resource
			assertEquals(1, result.getAllResources().size());
			assertNotNull(result.size());
			assertEquals(1, result.size());
		} finally {
			// reset values
			myStorageSettings.setFilterParameterEnabled(presetFilterParameterEnabled);
			myStorageSettings.setAdvancedHSearchIndexing(presetAdvancedHSearchIndexing);
		}
	}

	@Test
	public void testValidateGeneratedCapabilityStatement() throws IOException {

		String input;
		HttpGet get = new HttpGet(myServerBase + "/metadata?_format=json");
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {
			assertEquals(200, resp.getStatusLine().getStatusCode());
			input = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(input);
		}


		HttpPost post = new HttpPost(myServerBase + "/CapabilityStatement/$validate?_pretty=true");
		post.setEntity(new StringEntity(input, ContentType.APPLICATION_JSON));

		try (CloseableHttpResponse resp = ourHttpClient.execute(post)) {
			String respString = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);
			ourLog.debug(respString);
			assertEquals(200, resp.getStatusLine().getStatusCode());

			// As of 2023-01-26, the above line was restored.
			// As of 2021-12-28, the R5 structures return a version string that isn't
			// actually in the fhirVersion ValueSet. If this stops being the case this
			// test will fail and the line above should be restored

		}
	}

	@Test
	public void testDateNowSyntax() {
		Observation observation = new Observation();
		observation.setEffective(new DateTimeType("1965-08-09"));
		IIdType oid = myObservationDao.create(observation).getId().toUnqualified();
		String nowParam = UrlUtil.escapeUrlParam("%now");
		Bundle output = myClient
			.search()
			.byUrl("Observation?date=lt" + nowParam)
			.returnBundle(Bundle.class)
			.execute();
		List<IIdType> ids = output.getEntry().stream().map(t -> t.getResource().getIdElement().toUnqualified()).collect(Collectors.toList());
		assertThat(ids).containsExactlyInAnyOrder(oid);
	}


	@Test
	public void testCount0() {
		Observation observation = new Observation();
		observation.setEffective(new DateTimeType("1965-08-09"));
		myObservationDao.create(observation).getId().toUnqualified();

		observation = new Observation();
		observation.setEffective(new DateTimeType("1965-08-10"));
		myObservationDao.create(observation).getId().toUnqualified();

		myCaptureQueriesListener.clear();
		Bundle output = myClient
			.search()
			.byUrl("Observation?_count=0")
			.returnBundle(Bundle.class)
			.execute();
		ourLog.debug("Output: {}", myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		myCaptureQueriesListener.logSelectQueries();

		assertEquals(2, output.getTotal());
		assertThat(output.getEntry()).isEmpty();
	}

	@Test
	public void testSearchWithCompositeSort() throws IOException {

		IIdType pid0;
		IIdType oid1;
		IIdType oid2;
		IIdType oid3;
		IIdType oid4;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester").addGiven("Joe");
			pid0 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);

			ObservationComponentComponent comp = obs.addComponent();
			CodeableConcept cc = new CodeableConcept();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
			comp.setCode(cc);
			comp.setValue(new Quantity().setValue(200));

			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.debug("Observation: \n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);

			ObservationComponentComponent comp = obs.addComponent();
			CodeableConcept cc = new CodeableConcept();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
			comp.setCode(cc);
			comp.setValue(new Quantity().setValue(300));

			oid2 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.debug("Observation: \n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);

			ObservationComponentComponent comp = obs.addComponent();
			CodeableConcept cc = new CodeableConcept();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
			comp.setCode(cc);
			comp.setValue(new Quantity().setValue(150));

			oid3 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.debug("Observation: \n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);

			ObservationComponentComponent comp = obs.addComponent();
			CodeableConcept cc = new CodeableConcept();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
			comp.setCode(cc);
			comp.setValue(new Quantity().setValue(250));
			oid4 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.debug("Observation: \n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		String uri = myServerBase + "/Observation?_sort=combo-code-value-quantity";
		Bundle found;

		HttpGet get = new HttpGet(uri);
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {
			String output = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);
			found = myFhirCtx.newXmlParser().parseResource(Bundle.class, output);
		}

		ourLog.debug("Bundle: \n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(found));

		List<IIdType> list = toUnqualifiedVersionlessIds(found);
		assertThat(found.getEntry()).hasSize(4);
		assertEquals(oid3, list.get(0));
		assertEquals(oid1, list.get(1));
		assertEquals(oid4, list.get(2));
		assertEquals(oid2, list.get(3));
	}

	@Test
	public void testEverythingPatientInstanceWithTypeParameter() {
		String methodName = "testEverythingPatientInstanceWithTypeParameter";

		//Patient 1 stuff.
		IIdType o1Id = createOrganization(methodName, "1");
		IIdType p1Id = createPatientWithIndexAtOrganization(methodName, "1", o1Id);
		IIdType c1Id = createConditionForPatient(methodName, "1", p1Id);
		IIdType obs1Id = createObservationForPatient(p1Id, "1");
		IIdType m1Id = createMedicationRequestForPatient(p1Id, "1");

		//Test for only one patient
		Parameters parameters = new Parameters();
		parameters.addParameter("_type", "Condition, Observation");

		myCaptureQueriesListener.clear();

		Parameters output = myClient.operation().onInstance(p1Id).named("everything").withParameters(parameters).execute();
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		Bundle b = (Bundle) output.getParameter().get(0).getResource();

		myCaptureQueriesListener.logSelectQueries();

		assertEquals(Bundle.BundleType.SEARCHSET, b.getType());
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);

		assertThat(ids).containsExactlyInAnyOrder(p1Id, c1Id, obs1Id);
		assertThat(ids).doesNotContain(o1Id);
		assertThat(ids).doesNotContain(m1Id);
	}

	@Test
	public void testEverythingPatientTypeWithTypeParameter() {
		String methodName = "testEverythingPatientTypeWithTypeParameter";

		//Patient 1 stuff.
		IIdType o1Id = createOrganization(methodName, "1");
		IIdType p1Id = createPatientWithIndexAtOrganization(methodName, "1", o1Id);
		IIdType c1Id = createConditionForPatient(methodName, "1", p1Id);
		IIdType obs1Id = createObservationForPatient(p1Id, "1");
		IIdType m1Id = createMedicationRequestForPatient(p1Id, "1");

		//Test for only one patient
		Parameters parameters = new Parameters();
		parameters.addParameter("_type", "Condition, Observation");

		myCaptureQueriesListener.clear();

		Parameters output = myClient.operation().onType(Patient.class).named("everything").withParameters(parameters).execute();
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		Bundle b = (Bundle) output.getParameter().get(0).getResource();

		myCaptureQueriesListener.logSelectQueries();

		assertEquals(Bundle.BundleType.SEARCHSET, b.getType());
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);

		assertThat(ids).containsExactlyInAnyOrder(p1Id, c1Id, obs1Id);
		assertThat(ids).doesNotContain(o1Id);
		assertThat(ids).doesNotContain(m1Id);
	}

	@Test
	public void testEverythingPatientTypeWithTypeAndIdParameter() {
		String methodName = "testEverythingPatientTypeWithTypeAndIdParameter";

		//Patient 1 stuff.
		IIdType o1Id = createOrganization(methodName, "1");
		IIdType p1Id = createPatientWithIndexAtOrganization(methodName, "1", o1Id);
		IIdType c1Id = createConditionForPatient(methodName, "1", p1Id);
		IIdType obs1Id = createObservationForPatient(p1Id, "1");
		IIdType m1Id = createMedicationRequestForPatient(p1Id, "1");

		//Patient 2 stuff.
		IIdType o2Id = createOrganization(methodName, "2");
		IIdType p2Id = createPatientWithIndexAtOrganization(methodName, "2", o2Id);
		IIdType c2Id = createConditionForPatient(methodName, "2", p2Id);
		IIdType obs2Id = createObservationForPatient(p2Id, "2");
		IIdType m2Id = createMedicationRequestForPatient(p2Id, "2");

		//Test for only patient 1
		Parameters parameters = new Parameters();
		parameters.addParameter("_type", "Condition, Observation");
		parameters.addParameter("_id", p1Id.getIdPart());

		myCaptureQueriesListener.clear();

		Parameters output = myClient.operation().onType(Patient.class).named("everything").withParameters(parameters).execute();
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		Bundle b = (Bundle) output.getParameter().get(0).getResource();

		myCaptureQueriesListener.logSelectQueries();

		assertEquals(Bundle.BundleType.SEARCHSET, b.getType());
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);

		assertThat(ids).containsExactlyInAnyOrder(p1Id, c1Id, obs1Id);
		assertThat(ids).doesNotContain(o1Id);
		assertThat(ids).doesNotContain(m1Id);
		assertThat(ids).doesNotContain(p2Id);
		assertThat(ids).doesNotContain(o2Id);
	}


	@Test
	void testTransactionBundleEntryUri() {
		CarePlan carePlan = new CarePlan();
		carePlan.getText().setDivAsString("A CarePlan");
		carePlan.setId("ACarePlan");
		myClient.create().resource(carePlan).execute();

		// GET CarePlans from server
		Bundle bundle = myClient.search()
			.byUrl(myServerBase + "/CarePlan")
			.returnBundle(Bundle.class).execute();

		// Create and populate list of CarePlans
		List<CarePlan> carePlans = new ArrayList<>();
		bundle.getEntry().forEach(entry -> carePlans.add((CarePlan) entry.getResource()));

		// Post CarePlans should not get: HAPI-2006: Unable to perform PUT, URL provided is invalid...
		myClient.transaction().withResources(carePlans).execute();
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testHistoryPaging(boolean theTypeLevel) {
		// Setup
		BundleBuilder bb = new BundleBuilder(myFhirContext);
		List<String> expectedIdentifiers = new ArrayList<>();
		for (int i = 0; i < 500; i++) {
			Patient p = new Patient();
			String identifier = leftPad(Integer.toString(i), 4, '0');
			expectedIdentifiers.add(identifier);
			p.addIdentifier().setValue(identifier);
			bb.addTransactionCreateEntry(p);
		}

		ourLog.info("Starting transaction with {} entries...", expectedIdentifiers.size());
		mySystemDao.transaction(mySrd, bb.getBundleTyped());

		// Test
		ourLog.info("Loading type history, expecting identifiers from {} to {}...", expectedIdentifiers.get(0), expectedIdentifiers.get(expectedIdentifiers.size() - 1));
		List<String> actualIdentifiers = new ArrayList<>();
		Bundle historyBundle;
		if (theTypeLevel) {
			historyBundle = myClient.history().onType(Patient.class).returnBundle(Bundle.class).execute();
		} else {
			historyBundle = myClient.history().onServer().returnBundle(Bundle.class).execute();
		}
        while (true) {
			historyBundle
				.getEntry()
				.stream()
				.map(t -> (Patient) t.getResource())
				.map(t -> t.getIdentifierFirstRep().getValue())
				.forEach(actualIdentifiers::add);

			BundleEntryComponent firstEntry = historyBundle.getEntry().get(0);
			BundleEntryComponent lastEntry = historyBundle.getEntry().get(historyBundle.getEntry().size() - 1);
			ourLog.info("""
                            Loaded history page:
                             * First ID[ {} ] LastUpdated: {}
                             * Last  ID[ {} ] LastUpdated: {}""",
				((Patient) firstEntry.getResource()).getIdentifierFirstRep().getValue(),
				firstEntry.getResource().getMeta().getLastUpdatedElement().getValueAsString(),
				((Patient) lastEntry.getResource()).getIdentifierFirstRep().getValue(),
				lastEntry.getResource().getMeta().getLastUpdatedElement().getValueAsString()
			);

			if (historyBundle.getLink(Constants.LINK_NEXT) != null) {
				historyBundle = myClient.loadPage().next(historyBundle).execute();
			} else {
				break;
			}
		}

		// Verify
		actualIdentifiers.sort(Comparators.comparable());

		assertEquals(expectedIdentifiers, actualIdentifiers);
	}


	private IIdType createOrganization(String methodName, String s) {
		Organization o1 = new Organization();
		o1.setName(methodName + s);
		return myClient.create().resource(o1).execute().getId().toUnqualifiedVersionless();
	}

	public IIdType createPatientWithIndexAtOrganization(String theMethodName, String theIndex, IIdType theOrganizationId) {
		Patient p1 = new Patient();
		p1.addName().setFamily(theMethodName + theIndex);
		p1.getManagingOrganization().setReferenceElement(theOrganizationId);
		IIdType p1Id = myClient.create().resource(p1).execute().getId().toUnqualifiedVersionless();
		return p1Id;
	}

	public IIdType createConditionForPatient(String theMethodName, String theIndex, IIdType thePatientId) {
		Condition c = new Condition();
		c.addIdentifier().setValue(theMethodName + theIndex);
		if (thePatientId != null) {
			c.getSubject().setReferenceElement(thePatientId);
		}
		IIdType cId = myClient.create().resource(c).execute().getId().toUnqualifiedVersionless();
		return cId;
	}

	private IIdType createMedicationRequestForPatient(IIdType thePatientId, String theIndex) {
		MedicationRequest m = new MedicationRequest();
		m.addIdentifier().setValue(theIndex);
		if (thePatientId != null) {
			m.getSubject().setReferenceElement(thePatientId);
		}
		IIdType mId = myClient.create().resource(m).execute().getId().toUnqualifiedVersionless();
		return mId;
	}

	private IIdType createObservationForPatient(IIdType thePatientId, String theIndex) {
		Observation o = new Observation();
		o.addIdentifier().setValue(theIndex);
		if (thePatientId != null) {
			o.getSubject().setReferenceElement(thePatientId);
		}
		IIdType oId = myClient.create().resource(o).execute().getId().toUnqualifiedVersionless();
		return oId;
	}

	protected List<IIdType> toUnqualifiedVersionlessIds(Bundle theFound) {
		List<IIdType> retVal = new ArrayList<>();
		for (BundleEntryComponent next : theFound.getEntry()) {
			if (next.getResource() != null) {
				retVal.add(next.getResource().getIdElement().toUnqualifiedVersionless());
			}
		}
		return retVal;
	}

}
