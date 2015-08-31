package ca.uhn.fhir.jpa.dao;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.hamcrest.core.StringContains;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.entity.TagTypeEnum;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.MetaDt;
import ca.uhn.fhir.model.dstu2.composite.PeriodDt;
import ca.uhn.fhir.model.dstu2.composite.QuantityDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Device;
import ca.uhn.fhir.model.dstu2.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu2.resource.Encounter;
import ca.uhn.fhir.model.dstu2.resource.Location;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.resource.Practitioner;
import ca.uhn.fhir.model.dstu2.resource.Questionnaire;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointSystemEnum;
import ca.uhn.fhir.model.dstu2.valueset.HTTPVerbEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.dstu2.valueset.QuantityComparatorEnum;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.CompositeParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;

@SuppressWarnings("unchecked")
public class FhirResourceDaoDstu2Test extends BaseJpaDstu2Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu2Test.class);

	@Before
	public void before() {
		deleteAll(myPatientDao);
		deleteAll(myObservationDao);
		deleteAll(myOrganizationDao);
		deleteAll(myDiagnosticReportDao);
		deleteAll(myEncounterDao);

		FhirSystemDaoDstu2Test.doDeleteEverything(mySystemDao);
		reset(myInterceptor);
	}

	private void deleteAll(IFhirResourceDao<?> dao) {
		IBundleProvider find = dao.search(new SearchParameterMap());
		List<IBaseResource> res = find.getResources(0, find.size());
		for (IBaseResource next : res) {
			dao.delete(next.getIdElement());
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

	@Test
	public void testChoiceParamConcept() {
		Observation o1 = new Observation();
		o1.getCode().addCoding().setSystem("foo").setCode("testChoiceParam01");
		o1.setValue(new CodeableConceptDt("testChoiceParam01CCS", "testChoiceParam01CCV"));
		IIdType id1 = myObservationDao.create(o1).getId();

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
		IIdType id2 = myObservationDao.create(o2).getId();

		{
			IBundleProvider found = myObservationDao.search(Observation.SP_VALUE_DATE, new DateParam("2001-01-02"));
			assertEquals(1, found.size());
			assertEquals(id2, found.getResources(0, 1).get(0).getIdElement());
		}
	}

	@Test
	public void testChoiceParamDateAlt() {
		Observation o2 = new Observation();
		o2.getCode().addCoding().setSystem("foo").setCode("testChoiceParamDateAlt02");
		o2.setEffective(new DateTimeDt("2015-03-08T11:11:11"));
		IIdType id2 = myObservationDao.create(o2).getId();

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
		IIdType id3 = myObservationDao.create(o3).getId();

		{
			IBundleProvider found = myObservationDao.search(Observation.SP_VALUE_QUANTITY, new QuantityParam(">100", "foo", "bar"));
			assertEquals(1, found.size());
			assertEquals(id3, found.getResources(0, 1).get(0).getIdElement());
		}
		{
			IBundleProvider found = myObservationDao.search(Observation.SP_VALUE_QUANTITY, new QuantityParam("<100", "foo", "bar"));
			assertEquals(0, found.size());
		}
		{
			IBundleProvider found = myObservationDao.search(Observation.SP_VALUE_QUANTITY, new QuantityParam("123.0001", "foo", "bar"));
			assertEquals(1, found.size());
			assertEquals(id3, found.getResources(0, 1).get(0).getIdElement());
		}
		{
			IBundleProvider found = myObservationDao.search(Observation.SP_VALUE_QUANTITY, new QuantityParam("~120", "foo", "bar"));
			assertEquals(1, found.size());
			assertEquals(id3, found.getResources(0, 1).get(0).getIdElement());
		}
	}

	@Test
	public void testChoiceParamString() {

		Observation o4 = new Observation();
		o4.getCode().addCoding().setSystem("foo").setCode("testChoiceParam04");
		o4.setValue(new StringDt("testChoiceParam04Str"));
		IIdType id4 = myObservationDao.create(o4).getId();

		{
			IBundleProvider found = myObservationDao.search(Observation.SP_VALUE_STRING, new StringParam("testChoiceParam04Str"));
			assertEquals(1, found.size());
			assertEquals(id4, found.getResources(0, 1).get(0).getIdElement());
		}
	}

	@Test
	public void testCreateOperationOutcome() {
		/*
		 * If any of this ever fails, it means that one of the OperationOutcome issue severity codes has changed code
		 * value across versions. We store the string as a constant, so something will need to be fixed.
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
		OperationOutcome oo = (OperationOutcome) dao.createErrorOperationOutcome("my message");
		assertEquals(IssueSeverityEnum.ERROR.getCode(), oo.getIssue().get(0).getSeverity());
		assertEquals("my message", oo.getIssue().get(0).getDiagnostics());
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
			myPatientDao.create(p);
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
			myPatientDao.create(p);
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
			myPatientDao.create(p);
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
		IIdType id = myPatientDao.create(p).getId();
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
		results = myPatientDao.create(p, "Patient?identifier=urn%3Asystem%7C" + methodName);
		assertEquals(id.getIdPart(), results.getId().getIdPart());
		assertFalse(results.getCreated().booleanValue());

		verifyNoMoreInteractions(myInterceptor);

		// Now create a second one

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		results = myPatientDao.create(p);
		assertNotEquals(id.getIdPart(), results.getId().getIdPart());
		assertTrue(results.getCreated().booleanValue());

		// Now try to create one with the original match URL and it should fail

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		try {
			myPatientDao.create(p, "Patient?identifier=urn%3Asystem%7C" + methodName);
			fail();
		} catch (PreconditionFailedException e) {
			assertThat(e.getMessage(), containsString("Failed to CREATE"));
		}

	}

	@Test
	public void testCreateWithInvalidReferenceFailsGracefully() {
		Patient patient = new Patient();
		patient.addName().addFamily("testSearchResourceLinkWithChainWithMultipleTypes01");
		patient.setManagingOrganization(new ResourceReferenceDt("Patient/99999999"));
		try {
			myPatientDao.create(patient);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), StringContains.containsString("99999 not found"));
		}

	}

	@Test
	public void testDatePeriodParamEndOnly() {
		{
			Encounter enc = new Encounter();
			enc.addIdentifier().setSystem("testDatePeriodParam").setValue("02");
			enc.getPeriod().getEndElement().setValueAsString("2001-01-02");
			myEncounterDao.create(enc);
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
			myEncounterDao.create(enc);
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
			myEncounterDao.create(enc);
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
	public void testDeleteResource() {
		int initialHistory = myPatientDao.history(null).size();

		IIdType id1;
		IIdType id2;
		IIdType id2b;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().addFamily("Tester_testDeleteResource").addGiven("Joe");
			id1 = myPatientDao.create(patient).getId();
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().addFamily("Tester_testDeleteResource").addGiven("John");
			id2 = myPatientDao.create(patient).getId();
		}
		{
			Patient patient = myPatientDao.read(id2);
			patient.addIdentifier().setSystem("ZZZZZZZ").setValue("ZZZZZZZZZ");
			id2b = myPatientDao.update(patient).getId();
		}
		ourLog.info("ID1:{}   ID2:{}   ID2b:{}", new Object[] { id1, id2, id2b });

		Map<String, IQueryParameterType> params = new HashMap<String, IQueryParameterType>();
		params.put(Patient.SP_FAMILY, new StringDt("Tester_testDeleteResource"));
		List<Patient> patients = toList(myPatientDao.search(params));
		assertEquals(2, patients.size());

		myPatientDao.delete(id1);

		patients = toList(myPatientDao.search(params));
		assertEquals(1, patients.size());

		myPatientDao.read(id1);
		try {
			myPatientDao.read(id1.toVersionless());
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

		IBundleProvider history = myPatientDao.history(null);
		assertEquals(4 + initialHistory, history.size());
		List<IBaseResource> resources = history.getResources(0, 4);
		assertNotNull(ResourceMetadataKeyEnum.DELETED_AT.get((IResource) resources.get(0)));

		try {
			myPatientDao.delete(id2);
			fail();
		} catch (InvalidRequestException e) {
			// good
		}

		myPatientDao.delete(id2.toVersionless());

		patients = toList(myPatientDao.search(params));
		assertEquals(0, patients.size());

	}

	@Test
	public void testDeleteThenUndelete() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("001");
		patient.addName().addFamily("Tester_testDeleteThenUndelete").addGiven("Joe");
		IIdType id = myPatientDao.create(patient).getId();
		assertThat(id.getValue(), Matchers.endsWith("/_history/1"));

		// should be ok
		myPatientDao.read(id.toUnqualifiedVersionless());

		// Delete it
		myPatientDao.delete(id.toUnqualifiedVersionless());

		try {
			myPatientDao.read(id.toUnqualifiedVersionless());
			fail();
		} catch (ResourceGoneException e) {
			// expected
		}

		patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("001");
		patient.addName().addFamily("Tester_testDeleteThenUndelete").addGiven("Joe");
		patient.setId(id.toUnqualifiedVersionless());
		IIdType id2 = myPatientDao.update(patient).getId();

		assertThat(id2.getValue(), endsWith("/_history/3"));

		IIdType gotId = myPatientDao.read(id.toUnqualifiedVersionless()).getId();
		assertEquals(id2, gotId);
	}

	@Test
	public void testDeleteWithMatchUrl() {
		String methodName = "testDeleteWithMatchUrl";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = myPatientDao.create(p).getId();
		ourLog.info("Created patient, got it: {}", id);

		Bundle request = new Bundle();
		request.addEntry().setResource(p).getRequest().setMethod(HTTPVerbEnum.DELETE).setUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

		myPatientDao.deleteByUrl("Patient?identifier=urn%3Asystem%7C" + methodName);

		try {
			myPatientDao.read(id.toVersionless());
			fail();
		} catch (ResourceGoneException e) {
			// ok
		}

		try {
			myPatientDao.read(new IdDt("Patient/" + methodName));
			fail();
		} catch (ResourceNotFoundException e) {
			// ok
		}

		IBundleProvider history = myPatientDao.history(id, null);
		assertEquals(2, history.size());

		assertNotNull(ResourceMetadataKeyEnum.DELETED_AT.get((IResource) history.getResources(0, 0).get(0)));
		assertNotNull(ResourceMetadataKeyEnum.DELETED_AT.get((IResource) history.getResources(0, 0).get(0)).getValue());
		assertNull(ResourceMetadataKeyEnum.DELETED_AT.get((IResource) history.getResources(1, 1).get(0)));

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
			idv1 = myPatientDao.update(patient).getId();

			patient.addName().addFamily("Tester").addGiven("testHistoryByForcedIdName2");
			patient.setId(patient.getId().toUnqualifiedVersionless());
			idv2 = myPatientDao.update(patient).getId();
		}

		List<Patient> patients = toList(myPatientDao.history(idv1.toVersionless(), null));
		assertTrue(patients.size() == 2);
		// Newest first
		assertEquals("Patient/testHistoryByForcedId/_history/2", patients.get(0).getId().toUnqualified().getValue());
		assertEquals("Patient/testHistoryByForcedId/_history/1", patients.get(1).getId().toUnqualified().getValue());
		assertNotEquals(idv1, idv2);
	}

	@Test
	public void testIdParam() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("001");
		patient.addName().addFamily("Tester").addGiven("Joe");

		MethodOutcome outcome = myPatientDao.create(patient);
		assertNotNull(outcome.getId());
		assertFalse(outcome.getId().isEmpty());

		Date now = new Date();

		{
			Patient retrieved = myPatientDao.read(outcome.getId());
			InstantDt published = (InstantDt) retrieved.getResourceMetadata().get(ResourceMetadataKeyEnum.PUBLISHED);
			InstantDt updated = (InstantDt) retrieved.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
			assertTrue(published.before(now));
			assertTrue(updated.before(now));
		}

		/*
		 * This ID points to a patient, so we should not be able to return othe types with it
		 */
		try {
			myEncounterDao.read(outcome.getId());
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}
		try {
			myEncounterDao.read(new IdDt(outcome.getId().getIdPart()));
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
		String inputStr = "{\"resourceType\":\"Organization\",\n" + 
				"                \"extension\":[\n" + 
				"                    {\n" + 
				"                        \"url\":\"http://fhir.connectinggta.ca/Profile/organization#providerIdPool\",\n" + 
				"                        \"valueUri\":\"urn:oid:2.16.840.1.113883.3.239.23.21.1\"\n" + 
				"                    }\n" + 
				"                ],\n" + 
				"                \"text\":{\n" + 
				"                    \"status\":\"empty\",\n" + 
				"                    \"div\":\"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">No narrative template available for resource profile: http://fhir.connectinggta.ca/Profile/organization</div>\"\n" + 
				"                },\n" + 
				"                \"identifier\":[\n" + 
				"                    {\n" + 
				"                        \"use\":\"official\",\n" + 
				"                        \"label\":\"HSP 2.16.840.1.113883.3.239.23.21\",\n" + 
				"                        \"system\":\"urn:cgta:hsp_ids\",\n" + 
				"                        \"value\":\"urn:oid:2.16.840.1.113883.3.239.23.21\"\n" + 
				"                    }\n" + 
				"                ],\n" + 
				"                \"name\":\"Peterborough Regional Health Centre\"\n" + 
				"            }\n" + 
				"        }";
		//@formatter:on

		Set<Long> val = myOrganizationDao.searchForIds("name", new StringParam("P"));
		int initial = val.size();

		Organization org = myFhirCtx.newJsonParser().parseResource(Organization.class, inputStr);
		myOrganizationDao.create(org);

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
		myPatientDao.create(patient);

		found = toList(myPatientDao.search(Patient.SP_TELECOM, new TokenParam(null, "555-123-4567")));
		assertEquals(1 + initialSize2000, found.size());

	}

	@Test
	public void testPersistResourceLink() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("testPersistResourceLink01");
		IIdType patientId01 = myPatientDao.create(patient).getId();

		Patient patient02 = new Patient();
		patient02.addIdentifier().setSystem("urn:system").setValue("testPersistResourceLink02");
		IIdType patientId02 = myPatientDao.create(patient02).getId();

		Observation obs01 = new Observation();
		obs01.setEffective(new DateTimeDt(new Date()));
		obs01.setSubject(new ResourceReferenceDt(patientId01));
		IIdType obsId01 = myObservationDao.create(obs01).getId();

		Observation obs02 = new Observation();
		obs02.setEffective(new DateTimeDt(new Date()));
		obs02.setSubject(new ResourceReferenceDt(patientId02));
		IIdType obsId02 = myObservationDao.create(obs02).getId();

		// Create another type, that shouldn't be returned
		DiagnosticReport dr01 = new DiagnosticReport();
		dr01.setSubject(new ResourceReferenceDt(patientId01));
		IIdType drId01 = myDiagnosticReportDao.create(dr01).getId();

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

		myPatientDao.create(patient);

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

		myObservationDao.create(obs);

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

		myObservationDao.create(obs);

		List<Observation> found = toList(myObservationDao.search("value-quantity", new QuantityDt(111)));
		assertEquals(1, found.size());

		found = toList(myObservationDao.search("value-quantity", new QuantityDt(112)));
		assertEquals(1, found.size());

		found = toList(myObservationDao.search("value-quantity", new QuantityDt(212)));
		assertEquals(0, found.size());

	}

	@Test
	public void testPersistSearchParams() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("001testPersistSearchParams");
		patient.getGenderElement().setValueAsEnum(AdministrativeGenderEnum.MALE);
		patient.addName().addFamily("Tester").addGiven("JoetestPersistSearchParams");

		MethodOutcome outcome = myPatientDao.create(patient);
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
		IIdType qid1 = myQuestionnaireDao.create(q).getId().toUnqualifiedVersionless();
		q = new Questionnaire();
		q.getGroup().setTitle("testQuestionnaireTitleGetsIndexedQ_NOTITLE");
		IIdType qid2 = myQuestionnaireDao.create(q).getId().toUnqualifiedVersionless();

		IBundleProvider results = myQuestionnaireDao.search("title", new StringParam("testQuestionnaireTitleGetsIndexedQ_TITLE"));
		assertEquals(1, results.size());
		assertEquals(qid1, results.getResources(0, 1).get(0).getIdElement().toUnqualifiedVersionless());
		assertNotEquals(qid2, results.getResources(0, 1).get(0).getIdElement().toUnqualifiedVersionless());

	}

	@Test
	public void testRead() {
		Observation o1 = new Observation();
		o1.getCode().addCoding().setSystem("foo").setCode("testRead");
		IIdType id1 = myObservationDao.create(o1).getId();

		/*
		 * READ
		 */

		reset(myInterceptor);
		Observation obs = myObservationDao.read(id1.toUnqualifiedVersionless());
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
		obs = myObservationDao.read(id1);
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
		IIdType p1id = myPatientDao.update(p1).getId();
		assertEquals("testReadVorcedIdVersionHistory", p1id.getIdPart());

		p1.addIdentifier().setSystem("urn:system").setValue("testReadVorcedIdVersionHistory02");
		p1.setId(p1id);
		IIdType p1idv2 = myPatientDao.update(p1).getId();
		assertEquals("testReadVorcedIdVersionHistory", p1idv2.getIdPart());

		assertNotEquals(p1id.getValue(), p1idv2.getValue());

		Patient v1 = myPatientDao.read(p1id);
		assertEquals(1, v1.getIdentifier().size());

		Patient v2 = myPatientDao.read(p1idv2);
		assertEquals(2, v2.getIdentifier().size());

	}

	@Test
	public void testResourceInstanceMetaOperation() {

		String methodName = "testResourceInstanceMetaOperation";
		IIdType id1, id2;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue(methodName);
			patient.addName().addFamily("Tester").addGiven("Joe");
			id1 = myPatientDao.create(patient).getId();

			MetaDt metaAdd = new MetaDt();
			metaAdd.addTag().setSystem((String) null).setCode("Dog").setDisplay("Puppies");
			metaAdd.addSecurity().setSystem("seclabel:sys:1").setCode("seclabel:code:1").setDisplay("seclabel:dis:1");
			metaAdd.addProfile("http://profile/1");
			myPatientDao.metaAddOperation(id1, metaAdd);
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

			id2 = myPatientDao.create(patient).getId();
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

			myDeviceDao.create(device);
		}

		MetaDt meta;

		meta = myPatientDao.metaGetOperation();
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

		meta = myPatientDao.metaGetOperation(id2);
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
			myPatientDao.metaDeleteOperation(id1, metaDel);
		}

		meta = myPatientDao.metaGetOperation();
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

			id1 = myPatientDao.create(patient).getId();
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

			id2 = myPatientDao.create(patient).getId();
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

			myDeviceDao.create(device);
		}

		MetaDt meta;

		meta = myPatientDao.metaGetOperation();
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

		meta = myPatientDao.metaGetOperation(id2);
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

		myPatientDao.removeTag(id1, TagTypeEnum.TAG, null, "Dog");
		myPatientDao.removeTag(id1, TagTypeEnum.SECURITY_LABEL, "seclabel:sys:1", "seclabel:code:1");
		myPatientDao.removeTag(id1, TagTypeEnum.PROFILE, BaseHapiFhirDao.NS_JPA_PROFILE, "http://profile/1");

		meta = myPatientDao.metaGetOperation();
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
		IIdType orgId = myOrganizationDao.create(org).getId();

		Patient pat = new Patient();
		pat.addName().addFamily("X" + methodName + "X");
		pat.getManagingOrganization().setReference(orgId.toUnqualifiedVersionless());
		myPatientDao.create(pat);

		SearchParameterMap map = new SearchParameterMap();
		map.add(Organization.SP_NAME, new StringParam("X" + methodName + "X"));
		map.setRevIncludes(Collections.singleton(Patient.INCLUDE_ORGANIZATION));
		IBundleProvider resultsP = myOrganizationDao.search(map);
		assertEquals(2, resultsP.size());

		List<IBaseResource> results = resultsP.getResources(0, resultsP.size());
		assertEquals(2, results.size());
		assertEquals(Organization.class, results.get(0).getClass());
		assertEquals(BundleEntrySearchModeEnum.MATCH, ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.get((IResource) results.get(0)));
		assertEquals(Patient.class, results.get(1).getClass());
		assertEquals(BundleEntrySearchModeEnum.INCLUDE, ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.get((IResource) results.get(1)));
	}


	@Test
	public void testSortByDate() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testtestSortByDate");
		p.addName().addFamily("testSortF1").addGiven("testSortG1");
		p.setBirthDate(new DateDt("2001-01-01"));
		IIdType id1 = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		// Create out of order
		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testtestSortByDate");
		p.addName().addFamily("testSortF2").addGiven("testSortG2");
		p.setBirthDate(new DateDt("2001-01-03"));
		IIdType id3 = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testtestSortByDate");
		p.addName().addFamily("testSortF3").addGiven("testSortG3");
		p.setBirthDate(new DateDt("2001-01-02"));
		IIdType id2 = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testtestSortByDate");
		IIdType id4 = myPatientDao.create(p).getId().toUnqualifiedVersionless();

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
		IIdType id1 = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id2 = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.setId(methodName);
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType idMethodName = myPatientDao.update(p).getId().toUnqualifiedVersionless();
		assertEquals(methodName, idMethodName.getIdPart());

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id3 = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id4 = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		SearchParameterMap pm;
		List<IIdType> actual;

		pm = new SearchParameterMap();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", methodName));
		pm.setSort(new SortSpec(Patient.SP_RES_ID));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertEquals(5, actual.size());
		assertThat(actual, contains(idMethodName, id1, id2, id3, id4));

		pm = new SearchParameterMap();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", methodName));
		pm.setSort(new SortSpec(Patient.SP_RES_ID).setOrder(SortOrderEnum.ASC));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertEquals(5, actual.size());
		assertThat(actual, contains(idMethodName, id1, id2, id3, id4));

		pm = new SearchParameterMap();
		pm.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", methodName));
		pm.setSort(new SortSpec(Patient.SP_RES_ID).setOrder(SortOrderEnum.DESC));
		actual = toUnqualifiedVersionlessIds(myPatientDao.search(pm));
		assertEquals(5, actual.size());
		assertThat(actual, contains(id4, id3, id2, id1, idMethodName));
	}

	@Test
	public void testSortByReference() {
		String methodName = "testSortByReference";

		Organization o1 = new Organization();
		IIdType oid1 = myOrganizationDao.create(o1).getId().toUnqualifiedVersionless();

		Organization o2 = new Organization();
		IIdType oid2 = myOrganizationDao.create(o2).getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("testSortF1").addGiven("testSortG1");
		p.getManagingOrganization().setReference(oid1);
		IIdType id1 = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("testSortF2").addGiven("testSortG2");
		p.getManagingOrganization().setReference(oid2);
		IIdType id2 = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("testSortF3").addGiven("testSortG3");
		p.getManagingOrganization().setReference(oid1);
		IIdType id3 = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.getManagingOrganization().setReference(oid2);
		IIdType id4 = myPatientDao.create(p).getId().toUnqualifiedVersionless();

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
		IIdType id1 = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		// Create out of order
		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(string);
		p.addName().addFamily("testSortF3").addGiven("testSortG3");
		IIdType id3 = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(string);
		p.addName().addFamily("testSortF2").addGiven("testSortG2");
		IIdType id2 = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(string);
		IIdType id4 = myPatientDao.create(p).getId().toUnqualifiedVersionless();

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
		myPatientDao.create(p).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(string);
		p.addName().addFamily("Fam2").addGiven("Giv1");
		myPatientDao.create(p).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(string);
		p.addName().addFamily("Fam2").addGiven("Giv2");
		myPatientDao.create(p).getId().toUnqualifiedVersionless();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(string);
		p.addName().addFamily("Fam1").addGiven("Giv2");
		myPatientDao.create(p).getId().toUnqualifiedVersionless();

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
	public void testStoreUnversionedResources() {
		Organization o1 = new Organization();
		o1.getNameElement().setValue("AAA");
		IIdType o1id = myOrganizationDao.create(o1).getId();
		assertTrue(o1id.hasVersionIdPart());

		Patient p1 = new Patient();
		p1.addName().addFamily("AAAA");
		p1.getManagingOrganization().setReference(o1id);
		IIdType p1id = myPatientDao.create(p1).getId();

		p1 = myPatientDao.read(p1id);

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
		IIdType orgId = myOrganizationDao.create(org).getId();

		Organization returned = myOrganizationDao.read(orgId);
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

		myOrganizationDao.create(org);

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

		ArrayList<IdDt> profiles = new ArrayList<IdDt>();
		profiles.add(new IdDt("http://profile/1"));
		profiles.add(new IdDt("http://profile/2"));
		ResourceMetadataKeyEnum.PROFILES.put(patient, profiles);

		MethodOutcome outcome = myPatientDao.create(patient);
		IIdType patientId = outcome.getId();
		assertNotNull(patientId);
		assertFalse(patientId.isEmpty());

		Patient retrieved = myPatientDao.read(patientId);
		TagList published = (TagList) retrieved.getResourceMetadata().get(ResourceMetadataKeyEnum.TAG_LIST);
		assertEquals(2, published.size());
		assertEquals("Dog", published.get(0).getTerm());
		assertEquals("Puppies", published.get(0).getLabel());
		assertEquals(null, published.get(0).getScheme());
		assertEquals("Cat", published.get(1).getTerm());
		assertEquals("Kittens", published.get(1).getLabel());
		assertEquals("http://foo", published.get(1).getScheme());
		assertEquals(2, ResourceMetadataKeyEnum.SECURITY_LABELS.get(retrieved).size());
		assertEquals("seclabel:sys:1", ResourceMetadataKeyEnum.SECURITY_LABELS.get(retrieved).get(0).getSystemElement().getValue());
		assertEquals("seclabel:code:1", ResourceMetadataKeyEnum.SECURITY_LABELS.get(retrieved).get(0).getCodeElement().getValue());
		assertEquals("seclabel:dis:1", ResourceMetadataKeyEnum.SECURITY_LABELS.get(retrieved).get(0).getDisplayElement().getValue());
		assertEquals("seclabel:sys:2", ResourceMetadataKeyEnum.SECURITY_LABELS.get(retrieved).get(1).getSystemElement().getValue());
		assertEquals("seclabel:code:2", ResourceMetadataKeyEnum.SECURITY_LABELS.get(retrieved).get(1).getCodeElement().getValue());
		assertEquals("seclabel:dis:2", ResourceMetadataKeyEnum.SECURITY_LABELS.get(retrieved).get(1).getDisplayElement().getValue());
		assertEquals(2, ResourceMetadataKeyEnum.PROFILES.get(retrieved).size());
		assertEquals("http://profile/1", ResourceMetadataKeyEnum.PROFILES.get(retrieved).get(0).getValue());
		assertEquals("http://profile/2", ResourceMetadataKeyEnum.PROFILES.get(retrieved).get(1).getValue());

		List<Patient> search = toList(myPatientDao.search(Patient.SP_IDENTIFIER, patient.getIdentifierFirstRep()));
		assertEquals(1, search.size());
		retrieved = search.get(0);
		published = (TagList) retrieved.getResourceMetadata().get(ResourceMetadataKeyEnum.TAG_LIST);
		assertEquals("Dog", published.get(0).getTerm());
		assertEquals("Puppies", published.get(0).getLabel());
		assertEquals(null, published.get(0).getScheme());
		assertEquals("Cat", published.get(1).getTerm());
		assertEquals("Kittens", published.get(1).getLabel());
		assertEquals("http://foo", published.get(1).getScheme());
		assertEquals(2, ResourceMetadataKeyEnum.SECURITY_LABELS.get(retrieved).size());
		assertEquals("seclabel:sys:1", ResourceMetadataKeyEnum.SECURITY_LABELS.get(retrieved).get(0).getSystemElement().getValue());
		assertEquals("seclabel:code:1", ResourceMetadataKeyEnum.SECURITY_LABELS.get(retrieved).get(0).getCodeElement().getValue());
		assertEquals("seclabel:dis:1", ResourceMetadataKeyEnum.SECURITY_LABELS.get(retrieved).get(0).getDisplayElement().getValue());
		assertEquals("seclabel:sys:2", ResourceMetadataKeyEnum.SECURITY_LABELS.get(retrieved).get(1).getSystemElement().getValue());
		assertEquals("seclabel:code:2", ResourceMetadataKeyEnum.SECURITY_LABELS.get(retrieved).get(1).getCodeElement().getValue());
		assertEquals("seclabel:dis:2", ResourceMetadataKeyEnum.SECURITY_LABELS.get(retrieved).get(1).getDisplayElement().getValue());
		assertEquals(2, ResourceMetadataKeyEnum.PROFILES.get(retrieved).size());
		assertEquals("http://profile/1", ResourceMetadataKeyEnum.PROFILES.get(retrieved).get(0).getValue());
		assertEquals("http://profile/2", ResourceMetadataKeyEnum.PROFILES.get(retrieved).get(1).getValue());

		myPatientDao.addTag(patientId, TagTypeEnum.TAG, "http://foo", "Cat", "Kittens");
		myPatientDao.addTag(patientId, TagTypeEnum.TAG, "http://foo", "Cow", "Calves");

		retrieved = myPatientDao.read(patientId);
		published = (TagList) retrieved.getResourceMetadata().get(ResourceMetadataKeyEnum.TAG_LIST);
		assertEquals(3, published.size());
		assertEquals("Dog", published.get(0).getTerm());
		assertEquals("Puppies", published.get(0).getLabel());
		assertEquals(null, published.get(0).getScheme());
		assertEquals("Cat", published.get(1).getTerm());
		assertEquals("Kittens", published.get(1).getLabel());
		assertEquals("http://foo", published.get(1).getScheme());
		assertEquals("Cow", published.get(2).getTerm());
		assertEquals("Calves", published.get(2).getLabel());
		assertEquals("http://foo", published.get(2).getScheme());
		assertEquals(2, ResourceMetadataKeyEnum.SECURITY_LABELS.get(retrieved).size());
		assertEquals("seclabel:sys:1", ResourceMetadataKeyEnum.SECURITY_LABELS.get(retrieved).get(0).getSystemElement().getValue());
		assertEquals("seclabel:code:1", ResourceMetadataKeyEnum.SECURITY_LABELS.get(retrieved).get(0).getCodeElement().getValue());
		assertEquals("seclabel:dis:1", ResourceMetadataKeyEnum.SECURITY_LABELS.get(retrieved).get(0).getDisplayElement().getValue());
		assertEquals("seclabel:sys:2", ResourceMetadataKeyEnum.SECURITY_LABELS.get(retrieved).get(1).getSystemElement().getValue());
		assertEquals("seclabel:code:2", ResourceMetadataKeyEnum.SECURITY_LABELS.get(retrieved).get(1).getCodeElement().getValue());
		assertEquals("seclabel:dis:2", ResourceMetadataKeyEnum.SECURITY_LABELS.get(retrieved).get(1).getDisplayElement().getValue());
		assertEquals(2, ResourceMetadataKeyEnum.PROFILES.get(retrieved).size());
		assertEquals("http://profile/1", ResourceMetadataKeyEnum.PROFILES.get(retrieved).get(0).getValue());
		assertEquals("http://profile/2", ResourceMetadataKeyEnum.PROFILES.get(retrieved).get(1).getValue());

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

		myOrganizationDao.create(org);

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
