package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.GZipUtil;
import ca.uhn.fhir.jpa.dao.r4.FhirSystemDaoR4;
import ca.uhn.fhir.jpa.interceptor.CascadingDeleteInterceptor;
import ca.uhn.fhir.jpa.model.entity.ResourceTag;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import ca.uhn.fhir.jpa.provider.SystemProviderDstu2Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.LenientErrorHandler;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.ClasspathUtil;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.dstu3.model.Appointment;
import org.hl7.fhir.dstu3.model.Attachment;
import org.hl7.fhir.dstu3.model.Binary;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryRequestComponent;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryResponseComponent;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.hl7.fhir.dstu3.model.Bundle.HTTPVerb;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.DiagnosticReport;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.EpisodeOfCare;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Medication;
import org.hl7.fhir.dstu3.model.MedicationRequest;
import org.hl7.fhir.dstu3.model.Meta;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Observation.ObservationStatus;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Quantity;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.internal.stubbing.answers.CallsRealMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

public class FhirSystemDaoDstu3Test extends BaseJpaDstu3SystemTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirSystemDaoDstu3Test.class);
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private IInterceptorService myInterceptorBroadcaster;

	@AfterEach
	public void after() {
		myDaoConfig.setAllowInlineMatchUrlReferences(false);
		myDaoConfig.setAllowMultipleDelete(new DaoConfig().isAllowMultipleDelete());
		myDaoConfig.setIndexMissingFields(new DaoConfig().getIndexMissingFields());
		myDaoConfig.setMaximumDeleteConflictQueryCount(new DaoConfig().getMaximumDeleteConflictQueryCount());
		myDaoConfig.setBundleBatchPoolSize(new DaoConfig().getBundleBatchPoolSize());
		myDaoConfig.setBundleBatchMaxPoolSize(new DaoConfig().getBundleBatchMaxPoolSize());

	}

	@BeforeEach
	public void beforeDisableResultReuse() {
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
		myDaoConfig.setBundleBatchPoolSize(1);
		myDaoConfig.setBundleBatchMaxPoolSize(1);
	}

	private Bundle createInputTransactionWithPlaceholderIdInMatchUrl(HTTPVerb theVerb) {

		Patient pat = new Patient();
		pat
			.addIdentifier()
			.setSystem("http://acme.org")
			.setValue("ID1");

		Observation obs = new Observation();
		obs
			.getCode()
			.addCoding()
			.setSystem("http://loinc.org")
			.setCode("29463-7");
		obs.setEffective(new DateTimeType("2011-09-03T11:13:00-04:00"));
		obs.setValue(new Quantity()
			.setValue(new BigDecimal("123.4"))
			.setCode("kg")
			.setSystem("http://unitsofmeasure.org")
			.setUnit("kg"));
		obs.getSubject().setReference("urn:uuid:0001");

		Observation obs2 = new Observation();
		obs2
			.getCode()
			.addCoding()
			.setSystem("http://loinc.org")
			.setCode("29463-7");
		obs2.setEffective(new DateTimeType("2017-09-03T11:13:00-04:00"));
		obs2.setValue(new Quantity()
			.setValue(new BigDecimal("123.4"))
			.setCode("kg")
			.setSystem("http://unitsofmeasure.org")
			.setUnit("kg"));
		obs2.getSubject().setReference("urn:uuid:0001");

		/*
		 * Put one observation before the patient it references, and
		 * one after it just to make sure that order doesn't matter
		 */
		Bundle input = new Bundle();
		input.setType(BundleType.TRANSACTION);

		if (theVerb == HTTPVerb.PUT) {
			input
				.addEntry()
				.setFullUrl("urn:uuid:0002")
				.setResource(obs)
				.getRequest()
				.setMethod(HTTPVerb.PUT)
				.setUrl("Observation?subject=urn:uuid:0001&code=http%3A%2F%2Floinc.org|29463-7&date=2011-09-03T11:13:00-04:00");
			input
				.addEntry()
				.setFullUrl("urn:uuid:0001")
				.setResource(pat)
				.getRequest()
				.setMethod(HTTPVerb.PUT)
				.setUrl("Patient?identifier=http%3A%2F%2Facme.org|ID1");
			input
				.addEntry()
				.setFullUrl("urn:uuid:0003")
				.setResource(obs2)
				.getRequest()
				.setMethod(HTTPVerb.PUT)
				.setUrl("Observation?subject=urn:uuid:0001&code=http%3A%2F%2Floinc.org|29463-7&date=2017-09-03T11:13:00-04:00");
		} else if (theVerb == HTTPVerb.POST) {
			input
				.addEntry()
				.setFullUrl("urn:uuid:0002")
				.setResource(obs)
				.getRequest()
				.setMethod(HTTPVerb.POST)
				.setUrl("Observation")
				.setIfNoneExist("Observation?subject=urn:uuid:0001&code=http%3A%2F%2Floinc.org|29463-7&date=2011-09-03T11:13:00-04:00");
			input
				.addEntry()
				.setFullUrl("urn:uuid:0001")
				.setResource(pat)
				.getRequest()
				.setMethod(HTTPVerb.POST)
				.setUrl("Patient")
				.setIfNoneExist("Patient?identifier=http%3A%2F%2Facme.org|ID1");
			input
				.addEntry()
				.setFullUrl("urn:uuid:0003")
				.setResource(obs2)
				.getRequest()
				.setMethod(HTTPVerb.POST)
				.setUrl("Observation")
				.setIfNoneExist("Observation?subject=urn:uuid:0001&code=http%3A%2F%2Floinc.org|29463-7&date=2017-09-03T11:13:00-04:00");
		}
		return input;
	}

	@SuppressWarnings("unchecked")
	private <T extends org.hl7.fhir.dstu3.model.Resource> T find(Bundle theBundle, Class<T> theType, int theIndex) {
		int count = 0;
		for (BundleEntryComponent nextEntry : theBundle.getEntry()) {
			if (nextEntry.getResource() != null && theType.isAssignableFrom(nextEntry.getResource().getClass())) {
				if (count == theIndex) {
					T t = (T) nextEntry.getResource();
					return t;
				}
				count++;
			}
		}
		fail();
		return null;
	}

	private Bundle loadBundle(String theFileName) throws IOException {
		String req = IOUtils.toString(FhirSystemDaoDstu3Test.class.getResourceAsStream(theFileName), StandardCharsets.UTF_8);
		return myFhirContext.newXmlParser().parseResource(Bundle.class, req);
	}

	@Test
	public void testTransactionWithDuplicateConditionalCreates2() throws IOException {
		Bundle request = myFhirContext.newJsonParser().parseResource(Bundle.class, IOUtils.toString(FhirSystemDaoR4.class.getResourceAsStream("/dstu3/duplicate-conditional-create.json"), Constants.CHARSET_UTF8));

		Bundle response = mySystemDao.transaction(null, request);

		ourLog.info("Response:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(response));

		List<String> responseTypes = response
			.getEntry()
			.stream()
			.map(t -> new org.hl7.fhir.r4.model.IdType(t.getResponse().getLocation()).getResourceType())
			.collect(Collectors.toList());
		assertThat(responseTypes.toString(), responseTypes, contains("Patient", "Encounter", "Location", "Location", "Practitioner", "ProcedureRequest", "DiagnosticReport", "Specimen", "Practitioner", "Observation", "Observation", "Observation", "Observation", "Observation", "Observation", "Observation", "Observation", "Observation"));
	}

	@Test
	public void testBatchCreateWithBadRead() {
		Bundle request = new Bundle();
		request.setType(BundleType.BATCH);

		Patient p;
		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("FOO");
		request
			.addEntry()
			.setResource(p)
			.getRequest()
			.setMethod(HTTPVerb.POST)
			.setUrl("Patient");

		request
			.addEntry()
			.getRequest()
			.setMethod(HTTPVerb.GET)
			.setUrl("Patient/BABABABA");

		Bundle response = mySystemDao.transaction(mySrd, request);
		assertEquals(2, response.getEntry().size());

		assertEquals("201 Created", response.getEntry().get(0).getResponse().getStatus());
		assertThat(response.getEntry().get(0).getResponse().getLocation(), matchesPattern(".*Patient/[0-9]+.*"));
		assertEquals("404 Not Found", response.getEntry().get(1).getResponse().getStatus());

		OperationOutcome oo = (OperationOutcome) response.getEntry().get(1).getResponse().getOutcome();
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertEquals(IssueSeverity.ERROR, oo.getIssue().get(0).getSeverity());
		assertEquals(Msg.code(2001) + "Resource Patient/BABABABA is not known", oo.getIssue().get(0).getDiagnostics());
	}

	@Test
	public void testBatchCreateWithBadSearch() {
		Bundle request = new Bundle();
		request.setType(BundleType.BATCH);

		Patient p;
		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("FOO");
		request
			.addEntry()
			.setResource(p)
			.getRequest()
			.setMethod(HTTPVerb.POST)
			.setUrl("Patient");

		request
			.addEntry()
			.getRequest()
			.setMethod(HTTPVerb.GET)
			.setUrl("Patient?foobadparam=1");

		Bundle response = mySystemDao.transaction(mySrd, request);
		assertEquals(2, response.getEntry().size());

		assertEquals("201 Created", response.getEntry().get(0).getResponse().getStatus());
		assertThat(response.getEntry().get(0).getResponse().getLocation(), matchesPattern(".*Patient/[0-9]+.*"));
		assertEquals("400 Bad Request", response.getEntry().get(1).getResponse().getStatus());

		OperationOutcome oo = (OperationOutcome) response.getEntry().get(1).getResponse().getOutcome();
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertEquals(IssueSeverity.ERROR, oo.getIssue().get(0).getSeverity());
		assertThat(oo.getIssue().get(0).getDiagnostics(), containsString("Unknown search parameter"));
	}

	@Test
	public void testCircularCreateAndDelete() {
		Encounter enc = new Encounter();
		enc.setId(IdType.newRandomUuid());

		Condition cond = new Condition();
		cond.setId(IdType.newRandomUuid());

		EpisodeOfCare ep = new EpisodeOfCare();
		ep.setId(IdType.newRandomUuid());

		enc.getEpisodeOfCareFirstRep().setReference(ep.getId());
		cond.getContext().setReference(enc.getId());
		ep.getDiagnosisFirstRep().getCondition().setReference(cond.getId());

		Bundle inputBundle = new Bundle();
		inputBundle.setType(Bundle.BundleType.TRANSACTION);
		inputBundle
			.addEntry()
			.setResource(ep)
			.setFullUrl(ep.getId())
			.getRequest().setMethod(HTTPVerb.POST);
		inputBundle
			.addEntry()
			.setResource(cond)
			.setFullUrl(cond.getId())
			.getRequest().setMethod(HTTPVerb.POST);
		inputBundle
			.addEntry()
			.setResource(enc)
			.setFullUrl(enc.getId())
			.getRequest().setMethod(HTTPVerb.POST);

		Bundle resp = mySystemDao.transaction(mySrd, inputBundle);
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));

		IdType epId = new IdType(resp.getEntry().get(0).getResponse().getLocation());
		IdType condId = new IdType(resp.getEntry().get(1).getResponse().getLocation());
		IdType encId = new IdType(resp.getEntry().get(2).getResponse().getLocation());

		// Make sure a single one can't be deleted
		try {
			myEncounterDao.delete(encId);
			fail();
		} catch (ResourceVersionConflictException e) {
			// good
		}

		/*
		 * Now delete all 3 by transaction
		 */
		inputBundle = new Bundle();
		inputBundle.setType(Bundle.BundleType.TRANSACTION);
		inputBundle
			.addEntry()
			.getRequest().setMethod(HTTPVerb.DELETE)
			.setUrl(epId.toUnqualifiedVersionless().getValue());
		inputBundle
			.addEntry()
			.getRequest().setMethod(HTTPVerb.DELETE)
			.setUrl(encId.toUnqualifiedVersionless().getValue());
		inputBundle
			.addEntry()
			.getRequest().setMethod(HTTPVerb.DELETE)
			.setUrl(condId.toUnqualifiedVersionless().getValue());

		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(inputBundle));
		resp = mySystemDao.transaction(mySrd, inputBundle);
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));

		// They should now be deleted
		try {
			myEncounterDao.read(encId.toUnqualifiedVersionless());
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

	}


	@Test
	@Disabled
	public void testProcessCollectionAsBatch() throws IOException {
		byte[] inputBytes = IOUtils.toByteArray(getClass().getResourceAsStream("/dstu3/Reilly_Libby_73.json.gz"));
		String input = GZipUtil.decompress(inputBytes);
		Bundle bundle = myFhirContext.newJsonParser().setParserErrorHandler(new LenientErrorHandler()).parseResource(Bundle.class, input);
		ourLog.info("Bundle has {} resources", bundle);

		Bundle output = mySystemDao.transaction(mySrd, bundle);
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(output));
	}


	/**
	 * See #410
	 */
	@Test
	public void testContainedArePreservedForBug410() throws IOException {
		String input = IOUtils.toString(getClass().getResourceAsStream("/bug-410-bundle.xml"), StandardCharsets.UTF_8);
		Bundle bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, input);

		Bundle output = mySystemDao.transaction(mySrd, bundle);
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(output));

		IdType id = new IdType(output.getEntry().get(1).getResponse().getLocation());
		MedicationRequest mo = myMedicationRequestDao.read(id);
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(mo));
	}

	@Test
	public void testDeleteWithHas() {
		Observation obs1 = new Observation();
		obs1.setStatus(ObservationStatus.FINAL);
		IIdType obs1id = myObservationDao.create(obs1).getId().toUnqualifiedVersionless();

		Observation obs2 = new Observation();
		obs2.setStatus(ObservationStatus.FINAL);
		IIdType obs2id = myObservationDao.create(obs2).getId().toUnqualifiedVersionless();

		DiagnosticReport rpt = new DiagnosticReport();
		rpt.addIdentifier().setSystem("foo").setValue("IDENTIFIER");
		rpt.addResult(new Reference(obs2id));
		IIdType rptId = myDiagnosticReportDao.create(rpt).getId().toUnqualifiedVersionless();

		myObservationDao.read(obs1id);
		myObservationDao.read(obs2id);

		rpt = new DiagnosticReport();
		rpt.addIdentifier().setSystem("foo").setValue("IDENTIFIER");

		Bundle b = new Bundle();
		b.addEntry().getRequest().setMethod(HTTPVerb.DELETE).setUrl("Observation?_has:DiagnosticReport:result:identifier=foo|IDENTIFIER");
		b.addEntry().setResource(rpt).getRequest().setMethod(HTTPVerb.PUT).setUrl("DiagnosticReport?identifier=foo|IDENTIFIER");
		mySystemDao.transaction(mySrd, b);

		myObservationDao.read(obs1id);
		try {
			myObservationDao.read(obs2id);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		rpt = myDiagnosticReportDao.read(rptId);
		assertThat(rpt.getResult(), empty());
	}

	@Test
	public void testMultipleUpdatesWithNoChangesDoesNotResultInAnUpdateForTransaction() {
		Bundle bundle;

		// First time
		Patient p = new Patient();
		p.setActive(true);
		bundle = new Bundle();
		bundle.setType(BundleType.TRANSACTION);
		bundle
			.addEntry()
			.setResource(p)
			.setFullUrl("Patient/A")
			.getRequest()
			.setMethod(HTTPVerb.PUT)
			.setUrl("Patient/A");
		Bundle resp = mySystemDao.transaction(mySrd, bundle);
		assertThat(resp.getEntry().get(0).getResponse().getLocation(), endsWith("Patient/A/_history/1"));

		// Second time should not result in an update
		p = new Patient();
		p.setActive(true);
		bundle = new Bundle();
		bundle.setType(BundleType.TRANSACTION);
		bundle
			.addEntry()
			.setResource(p)
			.setFullUrl("Patient/A")
			.getRequest()
			.setMethod(HTTPVerb.PUT)
			.setUrl("Patient/A");
		resp = mySystemDao.transaction(mySrd, bundle);
		assertThat(resp.getEntry().get(0).getResponse().getLocation(), endsWith("Patient/A/_history/1"));

		// And third time should not result in an update
		p = new Patient();
		p.setActive(true);
		bundle = new Bundle();
		bundle.setType(BundleType.TRANSACTION);
		bundle
			.addEntry()
			.setResource(p)
			.setFullUrl("Patient/A")
			.getRequest()
			.setMethod(HTTPVerb.PUT)
			.setUrl("Patient/A");
		resp = mySystemDao.transaction(mySrd, bundle);
		assertThat(resp.getEntry().get(0).getResponse().getLocation(), endsWith("Patient/A/_history/1"));

		myPatientDao.read(new IdType("Patient/A"));
		myPatientDao.read(new IdType("Patient/A/_history/1"));
		try {
			myPatientDao.read(new IdType("Patient/A/_history/2"));
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}
		try {
			myPatientDao.read(new IdType("Patient/A/_history/3"));
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}
	}

	/**
	 * See #1044
	 */
	@Test
	public void testStructureDefinitionInBundle() throws IOException {
		String input = IOUtils.toString(FhirSystemDaoDstu3Test.class.getResourceAsStream("/bug1044-bundle.xml"), Charsets.UTF_8);
		Bundle inputBundle = myFhirContext.newXmlParser().parseResource(Bundle.class, input);

		mySystemDao.transaction(mySrd, inputBundle);
	}

	@Test
	public void testSystemMetaOperation() {

		Meta meta = mySystemDao.metaGetOperation(mySrd);
		List<Coding> published = meta.getTag();
		assertEquals(0, published.size());

		String methodName = "testSystemMetaOperation";
		IIdType id1;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue(methodName);
			patient.addName().setFamily("Tester").addGiven("Joe");

			patient.getMeta().addTag(null, "Dog", "Puppies");
			patient.getMeta().getSecurity().add(new Coding().setSystem("seclabel:sys:1").setCode("seclabel:code:1").setDisplay("seclabel:dis:1"));
			patient.getMeta().getProfile().add(new IdType("http://profile/1"));

			id1 = myPatientDao.create(patient, mySrd).getId();
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue(methodName);
			patient.addName().setFamily("Tester").addGiven("Joe");

			patient.getMeta().addTag("http://foo", "Cat", "Kittens");
			patient.getMeta().getSecurity().add(new Coding().setSystem("seclabel:sys:2").setCode("seclabel:code:2").setDisplay("seclabel:dis:2"));
			patient.getMeta().getProfile().add(new IdType("http://profile/2"));

			myPatientDao.create(patient, mySrd);
		}

		meta = mySystemDao.metaGetOperation(mySrd);
		published = meta.getTag();
		assertEquals(2, published.size());
		assertEquals(null, published.get(0).getSystem());
		assertEquals("Dog", published.get(0).getCode());
		assertEquals("Puppies", published.get(0).getDisplay());
		assertEquals("http://foo", published.get(1).getSystem());
		assertEquals("Cat", published.get(1).getCode());
		assertEquals("Kittens", published.get(1).getDisplay());
		List<Coding> secLabels = meta.getSecurity();
		assertEquals(2, secLabels.size());
		assertEquals("seclabel:sys:1", secLabels.get(0).getSystemElement().getValue());
		assertEquals("seclabel:code:1", secLabels.get(0).getCodeElement().getValue());
		assertEquals("seclabel:dis:1", secLabels.get(0).getDisplayElement().getValue());
		assertEquals("seclabel:sys:2", secLabels.get(1).getSystemElement().getValue());
		assertEquals("seclabel:code:2", secLabels.get(1).getCodeElement().getValue());
		assertEquals("seclabel:dis:2", secLabels.get(1).getDisplayElement().getValue());
		List<UriType> profiles = meta.getProfile();
		assertEquals(2, profiles.size());
		assertEquals("http://profile/1", profiles.get(0).getValue());
		assertEquals("http://profile/2", profiles.get(1).getValue());

		myPatientDao.removeTag(id1, TagTypeEnum.TAG, null, "Dog", mySrd);
		myPatientDao.removeTag(id1, TagTypeEnum.SECURITY_LABEL, "seclabel:sys:1", "seclabel:code:1", mySrd);
		myPatientDao.removeTag(id1, TagTypeEnum.PROFILE, BaseHapiFhirDao.NS_JPA_PROFILE, "http://profile/1", mySrd);

		meta = mySystemDao.metaGetOperation(mySrd);
		published = meta.getTag();
		assertEquals(1, published.size());
		assertEquals("http://foo", published.get(0).getSystem());
		assertEquals("Cat", published.get(0).getCode());
		assertEquals("Kittens", published.get(0).getDisplay());
		secLabels = meta.getSecurity();
		assertEquals(1, secLabels.size());
		assertEquals("seclabel:sys:2", secLabels.get(0).getSystemElement().getValue());
		assertEquals("seclabel:code:2", secLabels.get(0).getCodeElement().getValue());
		assertEquals("seclabel:dis:2", secLabels.get(0).getDisplayElement().getValue());
		profiles = meta.getProfile();
		assertEquals(1, profiles.size());
		assertEquals("http://profile/2", profiles.get(0).getValue());

	}

	@Test
	public void testTransaction1() throws IOException {
		String inputBundleString = ClasspathUtil.loadResource("/david-bundle-error.json");
		Bundle bundle = myFhirContext.newJsonParser().parseResource(Bundle.class, inputBundleString);
		Bundle resp = mySystemDao.transaction(mySrd, bundle);

		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));

		assertEquals("201 Created", resp.getEntry().get(0).getResponse().getStatus());
	}

	@Test
	public void testTransactionBatchWithFailingRead() {
		String methodName = "testTransactionBatchWithFailingRead";
		Bundle request = new Bundle();
		request.setType(BundleType.BATCH);

		Patient p = new Patient();
		p.addName().setFamily(methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST);

		request.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl("Patient/THIS_ID_DOESNT_EXIST");

		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(2, resp.getEntry().size());
		assertEquals(BundleType.BATCHRESPONSE, resp.getTypeElement().getValue());

		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));
		BundleEntryResponseComponent respEntry;

		// Bundle.entry[0] is create response
		assertEquals("201 Created", resp.getEntry().get(0).getResponse().getStatus());
		assertThat(resp.getEntry().get(0).getResponse().getLocation(), startsWith("Patient/"));

		// Bundle.entry[1] is failed read response
		Resource oo = resp.getEntry().get(1).getResponse().getOutcome();
		assertEquals(OperationOutcome.class, oo.getClass());
		assertEquals(IssueSeverity.ERROR, ((OperationOutcome) oo).getIssue().get(0).getSeverityElement().getValue());
		assertEquals(Msg.code(2001) + "Resource Patient/THIS_ID_DOESNT_EXIST is not known", ((OperationOutcome) oo).getIssue().get(0).getDiagnostics());
		assertEquals("404 Not Found", resp.getEntry().get(1).getResponse().getStatus());

		// Check POST
		respEntry = resp.getEntry().get(0).getResponse();
		assertEquals("201 Created", respEntry.getStatus());
		IdType createdId = new IdType(respEntry.getLocation());
		assertEquals("Patient", createdId.getResourceType());
		myPatientDao.read(createdId, mySrd); // shouldn't fail

		// Check GET
		respEntry = resp.getEntry().get(1).getResponse();
		assertThat(respEntry.getStatus(), startsWith("404"));

	}

	@Test
	public void testTransactionCreateInlineMatchUrlWithNoMatches() {
		String methodName = "testTransactionCreateInlineMatchUrlWithNoMatches";
		Bundle request = new Bundle();

		myDaoConfig.setAllowInlineMatchUrlReferences(true);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient?identifier=urn%3Asystem%7C" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerb.POST);

		try {
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals(Msg.code(1091) + "Invalid match URL \"Patient?identifier=urn%3Asystem%7CtestTransactionCreateInlineMatchUrlWithNoMatches\" - No resources match this search", e.getMessage());
		}
	}

	@Test
	public void testTransactionCreateInlineMatchUrlWithOneMatch() {
		String methodName = "testTransactionCreateInlineMatchUrlWithOneMatch";
		Bundle request = new Bundle();

		myDaoConfig.setAllowInlineMatchUrlReferences(true);

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		IIdType id = myPatientDao.update(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient?identifier=urn%3Asystem%7C" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerb.POST);

		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(1, resp.getEntry().size());

		BundleEntryComponent respEntry = resp.getEntry().get(0);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", respEntry.getResponse().getStatus());
		assertThat(respEntry.getResponse().getLocation(), containsString("Observation/"));
		assertThat(respEntry.getResponse().getLocation(), endsWith("/_history/1"));
		assertEquals("1", respEntry.getResponse().getEtag());

		o = myObservationDao.read(new IdType(respEntry.getResponse().getLocationElement()), mySrd);
		assertEquals(id.toVersionless().getValue(), o.getSubject().getReference());
		assertEquals("1", o.getIdElement().getVersionIdPart());

	}

	@Test
	public void testTransactionCreateInlineMatchUrlWithOneMatch2() {
		String methodName = "testTransactionCreateInlineMatchUrlWithOneMatch2";
		Bundle request = new Bundle();

		myDaoConfig.setAllowInlineMatchUrlReferences(true);

		Patient p = new Patient();
		p.addName().addGiven("Heute");
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		IIdType id = myPatientDao.update(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient/?given=heute");
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerb.POST);

		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(1, resp.getEntry().size());

		BundleEntryComponent respEntry = resp.getEntry().get(0);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", respEntry.getResponse().getStatus());
		assertThat(respEntry.getResponse().getLocation(), containsString("Observation/"));
		assertThat(respEntry.getResponse().getLocation(), endsWith("/_history/1"));
		assertEquals("1", respEntry.getResponse().getEtag());

		o = myObservationDao.read(new IdType(respEntry.getResponse().getLocationElement()), mySrd);
		assertEquals(id.toVersionless().getValue(), o.getSubject().getReference());
		assertEquals("1", o.getIdElement().getVersionIdPart());

	}

	@Test
	public void testTransactionCreateInlineMatchUrlWithOneMatchLastUpdated() {
		Bundle request = new Bundle();
		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerb.POST).setIfNoneExist("Observation?_lastUpdated=gt2011-01-01");
		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(1, resp.getEntry().size());

		BundleEntryComponent respEntry = resp.getEntry().get(0);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", respEntry.getResponse().getStatus());
		assertThat(respEntry.getResponse().getLocation(), containsString("Observation/"));
		assertThat(respEntry.getResponse().getLocation(), endsWith("/_history/1"));
		assertEquals("1", respEntry.getResponse().getEtag());

		/*
		 * Second time should not update
		 */

		request = new Bundle();
		o = new Observation();
		o.getCode().setText("Some Observation");
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerb.POST).setIfNoneExist("Observation?_lastUpdated=gt2011-01-01");
		resp = mySystemDao.transaction(mySrd, request);
		assertEquals(1, resp.getEntry().size());

		respEntry = resp.getEntry().get(0);
		assertEquals(Constants.STATUS_HTTP_200_OK + " OK", respEntry.getResponse().getStatus());
		assertThat(respEntry.getResponse().getLocation(), containsString("Observation/"));
		assertThat(respEntry.getResponse().getLocation(), endsWith("/_history/1"));
		assertEquals("1", respEntry.getResponse().getEtag());

	}

	@Test
	public void testTransactionCreateInlineMatchUrlWithTwoMatches() {
		String methodName = "testTransactionCreateInlineMatchUrlWithTwoMatches";
		Bundle request = new Bundle();

		myDaoConfig.setAllowInlineMatchUrlReferences(true);

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		myPatientDao.create(p, mySrd).getId();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		myPatientDao.create(p, mySrd).getId();

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient?identifier=urn%3Asystem%7C" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerb.POST);

		try {
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals(Msg.code(1092) + "Invalid match URL \"Patient?identifier=urn%3Asystem%7CtestTransactionCreateInlineMatchUrlWithTwoMatches\" - Multiple resources match this search", e.getMessage());
		}
	}

	@Test
	public void testTransactionCreateMatchUrlWithOneMatch() {
		String methodName = "testTransactionCreateMatchUrlWithOneMatch";
		Bundle request = new Bundle();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		IIdType id = myPatientDao.update(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("Hello");
		p.setId("Patient/" + methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST).setIfNoneExist("Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerb.POST);

		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(2, resp.getEntry().size());

		BundleEntryComponent respEntry = resp.getEntry().get(0);
		assertEquals(Constants.STATUS_HTTP_200_OK + " OK", respEntry.getResponse().getStatus());
		assertThat(respEntry.getResponse().getLocation(), endsWith("Patient/" + id.getIdPart() + "/_history/1"));
		assertEquals("1", respEntry.getResponse().getEtag());

		respEntry = resp.getEntry().get(1);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", respEntry.getResponse().getStatus());
		assertThat(respEntry.getResponse().getLocation(), containsString("Observation/"));
		assertThat(respEntry.getResponse().getLocation(), endsWith("/_history/1"));
		assertEquals("1", respEntry.getResponse().getEtag());

		o = myObservationDao.read(new IdType(respEntry.getResponse().getLocationElement()), mySrd);
		assertEquals(id.toVersionless().getValue(), o.getSubject().getReference());
		assertEquals("1", o.getIdElement().getVersionIdPart());

	}

	@Test
	public void testTransactionCreateMatchUrlWithTwoMatch() {
		String methodName = "testTransactionCreateMatchUrlWithTwoMatch";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		Bundle request = new Bundle();
		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("Hello");
		p.setId("Patient/" + methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST).setIfNoneExist("Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerb.POST);

		try {
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (PreconditionFailedException e) {
			assertThat(e.getMessage(), containsString("with match URL \"Patient"));
		}
	}

	@Test
	public void testTransactionCreateMatchUrlWithZeroMatch() {
		String methodName = "testTransactionCreateMatchUrlWithZeroMatch";
		Bundle request = new Bundle();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("Hello");
		p.setId("Patient/" + methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST).setIfNoneExist("Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerb.POST);

		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(BundleType.TRANSACTIONRESPONSE, resp.getTypeElement().getValue());
		assertEquals(2, resp.getEntry().size());

		BundleEntryComponent respEntry = resp.getEntry().get(0);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", respEntry.getResponse().getStatus());
		String patientId = respEntry.getResponse().getLocation();
		assertThat(patientId, not(endsWith("Patient/" + methodName + "/_history/1")));
		assertThat(patientId, (endsWith("/_history/1")));
		assertThat(patientId, (containsString("Patient/")));
		assertEquals("1", respEntry.getResponse().getEtag());

		respEntry = resp.getEntry().get(1);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", respEntry.getResponse().getStatus());
		assertThat(respEntry.getResponse().getLocation(), containsString("Observation/"));
		assertThat(respEntry.getResponse().getLocation(), endsWith("/_history/1"));
		assertEquals("1", respEntry.getResponse().getEtag());

		o = myObservationDao.read(new IdType(respEntry.getResponse().getLocationElement()), mySrd);
		assertEquals(new IdType(patientId).toUnqualifiedVersionless().getValue(), o.getSubject().getReference());
	}


	@Test
	public void testTransactionCreateWithBadRead() {
		Bundle request = new Bundle();
		request.setType(BundleType.TRANSACTION);

		Patient p;
		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("FOO");
		request
			.addEntry()
			.setResource(p)
			.getRequest()
			.setMethod(HTTPVerb.POST)
			.setUrl("Patient");

		request
			.addEntry()
			.getRequest()
			.setMethod(HTTPVerb.GET)
			.setUrl("Patient/BABABABA");

		Bundle response = mySystemDao.transaction(mySrd, request);
		assertEquals(2, response.getEntry().size());

		assertEquals("201 Created", response.getEntry().get(0).getResponse().getStatus());
		assertThat(response.getEntry().get(0).getResponse().getLocation(), matchesPattern(".*Patient/[0-9]+.*"));
		assertEquals("404 Not Found", response.getEntry().get(1).getResponse().getStatus());

		OperationOutcome oo = (OperationOutcome) response.getEntry().get(1).getResponse().getOutcome();
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertEquals(IssueSeverity.ERROR, oo.getIssue().get(0).getSeverity());
		assertEquals(Msg.code(2001) + "Resource Patient/BABABABA is not known", oo.getIssue().get(0).getDiagnostics());
	}

	@Test
	public void testTransactionCreateWithBadSearch() {
		Bundle request = new Bundle();
		request.setType(BundleType.TRANSACTION);

		Patient p;
		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("FOO");
		request
			.addEntry()
			.setResource(p)
			.getRequest()
			.setMethod(HTTPVerb.POST)
			.setUrl("Patient");

		request
			.addEntry()
			.getRequest()
			.setMethod(HTTPVerb.GET)
			.setUrl("Patient?foobadparam=1");

		Bundle response = mySystemDao.transaction(mySrd, request);
		assertEquals(2, response.getEntry().size());

		assertEquals("201 Created", response.getEntry().get(0).getResponse().getStatus());
		assertThat(response.getEntry().get(0).getResponse().getLocation(), matchesPattern(".*Patient/[0-9]+.*"));
		assertEquals("400 Bad Request", response.getEntry().get(1).getResponse().getStatus());

		OperationOutcome oo = (OperationOutcome) response.getEntry().get(1).getResponse().getOutcome();
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(oo));
		assertEquals(IssueSeverity.ERROR, oo.getIssue().get(0).getSeverity());
		assertThat(oo.getIssue().get(0).getDiagnostics(), containsString("Unknown search parameter"));
	}

	@Test
	public void testTransactionCreateWithDuplicateMatchUrl02() {
		String methodName = "testTransactionCreateWithDuplicateMatchUrl02";
		Bundle request = new Bundle();

		Patient p;
		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST).setIfNoneExist("Patient?identifier=urn%3Asystem%7C" + methodName);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST);

		try {
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(e.getMessage(),
				Msg.code(542) + "Unable to process Transaction - Request would cause multiple resources to match URL: \"Patient?identifier=urn%3Asystem%7CtestTransactionCreateWithDuplicateMatchUrl02\". Does transaction request contain duplicates?");
		}
	}

	@Test
	public void testTransactionCreateWithInvalidMatchUrl() {
		String methodName = "testTransactionCreateWithInvalidMatchUrl";
		Bundle request = new Bundle();

		Patient p;
		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		BundleEntryRequestComponent entry = request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST);

		try {
			entry.setIfNoneExist("Patient?identifier   identifier" + methodName);
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(1744) + "Failed to parse match URL[Patient?identifier   identifiertestTransactionCreateWithInvalidMatchUrl] - URL is invalid (must not contain spaces)", e.getMessage());
		}

		try {
			entry.setIfNoneExist("Patient?identifier=");
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(518) + "Invalid match URL[Patient?identifier=] - URL has no search parameters", e.getMessage());
		}

		try {
			entry.setIfNoneExist("Patient?foo=bar");
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(488) + "Failed to parse match URL[Patient?foo=bar] - Resource type Patient does not have a parameter with name: foo", e.getMessage());
		}
	}

	@Test
	public void testTransactionCreateWithInvalidReferenceNumeric() {
		String methodName = "testTransactionCreateWithInvalidReferenceNumeric";
		Bundle request = new Bundle();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("Hello");
		p.getManagingOrganization().setReference("Organization/9999999999999999");
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST);

		try {
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Resource Organization/9999999999999999 not found, specified in path: Patient.managingOrganization"));
		}
	}

	@Test
	public void testTransactionCreateWithInvalidReferenceTextual() {
		String methodName = "testTransactionCreateWithInvalidReferenceTextual";
		Bundle request = new Bundle();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("Hello");
		p.getManagingOrganization().setReference("Organization/" + methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST);

		try {
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Resource Organization/" + methodName + " not found, specified in path: Patient.managingOrganization"));
		}
	}

	@Test
	public void testTransactionCreateWithPutUsingUrl() {
		String methodName = "testTransactionCreateWithPutUsingUrl";
		Bundle request = new Bundle();
		request.setType(BundleType.TRANSACTION);

		Observation o = new Observation();
		o.getSubject().setReference("Patient/" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerb.PUT).setUrl("Observation/a" + methodName);

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.PUT).setUrl("Patient/" + methodName);

		mySystemDao.transaction(mySrd, request);

		myObservationDao.read(new IdType("Observation/a" + methodName), mySrd);
		myPatientDao.read(new IdType("Patient/" + methodName), mySrd);
	}

	@Test
	public void testTransactionCreateWithPutUsingUrl2() throws Exception {
		Bundle request = loadBundle("/bundle-dstu3.xml");
		mySystemDao.transaction(mySrd, request);
	}

	@Test
	public void testTransactionDeleteByResourceId() {
		String methodName = "testTransactionDeleteByResourceId";

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id1 = myPatientDao.create(p1, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id1);

		Patient p2 = new Patient();
		p2.addIdentifier().setSystem("urn:system").setValue(methodName);
		p2.setId("Patient/" + methodName);
		IIdType id2 = myPatientDao.update(p2, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id2);

		Bundle request = new Bundle();

		request.addEntry().getRequest().setMethod(HTTPVerb.DELETE).setUrl("Patient/" + id1.getIdPart());
		request.addEntry().getRequest().setMethod(HTTPVerb.DELETE).setUrl("Patient/" + id2.getIdPart());

		myPatientDao.read(id1.toVersionless(), mySrd);
		myPatientDao.read(id2.toVersionless(), mySrd);

		Bundle resp = mySystemDao.transaction(mySrd, request);

		assertEquals(2, resp.getEntry().size());
		assertEquals("204 No Content", resp.getEntry().get(0).getResponse().getStatus());
		assertEquals("204 No Content", resp.getEntry().get(1).getResponse().getStatus());

		try {
			myPatientDao.read(id1.toVersionless(), mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		try {
			myPatientDao.read(id2.toVersionless(), mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

	}

	/**
	 * See #253 Test that the order of deletes is version independent
	 */
	@Test
	public void testTransactionDeleteIsOrderIndependantTargetFirst() {
		String methodName = "testTransactionDeleteIsOrderIndependantTargetFirst";

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType pid = myPatientDao.create(p1, mySrd).getId().toUnqualifiedVersionless();
		ourLog.info("Created patient, got it: {}", pid);

		Observation o1 = new Observation();
		o1.getSubject().setReferenceElement(pid);
		IIdType oid1 = myObservationDao.create(o1, mySrd).getId().toUnqualifiedVersionless();

		Observation o2 = new Observation();
		o2.addIdentifier().setValue(methodName);
		o2.getSubject().setReferenceElement(pid);
		IIdType oid2 = myObservationDao.create(o2, mySrd).getId().toUnqualifiedVersionless();

		myPatientDao.read(pid, mySrd);
		myObservationDao.read(oid1, mySrd);

		// The target is Patient, so try with it first in the bundle
		Bundle request = new Bundle();
		request.addEntry().getRequest().setMethod(HTTPVerb.DELETE).setUrl(pid.getValue());
		request.addEntry().getRequest().setMethod(HTTPVerb.DELETE).setUrl(oid1.getValue());
		request.addEntry().getRequest().setMethod(HTTPVerb.DELETE).setUrl("Observation?identifier=" + methodName);
		Bundle resp = mySystemDao.transaction(mySrd, request);

		assertEquals(3, resp.getEntry().size());
		assertEquals("204 No Content", resp.getEntry().get(0).getResponse().getStatus());
		assertEquals("204 No Content", resp.getEntry().get(1).getResponse().getStatus());
		assertEquals("204 No Content", resp.getEntry().get(2).getResponse().getStatus());

		try {
			myPatientDao.read(pid, mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		try {
			myObservationDao.read(oid1, mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		try {
			myObservationDao.read(oid2, mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

	}

	/**
	 * See #253 Test that the order of deletes is version independent
	 */
	@Test
	public void testTransactionDeleteIsOrderIndependantTargetLast() {
		String methodName = "testTransactionDeleteIsOrderIndependantTargetFirst";

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType pid = myPatientDao.create(p1, mySrd).getId().toUnqualifiedVersionless();
		ourLog.info("Created patient, got it: {}", pid);

		Observation o1 = new Observation();
		o1.getSubject().setReferenceElement(pid);
		IIdType oid1 = myObservationDao.create(o1, mySrd).getId().toUnqualifiedVersionless();

		Observation o2 = new Observation();
		o2.addIdentifier().setValue(methodName);
		o2.getSubject().setReferenceElement(pid);
		IIdType oid2 = myObservationDao.create(o2, mySrd).getId().toUnqualifiedVersionless();

		myPatientDao.read(pid, mySrd);
		myObservationDao.read(oid1, mySrd);

		// The target is Patient, so try with it last in the bundle
		Bundle request = new Bundle();
		request.addEntry().getRequest().setMethod(HTTPVerb.DELETE).setUrl(oid1.getValue());
		request.addEntry().getRequest().setMethod(HTTPVerb.DELETE).setUrl("Observation?identifier=" + methodName);
		request.addEntry().getRequest().setMethod(HTTPVerb.DELETE).setUrl(pid.getValue());
		Bundle resp = mySystemDao.transaction(mySrd, request);

		assertEquals(3, resp.getEntry().size());
		assertEquals("204 No Content", resp.getEntry().get(0).getResponse().getStatus());
		assertEquals("204 No Content", resp.getEntry().get(1).getResponse().getStatus());
		assertEquals("204 No Content", resp.getEntry().get(2).getResponse().getStatus());

		try {
			myPatientDao.read(pid, mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		try {
			myObservationDao.read(oid1, mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		try {
			myObservationDao.read(oid2, mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

	}

	@Test
	public void testTransactionDeleteMatchUrlWithOneMatch() {
		String methodName = "testTransactionDeleteMatchUrlWithOneMatch";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		Bundle request = new Bundle();
		request.addEntry().getRequest().setMethod(HTTPVerb.DELETE).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(1, resp.getEntry().size());

		BundleEntryComponent nextEntry = resp.getEntry().get(0);
		assertEquals(Constants.STATUS_HTTP_204_NO_CONTENT + " No Content", nextEntry.getResponse().getStatus());

		try {
			myPatientDao.read(id.toVersionless(), mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// ok
		}

		try {
			myPatientDao.read(new IdType("Patient/" + methodName), mySrd);
			fail();
		} catch (ResourceNotFoundException e) {
			// ok
		}

		IBundleProvider history = myPatientDao.history(id, null, null, null, mySrd);
		assertEquals(2, history.size().intValue());

		assertNotNull(ResourceMetadataKeyEnum.DELETED_AT.get((IAnyResource) history.getResources(0, 1).get(0)));
		assertNotNull(ResourceMetadataKeyEnum.DELETED_AT.get((IAnyResource) history.getResources(0, 1).get(0)).getValue());
		assertNull(ResourceMetadataKeyEnum.DELETED_AT.get((IAnyResource) history.getResources(1, 2).get(0)));

	}

	@Test
	public void testTransactionDeleteMatchUrlWithTwoMatch() {
		myDaoConfig.setAllowMultipleDelete(false);

		String methodName = "testTransactionDeleteMatchUrlWithTwoMatch";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("Hello");
		p.setId("Patient/" + methodName);

		Bundle request = new Bundle();
		request.addEntry().getRequest().setMethod(HTTPVerb.DELETE).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

		try {
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (PreconditionFailedException e) {
			assertThat(e.getMessage(), containsString("resource with match URL \"Patient?"));
		}
	}

	@Test
	public void testTransactionDeleteMatchUrlWithZeroMatch() {
		String methodName = "testTransactionDeleteMatchUrlWithZeroMatch";

		Bundle request = new Bundle();
		request.addEntry().getRequest().setMethod(HTTPVerb.DELETE).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

		// try {
		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(1, resp.getEntry().size());
		assertEquals("204 No Content", resp.getEntry().get(0).getResponse().getStatus());

		// fail();
		// } catch (ResourceNotFoundException e) {
		// assertThat(e.getMessage(), containsString("resource matching URL \"Patient?"));
		// }
	}

	@Test
	public void testTransactionDeleteNoMatchUrl() {
		String methodName = "testTransactionDeleteNoMatchUrl";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		IIdType id = myPatientDao.update(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		Bundle request = new Bundle();
		request.addEntry().getRequest().setMethod(HTTPVerb.DELETE).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

		Bundle res = mySystemDao.transaction(mySrd, request);
		assertEquals(1, res.getEntry().size());

		assertEquals(Constants.STATUS_HTTP_204_NO_CONTENT + " No Content", res.getEntry().get(0).getResponse().getStatus());

		try {
			myPatientDao.read(id.toVersionless(), mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// ok
		}
	}

	@Test
	public void testTransactionDoesNotLeavePlaceholderIds() {
		String input;
		try {
			input = IOUtils.toString(getClass().getResourceAsStream("/cdr-bundle.json"), StandardCharsets.UTF_8);
		} catch (IOException e) {
			fail(e.toString());
			return;
		}
		Bundle bundle = myFhirContext.newJsonParser().parseResource(Bundle.class, input);
		mySystemDao.transaction(mySrd, bundle);

		IBundleProvider history = mySystemDao.history(null, null, null, null);
		Bundle list = toBundle(history);
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(list));

		assertEquals(6, list.getEntry().size());

		Patient p = find(list, Patient.class, 0);
		assertTrue(p.getIdElement().isIdPartValidLong());
		assertTrue(p.getGeneralPractitionerFirstRep().getReferenceElement().isIdPartValidLong());
	}

	@Test
	public void testTransactionDoesntUpdateUnchangesResourceWithPlaceholderIds() {
		Bundle output, input;
		BundleEntryResponseComponent respEntry;
		IdType createdPatientId;
		SearchParameterMap map;
		IBundleProvider search;

		input = new Bundle();

		/*
		 * Create a transaction with a patient and an observation using
		 * placeholder IDs in them
		 */
		Patient pat = new Patient();
		pat.setId(IdType.newRandomUuid());
		pat.addIdentifier().setSystem("foo").setValue("bar");
		input
			.addEntry()
			.setResource(pat)
			.setFullUrl(pat.getId())
			.getRequest()
			.setMethod(HTTPVerb.POST)
			.setUrl("/Patient")
			.setIfNoneExist("Patient?identifier=foo|bar");
		Observation obs = new Observation();
		obs.addIdentifier().setSystem("foo").setValue("dog");
		obs.getSubject().setReference(pat.getId());
		input
			.addEntry()
			.setResource(obs)
			.getRequest()
			.setMethod(HTTPVerb.PUT)
			.setUrl("/Observation?identifier=foo|dog");
		output = mySystemDao.transaction(mySrd, input);

		/*
		 * Both resrouces should be created and have version 1
		 */
		respEntry = output.getEntry().get(0).getResponse();
		assertEquals("201 Created", respEntry.getStatus());
		createdPatientId = new IdType(respEntry.getLocation());
		assertEquals("Patient", createdPatientId.getResourceType());
		assertEquals("1", createdPatientId.getVersionIdPart());

		respEntry = output.getEntry().get(1).getResponse();
		assertEquals("201 Created", respEntry.getStatus());
		IdType createdObservationId = new IdType(respEntry.getLocation());
		assertEquals("Observation", createdObservationId.getResourceType());
		assertEquals("1", createdObservationId.getVersionIdPart());

		/*
		 * Searches for both resources should work and the reference
		 * should be substituted correctly
		 */
		// Patient
		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Patient.SP_IDENTIFIER, new TokenParam("foo", "bar"));
		search = myPatientDao.search(map);
		assertThat(toUnqualifiedVersionlessIdValues(search), contains(createdPatientId.toUnqualifiedVersionless().getValue()));
		pat = (Patient) search.getResources(0, 1).get(0);
		assertEquals("foo", pat.getIdentifierFirstRep().getSystem());
		// Observation
		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Observation.SP_IDENTIFIER, new TokenParam("foo", "dog"));
		search = myObservationDao.search(map);
		assertThat(toUnqualifiedVersionlessIdValues(search), contains(createdObservationId.toUnqualifiedVersionless().getValue()));
		obs = (Observation) search.getResources(0, 1).get(0);
		assertEquals("foo", obs.getIdentifierFirstRep().getSystem());
		assertEquals(createdPatientId.toUnqualifiedVersionless().getValue(), obs.getSubject().getReference());

		/*
		 * Now run the same transaction, which should not make any changes this time
		 * around
		 */

		input = new Bundle();
		pat = new Patient();
		pat.setId(IdType.newRandomUuid());
		pat.addIdentifier().setSystem("foo").setValue("bar");
		input
			.addEntry()
			.setResource(pat)
			.setFullUrl(pat.getId())
			.getRequest()
			.setMethod(HTTPVerb.POST)
			.setUrl("/Patient")
			.setIfNoneExist("Patient?identifier=foo|bar");
		obs = new Observation();
		obs.addIdentifier().setSystem("foo").setValue("dog");
		obs.getSubject().setReference(pat.getId());
		input
			.addEntry()
			.setResource(obs)
			.getRequest()
			.setMethod(HTTPVerb.PUT)
			.setUrl("/Observation?identifier=foo|dog");
		output = mySystemDao.transaction(mySrd, input);

		/*
		 * Should still have version 1 of both resources
		 */
		respEntry = output.getEntry().get(0).getResponse();
		assertEquals("200 OK", respEntry.getStatus());
		createdObservationId = new IdType(respEntry.getLocation());
		assertEquals("Patient", createdObservationId.getResourceType());
		assertEquals("1", createdObservationId.getVersionIdPart());

		respEntry = output.getEntry().get(1).getResponse();
		assertEquals("200 OK", respEntry.getStatus());
		createdObservationId = new IdType(respEntry.getLocation());
		assertEquals("Observation", createdObservationId.getResourceType());
		assertEquals("1", createdObservationId.getVersionIdPart());

		/*
		 * Searches for both resources should still work and the reference
		 * should be substituted correctly
		 */
		// Patient
		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Patient.SP_IDENTIFIER, new TokenParam("foo", "bar"));
		search = myPatientDao.search(map);
		assertThat(toUnqualifiedVersionlessIdValues(search), contains(createdPatientId.toUnqualifiedVersionless().getValue()));
		pat = (Patient) search.getResources(0, 1).get(0);
		assertEquals("foo", pat.getIdentifierFirstRep().getSystem());
		// Observation
		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Observation.SP_IDENTIFIER, new TokenParam("foo", "dog"));
		search = myObservationDao.search(map);
		assertThat(toUnqualifiedVersionlessIdValues(search), contains(createdObservationId.toUnqualifiedVersionless().getValue()));
		obs = (Observation) search.getResources(0, 1).get(0);
		assertEquals("foo", obs.getIdentifierFirstRep().getSystem());
		assertEquals(createdPatientId.toUnqualifiedVersionless().getValue(), obs.getSubject().getReference());

		/*
		 * Now run the transaction, but this time with an actual
		 * change to the Observation
		 */

		input = new Bundle();
		pat = new Patient();
		pat.setId(IdType.newRandomUuid());
		pat.addIdentifier().setSystem("foo").setValue("bar");
		input
			.addEntry()
			.setResource(pat)
			.setFullUrl(pat.getId())
			.getRequest()
			.setMethod(HTTPVerb.POST)
			.setUrl("/Patient")
			.setIfNoneExist("Patient?identifier=foo|bar");
		obs = new Observation();
		obs.addIdentifier().setSystem("foo").setValue("dog");
		obs.setStatus(ObservationStatus.FINAL);
		obs.getSubject().setReference(pat.getId());
		input
			.addEntry()
			.setResource(obs)
			.getRequest()
			.setMethod(HTTPVerb.PUT)
			.setUrl("/Observation?identifier=foo|dog");
		output = mySystemDao.transaction(mySrd, input);

		/*
		 * Observation should now be version 2
		 */
		respEntry = output.getEntry().get(0).getResponse();
		assertEquals("200 OK", respEntry.getStatus());
		createdObservationId = new IdType(respEntry.getLocation());
		assertEquals("Patient", createdObservationId.getResourceType());
		assertEquals("1", createdObservationId.getVersionIdPart());

		respEntry = output.getEntry().get(1).getResponse();
		assertEquals("200 OK", respEntry.getStatus());
		createdObservationId = new IdType(respEntry.getLocation());
		assertEquals("Observation", createdObservationId.getResourceType());
		assertEquals("2", createdObservationId.getVersionIdPart());

		/*
		 * Searches for both resources should still work and the reference
		 * should be substituted correctly
		 */
		// Patient
		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Patient.SP_IDENTIFIER, new TokenParam("foo", "bar"));
		search = myPatientDao.search(map);
		assertThat(toUnqualifiedVersionlessIdValues(search), contains(createdPatientId.toUnqualifiedVersionless().getValue()));
		pat = (Patient) search.getResources(0, 1).get(0);
		assertEquals("foo", pat.getIdentifierFirstRep().getSystem());
		// Observation
		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Observation.SP_IDENTIFIER, new TokenParam("foo", "dog"));
		search = myObservationDao.search(map);
		assertThat(toUnqualifiedVersionlessIdValues(search), contains(createdObservationId.toUnqualifiedVersionless().getValue()));
		obs = (Observation) search.getResources(0, 1).get(0);
		assertEquals("foo", obs.getIdentifierFirstRep().getSystem());
		assertEquals(createdPatientId.toUnqualifiedVersionless().getValue(), obs.getSubject().getReference());
		assertEquals(ObservationStatus.FINAL, obs.getStatus());

	}

	@Test
	public void testTransactionFailsWithDuplicateIds() {
		Bundle request = new Bundle();

		Patient patient1 = new Patient();
		patient1.setId(new IdType("Patient/testTransactionFailsWithDusplicateIds"));
		patient1.addIdentifier().setSystem("urn:system").setValue("testPersistWithSimpleLinkP01");
		request.addEntry().setResource(patient1).getRequest().setMethod(HTTPVerb.POST);

		Patient patient2 = new Patient();
		patient2.setId(new IdType("Patient/testTransactionFailsWithDusplicateIds"));
		patient2.addIdentifier().setSystem("urn:system").setValue("testPersistWithSimpleLinkP02");
		request.addEntry().setResource(patient2).getRequest().setMethod(HTTPVerb.POST);
		try {
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(535) + "Transaction bundle contains multiple resources with ID: Patient/testTransactionFailsWithDusplicateIds", e.getMessage());
		}
	}

	@Test
	public void testTransactionFromBundle() throws Exception {

		InputStream bundleRes = SystemProviderDstu2Test.class.getResourceAsStream("/transaction_link_patient_eve.xml");
		String bundleStr = IOUtils.toString(bundleRes, StandardCharsets.UTF_8);
		Bundle bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, bundleStr);

		Bundle resp = mySystemDao.transaction(mySrd, bundle);

		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp));

		assertThat(resp.getEntry().get(0).getResponse().getLocation(), startsWith("Patient/a555-44-4444/_history/"));
		assertThat(resp.getEntry().get(1).getResponse().getLocation(), startsWith("Patient/temp6789/_history/"));
		assertThat(resp.getEntry().get(2).getResponse().getLocation(), startsWith("Organization/GHH/_history/"));

		Patient p = myPatientDao.read(new IdType("Patient/a555-44-4444/_history/1"), mySrd);
		assertEquals("Patient/temp6789", p.getLink().get(0).getOther().getReference());
	}

	@Test
	public void testTransactionFromBundle2() throws Exception {
		String input = IOUtils.toString(getClass().getResourceAsStream("/transaction-bundle.xml"), StandardCharsets.UTF_8);
		Bundle bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, input);
		Bundle response = mySystemDao.transaction(mySrd, bundle);

		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(response));
		assertEquals("201 Created", response.getEntry().get(0).getResponse().getStatus());
		assertThat(response.getEntry().get(0).getResponse().getLocation(), matchesPattern("Practitioner/[0-9]+/_history/1"));

		/*
		 * Now a second time
		 */

		bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, input);
		response = mySystemDao.transaction(mySrd, bundle);

		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(response));
		assertEquals("200 OK", response.getEntry().get(0).getResponse().getStatus());
		assertThat(response.getEntry().get(0).getResponse().getLocation(), matchesPattern("Practitioner/[0-9]+/_history/1"));

	}

	@Test
	public void testTransactionFromBundle6() throws Exception {
		InputStream bundleRes = SystemProviderDstu2Test.class.getResourceAsStream("/simone_bundle3.xml");
		String bundle = IOUtils.toString(bundleRes, StandardCharsets.UTF_8);
		Bundle output = mySystemDao.transaction(mySrd, myFhirContext.newXmlParser().parseResource(Bundle.class, bundle));
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(output));
	}

	@Test
	public void testTransactionFromBundleJosh() throws Exception {

		InputStream bundleRes = SystemProviderDstu2Test.class.getResourceAsStream("/josh-bundle.json");
		String bundleStr = IOUtils.toString(bundleRes, StandardCharsets.UTF_8);
		Bundle bundle = myFhirContext.newJsonParser().parseResource(Bundle.class, bundleStr);

		Bundle resp = mySystemDao.transaction(mySrd, bundle);

		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp));

		assertEquals("201 Created", resp.getEntry().get(0).getResponse().getStatus());
		assertEquals("201 Created", resp.getEntry().get(1).getResponse().getStatus());
	}

	/**
	 * There is nothing here that isn't tested elsewhere, but it's useful for testing a large transaction followed
	 * by a large cascading delete
	 */
	@Test
	@Disabled
	public void testTransactionFromBundle_Slow() throws Exception {
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.DISABLED);
		myDaoConfig.setMaximumDeleteConflictQueryCount(10000);

		StopWatch sw = new StopWatch();
		sw.startTask("Parse Bundle");
		Bundle bundle = loadBundle("/dstu3/slow_bundle.xml");

		sw.startTask("Process transaction");
		Bundle resp = mySystemDao.transaction(mySrd, bundle);
		ourLog.info("Tasks: {}", sw.formatTaskDurations());

		assertEquals("201 Created", resp.getEntry().get(0).getResponse().getStatus());


		doAnswer(new CallsRealMethods()).when(mySrd).setParameters(any());
		when(mySrd.getParameters()).thenCallRealMethod();
		when(mySrd.getUserData()).thenReturn(new HashMap<>());
		Map<String, String[]> params = Maps.newHashMap();
		params.put(Constants.PARAMETER_CASCADE_DELETE, new String[]{Constants.CASCADE_DELETE});
		mySrd.setParameters(params);

		CascadingDeleteInterceptor deleteInterceptor = new CascadingDeleteInterceptor(myFhirContext, myDaoRegistry, myInterceptorBroadcaster);
		myInterceptorBroadcaster.registerInterceptor(deleteInterceptor);


		myPatientDao.deleteByUrl("Patient?identifier=http://fhir.nl/fhir/NamingSystem/bsn|900197341", mySrd);

	}

	@Test
	public void testTransactionOrdering() {
		String methodName = "testTransactionOrdering";

		//@formatter:off
		/*
		 * Transaction Order, per the spec:
		 *
		 * Process any DELETE interactions
		 * Process any POST interactions
		 * Process any PUT interactions
		 * Process any GET interactions
		 *
		 * This test creates a transaction bundle that includes
		 * these four operations in the reverse order and verifies
		 * that they are invoked correctly.
		 */
		//@formatter:off

		int pass = 0;
		IdType patientPlaceholderId = IdType.newRandomUuid();

		Bundle req = testTransactionOrderingCreateBundle(methodName, pass, patientPlaceholderId);
		Bundle resp = mySystemDao.transaction(mySrd, req);
		testTransactionOrderingValidateResponse(pass, resp);

		pass = 1;
		patientPlaceholderId = IdType.newRandomUuid();

		req = testTransactionOrderingCreateBundle(methodName, pass, patientPlaceholderId);
		resp = mySystemDao.transaction(mySrd, req);
		testTransactionOrderingValidateResponse(pass, resp);

	}

	private Bundle testTransactionOrderingCreateBundle(String methodName, int pass, IdType patientPlaceholderId) {
		Bundle req = new Bundle();
		req.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl("Patient?identifier=" + methodName);

		Observation obs = new Observation();
		obs.getSubject().setReferenceElement(patientPlaceholderId);
		obs.addIdentifier().setValue(methodName);
		obs.getCode().setText(methodName + pass);
		req.addEntry().setResource(obs).getRequest().setMethod(HTTPVerb.PUT).setUrl("Observation?identifier=" + methodName);

		Patient pat = new Patient();
		pat.addIdentifier().setValue(methodName);
		pat.addName().setFamily(methodName + pass);
		req.addEntry().setResource(pat).setFullUrl(patientPlaceholderId.getValue()).getRequest().setMethod(HTTPVerb.POST).setUrl("Patient");

		req.addEntry().getRequest().setMethod(HTTPVerb.DELETE).setUrl("Patient?identifier=" + methodName);
		return req;
	}

	private void testTransactionOrderingValidateResponse(int pass, Bundle resp) {
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));
		assertEquals(4, resp.getEntry().size());
		assertEquals("200 OK", resp.getEntry().get(0).getResponse().getStatus());
		if (pass == 0) {
			assertEquals("201 Created", resp.getEntry().get(1).getResponse().getStatus());
			assertThat(resp.getEntry().get(1).getResponse().getLocation(), startsWith("Observation/"));
			assertThat(resp.getEntry().get(1).getResponse().getLocation(), endsWith("_history/1"));
		} else {
			assertEquals("200 OK", resp.getEntry().get(1).getResponse().getStatus());
			assertThat(resp.getEntry().get(1).getResponse().getLocation(), startsWith("Observation/"));
			assertThat(resp.getEntry().get(1).getResponse().getLocation(), endsWith("_history/2"));
		}
		assertEquals("201 Created", resp.getEntry().get(2).getResponse().getStatus());
		assertThat(resp.getEntry().get(2).getResponse().getLocation(), startsWith("Patient/"));
		if (pass == 0) {
			assertEquals("204 No Content", resp.getEntry().get(3).getResponse().getStatus());
		} else {
			assertEquals("204 No Content", resp.getEntry().get(3).getResponse().getStatus());
		}


		Bundle respGetBundle = (Bundle) resp.getEntry().get(0).getResource();
		assertEquals(1, respGetBundle.getEntry().size());
		assertEquals("testTransactionOrdering" + pass, ((Patient) respGetBundle.getEntry().get(0).getResource()).getName().get(0).getFamily());
		assertThat(respGetBundle.getLink("self").getUrl(), endsWith("/Patient?identifier=testTransactionOrdering"));
	}

	@Test
	public void testTransactionOruBundle() throws IOException {
		myDaoConfig.setAllowMultipleDelete(true);

		String input = IOUtils.toString(getClass().getResourceAsStream("/oruBundle.json"), StandardCharsets.UTF_8);

		Bundle inputBundle;
		Bundle outputBundle;
		inputBundle = myFhirContext.newJsonParser().parseResource(Bundle.class, input);
		outputBundle = mySystemDao.transaction(mySrd, inputBundle);
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outputBundle));

		inputBundle = myFhirContext.newJsonParser().parseResource(Bundle.class, input);
		outputBundle = mySystemDao.transaction(mySrd, inputBundle);
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outputBundle));

		IBundleProvider allPatients = myPatientDao.search(new SearchParameterMap());
		assertEquals(1, allPatients.size().intValue());
	}


	@Test
	public void testTransactionReadWithIfNoneMatch() {
		String methodName = "testTransactionReadWithIfNoneMatch";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		IIdType idv1 = myPatientDao.update(p, mySrd).getId();
		ourLog.info("Created patient, got id: {}", idv1);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("Family Name");
		p.setId("Patient/" + methodName);
		IIdType idv2 = myPatientDao.update(p, mySrd).getId();
		ourLog.info("Updated patient, got id: {}", idv2);

		Bundle request = new Bundle();
		request.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl(idv1.toUnqualifiedVersionless().getValue());
		request.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl(idv1.toUnqualifiedVersionless().getValue()).setIfNoneMatch("W/\"" + idv1.getVersionIdPart() + "\"");
		request.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl(idv1.toUnqualifiedVersionless().getValue()).setIfNoneMatch("W/\"" + idv2.getVersionIdPart() + "\"");

		Bundle resp = mySystemDao.transaction(mySrd, request);

		assertEquals(3, resp.getEntry().size());

		BundleEntryComponent nextEntry;

		nextEntry = resp.getEntry().get(0);
		assertNotNull(nextEntry.getResource());
		assertEquals(Patient.class, nextEntry.getResource().getClass());
		assertEquals(idv2.toUnqualified(), nextEntry.getResource().getIdElement().toUnqualified());
		assertEquals("200 OK", nextEntry.getResponse().getStatus());

		nextEntry = resp.getEntry().get(1);
		assertNotNull(nextEntry.getResource());
		assertEquals(Patient.class, nextEntry.getResource().getClass());
		assertEquals(idv2.toUnqualified(), nextEntry.getResource().getIdElement().toUnqualified());
		assertEquals("200 OK", nextEntry.getResponse().getStatus());

		nextEntry = resp.getEntry().get(2);
		assertEquals("304 Not Modified", nextEntry.getResponse().getStatus());
		assertNull(nextEntry.getResource());
	}

	@Test
	public void testTransactionSearchWithCount() {
		String methodName = "testTransactionSearchWithCount";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		IIdType idv1 = myPatientDao.update(p, mySrd).getId();
		ourLog.info("Created patient, got id: {}", idv1);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("Family Name");
		p.setId("Patient/" + methodName);
		IIdType idv2 = myPatientDao.update(p, mySrd).getId();
		ourLog.info("Updated patient, got id: {}", idv2);

		Bundle request = new Bundle();
		request.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl("Patient?" + Constants.PARAM_COUNT + "=1&_total=accurate");
		Bundle resp = mySystemDao.transaction(mySrd, request);

		assertEquals(1, resp.getEntry().size());

		BundleEntryComponent nextEntry = resp.getEntry().get(0);
		assertEquals(Bundle.class, nextEntry.getResource().getClass());
		Bundle respBundle = (Bundle) nextEntry.getResource();
		assertThat(respBundle.getTotal(), greaterThan(0));

		// Invalid _count

		request = new Bundle();
		request.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl("Patient?" + Constants.PARAM_COUNT + "=GKJGKJG");
		try {
			mySystemDao.transaction(mySrd, request);
		} catch (InvalidRequestException e) {
			assertEquals(e.getMessage(), ("Invalid _count value: GKJGKJG"));
		}

		// Empty _count

		request = new Bundle();
		request.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl("Patient?" + Constants.PARAM_COUNT + "=");
		respBundle = mySystemDao.transaction(mySrd, request);
		assertThat(respBundle.getEntry().size(), greaterThan(0));
	}

	@Test
	public void testTransactionSingleEmptyResource() {

		Bundle request = new Bundle();
		request.setType(BundleType.SEARCHSET);
		Patient p = new Patient();
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST);

		try {
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(527) + "Unable to process transaction where incoming Bundle.type = searchset", e.getMessage());
		}

	}

	@Test
	public void testTransactionUpdateMatchUrlWithOneMatch() {
		String methodName = "testTransactionUpdateMatchUrlWithOneMatch";
		Bundle request = new Bundle();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("Hello");
		p.setId("Patient/" + methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.PUT).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerb.POST);

		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(2, resp.getEntry().size());

		BundleEntryComponent nextEntry = resp.getEntry().get(0);
		assertEquals("200 OK", nextEntry.getResponse().getStatus());
		assertThat(nextEntry.getResponse().getLocation(), not(containsString("test")));
		assertEquals(id.toVersionless(), p.getIdElement().toVersionless());
		assertNotEquals(id, p.getId());
		assertThat(p.getId(), endsWith("/_history/2"));

		nextEntry = resp.getEntry().get(0);
		assertEquals(Constants.STATUS_HTTP_200_OK + " OK", nextEntry.getResponse().getStatus());
		assertThat(nextEntry.getResponse().getLocation(), not(emptyString()));

		nextEntry = resp.getEntry().get(1);
		o = myObservationDao.read(new IdType(nextEntry.getResponse().getLocation()), mySrd);
		assertEquals(id.toVersionless().getValue(), o.getSubject().getReference());

	}

	@Test
	public void testTransactionUpdateMatchUrlWithTwoMatch() {
		String methodName = "testTransactionUpdateMatchUrlWithTwoMatch";
		Bundle request = new Bundle();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("Hello");
		p.setId("Patient/" + methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.PUT).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerb.POST);

		try {
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (PreconditionFailedException e) {
			assertThat(e.getMessage(), containsString("with match URL \"Patient"));
		}
	}

	@Test
	public void testTransactionUpdateMatchUrlWithZeroMatch() {
		String methodName = "testTransactionUpdateMatchUrlWithZeroMatch";
		Bundle request = new Bundle();

		Patient p = new Patient();
		p.addName().setFamily("Hello");
		IIdType id = myPatientDao.create(p, mySrd).getId();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("Hello");
		p.setId(methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.PUT).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerb.POST);

		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(2, resp.getEntry().size());

		BundleEntryComponent nextEntry = resp.getEntry().get(0);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", nextEntry.getResponse().getStatus());
		IdType patientId = new IdType(nextEntry.getResponse().getLocation());

		assertThat(nextEntry.getResponse().getLocation(), not(containsString("test")));
		assertNotEquals(id.toVersionless(), patientId.toVersionless());

		assertThat(patientId.getValue(), endsWith("/_history/1"));

		nextEntry = resp.getEntry().get(1);
		o = myObservationDao.read(new IdType(nextEntry.getResponse().getLocation()), mySrd);
		assertEquals(patientId.toVersionless().getValue(), o.getSubject().getReference());

	}

	@Test
	public void testTransactionUpdateNoMatchUrl() {
		String methodName = "testTransactionUpdateNoMatchUrl";
		Bundle request = new Bundle();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.setId("Patient/" + methodName);
		IIdType id = myPatientDao.update(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("Hello");
		p.setId("Patient/" + methodName);
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.PUT).setUrl("Patient/" + id.getIdPart());

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference("Patient/" + methodName);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerb.POST);

		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(2, resp.getEntry().size());

		BundleEntryComponent nextEntry = resp.getEntry().get(0);
		assertEquals("200 OK", nextEntry.getResponse().getStatus());

		assertThat(nextEntry.getResponse().getLocation(), (containsString("test")));
		assertEquals(id.toVersionless(), new IdType(nextEntry.getResponse().getLocation()).toVersionless());
		assertNotEquals(id, new IdType(nextEntry.getResponse().getLocation()));
		assertThat(nextEntry.getResponse().getLocation(), endsWith("/_history/2"));

		nextEntry = resp.getEntry().get(1);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", nextEntry.getResponse().getStatus());

		o = myObservationDao.read(new IdType(resp.getEntry().get(1).getResponse().getLocation()), mySrd);
		assertEquals(id.toVersionless().getValue(), o.getSubject().getReference());

	}

	@Test
	public void testTransactionUpdateTwoResourcesWithSameId() {
		Bundle request = new Bundle();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("DDD");
		p.setId("Patient/ABC");
		request.addEntry()
			.setResource(p)
			.getRequest()
			.setMethod(HTTPVerb.PUT)
			.setUrl("Patient/ABC");

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("DDD");
		p.setId("Patient/ABC");
		request.addEntry()
			.setResource(p)
			.getRequest()
			.setMethod(HTTPVerb.PUT)
			.setUrl("Patient/ABC");

		try {
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Transaction bundle contains multiple resources with ID: Patient/ABC"));
		}
	}

	@Test
	public void testTransactionWIthInvalidPlaceholder() {
		Bundle res = new Bundle();
		res.setType(BundleType.TRANSACTION);

		Observation o1 = new Observation();
		o1.setId("cid:observation1");
		o1.addIdentifier().setSystem("system").setValue("testTransactionWithRelativeOidIds02");
		res.addEntry().setResource(o1).getRequest().setMethod(HTTPVerb.POST).setUrl("Observation");

		try {
			mySystemDao.transaction(mySrd, res);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(533) + "Invalid placeholder ID found: cid:observation1 - Must be of the form 'urn:uuid:[uuid]' or 'urn:oid:[oid]'", e.getMessage());
		}
	}

	@Test
	public void testTransactionWhichFailsPersistsNothing() {

		// Run a transaction which points to that practitioner
		// in a field that isn't allowed to refer to a practitioner
		Bundle input = new Bundle();
		input.setType(BundleType.TRANSACTION);

		Patient pt = new Patient();
		pt.setId("PT");
		pt.setActive(true);
		pt.addName().setFamily("FAMILY");
		input.addEntry()
			.setResource(pt)
			.getRequest().setMethod(HTTPVerb.PUT).setUrl("Patient/PT");

		Observation obs = new Observation();
		obs.setId("OBS");
		obs.getCode().addCoding().setSystem("foo").setCode("bar");
		obs.addPerformer().setReference("Practicioner/AAAAA");
		input.addEntry()
			.setResource(obs)
			.getRequest().setMethod(HTTPVerb.PUT).setUrl("Observation/OBS");

		try {
			mySystemDao.transaction(mySrd, input);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("Resource type 'Practicioner' is not valid for this path"));
		}

		assertThat(myResourceTableDao.findAll(), empty());
		assertThat(myResourceIndexedSearchParamStringDao.findAll(), empty());

	}

	/**
	 * Format changed, source isn't valid
	 */
	@Test
	@Disabled
	public void testTransactionWithBundledValidationSourceAndTarget() throws Exception {

		InputStream bundleRes = SystemProviderDstu2Test.class.getResourceAsStream("/questionnaire-sdc-profile-example-ussg-fht.xml");
		String bundleStr = IOUtils.toString(bundleRes, StandardCharsets.UTF_8);
		Bundle bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, bundleStr);

		Bundle resp = mySystemDao.transaction(mySrd, bundle);

		String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp);
		ourLog.info(encoded);

		encoded = myFhirContext.newJsonParser().setPrettyPrint(false).encodeResourceToString(resp);
		//@formatter:off
		assertThat(encoded, containsString("\"response\":{" +
			"\"status\":\"201 Created\"," +
			"\"location\":\"Questionnaire/54127-6/_history/1\","));
		//@formatter:on

		/*
		 * Upload again to update
		 */

		resp = mySystemDao.transaction(mySrd, bundle);

		encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp);
		ourLog.info(encoded);

		encoded = myFhirContext.newJsonParser().setPrettyPrint(false).encodeResourceToString(resp);
		//@formatter:off
		assertThat(encoded, containsString("\"response\":{" +
			"\"status\":\"200 OK\"," +
			"\"location\":\"Questionnaire/54127-6/_history/2\","));
		//@formatter:on

	}

	@Test
	public void testTransactionWithCircularReferences() {
		Bundle request = new Bundle();
		request.setType(BundleType.TRANSACTION);

		Encounter enc = new Encounter();
		enc.addIdentifier().setSystem("A").setValue("1");
		enc.setId(IdType.newRandomUuid());

		Condition cond = new Condition();
		cond.addIdentifier().setSystem("A").setValue("2");
		cond.setId(IdType.newRandomUuid());

		enc.addDiagnosis().getCondition().setReference(cond.getId());
		cond.getContext().setReference(enc.getId());

		request
			.addEntry()
			.setFullUrl(enc.getId())
			.setResource(enc)
			.getRequest()
			.setMethod(HTTPVerb.PUT)
			.setUrl("Encounter?identifier=A|1");
		request
			.addEntry()
			.setFullUrl(cond.getId())
			.setResource(cond)
			.getRequest()
			.setMethod(HTTPVerb.PUT)
			.setUrl("Condition?identifier=A|2");

		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(2, resp.getEntry().size());

		BundleEntryComponent respEntry = resp.getEntry().get(0);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", respEntry.getResponse().getStatus());

		respEntry = resp.getEntry().get(1);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", respEntry.getResponse().getStatus());

	}

	@Test
	public void testTransactionWithCircularReferences2() throws IOException {
		Bundle request = loadResourceFromClasspath(Bundle.class, "/dstu3_transaction.xml");

		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(3, resp.getEntry().size());

		BundleEntryComponent respEntry = resp.getEntry().get(0);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", respEntry.getResponse().getStatus());

		respEntry = resp.getEntry().get(1);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", respEntry.getResponse().getStatus());

	}

	@Test
	public void testTransactionWithCircularReferences3() throws IOException {
		Bundle request = loadResourceFromClasspath(Bundle.class, "/dstu3_transaction2.xml");

		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(3, resp.getEntry().size());

		BundleEntryComponent respEntry = resp.getEntry().get(0);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", respEntry.getResponse().getStatus());

		respEntry = resp.getEntry().get(1);
		assertEquals(Constants.STATUS_HTTP_201_CREATED + " Created", respEntry.getResponse().getStatus());

	}

	@Test
	public void testTransactionWithIfMatch() {
		Patient p = new Patient();
		p.setId("P1");
		p.setActive(true);
		myPatientDao.update(p);

		p.setActive(false);
		Bundle b = new Bundle();
		b.setType(BundleType.TRANSACTION);
		b.addEntry()
			.setFullUrl("Patient/P1")
			.setResource(p)
			.getRequest()
			.setMethod(HTTPVerb.PUT)
			.setUrl("Patient/P1")
			.setIfMatch("2");

		try {
			mySystemDao.transaction(mySrd, b);
		} catch (ResourceVersionConflictException e) {
			assertEquals(Msg.code(550) + Msg.code(550) + Msg.code(989) + "Trying to update Patient/P1/_history/2 but this is not the current version", e.getMessage());
		}

		b = new Bundle();
		b.setType(BundleType.TRANSACTION);
		b.addEntry()
			.setFullUrl("Patient/P1")
			.setResource(p)
			.getRequest()
			.setMethod(HTTPVerb.PUT)
			.setUrl("Patient/P1")
			.setIfMatch("1");

		Bundle resp = mySystemDao.transaction(mySrd, b);
		assertEquals("Patient/P1/_history/2", new IdType(resp.getEntry().get(0).getResponse().getLocation()).toUnqualified().getValue());


	}

	@Test
	public void testTransactionWithIfNoneExist() {
		Patient p = new Patient();
		p.setId("P1");
		p.setActive(true);
		myPatientDao.update(p);

		p = new Patient();
		p.setActive(true);
		p.addName().setFamily("AAA");

		Bundle b = new Bundle();
		b.setType(BundleType.TRANSACTION);
		b.addEntry()
			.setFullUrl("Patient")
			.setResource(p)
			.getRequest()
			.setMethod(HTTPVerb.POST)
			.setUrl("Patient/P1")
			.setIfNoneExist("Patient?active=true");

		Bundle resp = mySystemDao.transaction(mySrd, b);
		assertEquals("Patient/P1/_history/1", new IdType(resp.getEntry().get(0).getResponse().getLocation()).toUnqualified().getValue());

		p = new Patient();
		p.setActive(false);
		p.addName().setFamily("AAA");

		b = new Bundle();
		b.setType(BundleType.TRANSACTION);
		b.addEntry()
			.setFullUrl("Patient")
			.setResource(p)
			.getRequest()
			.setMethod(HTTPVerb.POST)
			.setUrl("Patient/P1")
			.setIfNoneExist("Patient?active=false");

		resp = mySystemDao.transaction(mySrd, b);
		assertThat(new IdType(resp.getEntry().get(0).getResponse().getLocation()).toUnqualified().getValue(), matchesPattern("Patient/[0-9]+/_history/1"));


	}

	@Test
	public void testTransactionWithInlineMatchUrl() throws Exception {
		myDaoConfig.setAllowInlineMatchUrlReferences(true);

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("http://www.ghh.org/identifiers").setValue("condreftestpatid1");
		myPatientDao.create(patient, mySrd);

		String input = IOUtils.toString(getClass().getResourceAsStream("/simone-conditional-url.xml"), StandardCharsets.UTF_8);
		Bundle bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, input);

		Bundle response = mySystemDao.transaction(mySrd, bundle);
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(response));

	}

	@Test
	public void testTransactionWithInlineMatchUrlMultipleMatches() throws Exception {
		myDaoConfig.setAllowInlineMatchUrlReferences(true);

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("http://www.ghh.org/identifiers").setValue("condreftestpatid1");
		myPatientDao.create(patient, mySrd);

		patient = new Patient();
		patient.addIdentifier().setSystem("http://www.ghh.org/identifiers").setValue("condreftestpatid1");
		myPatientDao.create(patient, mySrd);

		String input = IOUtils.toString(getClass().getResourceAsStream("/simone-conditional-url.xml"), StandardCharsets.UTF_8);
		Bundle bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, input);

		try {
			mySystemDao.transaction(mySrd, bundle);
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals(Msg.code(1092) + "Invalid match URL \"Patient?identifier=http://www.ghh.org/identifiers|condreftestpatid1\" - Multiple resources match this search", e.getMessage());
		}

	}

	@Test
	public void testTransactionWithInlineMatchUrlNoMatches() throws Exception {
		myDaoConfig.setAllowInlineMatchUrlReferences(true);

		String input = IOUtils.toString(getClass().getResourceAsStream("/simone-conditional-url.xml"), StandardCharsets.UTF_8);
		Bundle bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, input);

		try {
			mySystemDao.transaction(mySrd, bundle);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals(Msg.code(1091) + "Invalid match URL \"Patient?identifier=http://www.ghh.org/identifiers|condreftestpatid1\" - No resources match this search", e.getMessage());
		}

	}

	@Test
	public void testTransactionWithInvalidType() {
		Bundle request = new Bundle();
		request.setType(BundleType.SEARCHSET);
		Patient p = new Patient();
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST);

		try {
			mySystemDao.transaction(mySrd, request);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(527) + "Unable to process transaction where incoming Bundle.type = searchset", e.getMessage());
		}

	}

	@Test
	public void testTransactionWithMultiBundle() throws IOException {
		String inputBundleString = ClasspathUtil.loadResource("/batch-error.xml");
		Bundle bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, inputBundleString);
		Bundle resp = mySystemDao.transaction(mySrd, bundle);

		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));

		assertEquals("201 Created", resp.getEntry().get(0).getResponse().getStatus());

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
				Set<String> values = new HashSet<>();
				for (ResourceTag next : myResourceTagDao.findAll()) {
					if (!values.add(next.toString())) {
						ourLog.info("Found duplicate tag on resource of type {}", next.getResource().getResourceType());
						ourLog.info("Tag was: {} / {}", next.getTag().getSystem(), next.getTag().getCode());
					}
				}
			}
		});

	}

	@Test
	public void testTransactionWithNullReference() {
		Patient p = new Patient();
		p.addName().setFamily("family");
		final IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		Bundle inputBundle = new Bundle();

		//@formatter:off
		Patient app0 = new Patient();
		app0.addName().setFamily("NEW PATIENT");
		String placeholderId0 = IdDt.newRandomUuid().getValue();
		inputBundle
			.addEntry()
			.setResource(app0)
			.setFullUrl(placeholderId0)
			.getRequest()
			.setMethod(HTTPVerb.POST)
			.setUrl("Patient");
		//@formatter:on

		//@formatter:off
		Appointment app1 = new Appointment();
		app1.addParticipant().getActor().setReference(id.getValue());
		inputBundle
			.addEntry()
			.setResource(app1)
			.getRequest()
			.setMethod(HTTPVerb.POST)
			.setUrl("Appointment");
		//@formatter:on

		//@formatter:off
		Appointment app2 = new Appointment();
		app2.addParticipant().getActor().setDisplay("NO REF");
		app2.addParticipant().getActor().setDisplay("YES REF").setReference(placeholderId0);
		inputBundle
			.addEntry()
			.setResource(app2)
			.getRequest()
			.setMethod(HTTPVerb.POST)
			.setUrl("Appointment");
		//@formatter:on

		Bundle outputBundle = mySystemDao.transaction(mySrd, inputBundle);
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(outputBundle));

		assertEquals(3, outputBundle.getEntry().size());
		IdDt id0 = new IdDt(outputBundle.getEntry().get(0).getResponse().getLocation());
		IdDt id2 = new IdDt(outputBundle.getEntry().get(2).getResponse().getLocation());

		app2 = myAppointmentDao.read(id2, mySrd);
		assertEquals("NO REF", app2.getParticipant().get(0).getActor().getDisplay());
		assertEquals(null, app2.getParticipant().get(0).getActor().getReference());
		assertEquals("YES REF", app2.getParticipant().get(1).getActor().getDisplay());
		assertEquals(id0.toUnqualifiedVersionless().getValue(), app2.getParticipant().get(1).getActor().getReference());
	}

	/*
	 * Make sure we are able to handle placeholder IDs in match URLs, e.g.
	 *
	 * "request": {
	 * "method": "PUT",
	 * "url": "Observation?subject=urn:uuid:8dba64a8-2aca-48fe-8b4e-8c7bf2ab695a&code=http%3A%2F%2Floinc.org|29463-7&date=2011-09-03T11:13:00-04:00"
	 * }
	 * </pre>
	 */
	@Test
	public void testTransactionWithPlaceholderIdInMatchUrlPost() {

		Bundle input = createInputTransactionWithPlaceholderIdInMatchUrl(HTTPVerb.POST);
		Bundle output = mySystemDao.transaction(null, input);

		assertEquals("201 Created", output.getEntry().get(0).getResponse().getStatus());
		assertEquals("201 Created", output.getEntry().get(1).getResponse().getStatus());
		assertEquals("201 Created", output.getEntry().get(2).getResponse().getStatus());

		Bundle input2 = createInputTransactionWithPlaceholderIdInMatchUrl(HTTPVerb.POST);
		Bundle output2 = mySystemDao.transaction(null, input2);

		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(output2));

		assertEquals("200 OK", output2.getEntry().get(0).getResponse().getStatus());
		assertEquals("200 OK", output2.getEntry().get(1).getResponse().getStatus());
		assertEquals("200 OK", output2.getEntry().get(2).getResponse().getStatus());

	}

	/*
	 * Make sure we are able to handle placeholder IDs in match URLs, e.g.
	 *
	 * "request": {
	 * "method": "PUT",
	 * "url": "Observation?subject=urn:uuid:8dba64a8-2aca-48fe-8b4e-8c7bf2ab695a&code=http%3A%2F%2Floinc.org|29463-7&date=2011-09-03T11:13:00-04:00"
	 * }
	 * </pre>
	 */
	@Test
	public void testTransactionWithPlaceholderIdInMatchUrlPut() {

		Bundle input = createInputTransactionWithPlaceholderIdInMatchUrl(HTTPVerb.PUT);
		Bundle output = mySystemDao.transaction(null, input);

		assertEquals("201 Created", output.getEntry().get(0).getResponse().getStatus());
		assertEquals("201 Created", output.getEntry().get(1).getResponse().getStatus());
		assertEquals("201 Created", output.getEntry().get(2).getResponse().getStatus());

		Bundle input2 = createInputTransactionWithPlaceholderIdInMatchUrl(HTTPVerb.PUT);
		Bundle output2 = mySystemDao.transaction(null, input2);

		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(output2));

		assertEquals("200 OK", output2.getEntry().get(0).getResponse().getStatus());
		assertEquals("200 OK", output2.getEntry().get(1).getResponse().getStatus());
		assertEquals("200 OK", output2.getEntry().get(2).getResponse().getStatus());

	}

	/**
	 * Per a message on the mailing list
	 */
	@Test
	public void testTransactionWithPostDoesntUpdate() throws Exception {

		// First bundle (name is Joshua)

		String input = IOUtils.toString(getClass().getResource("/dstu3-post1.xml"), StandardCharsets.UTF_8);
		Bundle request = myFhirContext.newXmlParser().parseResource(Bundle.class, input);
		Bundle response = mySystemDao.transaction(mySrd, request);
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(response));

		assertEquals(1, response.getEntry().size());
		assertEquals("201 Created", response.getEntry().get(0).getResponse().getStatus());
		assertEquals("1", response.getEntry().get(0).getResponse().getEtag());
		String id = response.getEntry().get(0).getResponse().getLocation();

		// Now the second (name is Adam, shouldn't get used)

		input = IOUtils.toString(getClass().getResource("/dstu3-post2.xml"), StandardCharsets.UTF_8);
		request = myFhirContext.newXmlParser().parseResource(Bundle.class, input);
		response = mySystemDao.transaction(mySrd, request);
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(response));

		assertEquals(1, response.getEntry().size());
		assertEquals("200 OK", response.getEntry().get(0).getResponse().getStatus());
		assertEquals("1", response.getEntry().get(0).getResponse().getEtag());
		String id2 = response.getEntry().get(0).getResponse().getLocation();
		assertEquals(id, id2);

		Patient patient = myPatientDao.read(new IdType(id), mySrd);
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient));
		assertEquals("Joshua", patient.getNameFirstRep().getGivenAsSingleString());
	}

	@Test
	public void testTransactionWithReferenceResource() {
		Bundle request = new Bundle();

		Patient p = new Patient();
		p.setActive(true);
		p.setId(IdType.newRandomUuid());
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST).setUrl("Patient/" + p.getId());

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setResource(p);
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerb.POST);

		Bundle resp = mySystemDao.transaction(mySrd, request);
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));

		String patientId = new IdType(resp.getEntry().get(0).getResponse().getLocation()).toUnqualifiedVersionless().getValue();
		assertThat(patientId, startsWith("Patient/"));

		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add("subject", new ReferenceParam(patientId));
		IBundleProvider found = myObservationDao.search(params);
		assertEquals(1, found.size().intValue());
	}

	@Test
	public void testTransactionWithReferenceToCreateIfNoneExist() {
		Bundle bundle = new Bundle();
		bundle.setType(BundleType.TRANSACTION);

		Medication med = new Medication();
		IdType medId = IdType.newRandomUuid();
		med.setId(medId);
		med.getCode().addCoding().setSystem("billscodes").setCode("theCode");
		bundle.addEntry().setResource(med).setFullUrl(medId.getValue()).getRequest().setMethod(HTTPVerb.POST).setIfNoneExist("Medication?code=billscodes|theCode");

		MedicationRequest mo = new MedicationRequest();
		mo.setMedication(new Reference(medId));
		bundle.addEntry().setResource(mo).setFullUrl(mo.getIdElement().getValue()).getRequest().setMethod(HTTPVerb.POST);

		ourLog.info("Request:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

		Bundle outcome = mySystemDao.transaction(mySrd, bundle);
		ourLog.info("Response:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		IdType medId1 = new IdType(outcome.getEntry().get(0).getResponse().getLocation());
		IdType medOrderId1 = new IdType(outcome.getEntry().get(1).getResponse().getLocation());

		/*
		 * Again!
		 */

		bundle = new Bundle();
		bundle.setType(BundleType.TRANSACTION);

		med = new Medication();
		medId = IdType.newRandomUuid();
		med.getCode().addCoding().setSystem("billscodes").setCode("theCode");
		bundle.addEntry().setResource(med).setFullUrl(medId.getValue()).getRequest().setMethod(HTTPVerb.POST).setIfNoneExist("Medication?code=billscodes|theCode");

		mo = new MedicationRequest();
		mo.setMedication(new Reference(medId));
		bundle.addEntry().setResource(mo).setFullUrl(mo.getIdElement().getValue()).getRequest().setMethod(HTTPVerb.POST);

		outcome = mySystemDao.transaction(mySrd, bundle);

		IdType medId2 = new IdType(outcome.getEntry().get(0).getResponse().getLocation());
		IdType medOrderId2 = new IdType(outcome.getEntry().get(1).getResponse().getLocation());

		assertTrue(medId1.isIdPartValidLong());
		assertTrue(medId2.isIdPartValidLong());
		assertTrue(medOrderId1.isIdPartValidLong());
		assertTrue(medOrderId2.isIdPartValidLong());

		assertEquals(medId1, medId2);
		assertNotEquals(medOrderId1, medOrderId2);
	}

	@Test
	public void testTransactionWithReferenceUuid() {
		Bundle request = new Bundle();

		Patient p = new Patient();
		p.setActive(true);
		p.setId(IdType.newRandomUuid());
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST);

		Observation o = new Observation();
		o.getCode().setText("Some Observation");
		o.getSubject().setReference(p.getId());
		request.addEntry().setResource(o).getRequest().setMethod(HTTPVerb.POST);

		Bundle resp = mySystemDao.transaction(mySrd, request);
		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));

		String patientId = new IdType(resp.getEntry().get(0).getResponse().getLocation()).toUnqualifiedVersionless().getValue();
		assertThat(patientId, startsWith("Patient/"));

		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add("subject", new ReferenceParam(patientId));
		IBundleProvider found = myObservationDao.search(params);
		assertEquals(1, found.size().intValue());
	}

	@Test
	public void testTransactionWithRelativeOidIds() {
		Bundle res = new Bundle();
		res.setType(BundleType.TRANSACTION);

		Patient p1 = new Patient();
		p1.setId("urn:oid:0.1.2.3");
		p1.addIdentifier().setSystem("system").setValue("testTransactionWithRelativeOidIds01");
		res.addEntry().setResource(p1).getRequest().setMethod(HTTPVerb.POST).setUrl("Patient");

		Observation o1 = new Observation();
		o1.addIdentifier().setSystem("system").setValue("testTransactionWithRelativeOidIds02");
		o1.setSubject(new Reference("urn:oid:0.1.2.3"));
		res.addEntry().setResource(o1).getRequest().setMethod(HTTPVerb.POST).setUrl("Observation");

		Observation o2 = new Observation();
		o2.addIdentifier().setSystem("system").setValue("testTransactionWithRelativeOidIds03");
		o2.setSubject(new Reference("urn:oid:0.1.2.3"));
		res.addEntry().setResource(o2).getRequest().setMethod(HTTPVerb.POST).setUrl("Observation");

		Bundle resp = mySystemDao.transaction(mySrd, res);

		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));

		assertEquals(BundleType.TRANSACTIONRESPONSE, resp.getTypeElement().getValue());
		assertEquals(3, resp.getEntry().size());

		assertTrue(new IdType(resp.getEntry().get(0).getResponse().getLocation()).getIdPart().matches("^[0-9]+$"), resp.getEntry().get(0).getResponse().getLocation());
		assertTrue(new IdType(resp.getEntry().get(1).getResponse().getLocation()).getIdPart().matches("^[0-9]+$"), resp.getEntry().get(1).getResponse().getLocation());
		assertTrue(new IdType(resp.getEntry().get(2).getResponse().getLocation()).getIdPart().matches("^[0-9]+$"), resp.getEntry().get(2).getResponse().getLocation());

		o1 = myObservationDao.read(new IdType(resp.getEntry().get(1).getResponse().getLocation()), mySrd);
		o2 = myObservationDao.read(new IdType(resp.getEntry().get(2).getResponse().getLocation()), mySrd);
		assertThat(o1.getSubject().getReferenceElement().getValue(), endsWith("Patient/" + p1.getIdElement().getIdPart()));
		assertThat(o2.getSubject().getReferenceElement().getValue(), endsWith("Patient/" + p1.getIdElement().getIdPart()));

	}

	/**
	 * This is not the correct way to do it, but we'll allow it to be lenient
	 */
	@Test
	public void testTransactionWithRelativeOidIdsQualified() {
		Bundle res = new Bundle();
		res.setType(BundleType.TRANSACTION);

		Patient p1 = new Patient();
		p1.setId("urn:oid:0.1.2.3");
		p1.addIdentifier().setSystem("system").setValue("testTransactionWithRelativeOidIds01");
		res.addEntry().setResource(p1).getRequest().setMethod(HTTPVerb.POST).setUrl("Patient");

		Observation o1 = new Observation();
		o1.addIdentifier().setSystem("system").setValue("testTransactionWithRelativeOidIds02");
		o1.setSubject(new Reference("Patient/urn:oid:0.1.2.3"));
		res.addEntry().setResource(o1).getRequest().setMethod(HTTPVerb.POST).setUrl("Observation");

		Observation o2 = new Observation();
		o2.addIdentifier().setSystem("system").setValue("testTransactionWithRelativeOidIds03");
		o2.setSubject(new Reference("Patient/urn:oid:0.1.2.3"));
		res.addEntry().setResource(o2).getRequest().setMethod(HTTPVerb.POST).setUrl("Observation");

		Bundle resp = mySystemDao.transaction(mySrd, res);

		ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp));

		assertEquals(BundleType.TRANSACTIONRESPONSE, resp.getTypeElement().getValue());
		assertEquals(3, resp.getEntry().size());

		assertTrue(new IdType(resp.getEntry().get(0).getResponse().getLocation()).getIdPart().matches("^[0-9]+$"), resp.getEntry().get(0).getResponse().getLocation());
		assertTrue(new IdType(resp.getEntry().get(1).getResponse().getLocation()).getIdPart().matches("^[0-9]+$"), resp.getEntry().get(1).getResponse().getLocation());
		assertTrue(new IdType(resp.getEntry().get(2).getResponse().getLocation()).getIdPart().matches("^[0-9]+$"), resp.getEntry().get(2).getResponse().getLocation());

		o1 = myObservationDao.read(new IdType(resp.getEntry().get(1).getResponse().getLocation()), mySrd);
		o2 = myObservationDao.read(new IdType(resp.getEntry().get(2).getResponse().getLocation()), mySrd);
		assertThat(o1.getSubject().getReferenceElement().getValue(), endsWith("Patient/" + p1.getIdElement().getIdPart()));
		assertThat(o2.getSubject().getReferenceElement().getValue(), endsWith("Patient/" + p1.getIdElement().getIdPart()));

	}

	@Test
	public void testTransactionWithReplacement() {
		byte[] bytes = new byte[]{0, 1, 2, 3, 4};

		Binary binary = new Binary();
		binary.setId(IdType.newRandomUuid());
		binary.setContent(bytes);
		binary.setContentType("application/pdf");

		DiagnosticReport dr = new DiagnosticReport();
		dr.setId(IdDt.newRandomUuid());

		Attachment attachment = new Attachment();
		attachment.setContentType("application/pdf");
		attachment.setUrl(binary.getId()); // this one has substitution
		dr.addPresentedForm(attachment);

		Attachment attachment2 = new Attachment();
		attachment2.setUrl(IdType.newRandomUuid().getValue()); // this one has no substitution
		dr.addPresentedForm(attachment2);

		Bundle transactionBundle = new Bundle();
		transactionBundle.setType(BundleType.TRANSACTION);

		Bundle.BundleEntryComponent binaryEntry = new Bundle.BundleEntryComponent();
		binaryEntry.setResource(binary).setFullUrl(binary.getId()).getRequest().setUrl("Binary").setMethod(Bundle.HTTPVerb.POST);
		transactionBundle.addEntry(binaryEntry);

		Bundle.BundleEntryComponent drEntry = new Bundle.BundleEntryComponent();
		drEntry.setResource(dr).setFullUrl(dr.getId()).getRequest().setUrl("DiagnosticReport").setMethod(Bundle.HTTPVerb.POST);
		transactionBundle.addEntry(drEntry);

		Bundle transactionResp = mySystemDao.transaction(mySrd, transactionBundle);

		assertEquals(2, transactionResp.getEntry().size());

		// Validate Binary
		binary = myBinaryDao.read(new IdType(transactionResp.getEntry().get(0).getResponse().getLocation()));
		assertArrayEquals(bytes, binary.getContent());

		// Validate DiagnosticReport
		dr = myDiagnosticReportDao.read(new IdType(transactionResp.getEntry().get(1).getResponse().getLocation()));
		assertEquals(binary.getIdElement().toUnqualifiedVersionless().getValue(), dr.getPresentedForm().get(0).getUrl());
		assertEquals(attachment2.getUrl(), dr.getPresentedForm().get(1).getUrl());
	}

	/**
	 * See #467
	 */
	@Test
	public void testTransactionWithSelfReferentialLink() {
		/*
		 * Link to each other
		 */
		Bundle request = new Bundle();

		Organization o1 = new Organization();
		o1.setId(IdType.newRandomUuid());
		o1.setName("ORG1");
		request.addEntry().setResource(o1).getRequest().setMethod(HTTPVerb.POST);

		Organization o2 = new Organization();
		o2.setName("ORG2");
		o2.setId(IdType.newRandomUuid());
		request.addEntry().setResource(o2).getRequest().setMethod(HTTPVerb.POST);

		o1.getPartOf().setReference(o2.getId());
		o2.getPartOf().setReference(o1.getId());

		Bundle resp = mySystemDao.transaction(mySrd, request);
		assertEquals(BundleType.TRANSACTIONRESPONSE, resp.getTypeElement().getValue());
		assertEquals(2, resp.getEntry().size());

		IdType id1 = new IdType(resp.getEntry().get(0).getResponse().getLocation());
		IdType id2 = new IdType(resp.getEntry().get(1).getResponse().getLocation());

		ourLog.info("ID1: {}", id1);

		SearchParameterMap map = new SearchParameterMap();
		map.add(Organization.SP_PARTOF, new ReferenceParam(id1.toUnqualifiedVersionless().getValue()));
		IBundleProvider res = myOrganizationDao.search(map);
		assertEquals(1, res.size().intValue());
		assertEquals(id2.toUnqualifiedVersionless().getValue(), res.getResources(0, 1).get(0).getIdElement().toUnqualifiedVersionless().getValue());

		map = new SearchParameterMap();
		map.add(Organization.SP_PARTOF, new ReferenceParam(id2.toUnqualifiedVersionless().getValue()));
		res = myOrganizationDao.search(map);
		assertEquals(1, res.size().intValue());
		assertEquals(id1.toUnqualifiedVersionless().getValue(), res.getResources(0, 1).get(0).getIdElement().toUnqualifiedVersionless().getValue());

		/*
		 * Link to self
		 */
		request = new Bundle();

		o1 = new Organization();
		o1.setId(id1);
		o1.setName("ORG1");
		request.addEntry().setResource(o1).getRequest().setMethod(HTTPVerb.PUT).setUrl(id1.toUnqualifiedVersionless().getValue());

		o2 = new Organization();
		o2.setName("ORG2");
		o2.setId(id2);
		request.addEntry().setResource(o2).getRequest().setMethod(HTTPVerb.PUT).setUrl(id2.toUnqualifiedVersionless().getValue());

		o1.getPartOf().setReference(o1.getId());
		o2.getPartOf().setReference(o2.getId());

		resp = mySystemDao.transaction(mySrd, request);
		assertEquals(BundleType.TRANSACTIONRESPONSE, resp.getTypeElement().getValue());
		assertEquals(2, resp.getEntry().size());

		id1 = new IdType(resp.getEntry().get(0).getResponse().getLocation());
		id2 = new IdType(resp.getEntry().get(1).getResponse().getLocation());

		ourLog.info("ID1: {}", id1);

		map = new SearchParameterMap();
		map.add(Organization.SP_PARTOF, new ReferenceParam(id1.toUnqualifiedVersionless().getValue()));
		res = myOrganizationDao.search(map);
		assertEquals(1, res.size().intValue());
		assertEquals(id1.toUnqualifiedVersionless().getValue(), res.getResources(0, 1).get(0).getIdElement().toUnqualifiedVersionless().getValue());

		map = new SearchParameterMap();
		map.add(Organization.SP_PARTOF, new ReferenceParam(id2.toUnqualifiedVersionless().getValue()));
		res = myOrganizationDao.search(map);
		assertEquals(1, res.size().intValue());
		assertEquals(id2.toUnqualifiedVersionless().getValue(), res.getResources(0, 1).get(0).getIdElement().toUnqualifiedVersionless().getValue());

	}


}
