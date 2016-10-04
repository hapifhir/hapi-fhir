package ca.uhn.fhir.jpa.dao.dstu2;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.hamcrest.core.StringContains;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.AfterClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.jpa.dao.FhirResourceDaoDstu2;
import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.dao.dstu3.FhirResourceDaoDstu3Test;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.entity.TagTypeEnum;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.MetaDt;
import ca.uhn.fhir.model.dstu2.composite.PeriodDt;
import ca.uhn.fhir.model.dstu2.composite.QuantityDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.BaseResource;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.ConceptMap;
import ca.uhn.fhir.model.dstu2.resource.Device;
import ca.uhn.fhir.model.dstu2.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu2.resource.Encounter;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.resource.Questionnaire;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.HTTPVerbEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.QuantityComparatorEnum;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.model.valueset.BundleEntryTransactionMethodEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.util.TestUtil;

@SuppressWarnings("unchecked")
public class FhirResourceDaoDstu2Test extends BaseJpaDstu2Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu2Test.class);

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


	private void assertGone(IIdType theId) {
		try {
			assertNotGone(theId);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}
	}

	@Test
	public void testCreateBundleAllowsDocumentAndCollection() {
		String methodName = "testCreateBundleAllowsDocumentAndCollection";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType pid = myPatientDao.create(p, mySrd).getId();
		p.setId(pid);
		ourLog.info("Created patient, got it: {}", pid);

		Bundle bundle = new Bundle();
		bundle.setType((BundleTypeEnum)null);
		bundle.addEntry().setResource(p).setFullUrl(pid.toUnqualifiedVersionless().getValue());
		try {
			myBundleDao.create(bundle, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("Unable to store a Bundle resource on this server with a Bundle.type of: (missing)", e.getMessage());
		}

		bundle = new Bundle();
		bundle.setType(BundleTypeEnum.BATCH_RESPONSE);
		bundle.addEntry().setResource(p).setFullUrl(pid.toUnqualifiedVersionless().getValue());
		try {
			myBundleDao.create(bundle, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("Unable to store a Bundle resource on this server with a Bundle.type of: batch-response", e.getMessage());
		}

		bundle = new Bundle();
		bundle.setType(BundleTypeEnum.COLLECTION);
		bundle.addEntry().setResource(p).setFullUrl(pid.toUnqualifiedVersionless().getValue());
		myBundleDao.create(bundle, mySrd);

		bundle = new Bundle();
		bundle.setType(BundleTypeEnum.DOCUMENT);
		bundle.addEntry().setResource(p).setFullUrl(pid.toUnqualifiedVersionless().getValue());
		myBundleDao.create(bundle, mySrd);

	}

	
	/**
	 * This gets called from assertGone too! Careful about exceptions...
	 */
	private void assertNotGone(IIdType theId) {
		if ("Patient".equals(theId.getResourceType())) {
			myPatientDao.read(theId, mySrd);
		} else if ("Organization".equals(theId.getResourceType())){
			myOrganizationDao.read(theId, mySrd);
		} else {
			fail("No type");
		}
	}

	private List<String> extractNames(IBundleProvider theSearch) {
		ArrayList<String> retVal = new ArrayList<String>();
		for (IBaseResource next : theSearch.getResources(0, theSearch.size())) {
			Patient nextPt = (Patient) next;
			retVal.add(nextPt.getNameFirstRep().getNameAsSingleString());
		}
		return retVal;
	}

	private String log(IBundleProvider theHistory) {
		StringBuilder b =new StringBuilder(theHistory.size() + " results: ");
		for (IBaseResource next : theHistory.getResources(0, theHistory.size())) {
			b.append("\n ").append(next.getIdElement().toUnqualified().getValue());
		}
		return b.toString();
	}

	private void sort(TagList thePublished) {
		ArrayList<Tag> tags = new ArrayList<Tag>(thePublished);
		Collections.sort(tags, new Comparator<Tag>() {
			@Override
			public int compare(Tag theO1, Tag theO2) {
				int retVal = defaultString(theO1.getScheme()).compareTo(defaultString(theO2.getScheme()));
				if (retVal == 0) {
					retVal = defaultString(theO1.getTerm()).compareTo(defaultString(theO2.getTerm()));
				}
				return retVal;
			}
		});
		thePublished.clear();
		for (Tag next : tags) {
			thePublished.add(next);
		}
	}

	private void sortCodings(List<BaseCodingDt> theSecLabels) {
		Collections.sort(theSecLabels, new Comparator<BaseCodingDt>() {
			@Override
			public int compare(BaseCodingDt theO1, BaseCodingDt theO2) {
				return theO1.getSystemElement().getValue().compareTo(theO2.getSystemElement().getValue());
			}
		});
	}

	private List<IdDt> sortIds(List<IdDt> theProfiles) {
		ArrayList<IdDt> retVal = new ArrayList<IdDt>(theProfiles);
		Collections.sort(retVal, new Comparator<IdDt>() {
			@Override
			public int compare(IdDt theO1, IdDt theO2) {
				return theO1.getValue().compareTo(theO2.getValue());
			}
		});
		return retVal;
	}

	@Test
	public void testCantSearchForDeletedResourceByLanguageOrTag() {
		String methodName = "testCantSearchForDeletedResourceByLanguageOrTag";
		Organization org = new Organization();
		org.setLanguage(new CodeDt("EN_ca"));
		org.setName(methodName);

		TagList tl = new TagList();
		tl.add(new Tag(methodName, methodName));
		ResourceMetadataKeyEnum.TAG_LIST.put(org, tl);

		IIdType orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap map = new SearchParameterMap();
		map.add("_language", new StringParam("EN_ca"));
		assertEquals(1, myOrganizationDao.search(map).size());

		map = new SearchParameterMap();
		map.add("_tag", new TokenParam(methodName, methodName));
		assertEquals(1, myOrganizationDao.search(map).size());

		myOrganizationDao.delete(orgId, mySrd);

		map = new SearchParameterMap();
		map.add("_language", new StringParam("EN_ca"));
		assertEquals(0, myOrganizationDao.search(map).size());

		map = new SearchParameterMap();
		map.add("_tag", new TokenParam(methodName, methodName));
		assertEquals(0, myOrganizationDao.search(map).size());
	}

	@Test
	public void testChoiceParamConcept() {
		Observation o1 = new Observation();
		o1.getCode().addCoding().setSystem("foo").setCode("testChoiceParam01");
		o1.setValue(new CodeableConceptDt("testChoiceParam01CCS", "testChoiceParam01CCV"));
		IIdType id1 = myObservationDao.create(o1, mySrd).getId();

		{
			IBundleProvider found = myObservationDao.search(Observation.SP_VALUE_CONCEPT, new TokenParam("testChoiceParam01CCS", "testChoiceParam01CCV"));
			assertEquals(1, found.size());
			assertEquals(id1, found.getResources(0, 1).get(0).getIdElement());
		}
	}

	@Test
	public void testChoiceParamDate() {
		Observation o2 = new Observation();
		o2.getCode().addCoding().setSystem("foo").setCode("testChoiceParam02");
		o2.setValue(new PeriodDt().setStart(new DateTimeDt("2001-01-01")).setEnd(new DateTimeDt("2001-01-03")));
		IIdType id2 = myObservationDao.create(o2, mySrd).getId();

		{
			IBundleProvider found = myObservationDao.search(Observation.SP_VALUE_DATE, new DateParam("2001"));
			assertEquals(1, found.size());
			assertEquals(id2, found.getResources(0, 1).get(0).getIdElement());
		}
	}

	@Test
	public void testChoiceParamDateAlt() {
		Observation o2 = new Observation();
		o2.getCode().addCoding().setSystem("foo").setCode("testChoiceParamDateAlt02");
		o2.setEffective(new DateTimeDt("2015-03-08T11:11:11"));
		IIdType id2 = myObservationDao.create(o2, mySrd).getId();

		{
			Set<Long> found = myObservationDao.searchForIds(Observation.SP_DATE, new DateParam(">2001-01-02"));
			assertThat(found, hasItem(id2.getIdPartAsLong()));
		}
		{
			Set<Long> found = myObservationDao.searchForIds(Observation.SP_DATE, new DateParam(">2016-01-02"));
			assertThat(found, not(hasItem(id2.getIdPartAsLong())));
		}
	}

	@Test
	public void testChoiceParamQuantity() {
		Observation o3 = new Observation();
		o3.getCode().addCoding().setSystem("foo").setCode("testChoiceParam03");
		o3.setValue(new QuantityDt(QuantityComparatorEnum.GREATER_THAN, 123.0, "foo", "bar").setCode("bar"));
		IIdType id3 = myObservationDao.create(o3, mySrd).getId();

		{
			IBundleProvider found = myObservationDao.search(Observation.SP_VALUE_QUANTITY, new QuantityParam(">100", "foo", "bar"));
			assertEquals(1, found.size());
			assertEquals(id3, found.getResources(0, 1).get(0).getIdElement());
		}
		{
			IBundleProvider found = myObservationDao.search(Observation.SP_VALUE_QUANTITY, new QuantityParam("gt100", "foo", "bar"));
			assertEquals(1, found.size());
			assertEquals(id3, found.getResources(0, 1).get(0).getIdElement());
		}
		{
			IBundleProvider found = myObservationDao.search(Observation.SP_VALUE_QUANTITY, new QuantityParam("<100", "foo", "bar"));
			assertEquals(0, found.size());
		}
		{
			IBundleProvider found = myObservationDao.search(Observation.SP_VALUE_QUANTITY, new QuantityParam("lt100", "foo", "bar"));
			assertEquals(0, found.size());
		}
		{
			IBundleProvider found = myObservationDao.search(Observation.SP_VALUE_QUANTITY, new QuantityParam("123.0001", "foo", "bar"));
			assertEquals(0, found.size());
		}
		{
			IBundleProvider found = myObservationDao.search(Observation.SP_VALUE_QUANTITY, new QuantityParam("~120", "foo", "bar"));
			assertEquals(1, found.size());
			assertEquals(id3, found.getResources(0, 1).get(0).getIdElement());
		}
		{
			IBundleProvider found = myObservationDao.search(Observation.SP_VALUE_QUANTITY, new QuantityParam("ap120", "foo", "bar"));
			assertEquals(1, found.size());
			assertEquals(id3, found.getResources(0, 1).get(0).getIdElement());
		}
		{
			IBundleProvider found = myObservationDao.search(Observation.SP_VALUE_QUANTITY, new QuantityParam("eq123", "foo", "bar"));
			assertEquals(1, found.size());
			assertEquals(id3, found.getResources(0, 1).get(0).getIdElement());
		}
		{
			IBundleProvider found = myObservationDao.search(Observation.SP_VALUE_QUANTITY, new QuantityParam("eq120", "foo", "bar"));
			assertEquals(0, found.size());
		}
		{
			IBundleProvider found = myObservationDao.search(Observation.SP_VALUE_QUANTITY, new QuantityParam("ne120", "foo", "bar"));
			assertEquals(1, found.size());
			assertEquals(id3, found.getResources(0, 1).get(0).getIdElement());
		}
		{
			IBundleProvider found = myObservationDao.search(Observation.SP_VALUE_QUANTITY, new QuantityParam("ne123", "foo", "bar"));
			assertEquals(0, found.size());
		}
	}

	@Test
	public void testChoiceParamString() {

		Observation o4 = new Observation();
		o4.getCode().addCoding().setSystem("foo").setCode("testChoiceParam04");
		o4.setValue(new StringDt("testChoiceParam04Str"));
		IIdType id4 = myObservationDao.create(o4, mySrd).getId();

		{
			IBundleProvider found = myObservationDao.search(Observation.SP_VALUE_STRING, new StringParam("testChoiceParam04Str"));
			assertEquals(1, found.size());
			assertEquals(id4, found.getResources(0, 1).get(0).getIdElement());
		}
	}

	@Test
	public void testCreateOperationOutcome() {
		/*
		 * If any of this ever fails, it means that one of the OperationOutcome issue severity codes has changed code value across versions. We store the string as a constant, so something will need to
		 * be fixed.
		 */
		assertEquals(org.hl7.fhir.instance.model.OperationOutcome.IssueSeverity.ERROR.toCode(), BaseHapiFhirResourceDao.OO_SEVERITY_ERROR);
		assertEquals(ca.uhn.fhir.model.dstu.valueset.IssueSeverityEnum.ERROR.getCode(), BaseHapiFhirResourceDao.OO_SEVERITY_ERROR);
		assertEquals(ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum.ERROR.getCode(), BaseHapiFhirResourceDao.OO_SEVERITY_ERROR);
		assertEquals(org.hl7.fhir.instance.model.OperationOutcome.IssueSeverity.INFORMATION.toCode(), BaseHapiFhirResourceDao.OO_SEVERITY_INFO);
		assertEquals(ca.uhn.fhir.model.dstu.valueset.IssueSeverityEnum.INFORMATION.getCode(), BaseHapiFhirResourceDao.OO_SEVERITY_INFO);
		assertEquals(ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum.INFORMATION.getCode(), BaseHapiFhirResourceDao.OO_SEVERITY_INFO);
		assertEquals(org.hl7.fhir.instance.model.OperationOutcome.IssueSeverity.WARNING.toCode(), BaseHapiFhirResourceDao.OO_SEVERITY_WARN);
		assertEquals(ca.uhn.fhir.model.dstu.valueset.IssueSeverityEnum.WARNING.getCode(), BaseHapiFhirResourceDao.OO_SEVERITY_WARN);
		assertEquals(ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum.WARNING.getCode(), BaseHapiFhirResourceDao.OO_SEVERITY_WARN);
	}

	@Test
	public void testCreateOperationOutcomeError() {
		FhirResourceDaoDstu2<Bundle> dao = new FhirResourceDaoDstu2<Bundle>();
		OperationOutcome oo = (OperationOutcome) dao.createErrorOperationOutcome("my message", "incomplete");
		assertEquals(IssueSeverityEnum.ERROR.getCode(), oo.getIssue().get(0).getSeverity());
		assertEquals("my message", oo.getIssue().get(0).getDiagnostics());
		assertEquals(IssueTypeEnum.INCOMPLETE_RESULTS, oo.getIssue().get(0).getCodeElement().getValueAsEnum());
	}

	@Test
	public void testCreateOperationOutcomeInfo() {
		FhirResourceDaoDstu2<Bundle> dao = new FhirResourceDaoDstu2<Bundle>();
		OperationOutcome oo = (OperationOutcome) dao.createInfoOperationOutcome("my message");
		assertEquals(IssueSeverityEnum.INFORMATION.getCode(), oo.getIssue().get(0).getSeverity());
		assertEquals("my message", oo.getIssue().get(0).getDiagnostics());
	}

	@Test
	public void testCreateSummaryFails() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testCreateTextIdFails");
		p.addName().addFamily("Hello");

		TagList tl = new TagList();
		tl.addTag(Constants.TAG_SUBSETTED_SYSTEM, Constants.TAG_SUBSETTED_CODE);
		ResourceMetadataKeyEnum.TAG_LIST.put(p, tl);

		try {
			myPatientDao.create(p, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("subsetted"));
		}
	}

	@Test
	public void testCreateTextIdFails() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testCreateTextIdFails");
		p.addName().addFamily("Hello");
		p.setId("Patient/ABC");
		try {
			myPatientDao.create(p, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Can not create resource with ID[ABC], ID must not be supplied"));
		}
	}

	@Test
	public void testCreateWithIdFails() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testCreateNumericIdFails");
		p.addName().addFamily("Hello");
		p.setId("Patient/abc");
		try {
			myPatientDao.create(p, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Can not create resource with ID[abc], ID must not be supplied"));
		}
	}

	@Test
	public void testCreateWithIfNoneExistBasic() {
		String methodName = "testCreateWithIfNoneExistBasic";
		MethodOutcome results;

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		// Verify interceptor
		ArgumentCaptor<ActionRequestDetails> detailsCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		verify(myInterceptor).incomingRequestPreHandled(eq(RestOperationTypeEnum.CREATE), detailsCapt.capture());
		ActionRequestDetails details = detailsCapt.getValue();
		assertNotNull(details.getId());
		assertEquals("Patient", details.getResourceType());
		assertEquals(Patient.class, details.getResource().getClass());

		reset(myInterceptor);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		results = myPatientDao.create(p, "Patient?identifier=urn%3Asystem%7C" + methodName, mySrd);
		assertEquals(id.getIdPart(), results.getId().getIdPart());
		assertFalse(results.getCreated().booleanValue());

		verifyNoMoreInteractions(myInterceptor);

		// Now create a second one

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		results = myPatientDao.create(p, mySrd);
		assertNotEquals(id.getIdPart(), results.getId().getIdPart());
		assertTrue(results.getCreated().booleanValue());

		// Now try to create one with the original match URL and it should fail

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		try {
			myPatientDao.create(p, "Patient?identifier=urn%3Asystem%7C" + methodName, mySrd);
			fail();
		} catch (PreconditionFailedException e) {
			assertThat(e.getMessage(), containsString("Failed to CREATE"));
		}

	}

	@Test
	public void testCreateWithIllegalReference() {
		Observation o1 = new Observation();
		o1.getCode().addCoding().setSystem("foo").setCode("testChoiceParam01");
		IIdType id1 = myObservationDao.create(o1, mySrd).getId().toUnqualifiedVersionless();

		try {
			Patient p = new Patient();
			p.getManagingOrganization().setReference(id1);
			myPatientDao.create(p, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("Invalid reference found at path 'Patient.managingOrganization'. Resource type 'Observation' is not valid for this path", e.getMessage());
		}

		try {
			Patient p = new Patient();
			p.getManagingOrganization().setReference(new IdDt("Organization", id1.getIdPart()));
			myPatientDao.create(p, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("Resource contains reference to Organization/" + id1.getIdPart() + " but resource with ID " + id1.getIdPart() + " is actually of type Observation", e.getMessage());
		}

		// Now with a forced ID

		o1 = new Observation();
		o1.setId("testCreateWithIllegalReference");
		o1.getCode().addCoding().setSystem("foo").setCode("testChoiceParam01");
		id1 = myObservationDao.update(o1, mySrd).getId().toUnqualifiedVersionless();

		try {
			Patient p = new Patient();
			p.getManagingOrganization().setReference(id1);
			myPatientDao.create(p, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("Invalid reference found at path 'Patient.managingOrganization'. Resource type 'Observation' is not valid for this path", e.getMessage());
		}

		try {
			Patient p = new Patient();
			p.getManagingOrganization().setReference(new IdDt("Organization", id1.getIdPart()));
			myPatientDao.create(p, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Resource Organization/testCreateWithIllegalReference not found, specified in path: Patient.managingOrganization", e.getMessage());
		}

	}

	@Test
	public void testCreateWithInvalid() {
		Observation o1 = new Observation();
		o1.getCode().addCoding().setSystem("foo").setCode("testChoiceParam01");
		o1.setValue(new CodeableConceptDt("testChoiceParam01CCS", "testChoiceParam01CCV"));
		IIdType id1 = myObservationDao.create(o1, mySrd).getId();

		{
			IBundleProvider found = myObservationDao.search(Observation.SP_VALUE_CONCEPT, new TokenParam("testChoiceParam01CCS", "testChoiceParam01CCV"));
			assertEquals(1, found.size());
			assertEquals(id1, found.getResources(0, 1).get(0).getIdElement());
		}
	}

	@Test
	public void testCreateWithInvalidReferenceFailsGracefully() {
		Patient patient = new Patient();
		patient.addName().addFamily("testSearchResourceLinkWithChainWithMultipleTypes01");
		patient.setManagingOrganization(new ResourceReferenceDt("Organization/99999999"));
		try {
			myPatientDao.create(patient, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), StringContains.containsString("99999 not found"));
		}

	}

	@Test
	public void testCreateWithInvalidReferenceNoId() {
		Patient p = new Patient();
		p.addName().addFamily("Hello");
		p.getManagingOrganization().setReference("Organization/");

		try {
			myPatientDao.create(p, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Does not contain resource ID"));
		}
	}

	@Test
	public void testCreateWithReferenceBadType() {
		Patient p = new Patient();
		p.addName().addFamily("Hello");
		p.getManagingOrganization().setReference("Blah/123");

		try {
			myPatientDao.create(p, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("Invalid reference found at path 'Patient.managingOrganization'. Resource type 'Blah' is not valid for this path"));
		}
	}

	@Test
	public void testCreateWithReferenceNoType() {
		Patient p = new Patient();
		p.addName().addFamily("Hello");
		p.getManagingOrganization().setReference("123");

		try {
			myPatientDao.create(p, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Does not contain resource type"));
		}
	}

	@Test
	public void testDatePeriodParamEndOnly() {
		{
			Encounter enc = new Encounter();
			enc.addIdentifier().setSystem("testDatePeriodParam").setValue("02");
			enc.getPeriod().getEndElement().setValueAsString("2001-01-02");
			myEncounterDao.create(enc, mySrd);
		}
		SearchParameterMap params;
		List<Encounter> encs;

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam(null, "2001-01-03"));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "02"));
		encs = toList(myEncounterDao.search(params));
		assertEquals(1, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam("2001-01-01", "2001-01-03"));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "02"));
		// encs = toList(ourEncounterDao.search(params));
		// assertEquals(1, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam("2001-01-01", null));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "02"));
		encs = toList(myEncounterDao.search(params));
		assertEquals(1, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam(null, "2001-01-01"));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "02"));
		encs = toList(myEncounterDao.search(params));
		assertEquals(0, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam("2001-01-03", null));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "02"));
		encs = toList(myEncounterDao.search(params));
		assertEquals(0, encs.size());

	}

	@Test
	public void testDatePeriodParamStartAndEnd() {
		{
			Encounter enc = new Encounter();
			enc.addIdentifier().setSystem("testDatePeriodParam").setValue("03");
			enc.getPeriod().getStartElement().setValueAsString("2001-01-02");
			enc.getPeriod().getEndElement().setValueAsString("2001-01-03");
			myEncounterDao.create(enc, mySrd);
		}

		SearchParameterMap params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam("2001-01-01", "2001-01-03"));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "03"));
		List<Encounter> encs = toList(myEncounterDao.search(params));
		assertEquals(1, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam("2001-01-02", "2001-01-06"));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "03"));
		encs = toList(myEncounterDao.search(params));
		assertEquals(1, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam("2001-01-01", null));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "03"));
		encs = toList(myEncounterDao.search(params));
		assertEquals(1, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam(null, "2001-01-03"));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "03"));
		encs = toList(myEncounterDao.search(params));
		assertEquals(1, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam(null, "2001-01-05"));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "03"));
		encs = toList(myEncounterDao.search(params));
		assertEquals(1, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam(null, "2001-01-01"));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "03"));
		encs = toList(myEncounterDao.search(params));
		assertEquals(0, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam("2001-01-05", null));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "03"));
		encs = toList(myEncounterDao.search(params));
		assertEquals(0, encs.size());

	}

	@Test
	public void testDatePeriodParamStartOnly() {
		{
			Encounter enc = new Encounter();
			enc.addIdentifier().setSystem("testDatePeriodParam").setValue("01");
			enc.getPeriod().getStartElement().setValueAsString("2001-01-02");
			myEncounterDao.create(enc, mySrd);
		}

		SearchParameterMap params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam("2001-01-01", "2001-01-03"));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "01"));
		List<Encounter> encs = toList(myEncounterDao.search(params));
		assertEquals(1, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam("2001-01-01", null));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "01"));
		encs = toList(myEncounterDao.search(params));
		assertEquals(1, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam(null, "2001-01-03"));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "01"));
		encs = toList(myEncounterDao.search(params));
		assertEquals(1, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam(null, "2001-01-01"));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "01"));
		encs = toList(myEncounterDao.search(params));
		assertEquals(0, encs.size());

		params = new SearchParameterMap();
		params.add(Encounter.SP_DATE, new DateRangeParam("2001-01-03", null));
		params.add(Encounter.SP_IDENTIFIER, new IdentifierDt("testDatePeriodParam", "01"));
		encs = toList(myEncounterDao.search(params));
		assertEquals(0, encs.size());

	}

	@Test
	public void testDeleteFailsIfIncomingLinks() {
		String methodName = "testDeleteFailsIfIncomingLinks";
		Organization org = new Organization();
		org.setName(methodName);
		IIdType orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

		Patient patient = new Patient();
		patient.addName().addFamily(methodName);
		patient.getManagingOrganization().setReference(orgId);
		IIdType patId = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap map = new SearchParameterMap();
		map.add("_id", new StringParam(orgId.getIdPart()));
		map.addRevInclude(new Include("*"));
		List<IIdType> found = toUnqualifiedVersionlessIds(myOrganizationDao.search(map));
		assertThat(found, contains(orgId, patId));

		try {
			myOrganizationDao.delete(orgId, mySrd);
			fail();
		} catch (ResourceVersionConflictException e) {
			FhirResourceDaoDstu3Test.assertConflictException(e);

		}

		myPatientDao.delete(patId, mySrd);

		map = new SearchParameterMap();
		map.add("_id", new StringParam(orgId.getIdPart()));
		map.addRevInclude(new Include("*"));
		found = toUnqualifiedVersionlessIds(myOrganizationDao.search(map));
		assertThat(found, contains(orgId));

		myOrganizationDao.delete(orgId, mySrd);

		map = new SearchParameterMap();
		map.add("_id", new StringParam(orgId.getIdPart()));
		map.addRevInclude(new Include("*"));
		found = toUnqualifiedVersionlessIds(myOrganizationDao.search(map));
		assertThat(found, empty());

	}
	
	
	@Test
	public void testDeleteResource() {
		int initialHistory = myPatientDao.history(null, null, mySrd).size();

		IIdType id1;
		IIdType id2;
		IIdType id2b;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().addFamily("Tester_testDeleteResource").addGiven("Joe");
			id1 = myPatientDao.create(patient, mySrd).getId();
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().addFamily("Tester_testDeleteResource").addGiven("John");
			id2 = myPatientDao.create(patient, mySrd).getId();
		}
		{
			Patient patient = myPatientDao.read(id2, mySrd);
			patient.addIdentifier().setSystem("ZZZZZZZ").setValue("ZZZZZZZZZ");
			id2b = myPatientDao.update(patient, mySrd).getId();
		}
		ourLog.info("ID1:{}   ID2:{}   ID2b:{}", new Object[] { id1, id2, id2b });

		Map<String, IQueryParameterType> params = new HashMap<String, IQueryParameterType>();
		params.put(Patient.SP_FAMILY, new StringDt("Tester_testDeleteResource"));
		List<Patient> patients = toList(myPatientDao.search(params));
		assertEquals(2, patients.size());

		myPatientDao.delete(id1, mySrd);

		patients = toList(myPatientDao.search(params));
		assertEquals(1, patients.size());

		myPatientDao.read(id1, mySrd);
		try {
			myPatientDao.read(id1.toVersionless(), mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		IBundleProvider history = myPatientDao.history(null, null, mySrd);
		assertEquals(4 + initialHistory, history.size());
		List<IBaseResource> resources = history.getResources(0, 4);
		assertNotNull(ResourceMetadataKeyEnum.DELETED_AT.get((IResource) resources.get(0)));

		try {
			myPatientDao.delete(id2, mySrd);
			fail();
		} catch (ResourceVersionConflictException e) {
			// good
		}

		myPatientDao.delete(id2.toVersionless(), mySrd);

		patients = toList(myPatientDao.search(params));
		assertEquals(0, patients.size());

	}
	

	@Test
	public void testDeleteThenUndelete() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("001");
		patient.addName().addFamily("Tester_testDeleteThenUndelete").addGiven("Joe");
		IIdType id = myPatientDao.create(patient, mySrd).getId();
		assertThat(id.getValue(), Matchers.endsWith("/_history/1"));

		// should be ok
		myPatientDao.read(id.toUnqualifiedVersionless(), mySrd);

		// Delete it
		myPatientDao.delete(id.toUnqualifiedVersionless(), mySrd);

		try {
			myPatientDao.read(id.toUnqualifiedVersionless(), mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// expected
		}

		patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("001");
		patient.addName().addFamily("Tester_testDeleteThenUndelete").addGiven("Joe");
		patient.setId(id.toUnqualifiedVersionless());
		IIdType id2 = myPatientDao.update(patient, mySrd).getId();

		assertThat(id2.getValue(), endsWith("/_history/3"));

		IIdType gotId = myPatientDao.read(id.toUnqualifiedVersionless(), mySrd).getId();
		assertEquals(id2, gotId);
	}

	
	@Test
	public void testDeleteWithMatchUrl() {
		String methodName = "testDeleteWithMatchUrl";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		Bundle request = new Bundle();
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerbEnum.DELETE).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

		myPatientDao.deleteByUrl("Patient?identifier=urn%3Asystem%7C" + methodName, mySrd);

		try {
			myPatientDao.read(id.toVersionless(), mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// ok
		}

		try {
			myPatientDao.read(new IdDt("Patient/" + methodName), mySrd);
			fail();
		} catch (ResourceNotFoundException e) {
			// ok
		}

		IBundleProvider history = myPatientDao.history(id, null, null, mySrd);
		assertEquals(2, history.size());

		assertNotNull(ResourceMetadataKeyEnum.DELETED_AT.get((IResource) history.getResources(0, 1).get(0)));
		assertNotNull(ResourceMetadataKeyEnum.DELETED_AT.get((IResource) history.getResources(0, 1).get(0)).getValue());
		assertNull(ResourceMetadataKeyEnum.DELETED_AT.get((IResource) history.getResources(1, 2).get(0)));

	}

	@Test
	public void testDeleteWithMatchUrlChainedIdentifier() {
		String methodName = "testDeleteWithMatchUrlChainedIdentifer";

		Organization org = new Organization();
		org.setName(methodName);
		org.addIdentifier().setSystem("http://example.com").setValue(methodName);
		IIdType orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.getManagingOrganization().setReference(orgId);
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		ourLog.info("Created patient, got it: {}", id);

		myPatientDao.deleteByUrl("Patient?organization.identifier=http://example.com|" + methodName, mySrd);
		assertGone(id);
		assertNotGone(orgId);
		
		myOrganizationDao.deleteByUrl("Organization?identifier=http://example.com|" + methodName, mySrd);
		assertGone(id);
		assertGone(orgId);

	}
	@Test
	public void testDeleteWithMatchUrlChainedProfile() {
		String methodName = "testDeleteWithMatchUrlChainedProfile";

		List<IdDt> profileList = new ArrayList<IdDt>();
		profileList.add(new IdDt("http://foo"));
		
		Organization org = new Organization();
		ResourceMetadataKeyEnum.PROFILES.put(org, profileList);
		org.setName(methodName);
		
		IIdType orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.getManagingOrganization().setReference(orgId);
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		ourLog.info("Created patient, got it: {}", id);

		myPatientDao.deleteByUrl("Patient?organization._profile=http://foo", mySrd);
		assertGone(id);
		
		myOrganizationDao.deleteByUrl("Organization?_profile=http://foo", mySrd);
		try {
			myOrganizationDao.read(orgId, mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		try {
			myPatientDao.deleteByUrl("Patient?organization._profile.identifier=http://foo", mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Invalid parameter chain: organization._profile.identifier", e.getMessage());
		}

		try {
			myOrganizationDao.deleteByUrl("Organization?_profile.identifier=http://foo", mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Invalid parameter chain: _profile.identifier", e.getMessage());
		}

	}

	
	
	@Test
	public void testDeleteWithMatchUrlChainedString() {
		String methodName = "testDeleteWithMatchUrlChainedString";

		Organization org = new Organization();
		org.setName(methodName);
		IIdType orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.getManagingOrganization().setReference(orgId);
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		ourLog.info("Created patient, got it: {}", id);

		myPatientDao.deleteByUrl("Patient?organization.name=" + methodName, mySrd);

		assertGone(id);
	}

	@Test
	public void testDeleteWithMatchUrlChainedTag() {
		String methodName = "testDeleteWithMatchUrlChainedString";

		TagList tl = new TagList();
		tl.addTag("http://foo", "term");
		
		Organization org = new Organization();
		ResourceMetadataKeyEnum.TAG_LIST.put(org, tl);
		org.setName(methodName);
		
		IIdType orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.getManagingOrganization().setReference(orgId);
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		ourLog.info("Created patient, got it: {}", id);

		myPatientDao.deleteByUrl("Patient?organization._tag=http://foo|term", mySrd);
		assertGone(id);
		
		myOrganizationDao.deleteByUrl("Organization?_tag=http://foo|term", mySrd);
		try {
			myOrganizationDao.read(orgId, mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		try {
			myPatientDao.deleteByUrl("Patient?organization._tag.identifier=http://foo|term", mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Invalid parameter chain: organization._tag.identifier", e.getMessage());
		}

		try {
			myOrganizationDao.deleteByUrl("Organization?_tag.identifier=http://foo|term", mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Invalid parameter chain: _tag.identifier", e.getMessage());
		}

	}

	@Test
	public void testDeleteWithMatchUrlQualifierMissing() {
		String methodName = "testDeleteWithMatchUrlChainedProfile";

		/*
		 * Org 2 has no name
		 */

		Organization org1 = new Organization();
		org1.addIdentifier().setValue(methodName);
		IIdType org1Id = myOrganizationDao.create(org1, mySrd).getId().toUnqualifiedVersionless();

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue(methodName);
		p1.getManagingOrganization().setReference(org1Id);
		IIdType patId1 = myPatientDao.create(p1, mySrd).getId().toUnqualifiedVersionless();

		/*
		 * Org 2 has a name
		 */
		
		Organization org2 = new Organization();
		org2.setName(methodName);		
		org2.addIdentifier().setValue(methodName);
		IIdType org2Id = myOrganizationDao.create(org2, mySrd).getId().toUnqualifiedVersionless();

		Patient p2 = new Patient();
		p2.addIdentifier().setSystem("urn:system").setValue(methodName);
		p2.getManagingOrganization().setReference(org2Id);
		IIdType patId2 = myPatientDao.create(p2, mySrd).getId().toUnqualifiedVersionless();

		ourLog.info("Pat ID 1 : {}", patId1);
		ourLog.info("Org ID 1 : {}", org1Id);
		ourLog.info("Pat ID 2 : {}", patId2);
		ourLog.info("Org ID 2 : {}", org2Id);
		
		myPatientDao.deleteByUrl("Patient?organization.name:missing=true", mySrd);
		assertGone(patId1);
		assertNotGone(patId2);
		assertNotGone(org1Id);
		assertNotGone(org2Id);
		
		myOrganizationDao.deleteByUrl("Organization?name:missing=true", mySrd);
		assertGone(patId1);
		assertNotGone(patId2);
		assertGone(org1Id);
		assertNotGone(org2Id);

		myPatientDao.deleteByUrl("Patient?organization.name:missing=false", mySrd);
		assertGone(patId1);
		assertGone(patId2);
		assertGone(org1Id);
		assertNotGone(org2Id);
		
		myOrganizationDao.deleteByUrl("Organization?name:missing=false", mySrd);
		assertGone(patId1);
		assertGone(patId2);
		assertGone(org1Id);
		assertGone(org2Id);
	}

	@Test
	public void testHistoryByForcedId() {
		IIdType idv1;
		IIdType idv2;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("testHistoryByForcedId");
			patient.addName().addFamily("Tester").addGiven("testHistoryByForcedId");
			patient.setId("Patient/testHistoryByForcedId");
			idv1 = myPatientDao.update(patient, mySrd).getId();

			patient.addName().addFamily("Tester").addGiven("testHistoryByForcedIdName2");
			patient.setId(patient.getId().toUnqualifiedVersionless());
			idv2 = myPatientDao.update(patient, mySrd).getId();
		}

		List<Patient> patients = toList(myPatientDao.history(idv1.toVersionless(), null, null, mySrd));
		assertTrue(patients.size() == 2);
		// Newest first
		assertEquals("Patient/testHistoryByForcedId/_history/2", patients.get(0).getId().toUnqualified().getValue());
		assertEquals("Patient/testHistoryByForcedId/_history/1", patients.get(1).getId().toUnqualified().getValue());
		assertNotEquals(idv1, idv2);
	}

	@Test
	public void testHistoryOverMultiplePages() throws Exception {
		String methodName = "testHistoryOverMultiplePages";

		/*
		for (int i = 0; i < 1000; i++) {
			Patient patient = new Patient();
			patient.addName().addFamily(methodName + "__" + i);
			myPatientDao.create(patient).getId().toUnqualifiedVersionless();
		}
		*/
		
		Patient patient = new Patient();
		patient.addName().addFamily(methodName);
		IIdType id = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();

		Date middleDate = null;
		int halfSize = 50;
		int fullSize = 100;
		for (int i = 0; i < fullSize; i++) {
			if (i == halfSize) {
				Thread.sleep(fullSize);
				middleDate = new Date();
				Thread.sleep(fullSize);
			}
			patient.setId(id);
			patient.getName().get(0).getFamily().get(0).setValue(methodName + "_i");
			myPatientDao.update(patient, mySrd);
		}

		// By instance
		IBundleProvider history = myPatientDao.history(id, null, null, mySrd);
		assertEquals(fullSize + 1, history.size());
		for (int i = 0; i < fullSize; i++) {
			String expected = id.withVersion(Integer.toString(fullSize + 1 - i)).getValue();
			String actual = history.getResources(i, i + 1).get(0).getIdElement().getValue();
			assertEquals(expected, actual);
		}

		// By type
		history = myPatientDao.history(null, null, mySrd);
		assertEquals(fullSize + 1, history.size());
		for (int i = 0; i < fullSize; i++) {
			String expected = id.withVersion(Integer.toString(fullSize + 1 - i)).getValue();
			String actual = history.getResources(i, i + 1).get(0).getIdElement().getValue();
			assertEquals(expected, actual);
		}

		// By server
		history = mySystemDao.history(null, null, mySrd);
		assertEquals(fullSize + 1, history.size());
		for (int i = 0; i < fullSize; i++) {
			String expected = id.withVersion(Integer.toString(fullSize + 1 - i)).getValue();
			String actual = history.getResources(i, i + 1).get(0).getIdElement().getValue();
			assertEquals(expected, actual);
		}

		/*
		 * With since date
		 */

		// By instance
		history = myPatientDao.history(id, middleDate, null, mySrd);
		assertEquals(halfSize, history.size());
		for (int i = 0; i < halfSize; i++) {
			String expected = id.withVersion(Integer.toString(fullSize + 1 - i)).getValue();
			String actual = history.getResources(i, i + 1).get(0).getIdElement().getValue();
			assertEquals(expected, actual);
		}

		// By type
		history = myPatientDao.history(middleDate, null, mySrd);
		assertEquals(halfSize, history.size());
		for (int i = 0; i < halfSize; i++) {
			String expected = id.withVersion(Integer.toString(fullSize + 1 - i)).getValue();
			String actual = history.getResources(i, i + 1).get(0).getIdElement().getValue();
			assertEquals(expected, actual);
		}

		// By server
		history = mySystemDao.history(middleDate, null, mySrd);
		assertEquals(halfSize, history.size());
		for (int i = 0; i < halfSize; i++) {
			String expected = id.withVersion(Integer.toString(fullSize + 1 - i)).getValue();
			String actual = history.getResources(i, i + 1).get(0).getIdElement().getValue();
			assertEquals(expected, actual);
		}

		/*
		 * Now delete the most recent version and make sure everything still works
		 */

		myPatientDao.delete(id.toVersionless(), mySrd);

		fullSize++;
		halfSize++;

		// By instance
		history = myPatientDao.history(id, null, null, mySrd);
		for (int i = 0; i < fullSize; i++) {
			String expected = id.withVersion(Integer.toString(fullSize + 1 - i)).getValue();
			String actual = history.getResources(i, i + 1).get(0).getIdElement().getValue();
			assertEquals(expected, actual);
		}
		assertEquals(log(history), fullSize + 1, history.size());

		// By type
		history = myPatientDao.history(null, null, mySrd);
		for (int i = 0; i < fullSize; i++) {
			String expected = id.withVersion(Integer.toString(fullSize + 1 - i)).getValue();
			String actual = history.getResources(i, i + 1).get(0).getIdElement().getValue();
			assertEquals(expected, actual);
		}
		ourLog.info(log(history));
		ourLog.info("Want {} but got {}", fullSize + 1, history.size());
		assertEquals(log(history), fullSize + 1, history.size()); // fails?

		// By server
		history = mySystemDao.history(null, null, mySrd);
		for (int i = 0; i < fullSize; i++) {
			String expected = id.withVersion(Integer.toString(fullSize + 1 - i)).getValue();
			String actual = history.getResources(i, i + 1).get(0).getIdElement().getValue();
			assertEquals(expected, actual);
		}
		assertEquals(log(history), fullSize + 1, history.size());

		/*
		 * With since date
		 */

		// By instance
		history = myPatientDao.history(id, middleDate, null, mySrd);
		for (int i = 0; i < halfSize; i++) {
			String expected = id.withVersion(Integer.toString(fullSize + 1 - i)).getValue();
			String actual = history.getResources(i, i + 1).get(0).getIdElement().getValue();
			assertEquals(expected, actual);
		}
		assertEquals(halfSize, history.size());

		// By type
		history = myPatientDao.history(middleDate, null, mySrd);
		for (int i = 0; i < halfSize; i++) {
			String expected = id.withVersion(Integer.toString(fullSize + 1 - i)).getValue();
			String actual = history.getResources(i, i + 1).get(0).getIdElement().getValue();
			assertEquals(expected, actual);
		}
		assertEquals(halfSize, history.size());

		// By server
		history = mySystemDao.history(middleDate, null, mySrd);
		for (int i = 0; i < halfSize; i++) {
			String expected = id.withVersion(Integer.toString(fullSize + 1 - i)).getValue();
			String actual = history.getResources(i, i + 1).get(0).getIdElement().getValue();
			assertEquals(expected, actual);
		}
		assertEquals(halfSize, history.size());

	}

	@Test
	public void testHistoryWithDeletedResource() throws Exception {
		String methodName = "testHistoryWithDeletedResource";

		Patient patient = new Patient();
		patient.addName().addFamily(methodName);
		IIdType id = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();

		myPatientDao.delete(id, mySrd);
		patient.setId(id);
		myPatientDao.update(patient, mySrd);

		IBundleProvider history = myPatientDao.history(id, null, null, mySrd);
		assertEquals(3, history.size());
		List<IBaseResource> entries = history.getResources(0, 3);
		ourLog.info("" + ResourceMetadataKeyEnum.UPDATED.get((IResource) entries.get(0)));
		ourLog.info("" + ResourceMetadataKeyEnum.UPDATED.get((IResource) entries.get(1)));
		ourLog.info("" + ResourceMetadataKeyEnum.UPDATED.get((IResource) entries.get(2)));

		assertEquals(id.withVersion("3"), entries.get(0).getIdElement());
		assertEquals(id.withVersion("2"), entries.get(1).getIdElement());
		assertEquals(id.withVersion("1"), entries.get(2).getIdElement());

		assertNull(ResourceMetadataKeyEnum.DELETED_AT.get((IResource) entries.get(0)));
		assertEquals(BundleEntryTransactionMethodEnum.PUT, ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.get((IResource) entries.get(0)));

		assertNotNull(ResourceMetadataKeyEnum.DELETED_AT.get((IResource) entries.get(1)));
		assertEquals(BundleEntryTransactionMethodEnum.DELETE, ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.get((IResource) entries.get(1)));

		assertNull(ResourceMetadataKeyEnum.DELETED_AT.get((IResource) entries.get(2)));
		assertEquals(BundleEntryTransactionMethodEnum.POST, ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.get((IResource) entries.get(2)));
	}

	@Test
	public void testIdParam() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("001");
		patient.addName().addFamily("Tester").addGiven("Joe");

		MethodOutcome outcome = myPatientDao.create(patient, mySrd);
		assertNotNull(outcome.getId());
		assertFalse(outcome.getId().isEmpty());

		Date now = new Date();

		{
			Patient retrieved = myPatientDao.read(outcome.getId(), mySrd);
			InstantDt published = (InstantDt) retrieved.getResourceMetadata().get(ResourceMetadataKeyEnum.PUBLISHED);
			InstantDt updated = (InstantDt) retrieved.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
			assertTrue(published.before(now));
			assertTrue(updated.before(now));
		}

		/*
		 * This ID points to a patient, so we should not be able to return othe types with it
		 */
		try {
			myEncounterDao.read(outcome.getId(), mySrd);
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}
		try {
			myEncounterDao.read(new IdDt(outcome.getId().getIdPart()), mySrd);
			fail();
		} catch (ResourceNotFoundException e) {
			// expected
		}

		// Now search by _id
		{
			SearchParameterMap paramMap = new SearchParameterMap();
			paramMap.add("_id", new StringParam(outcome.getId().getIdPart()));
			List<Patient> ret = toList(myPatientDao.search(paramMap));
			assertEquals(1, ret.size());
			Patient p = ret.get(0);
			assertEquals("Tester", p.getNameFirstRep().getFamilyAsSingleString());
		}
		{
			SearchParameterMap paramMap = new SearchParameterMap();
			paramMap.add("_id", new StringParam(outcome.getId().getIdPart()));
			paramMap.add(Patient.SP_NAME, new StringParam("tester"));
			List<Patient> ret = toList(myPatientDao.search(paramMap));
			assertEquals(1, ret.size());
			Patient p = ret.get(0);
			assertEquals("Tester", p.getNameFirstRep().getFamilyAsSingleString());
		}
		{
			SearchParameterMap paramMap = new SearchParameterMap();
			paramMap.add(Patient.SP_NAME, new StringParam("tester"));
			paramMap.add("_id", new StringParam(outcome.getId().getIdPart()));
			List<Patient> ret = toList(myPatientDao.search(paramMap));
			assertEquals(1, ret.size());
			Patient p = ret.get(0);
			assertEquals("Tester", p.getNameFirstRep().getFamilyAsSingleString());
		}
		{
			SearchParameterMap paramMap = new SearchParameterMap();
			paramMap.add(Patient.SP_NAME, new StringParam("tester"));
			paramMap.add("_id", new StringParam("000"));
			List<Patient> ret = toList(myPatientDao.search(paramMap));
			assertEquals(0, ret.size());
		}
	}

	@Test
	public void testInstanceMetaOperations() {
		String methodName = "testMetaRead";
		IIdType id;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue(methodName);
			TagList tagList = new TagList();
			tagList.addTag("tag_scheme1", "tag_code1", "tag_display1");
			tagList.addTag("tag_scheme2", "tag_code2", "tag_display2");
			ResourceMetadataKeyEnum.TAG_LIST.put(patient, tagList);

			List<BaseCodingDt> securityLabels = new ArrayList<BaseCodingDt>();
			securityLabels.add(new CodingDt().setSystem("seclabel_sys1").setCode("seclabel_code1").setDisplay("seclabel_dis1"));
			securityLabels.add(new CodingDt().setSystem("seclabel_sys2").setCode("seclabel_code2").setDisplay("seclabel_dis2"));
			ResourceMetadataKeyEnum.SECURITY_LABELS.put(patient, securityLabels);

			ArrayList<IdDt> profiles = new ArrayList<IdDt>();
			profiles.add(new IdDt("http://profile/1"));
			profiles.add(new IdDt("http://profile/2"));
			ResourceMetadataKeyEnum.PROFILES.put(patient, profiles);

			id = myPatientDao.create(patient, mySrd).getId();
		}

		assertTrue(id.hasVersionIdPart());

		/*
		 * Create a second version
		 */

		Patient pt = myPatientDao.read(id, mySrd);
		pt.addName().addFamily("anotherName");
		myPatientDao.update(pt, mySrd);

		/*
		 * Meta-Delete on previous version
		 */

		MetaDt meta = new MetaDt();
		meta.addTag().setSystem("tag_scheme1").setCode("tag_code1");
		meta.addProfile("http://profile/1");
		meta.addSecurity().setSystem("seclabel_sys1").setCode("seclabel_code1");
		MetaDt newMeta = myPatientDao.metaDeleteOperation(id.withVersion("1"), meta, mySrd);
		assertEquals(1, newMeta.getProfile().size());
		assertEquals(1, newMeta.getSecurity().size());
		assertEquals(1, newMeta.getTag().size());
		assertEquals("tag_code2", newMeta.getTag().get(0).getCode());
		assertEquals("http://profile/2", newMeta.getProfile().get(0).getValue());
		assertEquals("seclabel_code2", newMeta.getSecurity().get(0).getCode());

		/*
		 * Meta Read on Version
		 */

		meta = myPatientDao.metaGetOperation(MetaDt.class, id.withVersion("1"), mySrd);
		assertEquals(1, meta.getProfile().size());
		assertEquals(1, meta.getSecurity().size());
		assertEquals(1, meta.getTag().size());
		assertEquals("tag_code2", meta.getTag().get(0).getCode());
		assertEquals("http://profile/2", meta.getProfile().get(0).getValue());
		assertEquals("seclabel_code2", meta.getSecurity().get(0).getCode());

		/*
		 * Meta-read on Version 2
		 */
		meta = myPatientDao.metaGetOperation(MetaDt.class, id.withVersion("2"), mySrd);
		assertEquals(2, meta.getProfile().size());
		assertEquals(2, meta.getSecurity().size());
		assertEquals(2, meta.getTag().size());

		/*
		 * Meta-read on latest version
		 */
		meta = myPatientDao.metaGetOperation(MetaDt.class, id.toVersionless(), mySrd);
		assertEquals(2, meta.getProfile().size());
		assertEquals(2, meta.getSecurity().size());
		assertEquals(2, meta.getTag().size());
		assertEquals("2", meta.getVersionId());

		/*
		 * Meta-Add on previous version
		 */

		meta = new MetaDt();
		meta.addTag().setSystem("tag_scheme1").setCode("tag_code1");
		meta.addProfile("http://profile/1");
		meta.addSecurity().setSystem("seclabel_sys1").setCode("seclabel_code1");
		newMeta = myPatientDao.metaAddOperation(id.withVersion("1"), meta, mySrd);
		assertEquals(2, newMeta.getProfile().size());
		assertEquals(2, newMeta.getSecurity().size());
		assertEquals(2, newMeta.getTag().size());

		/*
		 * Meta Read on Version
		 */

		meta = myPatientDao.metaGetOperation(MetaDt.class, id.withVersion("1"), mySrd);
		assertEquals(2, meta.getProfile().size());
		assertEquals(2, meta.getSecurity().size());
		assertEquals(2, meta.getTag().size());
		assertEquals("1", meta.getVersionId());

		/*
		 * Meta delete on latest
		 */

		meta = new MetaDt();
		meta.addTag().setSystem("tag_scheme1").setCode("tag_code1");
		meta.addProfile("http://profile/1");
		meta.addSecurity().setSystem("seclabel_sys1").setCode("seclabel_code1");
		newMeta = myPatientDao.metaDeleteOperation(id.toVersionless(), meta, mySrd);
		assertEquals(1, newMeta.getProfile().size());
		assertEquals(1, newMeta.getSecurity().size());
		assertEquals(1, newMeta.getTag().size());
		assertEquals("tag_code2", newMeta.getTag().get(0).getCode());
		assertEquals("http://profile/2", newMeta.getProfile().get(0).getValue());
		assertEquals("seclabel_code2", newMeta.getSecurity().get(0).getCode());

		/*
		 * Meta-Add on latest version
		 */

		meta = new MetaDt();
		meta.addTag().setSystem("tag_scheme1").setCode("tag_code1");
		meta.addProfile("http://profile/1");
		meta.addSecurity().setSystem("seclabel_sys1").setCode("seclabel_code1");
		newMeta = myPatientDao.metaAddOperation(id.toVersionless(), meta, mySrd);
		assertEquals(2, newMeta.getProfile().size());
		assertEquals(2, newMeta.getSecurity().size());
		assertEquals(2, newMeta.getTag().size());
		assertEquals("2", newMeta.getVersionId());

	}

	/**
	 * See #196
	 */
	@Test
	public void testInvalidChainNames() {
		ReferenceParam param = null;

		// OK
		param = new ReferenceParam("999999999999");
		param.setChain("organization");
		myLocationDao.search("partof", param);

		// OK
		param = new ReferenceParam("999999999999");
		param.setChain("organization.name");
		myLocationDao.search("partof", param);

		try {
			param = new ReferenceParam("999999999999");
			param.setChain("foo");
			myLocationDao.search("partof", param);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Invalid parameter chain: partof." + param.getChain()));
		}

		try {
			param = new ReferenceParam("999999999999");
			param.setChain("organization.foo");
			myLocationDao.search("partof", param);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Invalid parameter chain: " + param.getChain()));
		}

		try {
			param = new ReferenceParam("999999999999");
			param.setChain("organization.name.foo");
			myLocationDao.search("partof", param);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Invalid parameter chain: " + param.getChain()));
		}
	}

	@Test
	public void testOrganizationName() {

		//@formatter:off
		String inputStr = 
				"{" +
				"  \"resourceType\":\"Organization\",\n" + 
				"  \"extension\":[\n" + 
				"     {\n" + 
				"       \"url\":\"http://fhir.connectinggta.ca/Profile/organization#providerIdPool\",\n" + 
				"       \"valueUri\":\"urn:oid:2.16.840.1.113883.3.239.23.21.1\"\n" + 
				"     }\n" + 
				"  ],\n" + 
				"  \"text\":{\n" + 
				"     \"status\":\"empty\",\n" + 
				"     \"div\":\"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">No narrative template available for resource profile: http://fhir.connectinggta.ca/Profile/organization</div>\"\n" + 
				"  },\n" + 
				"  \"identifier\":[\n" + 
				"     {\n" + 
				"       \"use\":\"official\",\n" + 
				"       \"label\":\"HSP 2.16.840.1.113883.3.239.23.21\",\n" + 
				"       \"system\":\"urn:cgta:hsp_ids\",\n" + 
				"       \"value\":\"urn:oid:2.16.840.1.113883.3.239.23.21\"\n" + 
				"     }\n" + 
				"  ],\n" + 
				"  \"name\":\"Peterborough Regional Health Centre\"\n" + 
				"}\n";
		//@formatter:on

		Set<Long> val = myOrganizationDao.searchForIds("name", new StringParam("P"));
		int initial = val.size();

		Organization org = myFhirCtx.newJsonParser().parseResource(Organization.class, inputStr);
		myOrganizationDao.create(org, mySrd);

		val = myOrganizationDao.searchForIds("name", new StringParam("P"));
		assertEquals(initial + 1, val.size());

	}

	@Test
	public void testPersistContactPoint() {
		List<IResource> found = toList(myPatientDao.search(Patient.SP_TELECOM, new TokenParam(null, "555-123-4567")));
		int initialSize2000 = found.size();

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("testPersistContactPoint");
		patient.addTelecom().setValue("555-123-4567");
		myPatientDao.create(patient, mySrd);

		found = toList(myPatientDao.search(Patient.SP_TELECOM, new TokenParam(null, "555-123-4567")));
		assertEquals(1 + initialSize2000, found.size());

	}

	@Test
	public void testPersistResourceLink() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("testPersistResourceLink01");
		IIdType patientId01 = myPatientDao.create(patient, mySrd).getId();

		Patient patient02 = new Patient();
		patient02.addIdentifier().setSystem("urn:system").setValue("testPersistResourceLink02");
		IIdType patientId02 = myPatientDao.create(patient02, mySrd).getId();

		Observation obs01 = new Observation();
		obs01.setEffective(new DateTimeDt(new Date()));
		obs01.setSubject(new ResourceReferenceDt(patientId01));
		IIdType obsId01 = myObservationDao.create(obs01, mySrd).getId();

		Observation obs02 = new Observation();
		obs02.setEffective(new DateTimeDt(new Date()));
		obs02.setSubject(new ResourceReferenceDt(patientId02));
		IIdType obsId02 = myObservationDao.create(obs02, mySrd).getId();

		// Create another type, that shouldn't be returned
		DiagnosticReport dr01 = new DiagnosticReport();
		dr01.setSubject(new ResourceReferenceDt(patientId01));
		IIdType drId01 = myDiagnosticReportDao.create(dr01, mySrd).getId();

		ourLog.info("P1[{}] P2[{}] O1[{}] O2[{}] D1[{}]", new Object[] { patientId01, patientId02, obsId01, obsId02, drId01 });

		List<Observation> result = toList(myObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam(patientId01.getIdPart())));
		assertEquals(1, result.size());
		assertEquals(obsId01.getIdPart(), result.get(0).getId().getIdPart());

		result = toList(myObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam(patientId02.getIdPart())));
		assertEquals(1, result.size());
		assertEquals(obsId02.getIdPart(), result.get(0).getId().getIdPart());

		result = toList(myObservationDao.search(Observation.SP_SUBJECT, new ReferenceParam("999999999999")));
		assertEquals(0, result.size());

	}

	@Test
	public void testPersistSearchParamDate() {
		List<Patient> found = toList(myPatientDao.search(Patient.SP_BIRTHDATE, new DateParam(QuantityCompararatorEnum.GREATERTHAN, "2000-01-01")));
		int initialSize2000 = found.size();

		found = toList(myPatientDao.search(Patient.SP_BIRTHDATE, new DateParam(QuantityCompararatorEnum.GREATERTHAN, "2002-01-01")));
		int initialSize2002 = found.size();

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("001");
		patient.setBirthDate(new DateDt("2001-01-01"));

		myPatientDao.create(patient, mySrd);

		found = toList(myPatientDao.search(Patient.SP_BIRTHDATE, new DateParam(QuantityCompararatorEnum.GREATERTHAN, "2000-01-01")));
		assertEquals(1 + initialSize2000, found.size());

		found = toList(myPatientDao.search(Patient.SP_BIRTHDATE, new DateParam(QuantityCompararatorEnum.GREATERTHAN, "2002-01-01")));
		assertEquals(initialSize2002, found.size());

		// If this throws an exception, that would be an acceptable outcome as well..
		found = toList(myPatientDao.search(Patient.SP_BIRTHDATE + "AAAA", new DateParam(QuantityCompararatorEnum.GREATERTHAN, "2000-01-01")));
		assertEquals(0, found.size());

	}

	@Test
	public void testPersistSearchParamObservationString() {
		Observation obs = new Observation();
		obs.getCode().addCoding().setSystem("foo").setCode("testPersistSearchParamQuantity");
		obs.setValue(new StringDt("AAAABBBB"));

		myObservationDao.create(obs, mySrd);

		List<Observation> found = toList(myObservationDao.search("value-string", new StringDt("AAAABBBB")));
		assertEquals(1, found.size());

		found = toList(myObservationDao.search("value-string", new StringDt("AAAABBBBCCC")));
		assertEquals(0, found.size());

	}

	@Test
	public void testPersistSearchParamQuantity() {
		Observation obs = new Observation();
		obs.getCode().addCoding().setSystem("foo").setCode("testPersistSearchParamQuantity");
		obs.setValue(new QuantityDt(111));

		myObservationDao.create(obs, mySrd);

		List<Observation> found = toList(myObservationDao.search("value-quantity", new QuantityDt(111)));
		assertEquals(1, found.size());

		found = toList(myObservationDao.search("value-quantity", new QuantityDt(112)));
		assertEquals(0, found.size());

		found = toList(myObservationDao.search("value-quantity", new QuantityDt(212)));
		assertEquals(0, found.size());

	}

	@Test
	public void testPersistSearchParams() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("001testPersistSearchParams");
		patient.getGenderElement().setValueAsEnum(AdministrativeGenderEnum.MALE);
		patient.addName().addFamily("Tester").addGiven("JoetestPersistSearchParams");

		MethodOutcome outcome = myPatientDao.create(patient, mySrd);
		assertNotNull(outcome.getId());
		assertFalse(outcome.getId().isEmpty());

		long id = outcome.getId().getIdPartAsLong();

		IdentifierDt value = new IdentifierDt("urn:system", "001testPersistSearchParams");
		List<Patient> found = toList(myPatientDao.search(Patient.SP_IDENTIFIER, value));
		assertEquals(1, found.size());
		assertEquals(id, found.get(0).getId().getIdPartAsLong().longValue());

		// found = ourPatientDao.search(Patient.SP_GENDER, new IdentifierDt(null, "M"));
		// assertEquals(1, found.size());
		// assertEquals(id, found.get(0).getId().asLong().longValue());
		//
		// found = ourPatientDao.search(Patient.SP_GENDER, new IdentifierDt(null, "F"));
		// assertEquals(0, found.size());

		SearchParameterMap map = new SearchParameterMap();
		map.add(Patient.SP_IDENTIFIER, new IdentifierDt("urn:system", "001testPersistSearchParams"));
		map.add(Patient.SP_GENDER, new IdentifierDt("urn:some:wrong:system", AdministrativeGenderEnum.MALE.getCode()));
		found = toList(myPatientDao.search(map));
		assertEquals(0, found.size());

		// Now with no system on the gender (should match)
		map = new SearchParameterMap();
		map.add(Patient.SP_IDENTIFIER, new IdentifierDt("urn:system", "001testPersistSearchParams"));
		map.add(Patient.SP_GENDER, new IdentifierDt(null, AdministrativeGenderEnum.MALE.getCode()));
		found = toList(myPatientDao.search(map));
		assertEquals(1, found.size());
		assertEquals(id, found.get(0).getId().getIdPartAsLong().longValue());

		// Now with the wrong gender
		map = new SearchParameterMap();
		map.add(Patient.SP_IDENTIFIER, new IdentifierDt("urn:system", "001testPersistSearchParams"));
		map.add(Patient.SP_GENDER, new IdentifierDt(AdministrativeGenderEnum.MALE.getSystem(), AdministrativeGenderEnum.FEMALE.getCode()));
		found = toList(myPatientDao.search(map));
		assertEquals(0, found.size());

	}

	@Test
	public void testQuestionnaireTitleGetsIndexed() {
		Questionnaire q = new Questionnaire();
		q.getGroup().setTitle("testQuestionnaireTitleGetsIndexedQ_TITLE");
		IIdType qid1 = myQuestionnaireDao.create(q, mySrd).getId().toUnqualifiedVersionless();
		q = new Questionnaire();
		q.getGroup().setTitle("testQuestionnaireTitleGetsIndexedQ_NOTITLE");
		IIdType qid2 = myQuestionnaireDao.create(q, mySrd).getId().toUnqualifiedVersionless();

		IBundleProvider results = myQuestionnaireDao.search("title", new StringParam("testQuestionnaireTitleGetsIndexedQ_TITLE"));
		assertEquals(1, results.size());
		assertEquals(qid1, results.getResources(0, 1).get(0).getIdElement().toUnqualifiedVersionless());
		assertNotEquals(qid2, results.getResources(0, 1).get(0).getIdElement().toUnqualifiedVersionless());

	}

	@Test
	public void testRead() {
		Observation o1 = new Observation();
		o1.getCode().addCoding().setSystem("foo").setCode("testRead");
		IIdType id1 = myObservationDao.create(o1, mySrd).getId();

		/*
		 * READ
		 */

		reset(myInterceptor);
		Observation obs = myObservationDao.read(id1.toUnqualifiedVersionless(), mySrd);
		assertEquals(o1.getCode().getCoding().get(0).getCode(), obs.getCode().getCoding().get(0).getCode());

		// Verify interceptor
		ArgumentCaptor<ActionRequestDetails> detailsCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		verify(myInterceptor).incomingRequestPreHandled(eq(RestOperationTypeEnum.READ), detailsCapt.capture());
		ActionRequestDetails details = detailsCapt.getValue();
		assertEquals(id1.toUnqualifiedVersionless().getValue(), details.getId().toUnqualifiedVersionless().getValue());
		assertEquals("Observation", details.getResourceType());

		/*
		 * VREAD
		 */
		assertTrue(id1.hasVersionIdPart()); // just to make sure..
		reset(myInterceptor);
		obs = myObservationDao.read(id1, mySrd);
		assertEquals(o1.getCode().getCoding().get(0).getCode(), obs.getCode().getCoding().get(0).getCode());

		// Verify interceptor
		detailsCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		verify(myInterceptor).incomingRequestPreHandled(eq(RestOperationTypeEnum.VREAD), detailsCapt.capture());
		details = detailsCapt.getValue();
		assertEquals(id1.toUnqualified().getValue(), details.getId().toUnqualified().getValue());
		assertEquals("Observation", details.getResourceType());

	}

	@Test
	public void testReadForcedIdVersionHistory() throws InterruptedException {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testReadVorcedIdVersionHistory01");
		p1.setId("testReadVorcedIdVersionHistory");
		IIdType p1id = myPatientDao.update(p1, mySrd).getId();
		assertEquals("testReadVorcedIdVersionHistory", p1id.getIdPart());

		p1.addIdentifier().setSystem("urn:system").setValue("testReadVorcedIdVersionHistory02");
		p1.setId(p1id);
		IIdType p1idv2 = myPatientDao.update(p1, mySrd).getId();
		assertEquals("testReadVorcedIdVersionHistory", p1idv2.getIdPart());

		assertNotEquals(p1id.getValue(), p1idv2.getValue());

		Patient v1 = myPatientDao.read(p1id, mySrd);
		assertEquals(1, v1.getIdentifier().size());

		Patient v2 = myPatientDao.read(p1idv2, mySrd);
		assertEquals(2, v2.getIdentifier().size());

	}

	@Test
	public void testReadInvalidVersion() throws Exception {
		String methodName = "testReadInvalidVersion";

		Patient pat = new Patient();
		pat.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = myPatientDao.create(pat, mySrd).getId();

		assertEquals(methodName, myPatientDao.read(id, mySrd).getIdentifier().get(0).getValue());

		try {
			myPatientDao.read(id.withVersion("0"), mySrd);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals("Version \"0\" is not valid for resource Patient/" + id.getIdPart(), e.getMessage());
		}

		try {
			myPatientDao.read(id.withVersion("2"), mySrd);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals("Version \"2\" is not valid for resource Patient/" + id.getIdPart(), e.getMessage());
		}

		try {
			myPatientDao.read(id.withVersion("H"), mySrd);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals("Version \"H\" is not valid for resource Patient/" + id.getIdPart(), e.getMessage());
		}

		try {
			myPatientDao.read(new IdDt("Patient/9999999999999/_history/1"), mySrd);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals("Resource Patient/9999999999999/_history/1 is not known", e.getMessage());
		}

	}

	@Test
	public void testReadWithDeletedResource() {
		String methodName = "testReadWithDeletedResource";

		Patient patient = new Patient();
		patient.addName().addFamily(methodName);
		IIdType id = myPatientDao.create(patient, mySrd).getId().toVersionless();
		myPatientDao.delete(id, mySrd);

		assertGone(id);

		patient.setId(id);
		patient.addAddress().addLine("AAA");
		myPatientDao.update(patient, mySrd);

		Patient p;

		p = myPatientDao.read(id, mySrd);
		assertEquals(1, (p).getName().size());

		p = myPatientDao.read(id.withVersion("1"), mySrd);
		assertEquals(1, (p).getName().size());

		try {
			myPatientDao.read(id.withVersion("2"), mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		p = myPatientDao.read(id.withVersion("3"), mySrd);
		assertEquals(1, (p).getName().size());
	}

	@Test
	public void testResourceInstanceMetaOperation() {

		String methodName = "testResourceInstanceMetaOperation";
		IIdType id1, id2;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue(methodName);
			patient.addName().addFamily("Tester").addGiven("Joe");
			id1 = myPatientDao.create(patient, mySrd).getId();

			MetaDt metaAdd = new MetaDt();
			metaAdd.addTag().setSystem((String) null).setCode("Dog").setDisplay("Puppies");
			metaAdd.addSecurity().setSystem("seclabel:sys:1").setCode("seclabel:code:1").setDisplay("seclabel:dis:1");
			metaAdd.addProfile("http://profile/1");
			myPatientDao.metaAddOperation(id1, metaAdd, mySrd);
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue(methodName);
			patient.addName().addFamily("Tester").addGiven("Joe");
			TagList tagList = new TagList();
			tagList.addTag("http://foo", "Cat", "Kittens");
			ResourceMetadataKeyEnum.TAG_LIST.put(patient, tagList);

			List<BaseCodingDt> securityLabels = new ArrayList<BaseCodingDt>();
			securityLabels.add(new CodingDt().setSystem("seclabel:sys:2").setCode("seclabel:code:2").setDisplay("seclabel:dis:2"));
			ResourceMetadataKeyEnum.SECURITY_LABELS.put(patient, securityLabels);

			ArrayList<IdDt> profiles = new ArrayList<IdDt>();
			profiles.add(new IdDt("http://profile/2"));
			ResourceMetadataKeyEnum.PROFILES.put(patient, profiles);

			id2 = myPatientDao.create(patient, mySrd).getId();
		}
		{
			Device device = new Device();
			device.addIdentifier().setSystem("urn:system").setValue(methodName);
			TagList tagList = new TagList();
			tagList.addTag("http://foo", "Foo", "Bars");
			ResourceMetadataKeyEnum.TAG_LIST.put(device, tagList);

			List<BaseCodingDt> securityLabels = new ArrayList<BaseCodingDt>();
			securityLabels.add(new CodingDt().setSystem("seclabel:sys:3").setCode("seclabel:code:3").setDisplay("seclabel:dis:3"));
			ResourceMetadataKeyEnum.SECURITY_LABELS.put(device, securityLabels);

			ArrayList<IdDt> profiles = new ArrayList<IdDt>();
			profiles.add(new IdDt("http://profile/3"));
			ResourceMetadataKeyEnum.PROFILES.put(device, profiles);

			myDeviceDao.create(device, mySrd);
		}

		MetaDt meta;

		meta = myPatientDao.metaGetOperation(MetaDt.class, mySrd);
		List<CodingDt> published = meta.getTag();
		assertEquals(2, published.size());
		assertEquals(null, published.get(0).getSystem());
		assertEquals("Dog", published.get(0).getCode());
		assertEquals("Puppies", published.get(0).getDisplay());
		assertEquals("http://foo", published.get(1).getSystem());
		assertEquals("Cat", published.get(1).getCode());
		assertEquals("Kittens", published.get(1).getDisplay());
		List<CodingDt> secLabels = meta.getSecurity();
		assertEquals(2, secLabels.size());
		assertEquals("seclabel:sys:1", secLabels.get(0).getSystemElement().getValue());
		assertEquals("seclabel:code:1", secLabels.get(0).getCodeElement().getValue());
		assertEquals("seclabel:dis:1", secLabels.get(0).getDisplayElement().getValue());
		assertEquals("seclabel:sys:2", secLabels.get(1).getSystemElement().getValue());
		assertEquals("seclabel:code:2", secLabels.get(1).getCodeElement().getValue());
		assertEquals("seclabel:dis:2", secLabels.get(1).getDisplayElement().getValue());
		List<UriDt> profiles = meta.getProfile();
		assertEquals(2, profiles.size());
		assertEquals("http://profile/1", profiles.get(0).getValue());
		assertEquals("http://profile/2", profiles.get(1).getValue());

		meta = myPatientDao.metaGetOperation(MetaDt.class, id2, mySrd);
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

		{
			MetaDt metaDel = new MetaDt();
			metaDel.addTag().setSystem((String) null).setCode("Dog");
			metaDel.addSecurity().setSystem("seclabel:sys:1").setCode("seclabel:code:1");
			metaDel.addProfile("http://profile/1");
			myPatientDao.metaDeleteOperation(id1, metaDel, mySrd);
		}

		meta = myPatientDao.metaGetOperation(MetaDt.class, mySrd);
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
	public void testResourceMetaOperation() {

		String methodName = "testResourceMetaOperation";
		IIdType id1, id2;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue(methodName);
			patient.addName().addFamily("Tester").addGiven("Joe");
			TagList tagList = new TagList();
			tagList.addTag(null, "Dog", "Puppies");
			ResourceMetadataKeyEnum.TAG_LIST.put(patient, tagList);

			List<BaseCodingDt> securityLabels = new ArrayList<BaseCodingDt>();
			securityLabels.add(new CodingDt().setSystem("seclabel:sys:1").setCode("seclabel:code:1").setDisplay("seclabel:dis:1"));
			ResourceMetadataKeyEnum.SECURITY_LABELS.put(patient, securityLabels);

			ArrayList<IdDt> profiles = new ArrayList<IdDt>();
			profiles.add(new IdDt("http://profile/1"));
			ResourceMetadataKeyEnum.PROFILES.put(patient, profiles);

			id1 = myPatientDao.create(patient, mySrd).getId();
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue(methodName);
			patient.addName().addFamily("Tester").addGiven("Joe");
			TagList tagList = new TagList();
			tagList.addTag("http://foo", "Cat", "Kittens");
			ResourceMetadataKeyEnum.TAG_LIST.put(patient, tagList);

			List<BaseCodingDt> securityLabels = new ArrayList<BaseCodingDt>();
			securityLabels.add(new CodingDt().setSystem("seclabel:sys:2").setCode("seclabel:code:2").setDisplay("seclabel:dis:2"));
			ResourceMetadataKeyEnum.SECURITY_LABELS.put(patient, securityLabels);

			ArrayList<IdDt> profiles = new ArrayList<IdDt>();
			profiles.add(new IdDt("http://profile/2"));
			ResourceMetadataKeyEnum.PROFILES.put(patient, profiles);

			id2 = myPatientDao.create(patient, mySrd).getId();
		}
		{
			Device device = new Device();
			device.addIdentifier().setSystem("urn:system").setValue(methodName);
			TagList tagList = new TagList();
			tagList.addTag("http://foo", "Foo", "Bars");
			ResourceMetadataKeyEnum.TAG_LIST.put(device, tagList);

			List<BaseCodingDt> securityLabels = new ArrayList<BaseCodingDt>();
			securityLabels.add(new CodingDt().setSystem("seclabel:sys:3").setCode("seclabel:code:3").setDisplay("seclabel:dis:3"));
			ResourceMetadataKeyEnum.SECURITY_LABELS.put(device, securityLabels);

			ArrayList<IdDt> profiles = new ArrayList<IdDt>();
			profiles.add(new IdDt("http://profile/3"));
			ResourceMetadataKeyEnum.PROFILES.put(device, profiles);

			myDeviceDao.create(device, mySrd);
		}

		MetaDt meta;

		meta = myPatientDao.metaGetOperation(MetaDt.class, mySrd);
		List<CodingDt> published = meta.getTag();
		assertEquals(2, published.size());
		assertEquals(null, published.get(0).getSystem());
		assertEquals("Dog", published.get(0).getCode());
		assertEquals("Puppies", published.get(0).getDisplay());
		assertEquals("http://foo", published.get(1).getSystem());
		assertEquals("Cat", published.get(1).getCode());
		assertEquals("Kittens", published.get(1).getDisplay());
		List<CodingDt> secLabels = meta.getSecurity();
		assertEquals(2, secLabels.size());
		assertEquals("seclabel:sys:1", secLabels.get(0).getSystemElement().getValue());
		assertEquals("seclabel:code:1", secLabels.get(0).getCodeElement().getValue());
		assertEquals("seclabel:dis:1", secLabels.get(0).getDisplayElement().getValue());
		assertEquals("seclabel:sys:2", secLabels.get(1).getSystemElement().getValue());
		assertEquals("seclabel:code:2", secLabels.get(1).getCodeElement().getValue());
		assertEquals("seclabel:dis:2", secLabels.get(1).getDisplayElement().getValue());
		List<UriDt> profiles = meta.getProfile();
		assertEquals(2, profiles.size());
		assertEquals("http://profile/1", profiles.get(0).getValue());
		assertEquals("http://profile/2", profiles.get(1).getValue());

		meta = myPatientDao.metaGetOperation(MetaDt.class, id2, mySrd);
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

		myPatientDao.removeTag(id1, TagTypeEnum.TAG, null, "Dog", mySrd);
		myPatientDao.removeTag(id1, TagTypeEnum.SECURITY_LABEL, "seclabel:sys:1", "seclabel:code:1", mySrd);
		myPatientDao.removeTag(id1, TagTypeEnum.PROFILE, BaseHapiFhirDao.NS_JPA_PROFILE, "http://profile/1", mySrd);

		meta = myPatientDao.metaGetOperation(MetaDt.class, mySrd);
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
	public void testReverseIncludes() {
		String methodName = "testReverseIncludes";
		Organization org = new Organization();
		org.setName("X" + methodName + "X");
		IIdType orgId = myOrganizationDao.create(org, mySrd).getId();

		Patient pat = new Patient();
		pat.addName().addFamily("X" + methodName + "X");
		pat.getManagingOrganization().setReference(orgId.toUnqualifiedVersionless());
		myPatientDao.create(pat, mySrd);

		SearchParameterMap map = new SearchParameterMap();
		map.add(Organization.SP_NAME, new StringParam("X" + methodName + "X"));
		map.setRevIncludes(Collections.singleton(Patient.INCLUDE_ORGANIZATION));
		IBundleProvider resultsP = myOrganizationDao.search(map);
		assertEquals(1, resultsP.size());

		List<IBaseResource> results = resultsP.getResources(0, resultsP.size());
		assertEquals(2, results.size());
		assertEquals(Organization.class, results.get(0).getClass());
		assertEquals(BundleEntrySearchModeEnum.MATCH, ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.get((IResource) results.get(0)));
		assertEquals(Patient.class, results.get(1).getClass());
		assertEquals(BundleEntrySearchModeEnum.INCLUDE, ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.get((IResource) results.get(1)));
	}

	@Test()
	public void testSortByComposite() {
		Observation o = new Observation();
		o.getCode().setText("testSortByComposite");
		myObservationDao.create(o, mySrd);

		SearchParameterMap pm = new SearchParameterMap();
		pm.setSort(new SortSpec(Observation.SP_CODE_VALUE_CONCEPT));
		try {
			myObservationDao.search(pm);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("This server does not support _sort specifications of type COMPOSITE - Can't serve _sort=code-value-concept", e.getMessage());
		}
	}

	@Test
	public void testSortByDate() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testtestSortByDate");
		p.addName().addFamily("testSortF1").addGiven("testSortG1");
		p.setBirthDate(new DateDt("2001-01-01"));
		IIdType id1 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		// Create out of order
		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testtestSortByDate");
		p.addName().addFamily("testSortF2").addGiven("testSortG2");
		p.setBirthDate(new DateDt("2001-01-03"));
		IIdType id3 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testtestSortByDate");
		p.addName().addFamily("testSortF3").addGiven("testSortG3");
		p.setBirthDate(new DateDt("2001-01-02"));
		IIdType id2 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testtestSortByDate");
		IIdType id4 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		List<IIdType> actual;
		SearchParameterMap pm;

		pm = new SearchParameterMap();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", "testtestSortByDate"));
		pm.setSort(new SortSpec(Patient.SP_BIRTHDATE));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertEquals(4, actual.size());
		assertThat(actual, contains(id1, id2, id3, id4));

		pm = new SearchParameterMap();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", "testtestSortByDate"));
		pm.setSort(new SortSpec(Patient.SP_BIRTHDATE).setOrder(SortOrderEnum.ASC));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertEquals(4, actual.size());
		assertThat(actual, contains(id1, id2, id3, id4));

		pm = new SearchParameterMap();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", "testtestSortByDate"));
		pm.setSort(new SortSpec(Patient.SP_BIRTHDATE).setOrder(SortOrderEnum.DESC));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertEquals(4, actual.size());
		assertThat(actual, contains(id3, id2, id1, id4));

	}

	@Test
	public void testSortById() {
		String methodName = "testSortBTyId";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id1 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id2 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.setId(methodName);
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType idMethodName = myPatientDao.update(p, mySrd).getId().toUnqualifiedVersionless();
		assertEquals(methodName, idMethodName.getIdPart());

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id3 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id4 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap pm;
		List<IIdType> actual;

		pm = new SearchParameterMap();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", methodName));
		pm.setSort(new SortSpec(BaseResource.SP_RES_ID));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertEquals(5, actual.size());
		assertThat(actual, contains(idMethodName, id1, id2, id3, id4));

		pm = new SearchParameterMap();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", methodName));
		pm.setSort(new SortSpec(BaseResource.SP_RES_ID).setOrder(SortOrderEnum.ASC));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertEquals(5, actual.size());
		assertThat(actual, contains(idMethodName, id1, id2, id3, id4));

		pm = new SearchParameterMap();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", methodName));
		pm.setSort(new SortSpec(BaseResource.SP_RES_ID).setOrder(SortOrderEnum.DESC));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertEquals(5, actual.size());
		assertThat(actual, contains(id4, id3, id2, id1, idMethodName));
	}

	@Test
	public void testSortByLastUpdated() {
		String methodName = "testSortByLastUpdated";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system1").setValue(methodName);
		p.addName().addFamily(methodName);
		IIdType id1 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system2").setValue(methodName);
		p.addName().addFamily(methodName);
		IIdType id2 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system3").setValue(methodName);
		p.addName().addFamily(methodName);
		IIdType id3 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system4").setValue(methodName);
		p.addName().addFamily(methodName);
		IIdType id4 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap pm;
		List<IIdType> actual;

		pm = new SearchParameterMap();
		pm.setSort(new SortSpec(Constants.PARAM_LASTUPDATED));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertThat(actual, contains(id1, id2, id3, id4));

		pm = new SearchParameterMap();
		pm.setSort(new SortSpec(Constants.PARAM_LASTUPDATED, SortOrderEnum.ASC));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertThat(actual, contains(id1, id2, id3, id4));

		pm = new SearchParameterMap();
		pm.setSort(new SortSpec(Constants.PARAM_LASTUPDATED, SortOrderEnum.DESC));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertThat(actual, contains(id4, id3, id2, id1));

		pm = new SearchParameterMap();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam(null, methodName));
		pm.setSort(new SortSpec(Patient.SP_NAME).setChain(new SortSpec(Constants.PARAM_LASTUPDATED, SortOrderEnum.DESC)));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertThat(actual, contains(id4, id3, id2, id1));
	}

	@Test
	public void testSortByNumber() {
		String methodName = "testSortByNumber";

		Encounter e1 = new Encounter();
		e1.addIdentifier().setSystem("foo").setValue(methodName);
		e1.getLength().setSystem(BaseHapiFhirDao.UCUM_NS).setCode("min").setValue(4.0 * 24 * 60);
		IIdType id1 = myEncounterDao.create(e1, mySrd).getId().toUnqualifiedVersionless();

		Encounter e3 = new Encounter();
		e3.addIdentifier().setSystem("foo").setValue(methodName);
		e3.getLength().setSystem(BaseHapiFhirDao.UCUM_NS).setCode("year").setValue(3.0);
		IIdType id3 = myEncounterDao.create(e3, mySrd).getId().toUnqualifiedVersionless();

		Encounter e2 = new Encounter();
		e2.addIdentifier().setSystem("foo").setValue(methodName);
		e2.getLength().setSystem(BaseHapiFhirDao.UCUM_NS).setCode("year").setValue(2.0);
		IIdType id2 = myEncounterDao.create(e2, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap pm;
		List<IIdType> actual;

		pm = new SearchParameterMap();
		pm.setSort(new SortSpec(Encounter.SP_LENGTH));
		actual = toUnqualifiedVersionlessIds(myEncounterDao.search(pm));
		assertThat(actual, contains(id1, id2, id3));

		pm = new SearchParameterMap();
		pm.setSort(new SortSpec(Encounter.SP_LENGTH, SortOrderEnum.DESC));
		actual = toUnqualifiedVersionlessIds(myEncounterDao.search(pm));
		assertThat(actual, contains(id3, id2, id1));
	}

	public void testSortByQuantity() {
		Observation res;

		res = new Observation();
		res.setValue(new QuantityDt().setSystem("sys1").setCode("code1").setValue(2L));
		IIdType id2 = myObservationDao.create(res, mySrd).getId().toUnqualifiedVersionless();

		res = new Observation();
		res.setValue(new QuantityDt().setSystem("sys1").setCode("code1").setValue(1L));
		IIdType id1 = myObservationDao.create(res, mySrd).getId().toUnqualifiedVersionless();

		res = new Observation();
		res.setValue(new QuantityDt().setSystem("sys1").setCode("code1").setValue(3L));
		IIdType id3 = myObservationDao.create(res, mySrd).getId().toUnqualifiedVersionless();

		res = new Observation();
		res.setValue(new QuantityDt().setSystem("sys1").setCode("code1").setValue(4L));
		IIdType id4 = myObservationDao.create(res, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap pm = new SearchParameterMap();
		pm.setSort(new SortSpec(Observation.SP_VALUE_QUANTITY));
		List<IIdType> actual = toUnqualifiedVersionlessIds(myConceptMapDao.search(pm));
		assertEquals(4, actual.size());
		assertThat(actual, contains(id1, id2, id3, id4));

		pm = new SearchParameterMap();
		pm.setSort(new SortSpec(Observation.SP_VALUE_QUANTITY, SortOrderEnum.ASC));
		actual = toUnqualifiedVersionlessIds(myConceptMapDao.search(pm));
		assertEquals(4, actual.size());
		assertThat(actual, contains(id1, id2, id3, id4));

		pm = new SearchParameterMap();
		pm.setSort(new SortSpec(Observation.SP_VALUE_QUANTITY, SortOrderEnum.DESC));
		actual = toUnqualifiedVersionlessIds(myObservationDao.search(pm));
		assertEquals(4, actual.size());
		assertThat(actual, contains(id4, id3, id2, id1));

	}

	@Test
	public void testSortByReference() {
		String methodName = "testSortByReference";

		Organization o1 = new Organization();
		IIdType oid1 = myOrganizationDao.create(o1, mySrd).getId().toUnqualifiedVersionless();

		Organization o2 = new Organization();
		IIdType oid2 = myOrganizationDao.create(o2, mySrd).getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("testSortF1").addGiven("testSortG1");
		p.getManagingOrganization().setReference(oid1);
		IIdType id1 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("testSortF2").addGiven("testSortG2");
		p.getManagingOrganization().setReference(oid2);
		IIdType id2 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("testSortF3").addGiven("testSortG3");
		p.getManagingOrganization().setReference(oid1);
		IIdType id3 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.getManagingOrganization().setReference(oid2);
		IIdType id4 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap pm;
		List<IIdType> actual;

		pm = new SearchParameterMap();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", methodName));
		pm.setSort(new SortSpec(Patient.SP_ORGANIZATION));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertEquals(4, actual.size());
		assertThat(actual.subList(0, 2), containsInAnyOrder(id1, id3));
		assertThat(actual.subList(2, 4), containsInAnyOrder(id2, id4));

		pm = new SearchParameterMap();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", methodName));
		pm.setSort(new SortSpec(Patient.SP_ORGANIZATION).setOrder(SortOrderEnum.ASC));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertEquals(4, actual.size());
		assertThat(actual.subList(0, 2), containsInAnyOrder(id1, id3));
		assertThat(actual.subList(2, 4), containsInAnyOrder(id2, id4));

		pm = new SearchParameterMap();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", methodName));
		pm.setSort(new SortSpec(Patient.SP_ORGANIZATION).setOrder(SortOrderEnum.DESC));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertEquals(4, actual.size());
		assertThat(actual.subList(0, 2), containsInAnyOrder(id2, id4));
		assertThat(actual.subList(2, 4), containsInAnyOrder(id1, id3));
	}

	@Test
	public void testSortByString01() {
		Patient p = new Patient();
		String string = "testSortByString01";
		p.addIdentifier().setSystem("urn:system").setValue(string);
		p.addName().addFamily("testSortF1").addGiven("testSortG1");
		IIdType id1 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		// Create out of order
		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(string);
		p.addName().addFamily("testSortF3").addGiven("testSortG3");
		IIdType id3 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(string);
		p.addName().addFamily("testSortF2").addGiven("testSortG2");
		IIdType id2 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(string);
		IIdType id4 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap pm;
		List<IIdType> actual;

		pm = new SearchParameterMap();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", string));
		pm.setSort(new SortSpec(Patient.SP_FAMILY));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertEquals(4, actual.size());
		assertThat(actual, contains(id1, id2, id3, id4));

		pm = new SearchParameterMap();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", string));
		pm.setSort(new SortSpec(Patient.SP_FAMILY).setOrder(SortOrderEnum.ASC));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertEquals(4, actual.size());
		assertThat(actual, contains(id1, id2, id3, id4));

		pm = new SearchParameterMap();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", string));
		pm.setSort(new SortSpec(Patient.SP_FAMILY).setOrder(SortOrderEnum.DESC));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertEquals(4, actual.size());
		assertThat(actual, contains(id3, id2, id1, id4));
	}

	/**
	 * See #198
	 */
	@Test
	public void testSortByString02() {
		Patient p;
		String string = "testSortByString02";

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(string);
		p.addName().addFamily("Fam1").addGiven("Giv1");
		myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(string);
		p.addName().addFamily("Fam2").addGiven("Giv1");
		myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(string);
		p.addName().addFamily("Fam2").addGiven("Giv2");
		myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(string);
		p.addName().addFamily("Fam1").addGiven("Giv2");
		myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap pm;
		List<String> names;

		pm = new SearchParameterMap();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", string));
		pm.setSort(new SortSpec(Patient.SP_FAMILY));
		names = extractNames(myPatientDao.search(pm));
		ourLog.info("Names: {}", names);
		assertThat(names.subList(0, 2), containsInAnyOrder("Giv1 Fam1", "Giv2 Fam1"));
		assertThat(names.subList(2, 4), containsInAnyOrder("Giv1 Fam2", "Giv2 Fam2"));

		pm = new SearchParameterMap();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", string));
		pm.setSort(new SortSpec(Patient.SP_FAMILY).setChain(new SortSpec(Patient.SP_GIVEN)));
		names = extractNames(myPatientDao.search(pm));
		ourLog.info("Names: {}", names);
		assertThat(names.subList(0, 2), contains("Giv1 Fam1", "Giv2 Fam1"));
		assertThat(names.subList(2, 4), contains("Giv1 Fam2", "Giv2 Fam2"));

		pm = new SearchParameterMap();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", string));
		pm.setSort(new SortSpec(Patient.SP_FAMILY).setChain(new SortSpec(Patient.SP_GIVEN, SortOrderEnum.DESC)));
		names = extractNames(myPatientDao.search(pm));
		ourLog.info("Names: {}", names);
		assertThat(names.subList(0, 2), contains("Giv2 Fam1", "Giv1 Fam1"));
		assertThat(names.subList(2, 4), contains("Giv2 Fam2", "Giv1 Fam2"));

		pm = new SearchParameterMap();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", string));
		pm.setSort(new SortSpec(Patient.SP_FAMILY, SortOrderEnum.DESC).setChain(new SortSpec(Patient.SP_GIVEN, SortOrderEnum.DESC)));
		names = extractNames(myPatientDao.search(pm));
		ourLog.info("Names: {}", names);
		assertThat(names.subList(0, 2), contains("Giv2 Fam2", "Giv1 Fam2"));
		assertThat(names.subList(2, 4), contains("Giv2 Fam1", "Giv1 Fam1"));
	}

	@Test
	public void testSortByToken() {
		String methodName = "testSortByToken";

		Patient p;

		p = new Patient();
		p.addIdentifier().setSystem("urn:system2").setValue(methodName + "1");
		IIdType id3 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system1").setValue(methodName + "2");
		IIdType id2 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system1").setValue(methodName + "1");
		IIdType id1 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system2").setValue(methodName + "2");
		IIdType id4 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap pm;
		List<IIdType> actual;

		pm = new SearchParameterMap();
		TokenOrListParam sp = new TokenOrListParam();
		sp.addOr(new TokenParam("urn:system1", methodName + "1"));
		sp.addOr(new TokenParam("urn:system1", methodName + "2"));
		sp.addOr(new TokenParam("urn:system2", methodName + "1"));
		sp.addOr(new TokenParam("urn:system2", methodName + "2"));
		pm.add(Patient.SP_IDENTIFIER, sp);
		pm.setSort(new SortSpec(Patient.SP_IDENTIFIER));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertEquals(4, actual.size());
		assertThat(actual, contains(id1, id2, id3, id4));

		pm = new SearchParameterMap();
		sp = new TokenOrListParam();
		sp.addOr(new TokenParam("urn:system1", methodName + "1"));
		sp.addOr(new TokenParam("urn:system1", methodName + "2"));
		sp.addOr(new TokenParam("urn:system2", methodName + "1"));
		sp.addOr(new TokenParam("urn:system2", methodName + "2"));
		pm.add(Patient.SP_IDENTIFIER, sp);
		pm.setSort(new SortSpec(Patient.SP_IDENTIFIER, SortOrderEnum.DESC));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertEquals(4, actual.size());
		assertThat(actual, contains(id4, id3, id2, id1));

	}

	public void testSortByUri() {
		ConceptMap res = new ConceptMap();
		res.addElement().addTarget().addDependsOn().setElement("http://foo2");
		IIdType id2 = myConceptMapDao.create(res, mySrd).getId().toUnqualifiedVersionless();

		res = new ConceptMap();
		res.addElement().addTarget().addDependsOn().setElement("http://foo1");
		IIdType id1 = myConceptMapDao.create(res, mySrd).getId().toUnqualifiedVersionless();

		res = new ConceptMap();
		res.addElement().addTarget().addDependsOn().setElement("http://bar3");
		IIdType id3 = myConceptMapDao.create(res, mySrd).getId().toUnqualifiedVersionless();

		res = new ConceptMap();
		res.addElement().addTarget().addDependsOn().setElement("http://bar4");
		IIdType id4 = myConceptMapDao.create(res, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap pm = new SearchParameterMap();
		pm.setSort(new SortSpec(ConceptMap.SP_DEPENDSON));
		List<IIdType> actual = toUnqualifiedVersionlessIds(myConceptMapDao.search(pm));
		assertEquals(4, actual.size());
		assertThat(actual, contains(id1, id2, id3, id4));

		pm = new SearchParameterMap();
		pm.setSort(new SortSpec(Encounter.SP_LENGTH, SortOrderEnum.DESC));
		actual = toUnqualifiedVersionlessIds(myConceptMapDao.search(pm));
		assertEquals(4, actual.size());
		assertThat(actual, contains(id4, id3, id2, id1));

	}

	@Test
	public void testSortNoMatches() {
		String methodName = "testSortNoMatches";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id1 = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap map;

		map = new SearchParameterMap();
		map.add(BaseResource.SP_RES_ID, new StringParam(id1.getIdPart()));
		map.setLastUpdated(new DateRangeParam("2001", "2003"));
		map.setSort(new SortSpec(Constants.PARAM_LASTUPDATED));
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(map)), empty());

		map = new SearchParameterMap();
		map.add(BaseResource.SP_RES_ID, new StringParam(id1.getIdPart()));
		map.setLastUpdated(new DateRangeParam("2001", "2003"));
		map.setSort(new SortSpec(Patient.SP_NAME));
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(map)), empty());

	}

	@Test
	public void testStoreUnversionedResources() {
		Organization o1 = new Organization();
		o1.getNameElement().setValue("AAA");
		IIdType o1id = myOrganizationDao.create(o1, mySrd).getId();
		assertTrue(o1id.hasVersionIdPart());

		Patient p1 = new Patient();
		p1.addName().addFamily("AAAA");
		p1.getManagingOrganization().setReference(o1id);
		IIdType p1id = myPatientDao.create(p1, mySrd).getId();

		p1 = myPatientDao.read(p1id, mySrd);

		assertFalse(p1.getManagingOrganization().getReference().hasVersionIdPart());
		assertEquals(o1id.toUnqualifiedVersionless(), p1.getManagingOrganization().getReference().toUnqualifiedVersionless());
	}

	/**
	 * Test for issue #60
	 */
	@Test
	public void testStoreUtf8Characters() throws Exception {
		Organization org = new Organization();
		org.setName("測試醫院");
		org.addIdentifier().setSystem("urn:system").setValue("testStoreUtf8Characters_01");
		IIdType orgId = myOrganizationDao.create(org, mySrd).getId();

		Organization returned = myOrganizationDao.read(orgId, mySrd);
		String val = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(returned);

		ourLog.info(val);
		assertThat(val, containsString("<name value=\"測試醫院\"/>"));
	}

	@Test
	public void testStringParamWhichIsTooLong() {

		Organization org = new Organization();
		String str = "testStringParamLong__lvdaoy843s89tll8gvs89l4s3gelrukveilufyebrew8r87bv4b77feli7fsl4lv3vb7rexloxe7olb48vov4o78ls7bvo7vb48o48l4bb7vbvx";
		str = str + str;
		org.getNameElement().setValue(str);

		assertThat(str.length(), greaterThan(ResourceIndexedSearchParamString.MAX_LENGTH));

		Set<Long> val = myOrganizationDao.searchForIds("name", new StringParam("P"));
		int initial = val.size();

		myOrganizationDao.create(org, mySrd);

		val = myOrganizationDao.searchForIds("name", new StringParam("P"));
		assertEquals(initial + 0, val.size());

		val = myOrganizationDao.searchForIds("name", new StringParam(str.substring(0, ResourceIndexedSearchParamString.MAX_LENGTH)));
		assertEquals(initial + 1, val.size());

		try {
			myOrganizationDao.searchForIds("name", new StringParam(str.substring(0, ResourceIndexedSearchParamString.MAX_LENGTH + 1)));
			fail();
		} catch (InvalidRequestException e) {
			// ok
		}
	}

	@Test
	public void testTagsAndProfilesAndSecurityLabelsWithCreateAndReadAndSearch() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("testTagsWithCreateAndReadAndSearch");
		patient.addName().addFamily("Tester").addGiven("Joe");
		TagList tagList = new TagList();
		tagList.addTag(null, "Dog", "Puppies");
		// Add this twice
		tagList.addTag("http://foo", "Cat", "Kittens");
		tagList.addTag("http://foo", "Cat", "Kittens");
		ResourceMetadataKeyEnum.TAG_LIST.put(patient, tagList);

		List<BaseCodingDt> securityLabels = new ArrayList<BaseCodingDt>();
		securityLabels.add(new CodingDt().setSystem("seclabel:sys:1").setCode("seclabel:code:1").setDisplay("seclabel:dis:1"));
		securityLabels.add(new CodingDt().setSystem("seclabel:sys:2").setCode("seclabel:code:2").setDisplay("seclabel:dis:2"));
		ResourceMetadataKeyEnum.SECURITY_LABELS.put(patient, securityLabels);

		List<IdDt> profiles = new ArrayList<IdDt>();
		profiles.add(new IdDt("http://profile/1"));
		profiles.add(new IdDt("http://profile/2"));
		ResourceMetadataKeyEnum.PROFILES.put(patient, profiles);

		MethodOutcome outcome = myPatientDao.create(patient, mySrd);
		IIdType patientId = outcome.getId();
		assertNotNull(patientId);
		assertFalse(patientId.isEmpty());

		Patient retrieved = myPatientDao.read(patientId, mySrd);
		TagList published = (TagList) retrieved.getResourceMetadata().get(ResourceMetadataKeyEnum.TAG_LIST);
		sort(published);
		assertEquals(2, published.size());
		assertEquals("Dog", published.get(0).getTerm());
		assertEquals("Puppies", published.get(0).getLabel());
		assertEquals(null, published.get(0).getScheme());
		assertEquals("Cat", published.get(1).getTerm());
		assertEquals("Kittens", published.get(1).getLabel());
		assertEquals("http://foo", published.get(1).getScheme());

		List<BaseCodingDt> secLabels = ResourceMetadataKeyEnum.SECURITY_LABELS.get(retrieved);
		sortCodings(secLabels);
		assertEquals(2, secLabels.size());
		assertEquals("seclabel:sys:1", secLabels.get(0).getSystemElement().getValue());
		assertEquals("seclabel:code:1", secLabels.get(0).getCodeElement().getValue());
		assertEquals("seclabel:dis:1", secLabels.get(0).getDisplayElement().getValue());
		assertEquals("seclabel:sys:2", secLabels.get(1).getSystemElement().getValue());
		assertEquals("seclabel:code:2", secLabels.get(1).getCodeElement().getValue());
		assertEquals("seclabel:dis:2", secLabels.get(1).getDisplayElement().getValue());
		profiles = ResourceMetadataKeyEnum.PROFILES.get(retrieved);
		profiles = sortIds(profiles);
		assertEquals(2, profiles.size());
		assertEquals("http://profile/1", profiles.get(0).getValue());
		assertEquals("http://profile/2", profiles.get(1).getValue());

		List<Patient> search = toList(myPatientDao.search(Patient.SP_IDENTIFIER, patient.getIdentifierFirstRep()));
		assertEquals(1, search.size());
		retrieved = search.get(0);

		published = (TagList) retrieved.getResourceMetadata().get(ResourceMetadataKeyEnum.TAG_LIST);
		sort(published);
		assertEquals("Dog", published.get(0).getTerm());
		assertEquals("Puppies", published.get(0).getLabel());
		assertEquals(null, published.get(0).getScheme());
		assertEquals("Cat", published.get(1).getTerm());
		assertEquals("Kittens", published.get(1).getLabel());
		assertEquals("http://foo", published.get(1).getScheme());

		secLabels = ResourceMetadataKeyEnum.SECURITY_LABELS.get(retrieved);
		sortCodings(secLabels);
		assertEquals(2, secLabels.size());
		assertEquals("seclabel:sys:1", secLabels.get(0).getSystemElement().getValue());
		assertEquals("seclabel:code:1", secLabels.get(0).getCodeElement().getValue());
		assertEquals("seclabel:dis:1", secLabels.get(0).getDisplayElement().getValue());
		assertEquals("seclabel:sys:2", secLabels.get(1).getSystemElement().getValue());
		assertEquals("seclabel:code:2", secLabels.get(1).getCodeElement().getValue());
		assertEquals("seclabel:dis:2", secLabels.get(1).getDisplayElement().getValue());

		profiles = ResourceMetadataKeyEnum.PROFILES.get(retrieved);
		profiles = sortIds(profiles);
		assertEquals(2, profiles.size());
		assertEquals("http://profile/1", profiles.get(0).getValue());
		assertEquals("http://profile/2", profiles.get(1).getValue());

		myPatientDao.addTag(patientId, TagTypeEnum.TAG, "http://foo", "Cat", "Kittens");
		myPatientDao.addTag(patientId, TagTypeEnum.TAG, "http://foo", "Cow", "Calves");

		retrieved = myPatientDao.read(patientId, mySrd);
		published = (TagList) retrieved.getResourceMetadata().get(ResourceMetadataKeyEnum.TAG_LIST);
		sort(published);
		assertEquals(3, published.size());
		assertEquals(published.toString(), "Dog", published.get(0).getTerm());
		assertEquals(published.toString(), "Puppies", published.get(0).getLabel());
		assertEquals(published.toString(), null, published.get(0).getScheme());
		assertEquals(published.toString(), "Cat", published.get(1).getTerm());
		assertEquals(published.toString(), "Kittens", published.get(1).getLabel());
		assertEquals(published.toString(), "http://foo", published.get(1).getScheme());
		assertEquals(published.toString(), "Cow", published.get(2).getTerm());
		assertEquals(published.toString(), "Calves", published.get(2).getLabel());
		assertEquals(published.toString(), "http://foo", published.get(2).getScheme());

		secLabels = ResourceMetadataKeyEnum.SECURITY_LABELS.get(retrieved);
		sortCodings(secLabels);
		assertEquals(2, secLabels.size());
		assertEquals("seclabel:sys:1", secLabels.get(0).getSystemElement().getValue());
		assertEquals("seclabel:code:1", secLabels.get(0).getCodeElement().getValue());
		assertEquals("seclabel:dis:1", secLabels.get(0).getDisplayElement().getValue());
		assertEquals("seclabel:sys:2", secLabels.get(1).getSystemElement().getValue());
		assertEquals("seclabel:code:2", secLabels.get(1).getCodeElement().getValue());
		assertEquals("seclabel:dis:2", secLabels.get(1).getDisplayElement().getValue());

		profiles = ResourceMetadataKeyEnum.PROFILES.get(retrieved);
		profiles = sortIds(profiles);
		assertEquals(2, profiles.size());
		assertEquals("http://profile/1", profiles.get(0).getValue());
		assertEquals("http://profile/2", profiles.get(1).getValue());

	}

	@Test
	public void testTokenParamWhichIsTooLong() {

		String longStr1 = RandomStringUtils.randomAlphanumeric(ResourceIndexedSearchParamString.MAX_LENGTH + 100);
		String longStr2 = RandomStringUtils.randomAlphanumeric(ResourceIndexedSearchParamString.MAX_LENGTH + 100);

		Organization org = new Organization();
		org.getNameElement().setValue("testTokenParamWhichIsTooLong");
		org.getType().addCoding().setSystem(longStr1).setCode(longStr2);

		String subStr1 = longStr1.substring(0, ResourceIndexedSearchParamString.MAX_LENGTH);
		String subStr2 = longStr2.substring(0, ResourceIndexedSearchParamString.MAX_LENGTH);
		Set<Long> val = myOrganizationDao.searchForIds("type", new IdentifierDt(subStr1, subStr2));
		int initial = val.size();

		myOrganizationDao.create(org, mySrd);

		val = myOrganizationDao.searchForIds("type", new IdentifierDt(subStr1, subStr2));
		assertEquals(initial + 1, val.size());

		try {
			myOrganizationDao.searchForIds("type", new IdentifierDt(longStr1, subStr2));
			fail();
		} catch (InvalidRequestException e) {
			// ok
		}

		try {
			myOrganizationDao.searchForIds("type", new IdentifierDt(subStr1, longStr2));
			fail();
		} catch (InvalidRequestException e) {
			// ok
		}
	}

}
