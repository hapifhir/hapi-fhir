package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantityNormalized;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceSearchUrlEntity;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.util.UcumServiceUtil;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.ClasspathUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.SampledData;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class FhirResourceDaoR4CreateTest extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4CreateTest.class);

	@AfterEach
	public void afterResetDao() {
		myStorageSettings.setResourceServerIdStrategy(new JpaStorageSettings().getResourceServerIdStrategy());
		myStorageSettings.setResourceClientIdStrategy(new JpaStorageSettings().getResourceClientIdStrategy());
		myStorageSettings.setDefaultSearchParamsCanBeOverridden(new JpaStorageSettings().isDefaultSearchParamsCanBeOverridden());
		myStorageSettings.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_NOT_SUPPORTED);
		myStorageSettings.setIndexOnContainedResources(new JpaStorageSettings().isIndexOnContainedResources());
		myStorageSettings.setIndexOnContainedResourcesRecursively(new JpaStorageSettings().isIndexOnContainedResourcesRecursively());
	}

	@Test
	public void testCreateDoesntIndexForMetaSearchTags() {
		Observation obs = new Observation();
		obs.setId("A");
		obs.addNote().setText("A non indexed value");
		obs.getMeta().setLastUpdatedElement(InstantType.now());
		obs.getMeta().addTag().setSystem("http://foo").setCode("blah");
		obs.getMeta().addTag().setSystem("http://foo").setCode("blah2");
		obs.getMeta().addSecurity().setSystem("http://foo").setCode("blah");
		obs.getMeta().addSecurity().setSystem("http://foo").setCode("blah2");
		obs.getMeta().addProfile("http://blah");
		obs.getMeta().addProfile("http://blah2");
		obs.getMeta().setSource("http://foo#bar");
		myObservationDao.update(obs, new SystemRequestDetails());

		runInTransaction(()->{
			logAllTokenIndexes();
			logAllStringIndexes();
			assertEquals(0, myResourceIndexedSearchParamStringDao.count());
			assertEquals(0, myResourceIndexedSearchParamTokenDao.count());
			assertEquals(0, myResourceIndexedSearchParamUriDao.count());
		});

	}



	@Test
	public void testCreateLinkCreatesAppropriatePaths() {
		Patient p = new Patient();
		p.setId("Patient/A");
		p.setActive(true);
		myPatientDao.update(p, mySrd);

		Observation obs = new Observation();
		obs.setSubject(new Reference("Patient/A"));
		myObservationDao.create(obs, mySrd);

		runInTransaction(() -> {
			List<ResourceLink> allLinks = myResourceLinkDao.findAll();
			List<String> paths = allLinks
				.stream()
				.map(ResourceLink::getSourcePath)
				.sorted()
				.collect(Collectors.toList());
			assertThat(paths).as(paths.toString()).containsExactly("Observation.subject", "Observation.subject.where(resolve() is Patient)");
		});

		myCaptureQueriesListener.clear();
		assertEquals(1, myObservationDao.search(SearchParameterMap.newSynchronous("patient", new ReferenceParam("Patient/A"))).sizeOrThrowNpe());
		myCaptureQueriesListener.logSelectQueries();
	}

	@Test
	public void testCreateLinkCreatesAppropriatePaths_ContainedResource() {
		myStorageSettings.setIndexOnContainedResources(true);

		Patient p = new Patient();
		p.setId("Patient/A");
		p.setActive(true);
		myPatientDao.update(p, mySrd);

		Observation containedObs = new Observation();
		containedObs.setId("#cont");
		containedObs.setSubject(new Reference("Patient/A"));

		Encounter enc = new Encounter();
		enc.getContained().add(containedObs);
		enc.addReasonReference(new Reference("#cont"));
		myEncounterDao.create(enc, mySrd);

		runInTransaction(() -> {
			List<ResourceLink> allLinks = myResourceLinkDao.findAll();
			Optional<ResourceLink> link = allLinks
				.stream()
				.filter(t -> "Encounter.reasonReference.subject".equals(t.getSourcePath()))
				.findFirst();
			assertTrue(link.isPresent());
			assertEquals("Patient", link.get().getTargetResourceType());
			assertEquals("A", link.get().getTargetResourceId());
		});
	}

	@Test
	public void testCreateLinkCreatesAppropriatePaths_ContainedResourceRecursive() {
		myStorageSettings.setIndexOnContainedResources(true);
		myStorageSettings.setIndexOnContainedResourcesRecursively(true);

		Patient p = new Patient();
		p.setId("pat");
		p.setActive(true);
		p.getNameFirstRep().setFamily("Smith");

		Observation containedObs = new Observation();
		containedObs.setId("obs");
		containedObs.setSubject(new Reference("#pat"));

		Encounter enc = new Encounter();
		enc.getContained().add(containedObs);
		enc.getContained().add(p);
		enc.addReasonReference(new Reference("#obs"));
		myEncounterDao.create(enc, mySrd);

		runInTransaction(() -> {
			List<ResourceIndexedSearchParamString> allParams = myResourceIndexedSearchParamStringDao.findAll();
			Optional<ResourceIndexedSearchParamString> link = allParams
				.stream()
				.filter(t -> "reason-reference.subject.family".equals(t.getParamName()))
				.findFirst();
			assertTrue(link.isPresent());
			assertEquals("Smith", link.get().getValueExact());
		});
	}


	@Test
	public void testCreateLinkCreatesAppropriatePaths_ContainedResourceRecursive_DoesNotLoop() {
		myStorageSettings.setIndexOnContainedResources(true);
		myStorageSettings.setIndexOnContainedResourcesRecursively(true);

		Organization org1 = new Organization();
		org1.setId("org1");
		org1.setName("EscherCorp");
		org1.setPartOf(new Reference("#org2"));

		Organization org2 = new Organization();
		org2.setId("org2");
		org2.setName("M.C.Escher Unlimited");
		org2.setPartOf(new Reference("#org1"));

		Observation containedObs = new Observation();
		containedObs.setId("obs");
		containedObs.addPerformer(new Reference("#org1"));

		Encounter enc = new Encounter();
		enc.getContained().add(containedObs);
		enc.getContained().add(org1);
		enc.getContained().add(org2);
		enc.addReasonReference(new Reference("#obs"));
		myEncounterDao.create(enc, mySrd);

		runInTransaction(() -> {
			List<ResourceIndexedSearchParamString> allParams = myResourceIndexedSearchParamStringDao.findAll();
			Optional<ResourceIndexedSearchParamString> firstOrg = allParams
				.stream()
				.filter(t -> "reason-reference.performer.name".equals(t.getParamName()))
				.findFirst();
			assertTrue(firstOrg.isPresent());
			assertEquals("EscherCorp", firstOrg.get().getValueExact());

			Optional<ResourceIndexedSearchParamString> secondOrg = allParams
				.stream()
				.filter(t -> "reason-reference.performer.partof.name".equals(t.getParamName()))
				.findFirst();
			assertTrue(secondOrg.isPresent());
			assertEquals("M.C.Escher Unlimited", secondOrg.get().getValueExact());

			Optional<ResourceIndexedSearchParamString> thirdOrg = allParams
				.stream()
				.filter(t -> "reason-reference.performer.partof.partof.name".equals(t.getParamName()))
				.findFirst();
			assertFalse(thirdOrg.isPresent());
		});
	}

	@Test
	public void testCreateLinkCreatesAppropriatePaths_ContainedResourceRecursive_ToOutboundReference() {
		myStorageSettings.setIndexOnContainedResources(true);
		myStorageSettings.setIndexOnContainedResourcesRecursively(true);

		Organization org = new Organization();
		org.setId("Organization/ABC");
		myOrganizationDao.update(org);

		Patient p = new Patient();
		p.setId("pat");
		p.setActive(true);
		p.setManagingOrganization(new Reference("Organization/ABC"));

		Observation containedObs = new Observation();
		containedObs.setId("#cont");
		containedObs.setSubject(new Reference("#pat"));

		Encounter enc = new Encounter();
		enc.getContained().add(p);
		enc.getContained().add(containedObs);
		enc.addReasonReference(new Reference("#cont"));
		myEncounterDao.create(enc, mySrd);

		runInTransaction(() -> {
			List<ResourceLink> allLinks = myResourceLinkDao.findAll();
			Optional<ResourceLink> link = allLinks
				.stream()
				.filter(t -> "Encounter.reasonReference.subject.managingOrganization".equals(t.getSourcePath()))
				.findFirst();
			assertTrue(link.isPresent());
			assertEquals("Organization", link.get().getTargetResourceType());
			assertEquals("ABC", link.get().getTargetResourceId());
		});
	}

	@Test
	public void testCreateLinkCreatesAppropriatePaths_ContainedResourceRecursive_ToOutboundReference_NoLoops() {
		myStorageSettings.setIndexOnContainedResources(true);
		myStorageSettings.setIndexOnContainedResourcesRecursively(true);

		Organization org = new Organization();
		org.setId("Organization/ABC");
		myOrganizationDao.update(org);

		Patient p = new Patient();
		p.setId("pat");
		p.setActive(true);
		p.setManagingOrganization(new Reference("Organization/ABC"));

		Observation obs1 = new Observation();
		obs1.setId("obs1");
		obs1.setSubject(new Reference("#pat"));
		obs1.addPartOf(new Reference("#obs2"));

		Observation obs2 = new Observation();
		obs2.setId("obs2");
		obs2.addPartOf(new Reference("#obs1"));

		Encounter enc = new Encounter();
		enc.getContained().add(p);
		enc.getContained().add(obs1);
		enc.getContained().add(obs2);
		enc.addReasonReference(new Reference("#obs2"));
		myEncounterDao.create(enc, mySrd);

		runInTransaction(() -> {
			List<ResourceLink> allLinks = myResourceLinkDao.findAll();
			Optional<ResourceLink> link = allLinks
				.stream()
				.filter(t -> "Encounter.reasonReference.partOf.subject.managingOrganization".equals(t.getSourcePath()))
				.findFirst();
			assertTrue(link.isPresent());
			assertEquals("Organization", link.get().getTargetResourceType());
			assertEquals("ABC", link.get().getTargetResourceId());

			Optional<ResourceLink> noLink = allLinks
				.stream()
				.filter(t -> "Encounter.reasonReference.partOf.partOf.partOf.subject.managingOrganization".equals(t.getSourcePath()))
				.findFirst();
			assertFalse(noLink.isPresent());
		});
	}

	@Test
	public void testConditionalCreateWithPlusInUrl() {
		Observation obs = new Observation();
		obs.addIdentifier().setValue("20210427133226.444+0800");
		DaoMethodOutcome outcome = myObservationDao.create(obs, "identifier=20210427133226.444%2B0800", new SystemRequestDetails());
		assertTrue(outcome.getCreated());

		logAllTokenIndexes();
		myCaptureQueriesListener.clear();
		obs = new Observation();
		obs.addIdentifier().setValue("20210427133226.444+0800");
		outcome = myObservationDao.create(obs, "identifier=20210427133226.444%2B0800", new SystemRequestDetails());
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertFalse(outcome.getCreated());
	}

	/**
	 * Simulate a client error: Identifier has a "+" but URL has an escaped space character
	 */
	@Test
	public void testConditionalCreateFailsIfMatchUrlDoesntMatch() {
		Observation obs = new Observation();
		obs.addIdentifier().setValue("A+B");
		try {
			myObservationDao.create(obs, "identifier=A%20B", new SystemRequestDetails());
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(929) + "Failed to process conditional create. The supplied resource did not satisfy the conditional URL.", e.getMessage());
		}
	}

	/**
	 * Simulate a client error: Identifier has a "+" but URL has an escaped space character
	 */
	@Test
	public void testConditionalCreateFailsIfMatchUrlDoesntMatch_InTransaction() {
		BundleBuilder bb = new BundleBuilder(myFhirContext);

		Patient patient = new Patient();
		patient.setId(IdType.newRandomUuid());
		patient.setActive(true);
		bb.addTransactionCreateEntry(patient);

		Observation obs = new Observation();
		obs.getSubject().setReference(patient.getId());
		obs.addIdentifier().setValue("A+B");
		bb.addTransactionCreateEntry(obs).conditional("identifier=A%20B");

		try {
			mySystemDao.transaction(new SystemRequestDetails(), (Bundle) bb.getBundle());
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(929) + "Failed to process conditional create. The supplied resource did not satisfy the conditional URL.", e.getMessage());
		}
	}

	@Test
	public void testCreateResource_withConditionalCreate_willAddSearchUrlEntity(){
		// given
		String identifierCode = "20210427133226.4440+800";
		String matchUrl = "identifier=" + identifierCode.replace("+", "%2B");
		Observation obs = new Observation();
		obs.addIdentifier().setValue(identifierCode);
		// when
		DaoMethodOutcome outcome = myObservationDao.create(obs, matchUrl, new SystemRequestDetails());

		// then
		Long expectedResId = outcome.getId().getIdPartAsLong();
		String expectedNormalizedMatchUrl = obs.fhirType() + "?" + matchUrl;

		assertTrue(outcome.getCreated());
		ResourceSearchUrlEntity searchUrlEntity = myResourceSearchUrlDao.findAll().get(0);
		assertNotNull(searchUrlEntity);
		assertEquals(expectedResId, searchUrlEntity.getResourcePid());
		Instant now = Instant.now();
		assertThat(searchUrlEntity.getCreatedTime())
			.as("Check that the creation time of the URL is within the last second")
			.isBetween(now.minus(1, ChronoUnit.SECONDS), now);
		assertEquals(expectedNormalizedMatchUrl, searchUrlEntity.getSearchUrl());

	}

	@Test
	public void testCreateResourceWithKoreanText() {
		String input = ClasspathUtil.loadResource("/r4/bug832-korean-text.xml");
		Patient p = myFhirContext.newXmlParser().parseResource(Patient.class, input);
		String id = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Patient.SP_FAMILY, new StringParam("김"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map))).containsExactly(id);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Patient.SP_GIVEN, new StringParam("준"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map))).containsExactly(id);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Patient.SP_GIVEN, new StringParam("준수"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map))).containsExactly(id);

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Patient.SP_GIVEN, new StringParam("수")); // rightmost character only
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(map))).isEmpty();
	}

	@Test
	public void testCreateWithUuidServerResourceStrategy() {
		myStorageSettings.setResourceServerIdStrategy(JpaStorageSettings.IdStrategyEnum.UUID);

		Patient p = new Patient();
		p.addName().setFamily("FAM");
		IIdType id = myPatientDao.create(p).getId().toUnqualified();

		assertThat(id.getIdPart()).matches("[a-z0-9]{8}-.*");

		p = myPatientDao.read(id);
		assertEquals("FAM", p.getNameFirstRep().getFamily());

	}

	@Test
	public void testCreateWithUuidServerResourceStrategy_ClientIdNotAllowed() {
		myStorageSettings.setResourceServerIdStrategy(JpaStorageSettings.IdStrategyEnum.UUID);
		myStorageSettings.setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.NOT_ALLOWED);

		Patient p = new Patient();
		p.addName().setFamily("FAM");
		IIdType id = myPatientDao.create(p).getId().toUnqualified();

		assertThat(id.getIdPart()).matches("[a-z0-9]{8}-.*");

		p = myPatientDao.read(id);
		assertEquals("FAM", p.getNameFirstRep().getFamily());

	}

	@Test
	public void testCreateWithUuidServerResourceStrategy_AnyClientIdAllowed() {
		myStorageSettings.setResourceServerIdStrategy(JpaStorageSettings.IdStrategyEnum.UUID);
		myStorageSettings.setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.ANY);

		CodeSystem cs = new CodeSystem();

		// alphanumeric ID
		cs.setId("123a");
		cs.setUrl("http://foo");
		IIdType id = myCodeSystemDao.create(cs).getId();
		cs = myCodeSystemDao.read(id);
		assertEquals("http://foo", cs.getUrl());

		// purely numeric ID
		cs.setId("123");
		cs.setUrl("http://fooCS");
		id = myCodeSystemDao.update(cs).getId();
		cs = myCodeSystemDao.read(id);
		assertEquals("http://fooCS", cs.getUrl());
	}

	/**
	 * See #1352
	 */
	@Test
	public void testCreateWithSampledDataInObservation() {
		Observation o = new Observation();
		o.setStatus(Observation.ObservationStatus.FINAL);
		SampledData sampledData = new SampledData();
		sampledData.setData("2 3 4 5 6");
		o.setValue(sampledData);
		assertTrue(myObservationDao.create(o).getCreated());
	}

	@Test
	public void testCreateWithClientAssignedIdDisallowed() {
		myStorageSettings.setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.NOT_ALLOWED);

		Patient p = new Patient();
		p.setId("AAA");
		p.addName().setFamily("FAM");
		try {
			myPatientDao.update(p);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals(Msg.code(959) + "No resource exists on this server resource with ID[AAA], and client-assigned IDs are not enabled.", e.getMessage());
		}
	}

	@Test
	public void testCreateWithClientAssignedIdPureNumeric() {
		myStorageSettings.setResourceServerIdStrategy(JpaStorageSettings.IdStrategyEnum.SEQUENTIAL_NUMERIC);
		myStorageSettings.setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.ANY);

		// Create a server assigned ID
		Patient p = new Patient();
		p.setActive(true);
		IIdType id0 = myPatientDao.create(p).getId();
		long firstClientAssignedId = id0.getIdPartAsLong();
		long newId = firstClientAssignedId + 2L;

		// Read it back
		p = myPatientDao.read(new IdType("Patient/" + firstClientAssignedId));
		assertTrue(p.getActive());

		// Now create a client assigned numeric ID
		p = new Patient();
		p.setId("Patient/" + newId);
		p.addName().setFamily("FAM");
		IIdType id1 = myPatientDao.update(p).getId();

		assertEquals(Long.toString(newId), id1.getIdPart());
		assertEquals("1", id1.getVersionIdPart());

		p = myPatientDao.read(id1);
		assertEquals("FAM", p.getNameFirstRep().getFamily());

		// Update it
		p = new Patient();
		p.setId("Patient/" + newId);
		p.addName().setFamily("FAM2");
		id1 = myPatientDao.update(p).getId();

		assertEquals(Long.toString(newId), id1.getIdPart());
		assertEquals("2", id1.getVersionIdPart());

		p = myPatientDao.read(id1);
		assertEquals("FAM2", p.getNameFirstRep().getFamily());

		// Try to create another server-assigned. This should fail since we have a
		// a conflict.
		p = new Patient();
		p.setActive(false);
		try {
			myPatientDao.create(p);
			fail();
		} catch (ResourceVersionConflictException e) {
			// good
		}

		ourLog.info("ID0: {}", id0);
		ourLog.info("ID1: {}", id1);
	}

	@Test
	public void testCreateWithClientAssignedIdPureNumericServerIdUuid() {
		myStorageSettings.setResourceServerIdStrategy(JpaStorageSettings.IdStrategyEnum.UUID);
		myStorageSettings.setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.ANY);

		// Create a server assigned ID
		Patient p = new Patient();
		p.setActive(true);
		IIdType id0 = myPatientDao.create(p).getId();

		// Read it back
		p = myPatientDao.read(id0.toUnqualifiedVersionless());
		assertTrue(p.getActive());

		// Pick an ID that was already used as an internal PID
		Long newId = runInTransaction(() -> myResourceTableDao.findIdsOfResourcesWithinUpdatedRangeOrderedFromNewest(
			PageRequest.of(0, 1),
			DateUtils.addDays(new Date(), -1),
			DateUtils.addDays(new Date(), 1)
		).getContent().get(0));

		// Not create a client assigned numeric ID
		p = new Patient();
		p.setId("Patient/" + newId);
		p.addName().setFamily("FAM");
		IIdType id1 = myPatientDao.update(p).getId();

		assertEquals(Long.toString(newId), id1.getIdPart());
		assertEquals("1", id1.getVersionIdPart());

		// Read it back
		p = myPatientDao.read(id1);
		assertEquals("FAM", p.getNameFirstRep().getFamily());

		// Update it
		p = new Patient();
		p.setId("Patient/" + newId);
		p.addName().setFamily("FAM2");
		id1 = myPatientDao.update(p).getId();

		assertEquals(Long.toString(newId), id1.getIdPart());
		assertEquals("2", id1.getVersionIdPart());

		p = myPatientDao.read(id1);
		assertEquals("FAM2", p.getNameFirstRep().getFamily());

		// Try to create another server-assigned. This should fail since we have a
		// a conflict.
		p = new Patient();
		p.setActive(false);
		IIdType id2 = myPatientDao.create(p).getId();

		ourLog.info("ID0: {}", id0);
		ourLog.info("ID1: {}", id1);
		ourLog.info("ID2: {}", id2);
	}

	@Test
	public void testCreateAndSearchWithUuidResourceStrategy() {
		myStorageSettings.setResourceServerIdStrategy(JpaStorageSettings.IdStrategyEnum.UUID);
		myStorageSettings.setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.ANY);

		StructureDefinition sd = new StructureDefinition();
		sd.setUrl("http://foo.com");
		DaoMethodOutcome result = myStructureDefinitionDao.create(sd);
		assertTrue(result.getCreated());
		StructureDefinition readSd = myStructureDefinitionDao.read(result.getId());
		assertEquals("http://foo.com", readSd.getUrl());

		logAllResources();
		logAllResourceVersions();

		runInTransaction(()->{
			List<ResourceTable> resources = myResourceTableDao.findAll();
			assertEquals(1, resources.size());
			assertEquals(1, resources.get(0).getVersion());

			List<ResourceHistoryTable> resourceVersions = myResourceHistoryTableDao.findAll();
			assertEquals(1, resourceVersions.size());
			assertEquals(1, resourceVersions.get(0).getVersion());
		});

		SearchParameterMap map = SearchParameterMap.newSynchronous();

		myCaptureQueriesListener.clear();
		IBundleProvider bundle = myStructureDefinitionDao.search(map);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(1, bundle.size());
	}

	@Test
	public void testTransactionCreateWithUuidResourceStrategy() {
		myStorageSettings.setResourceServerIdStrategy(JpaStorageSettings.IdStrategyEnum.UUID);

		Organization org = new Organization();
		org.setId(IdType.newRandomUuid());
		org.setName("ORG");

		Patient p = new Patient();
		p.setId(IdType.newRandomUuid());
		p.addName().setFamily("FAM");
		p.setActive(true);
		p.setBirthDateElement(new DateType("2011-01-01"));
		p.getManagingOrganization().setReference(org.getId());

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input.addEntry()
			.setResource(org)
			.setFullUrl(org.getId())
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST);
		input.addEntry()
			.setResource(p)
			.setFullUrl(p.getId())
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST);

		ourLog.debug(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(input));

		Bundle output = mySystemDao.transaction(mySrd, input);

		ourLog.debug(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(output));

		assertThat(output.getEntry().get(0).getResponse().getLocation()).matches("Organization/[a-z0-9]{8}-.*");
		assertThat(output.getEntry().get(1).getResponse().getLocation()).matches("Patient/[a-z0-9]{8}-.*");


	}

	@Test
	public void testTagsInContainedResourcesPreserved() {
		Patient p = new Patient();
		p.setActive(true);

		Organization o = new Organization();
		o.getMeta().addTag("http://foo", "bar", "FOOBAR");
		p.getManagingOrganization().setResource(o);

		String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info("Input: {}", encoded);
		assertThat(encoded).contains("#1");

		IIdType id = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		p = myPatientDao.read(id);

		encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info("Output: {}", encoded);
		assertThat(encoded).contains("#1");

		Organization org = (Organization) p.getManagingOrganization().getResource();
		assertEquals("#1", org.getId());
		assertThat(org.getMeta().getTag()).hasSize(1);

	}

	@Test
	public void testOverrideBuiltInSearchParamFailsIfDisabled() {
		myStorageSettings.setDefaultSearchParamsCanBeOverridden(false);

		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-birthdate");
		sp.setType(Enumerations.SearchParamType.DATE);
		sp.setCode("birthdate");
		sp.setExpression("Patient.birthDate");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		try {
			mySearchParameterDao.update(sp);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals(Msg.code(1111) + "Can not override built-in search parameter Patient:birthdate because overriding is disabled on this server", e.getMessage());
		}

	}

	@Test
	public void testCreateWithNormalizedQuantitySearchSupported_AlreadyCanonicalUnit() {

		myStorageSettings.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);
		Observation obs = new Observation();
		obs.setStatus(Observation.ObservationStatus.FINAL);
		Quantity q = new Quantity();
		q.setValueElement(new DecimalType(1.2));
		q.setUnit("CM");
		q.setSystem("http://unitsofmeasure.org");
		q.setCode("cm");
		obs.setValue(q);

		ourLog.debug("Observation1: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));

		assertTrue(myObservationDao.create(obs).getCreated());

		// Same value should be placed in both quantity tables
		runInTransaction(() -> {
			List<ResourceIndexedSearchParamQuantity> quantityIndexes = myResourceIndexedSearchParamQuantityDao.findAll().stream().filter(t -> t.getParamName().equals("value-quantity")).collect(Collectors.toList());
			assertEquals(1, quantityIndexes.size());
			assertEquals("1.2", Double.toString(quantityIndexes.get(0).getValue().doubleValue()));
			assertEquals("http://unitsofmeasure.org", quantityIndexes.get(0).getSystem());
			assertEquals("cm", quantityIndexes.get(0).getUnits());

			List<ResourceIndexedSearchParamQuantityNormalized> normalizedQuantityIndexes = myResourceIndexedSearchParamQuantityNormalizedDao.findAll().stream().filter(t -> t.getParamName().equals("value-quantity")).collect(Collectors.toList());
			assertEquals(1, normalizedQuantityIndexes.size());
			assertEquals("0.012", Double.toString(normalizedQuantityIndexes.get(0).getValue()));
			assertEquals("http://unitsofmeasure.org", normalizedQuantityIndexes.get(0).getSystem());
			assertEquals("m", normalizedQuantityIndexes.get(0).getUnits());
		});

		SearchParameterMap map = SearchParameterMap.newSynchronous(Observation.SP_VALUE_QUANTITY, new QuantityParam()
			.setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL)
			.setValue(new BigDecimal("0.012"))
			.setUnits("m")
		);
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map))).hasSize(1);
	}

	@Test
	public void testCreateWithNormalizedQuantitySearchSupported_SmallerThanCanonicalUnit() {

		myStorageSettings.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);
		Observation obs = new Observation();
		obs.setStatus(Observation.ObservationStatus.FINAL);
		Quantity q = new Quantity();
		q.setValueElement(new DecimalType(0.0000012));
		q.setUnit("MM");
		q.setSystem("http://unitsofmeasure.org");
		q.setCode("mm");
		obs.setValue(q);

		ourLog.debug("Observation1: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));

		myCaptureQueriesListener.clear();
		assertTrue(myObservationDao.create(obs).getCreated());
		myCaptureQueriesListener.logInsertQueries();

		// Original value should be in Quantity index, normalized should be in normalized table
		runInTransaction(() -> {
			List<ResourceIndexedSearchParamQuantity> quantityIndexes = myResourceIndexedSearchParamQuantityDao.findAll().stream().filter(t -> t.getParamName().equals("value-quantity")).collect(Collectors.toList());
			assertEquals(1, quantityIndexes.size());
			double d = quantityIndexes.get(0).getValue().doubleValue();
			assertEquals("1.2E-6", Double.toString(d));
			assertEquals("http://unitsofmeasure.org", quantityIndexes.get(0).getSystem());
			assertEquals("mm", quantityIndexes.get(0).getUnits());

			List<ResourceIndexedSearchParamQuantityNormalized> normalizedQuantityIndexes = myResourceIndexedSearchParamQuantityNormalizedDao.findAll().stream().filter(t -> t.getParamName().equals("value-quantity")).collect(Collectors.toList());
			assertEquals(1, normalizedQuantityIndexes.size());
			assertEquals("1.2E-9", Double.toString(normalizedQuantityIndexes.get(0).getValue()));
			assertEquals("http://unitsofmeasure.org", normalizedQuantityIndexes.get(0).getSystem());
			assertEquals("m", normalizedQuantityIndexes.get(0).getUnits());
		});

		String searchSql;
		SearchParameterMap map;
		List<String> ids;

		// Try with normalized value
		myCaptureQueriesListener.clear();
		map = SearchParameterMap.newSynchronous(Observation.SP_VALUE_QUANTITY, new QuantityParam()
			.setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL)
			.setValue(new BigDecimal("0.0000000012"))
			.setUnits("m")
		);
		ids = toUnqualifiedVersionlessIdValues(myObservationDao.search(map));
		searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		assertThat(searchSql).contains("HFJ_SPIDX_QUANTITY_NRML t0");
		assertThat(searchSql).contains("t0.SP_VALUE = '1.2E-9'");
		assertThat(ids).hasSize(1);

		// Try with non-normalized value
		myCaptureQueriesListener.clear();
		map = SearchParameterMap.newSynchronous(Observation.SP_VALUE_QUANTITY, new QuantityParam()
			.setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL)
			.setValue(new BigDecimal("0.0000012"))
			.setUnits("mm")
		);
		ids = toUnqualifiedVersionlessIdValues(myObservationDao.search(map));
		searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		assertThat(searchSql).contains("HFJ_SPIDX_QUANTITY_NRML t0");
		assertThat(searchSql).contains("t0.SP_VALUE = '1.2E-9'");
		assertThat(ids).hasSize(1);

		// Try with no units value
		myCaptureQueriesListener.clear();
		map = SearchParameterMap.newSynchronous(Observation.SP_VALUE_QUANTITY, new QuantityParam()
			.setValue(new BigDecimal("0.0000012"))
		);
		ids = toUnqualifiedVersionlessIdValues(myObservationDao.search(map));
		searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		assertThat(searchSql).contains("HFJ_SPIDX_QUANTITY t0");
		assertThat(searchSql).contains("t0.SP_VALUE = '0.0000012'");
		assertThat(ids).hasSize(1);
	}

	@Test
	public void testCreateWithNormalizedQuantitySearchSupported_SmallerThanCanonicalUnit2() {

		myStorageSettings.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);
		Observation obs = new Observation();
		obs.setStatus(Observation.ObservationStatus.FINAL);
		Quantity q = new Quantity();
		q.setValueElement(new DecimalType("149597.870691"));
		q.setUnit("MM");
		q.setSystem("http://unitsofmeasure.org");
		q.setCode("mm");
		obs.setValue(q);

		ourLog.debug("Observation1: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));

		assertTrue(myObservationDao.create(obs).getCreated());

		// Original value should be in Quantity index, normalized should be in normalized table
		runInTransaction(() -> {
			List<ResourceIndexedSearchParamQuantity> quantityIndexes = myResourceIndexedSearchParamQuantityDao.findAll().stream().filter(t -> t.getParamName().equals("value-quantity")).collect(Collectors.toList());
			assertEquals(1, quantityIndexes.size());
			assertEquals("149597.870691", Double.toString(quantityIndexes.get(0).getValue().doubleValue()));
			assertEquals("http://unitsofmeasure.org", quantityIndexes.get(0).getSystem());
			assertEquals("mm", quantityIndexes.get(0).getUnits());

			List<ResourceIndexedSearchParamQuantityNormalized> normalizedQuantityIndexes = myResourceIndexedSearchParamQuantityNormalizedDao.findAll().stream().filter(t -> t.getParamName().equals("value-quantity")).collect(Collectors.toList());
			assertEquals(1, normalizedQuantityIndexes.size());
			assertEquals("149.597870691", Double.toString(normalizedQuantityIndexes.get(0).getValue()));
			assertEquals("http://unitsofmeasure.org", normalizedQuantityIndexes.get(0).getSystem());
			assertEquals("m", normalizedQuantityIndexes.get(0).getUnits());
		});

		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		QuantityParam qp = new QuantityParam();
		qp.setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL);
		qp.setValue(new BigDecimal("149.597870691"));
		qp.setUnits("m");

		map.add(Observation.SP_VALUE_QUANTITY, qp);

		IBundleProvider found = myObservationDao.search(map);
		List<String> ids = toUnqualifiedVersionlessIdValues(found);

		List<IBaseResource> resources = found.getResources(0, found.sizeOrThrowNpe());

		assertThat(ids).hasSize(1);

		ourLog.debug("Observation2: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(resources.get(0)));

	}

	@Test
	public void testCreateWithNormalizedQuantitySearchSupported_LargerThanCanonicalUnit() {

		myStorageSettings.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);
		Observation obs = new Observation();
		obs.setStatus(Observation.ObservationStatus.FINAL);
		Quantity q = new Quantity();
		q.setValueElement(new DecimalType("95.7412345"));
		q.setUnit("kg/dL");
		q.setSystem("http://unitsofmeasure.org");
		q.setCode("kg/dL");
		obs.setValue(q);

		ourLog.debug("Observation1: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));

		assertTrue(myObservationDao.create(obs).getCreated());

		// Original value should be in Quantity index, normalized should be in normalized table
		runInTransaction(() -> {
			List<ResourceIndexedSearchParamQuantity> quantityIndexes = myResourceIndexedSearchParamQuantityDao.findAll().stream().filter(t -> t.getParamName().equals("value-quantity")).collect(Collectors.toList());
			assertEquals(1, quantityIndexes.size());
			assertEquals("95.7412345", Double.toString(quantityIndexes.get(0).getValue().doubleValue()));
			assertEquals("http://unitsofmeasure.org", quantityIndexes.get(0).getSystem());
			assertEquals("kg/dL", quantityIndexes.get(0).getUnits());

			List<ResourceIndexedSearchParamQuantityNormalized> normalizedQuantityIndexes = myResourceIndexedSearchParamQuantityNormalizedDao.findAll().stream().filter(t -> t.getParamName().equals("value-quantity")).collect(Collectors.toList());
			assertEquals(1, normalizedQuantityIndexes.size());
			assertEquals("9.57412345E8", Double.toString(normalizedQuantityIndexes.get(0).getValue()));
			assertEquals("http://unitsofmeasure.org", normalizedQuantityIndexes.get(0).getSystem());
			assertEquals("g.m-3", normalizedQuantityIndexes.get(0).getUnits());
		});

		SearchParameterMap map = SearchParameterMap.newSynchronous(Observation.SP_VALUE_QUANTITY, new QuantityParam()
			.setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL)
			.setValue(new BigDecimal("957412345"))
			.setUnits("g.m-3")
		);
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(map))).hasSize(1);
	}

	@Test
	public void testCreateWithNormalizedQuantitySearchSupported_NonCanonicalUnit() {

		myStorageSettings.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);
		Observation obs = new Observation();
		obs.setStatus(Observation.ObservationStatus.FINAL);
		Quantity q = new Quantity();
		q.setValueElement(new DecimalType(95.7412345));
		q.setUnit("kg/dL");
		q.setSystem("http://example.com");
		q.setCode("kg/dL");
		obs.setValue(q);

		ourLog.debug("Observation1: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));

		assertTrue(myObservationDao.create(obs).getCreated());

		// The Quantity can't be normalized, it should be stored in the non normalized quantity table only
		runInTransaction(() -> {
			List<ResourceIndexedSearchParamQuantity> quantityIndexes = myResourceIndexedSearchParamQuantityDao.findAll().stream().filter(t -> t.getParamName().equals("value-quantity")).collect(Collectors.toList());
			assertEquals(1, quantityIndexes.size());
			assertEquals("95.7412345", Double.toString(quantityIndexes.get(0).getValue().doubleValue()));
			assertEquals("http://example.com", quantityIndexes.get(0).getSystem());
			assertEquals("kg/dL", quantityIndexes.get(0).getUnits());

			List<ResourceIndexedSearchParamQuantityNormalized> normalizedQuantityIndexes = myResourceIndexedSearchParamQuantityNormalizedDao.findAll().stream().filter(t -> t.getParamName().equals("value-quantity")).collect(Collectors.toList());
			assertEquals(0, normalizedQuantityIndexes.size());
		});

		List<String> ids;

		// Search should succeed using non-normalized table
		myCaptureQueriesListener.clear();
		SearchParameterMap map = SearchParameterMap.newSynchronous(Observation.SP_VALUE_QUANTITY, new QuantityParam()
			.setSystem("http://example.com")
			.setValue(95.7412345)
			.setUnits("kg/dL")
		);
		ids = toUnqualifiedVersionlessIdValues(myObservationDao.search(map));
		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		assertThat(searchSql).contains("HFJ_SPIDX_QUANTITY t0");
		assertThat(searchSql).contains("t0.SP_VALUE = '95.7412345'");
		assertThat(ids).hasSize(1);

	}


	@Test
	public void testCreateWithNormalizedQuantityStorageSupported_SmallerThanCanonicalUnit() {

		myStorageSettings.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_STORAGE_SUPPORTED);
		Observation obs = new Observation();
		obs.setStatus(Observation.ObservationStatus.FINAL);
		Quantity q = new Quantity();
		q.setValueElement(new DecimalType(0.0000012));
		q.setUnit("MM");
		q.setSystem("http://unitsofmeasure.org");
		q.setCode("mm");
		obs.setValue(q);

		ourLog.debug("Observation1: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));

		myCaptureQueriesListener.clear();
		assertTrue(myObservationDao.create(obs).getCreated());
		myCaptureQueriesListener.logInsertQueries();

		// Original value should be in Quantity index, normalized should be in normalized table
		runInTransaction(() -> {
			List<ResourceIndexedSearchParamQuantity> quantityIndexes = myResourceIndexedSearchParamQuantityDao.findAll().stream().filter(t -> t.getParamName().equals("value-quantity")).collect(Collectors.toList());
			assertEquals(1, quantityIndexes.size());
			double d = quantityIndexes.get(0).getValue().doubleValue();
			assertEquals("1.2E-6", Double.toString(d));
			assertEquals("http://unitsofmeasure.org", quantityIndexes.get(0).getSystem());
			assertEquals("mm", quantityIndexes.get(0).getUnits());

			List<ResourceIndexedSearchParamQuantityNormalized> normalizedQuantityIndexes = myResourceIndexedSearchParamQuantityNormalizedDao.findAll().stream().filter(t -> t.getParamName().equals("value-quantity")).collect(Collectors.toList());
			assertEquals(1, normalizedQuantityIndexes.size());
			assertEquals("1.2E-9", Double.toString(normalizedQuantityIndexes.get(0).getValue()));
			assertEquals("http://unitsofmeasure.org", normalizedQuantityIndexes.get(0).getSystem());
			assertEquals("m", normalizedQuantityIndexes.get(0).getUnits());
		});

		String searchSql;
		SearchParameterMap map;
		List<String> ids;

		// Try with normalized value
		myCaptureQueriesListener.clear();
		map = SearchParameterMap.newSynchronous(Observation.SP_VALUE_QUANTITY, new QuantityParam()
			.setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL)
			.setValue(new BigDecimal("0.0000000012"))
			.setUnits("m")
		);
		ids = toUnqualifiedVersionlessIdValues(myObservationDao.search(map));
		searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		assertThat(searchSql).contains("HFJ_SPIDX_QUANTITY t0");
		assertThat(searchSql).contains("t0.SP_VALUE = '1.2E-9'");
		assertThat(ids).isEmpty();

		// Try with non-normalized value
		myCaptureQueriesListener.clear();
		map = SearchParameterMap.newSynchronous(Observation.SP_VALUE_QUANTITY, new QuantityParam()
			.setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL)
			.setValue(new BigDecimal("0.0000012"))
			.setUnits("mm")
		);
		ids = toUnqualifiedVersionlessIdValues(myObservationDao.search(map));
		searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		assertThat(searchSql).contains("HFJ_SPIDX_QUANTITY t0");
		assertThat(searchSql).contains("t0.SP_VALUE = '0.0000012'");
		assertThat(ids).hasSize(1);

		// Try with no units value
		myCaptureQueriesListener.clear();
		map = SearchParameterMap.newSynchronous(Observation.SP_VALUE_QUANTITY, new QuantityParam()
			.setValue(new BigDecimal("0.0000012"))
		);
		ids = toUnqualifiedVersionlessIdValues(myObservationDao.search(map));
		searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		assertThat(searchSql).contains("HFJ_SPIDX_QUANTITY t0");
		assertThat(searchSql).contains("t0.SP_VALUE = '0.0000012'");
		assertThat(ids).hasSize(1);
	}

	@Test
	public void testResourceWithTagCreationNoFailures() throws ExecutionException, InterruptedException {
		// we need to leave at least one free thread
		// due to a REQUIRED_NEW transaction internally
		int maxThreadsUsed = TestR4Config.ourMaxThreads - 1;
		ExecutorService pool = Executors.newFixedThreadPool(Math.min(maxThreadsUsed, 5));
		try {
			Coding tag = new Coding();
			tag.setCode("code123");
			tag.setDisplay("Display Name");
			tag.setSystem("System123");

			Patient p = new Patient();
			IIdType id = myPatientDao.create(p).getId();

			List<Future<String>> futures = new ArrayList<>();
			for (int i = 0; i < 50; i++) {
				Patient updatePatient = new Patient();
				updatePatient.setId(id.toUnqualifiedVersionless());
				updatePatient.addIdentifier().setSystem("" + i);
				updatePatient.setActive(true);
				updatePatient.getMeta().addTag(tag);

				int finalI = i;
				Future<String> future = pool.submit(() -> {
					ourLog.info("Starting update {}", finalI);
					try {
						try {
							myPatientDao.update(updatePatient);
						} catch (ResourceVersionConflictException e) {
							assertTrue(e.getMessage().contains(
								"The operation has failed with a version constraint failure. This generally means that two clients/threads were trying to update the same resource at the same time, and this request was chosen as the failing request."
							));
						}
					} catch (Exception e) {
						ourLog.error("Failure", e);
						return e.toString();
					}
					ourLog.info("Finished update {}", finalI);
					return null;
				});
				futures.add(future);
			}

			for (Future<String> next : futures) {
				String nextError = next.get();
				if (StringUtils.isNotBlank(nextError)) {
					fail(nextError);
				}
			}

		} finally {
			pool.shutdown();
		}
	}

	@Test
	public void testCreateWithNormalizedQuantitySearchNotSupported_SmallerThanCanonicalUnit() {

		myStorageSettings.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_NOT_SUPPORTED);
		Observation obs = new Observation();
		obs.setStatus(Observation.ObservationStatus.FINAL);
		Quantity q = new Quantity();
		q.setValueElement(new DecimalType(0.0000012));
		q.setUnit("MM");
		q.setSystem("http://unitsofmeasure.org");
		q.setCode("mm");
		obs.setValue(q);

		ourLog.debug("Observation1: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));

		myCaptureQueriesListener.clear();
		assertTrue(myObservationDao.create(obs).getCreated());
		myCaptureQueriesListener.logInsertQueries();

		// Original value should be in Quantity index, no normalized should be in normalized table
		runInTransaction(() -> {
			List<ResourceIndexedSearchParamQuantity> quantityIndexes = myResourceIndexedSearchParamQuantityDao.findAll().stream().filter(t -> t.getParamName().equals("value-quantity")).collect(Collectors.toList());
			assertEquals(1, quantityIndexes.size());
			double d = quantityIndexes.get(0).getValue().doubleValue();
			assertEquals("1.2E-6", Double.toString(d));
			assertEquals("http://unitsofmeasure.org", quantityIndexes.get(0).getSystem());
			assertEquals("mm", quantityIndexes.get(0).getUnits());

			List<ResourceIndexedSearchParamQuantityNormalized> normalizedQuantityIndexes = myResourceIndexedSearchParamQuantityNormalizedDao.findAll().stream().filter(t -> t.getParamName().equals("value-quantity")).collect(Collectors.toList());
			assertEquals(0, normalizedQuantityIndexes.size());
		});

		String searchSql;
		SearchParameterMap map;
		List<String> ids;

		// Try with normalized value
		myCaptureQueriesListener.clear();
		map = SearchParameterMap.newSynchronous(Observation.SP_VALUE_QUANTITY, new QuantityParam()
			.setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL)
			.setValue(new BigDecimal("0.0000000012"))
			.setUnits("m")
		);
		ids = toUnqualifiedVersionlessIdValues(myObservationDao.search(map));
		searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		assertThat(searchSql).contains("HFJ_SPIDX_QUANTITY t0");
		assertThat(searchSql).contains("t0.SP_VALUE = '1.2E-9'");
		assertThat(ids).isEmpty();

		// Try with non-normalized value
		myCaptureQueriesListener.clear();
		map = SearchParameterMap.newSynchronous(Observation.SP_VALUE_QUANTITY, new QuantityParam()
			.setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL)
			.setValue(new BigDecimal("0.0000012"))
			.setUnits("mm")
		);
		ids = toUnqualifiedVersionlessIdValues(myObservationDao.search(map));
		searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		assertThat(searchSql).contains("HFJ_SPIDX_QUANTITY t0");
		assertThat(searchSql).contains("t0.SP_VALUE = '0.0000012'");
		assertThat(ids).hasSize(1);

		// Try with no units value
		myCaptureQueriesListener.clear();
		map = SearchParameterMap.newSynchronous(Observation.SP_VALUE_QUANTITY, new QuantityParam()
			.setValue(new BigDecimal("0.0000012"))
		);
		ids = toUnqualifiedVersionlessIdValues(myObservationDao.search(map));
		searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		assertThat(searchSql).contains("HFJ_SPIDX_QUANTITY t0");
		assertThat(searchSql).contains("t0.SP_VALUE = '0.0000012'");
		assertThat(ids).hasSize(1);
	}

	@Nested
	class ConditionalCreates {
		private static final String SYSTEM = "http://tempuri.org";
		private static final String VALUE_1 = "1";
		private static final String VALUE_2 = "2";

		private final Task myTask1 = new Task()
			.setStatus(Task.TaskStatus.DRAFT)
			.setIntent(Task.TaskIntent.UNKNOWN)
			.addIdentifier(new Identifier()
				.setSystem(SYSTEM)
				.setValue(VALUE_1));

		private final Task myTask2 = new Task()
			.setStatus(Task.TaskStatus.DRAFT)
			.setIntent(Task.TaskIntent.UNKNOWN)
			.addIdentifier(new Identifier()
				.setSystem(SYSTEM)
				.setValue(VALUE_2))
			.addBasedOn(new Reference().setReference("urn:uuid:59cda086-4763-4ef0-8e36-8c90058686ea"));

		@ParameterizedTest
		@ValueSource(booleans = {true, false})
		public void testConditionalCreateDependsOnPOSTedResource(boolean theHasQuestionMark) {
			final IFhirResourceDao<Task> taskDao = getTaskDao();
			taskDao.create(myTask1, new SystemRequestDetails());

			final List<Task> allTasksPreBundle = searchAllTasks();
			assertEquals(1, allTasksPreBundle.size());
			final Task taskPreBundle = allTasksPreBundle.get(0);
			assertEquals(VALUE_1, taskPreBundle.getIdentifier().get(0).getValue());
			assertEquals(SYSTEM, taskPreBundle.getIdentifier().get(0).getSystem());

			final BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);

			final String entryConditionalTemplate = "%sidentifier=http://tempuri.org|1";
			final String matchUrl = String.format(entryConditionalTemplate, theHasQuestionMark ? "?" : "");

			bundleBuilder.addTransactionCreateEntry(myTask2)
				.conditional(matchUrl);

			final List<Bundle.BundleEntryComponent> responseEntries = sendBundleAndGetResponse(bundleBuilder.getBundle());

			assertEquals(1, responseEntries.size());

			final Bundle.BundleEntryComponent bundleEntry = responseEntries.get(0);

			assertEquals("200 OK", bundleEntry.getResponse().getStatus());

			final List<Task> allTasksPostBundle = searchAllTasks();
			assertEquals(1, allTasksPostBundle.size());
			final Task taskPostBundle = allTasksPostBundle.get(0);
			assertEquals(VALUE_1, taskPostBundle.getIdentifier().get(0).getValue());
			assertEquals(SYSTEM, taskPostBundle.getIdentifier().get(0).getSystem());
		}

		@ParameterizedTest
		@ValueSource(booleans = {true, false})
		public void testConditionalCreateDependsOnFirstEntryExisting(boolean theHasQuestionMark) {
			final BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);

			final String firstMatchUrl = "identifier=http://tempuri.org|1";
			final String secondEntryConditionalTemplate = "%sidentifier=http://tempuri.org|2&based-on=urn:uuid:59cda086-4763-4ef0-8e36-8c90058686ea";
			final String secondMatchUrl = String.format(secondEntryConditionalTemplate, theHasQuestionMark ? "?" : "");

			bundleBuilder.addTransactionCreateEntry(myTask1, "urn:uuid:59cda086-4763-4ef0-8e36-8c90058686ea")
				.conditional(firstMatchUrl);

			bundleBuilder.addTransactionCreateEntry(myTask2)
				.conditional(secondMatchUrl);

			final IBaseBundle requestBundle = bundleBuilder.getBundle();
			assertInstanceOf(Bundle.class, requestBundle);

			final List<Bundle.BundleEntryComponent> responseEntries = sendBundleAndGetResponse(requestBundle);

			assertEquals(2, responseEntries.size());
			assertEquals(Set.of("201 Created"), responseEntries.stream().map(Bundle.BundleEntryComponent::getResponse).map(Bundle.BundleEntryResponseComponent::getStatus).collect(Collectors.toUnmodifiableSet()));

			final List<Task> allTasksPostBundle = searchAllTasks();
			assertEquals(2, allTasksPostBundle.size());
			final Task taskPostBundle1 = allTasksPostBundle.get(0);
			assertEquals(VALUE_1, taskPostBundle1.getIdentifier().get(0).getValue());
			assertEquals(SYSTEM, taskPostBundle1.getIdentifier().get(0).getSystem());
			final Task taskPostBundle2 = allTasksPostBundle.get(1);
			assertEquals(VALUE_2, taskPostBundle2.getIdentifier().get(0).getValue());
			assertEquals(SYSTEM, taskPostBundle2.getIdentifier().get(0).getSystem());

			final List<Reference> task2BasedOn = taskPostBundle2.getBasedOn();
			assertEquals(1, task2BasedOn.size());
			final Reference task2BasedOnReference = task2BasedOn.get(0);
			assertEquals(taskPostBundle1.getIdElement().toUnqualifiedVersionless().asStringValue(), task2BasedOnReference.getReference());

			assertRemainingTasks(myTask1, myTask2);

			deleteExpunge(myTask2);
			assertRemainingTasks(myTask1);

			deleteExpunge(myTask1);
			assertRemainingTasks();
		}

		@ParameterizedTest
		@ValueSource(booleans = {true, false})
		void conditionalCreateSameIdentifierCrossPartition(boolean theIsSearchUrlDuplicateAcrossPartitionsEnabled) {
			myPartitionSettings.setPartitioningEnabled(true);
			myPartitionSettings.setConditionalCreateDuplicateIdentifiersEnabled(theIsSearchUrlDuplicateAcrossPartitionsEnabled);

			final PartitionEntity partitionEntity1 = new PartitionEntity();
			partitionEntity1.setId(1);
			partitionEntity1.setName("Partition-A");
			myPartitionDao.save(partitionEntity1);

			final PartitionEntity partitionEntity2 = new PartitionEntity();
			partitionEntity2.setId(2);
			partitionEntity2.setName("Partition-B");
			myPartitionDao.save(partitionEntity2);

			final BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);
			final String matchUrl = "identifier=http://tempuri.org|1";
			bundleBuilder.addTransactionCreateEntry(myTask1, "urn:uuid:59cda086-4763-4ef0-8e36-8c90058686ea")
				.conditional(matchUrl);

			final RequestPartitionId requestPartitionId1 = RequestPartitionId.fromPartitionId(1, LocalDate.now());
			final RequestPartitionId requestPartitionId2 = RequestPartitionId.fromPartitionId(2, LocalDate.now());

			final List<Bundle.BundleEntryComponent> responseEntries1 = sendBundleAndGetResponse(bundleBuilder.getBundle(), requestPartitionId1);
			assertEquals(1, responseEntries1.size());
			final Bundle.BundleEntryComponent bundleEntry1 = responseEntries1.get(0);
			assertEquals("201 Created", bundleEntry1.getResponse().getStatus());

			if (!theIsSearchUrlDuplicateAcrossPartitionsEnabled) {
				final IBaseBundle bundle = bundleBuilder.getBundle();
				assertThatThrownBy(() -> sendBundleAndGetResponse(bundle, requestPartitionId2)).isInstanceOf(ResourceVersionConflictException.class);
				return;
			}

			final List<Bundle.BundleEntryComponent> responseEntries2 = sendBundleAndGetResponse(bundleBuilder.getBundle(), requestPartitionId2);
			assertEquals(1, responseEntries2.size());
			final Bundle.BundleEntryComponent bundleEntry2 = responseEntries1.get(0);
			assertEquals("201 Created", bundleEntry2.getResponse().getStatus());

			final List<ResourceSearchUrlEntity> allSearchUrls = myResourceSearchUrlDao.findAll();

			assertThat(allSearchUrls).hasSize(2);

			final String resolvedSearchUrl = "Task?identifier=http%3A%2F%2Ftempuri.org%7C1";

			final ResourceSearchUrlEntity resourceSearchUrlEntity1 = allSearchUrls.get(0);
			final ResourceSearchUrlEntity resourceSearchUrlEntity2 = allSearchUrls.get(1);

			assertThat(resourceSearchUrlEntity1.getSearchUrl()).isEqualTo(resolvedSearchUrl);
			assertThat(resourceSearchUrlEntity1.getPartitionId()).isEqualTo(partitionEntity1.getId());

			assertThat(resourceSearchUrlEntity2.getSearchUrl()).isEqualTo(resolvedSearchUrl);
			assertThat(resourceSearchUrlEntity2.getPartitionId()).isEqualTo(partitionEntity2.getId());
		}

		private void assertRemainingTasks(Task... theExpectedTasks) {
			final List<ResourceSearchUrlEntity> searchUrlsPreDelete = myResourceSearchUrlDao.findAll();

			assertEquals(theExpectedTasks.length, searchUrlsPreDelete.size());
			assertEquals(Arrays.stream(theExpectedTasks).map(Resource::getIdElement).map(IdType::getIdPartAsLong).toList(),
						 searchUrlsPreDelete.stream().map(ResourceSearchUrlEntity::getResourcePid).toList());
		}

		private void deleteExpunge(Task theTask) {
			final JpaPid pidOrThrowException = myIdHelperService.getPidOrThrowException(theTask);
			final List<JpaPid> pidOrThrowException1 = List.of(pidOrThrowException);

			final TransactionTemplate transactionTemplate = new TransactionTemplate(getTxManager());
			transactionTemplate.execute(x -> myDeleteExpungeSvc.deleteExpunge(pidOrThrowException1, true, 10));
		}
	}

	private List<Bundle.BundleEntryComponent> sendBundleAndGetResponse(IBaseBundle theRequestBundle, RequestPartitionId thePartitionId) {
		assertThat(theRequestBundle).isInstanceOf(Bundle.class);

		final SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setRequestPartitionId(thePartitionId);
		return mySystemDao.transaction(requestDetails, (Bundle)theRequestBundle).getEntry();
	}

	private List<Bundle.BundleEntryComponent> sendBundleAndGetResponse(IBaseBundle theRequestBundle) {
		assertTrue(theRequestBundle instanceof Bundle);

		return mySystemDao.transaction(new SystemRequestDetails(), (Bundle)theRequestBundle).getEntry();
	}

	private List<Task> searchAllTasks() {
		return unsafeCast(getTaskDao().search(SearchParameterMap.newSynchronous(), new SystemRequestDetails()).getAllResources());
	}

	private IFhirResourceDao<Task> getTaskDao() {
		return unsafeCast(myDaoRegistry.getResourceDao("Task"));
	}

	@SuppressWarnings("unchecked")
	private static <T> T unsafeCast(Object theObject) {
		return (T)theObject;
	}
}
