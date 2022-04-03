package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.entity.ResourceReindexJobEntity;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.BundleUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.CapabilityStatement.CapabilityStatementRestComponent;
import org.hl7.fhir.r4.model.CapabilityStatement.CapabilityStatementRestResourceComponent;
import org.hl7.fhir.r4.model.CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.ExplanationOfBenefit;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.SearchParameter.XPathUsageType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

public class ResourceProviderCustomSearchParamR4Test extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderCustomSearchParamR4Test.class);


	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();
		myModelConfig.setDefaultSearchParamsCanBeOverridden(new ModelConfig().isDefaultSearchParamsCanBeOverridden());
		myDaoConfig.setAllowContainsSearches(new DaoConfig().isAllowContainsSearches());
	}

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();
		myDaoConfig.setAllowContainsSearches(new DaoConfig().isAllowContainsSearches());
	}

	@Override
	@BeforeEach
	public void beforeResetConfig() {
		super.beforeResetConfig();

		myModelConfig.setDefaultSearchParamsCanBeOverridden(new ModelConfig().isDefaultSearchParamsCanBeOverridden());
		mySearchParamRegistry.forceRefresh();
	}

	private Map<String, CapabilityStatementRestResourceSearchParamComponent> extractSearchParams(CapabilityStatement conformance, String resType) {
		Map<String, CapabilityStatementRestResourceSearchParamComponent> map = new HashMap<>();
		for (CapabilityStatementRestComponent nextRest : conformance.getRest()) {
			for (CapabilityStatementRestResourceComponent nextResource : nextRest.getResource()) {
				if (!resType.equals(nextResource.getType())) {
					continue;
				}
				for (CapabilityStatementRestResourceSearchParamComponent nextParam : nextResource.getSearchParam()) {
					map.put(nextParam.getName(), nextParam);
				}
			}
		}
		return map;
	}

	@Test
	public void saveCreateSearchParamInvalidWithMissingStatus() {
		SearchParameter sp = new SearchParameter();
		sp.setCode("foo");
		sp.setExpression("Patient.gender");
		sp.setXpathUsage(XPathUsageType.NORMAL);
		sp.setTitle("Foo Param");

		try {
			myClient.create().resource(sp).execute();
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("HTTP 422 Unprocessable Entity: " + Msg.code(1112) + "SearchParameter.status is missing or invalid", e.getMessage());
		}
	}

	@Test
	public void saveCreateSearchParamInvalidWithPathMissingElementName() {
		SearchParameter sp = new SearchParameter();
		sp.setCode("foo");
		sp.setExpression("Patient");
		sp.addBase("Patient");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setTitle("Foo Param");

		try {
			myClient.create().resource(sp).execute();
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("HTTP 422 Unprocessable Entity: " + Msg.code(1120) + "SearchParameter.expression value \"Patient\" is invalid", e.getMessage());
		}
	}

	@Test
	public void testConformanceOverrideAllowed() {
		myModelConfig.setDefaultSearchParamsCanBeOverridden(true);

		CapabilityStatement conformance = myClient
			.fetchConformance()
			.ofType(CapabilityStatement.class)
			.execute();
		Map<String, CapabilityStatementRestResourceSearchParamComponent> map = extractSearchParams(conformance, "Patient");

		CapabilityStatementRestResourceSearchParamComponent param = map.get("foo");
		assertNull(param);

		param = map.get("gender");
		assertNotNull(param);

		TransactionTemplate txTemplate = newTxTemplate();
		txTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				// Add a custom search parameter
				SearchParameter fooSp = new SearchParameter();
				fooSp.addBase("Patient");
				fooSp.setCode("foo");
				fooSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.TOKEN);
				fooSp.setTitle("FOO SP");
				fooSp.setExpression("Patient.gender");
				fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
				fooSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
				mySearchParameterDao.create(fooSp, mySrd);
			}
		});

		// Disable an existing parameter
		txTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				SearchParameter fooSp = new SearchParameter();
				fooSp.addBase("Patient");
				fooSp.setCode("gender");
				fooSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.TOKEN);
				fooSp.setTitle("Gender");
				fooSp.setExpression("Patient.gender");
				fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
				fooSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.RETIRED);
				mySearchParameterDao.create(fooSp, mySrd);
			}
		});

		txTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				mySearchParamRegistry.forceRefresh();
			}
		});

		conformance = myClient
			.fetchConformance()
			.ofType(CapabilityStatement.class)
			.execute();
		map = extractSearchParams(conformance, "Patient");

		param = map.get("foo");
		assertEquals("foo", param.getName());

		param = map.get("gender");
		assertNull(param);

	}

	@Test
	public void testCreatingParamMarksCorrectResourcesForReindexing() {
		Patient pat = new Patient();
		pat.setGender(AdministrativeGender.MALE);
		IIdType patId = myPatientDao.create(pat, mySrd).getId().toUnqualifiedVersionless();

		Observation obs2 = new Observation();
		obs2.setStatus(ObservationStatus.FINAL);
		IIdType obsId = myObservationDao.create(obs2, mySrd).getId().toUnqualifiedVersionless();

		runInTransaction(() -> {
			ResourceTable res = myResourceTableDao.findById(patId.getIdPartAsLong()).orElseThrow(IllegalStateException::new);
			assertEquals(BaseHapiFhirDao.INDEX_STATUS_INDEXED, res.getIndexStatus().longValue());
			res = myResourceTableDao.findById(obsId.getIdPartAsLong()).orElseThrow(IllegalStateException::new);
			assertEquals(BaseHapiFhirDao.INDEX_STATUS_INDEXED, res.getIndexStatus().longValue());
		});

		SearchParameter fooSp = new SearchParameter();
		fooSp.addBase("Patient");
		fooSp.setCode("foo");
		fooSp.setName("foo");
		fooSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.TOKEN);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("Patient.gender");
		fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(fooSp, mySrd);

		runInTransaction(() -> {
			List<ResourceReindexJobEntity> allJobs = myResourceReindexJobDao.findAll();
			assertEquals(1, allJobs.size());
			assertEquals("Patient", allJobs.get(0).getResourceType());
		});

	}

	@Test
	public void testIncludeExtensionReferenceAsRecurse() throws Exception {
		SearchParameter attendingSp = new SearchParameter();
		attendingSp.addBase("Patient");
		attendingSp.setCode("attending");
		attendingSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.REFERENCE);
		attendingSp.setTitle("Attending");
		attendingSp.setExpression("Patient.extension('http://acme.org/attending')");
		attendingSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		attendingSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		attendingSp.getTarget().add(new CodeType("Practitioner"));
		IIdType spId = mySearchParameterDao.create(attendingSp, mySrd).getId().toUnqualifiedVersionless();

		mySearchParamRegistry.forceRefresh();

		Practitioner p1 = new Practitioner();
		p1.addName().setFamily("P1");
		IIdType p1id = myPractitionerDao.create(p1).getId().toUnqualifiedVersionless();

		Patient p2 = new Patient();
		p2.addName().setFamily("P2");
		p2.addExtension().setUrl("http://acme.org/attending").setValue(new Reference(p1id));
		IIdType p2id = myPatientDao.create(p2).getId().toUnqualifiedVersionless();

		Appointment app = new Appointment();
		app.addParticipant().getActor().setReference(p2id.getValue());
		IIdType appId = myAppointmentDao.create(app).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		HttpGet get = new HttpGet(ourServerBase + "/Appointment?_include:recurse=Appointment:patient&_include:recurse=Appointment:location&_include:recurse=Patient:attending&_pretty=true");
		CloseableHttpResponse response = ourHttpClient.execute(get);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent(), Constants.CHARSET_UTF8);
			ourLog.info(resp);
			assertEquals(200, response.getStatusLine().getStatusCode());

			assertThat(resp, containsString("<fullUrl value=\"http://localhost:" + ourPort + "/fhir/context/Practitioner/"));
		} finally {
			IOUtils.closeQuietly(response);
		}
	}

	@Test
	public void testSearchForExtension() {
		SearchParameter eyeColourSp = new SearchParameter();
		eyeColourSp.addBase("Patient");
		eyeColourSp.setCode("eyecolour");
		eyeColourSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.TOKEN);
		eyeColourSp.setTitle("Eye Colour");
		eyeColourSp.setExpression("Patient.extension('http://acme.org/eyecolour')");
		eyeColourSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		eyeColourSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);

		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(eyeColourSp));

		myClient
			.create()
			.resource(eyeColourSp)
			.execute();

		mySearchParamRegistry.forceRefresh();

		Patient p1 = new Patient();
		p1.setActive(true);
		p1.addExtension().setUrl("http://acme.org/eyecolour").setValue(new CodeType("blue"));
		IIdType p1id = myPatientDao.create(p1).getId().toUnqualifiedVersionless();

		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(p1));

		Patient p2 = new Patient();
		p2.setActive(true);
		p2.addExtension().setUrl("http://acme.org/eyecolour").setValue(new CodeType("green"));
		IIdType p2id = myPatientDao.create(p2).getId().toUnqualifiedVersionless();

		Bundle bundle = myClient
			.search()
			.forResource(Patient.class)
			.where(new TokenClientParam("eyecolour").exactly().code("blue"))
			.returnBundle(Bundle.class)
			.execute();

		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

		List<String> foundResources = toUnqualifiedVersionlessIdValues(bundle);
		assertThat(foundResources, contains(p1id.getValue()));
	}


	@SuppressWarnings("unused")
	@Test
	public void testSearchQualifiedWithCustomReferenceParam() {

		SearchParameter fooSp = new SearchParameter();
		fooSp.addBase("Observation");
		fooSp.setCode("foo");
		fooSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.REFERENCE);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("Observation.subject");
		fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(fooSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		Patient pat = new Patient();
		pat.setGender(AdministrativeGender.MALE);
		IIdType patId = myPatientDao.create(pat, mySrd).getId().toUnqualifiedVersionless();

		Observation obs1 = new Observation();
		obs1.getSubject().setReferenceElement(patId);
		IIdType obsId1 = myObservationDao.create(obs1, mySrd).getId().toUnqualifiedVersionless();

		Observation obs2 = new Observation();
		obs2.setStatus(org.hl7.fhir.r4.model.Observation.ObservationStatus.FINAL);
		IIdType obsId2 = myObservationDao.create(obs2, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;
		Bundle result;

		result = myClient
			.search()
			.forResource(Observation.class)
			.where(new ReferenceClientParam("foo").hasChainedProperty(Patient.GENDER.exactly().code("male")))
			.returnBundle(Bundle.class)
			.execute();
		foundResources = toUnqualifiedVersionlessIdValues(result);
		assertThat(foundResources, contains(obsId1.getValue()));

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchWithCustomParam() {

		SearchParameter fooSp = new SearchParameter();
		fooSp.addBase("Patient");
		fooSp.setCode("foo");
		fooSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.TOKEN);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("Patient.gender");
		fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(fooSp, mySrd);

		myCaptureQueriesListener.clear();
		mySearchParamRegistry.forceRefresh();
		myCaptureQueriesListener.logAllQueriesForCurrentThread();

		Patient pat = new Patient();
		pat.setGender(AdministrativeGender.MALE);
		IIdType patId = myPatientDao.create(pat, mySrd).getId().toUnqualifiedVersionless();

		Patient pat2 = new Patient();
		pat2.setGender(AdministrativeGender.FEMALE);
		IIdType patId2 = myPatientDao.create(pat2, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;
		Bundle result;

		result = myClient
			.search()
			.forResource(Patient.class)
			.where(new TokenClientParam("foo").exactly().code("male"))
			.returnBundle(Bundle.class)
			.execute();

		foundResources = toUnqualifiedVersionlessIdValues(result);
		assertThat(foundResources, contains(patId.getValue()));

	}


	@SuppressWarnings("unused")
	@Test
	public void testSearchWithCustomParamInvalidDateFormat() {

		SearchParameter dateParameter = new SearchParameter();
		dateParameter.setId("explanationofbenefit-service-date");
		dateParameter.setName("ExplanationOfBenefit_ServiceDate");
		dateParameter.setCode("service-date");
		dateParameter.setDescription("test");
		dateParameter.setUrl("http://integer");
		dateParameter.setStatus(Enumerations.PublicationStatus.ACTIVE);
		dateParameter.addBase("ExplanationOfBenefit");
		dateParameter.setType(Enumerations.SearchParamType.DATE);
		dateParameter.setExpression("ExplanationOfBenefit.billablePeriod | ExplanationOfBenefit.item.serviced as Date |  ExplanationOfBenefit.item.serviced as Period");
		dateParameter.setXpath("f:ExplanationOfBenefit/f:billablePeriod | f:ExplanationOfBenefit/f:item/f:serviced/f:servicedDate | f:ExplanationOfBenefit/f:item/f:serviced/f:servicedPeriod");
		dateParameter.setXpathUsage(SearchParameter.XPathUsageType.NORMAL);
		mySearchParameterDao.update(dateParameter);

		mySearchParamRegistry.forceRefresh();

		IBundleProvider results;
		List<String> foundResources;
		Bundle result;


		//Try with builtin SP
		try {
			myClient
				.search()
				.forResource(ExplanationOfBenefit.class)
				.where(new StringClientParam("created").matches().value("01-01-2020"))
				.returnBundle(Bundle.class)
				.execute();

		} catch (Exception e) {
			assertThat(e.getMessage(), is(equalTo("HTTP 400 Bad Request: " + Msg.code(1882) + "Invalid date/time format: \"01-01-2020\"")));
		}

		//Now with custom SP
		try {
			myClient
				.search()
				.forResource(ExplanationOfBenefit.class)
				.where(new StringClientParam("service-date").matches().value("01-01-2020"))
				.returnBundle(Bundle.class)
				.execute();

		} catch (Exception e) {
			assertThat(e.getMessage(), is(equalTo("HTTP 400 Bad Request: " + Msg.code(1882) + "Invalid date/time format: \"01-01-2020\"")));
		}
	}

	/**
	 * See #1300
	 */
	@Test
	public void testCustomParameterMatchingManyValues() {

		List<String> found = new ArrayList<>();

		class Interceptor {
			@Hook(Pointcut.JPA_PERFTRACE_SEARCH_FOUND_ID)
			public void foundId(Integer theSearchId, Object theId) {
				found.add(theSearchId + "/" + theId);
			}
		}
		Interceptor interceptor = new Interceptor();
		myInterceptorRegistry.registerInterceptor(interceptor);
		try {

			int textIndex = 0;
			List<Long> ids = new ArrayList<>();
			for (int i = 0; i < 200; i++) {
				//Lots and lots of matches
				Patient q = new Patient();
				q.addIdentifier().setSystem("System_" + textIndex++).setValue("FOO");
				q.addIdentifier().setSystem("System_" + textIndex++).setValue("FOO");
				q.addIdentifier().setSystem("System_" + textIndex++).setValue("FOO");
				q.addIdentifier().setSystem("System_" + textIndex++).setValue("FOO");
				q.addIdentifier().setSystem("System_" + textIndex++).setValue("FOO");
				q.addIdentifier().setSystem("System_" + textIndex++).setValue("FOO");
				q.addIdentifier().setSystem("System_" + textIndex++).setValue("FOO");
				q.addIdentifier().setSystem("System_" + textIndex++).setValue("FOO");
				q.addIdentifier().setSystem("System_" + textIndex++).setValue("FOO");
				q.addIdentifier().setSystem("System_" + textIndex++).setValue("FOO");
				ids.add(myPatientDao.create(q).getId().getIdPartAsLong());
			}

			myCaptureQueriesListener.clear();

			int foundCount = 0;
			Bundle bundle = null;
			List<Long> actualIds = new ArrayList<>();
			do {

				if (bundle == null) {
					bundle = myClient
						.search()
						.byUrl(ourServerBase + "/Patient?identifier=FOO")
						.returnBundle(Bundle.class)
						.execute();
				} else {
					bundle = myClient
						.loadPage()
						.next(bundle)
						.execute();
				}
				List<IBaseResource> resources = BundleUtil.toListOfResources(myFhirContext, bundle);
				resources.forEach(t -> actualIds.add(t.getIdElement().getIdPartAsLong()));
				foundCount += resources.size();

			} while (bundle.getLink("next") != null);

			String queries = " * " + myCaptureQueriesListener
				.getSelectQueries()
				.stream()
				.map(t -> t.getSql(true, false))
				.collect(Collectors.joining("\n * "));

			ourLog.info("Found: {}", found);

			runInTransaction(() -> {

				List currentResults = myEntityManager.createNativeQuery("select distinct resourceta0_.RES_ID as col_0_0_ from HFJ_RESOURCE resourceta0_ left outer join HFJ_SPIDX_STRING myparamsst1_ on resourceta0_.RES_ID=myparamsst1_.RES_ID where myparamsst1_.HASH_NORM_PREFIX='5901791607832193956' and (myparamsst1_.SP_VALUE_NORMALIZED like 'SECTION%') limit '500'").getResultList();
				List currentResources = myEntityManager.createNativeQuery("select resourceta0_.RES_ID as col_0_0_ from HFJ_RESOURCE resourceta0_").getResultList();

				List<Search> searches = mySearchEntityDao.findAll();
				assertEquals(1, searches.size());
				Search search = searches.get(0);
				String message = "\nWanted : " + (ids) + "\n" +
					"Actual : " + (actualIds) + "\n" +
					"Found  : " + (found) + "\n" +
					"Current: " + currentResults + "\n" +
					"Current: " + currentResources + "\n" +
					search.toString() +
					"\nQueries :\n" + queries;

				for (Long next : ids) {
					if (!actualIds.contains(next)) {
						List<ResourceIndexedSearchParamString> indexes = myResourceIndexedSearchParamStringDao
							.findAll()
							.stream()
							.filter(t -> t.getResourcePid().equals(next))
							.collect(Collectors.toList());
						message += "\n\nResource " + next + " has prefixes:\n * " + indexes.stream().map(t -> t.toString()).collect(Collectors.joining("\n * "));
						break;
					}
				}

				assertEquals(200, search.getNumFound(), message);
				assertEquals(200, search.getTotalCount().intValue(), message);
				assertEquals(SearchStatusEnum.FINISHED, search.getStatus(), message);
			});

			assertEquals(200, foundCount);
		} finally {
			myInterceptorRegistry.unregisterInterceptor(interceptor);
		}
	}


}
