package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class FhirResourceDaoDstu3UpdateTest extends BaseJpaDstu3Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu3UpdateTest.class);

	@Test
	public void testReCreateMatchResource() {

		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl("http://foo");
		IIdType id = myCodeSystemDao.create(codeSystem).getId().toUnqualifiedVersionless();

		myCodeSystemDao.delete(id);

		codeSystem = new CodeSystem();
		codeSystem.setUrl("http://foo");
		myCodeSystemDao.update(codeSystem, "Patient?name=FAM").getId().toUnqualifiedVersionless();

	}

	@Test
	public void testCreateAndUpdateWithoutRequest() throws Exception {
		String methodName = "testUpdateByUrl";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName + "2");
		IIdType id = myPatientDao.create(p).getId().toUnqualified();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName + "2");
		p.setActive(true);
		IIdType id2 = myPatientDao.create(p, "Patient?identifier=urn:system|" + methodName + "2").getId().toUnqualified();
		assertEquals(id.getValue(), id2.getValue());

		p = new Patient();
		p.setId(id);
		p.addIdentifier().setSystem("urn:system").setValue(methodName + "2");
		p.setActive(false);
		myPatientDao.update(p).getId();

		p.setActive(true);
		id2 = myPatientDao.update(p, "Patient?identifier=urn:system|" + methodName + "2").getId().toUnqualified();
		assertEquals(id.getIdPart(), id2.getIdPart());
		assertEquals("3", id2.getVersionIdPart());

		Patient newPatient = myPatientDao.read(id);
		assertEquals("1", newPatient.getIdElement().getVersionIdPart());

		newPatient = myPatientDao.read(id.toVersionless());
		assertEquals("3", newPatient.getIdElement().getVersionIdPart());

		myPatientDao.delete(id.toVersionless());

		try {
			myPatientDao.read(id.toVersionless());
			fail();
		} catch (ResourceGoneException e) {
			// nothing
		}

	}

	@Test
	public void testDuplicateProfilesIgnored() {
		String name = "testDuplicateProfilesIgnored";
		IIdType id;
		{
			Patient patient = new Patient();
			patient.addName().setFamily(name);

			List<IdType> tl = new ArrayList<IdType>();
			tl.add(new IdType("http://foo/bar"));
			tl.add(new IdType("http://foo/bar"));
			tl.add(new IdType("http://foo/bar"));
			patient.getMeta().getProfile().addAll(tl);

			id = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		// Do a read
		{
			Patient patient = myPatientDao.read(id, mySrd);
			List<UriType> tl = patient.getMeta().getProfile();
			assertEquals(1, tl.size());
			assertEquals("http://foo/bar", tl.get(0).getValue());
		}

	}

	@After
	public void afterResetDao() {
		myDaoConfig.setResourceMetaCountHardLimit(new DaoConfig().getResourceMetaCountHardLimit());
	}
	
	@Test
	public void testHardMetaCapIsEnforcedOnCreate() {
		myDaoConfig.setResourceMetaCountHardLimit(3);

		IIdType id;
		{
			Patient patient = new Patient();
			patient.getMeta().addTag().setSystem("http://foo").setCode("1");
			patient.getMeta().addTag().setSystem("http://foo").setCode("2");
			patient.getMeta().addTag().setSystem("http://foo").setCode("3");
			patient.getMeta().addTag().setSystem("http://foo").setCode("4");
			patient.setActive(true);
			try {
				id = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
				fail();
			} catch (UnprocessableEntityException e) {
				assertEquals("Resource contains 4 meta entries (tag/profile/security label), maximum is 3", e.getMessage());
			}
		}
	}
	
	@Test
	public void testHardMetaCapIsEnforcedOnMetaAdd() {
		myDaoConfig.setResourceMetaCountHardLimit(3);

		IIdType id;
		{
			Patient patient = new Patient();
			patient.setActive(true);
			id = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		
		{
			Meta meta = new Meta();
			meta.addTag().setSystem("http://foo").setCode("1");
			meta.addTag().setSystem("http://foo").setCode("2");
			meta.addTag().setSystem("http://foo").setCode("3");
			meta.addTag().setSystem("http://foo").setCode("4");
			try {
				myPatientDao.metaAddOperation(id, meta, null);
				fail();
			} catch (UnprocessableEntityException e) {
				assertEquals("Resource contains 4 meta entries (tag/profile/security label), maximum is 3", e.getMessage());
			}

		}
	}
	
	@Test
	public void testDuplicateTagsOnAddTagsIgnored() {
		IIdType id;
		{
			Patient patient = new Patient();
			patient.setActive(true);
			id = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		Meta meta = new Meta();
		meta.addTag().setSystem("http://foo").setCode("bar").setDisplay("Val1");
		meta.addTag().setSystem("http://foo").setCode("bar").setDisplay("Val2");
		meta.addTag().setSystem("http://foo").setCode("bar").setDisplay("Val3");
		myPatientDao.metaAddOperation(id, meta, null);
		
		// Do a read
		{
			Patient patient = myPatientDao.read(id, mySrd);
			List<Coding> tl = patient.getMeta().getTag();
			assertEquals(1, tl.size());
			assertEquals("http://foo", tl.get(0).getSystem());
			assertEquals("bar", tl.get(0).getCode());
		}

	}

	@Test
	public void testDuplicateTagsOnUpdateIgnored() {
		IIdType id;
		{
			Patient patient = new Patient();
			patient.setActive(true);
			id = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		{
			Patient patient = new Patient();
			patient.setId(id.getValue());
			patient.setActive(true);
			patient.getMeta().addTag().setSystem("http://foo").setCode("bar").setDisplay("Val1");
			patient.getMeta().addTag().setSystem("http://foo").setCode("bar").setDisplay("Val2");
			patient.getMeta().addTag().setSystem("http://foo").setCode("bar").setDisplay("Val3");
			myPatientDao.update(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		
		// Do a read on second version
		{
			Patient patient = myPatientDao.read(id, mySrd);
			List<Coding> tl = patient.getMeta().getTag();
			assertEquals(1, tl.size());
			assertEquals("http://foo", tl.get(0).getSystem());
			assertEquals("bar", tl.get(0).getCode());
		}

		// Do a read on first version
		{
			Patient patient = myPatientDao.read(id.withVersion("1"), mySrd);
			List<Coding> tl = patient.getMeta().getTag();
			assertEquals(0, tl.size());
		}

		Meta meta = new Meta();
		meta.addTag().setSystem("http://foo").setCode("bar").setDisplay("Val1");
		meta.addTag().setSystem("http://foo").setCode("bar").setDisplay("Val2");
		meta.addTag().setSystem("http://foo").setCode("bar").setDisplay("Val3");
		myPatientDao.metaAddOperation(id.withVersion("1"), meta, null);

		// Do a read on first version
		{
			Patient patient = myPatientDao.read(id.withVersion("1"), mySrd);
			List<Coding> tl = patient.getMeta().getTag();
			assertEquals(1, tl.size());
			assertEquals("http://foo", tl.get(0).getSystem());
			assertEquals("bar", tl.get(0).getCode());
		}

	}

	@Test
	public void testMultipleUpdatesWithNoChangesDoesNotResultInAnUpdateForDiscreteUpdates() {

		// First time
		Patient p = new Patient();
		p.setActive(true);
		p.setId("Patient/A");
		String id = myPatientDao.update(p).getId().getValue();
		assertThat(id, endsWith("Patient/A/_history/1"));
		assertEquals("1", myPatientDao.read(new IdType("Patient/A")).getIdElement().getVersionIdPart());

		// Second time should not result in an update
		p = new Patient();
		p.setActive(true);
		p.setId("Patient/A");
		id = myPatientDao.update(p).getId().getValue();
		assertThat(id, endsWith("Patient/A/_history/1"));
		assertEquals("1", myPatientDao.read(new IdType("Patient/A")).getIdElement().getVersionIdPart());

		// And third time should not result in an update
		p = new Patient();
		p.setActive(true);
		p.setId("Patient/A");
		id = myPatientDao.update(p).getId().getValue();
		assertThat(id, endsWith("Patient/A/_history/1"));
		assertEquals("1", myPatientDao.read(new IdType("Patient/A")).getIdElement().getVersionIdPart());

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

		// Create one more
		p = new Patient();
		p.setActive(false);
		p.setId("Patient/A");
		id = myPatientDao.update(p).getId().getValue();
		assertThat(id, endsWith("Patient/A/_history/2"));

	}



	@Test
	public void testUpdateByUrl() {
		String methodName = "testUpdateByUrl";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("Hello");
		p.setId("Patient/" + methodName);

		myPatientDao.update(p, "Patient?identifier=urn%3Asystem%7C" + methodName, mySrd);

		p = myPatientDao.read(id.toVersionless(), mySrd);
		assertThat(p.getIdElement().toVersionless().toString(), not(containsString("test")));
		assertEquals(id.toVersionless(), p.getIdElement().toVersionless());
		assertNotEquals(id, p.getIdElement());
		assertThat(p.getIdElement().toString(), endsWith("/_history/2"));

	}

	@Test
	public void testUpdateConditionalByLastUpdated() throws Exception {
		String methodName = "testUpdateByUrl";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName + "2");
		myPatientDao.create(p, mySrd).getId();

		InstantDt start = InstantDt.withCurrentTime();
		ourLog.info("First time: {}", start.getValueAsString());
		Thread.sleep(100);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got ID: {}", id);

		Thread.sleep(100);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("Hello");
		p.setId("Patient/" + methodName);

		String matchUrl = "Patient?_lastUpdated=gt" + start.getValueAsString();
		ourLog.info("URL is: {}", matchUrl);
		myPatientDao.update(p, matchUrl, mySrd);

		p = myPatientDao.read(id.toVersionless(), mySrd);
		assertThat(p.getIdElement().toVersionless().toString(), not(containsString("test")));
		assertEquals(id.toVersionless(), p.getIdElement().toVersionless());
		assertNotEquals(id, p.getIdElement());
		assertThat(p.getIdElement().toString(), endsWith("/_history/2"));

	}

	@Test
	public void testUpdateConditionalByLastUpdatedWithWrongTimezone() throws Exception {
		TimeZone def = TimeZone.getDefault();
		try {
			TimeZone.setDefault(TimeZone.getTimeZone("GMT-0:00"));
			String methodName = "testUpdateByUrl";

			Patient p = new Patient();
			p.addIdentifier().setSystem("urn:system").setValue(methodName + "2");
			myPatientDao.create(p, mySrd).getId();

			InstantDt start = InstantDt.withCurrentTime();
			Thread.sleep(100);

			p = new Patient();
			p.addIdentifier().setSystem("urn:system").setValue(methodName);
			IIdType id = myPatientDao.create(p, mySrd).getId();
			ourLog.info("Created patient, got it: {}", id);

			Thread.sleep(100);

			p = new Patient();
			p.addIdentifier().setSystem("urn:system").setValue(methodName);
			p.addName().setFamily("Hello");
			p.setId("Patient/" + methodName);

			myPatientDao.update(p, "Patient?_lastUpdated=gt" + start.getValueAsString(), mySrd);

			p = myPatientDao.read(id.toVersionless(), mySrd);
			assertThat(p.getIdElement().toVersionless().toString(), not(containsString("test")));
			assertEquals(id.toVersionless(), p.getIdElement().toVersionless());
			assertNotEquals(id, p.getIdElement());
			assertThat(p.getIdElement().toString(), endsWith("/_history/2"));
		} finally {
			TimeZone.setDefault(def);
		}
	}

	@Test
	public void testUpdateCreatesTextualIdIfItDoesntAlreadyExist() {
		Patient p = new Patient();
		String methodName = "testUpdateCreatesTextualIdIfItDoesntAlreadyExist";
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("Hello");
		p.setId("Patient/" + methodName);

		IIdType id = myPatientDao.update(p, mySrd).getId();
		assertEquals("Patient/" + methodName, id.toUnqualifiedVersionless().getValue());

		p = myPatientDao.read(id, mySrd);
		assertEquals(methodName, p.getIdentifier().get(0).getValue());
	}

	@Test
	public void testUpdateDoesntFailForUnknownIdWithNumberThenText() {
		String methodName = "testUpdateFailsForUnknownIdWithNumberThenText";
		Patient p = new Patient();
		p.setId("0" + methodName);
		p.addName().setFamily(methodName);

		myPatientDao.update(p, mySrd);
	}

	@Test
	@Ignore
	public void testUpdateIgnoresIdenticalVersions() {
		String methodName = "testUpdateIgnoresIdenticalVersions";

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue(methodName);
		p1.addName().setFamily("Tester").addGiven(methodName);
		IIdType p1id = myPatientDao.create(p1, mySrd).getId();

		IIdType p1id2 = myPatientDao.update(p1, mySrd).getId();
		assertEquals(p1id.getValue(), p1id2.getValue());

		p1.addName().addGiven("NewGiven");
		IIdType p1id3 = myPatientDao.update(p1, mySrd).getId();
		assertNotEquals(p1id.getValue(), p1id3.getValue());

	}

	@Test
	public void testUpdateMaintainsSearchParams() {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testUpdateMaintainsSearchParamsDstu2AAA");
		p1.addName().setFamily("Tester").addGiven("testUpdateMaintainsSearchParamsDstu2AAA");
		IIdType p1id = myPatientDao.create(p1, mySrd).getId();

		Patient p2 = new Patient();
		p2.addIdentifier().setSystem("urn:system").setValue("testUpdateMaintainsSearchParamsDstu2BBB");
		p2.addName().setFamily("Tester").addGiven("testUpdateMaintainsSearchParamsDstu2BBB");
		myPatientDao.create(p2, mySrd).getId();

		Set<Long> ids = myPatientDao.searchForIds(new SearchParameterMap(Patient.SP_GIVEN, new StringParam("testUpdateMaintainsSearchParamsDstu2AAA")), null);
		assertEquals(1, ids.size());
		assertThat(ids, contains(p1id.getIdPartAsLong()));

		// Update the name
		p1.getName().get(0).getGiven().get(0).setValue("testUpdateMaintainsSearchParamsDstu2BBB");
		MethodOutcome update2 = myPatientDao.update(p1, mySrd);
		IIdType p1id2 = update2.getId();

		ids = myPatientDao.searchForIds(new SearchParameterMap(Patient.SP_GIVEN, new StringParam("testUpdateMaintainsSearchParamsDstu2AAA")), null);
		assertEquals(0, ids.size());

		ids = myPatientDao.searchForIds(new SearchParameterMap(Patient.SP_GIVEN, new StringParam("testUpdateMaintainsSearchParamsDstu2BBB")), null);
		assertEquals(2, ids.size());

		// Make sure vreads work
		p1 = myPatientDao.read(p1id, mySrd);
		assertEquals("testUpdateMaintainsSearchParamsDstu2AAA", p1.getName().get(0).getGivenAsSingleString());

		p1 = myPatientDao.read(p1id2, mySrd);
		assertEquals("testUpdateMaintainsSearchParamsDstu2BBB", p1.getName().get(0).getGivenAsSingleString());

	}

	/**
	 * Per the spec, update should preserve tags and security labels but not profiles
	 */
	@Test
	public void testUpdateMaintainsTagsAndSecurityLabels() {
		String methodName = "testUpdateMaintainsTagsAndSecurityLabels";

		IIdType p1id;
		{
			Patient p1 = new Patient();
			p1.addName().setFamily(methodName);

			p1.getMeta().addTag("tag_scheme1", "tag_term1", null);
			p1.getMeta().addSecurity("sec_scheme1", "sec_term1", null);
			p1.getMeta().addProfile("http://foo1");

			p1id = myPatientDao.create(p1, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Patient p1 = new Patient();
			p1.setId(p1id.getValue());
			p1.addName().setFamily(methodName);

			p1.getMeta().addTag("tag_scheme2", "tag_term2", null);
			p1.getMeta().addSecurity("sec_scheme2", "sec_term2", null);
			p1.getMeta().addProfile("http://foo2");

			myPatientDao.update(p1, mySrd);
		}
		{
			Patient p1 = myPatientDao.read(p1id, mySrd);
			List<Coding> tagList = p1.getMeta().getTag();
			Set<String> secListValues = new HashSet<String>();
			for (Coding next : tagList) {
				secListValues.add(next.getSystemElement().getValue() + "|" + next.getCodeElement().getValue());
			}
			assertThat(secListValues, containsInAnyOrder("tag_scheme1|tag_term1", "tag_scheme2|tag_term2"));
			List<Coding> secList = p1.getMeta().getSecurity();
			secListValues = new HashSet<>();
			for (Coding next : secList) {
				secListValues.add(next.getSystemElement().getValue() + "|" + next.getCodeElement().getValue());
			}
			assertThat(secListValues, containsInAnyOrder("sec_scheme1|sec_term1", "sec_scheme2|sec_term2"));
			List<UriType> profileList = p1.getMeta().getProfile();
			assertEquals(1, profileList.size());
			assertEquals("http://foo2", profileList.get(0).getValueAsString()); // no foo1
		}
	}

	@Test
	public void testUpdateModifiesProfiles() {
		String name = "testUpdateModifiesProfiles";
		IIdType id;
		{
			Patient patient = new Patient();
			patient.addName().setFamily(name);

			List<IdType> tl = new ArrayList<IdType>();
			tl.add(new IdType("http://foo/bar"));
			patient.getMeta().getProfile().addAll(tl);

			id = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		// Do a read
		{
			Patient patient = myPatientDao.read(id, mySrd);
			List<UriType> tl = patient.getMeta().getProfile();
			assertEquals(1, tl.size());
			assertEquals("http://foo/bar", tl.get(0).getValue());
		}

		// Update
		{
			Patient patient = new Patient();
			patient.setId(id);
			patient.addName().setFamily(name);

			List<IdType> tl = new ArrayList<IdType>();
			tl.add(new IdType("http://foo/baz"));
			patient.getMeta().getProfile().clear();
			patient.getMeta().getProfile().addAll(tl);

			id = myPatientDao.update(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		// Do a read
		{
			Patient patient = myPatientDao.read(id, mySrd);
			List<UriType> tl = patient.getMeta().getProfile();
			assertEquals(1, tl.size());
			assertEquals("http://foo/baz", tl.get(0).getValue());
		}

	}

	@Test
	public void testUpdateRejectsInvalidTypes() {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testUpdateRejectsInvalidTypes");
		p1.addName().setFamily("Tester").addGiven("testUpdateRejectsInvalidTypes");
		IIdType p1id = myPatientDao.create(p1, mySrd).getId();

		Organization p2 = new Organization();
		p2.getNameElement().setValue("testUpdateRejectsInvalidTypes");
		try {
			p2.setId(new IdType("Organization/" + p1id.getIdPart()));
			myOrganizationDao.update(p2, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			// good
		}

		try {
			p2.setId(new IdType("Patient/" + p1id.getIdPart()));
			myOrganizationDao.update(p2, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			ourLog.error("Good", e);
		}

	}

	@Test
	public void testUpdateUnknownNumericIdFails() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testCreateNumericIdFails");
		p.addName().setFamily("Hello");
		p.setId("Patient/9999999999999999");
		try {
			myPatientDao.update(p, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Can not create resource with ID[9999999999999999], no resource with this ID exists and clients may only"));
		}
	}

	@Test
	public void testUpdateWithInvalidIdFails() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testCreateNumericIdFails");
		p.addName().setFamily("Hello");
		p.setId("Patient/123:456");
		try {
			myPatientDao.update(p, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Can not process entity with ID[123:456], this is not a valid FHIR ID", e.getMessage());
		}
	}

	@Test
	public void testUpdateWithNoChangeDetectionDisabledUpdateUnchanged() {
		myDaoConfig.setSuppressUpdatesWithNoChange(false);

		String name = "testUpdateUnchanged";
		IIdType id1, id2;
		{
			Patient patient = new Patient();
			patient.addName().setFamily(name);
			id1 = myPatientDao.create(patient, mySrd).getId().toUnqualified();
		}

		// Update
		{
			Patient patient = new Patient();
			patient.setId(id1);
			patient.addName().setFamily(name);
			id2 = myPatientDao.update(patient, mySrd).getId().toUnqualified();
		}

		assertNotEquals(id1.getValue(), id2.getValue());
	}

	@Test
	public void testUpdateWithNoChangeDetectionUpdateTagAdded() {
		String name = "testUpdateUnchanged";
		IIdType id1, id2;
		{
			Patient patient = new Patient();
			patient.addName().setFamily(name);
			id1 = myPatientDao.create(patient, mySrd).getId().toUnqualified();
		}

		// Update
		{
			Patient patient = new Patient();
			patient.getMeta().addTag().setCode("CODE");
			patient.setId(id1);
			patient.addName().setFamily(name);
			id2 = myPatientDao.update(patient, mySrd).getId().toUnqualified();
		}

		assertNotEquals(id1.getValue(), id2.getValue());
	}

	@Test
	public void testUpdateWithNoChangeDetectionUpdateTagMetaRemoved() {
		String name = "testUpdateUnchanged";
		IIdType id1, id2;
		{
			Patient patient = new Patient();
			patient.getMeta().addTag().setCode("CODE");
			patient.addName().setFamily(name);
			id1 = myPatientDao.create(patient, mySrd).getId().toUnqualified();
		}

		Meta meta = new Meta();
		meta.addTag().setCode("CODE");
		myPatientDao.metaDeleteOperation(id1, meta, null);

		meta = myPatientDao.metaGetOperation(Meta.class, id1, null);
		assertEquals(0, meta.getTag().size());

		// Update
		{
			Patient patient = new Patient();
			patient.setId(id1);
			patient.addName().setFamily(name);
			id2 = myPatientDao.update(patient, mySrd).getId().toUnqualified();
		}

		assertEquals(id1.getValue(), id2.getValue());

		meta = myPatientDao.metaGetOperation(Meta.class, id2, null);
		assertEquals(0, meta.getTag().size());
	}

	@Test
	public void testUpdateWithNoChangeDetectionUpdateTagNoChange() {
		String name = "testUpdateUnchanged";
		IIdType id1, id2;
		{
			Patient patient = new Patient();
			patient.addName().setFamily(name);
			id1 = myPatientDao.create(patient, mySrd).getId().toUnqualified();
		}

		// Add tag
		Meta meta = new Meta();
		meta.addTag().setCode("CODE");
		myPatientDao.metaAddOperation(id1, meta, null);

		// Update with tag
		{
			Patient patient = new Patient();
			patient.getMeta().addTag().setCode("CODE");
			patient.setId(id1);
			patient.addName().setFamily(name);
			id2 = myPatientDao.update(patient, mySrd).getId().toUnqualified();
		}

		assertEquals(id1.getValue(), id2.getValue());

		meta = myPatientDao.metaGetOperation(Meta.class, id2, null);
		assertEquals(1, meta.getTag().size());
		assertEquals("CODE", meta.getTag().get(0).getCode());
	}

	@Test
	public void testUpdateWithNoChangeDetectionUpdateTagRemoved() {
		String name = "testUpdateUnchanged";
		IIdType id1, id2;
		{
			Patient patient = new Patient();
			patient.getMeta().addTag().setCode("CODE");
			patient.addName().setFamily(name);
			id1 = myPatientDao.create(patient, mySrd).getId().toUnqualified();
		}

		// Update
		{
			Patient patient = new Patient();
			patient.setId(id1);
			patient.addName().setFamily(name);
			id2 = myPatientDao.update(patient, mySrd).getId().toUnqualified();
		}

		assertEquals(id1.getValue(), id2.getValue());

		Meta meta = myPatientDao.metaGetOperation(Meta.class, id2, null);
		assertEquals(1, meta.getTag().size());
		assertEquals("CODE", meta.getTag().get(0).getCode());
	}

	@Test
	public void testUpdateWithNoChangeDetectionUpdateUnchanged() {
		String name = "testUpdateUnchanged";
		IIdType id1, id2;
		{
			Patient patient = new Patient();
			patient.addName().setFamily(name);
			id1 = myPatientDao.create(patient, mySrd).getId().toUnqualified();
		}

		// Update
		{
			Patient patient = new Patient();
			patient.setId(id1);
			patient.addName().setFamily(name);
			id2 = myPatientDao.update(patient, mySrd).getId().toUnqualified();
		}

		assertEquals(id1.getValue(), id2.getValue());
	}

	@Test
	public void testUpdateWithNumericIdFails() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testCreateNumericIdFails");
		p.addName().setFamily("Hello");
		p.setId("Patient/123");
		try {
			myPatientDao.update(p, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("clients may only assign IDs which contain at least one non-numeric"));
		}
	}

	@Test
	public void testUpdateWithNumericThenTextIdSucceeds() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testCreateNumericIdFails");
		p.addName().setFamily("Hello");
		p.setId("Patient/123abc");
		IIdType id = myPatientDao.update(p, mySrd).getId();
		assertEquals("123abc", id.getIdPart());
		assertEquals("1", id.getVersionIdPart());

		p = myPatientDao.read(id.toUnqualifiedVersionless(), mySrd);
		assertEquals("Patient/123abc", p.getIdElement().toUnqualifiedVersionless().getValue());
		assertEquals("Hello", p.getName().get(0).getFamily());

	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
